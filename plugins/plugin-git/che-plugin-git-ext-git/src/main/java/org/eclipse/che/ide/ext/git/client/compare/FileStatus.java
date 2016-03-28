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
package org.eclipse.che.ide.ext.git.client.compare;

import static org.eclipse.che.ide.ext.git.client.compare.FileStatus.Status.Added;
import static org.eclipse.che.ide.ext.git.client.compare.FileStatus.Status.Copied;
import static org.eclipse.che.ide.ext.git.client.compare.FileStatus.Status.Deleted;
import static org.eclipse.che.ide.ext.git.client.compare.FileStatus.Status.Modified;
import static org.eclipse.che.ide.ext.git.client.compare.FileStatus.Status.Unmodified;
import static org.eclipse.che.ide.ext.git.client.compare.FileStatus.Status.UpdatedButUnmerged;

/**
 * Class for determining git status of given changed file.
 *
 * @author Igor Vinokur
 */
public class FileStatus {

    /**
     * Git statuses.
     */
    public enum Status {
        Modified, Added, Deleted, Copied, UpdatedButUnmerged, Unmodified
    }

    /**
     * determining git status of changed file.
     *
     * @param status
     *         String representation of git status
     */
    public static Status defineStatus(String status) {
        switch (status) {
            case "M":
                return Modified;
            case "D":
                return Deleted;
            case "A":
                return Added;
            case "C":
                return Copied;
            case "U":
                return UpdatedButUnmerged;
        }
        return Unmodified;
    }

}
