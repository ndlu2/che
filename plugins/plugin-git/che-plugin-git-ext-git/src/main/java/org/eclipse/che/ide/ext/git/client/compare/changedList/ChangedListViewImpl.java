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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.project.node.HasStorablePath;
import org.eclipse.che.ide.api.project.node.Node;
import org.eclipse.che.ide.api.project.node.interceptor.NodeInterceptor;
import org.eclipse.che.ide.ext.git.client.GitLocalizationConstant;
import org.eclipse.che.ide.ext.git.client.GitResources;
import org.eclipse.che.ide.project.shared.NodesResources;
import org.eclipse.che.ide.ui.smartTree.NodeUniqueKeyProvider;
import org.eclipse.che.ide.ui.smartTree.Tree;
import org.eclipse.che.ide.ui.smartTree.NodeLoader;
import org.eclipse.che.ide.ui.smartTree.NodeStorage;
import org.eclipse.che.ide.ui.smartTree.SelectionModel;
import org.eclipse.che.ide.ui.smartTree.compare.NameComparator;
import org.eclipse.che.ide.ui.smartTree.event.SelectionChangedEvent;
import org.eclipse.che.ide.ui.window.Window;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link ChangedListView}.
 *
 * @author Igor Vinokur
 */
@Singleton
public class ChangedListViewImpl extends Window implements ChangedListView {
    interface ChangedListViewImplUiBinder extends UiBinder<Widget, ChangedListViewImpl> {
    }

    private static ChangedListViewImplUiBinder ourUiBinder = GWT.create(ChangedListViewImplUiBinder.class);

    @UiField
    LayoutPanel changedFilesPanel;
    @UiField
    CheckBox    groupByDirectory;

    @UiField(provided = true)
    final GitLocalizationConstant locale;
    @UiField(provided = true)
    final GitResources            res;

    private ActionDelegate      delegate;
    private Tree                tree;
    private Button              btnCompare;

    private final NodesResources nodesResources;

    @Inject
    protected ChangedListViewImpl(GitResources resources,
                                  GitLocalizationConstant locale,
                                  NodesResources nodesResources) {
        this.res = resources;
        this.locale = locale;
        this.nodesResources = nodesResources;

        Widget widget = ourUiBinder.createAndBindUi(this);

        this.setTitle(locale.changeListTitle());
        this.setWidget(widget);

        NodeStorage nodeStorage = new NodeStorage(new NodeUniqueKeyProvider() {
            @NotNull
            @Override
            public String getKey(@NotNull Node item) {
                if (item instanceof HasStorablePath) {
                    return ((HasStorablePath)item).getStorablePath();
                } else {
                    return String.valueOf(item.hashCode());
                }
            }
        });
        NodeLoader nodeLoader = new NodeLoader(Collections.<NodeInterceptor>emptySet());
        tree = new Tree(nodeStorage, nodeLoader);
        tree.getSelectionModel().setSelectionMode(SelectionModel.Mode.SINGLE);
        tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent event) {
                List<Node> selection = event.getSelection();
                if (!selection.isEmpty() && !(selection.get(0) instanceof ChangedFolderNode)) {
                    delegate.onFileNodeSelected(event.getSelection().get(0));
                } else {
                    delegate.onFileNodeUnselected();
                }
            }
        });
        changedFilesPanel.add(tree);

        createButtons();

        groupByDirectory.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                delegate.onGroupByDirectoryCheckBoxValueChanged();
            }
        });

        SafeHtmlBuilder shb = new SafeHtmlBuilder();

        shb.appendHtmlConstant("<table height =\"20\">");
        shb.appendHtmlConstant("<tr height =\"3\"></tr><tr>");
        shb.appendHtmlConstant("<td width =\"20\" bgcolor =\"dodgerBlue\"></td>");
        shb.appendHtmlConstant("<td>modified</td>");
        shb.appendHtmlConstant("<td width =\"20\" bgcolor =\"red\"></td>");
        shb.appendHtmlConstant("<td>deleted</td>");
        shb.appendHtmlConstant("<td width =\"20\" bgcolor =\"green\"></td>");
        shb.appendHtmlConstant("<td>added</td>");
        shb.appendHtmlConstant("<td width =\"20\" bgcolor =\"purple\"></td>");
        shb.appendHtmlConstant("<td>has conflicts</td>");
        shb.appendHtmlConstant("</tr></table>");

        getFooter().add(new HTML(shb.toSafeHtml()));
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public void setChanges(@NotNull Map<String, String> items) {
        tree.getNodeStorage().clear();
        if (groupByDirectory.getValue()) {
            for (Node node : getGroupedNodes(items)) {
                tree.getNodeStorage().add(node);
            }
        } else {
            for (String file : items.keySet()) {
                tree.getNodeStorage().add(new ChangedFileNode(file, items.get(file), nodesResources, false) {
                    @Override
                    public void actionPerformed() {
                        delegate.onCompareProvided();
                    }
                });

            }
        }
        if (this.tree.getSelectionModel().getSelectedNodes() == null) {
            delegate.onFileNodeUnselected();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        this.hide();
    }

    /** {@inheritDoc} */
    @Override
    public void showDialog() {
        this.show();
    }

    /** {@inheritDoc} */
    @Override
    public void setEnableCompareButton(boolean enabled) {
        btnCompare.setEnabled(enabled);
    }

    private void createButtons() {
        Button btnClose = createButton(locale.buttonClose(), "git-compare-btn-close", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                delegate.onCloseClicked();
            }
        });
        addButtonToFooter(btnClose);

        btnCompare = createButton(locale.buttonCompare(), "git-compare-btn-compare", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                delegate.onCompareProvided();
            }
        });
        addButtonToFooter(btnCompare);
    }

    private List<Node> getGroupedNodes(Map<String, String> items) {
        List<String> allFiles = new ArrayList<>(items.keySet());
        List<String> allPaths = new ArrayList<>();
        for (String file : allFiles) {
            String path = file.substring(0, file.lastIndexOf("/"));
            if (!allPaths.contains(path)) {
                allPaths.add(path);
            }
        }
        String commonPath = getCommonPath(allPaths);
        Map<String, Node> preparedNodes = new HashMap<>();
        for (int i = getMaxNestedLevel(allFiles); i > 0; i--) {
            Map<String, List<Node>> currentChildNodes = new HashMap<>();
            for (String file : allFiles) {
                if (file.split("/").length != i) {
                    continue;
                }
                String path = file.substring(0, file.lastIndexOf("/"));
                String name = file.substring(file.lastIndexOf("/") + 1);
                String pathName = path.isEmpty() ? name : path + "/" + name;
                Node fileNode = new ChangedFileNode(pathName, items.get(pathName), nodesResources, true) {
                    @Override
                    public void actionPerformed() {
                        delegate.onCompareProvided();
                    }
                };
                if (currentChildNodes.keySet().contains(path)) {
                    currentChildNodes.get(path).add(fileNode);
                } else {
                    List<Node> listFiles = new ArrayList<>();
                    listFiles.add(fileNode);
                    currentChildNodes.put(path, listFiles);
                }
            }
            for (String path : currentChildNodes.keySet()) {
                Node folder = new ChangedFolderNode(getFolders(allPaths, path), nodesResources);
                folder.setChildren(currentChildNodes.get(path));
                preparedNodes.put(path, folder);
            }
            List<String> currentPaths = new ArrayList<>(preparedNodes.keySet());
            for (String currentPath : currentPaths) {
                List<Node> nodesToNest = new ArrayList<>();
                for (String nestedItem : currentPaths) {
                    if (!currentPath.equals(nestedItem) && nestedItem.startsWith(currentPath)) {
                        nodesToNest.add(preparedNodes.remove(nestedItem));
                    }
                }
                if (nodesToNest.isEmpty()) {
                    continue;
                }
                Collections.sort(nodesToNest, new NameComparator());
                nodesToNest.addAll(currentChildNodes.get(currentPath));
                if (currentPath.equals(commonPath)) {
                    return nodesToNest;
                } else {
                    preparedNodes.get(currentPath).setChildren(nodesToNest);
                }
            }
        }
        return new ArrayList<>(preparedNodes.values());
    }

    private String getFolders(List<String> allPaths, String comparedPath) {
        String[] segments = comparedPath.split("/");
        String trimmedPath = comparedPath;
        for (int i = segments.length; i > 0; i--) {
            trimmedPath = trimmedPath.replace("/" + segments[i - 1], "");
            if (allPaths.contains(trimmedPath)) {
                return comparedPath.replace(trimmedPath + "/", "");
            }
        }
        return comparedPath;
    }

    private int getMaxNestedLevel(List<String> items) {
        int level = 0;
        for (String item : items) {
            int currentLevel = item.split("/").length;
            level = currentLevel > level ? currentLevel : level;
        }
        return level;
    }

    private String getCommonPath(List<String> paths) {
        String commonPath = "";
        for (String segment : paths.get(0).split("/")) {
            commonPath += commonPath.isEmpty() ? segment : "/" + segment;
            for (String path : paths) {
                if (!path.startsWith(commonPath)) {
                    return commonPath.substring(0, commonPath.lastIndexOf("/"));
                }
            }
        }
        return commonPath;
    }
}
