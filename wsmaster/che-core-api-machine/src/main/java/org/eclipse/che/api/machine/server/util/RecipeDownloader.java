/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.machine.server.util;

import org.eclipse.che.api.core.model.machine.MachineConfig;
import org.eclipse.che.api.core.model.machine.MachineSource;
import org.eclipse.che.api.machine.server.exception.MachineException;
import org.eclipse.che.api.machine.server.recipe.RecipeImpl;
import org.eclipse.che.commons.env.EnvironmentContext;
import org.eclipse.che.commons.lang.IoUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Downloads machine recipe set in machine source.
 *
 * <p>Adds user token if target url points to current CHE server.
 *
 * @author Alexander Garagatyi
 */
public class RecipeDownloader {
    private static final Logger LOG = getLogger(RecipeDownloader.class);

    private final String apiEndpoint;

    @Inject
    public RecipeDownloader(@Named("api.endpoint") String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    /**
     * Downloads recipe by location from {@link MachineSource#getLocation()}.
     *
     * @param machineConfig
     *         config used to get recipe location
     * @return recipe with set content and type
     * @throws MachineException
     *         if any error occurs
     */
    public RecipeImpl getRecipe(MachineConfig machineConfig) throws MachineException {
        URL recipeUrl;
        File file = null;
        try {
            UriBuilder targetUriBuilder = UriBuilder.fromUri(machineConfig.getSource().getLocation());
            // add user token to be able to download user's private recipe
            if (machineConfig.getSource().getLocation().startsWith(apiEndpoint)) {
                if (EnvironmentContext.getCurrent().getUser() != null
                    && EnvironmentContext.getCurrent().getUser().getToken() != null) {
                    targetUriBuilder.queryParam("token", EnvironmentContext.getCurrent().getUser().getToken());
                }
            }
            recipeUrl = targetUriBuilder.build().toURL();
            file = IoUtil.downloadFile(null, "recipe", null, recipeUrl);

            return new RecipeImpl().withType(machineConfig.getSource().getType())
                                   .withScript(IoUtil.readAndCloseQuietly(new FileInputStream(file)));
        } catch (IOException | IllegalArgumentException e) {
            throw new MachineException(format("Can't start machine %s because machine recipe downloading failed. Recipe url %s. Error: %s",
                                              machineConfig.getName(),
                                              machineConfig.getSource().getLocation(),
                                              e.getLocalizedMessage()));
        } finally {
            if (file != null && file.delete()) {
                LOG.error(String.format("Removal of recipe file %s failed.", file.getAbsolutePath()));
            }
        }
    }
}
