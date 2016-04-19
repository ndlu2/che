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
package org.eclipse.che.ide.gdb.client.configuration;

import org.eclipse.che.ide.api.mvp.View;

/**
 * The view of {@link GdbConfigurationPagePresenter}.
 *
 * @author Artem Zatsarynnyi
 */
public interface GdbConfigurationPageView extends View<GdbConfigurationPageView.ActionDelegate> {

    /** Returns host. */
    String getHost();

    /** Sets host. */
    void setHost(String host);

    /** Returns port. */
    int getPort();

    /** Sets port. */
    void setPort(int port);

    /** Returns path to the binary. */
    String getBinaryPath();

    /** Sets path to the binary. */
    void setBinaryPath(String path);

    /** Action handler for the view's controls. */
    interface ActionDelegate {

        /** Called when 'Host' has been changed. */
        void onHostChanged();

        /** Called when 'Port' has been changed. */
        void onPortChanged();

        /** Called when 'Binary Path' has been changed. */
        void onBinaryPathChanged();
    }
}
