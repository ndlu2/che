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
package org.eclipse.che.plugin.svn.ide.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.plugin.svn.ide.SubversionExtensionLocalizationConstants;
import org.eclipse.che.plugin.svn.ide.SubversionExtensionResources;
import org.eclipse.che.plugin.svn.ide.cleanup.CleanupPresenter;
import org.eclipse.che.ide.part.explorer.project.ProjectExplorerPresenter;

/**
 * Extension of {@link SubversionAction} for implementing the "svn cleanup" command.
 */
@Singleton
public class CleanupAction extends SubversionAction {

    private final CleanupPresenter cleanupPresenter;

    @Inject
    public CleanupAction(final AppContext appContext,
                         final CleanupPresenter cleanupPresenter,
                         final ProjectExplorerPresenter projectExplorerPresenter,
                         final SubversionExtensionLocalizationConstants constants,
                         final SubversionExtensionResources resources) {
        super(constants.cleanupTitle(), constants.cleanupDescription(), resources.cleanup(),
              appContext, constants, resources, projectExplorerPresenter);
        this.cleanupPresenter = cleanupPresenter;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        this.cleanupPresenter.cleanup();
    }

}
