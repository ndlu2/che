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

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.js.Promises;
import org.eclipse.che.ide.api.project.node.AbstractTreeNode;
import org.eclipse.che.ide.api.project.node.HasAction;
import org.eclipse.che.ide.api.project.node.Node;
import org.eclipse.che.ide.ext.git.client.compare.FileStatus.Status;
import org.eclipse.che.ide.project.shared.NodesResources;
import org.eclipse.che.ide.ui.smartTree.presentation.HasPresentation;
import org.eclipse.che.ide.ui.smartTree.presentation.NodePresentation;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * Node Element used for setting it to TreeNodeStorage and viewing changed files.
 *
 * @author Igor Vinokur
 */
public class ChangedFileNode extends AbstractTreeNode implements HasPresentation, HasAction {

    private String           pathName;
    private Status           status;
    private NodePresentation nodePresentation;

    private final NodesResources          nodesResources;
    private final boolean                 viewPath;
    private final ActionPerformedCallBack actionPerformedCallBack;

    /**
     * CallBack for describing actions that will be performed on node double clicked.
     */
    public interface ActionPerformedCallBack {
        /**
         * Actions that will be performed on node double clicked.
         */
        void actionPerformed();
    }

    /**
     * Create instance of ChangedFileNode.
     *
     * @param pathName
     *         name of the file that represents this node with its full path
     * @param status
     *         git status of the file that represents this node
     * @param nodesResources
     *         resources that contain icons
     * @param viewPath
     *         <code>true</code> if it is needed to view file name with its full path,
     *         and <code>false</code> if it is needed to view only name of the file
     */
    public ChangedFileNode(String pathName,
                           Status status,
                           NodesResources nodesResources,
                           boolean viewPath,
                           ActionPerformedCallBack actionPerformedCallBack) {
        this.pathName = pathName;
        this.status = status;
        this.nodesResources = nodesResources;
        this.viewPath = viewPath;
        this.actionPerformedCallBack = actionPerformedCallBack;
    }

    @Override
    protected Promise<List<Node>> getChildrenImpl() {
        return Promises.resolve(Collections.<Node>emptyList());
    }

    @Override
    public String getName() {
        return pathName;
    }

    /**
     * Git status of the file.
     *
     * @return Git status of the file
     */
    public Status getStatus() {
        return status;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public void updatePresentation(@NotNull NodePresentation presentation) {
        String path = pathName.substring(0, pathName.lastIndexOf("/"));
        String name = pathName.substring(pathName.lastIndexOf("/") + 1);
        presentation.setPresentableText(viewPath ? name : path.isEmpty() ? name : path + "/" + name);
        presentation.setPresentableIcon(nodesResources.file());

        switch (status) {
            case Modified:
                presentation.setPresentableTextCss("color: DodgerBlue ;");
                return;
            case Deleted:
                presentation.setPresentableTextCss("color: red;");
                return;
            case Added:
                presentation.setPresentableTextCss("color: green;");
                return;
            case Copied:
                presentation.setPresentableTextCss("color: purple;");
        }
    }

    @Override
    public NodePresentation getPresentation(boolean update) {
        if (nodePresentation == null) {
            nodePresentation = new NodePresentation();
            updatePresentation(nodePresentation);
        }

        if (update) {
            updatePresentation(nodePresentation);
        }
        return nodePresentation;
    }

    @Override
    public void actionPerformed() {
        actionPerformedCallBack.actionPerformed();
    }
}
