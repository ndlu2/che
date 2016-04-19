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
package org.eclipse.che.ide.gdb.server.parser;

import org.eclipse.che.ide.ext.debugger.shared.Location;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Anatoliy Bazko
 */
public class GdbInfoLineTest {

    @Test
    public void testParse1() throws Exception {
        GdbOutput gdbOutput = GdbOutput.of("Line 6 of \"h.cpp\" starts at address 0x4008ae <main()+17> and ends at 0x4008ca <main()+45>.\n");

        GdbInfoLine gdbInfoLine = GdbInfoLine.parse(gdbOutput);
        Location location = gdbInfoLine.getLocation();

        assertEquals(location.getClassName(), "h.cpp");
        assertEquals(location.getLineNumber(), 6);
    }

    @Test
    public void testParse2() throws Exception {
        GdbOutput gdbOutput = GdbOutput.of("Line 530 of \"/usr/src/debug/gcc-4.8.3-20140911/obj-x86_64-redhat-linux/x86_64-redhat-linux/libstdc++-v3/include/ostream\"\n" +
                                           "   starts at address 0x3e8ba94e60 <std::operator<< <std::char_traits<char> >(std::basic_ostream<char, std::char_traits<char> >&, char const*)>\n" +
                                           "   and ends at 0x3e8ba94e6c <std::operator<< <std::char_traits<char> >(std::basic_ostream<char, std::char_traits<char> >&, char const*)+12>.\n");

        GdbInfoLine gdbInfoLine = GdbInfoLine.parse(gdbOutput);
        Location location = gdbInfoLine.getLocation();

        assertEquals(location.getClassName(), "/usr/src/debug/gcc-4.8.3-20140911/obj-x86_64-redhat-linux/x86_64-redhat-linux/libstdc++-v3/include/ostream");
        assertEquals(location.getLineNumber(), 530);
    }
}
