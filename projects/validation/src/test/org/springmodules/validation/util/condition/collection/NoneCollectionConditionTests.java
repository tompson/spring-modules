/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.util.condition.collection;

import java.util.Collection;

import org.springmodules.validation.util.condition.AbstractCondition;

/**
 * Tests for {@link NoneCollectionCondition}.
 *
 * @author Uri Boness
 */
public class NoneCollectionConditionTests extends AbstractCollectionElementConditionTests {

    protected AbstractCollectionCondition createCondition() {
        return new NoneCollectionCondition(new AbstractCondition() {
            public boolean doCheck(Object object) {
                return (object instanceof String); // only elements of type string pass the checkBean
            }
        });
    }

    public void testConstructor_WithNullElementCondition() throws Exception {
        try {
            new NoneCollectionCondition(null);
            fail("An IllegalArgumentException must be thrown if given element condition is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WhenAllCollectionElementsPass() throws Exception {
        Collection collection = new FluentList().addObject("this").addObject("is").addObject("a").addObject("test");
        assertFalse(condition.check(collection));
    }

    public void testCheck_WhenAllArrayElementsPass() throws Exception {
        Object[] array = new Object[] { "this", "is", "a", "test" };
        assertFalse(condition.check(array));
    }

    public void testCheck_WhenSomeCollectionElementsPass() throws Exception {
        Collection collection = new FluentList().addObject("this").addObject("is").addObject("a").addObject(new Integer(3));
        assertFalse(condition.check(collection));
    }

    public void testCheck_WhenSomeArrayElementsPass() throws Exception {
        Object[] array = new Object[] { "this", "is", "a", new Integer(3) };
        assertFalse(condition.check(array));
    }

    public void testCheck_WhenNoneCollectionElementsPass() throws Exception {
        Collection collection = new FluentList().addObject(new Integer(1)).addObject(new Integer(2));
        assertTrue(condition.check(collection));
    }

    public void testCheck_WhenNoneArrayElementsPass() throws Exception {
        Object[] array = new Object[] { new Integer(1), new Integer(2) };
        assertTrue(condition.check(array));
    }

}
