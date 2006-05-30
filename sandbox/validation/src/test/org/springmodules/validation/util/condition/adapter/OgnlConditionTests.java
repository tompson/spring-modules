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

package org.springmodules.validation.util.condition.adapter;

import junit.framework.TestCase;

/**
 * Tests for {@link OgnlCondition}.
 *
 * @author Uri Boness
 */
public class OgnlConditionTests extends TestCase {

    public void testContructor_WithNullExpression() throws Exception {
        try {
            new OgnlCondition(null);
            fail("An IllegalArgumentException must be thrown if passed in expression is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_Success() throws Exception {
        OgnlCondition condition = new OgnlCondition("count == 4");
        assertTrue(condition.check(new CountBean(4)));
    }

    public void testCheck_Failure() throws Exception {
        OgnlCondition condition = new OgnlCondition("count == 5");
        assertFalse(condition.check(new CountBean(4)));
    }

    public void testCheck_WithUnfittedBeanForExpression() throws Exception {
        OgnlCondition condition = new OgnlCondition("age == 88");
        try {
            condition.check(new CountBean(4));
            fail("An IllegalArgumentException must be thrown if checked bean doesn't fit the ognl expression");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    //=============================================== Helper Classes ===================================================

    /**
     * A bean that is used in the tests
     */
    protected class CountBean {

        private int count;

        public CountBean() {
        }

        public CountBean(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}