/* 
 * Created on May 28, 2005
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
package org.springmodules.cache.provider.oscache;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.provider.CacheModelEditor;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.FatalCacheException;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.base.events.CacheEntryEventListener;
import com.opensymphony.oscache.extra.CacheEntryEventListenerImpl;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.12 $ $Date: 2005/09/29 01:22:08 $
 */
public class OsCacheFacadeTests extends TestCase {

  private static final String CACHE_KEY = "key";

  private static final String CACHE_MODEL_ID = "cacheModel";

  private GeneralCacheAdministrator cacheAdministrator;

  private MockClassControl cacheAdministratorControl;

  private CacheEntryEventListenerImpl cacheEntryEventListener;

  private OsCacheModel cacheModel;

  /**
   * Name of the groups in <code>{@link #cacheAdministrator}</code> to use.
   */
  private String[] groups;

  private OsCacheFacade osCacheFacade;

  public OsCacheFacadeTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    cacheModel = new OsCacheModel();

    groups = new String[] { "Empire", "Rebels" };

    Map cacheModels = new HashMap();
    cacheModels.put(CACHE_MODEL_ID, cacheModel);

    osCacheFacade = new OsCacheFacade();
    osCacheFacade.setCacheModels(cacheModels);
  }

  private void setUpCacheAdministrator() {
    cacheAdministrator = new GeneralCacheAdministrator();
    osCacheFacade.setCacheManager(cacheAdministrator);

    Cache cache = cacheAdministrator.getCache();

    cacheEntryEventListener = new CacheEntryEventListenerImpl();
    cache.addCacheEventListener(cacheEntryEventListener,
        CacheEntryEventListener.class);

    osCacheFacade.setCacheManager(cacheAdministrator);
  }

  private void setUpCacheAdministratorAsMockObject(Method methodToMock) {
    setUpCacheAdministratorAsMockObject(new Method[] { methodToMock });
  }

  private void setUpCacheAdministratorAsMockObject(Method[] methodsToMock) {
    Class targetClass = GeneralCacheAdministrator.class;

    cacheAdministratorControl = MockClassControl.createControl(targetClass,
        null, null, methodsToMock);
    cacheAdministrator = (GeneralCacheAdministrator) cacheAdministratorControl
        .getMock();

    osCacheFacade.setCacheManager(cacheAdministrator);
  }

  protected void tearDown() throws Exception {
    super.tearDown();

    if (cacheAdministrator != null) {
      cacheAdministrator.destroy();
    }
  }

  public void testGetCacheModelEditor() {
    PropertyEditor editor = osCacheFacade.getCacheModelEditor();

    assertNotNull(editor);
    assertEquals(CacheModelEditor.class, editor.getClass());

    CacheModelEditor modelEditor = (CacheModelEditor) editor;
    assertEquals(OsCacheModel.class, modelEditor.getCacheModelClass());

    Map cacheModelPropertyEditors = modelEditor.getCacheModelPropertyEditors();
    assertNotNull(cacheModelPropertyEditors);
    assertEquals(1, cacheModelPropertyEditors.size());

    PropertyEditor refreshPeriodEditor = (PropertyEditor) cacheModelPropertyEditors
        .get("refreshPeriod");
    assertNotNull(refreshPeriodEditor);
    assertEquals(RefreshPeriodEditor.class, refreshPeriodEditor.getClass());
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#getCacheModelValidator()}</code> returns an an
   * instance of <code>{@link OsCacheModelValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheModelValidator() {
    CacheModelValidator validator = osCacheFacade.getCacheModelValidator();

    assertNotNull(validator);

    assertEquals(OsCacheModelValidator.class, validator.getClass());
  }

  public void testIsSerializableCacheElementRequired() {
    assertFalse(osCacheFacade.isSerializableCacheElementRequired());
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onCancelCacheUpdate(java.io.Serializable)}</code>
   * cancels the update of the entry under the given key.
   */
  public void testOnCancelCacheUpdate() throws Exception {
    Method cancelUpdateMethod = GeneralCacheAdministrator.class.getMethod(
        "cancelUpdate", new Class[] { String.class });

    setUpCacheAdministratorAsMockObject(cancelUpdateMethod);

    String key = "Jedi";

    cacheAdministrator.cancelUpdate(key);
    cacheAdministratorControl.replay();

    // execute the method to test.
    osCacheFacade.cancelCacheUpdate(key);

    cacheAdministratorControl.verify();
  }

  public void testOnFlushCache() {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";
    cacheAdministrator.putInCache(CACHE_KEY, objectToStore, groups);

    String groupToFlush = groups[0];
    cacheModel.setGroups(new String[] { groupToFlush });

    // execute the method to test.
    osCacheFacade.onFlushCache(cacheModel);

    assertEquals("<Number of groups flushed>", 1, cacheEntryEventListener
        .getGroupFlushedCount());
  }

  public void testOnFlushCacheWithoutGroups() {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";
    cacheAdministrator.putInCache(CACHE_KEY, objectToStore, groups);

    cacheModel.setGroups((String[]) null);

    // execute the method to test.
    osCacheFacade.onFlushCache(cacheModel);

    String cachedObject = cacheAdministrator.getProperty(CACHE_KEY);
    assertNull(cachedObject);
  }

  public void testOnGetFromCache() {
    setUpCacheAdministrator();

    Object expected = "An Object";
    cacheAdministrator.putInCache(CACHE_KEY, expected);

    // execute the method to test.
    Object actual = osCacheFacade.onGetFromCache(CACHE_KEY, cacheModel);

    assertSame(expected, actual);
  }

  public void testOnGetFromCacheWhenKeyIsNotFound() {
    setUpCacheAdministrator();

    Object cachedObject = osCacheFacade.onGetFromCache("NonExistingKey",
        cacheModel);

    assertNull(cachedObject);
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNotNullAndCronExpressionIsNotNull()
      throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class,
            int.class, String.class });

    setUpCacheAdministratorAsMockObject(getFromCacheMethod);
    String cronExpression = "* * * 0 0";
    int refreshPeriod = 45;

    cacheModel.setCronExpression(cronExpression);
    cacheModel.setRefreshPeriod(refreshPeriod);
    Object expected = "Anakin";

    cacheAdministrator.getFromCache(CACHE_KEY, refreshPeriod, cronExpression);
    cacheAdministratorControl.setReturnValue(expected);

    cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = osCacheFacade.onGetFromCache(CACHE_KEY, cacheModel);

    assertSame(expected, actual);

    cacheAdministratorControl.verify();
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNotNullAndCronExpressionIsNull()
      throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class,
            int.class });

    setUpCacheAdministratorAsMockObject(getFromCacheMethod);
    int refreshPeriod = 556;

    cacheModel.setRefreshPeriod(refreshPeriod);
    Object expected = "Anakin";

    cacheAdministrator.getFromCache(CACHE_KEY, refreshPeriod);
    cacheAdministratorControl.setReturnValue(expected);

    cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = osCacheFacade.onGetFromCache(CACHE_KEY, cacheModel);

    assertSame(expected, actual);

    cacheAdministratorControl.verify();
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNull() throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class });

    setUpCacheAdministratorAsMockObject(getFromCacheMethod);

    cacheModel.setRefreshPeriod(null);
    Object expected = "Anakin";

    // retrieve an entry using only the provided key.
    cacheAdministrator.getFromCache(CACHE_KEY);
    cacheAdministratorControl.setReturnValue(expected);

    cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = osCacheFacade.onGetFromCache(CACHE_KEY, cacheModel);

    assertSame(expected, actual);

    cacheAdministratorControl.verify();
  }

  public void testOnPutInCacheWithGroups() throws Exception {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";

    String group = groups[0];
    cacheModel.setGroups(new String[] { group });

    // execute the method to test.
    osCacheFacade.onPutInCache(CACHE_KEY, cacheModel, objectToStore);

    Object cachedObject = cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame(objectToStore, cachedObject);

    // if we flush the group used, we should not be able to get the cached
    // object.
    cacheAdministrator.flushGroup(group);

    try {
      cacheAdministrator.getFromCache(CACHE_KEY);
      fail();
    } catch (NeedsRefreshException exception) {
      // we are expecting this exception
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheModel, Object)}</code>
   * stores an entry using the given key. The entry should not be associated
   * with any group.
   */
  public void testOnPutInCacheWithoutGroups() throws Exception {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";

    cacheModel.setGroups((String[]) null);

    // execute the method to test.
    osCacheFacade.onPutInCache(CACHE_KEY, cacheModel, objectToStore);

    Object cachedObject = cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame(objectToStore, cachedObject);

    // if we flush all the groups, we should be able to get the cached object.
    int groupCount = groups.length;
    for (int i = 0; i < groupCount; i++) {
      String group = groups[i];
      cacheAdministrator.flushGroup(group);
    }

    cachedObject = cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame(objectToStore, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onRemoveFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheModel)}</code>
   * removes from the cache the entry stored under the given key.
   */
  public void testOnRemoveFromCache() throws Exception {
    Method flushEntryMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("flushEntry", new Class[] { String.class });

    setUpCacheAdministratorAsMockObject(flushEntryMethod);

    String key = "Luke";

    cacheAdministrator.flushEntry(key);
    cacheAdministratorControl.replay();

    // execute the method to test.
    osCacheFacade.onRemoveFromCache(key, null);

    cacheAdministratorControl.verify();
  }

  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    osCacheFacade.setCacheManager(null);
    try {
      osCacheFacade.validateCacheManager();
      fail();
    } catch (FatalCacheException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the cache manager is not <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotEqualToNull()
      throws Exception {
    setUpCacheAdministrator();
    osCacheFacade.validateCacheManager();
  }
}
