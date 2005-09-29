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

package org.springmodules.cache.interceptor.flush;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link CacheFlushAttributeEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/29 01:21:41 $
 */
public final class CacheFlushAttributeEditorTests extends TestCase {

  private CacheFlushAttributeEditor editor;

  public CacheFlushAttributeEditorTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.editor = new CacheFlushAttributeEditor();
  }

  public void testSetAsTextWithFlushBeforeExecutionEqualToTrue() {
    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test", true);
    String properties = "[cacheModelIds=main,test][flushBeforeExecution=true]";

    // execute the method to test.
    this.editor.setAsText(properties);

    FlushCache actualCacheFlushAttribute = (FlushCache) this.editor.getValue();
    assertEquals(expectedCacheFlushAttribute, actualCacheFlushAttribute);
  }

  public void testSetAsTextWithFlushBeforeExecutionEqualToYes() {
    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test", true);
    String properties = "[cacheModelIds=main,test][flushBeforeExecution=yes]";

    // execute the method to test.
    this.editor.setAsText(properties);

    FlushCache actualCacheFlushAttribute = (FlushCache) this.editor.getValue();
    assertEquals(expectedCacheFlushAttribute, actualCacheFlushAttribute);
  }

  public void testSetAsTextWithoutFlushBeforeExecution() {
    FlushCache expectedCacheFlushAttribute = new FlushCache("main,test");
    String properties = "[cacheModelIds=main,test]";

    // execute the method to test.
    this.editor.setAsText(properties);

    FlushCache actualCacheFlushAttribute = (FlushCache) this.editor.getValue();
    assertEquals(expectedCacheFlushAttribute, actualCacheFlushAttribute);
  }
}