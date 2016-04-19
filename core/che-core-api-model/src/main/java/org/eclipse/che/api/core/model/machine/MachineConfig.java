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
package org.eclipse.che.api.core.model.machine;

import com.google.common.annotations.Beta;

import org.eclipse.che.commons.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author gazarenkov
 */
public interface MachineConfig {

    /**
     * Display name.
     */
    String getName();

    /**
     * From where to create this Machine (Recipe/Snapshot).
     */
    MachineSource getSource();

    /**
     * Is workspace bound to machine or not.
     */
    boolean isDev();

    /**
     * Machine type (i.e. "docker").
     */
    String getType();

    /**
     * Machine limits such as RAM size.
     */
    @Nullable
    Limits getLimits();

    /**
     * Get configuration of servers inside of machine.
     *
     * <p>Key is port/transport protocol, e.g. 8080/tcp or 100100/udp
     */
    List<? extends ServerConf> getServers();

    /**
     * Get predefined environment variables of machine.
     */
    Map<String, String> getEnvVariables();

    /**
     * Architecture of target machine. Default is 'linux_amd64'.
     *
     * <p/>This method is in beta. And can be removed in next versions.
     */
    // todo remove after resolving CHE-995
    @Beta
    String getArchitecture();
}
