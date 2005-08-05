/* 
 * Created on Jan 18, 2005
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

package org.springmodules.cache.interceptor.caching;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link NameMatchCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/05 02:18:48 $
 */
public class NameMatchCachingAttributeSourceTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private NameMatchCachingAttributeSource attributeSource;

  private Method method;

  public NameMatchCachingAttributeSourceTests(String name) {
    super(name);
  }

  private void assertEqualCachingAttributes(Cached expected, Cached actual) {
    assertEquals("<Caching attribute>", expected, actual);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.attributeSource = new NameMatchCachingAttributeSource();
  }

  private void setUpMethod() throws Exception {
    this.method = String.class.getDeclaredMethod("charAt",
        new Class[] { int.class });
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * adds the specified caching attribute to the map of attributes using the
   * specified method name as the key.
   */
  public void testAddCachingAttribute() {
    String methodName = "addUser";
    Cached cachingAttribute = new Cached();

    this.attributeSource.addCachingAttribute(methodName, cachingAttribute);

    Map cachingAttributeMap = this.attributeSource.getCachingAttributeMap();
    assertTrue("There should be an entry with key '" + methodName + "'",
        cachingAttributeMap.containsKey(methodName));

    assertSame("<Added caching attribute>", cachingAttribute,
        cachingAttributeMap.get(methodName));
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCacheAttributeEditor()}</code>
   * returns a new instance of <code>{@link CachingAttributeEditor}</code>.
   */
  public void testGetCacheAttributeEditor() {
    PropertyEditor cacheAttributeEditor = this.attributeSource
        .getCacheAttributeEditor();

    assertNotNull("The editor of caching attributes should not be null",
        cacheAttributeEditor);

    Class expectedEditorClass = CachingAttributeEditor.class;
    assertEquals("<Property editor class>", expectedEditorClass,
        cacheAttributeEditor.getClass());
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns a caching attribute when the name of the given method matches a
   * mapped name that does not contain wild cards.
   */
  public void testGetCachingAttributeWithMethodNameMatchingMappedNameWithoutWildCards()
      throws Exception {

    // set the properties to be used to create caching attributes.
    Properties properties = new Properties();
    properties.setProperty("charAt", "[cacheProfileId=main]");
    this.attributeSource.setProperties(properties);

    this.setUpMethod();
    Cached expectedCachingAttribute = new Cached("main");

    // execute the method to test.
    Cached actualCachingAttribute = this.attributeSource.getCachingAttribute(
        this.method, this.method.getDeclaringClass());

    this.assertEqualCachingAttributes(expectedCachingAttribute,
        actualCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns a caching attribute when the name of the given method matches a
   * mapped name containing wild cards.
   */
  public void testGetCachingAttributeWithMethodNameMatchingMappedNameWithWildCards()
      throws Exception {

    // set the properties to be used to create caching attributes.
    Properties properties = new Properties();

    // both property keys match the name of the method, but the second one is
    // used since has a longer length.
    properties.setProperty("ch*", "[cacheProfileId=test]");
    properties.setProperty("char*", "[cacheProfileId=main]");
    this.attributeSource.setProperties(properties);

    this.setUpMethod();
    Cached expectedCachingAttribute = new Cached("main");

    // execute the method to test.
    Cached actualCachingAttribute = this.attributeSource.getCachingAttribute(
        this.method, this.method.getDeclaringClass());

    this.assertEqualCachingAttributes(expectedCachingAttribute,
        actualCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * does not return any caching attribute if the name of the given method does
   * not match any of the mapped methods.
   */
  public void testGetCachingAttributeWithNotMatchingMethodName()
      throws Exception {

    this.setUpMethod();

    Properties properties = new Properties();
    properties.setProperty("aMethodThatDoesNotExist", "[cacheProfileId=main]");

    this.attributeSource.setProperties(properties);

    // execute the method to test.
    Cached actualCachingAttribute = this.attributeSource.getCachingAttribute(
        this.method, this.method.getDeclaringClass());

    assertNull("The caching attribute should be null", actualCachingAttribute);
  }
}