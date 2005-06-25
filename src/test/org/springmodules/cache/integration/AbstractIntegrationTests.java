/* 
 * Created on Oct 22, 2004
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

package org.springmodules.cache.integration;

import java.util.Collections;
import java.util.Map;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.util.StringUtils;
import org.springmodules.cache.interceptor.caching.CachingAspectSupport;
import org.springmodules.cache.provider.AbstractCacheProviderFacadeImpl;

/**
 * <p>
 * Template for integration test cases that test that caching and cache-flushing
 * work correctly using a Spring bean context.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/06/25 06:53:19 $
 */
public abstract class AbstractIntegrationTests extends
    AbstractDependencyInjectionSpringContextTests {

  /**
   * Listener that stores the keys used to store objects in the cache. This
   * listener is used to verify that objects are being cached.
   */
  private KeyCollectionListener entryStoredListener;

  /**
   * Bean managed by the Spring bean context. Should be advised by the
   * interceptors that perform caching and flush the cache.
   */
  private Cacheable target;

  /**
   * Constructor.
   */
  public AbstractIntegrationTests() {
    super();
  }

  /**
   * Asserts that the cache (or part(s) of the cache, depending on the cache
   * provider) was flushed. This method depends on the implementation of the
   * cache provider used in the Spring bean context.
   */
  protected abstract void assertCacheWasFlushed() throws Exception;

  /**
   * Asserts that the cache profiles were configured correctly in the Spring
   * bean context.
   * 
   * @param cacheProfiles
   *          the cache profiles to verify. It is an unmodifiable map.
   */
  protected abstract void assertCorrectCacheProfileConfiguration(
      Map cacheProfiles);

  /**
   * Asserts that the object expected to be cached is actually cached. This
   * method depends on the implementation of the cache provider used in the
   * Spring bean context.
   * 
   * @param expectedCachedObject
   *          the object that should have been cached.
   * @param keyIndex
   *          the index of the key stored in <code>entryStoredListener</code>.
   */
  protected abstract void assertObjectWasCached(Object expectedCachedObject,
      int keyIndex) throws Exception;

  /**
   * Getter for <code>{@link #entryStoredListener}</code>.
   * 
   * @return the value of the member variable <code>listener</code>.
   */
  protected final KeyCollectionListener getEntryStoredListener() {
    return this.entryStoredListener;
  }

  /**
   * Sets up <code>{@link #entryStoredListener}</code>.
   */
  private void setUpEntryStoredListener() {
    this.entryStoredListener = (KeyCollectionListener) super.applicationContext
        .getBean("listener");
  }

  /**
   * Sets up <code>{@link #target}</code>.
   */
  private void setUpTarget() {
    this.target = (Cacheable) super.applicationContext.getBean("cacheable");
  }

  /**
   * Verifies that the cache profiles were configured correctly in the Spring
   * bean context.
   */
  public final void testCacheProfileConfiguration() {
    // get the cache profiles.
    AbstractCacheProviderFacadeImpl cacheProviderFacade = (AbstractCacheProviderFacadeImpl) super.applicationContext
        .getBean("cacheProvider");
    Map cacheProfiles = cacheProviderFacade.getCacheProfiles();

    // there should be cache profiles.
    assertNotNull("The map of cache profiles should not be null", cacheProfiles);
    assertFalse("The map of cache profiles should not be empty", cacheProfiles
        .isEmpty());

    // send an unmodifiable map as argument, to prevent its possible
    // modification by subclasses of this class.
    Map unmodifiableCacheProfileMap = Collections
        .unmodifiableMap(cacheProfiles);

    // verify that the cache profiles were configured correctly.
    this.assertCorrectCacheProfileConfiguration(unmodifiableCacheProfileMap);
  }

  /**
   * Verifies that the caching and the cache-flushing are working correctly.
   */
  public final void testCachingAndCacheFlushing() throws Exception {
    // get the "target" object from the Spring bean context.
    this.setUpTarget();

    // get the listener to verify that the caching is working correctly.
    this.setUpEntryStoredListener();

    // call the method 'getName(int)' which return value should be cached.
    if (super.logger.isDebugEnabled()) {
      super.logger.debug("Storing in the cache...");
    }

    int nameIndex = 0;

    String cachedObject = this.target.getName(nameIndex);
    assertTrue("The retrieved name should not be empty", StringUtils
        .hasText(cachedObject));

    // verify the return value of the called method was cached.
    this.assertObjectWasCached(cachedObject, nameIndex);

    // call the same method again. This time the return value should be read
    // from the cache.
    if (super.logger.isDebugEnabled()) {
      super.logger.debug("Reading from the cache...");
    }
    cachedObject = this.target.getName(nameIndex);

    // verify that the element was cached only once. There should be only
    // one caching event registered in the listener.
    int expectedTimesObjectWasCached = 1;
    int actualTimesObjectWasCached = this.entryStoredListener
        .getGeneratedKeys().size();
    assertEquals("<Number of times the same object was cached>",
        expectedTimesObjectWasCached, actualTimesObjectWasCached);

    // call the method 'updateName(int, String)'. When executed, the cache
    // (or part(s) of the cache, depending on the cache provider) should be
    // flushed.
    if (super.logger.isDebugEnabled()) {
      super.logger.debug("Flushing the cache ...");
    }
    this.target.updateName(nameIndex, "Rod Johnson");

    // verify that the cache was flushed.
    this.assertCacheWasFlushed();

    // NULL_ENTRY should be cached, and null should be returned.
    this.target.updateName(++nameIndex, null);
    cachedObject = this.target.getName(nameIndex);
    this.assertObjectWasCached(CachingAspectSupport.NULL_ENTRY, nameIndex);

    assertNull("The returned value should be null", cachedObject);
  }
}