/* 
 * Created on Jan 19, 2005
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

package org.springmodules.cache.interceptor;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.mock.MockCacheAttribute;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractNameMatchCacheAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/29 01:22:15 $
 */
public final class NameMatchCacheAttributeSourceTests extends TestCase {

  /**
   * Property editor for instances of <code>{@link CacheAttribute}</code>.
   * This editor is used as the return value of the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#getCacheAttributeEditor()}</code>.
   */
  private PropertyEditor cacheAttributeEditor;

  private MockControl cacheAttributeEditorControl;

  private AbstractNameMatchCacheAttributeSource cacheAttributeSource;

  private MockClassControl cacheAttributeSourceControl;

  public NameMatchCacheAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Asserts that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#isMatch(String, String)}</code>
   * returns <code>false</code> for the given method name and mapped name.
   * 
   * @param methodName
   *          the given method name.
   * @param mappedName
   *          the given mapped name.
   */
  private void assertMethodNameDoesNotMatchMappedName(String methodName,
      String mappedName) {
    boolean matches = cacheAttributeSource.isMatch(methodName, mappedName);
    assertFalse("The method name '" + methodName
        + "' should not match the mapped name '" + mappedName + "'", matches);
  }

  /**
   * Asserts that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#isMatch(String, String)}</code>
   * returns <code>true</code> for the given method name and mapped name.
   * 
   * @param methodName
   *          the given method name.
   * @param mappedName
   *          the given mapped name.
   */
  private void assertMethodNameMatchesMappedName(String methodName,
      String mappedName) {
    boolean matches = cacheAttributeSource.isMatch(methodName, mappedName);
    assertTrue("The method name '" + methodName
        + "' should match the mapped name '" + mappedName + "'", matches);
  }

  private void setStateOfMockControlsToReplay() {
    cacheAttributeEditorControl.replay();
    cacheAttributeSourceControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();

    setUpCacheAttributeSourceAsMockObject();
  }

  private void setUpCacheAttributeEditorAsMockObject() {
    cacheAttributeEditorControl = MockControl
        .createControl(PropertyEditor.class);

    cacheAttributeEditor = (PropertyEditor) cacheAttributeEditorControl
        .getMock();
  }

  /**
   * Sets up <code>{@link #cacheAttributeSource}</code> as a mock object.
   */
  private void setUpCacheAttributeSourceAsMockObject() throws Exception {
    Class classToMock = AbstractNameMatchCacheAttributeSource.class;

    Method getCacheAttributeEditorMethod = classToMock.getDeclaredMethod(
        "getCacheAttributeEditor", null);
    Method[] methodsToMock = new Method[] { getCacheAttributeEditorMethod };

    cacheAttributeSourceControl = MockClassControl.createControl(classToMock,
        null, null, methodsToMock);

    cacheAttributeSource = (AbstractNameMatchCacheAttributeSource) cacheAttributeSourceControl
        .getMock();
  }

  public void testIsMatchWithMatchingMethodNameAndMappedNameEndingWithWildcard() {
    assertMethodNameMatchesMappedName("getNewCustomer", "getNew*");
  }

  public void testIsMatchWithMatchingMethodNameAndMappedNameStartingWithWildcard() {
    assertMethodNameMatchesMappedName("getNewCustomer", "*Customer");
  }

  public void testIsMatchWithNotMatchingMethodName() {
    assertMethodNameDoesNotMatchMappedName("getNewCustomer",
        "getCurrentCustomer");
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#setProperties(Properties)}</code>
   * does not add any entry to the map of <code>{@link CacheAttribute}</code>
   * if the <code>{@link CacheAttribute}</code> returned by the editor (
   * <code>{@link #cacheAttributeEditor}</code>) is <code>null</code>.
   */
  public void testSetPropertiesWhenPropertyEditorReturnsCacheAttributeEqualToNull() {
    setUpCacheAttributeEditorAsMockObject();

    String cacheAttributeProperties = "[cacheModelId=test]";
    Properties cacheAttributes = new Properties();
    cacheAttributes.setProperty("getNew*", cacheAttributeProperties);

    // get the the editor of cache attributes.
    cacheAttributeSource.getCacheAttributeEditor();
    cacheAttributeSourceControl.setReturnValue(cacheAttributeEditor);

    // set the properties of the cache attributes to the editor.
    cacheAttributeEditor.setAsText(cacheAttributeProperties);

    // the cache attribute created by the editor should be null.
    cacheAttributeEditor.getValue();
    cacheAttributeEditorControl.setReturnValue(null);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheAttributeSource.setProperties(cacheAttributes);

    // verify that the map of cache attributes is empty.
    Map actualCacheAttributes = cacheAttributeSource.getAttributeMap();
    assertTrue("The map of attributes should be empty", actualCacheAttributes
        .isEmpty());

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractNameMatchCacheAttributeSource#setProperties(Properties)}</code>
   * adds the <code>{@link CacheAttribute}</code> returned by the editor (
   * <code>{@link #cacheAttributeEditor}</code>) to the map of cache
   * attributes when such <code>{@link CacheAttribute}</code> is not
   * <code>null</code>.
   */
  public void testSetPropertiesWhenPropertyEditorReturnsObjectNotEqualToNull() {
    setUpCacheAttributeEditorAsMockObject();

    String mappedName = "getNew*";
    String cacheAttributeProperties = "[cacheModelId=test]";
    Properties cacheAttributes = new Properties();
    cacheAttributes.setProperty(mappedName, cacheAttributeProperties);

    // get the the editor of cache attributes.
    cacheAttributeSource.getCacheAttributeEditor();
    cacheAttributeSourceControl.setReturnValue(cacheAttributeEditor);

    // set the properties of the cache attributes to the editor.
    cacheAttributeEditor.setAsText(cacheAttributeProperties);

    // the cache attribute created by the editor should not null.
    cacheAttributeEditor.getValue();
    CacheAttribute cacheAttribute = new MockCacheAttribute();
    cacheAttributeEditorControl.setReturnValue(cacheAttribute);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheAttributeSource.setProperties(cacheAttributes);

    // verify the cache attribute was added to the map.
    Map actualCacheAttributes = cacheAttributeSource.getAttributeMap();
    Object actualCacheAttribute = actualCacheAttributes.get(mappedName);
    assertSame(cacheAttribute, actualCacheAttribute);

    verifyExpectationsOfMockControlsWereMet();
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    cacheAttributeEditorControl.verify();
    cacheAttributeSourceControl.verify();
  }
}