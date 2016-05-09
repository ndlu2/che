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
package org.eclipse.che.commons.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class JsonTest {
    public static class Foo {
        private String fooBar;
        private HashMap<String, String> things;
        public Foo() {

        }

        public Foo(String fooBar) {
            this.fooBar = fooBar;
        }

        public String getFooBar() {
            return fooBar;
        }

        public void setFooBar(String fooBar) {
            this.fooBar = fooBar;
        }

        public void addThing(String key, String value)
        {
            if(this.things == null)
                this.things = new HashMap<String, String>();
            this.things.put(key, value);
        }
        public String getThing(String key)
        {
            return things.get(key);
        }
    }

    @Test
    public void testSerializeDefault() throws Exception {
        String expectedJson = "{\"fooBar\":\"test\"}";
        Foo foo = new Foo();
        foo.setFooBar("test");
        //assertEquals(expectedJson, JsonHelper.toJson(foo));

        foo.addThing("hi", "there");
        System.out.println(foo.getThing("hi"));
        System.out.println(JsonHelper.toJson(foo));

        JsonHelper.fromJson(JsonHelper.toJson(foo), Foo.class, null);
    }

    @Test
    public void testSerializeArray() throws Exception {
        String expectedJson = "[{\"fooBar\":\"test 0\"},{\"fooBar\":\"test 1\"},{\"fooBar\":\"test 2\"}]";
        Foo[] foos = new Foo[3];
        foos[0] = new Foo("test 0");
        foos[1] = new Foo("test 1");
        foos[2] = new Foo("test 2");
        assertEquals(expectedJson, JsonHelper.toJson(foos));
    }

    @Test
    public void testSerializeCollection() throws Exception {
        String expectedJson = "[{\"fooBar\":\"test 0\"},{\"fooBar\":\"test 1\"},{\"fooBar\":\"test 2\"}]";
        Foo[] foos = new Foo[3];
        foos[0] = new Foo("test 0");
        foos[1] = new Foo("test 1");
        foos[2] = new Foo("test 2");
        assertEquals(expectedJson, JsonHelper.toJson(Arrays.asList(foos)));
    }

    @Test
    public void testSerializeMap() throws Exception {
        String expectedJson = "{\"foo 1\":{\"fooBar\":\"test 1\"},\"foo 3\":{\"fooBar\":\"test 3\"},\"foo 2\":{\"fooBar\":\"test 2\"}}";
        Map<String, Foo> foos = new HashMap<String, Foo>();
        foos.put("foo 1", new Foo("test 1"));
        foos.put("foo 2", new Foo("test 2"));
        foos.put("foo 3", new Foo("test 3"));
        assertEquals(expectedJson, JsonHelper.toJson(foos));
        //JsonHelper.fromJson(JsonHelper.toJson(foos), foos.getClass(), null);
    }

    @Test
    public void testSerializeUnderscore() throws Exception {
        String expectedJson = "{\"foo_bar\":\"test\"}";
        Foo foo = new Foo();
        foo.setFooBar("test");
        assertEquals(expectedJson, JsonHelper.toJson(foo, JsonNameConventions.CAMEL_UNDERSCORE));
    }

    @Test
    public void testSerializeDash() throws Exception {
        String expectedJson = "{\"foo-bar\":\"test\"}";
        Foo foo = new Foo();
        foo.setFooBar("test");
        assertEquals(expectedJson, JsonHelper.toJson(foo, JsonNameConventions.CAMEL_DASH));
    }

    @Test
    public void testDeserializeDefault() throws Exception {
        String json = "{\"fooBar\":\"test\"}";
        Foo foo = JsonHelper.fromJson(json, Foo.class, null);
        assertEquals("test", foo.getFooBar());
    }

    @Test
    public void testDeserializeUnderscore() throws Exception {
        String json = "{\"foo_bar\":\"test\"}";
        Foo foo = JsonHelper.fromJson(json, Foo.class, null, JsonNameConventions.CAMEL_UNDERSCORE);
        assertEquals("test", foo.getFooBar());
    }

    @Test
    public void testDeserializeDash() throws Exception {
        String json = "{\"foo-bar\":\"test\"}";
        Foo foo = JsonHelper.fromJson(json, Foo.class, null, JsonNameConventions.CAMEL_DASH);
        assertEquals("test", foo.getFooBar());
    }

    @Test
    public void testDeserializeArray() throws Exception {
        String json = "[{\"fooBar\":\"test 0\"},{\"fooBar\":\"test 1\"},{\"fooBar\":\"test 2\"}]";
        Foo[] foos = JsonHelper.fromJson(json, Foo[].class, null);

        assertEquals(3, foos.length);
        assertEquals("test 0", foos[0].getFooBar());
        assertEquals("test 1", foos[1].getFooBar());
        assertEquals("test 2", foos[2].getFooBar());
    }
/*
    @Test
    public void testDeserializeCollection() throws Exception {
        String json = "[{\"fooBar\":\"test 0\"},{\"fooBar\":\"test 1\"},{\"fooBar\":\"test 2\"}]";
        List<Foo> foos = (List<Foo>)JsonHelper.fromJson(json, List.class, null);
        
        assertEquals(3, foos.size());
        assertEquals("test 0", foos.get(0).getFooBar());
        assertEquals("test 1", foos.get(1).getFooBar());
        assertEquals("test 2", foos.get(2).getFooBar());
    }


    @Test
    public void testDeserializeMap() throws Exception {
        String expectedJson = "{\"foo 1\":{\"fooBar\":\"test 1\"},\"foo 3\":{\"fooBar\":\"test 3\"},\"foo 2\":{\"fooBar\":\"test 2\"}}";
        Map<String, Foo> foos = new HashMap<String, Foo>();
        foos.put("foo 1", new Foo("test 1"));
        foos.put("foo 2", new Foo("test 2"));
        foos.put("foo 3", new Foo("test 3"));
        assertEquals(expectedJson, JsonHelper.toJson(foos));
        //JsonHelper.fromJson(JsonHelper.toJson(foos), foos.getClass(), null);
    }
*/
}
