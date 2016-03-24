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
package org.eclipse.che.ide.extension.machine.client.actions;

import com.google.inject.Inject;

import org.eclipse.che.api.machine.gwt.client.MachineManager;
import org.eclipse.che.api.machine.shared.dto.MachineDto;
import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.extension.machine.client.MachineLocalizationConstant;
import org.eclipse.che.ide.extension.machine.client.perspective.widgets.machine.panel.MachinePanelPresenter;
import org.eclipse.che.ide.ui.dialogs.DialogFactory;

import javax.validation.constraints.NotNull;
import java.util.Collections;

import static org.eclipse.che.ide.extension.machine.client.perspective.OperationsPerspective.OPERATIONS_PERSPECTIVE_ID;

/**
 * The action contains business logic which calls special method to destroy machine.
 *
 * @author Dmitry Shnurenko
 */
public class DestroyMachineAction extends AbstractPerspectiveAction {

    private final MachineLocalizationConstant locale;
    private final MachinePanelPresenter       panelPresenter;
    private final MachineManager              machineManager;
    private final DialogFactory               dialogFactory;

    @Inject
    public DestroyMachineAction(MachineLocalizationConstant locale,
                                MachinePanelPresenter panelPresenter,
                                MachineManager machineManager,
                                DialogFactory dialogFactory) {
        super(Collections.singletonList(OPERATIONS_PERSPECTIVE_ID),
              locale.machineDestroyTitle(),
              locale.machineDestroyDescription(),
              null, null);

        this.locale = locale;
        this.panelPresenter = panelPresenter;
        this.machineManager = machineManager;
        this.dialogFactory = dialogFactory;
    }

    /** {@inheritDoc} */
    @Override
    public void updateInPerspective(@NotNull ActionEvent event) {
        final MachineDto selectedMachine = panelPresenter.getSelectedMachineState();
        event.getPresentation().setEnabled(selectedMachine != null
                                           && !selectedMachine.getConfig().isDev()
                                           && panelPresenter.isMachineRunning());
        event.getPresentation().setText(selectedMachine != null ? locale.machineDestroyTitle(selectedMachine.getConfig().getName())
                                                                : locale.machineDestroyTitle());
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(@NotNull ActionEvent event) {
        final MachineDto selectedMachine = panelPresenter.getSelectedMachineState();
        if (selectedMachine == null) {
            return;
        }

        machineManager.destroyMachine(selectedMachine);
    }
}
