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
package org.eclipse.che.api.workspace.server.event;

import org.eclipse.che.api.core.model.workspace.UsersWorkspace;

/**
 * @author Sergii Leschenko
 */
public class WorkspaceCreatedEvent {
    private final UsersWorkspace workspace;
    private final String         accountId;

    public WorkspaceCreatedEvent(UsersWorkspace workspace, String accountId) {
        this.workspace = workspace;
        this.accountId = accountId;
    }

    public UsersWorkspace getWorkspace() {
        return workspace;
    }

    public String getAccountId() {
        return accountId;
    }
}
