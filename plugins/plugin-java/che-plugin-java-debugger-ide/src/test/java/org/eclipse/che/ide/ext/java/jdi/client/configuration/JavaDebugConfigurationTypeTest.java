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
package org.eclipse.che.ide.ext.java.jdi.client.configuration;

import com.google.gwtmockito.GwtMockitoTestRunner;

import org.eclipse.che.ide.api.debug.DebugConfiguration;
import org.eclipse.che.ide.api.debug.DebugConfigurationPage;
import org.eclipse.che.ide.api.icon.IconRegistry;
import org.eclipse.che.ide.ext.java.jdi.client.JavaRuntimeResources;
import org.eclipse.che.ide.ext.java.jdi.client.debug.JavaDebugger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

/** @author Artem Zatsarynnyi */
@RunWith(GwtMockitoTestRunner.class)
public class JavaDebugConfigurationTypeTest {

    @Mock
    private JavaRuntimeResources                resources;
    @Mock
    private JavaDebugConfigurationPagePresenter javaDebugConfigurationPagePresenter;
    @Mock
    private IconRegistry                        iconRegistry;

    @InjectMocks
    private JavaDebugConfigurationType javaDebugConfigurationType;

    @Test
    public void testGetId() throws Exception {
        final String id = javaDebugConfigurationType.getId();

        assertEquals(JavaDebugger.ID, id);
    }

    @Test
    public void testGetDisplayName() throws Exception {
        final String displayName = javaDebugConfigurationType.getDisplayName();

        assertEquals(JavaDebugConfigurationType.DISPLAY_NAME, displayName);
    }

    @Test
    public void testGetConfigurationPage() throws Exception {
        final DebugConfigurationPage<? extends DebugConfiguration> page = javaDebugConfigurationType.getConfigurationPage();

        assertEquals(javaDebugConfigurationPagePresenter, page);
    }
}
