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
package org.eclipse.che.ide.ext.java.jdi.server;

import org.eclipse.che.ide.ext.debugger.shared.Breakpoint;

import java.util.Comparator;

/**
 * Helps to order breakpoints by name of location and line number.
 *
 * @author andrew00x
 */
final class BreakPointComparator implements Comparator<Breakpoint> {
    @Override
    public int compare(Breakpoint o1, Breakpoint o2) {
        String className1 = o1.getLocation().getClassName();
        String className2 = o2.getLocation().getClassName();
        if (className1 == null && className2 == null) {
            return 0;
        }
        if (className1 == null) {
            return 1;
        }
        if (className2 == null) {
            return -1;
        }
        int result = className1.compareTo(className2);
        if (result == 0) {
            result = o1.getLocation().getLineNumber() - o2.getLocation().getLineNumber();
        }
        return result;
    }
}
