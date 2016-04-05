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
package org.eclipse.che.ide.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of the String Utils class
 * @author Nicholas Lu/Wajih Ul Hassan
 */
public class StringUtilsTest {
    @Test
    public void testSecToHumanReadable() {
        Assert.assertEquals("-1s", StringUtils.timeSecToHumanReadable(-1));
        Assert.assertEquals("01s", StringUtils.timeSecToHumanReadable(1));
        Assert.assertEquals("1m:01s", StringUtils.timeSecToHumanReadable(61));
        Assert.assertEquals("1h:01m:01s", StringUtils.timeSecToHumanReadable(3661));
        Assert.assertEquals("115d:17h:46m:39s", StringUtils.timeSecToHumanReadable(9999999));
    }

}
