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

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.util.ArrayUtils;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Facade for OSCache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.14 $ $Date: 2005/10/13 04:52:15 $
 */
public final class OsCacheFacade extends AbstractCacheProviderFacade {

  /**
   * OSCache cache manager.
   */
  private GeneralCacheAdministrator cacheManager;

  private CacheModelValidator cacheModelValidator;

  public OsCacheFacade() {
    super();
    cacheModelValidator = new OsCacheModelValidator();
  }

  public CacheModelValidator getCacheModelValidator() {
    return cacheModelValidator;
  }

  public PropertyEditor getCachingModelEditor() {
    Map propertyEditors = new HashMap();
    propertyEditors.put("refreshPeriod", new RefreshPeriodEditor());

    ReflectionCacheModelEditor editor = new ReflectionCacheModelEditor();
    editor.setCacheModelClass(OsCacheCachingModel.class);
    editor.setCacheModelPropertyEditors(propertyEditors);
    return editor;
  }

  /**
   * Returns the <code>String</code> representation of the given key.
   * 
   * @param key
   *          the cache key.
   * @return the <code>String</code> representation of <code>cacheKey</code>.
   */
  protected String getEntryKey(Serializable key) {
    return key.toString();
  }

  public PropertyEditor getFlushingModelEditor() {
    ReflectionCacheModelEditor editor = new ReflectionCacheModelEditor();
    editor.setCacheModelClass(OsCacheFlushingModel.class);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacade#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    return false;
  }

  /**
   * 
   * @see AbstractCacheProviderFacade#onCancelCacheUpdate(Serializable)
   */
  protected void onCancelCacheUpdate(Serializable key) {
    String newKey = getEntryKey(key);
    cacheManager.cancelUpdate(newKey);
  }

  /**
   * @see AbstractCacheProviderFacade#onFlushCache(FlushingModel)
   */
  protected void onFlushCache(FlushingModel model) {
    OsCacheFlushingModel cachingModel = (OsCacheFlushingModel) model;
    String[] groups = cachingModel.getGroups();

    if (ArrayUtils.hasElements(groups)) {
      int groupCount = groups.length;

      for (int i = 0; i < groupCount; i++) {
        String group = groups[i];
        cacheManager.flushGroup(group);
      }

    } else {
      cacheManager.flushAll();
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onGetFromCache(Serializable, CachingModel)
   */
  protected Object onGetFromCache(Serializable key, CachingModel model) {
    OsCacheCachingModel cachingModel = (OsCacheCachingModel) model;

    Integer refreshPeriod = cachingModel.getRefreshPeriod();
    String cronExpression = cachingModel.getCronExpression();

    String newKey = getEntryKey(key);
    Object cachedObject = null;

    try {
      if (null == refreshPeriod) {
        cachedObject = cacheManager.getFromCache(newKey);

      } else if (null == cronExpression) {
        cachedObject = cacheManager.getFromCache(newKey, refreshPeriod
            .intValue());

      } else {
        cachedObject = cacheManager.getFromCache(newKey, refreshPeriod
            .intValue(), cronExpression);
      }
    } catch (NeedsRefreshException needsRefreshException) {
      // the cache does not have that entry. Ignore the exception.
    }

    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacade#onPutInCache(Serializable, CachingModel,
   *      Object)
   */
  protected void onPutInCache(Serializable key, CachingModel model, Object obj) {
    OsCacheCachingModel cachingModel = (OsCacheCachingModel) model;

    String newKey = getEntryKey(key);
    String[] groups = cachingModel.getGroups();

    if (groups == null || groups.length == 0) {
      cacheManager.putInCache(newKey, obj);

    } else {
      cacheManager.putInCache(newKey, obj, groups);
    }
  }

  /**
   * 
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CachingModel)
   */
  protected void onRemoveFromCache(Serializable key, CachingModel model) {
    String newKey = getEntryKey(key);
    cacheManager.flushEntry(newKey);
  }

  public void setCacheManager(GeneralCacheAdministrator newCacheManager) {
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