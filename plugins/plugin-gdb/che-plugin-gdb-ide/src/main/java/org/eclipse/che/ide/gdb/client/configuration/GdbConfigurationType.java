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
package org.eclipse.che.ide.gdb.client.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.debug.DebugConfiguration;
import org.eclipse.che.ide.api.debug.DebugConfigurationPage;
import org.eclipse.che.ide.api.debug.DebugConfigurationType;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.icon.IconRegistry;
import org.eclipse.che.ide.gdb.client.GdbDebuggerClient;
import org.eclipse.che.ide.gdb.client.GdbResources;

import static org.eclipse.che.ide.gdb.client.GdbDebuggerClient.ID;

/**
 * GDB debug configuration type.
 *
 * @author Artem Zatsarynnyi
 */
@Singleton
public class GdbConfigurationType implements DebugConfigurationType {

    public static final String DISPLAY_NAME = "GDB";

    private final GdbConfigurationPagePresenter page;

    @Inject
    public GdbConfigurationType(GdbConfigurationPagePresenter page,
                                IconRegistry iconRegistry,
                                GdbResources resources) {
        this.page = page;
        iconRegistry.registerIcon(new Icon(ID + ".debug.configuration.type.icon", resources.gdbDebugConfigurationType()));
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public DebugConfigurationPage<? extends DebugConfiguration> getConfigurationPage() {
        return page;
    }
}
