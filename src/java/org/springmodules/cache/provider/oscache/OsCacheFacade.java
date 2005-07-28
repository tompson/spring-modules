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

package org.springmodules.cache.provider.oscache;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.AbstractCacheProviderFacadeImpl;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Implementation of
 * <code>{@link org.springmodules.cache.provider.CacheProviderFacade}</code>
 * that uses OSCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/07/28 03:43:10 $
 */
public final class OsCacheFacade extends AbstractCacheProviderFacadeImpl {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(OsCacheFacade.class);

  /**
   * OSCache cache manager.
   */
  private GeneralCacheAdministrator cacheManager;

  /**
   * Constructor.
   */
  public OsCacheFacade() {
    super();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileEditor()
   */
  protected AbstractCacheProfileEditor getCacheProfileEditor() {
    return new OsCacheProfileEditor();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileValidator()
   * @see OsCacheProfileValidator#validateCacheProfile(Object)
   */
  protected CacheProfileValidator getCacheProfileValidator() {
    return new OsCacheProfileValidator();
  }

  /**
   * Returns the <code>String</code> representation of the given key.
   * 
   * @param cacheKey
   *          the cache key.
   * @return the <code>String</code> representation of <code>cacheKey</code>.
   */
  protected String getEntryKey(Serializable cacheKey) {
    return cacheKey.toString();
  }

  /**
   * 
   * @see AbstractCacheProviderFacadeImpl#onCancelCacheUpdate(Serializable)
   */
  protected void onCancelCacheUpdate(Serializable cacheKey) {
    String key = this.getEntryKey(cacheKey);
    this.cacheManager.cancelUpdate(key);
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)
   */
  protected void onFlushCache(CacheProfile cacheProfile) {
    OsCacheProfile profile = (OsCacheProfile) cacheProfile;
    String[] groups = profile.getGroups();

    if (groups == null || groups.length == 0) {
      this.cacheManager.flushAll();
    } else {
      int groupCount = groups.length;

      for (int i = 0; i < groupCount; i++) {
        String group = groups[i];
        this.cacheManager.flushGroup(group);
      }
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable,
   *      CacheProfile)
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) {
    OsCacheProfile profile = (OsCacheProfile) cacheProfile;

    Integer refreshPeriod = profile.getRefreshPeriod();
    String cronExpression = profile.getCronExpression();

    String key = this.getEntryKey(cacheKey);
    Object cachedObject = null;

    try {
      if (null == refreshPeriod) {
        cachedObject = this.cacheManager.getFromCache(key);
      } else if (null == cronExpression) {
        cachedObject = this.cacheManager.getFromCache(key, refreshPeriod
            .intValue());
      } else {
        cachedObject = this.cacheManager.getFromCache(key, refreshPeriod
            .intValue(), cronExpression);
      }
    } catch (NeedsRefreshException needsRefreshException) {
      // the cache does not have that entry.
      if (logger.isDebugEnabled()) {
        StringBuffer messageBuffer = new StringBuffer();
        messageBuffer.append("Method 'getFromCache(..)'. ");
        messageBuffer.append("Object not found in OSCache under key ");
        
        if (cacheKey instanceof String) {
          messageBuffer.append("'" + cacheKey + "'");
        } else {
          messageBuffer.append(cacheKey);
        }

        logger.debug(messageBuffer.toString());
      }
    }

    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onPutInCache(Serializable,
   *      CacheProfile, Object)
   */
  protected void onPutInCache(Serializable cacheKey, CacheProfile cacheProfile,
      Object objectToCache) {

    OsCacheProfile profile = (OsCacheProfile) cacheProfile;

    String key = this.getEntryKey(cacheKey);
    String[] groups = profile.getGroups();

    if (groups == null || groups.length == 0) {
      this.cacheManager.putInCache(key, objectToCache);
    } else {
      this.cacheManager.putInCache(key, objectToCache, groups);
    }
  }

  /**
   * @see org.springmodules.cache.provider.CacheProviderFacade#removeFromCache(Serializable,
   *      String)
   */
  public void removeFromCache(Serializable cacheKey, String cacheProfileId) {
    String key = this.getEntryKey(cacheKey);
    this.cacheManager.flushEntry(key);
  }

  /**
   * Setter for the field <code>{@link #cacheManager}</code>.
   * 
   * @param cacheManager
   *          the new value to set
   */
  public void setCacheManager(GeneralCacheAdministrator cacheManager) {
    this.cacheManager = cacheManager;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#validateCacheManager()
   * 
   * @throws IllegalStateException
   *           if the cache manager is <code>null</code>.
   */
  protected void validateCacheManager() {
    if (null == this.cacheManager) {
      throw new IllegalStateException("The Cache Manager should not be null");
    }
  }
}