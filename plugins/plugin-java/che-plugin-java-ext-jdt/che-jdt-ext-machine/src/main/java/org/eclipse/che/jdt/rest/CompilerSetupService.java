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

package org.eclipse.che.jdt.rest;

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.vfs.VirtualFile;
import org.eclipse.che.api.vfs.VirtualFileSystemProvider;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.core.JavaModel;
import org.eclipse.jdt.internal.core.JavaModelManager;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Special service which allows control parameters of compiler for current project or current workspace.
 *
 * @author Dmitry Shnurenko
 */
@Path("/jdt/{wsId}/compiler-settings")
public class CompilerSetupService {
    @Inject
    VirtualFileSystemProvider vfsProvider;

    private static final JavaModel JAVA_MODEL = JavaModelManager.getJavaModelManager().getJavaModel();

    /**
     * Set java compiler preferences {@code changedParameters} for project by not empty path {@code projectpath}. If {@code projectpath}
     * is empty then java compiler preferences will be set for current workspace.
     *
     * @param projectPath project path
     * @param changedParameters java compiler preferences
     */
    @POST
    @Path("/set")
    @Consumes(APPLICATION_JSON)
    public void setParameters(@QueryParam("projectpath") String projectPath, @NotNull Map<String, String> changedParameters) throws ServerException, ConflictException, ForbiddenException {
        if (projectPath == null || projectPath.isEmpty()) {
            JavaCore.setOptions(new Hashtable<>(changedParameters));


            StringBuilder stringBuilder = new StringBuilder();
            for (String parameter : changedParameters.keySet()) {
                stringBuilder.
            }

            VirtualFile file = vfsProvider.getVirtualFileSystem().getRoot().getChild(org.eclipse.che.api.vfs.Path.of("file"));
            if (file == null) {
                vfsProvider.getVirtualFileSystem().getRoot().createFile("file", "hello");
            }


            return;
        }
        IJavaProject project = JAVA_MODEL.getJavaProject(projectPath);
        project.setOptions(changedParameters);
    }

    /**
     * Return java compiler preferences for current project by not empty path {@code projectpath}. If {@code projectpath} if empty then
     * return java compile preferences for current workspace.
     *
     * @param projectPath project path
     * @return java compiler preferences
     */
    @GET
    @Path("/all")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Map<String, String> getAllParameters(@QueryParam("projectpath") String projectPath)
            throws ServerException, ConflictException, ForbiddenException {
        if (projectPath == null || projectPath.isEmpty()) {


            VirtualFile file = vfsProvider.getVirtualFileSystem().getRoot().getChild(org.eclipse.che.api.vfs.Path.of("file"));



            //noinspection unchecked
            CompilerOptions options = new CompilerOptions(new HashMap<>(JavaCore.getOptions()));
            //noinspection unchecked
            return options.getMap();
        }

        IJavaProject project = JAVA_MODEL.getJavaProject(projectPath);

        //noinspection unchecked
        Map<String, String> map = project.getOptions(true);
        CompilerOptions options = new CompilerOptions(map);

//        VirtualFile file = vfsProvider.getVirtualFileSystem().getRoot().getChild(org.eclipse.che.api.vfs.Path.of(".codenvy"));
//        if (file.exists()) {
            vfsProvider.getVirtualFileSystem().getRoot().createFile("file", "hello");
//        }

        //noinspection unchecked
        return options.getMap();
    }
}
