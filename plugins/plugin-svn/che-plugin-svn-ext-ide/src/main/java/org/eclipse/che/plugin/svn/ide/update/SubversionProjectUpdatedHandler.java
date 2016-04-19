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
package org.eclipse.che.plugin.svn.ide.update;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link SubversionProjectUpdatedEvent} event.
 *
 * @author Vladyslav Zhukovskyi
 */
public interface SubversionProjectUpdatedHandler extends EventHandler {

    /**
     * Perform actions when project has been updated.
     *
     * @param event updated event
     */
    void onProjectUpdated(SubversionProjectUpdatedEvent event);
}
