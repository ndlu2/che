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
    private String           state;
    private NodePresentation nodePresentation;

    private final NodesResources nodesResources;
    private final boolean        viewPath;

    /**
     * Create instance of ChangedFileNode.
     *
     * @param pathName
     *         name of the file that represents this node with its full path
     * @param state
     *         state of the file that represents this node
     * @param nodesResources
     *         resources that contain icons
     * @param viewPath
     *         <code>true</code> if it is needed to view file name with its full path,
     *         and <code>false</code> if it is needed to view only name of the file
     */
    public ChangedFileNode(String pathName, String state, NodesResources nodesResources, boolean viewPath) {
        this.pathName = pathName;
        this.state = state;
        this.nodesResources = nodesResources;
        this.viewPath = viewPath;
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
     * Represents state of the file, 'A' if added 'D' if deleted 'M' if modified, etc.
     *
     * @return state state of the node
     */
    public String getState() {
        return state;
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

        if (state.startsWith("M")) {
            presentation.setPresentableTextCss("color: DodgerBlue ;");
        } else if (state.startsWith("D")) {
            presentation.setPresentableTextCss("color: red;");
        } else if (state.startsWith("A")) {
            presentation.setPresentableTextCss("color: green;");
        } else if (state.startsWith("C")) {
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

    }
}
