/* 
 * Created on Aug 19, 2005
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.cache.serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.AbstractEqualsHashCodeTestCase;
import org.springmodules.cache.serializable.XStreamSerializableFactory.ObjectWrapper;
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link XStreamSerializableFactory.ObjectWrapper}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ObjectWrapperTests extends AbstractEqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(ObjectWrapperTests.class);

  private ObjectWrapper wrapper;

  public ObjectWrapperTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(wrapper.getClass().getName());
    buffer.append("@" + System.identityHashCode(wrapper) + "[");
    buffer.append("value=" + Strings.quoteIfString(wrapper.getValue()) + "]");

    String expected = buffer.toString();
    String actual = wrapper.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() {
    wrapper = new ObjectWrapper();
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String obj = "Leia";

    wrapper.setValue(obj);
    ObjectWrapper wrapper2 = new ObjectWrapper(obj);

    assertEqualsHashCodeRelationshipIsCorrect(wrapper, wrapper2);

    obj = null;

    wrapper.setValue(obj);
    wrapper2.setValue(obj);

    assertEqualsHashCodeRelationshipIsCorrect(wrapper, wrapper2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    ObjectWrapper wrapper2 = new ObjectWrapper();
    assertEquals(wrapper, wrapper2);

    wrapper2.setValue("Luke");
    assertFalse(wrapper.equals(wrapper2));
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEqualsIsReflexive(wrapper);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    ObjectWrapper wrapper2 = new ObjectWrapper();
    assertEqualsIsSymmetric(wrapper, wrapper2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String obj = "Han";

    wrapper.setValue(obj);
    ObjectWrapper wrapper2 = new ObjectWrapper(obj);
    ObjectWrapper wrapper3 = new ObjectWrapper(obj);

    assertEqualsIsTransitive(wrapper, wrapper2, wrapper3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(wrapper);
  }

  public void testToStringWithValueBeingString() {
    wrapper.setValue("C-3PO");

    assertToStringIsCorrect();
  }

  public void testToStringWithValueNotBeingString() {
    wrapper.setValue(new Integer(10));

    assertToStringIsCorrect();
  }
}
