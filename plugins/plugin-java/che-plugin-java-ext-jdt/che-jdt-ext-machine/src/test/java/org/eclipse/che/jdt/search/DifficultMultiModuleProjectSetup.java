/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.jdt.search;

import org.eclipse.che.jdt.testplugin.JavaProjectHelper;
import org.eclipse.che.jdt.testplugin.TestOptions;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.search.SearchParticipantRecord;
import org.eclipse.jdt.internal.ui.search.SearchParticipantsExtensionPoint;

/**
 * Initialization MultiModule project for tests
 *
 * @author Alexander Andrienko
 */
public class DifficultMultiModuleProjectSetup {
    public static final String PROJECT_NAME  = "/MultimodulePrj";
    public static final String ROOT_SRC_CONTAINER = "/src/main/java";

    private IJavaProject                     fProject;
    private SearchParticipantsExtensionPoint fExtensionPoint;

    static class NullExtensionPoint extends SearchParticipantsExtensionPoint {
        public SearchParticipantRecord[] getSearchParticipants(IProject[] concernedProjects) {
            return new SearchParticipantRecord[0];
        }
    }

    public static IJavaProject getProject() {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
        return JavaCore.create(project);
    }

    public DifficultMultiModuleProjectSetup(SearchParticipantsExtensionPoint participants) {
        fExtensionPoint = participants;
    }

    public DifficultMultiModuleProjectSetup(/*Test test*/) {
        this(/*test,*/ new NullExtensionPoint());
    }

    protected void setUp() throws Exception {
        SearchParticipantsExtensionPoint.debugSetInstance(fExtensionPoint);
        fProject = JavaProjectHelper.createJavaProject(PROJECT_NAME, "bin");
        JavaProjectHelper.addRTJar(fProject);
        JavaCore.setOptions(TestOptions.getDefaultOptions());
        TestOptions.initializeCodeGenerationOptions();
        JavaPlugin.getDefault().getCodeTemplateStore().load();
    }

    /* (non-Javadoc)
     * @see junit.extensions.TestSetup#tearDown()
     */
    protected void tearDown() throws Exception {
        JavaProjectHelper.delete(fProject);
        SearchParticipantsExtensionPoint.debugSetInstance(null);
    }
}
