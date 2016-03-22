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

import com.google.inject.Inject;

import org.eclipse.che.ide.ext.java.shared.dto.search.FindUsagesRequest;
import org.eclipse.che.ide.ext.java.shared.dto.search.FindUsagesResponse;
import org.eclipse.che.jdt.search.SearchException;
import org.eclipse.che.jdt.search.SearchManager;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.JavaModel;
import org.eclipse.jdt.internal.core.JavaModelManager;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * REST service for all java project related searches.
 *
 * @author Evgen Vidolob
 */
@Path("jdt/{ws-id}/search")
public class SearchService {

    @Inject
    private SearchManager manager;

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("find/usages")
    public FindUsagesResponse findUsages(FindUsagesRequest request) throws SearchException {
        JavaModel javaModel = JavaModelManager.getJavaModelManager().getJavaModel();
        IJavaProject javaProject = javaModel.getJavaProject(request.getProjectPath());
        return manager.findUsage(javaProject, request.getFQN(), request.getOffset());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    @Path("find/byFqn")
    public Response findClassesByFqn(FindUsagesRequest request) throws SearchException {//todo use own dto or simple query
        List<IJavaElement> result = manager.findByFqn(request.getFQN());
        if (result.isEmpty()) {
            throw new NotFoundException("Files by fqn: " + request.getFQN() + " was not found");
        } else {
            String message = "[ ";
            for (IJavaElement iJavaElement: result) {
                message += iJavaElement.getElementName() + " ";
            }
            message += "]";
            return Response.ok(message).build();
        }
    }
}
