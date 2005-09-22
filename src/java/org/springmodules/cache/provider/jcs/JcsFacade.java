/* 
 * Created on Nov 10, 2004
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

package org.springmodules.cache.provider.jcs;

import java.beans.PropertyEditor;
import java.io.Serializable;

import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;
import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.FatalCacheException;

/**
 * <p>
 * Facade for JCS.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.21 $ $Date: 2005/09/22 10:03:44 $
 */
public final class JcsFacade extends AbstractCacheProviderFacade {

  /**
   * JCS cache manager.
   */
  private CompositeCacheManager cacheManager;

  public JcsFacade() {
    super();
  }

  /**
   * @param cacheName
   *          the name of the cache.
   * @return the cache retrieved from the cache manager.
   * @throws CacheNotFoundException
   *           if the cache does not exist.
   */
  protected CompositeCache getCache(String cacheName) {
    CompositeCache cache = cacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    return cache;
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheProfileEditor()
   */
  protected PropertyEditor getCacheProfileEditor() {
    CacheProfileEditor editor = new CacheProfileEditor();
    editor.setCacheProfileClass(JcsProfile.class);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheProfileValidator()
   * @see JcsProfileValidator#validateCacheProfile(Object)
   */
  protected CacheProfileValidator getCacheProfileValidator() {
    return new JcsProfileValidator();
  }

  /**
   * Returns the key of a cache entry.
   * 
   * @param cacheKey
   *          the generated key.
   * @param profile
   *          the the cache profile that specifies how to retrieve or store an
   *          entry.
   * @return the key of a cache entry.
   */
  protected Serializable getKey(Serializable cacheKey, JcsProfile profile) {
    Serializable key = cacheKey;

    String group = profile.getGroup();
    if (StringUtils.hasText(group)) {
      GroupId groupId = new GroupId(profile.getCacheName(), group);
      GroupAttrName groupAttrName = new GroupAttrName(groupId, cacheKey);
      key = groupAttrName;
    }

    return key;
  }

  /**
   * @see AbstractCacheProviderFacade#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    return true;
  }

  /**
   * @see AbstractCacheProviderFacade#onFlushCache(CacheProfile)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onFlushCache(CacheProfile cacheProfile) throws CacheException {
    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    CompositeCache cache = getCache(cacheName);

    String cacheGroup = profile.getGroup();

    try {
      if (StringUtils.hasText(cacheGroup)) {
        GroupId groupId = new GroupId(cacheName, cacheGroup);
        cache.remove(groupId);

      } else {
        cache.removeAll();
      }

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onGetFromCache(Serializable,
   *      CacheProfile)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws CacheException {

    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable key = getKey(cacheKey, profile);
    Object cachedObject = null;

    try {
      ICacheElement cacheElement = cache.get(key);
      if (cacheElement != null) {
        cachedObject = cacheElement.getVal();
      }

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }

    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacade#onPutInCache(Serializable,
   *      CacheProfile, Object)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onPutInCache(Serializable cacheKey, CacheProfile cacheProfile,
      Object objectToCache) throws CacheException {

    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable key = getKey(cacheKey, profile);
    ICacheElement newCacheElement = new CacheElement(cache.getCacheName(), key,
        objectToCache);

    IElementAttributes elementAttributes = cache.getElementAttributes().copy();
    newCacheElement.setElementAttributes(elementAttributes);

    try {
      cache.update(newCacheElement);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CacheProfile)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onRemoveFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws CacheException {

    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable key = getKey(cacheKey, profile);

    try {
      cache.remove(key);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  public void setCacheManager(CompositeCacheManager newCacheManager) {
    cacheManager = newCacheManager;
  }

  /**
   * @see AbstractCacheProviderFacade#validateCacheManager()
   * 
   * @throws FatalCacheException
   *           if the cache manager is <code>null</code>.
   */
  protected void validateCacheManager() throws FatalCacheException {
    assertCacheManagerIsNotNull(cacheManager);
  }

}