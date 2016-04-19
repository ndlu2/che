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
package org.eclipse.che.ide.ext.git.client.importer.page;

import org.eclipse.che.api.workspace.shared.dto.ProjectConfigDto;
import org.eclipse.che.api.workspace.shared.dto.SourceStorageDto;
import org.eclipse.che.ide.api.wizard.Wizard;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import org.eclipse.che.ide.ext.git.client.GitLocalizationConstant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing {@link GitImporterPagePresenter} functionality.
 *
 * @author Roman Nikitenko
 */
@RunWith(MockitoJUnitRunner.class)
public class GitImporterPagePresenterTest {
    @Mock
    private Wizard.UpdateDelegate    updateDelegate;
    @Mock
    private GitImporterPageView      view;
    @Mock
    private GitLocalizationConstant  locale;
    @Mock
    private ProjectConfigDto         dataObject;
    @Mock
    private SourceStorageDto         source;
    @Mock
    private Map<String, String>      parameters;
    @InjectMocks
    private GitImporterPagePresenter presenter;

    @Before
    public void setUp() {
        when(dataObject.getSource()).thenReturn(source);

        presenter.setUpdateDelegate(updateDelegate);
        presenter.init(dataObject);
    }

    @Test
    public void testGo() {
        AcceptsOneWidget container = mock(AcceptsOneWidget.class);

        presenter.go(container);

        verify(container).setWidget(eq(view));
        verify(view).setProjectName(anyString());
        verify(view).setProjectDescription(anyString());
        verify(view).setProjectUrl(anyString());
        verify(view).setInputsEnableState(eq(true));
        verify(view).focusInUrlInput();
    }

    @Test
    public void projectUrlStartWithWhiteSpaceEnteredTest() {
        String incorrectUrl = " https://github.com/codenvy/ide.git";
        String name = "ide";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(incorrectUrl);

        verify(view).markURLInvalid();
        verify(view).setURLErrorMessage(eq(locale.importProjectMessageStartWithWhiteSpace()));

        verify(dataObject).setName(eq(name));
        verify(view).setProjectName(name);
        verify(updateDelegate).updateControls();
    }

    @Test
    public void testUrlMatchScpLikeSyntax() {
        // test for url with an alternative scp-like syntax: [user@]host.xz:path/to/repo.git/
        String correctUrl = "host.xz:path/to/repo.git";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(correctUrl);

        verifyInvocationsForCorrectUrl(correctUrl);
    }

    @Test
    public void testUrlWithoutUsername() {
        String correctUrl = "git@hostname.com:projectName.git";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(correctUrl);

        verifyInvocationsForCorrectUrl(correctUrl);
    }

    @Test
    public void testSshUriWithHostBetweenDoubleSlashAndSlash() {
        //Check for type uri which start with ssh:// and has host between // and /
        String correctUrl = "ssh://host.com/some/path";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(correctUrl);

        verifyInvocationsForCorrectUrl(correctUrl);
    }

    @Test
    public void testSshUriWithHostBetweenDoubleSlashAndColon() {
        //Check for type uri with host between // and :
        String correctUrl = "ssh://host.com:port/some/path";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(correctUrl);

        verifyInvocationsForCorrectUrl(correctUrl);
    }

    @Test
    public void testGitUriWithHostBetweenDoubleSlashAndSlash() {
        //Check for type uri which start with git:// and has host between // and /
        String correctUrl = "git://host.com/user/repo";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(correctUrl);

        verifyInvocationsForCorrectUrl(correctUrl);
    }

    @Test
    public void testSshUriWithHostBetweenAtAndColon() {
        //Check for type uri with host between @ and :
        String correctUrl = "user@host.com:login/repo";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(correctUrl);

        verifyInvocationsForCorrectUrl(correctUrl);
    }

    @Test
    public void testSshUriWithHostBetweenAtAndSlash() {
        //Check for type uri with host between @ and /
        String correctUrl = "ssh://user@host.com/some/path";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(correctUrl);

        verifyInvocationsForCorrectUrl(correctUrl);
    }

    @Test
    public void projectUrlWithIncorrectProtocolEnteredTest() {
        String incorrectUrl = "htps://github.com/codenvy/ide.git";
        when(view.getProjectName()).thenReturn("");

        presenter.projectUrlChanged(incorrectUrl);

        verify(view).markURLInvalid();
        verify(view).setURLErrorMessage(eq(locale.importProjectMessageProtocolIncorrect()));

        verify(source).setLocation(eq(incorrectUrl));
        verify(view).setProjectName(anyString());
        verify(updateDelegate).updateControls();
    }

    @Test
    public void correctProjectNameEnteredTest() {
        String correctName = "angularjs";
        when(view.getProjectName()).thenReturn(correctName);

        presenter.projectNameChanged(correctName);

        verify(dataObject).setName(eq(correctName));

        verify(view).markURLValid();
        verify(view).setURLErrorMessage(eq(null));
        verify(view, never()).markURLInvalid();
        verify(updateDelegate).updateControls();
    }

    @Test
    public void correctProjectNameWithPointEnteredTest() {
        String correctName = "Test.project..ForCodenvy";
        when(view.getProjectName()).thenReturn(correctName);

        presenter.projectNameChanged(correctName);

        verify(dataObject).setName(eq(correctName));
        verify(view).markNameValid();
        verify(view, never()).markNameInvalid();
        verify(updateDelegate).updateControls();
    }

    @Test
    public void emptyProjectNameEnteredTest() {
        String emptyName = "";
        when(view.getProjectName()).thenReturn(emptyName);

        presenter.projectNameChanged(emptyName);

        verify(dataObject).setName(eq(emptyName));
        verify(updateDelegate).updateControls();
    }

    @Test
    public void incorrectProjectNameEnteredTest() {
        String incorrectName = "angularjs+";
        when(view.getProjectName()).thenReturn(incorrectName);

        presenter.projectNameChanged(incorrectName);

        verify(dataObject).setName(eq(incorrectName));
        verify(view).markNameInvalid();
        verify(updateDelegate).updateControls();
    }

    @Test
    public void projectDescriptionChangedTest() {
        String description = "description";
        presenter.projectDescriptionChanged(description);

        verify(dataObject).setDescription(eq(description));
    }

    @Test
    public void keepDirectorySelectedTest() {
        Map<String, String> parameters = new HashMap<>();
        when(source.getParameters()).thenReturn(parameters);
        when(view.getDirectoryName()).thenReturn("directory");

        presenter.keepDirectorySelected(true);

        assertEquals("directory", parameters.get("keepDirectory"));
        verify(dataObject).withType("blank");
        verify(view).highlightDirectoryNameField(eq(false));
        verify(view).focusDirectoryNameFiend();
    }

    @Test
    public void keepDirectoryUnSelectedTest() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keepDir", "directory");
        when(source.getParameters()).thenReturn(parameters);

        presenter.keepDirectorySelected(false);

        assertTrue(parameters.isEmpty());
        verify(dataObject).withType(eq(null));
        verify(view).highlightDirectoryNameField(eq(false));
    }

    @Test
    public void keepDirectoryNameChangedAndKeepDirectorySelectedTest() {
        Map<String, String> parameters = new HashMap<>();
        when(source.getParameters()).thenReturn(parameters);
        when(view.getDirectoryName()).thenReturn("directory");
        when(view.keepDirectory()).thenReturn(true);

        presenter.keepDirectoryNameChanged("directory");

        assertEquals("directory", parameters.get("keepDirectory"));
        verify(dataObject, never()).setPath(any());
        verify(dataObject).withType(eq("blank"));
        verify(view).highlightDirectoryNameField(eq(false));
    }

    @Test
    public void keepDirectoryNameChangedAndKeepDirectoryUnSelectedTest() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keepDirectory", "directory");
        when(source.getParameters()).thenReturn(parameters);
        when(view.keepDirectory()).thenReturn(false);

        presenter.keepDirectoryNameChanged("directory");

        assertTrue(parameters.isEmpty());
        verify(dataObject, never()).setPath(any());
        verify(dataObject).withType(eq(null));
        verify(view).highlightDirectoryNameField(eq(false));
    }

    /**
     * Branch name field must become enabled when Branch is checked.
     */
    @Test
    public void branchSelectedTest() {
        presenter.branchSelected(true);
        verify(view).enableBranchNameField(true);
    }

    /**
     * Branch name field must become disabled when Branch is unchecked.
     */
    @Test
    public void branchNotSelectedTest() {
        presenter.branchSelected(false);
        verify(view).enableBranchNameField(false);
    }

    /**
     * Empty directory name field must be highlighted when Keep directory is checked.
     */
    @Test
    public void emptyDirectoryNameEnteredTest() {
        when(view.getDirectoryName()).thenReturn("");
        presenter.keepDirectorySelected(true);
        verify(view).highlightDirectoryNameField(true);
    }

    /**
     * Non empty directory name field must be without highlighting when Keep directory is checked.
     */
    @Test
    public void directoryNameEnteredTest() {
        when(view.getDirectoryName()).thenReturn("test");
        presenter.keepDirectorySelected(true);
        verify(view).highlightDirectoryNameField(false);
    }

    private void verifyInvocationsForCorrectUrl(String correctUrl) {
        verify(view, never()).markURLInvalid();
        verify(source).setLocation(eq(correctUrl));
        verify(view).markURLValid();
        verify(view).setProjectName(anyString());
        verify(updateDelegate).updateControls();
    }

}
