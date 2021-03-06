/* 
 * Created on Mar 28, 2006
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.integration.jcs;

import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;

import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.provider.jcs.JcsCachingModel;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using JCS as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JcsIntegrationTests extends AbstractIntegrationTests {

  /**
   * JCS cache manager.
   */
  private CompositeCacheManager cacheManager;

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected final void assertCacheWasFlushed() {
    setUpCacheManager();

    int index = 0;
    ICacheElement cacheElement = getCacheElement(0);
    assertCacheEntryFromCacheIsNull(cacheElement, getCacheElement(index));
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected final void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) {
    setUpCacheManager();

    ICacheElement cacheElement = getCacheElement(keyAndModelIndex);
    assertEquals(expectedCachedObject, cacheElement.getVal());
  }

  private ICacheElement getCacheElement(int keyAndModelIndex) {
    KeyAndModel keyAndModel = getKeyAndModel(keyAndModelIndex);
    JcsCachingModel model = (JcsCachingModel) keyAndModel.model;

    String cacheName = model.getCacheName();
    CompositeCache cache = cacheManager.getCache(cacheName);
    GroupId groupId = new GroupId(cacheName, model.getGroup());
    GroupAttrName groupAttrName = new GroupAttrName(groupId, keyAndModel.key);
    return cache.get(groupAttrName);
  }

  private void setUpCacheManager() {
    cacheManager = (CompositeCacheManager) cacheManager();
  }

}