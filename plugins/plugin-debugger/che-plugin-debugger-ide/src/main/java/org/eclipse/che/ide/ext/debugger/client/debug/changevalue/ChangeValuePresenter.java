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
package org.eclipse.che.ide.ext.debugger.client.debug.changevalue;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.debug.Debugger;
import org.eclipse.che.ide.debug.DebuggerManager;
import org.eclipse.che.ide.ext.debugger.client.DebuggerLocalizationConstant;
import org.eclipse.che.ide.ext.debugger.client.debug.DebuggerPresenter;
import org.eclipse.che.ide.ext.debugger.client.debug.DebuggerVariable;

import java.util.List;

/**
 * Presenter for changing variables value.
 *
 * @author Artem Zatsarynnyi
 */
@Singleton
public class ChangeValuePresenter implements ChangeValueView.ActionDelegate {
    private final DebuggerManager              debuggerManager;
    private final ChangeValueView              view;
    private final DebuggerPresenter            debuggerPresenter;
    private final DebuggerLocalizationConstant constant;

    private DebuggerVariable debuggerVariable;

    /** Create presenter. */
    @Inject
    public ChangeValuePresenter(ChangeValueView view,
                                DebuggerLocalizationConstant constant,
                                DebuggerManager debuggerManager,
                                DebuggerPresenter debuggerPresenter) {
        this.view = view;
        this.debuggerManager = debuggerManager;
        this.debuggerPresenter = debuggerPresenter;
        this.view.setDelegate(this);
        this.constant = constant;
    }

    /** Show dialog. */
    public void showDialog() {
        debuggerVariable = debuggerPresenter.getSelectedVariable();
        view.setValueTitle(constant.changeValueViewExpressionFieldTitle(debuggerVariable.getName()));
        view.setValue(debuggerVariable.getValue());
        view.focusInValueField();
        view.selectAllText();
        view.setEnableChangeButton(false);
        view.showDialog();
    }

    /** {@inheritDoc} */
    @Override
    public void onCancelClicked() {
        view.close();
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeClicked() {
        Debugger debugger = debuggerManager.getActiveDebugger();
        if (debugger != null) {
            List<String> variablePath = debuggerVariable.getVariablePath().getPath();
            String newValue = view.getValue();

            debugger.changeVariableValue(variablePath, newValue);
        }

        view.close();
    }

    /** {@inheritDoc} */
    @Override
    public void onVariableValueChanged() {
        final String value = view.getValue();
        boolean isExpressionFieldNotEmpty = !value.trim().isEmpty();
        view.setEnableChangeButton(isExpressionFieldNotEmpty);
    }
}