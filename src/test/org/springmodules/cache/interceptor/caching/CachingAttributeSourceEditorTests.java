/* 
 * Created on Apr 6, 2005
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.springmodules.cache.interceptor.SimulatedService;

/**
 * <p>
 * Unit Tests for <code>{@link CachingAttributeSourceEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/09/06 01:41:32 $
 */
public class CachingAttributeSourceEditorTests extends TestCase {

  public static final String LINE_SEPARATOR = System
      .getProperty("line.separator");

  private CachingAttributeSourceEditor editor;

  /**
   * Reference to the class <code>{@link SimulatedService}</code>.
   */
  private Class targetClass;

  public CachingAttributeSourceEditorTests(String name) {
    super(name);
  }

  private void assertCachingAttributeSourceEditorDidNotCreateAnyObject() {
    assertNull("The PropertyEditor should not create any object", this.editor
        .getValue());
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.editor = new CachingAttributeSourceEditor();
    this.targetClass = SimulatedService.class;
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeSourceEditor#setAsText(String)}</code>
   * creates a new instance of
   * <code>{@link MethodMapCachingAttributeSource}</code> by parsing the given
   * text.
   */
  public void testSetAsText() throws Exception {
    String myCacheAttrName = "myCache";
    String myOtherCacheAttrName = "myOtherCache";

    Map expectedAdvisedMethods = new HashMap();
    expectedAdvisedMethods.put(this.targetClass.getName() + ".get*",
        "[cacheProfileId=" + myCacheAttrName + "]");
    expectedAdvisedMethods.put(this.targetClass.getName() + ".getPersonName",
        "[cacheProfileId=" + myOtherCacheAttrName + "]");

    // build the text to be used to create a MethodMapCachingAttributeSource.
    StringBuffer buffer = new StringBuffer();
    Set entrySet = expectedAdvisedMethods.entrySet();
    Iterator entrySetIterator = entrySet.iterator();
    while (entrySetIterator.hasNext()) {
      Map.Entry entry = (Map.Entry) entrySetIterator.next();
      buffer.append(entry.getKey());
      buffer.append("=");
      buffer.append(entry.getValue());
      buffer.append(LINE_SEPARATOR);
    }
    String text = buffer.toString();

    // execute the method to test.
    this.editor.setAsText(text);

    Object value = this.editor.getValue();
    MethodMapCachingAttributeSource cachingAttributeSource = (MethodMapCachingAttributeSource) value;

    Map actualAttributeMap = cachingAttributeSource.getAttributeMap();

    // verify that the methods 'getPersonName' and 'getPersons' are advised.
    Class clazz = SimulatedService.class;
    Method getPersonNameMethod = clazz.getDeclaredMethod("getPersonName",
        new Class[] { long.class });
    Method getPersonsMethod = clazz.getDeclaredMethod("getPersons", null);

    Cached myCacheAttr = (Cached) actualAttributeMap.get(getPersonsMethod);
    assertEquals("<Cache profile id>", myCacheAttrName, myCacheAttr
        .getCacheProfileId());

    Cached myOtherCacheAttr = (Cached) actualAttributeMap
        .get(getPersonNameMethod);
    assertEquals("<Cache profile id>", myOtherCacheAttrName, myOtherCacheAttr
        .getCacheProfileId());
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeSourceEditor#setAsText(String)}</code> does
   * not create any <code>{@link MethodMapCachingAttributeSource}</code> if
   * the given text is <code>null</code>.
   */
  public void testSetAsTextWithEmptyText() {
    this.editor.setAsText("");
    assertCachingAttributeSourceEditorDidNotCreateAnyObject();
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeSourceEditor#setAsText(String)}</code> does
   * not create any <code>{@link MethodMapCachingAttributeSource}</code> if
   * the given text is <code>null</code>.
   */
  public void testSetAsTextWithTextEqualToNull() {
    this.editor.setAsText(null);
    assertCachingAttributeSourceEditorDidNotCreateAnyObject();
  }
}
