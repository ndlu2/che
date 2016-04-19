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
package org.eclipse.che.ide.ext.debugger.shared;

import org.eclipse.che.dto.shared.DTO;

/**
 * Event will be generated when breakpoint become active.
 *
 * @author Anatoliy Bazko
 */
@DTO
public interface BreakpointActivatedEvent extends DebuggerEvent {
    Breakpoint getBreakpoint();

    void setBreakpoint(Breakpoint breakpoint);

    BreakpointActivatedEvent withBreakpoint(Breakpoint breakpoint);
}