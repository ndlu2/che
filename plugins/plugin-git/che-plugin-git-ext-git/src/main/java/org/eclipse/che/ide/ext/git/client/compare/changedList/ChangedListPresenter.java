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
package org.eclipse.che.ide.ext.git.client.compare.changedList;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.project.node.Node;
import org.eclipse.che.ide.ext.git.client.GitLocalizationConstant;
import org.eclipse.che.ide.ext.git.client.compare.ComparePresenter;
import org.eclipse.che.ide.ext.git.client.compare.FileStatus.Status;

import javax.validation.constraints.NotNull;

import java.util.Map;

/**
 * Presenter for displaying list of changed files.
 *
 * @author Igor Vinokur
 */
@Singleton
public class ChangedListPresenter implements ChangedListView.ActionDelegate {
    private final ChangedListView         view;
    private final GitLocalizationConstant locale;
    private final ComparePresenter        comparePresenter;

    private Map<String, Status> changedFiles;
    private String              file;
    private String              revision;
    private Status              status;
    private boolean             treeViewEnabled;
    private boolean             treeExpanded;

    @Inject
    public ChangedListPresenter(ChangedListView view,
                                ComparePresenter comparePresenter,
                                GitLocalizationConstant locale) {
        this.comparePresenter = comparePresenter;
        this.view = view;
        this.locale = locale;
        this.view.setDelegate(this);
    }

    /**
     * Show window with changed files list.
     *
     * @param changedFiles
     *         Map with files and their status
     * @param revision
     *         hash of revision or branch
     */
    public void show(Map<String, Status> changedFiles, String revision) {
        treeExpanded = false;
        this.changedFiles = changedFiles;
        view.showDialog();
        viewChangedFiles();
        view.setEnableExpandCollapseButton(treeViewEnabled);
        view.setEnableCompareButton(false);
        this.revision = revision;
    }

    /** {@inheritDoc} */
    @Override
    public void onCloseClicked() {
        view.close();
    }

    /** {@inheritDoc} */
    @Override
    public void onCompareClicked() {
        showCompare();
    }

    @Override
    public void onFileNodeDoubleClicked() {
        showCompare();
    }

    /** {@inheritDoc} */
    @Override
    public void onGroupByDirectoryListViewButtonClicked() {
        treeViewEnabled = !treeViewEnabled;
        viewChangedFiles();
        view.setEnableExpandCollapseButton(treeViewEnabled);
    }

    @Override
    public void onExpandCollapseClicked() {
        treeExpanded = !treeExpanded;
        if (treeExpanded) {
            view.expandAllNodes();
            view.setCollapseIconToExpandCollapseButton();
        } else {
            view.collapseAllNodes();
            view.setExpandIconToExpandCollapseButton();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onNodeNodeSelected(@NotNull Node node) {
        if (node instanceof ChangedFolderNode) {
            return;
        }
        view.setEnableCompareButton(true);
        this.file = node.getName();
        this.status = ((ChangedFileNode)node).getStatus();
    }

    /** {@inheritDoc} */
    @Override
    public void onNodeNotSelected() {
        view.setEnableCompareButton(false);
    }

    private void viewChangedFiles() {
        if (treeViewEnabled) {
            view.viewChangedFilesAsTree(changedFiles);
            view.setExpandIconToExpandCollapseButton();
            view.setTextToChangeViewModeButton(locale.changeListRowListViewButtonText());
        } else {
            view.viewChangedFilesAsList(changedFiles);
            view.setTextToChangeViewModeButton(locale.changeListGroupByDirectoryButtonText());
        }
    }

    private void showCompare() {
        comparePresenter.show(file, status, revision);
    }
}
