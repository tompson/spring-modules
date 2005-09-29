/* 
 * Created on Jan 17, 2005
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

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link CachingAttributeEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/29 01:22:06 $
 */
public final class CachingAttributeEditorTests extends TestCase {

  private CachingAttributeEditor editor;

  public CachingAttributeEditorTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    editor = new CachingAttributeEditor();
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeEditor#setAsText(String)}</code> creates a
   * new instance of <code>{@link Cached}</code> using the properties
   * specified in the given String.
   */
  public void testSetAsTextWithNotEmptyString() {
    String cacheModelId = "[cacheModelId=main]";

    Cached expectedCached = new Cached();
    expectedCached.setCacheModelId("main");

    editor.setAsText(cacheModelId);
    Object actualCached = editor.getValue();

    assertEquals("<Caching attribute>", expectedCached, actualCached);
  }
}