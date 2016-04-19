
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test of the String Utils class
 * @author Nicholas Lu/Wajih Ul Hassan
 */
@RunWith(Parameterized.class)
public class StringUtilsTest {
    enum Type {SEC_TO_HUMAN_READABLE, COMMON_PREFIX_LENGTH, ENDS_WITH, STARTS_WITH};
    
    @Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][] {
          {Type.SEC_TO_HUMAN_READABLE, "-1s", Arrays.asList(-1)},
          {Type.SEC_TO_HUMAN_READABLE, "01s", Arrays.asList(1)},
          {Type.SEC_TO_HUMAN_READABLE, "1m:01s", Arrays.asList(61)},
          {Type.SEC_TO_HUMAN_READABLE, "1h:01m:01s", Arrays.asList(3661)},
          {Type.SEC_TO_HUMAN_READABLE, "115d:17h:46m:39s", Arrays.asList(9999999)},
          
          {Type.COMMON_PREFIX_LENGTH, 0, Arrays.asList("no", "common")},
          {Type.COMMON_PREFIX_LENGTH, 3, Arrays.asList("com", "common")},
          
          {Type.ENDS_WITH, true, Arrays.asList(null, null)},
          {Type.ENDS_WITH, false, Arrays.asList(null, "foo")},
          {Type.ENDS_WITH, false, Arrays.asList("foo", "bar")},
          {Type.ENDS_WITH, true, Arrays.asList("foobar", "bar")},
          
          {Type.STARTS_WITH, false, Arrays.asList("foo", "bar", true)},
          {Type.STARTS_WITH, false, Arrays.asList("foo", "bar", false)},
          {Type.STARTS_WITH, true, Arrays.asList("foo", "FoObar", true)},		
          {Type.STARTS_WITH, false, Arrays.asList("foo", "FoObar", false)},
          {Type.STARTS_WITH, true, Arrays.asList("foo", "FoO", true)},
        });
    }

    @Parameter(value = 0)
    public Type type;
    
    @Parameter(value = 1)
    public Object expected;
    
    @Parameter(value = 2)
    public List<Object> args;

    @Test
    public void testSecToHumanReadable(){
        Assume.assumeTrue(type == Type.SEC_TO_HUMAN_READABLE);
        Assert.assertEquals(expected, StringUtils.timeSecToHumanReadable(new Long((Integer)args.get(0))));
    }

    @Test
    public void testCommonPrefixLength(){
        Assume.assumeTrue(type == Type.COMMON_PREFIX_LENGTH);
        Assert.assertEquals(expected, StringUtils.findCommonPrefixLength((String)args.get(0), (String)args.get(1)));
    }
    
    @Test
    public void testEndsWith(){
    	Assume.assumeTrue(type == Type.ENDS_WITH);
    	Assert.assertEquals(expected, StringUtils.endsWithIgnoreCase((String)args.get(0), (String)args.get(1)));
    }
    
    @Test
    public void testStartsWith() {
    	Assume.assumeTrue(type == Type.STARTS_WITH);
    	Assert.assertEquals(expected, StringUtils.startsWith((String)args.get(0), (String)args.get(1), (Boolean)args.get(2)));
    }
}
