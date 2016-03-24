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

import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.extension.machine.client.MachineLocalizationConstant;
import org.eclipse.che.ide.extension.machine.client.machine.create.CreateMachinePresenter;

import javax.validation.constraints.NotNull;
import java.util.Collections;

import static org.eclipse.che.ide.extension.machine.client.perspective.OperationsPerspective.OPERATIONS_PERSPECTIVE_ID;

/**
 * The action contains business logic which calls special method to create machine.
 *
 * @author Dmitry Shnurenko
 */
public class CreateMachineAction extends AbstractPerspectiveAction {

    private final CreateMachinePresenter createMachinePresenter;

    @Inject
    public CreateMachineAction(MachineLocalizationConstant locale,
                               CreateMachinePresenter createMachinePresenter) {
        super(Collections.singletonList(OPERATIONS_PERSPECTIVE_ID),
              locale.machineCreateTitle(),
              locale.machineCreateDescription(),
              null, null);

        this.createMachinePresenter = createMachinePresenter;
    }

    /** {@inheritDoc} */
    @Override
    public void updateInPerspective(@NotNull ActionEvent event) {
        //to do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(@NotNull ActionEvent event) {
        createMachinePresenter.showDialog();
    }

}
