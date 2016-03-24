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
package org.eclipse.che.plugin.docker.client.params;

import org.eclipse.che.plugin.docker.client.ProgressMonitor;

/**
 * Arguments holder for {@code pull} method of {@link org.eclipse.che.plugin.docker.client.DockerClient}.
 *
 * @author Mykola Morhun
 */
public class PullParams {
    /** full repository name to be applied to newly created image */
    private String          image;
    /** tag of the image */
    private String          tag;
    /** registry url */
    private String          registry;
    /** ProgressMonitor for images creation process */
    private ProgressMonitor progressMonitor;

    public PullParams withImage(String image) {
        this.image = image;
        return this;
    }

    public PullParams withTag(String tag) {
        this.tag = tag;
        return this;
    }

    public PullParams withRegistry(String registry) {
        this.registry = registry;
        return this;
    }

    public PullParams withProgressMonitor(ProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
        return this;
    }

    public String getImage() {
        return image;
    }

    public String getTag() {
        return tag;
    }

    public String getRegistry() {
        return registry;
    }

    public ProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

}
