/* 
 * Created on Oct 18, 2004
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

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springmodules.cache.provider.AbstractCacheManagerFactoryBeanTests;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Unit Test for <code>{@link OscacheManagerFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:49 $
 */
public final class OscacheManagerFactoryBeanTests extends
    AbstractCacheManagerFactoryBeanTests {

  /**
   * Property of the cache configuration file that returns the capacity of the
   * cache.
   */
  private static final String CACHE_CAPACITY_PROPERTY_NAME = "cache.capacity";

  /**
   * Message logger.
   */
  private static Log logger = LogFactory
      .getLog(OscacheManagerFactoryBeanTests.class);

  /**
   * Primary object *instance of the class to test).
   */
  private OscacheManagerFactoryBean cacheManagerFactoryBean;

  /**
   * Contains the location of the configuration file for
   * <code>{@link #cacheManagerFactoryBean}</code>.
   */
  private ClassPathResource configLocation;

  /**
   * Configuration properties read from <code>{@link #configLocation}</code>.
   */
  private Properties configProperties;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public OscacheManagerFactoryBeanTests(String name) {

    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.configLocation = new ClassPathResource(super.getPackageNameAsPath()
        + "/oscache-config.properties");

    this.cacheManagerFactoryBean = new OscacheManagerFactoryBean();
    this.cacheManagerFactoryBean.setConfigLocation(this.configLocation);

    this.setUpConfigProperties();
  }

  /**
   * Sets up <code>{@link #configProperties}</code>.
   */
  protected void setUpConfigProperties() throws Exception {

    InputStream inputStream = this.configLocation.getInputStream();
    this.configProperties = new Properties();
    this.configProperties.load(inputStream);
  }

  /**
   * Cleans up resources.
   */
  protected void tearDown() {
    if (this.cacheManagerFactoryBean != null) {
      try {
        this.cacheManagerFactoryBean.destroy();
      } catch (Exception exception) {
        // ignore the exception.
      } // end 'catch'
    } // end 'if'
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheManagerFactoryBean#afterPropertiesSet()}</code>
   * creates a new cache manager which properties are loaded from a
   * configuration file.
   */
  public void testAfterPropertiesSet() throws Exception {
    this.cacheManagerFactoryBean.afterPropertiesSet();

    GeneralCacheAdministrator cacheAdministrator = (GeneralCacheAdministrator) this.cacheManagerFactoryBean
        .getObject();

    assertNotNull("The cache manager should not be null", cacheAdministrator);

    // if this property was set from the properties file, we can safely assume
    // the rest of properties were set too.
    String expectedCacheCapacity = this.configProperties
        .getProperty(CACHE_CAPACITY_PROPERTY_NAME);

    String actualCacheCapacity = cacheAdministrator
        .getProperty(CACHE_CAPACITY_PROPERTY_NAME);

    assertEquals("<Cache capacity>", expectedCacheCapacity, actualCacheCapacity);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheManagerFactoryBean#afterPropertiesSet()}</code>
   * creates a new cache manager using the default configuration file,
   * "oscache.properties", if there is not any configuration file specified.
   */
  public void testAfterPropertiesSetWithConfigLocationEqualToNull()
      throws Exception {

    this.configLocation = new ClassPathResource("oscache.properties");
    this.setUpConfigProperties();

    this.cacheManagerFactoryBean.setConfigLocation(null);
    this.cacheManagerFactoryBean.afterPropertiesSet();
    GeneralCacheAdministrator cacheAdministrator = (GeneralCacheAdministrator) this.cacheManagerFactoryBean
        .getObject();

    assertNotNull("The cache manager should not be null", cacheAdministrator);

    // if this property was set from the properties file, we can safely assume
    // the rest of properties were set too.
    String expectedCacheCapacity = this.configProperties
        .getProperty(CACHE_CAPACITY_PROPERTY_NAME);

    String actualCacheCapacity = cacheAdministrator
        .getProperty(CACHE_CAPACITY_PROPERTY_NAME);
    logger.debug("actualCacheCapacity: " + actualCacheCapacity);

    assertEquals("<Cache capacity>", expectedCacheCapacity, actualCacheCapacity);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheManagerFactoryBean#destroy()}</code> flushes the
   * cache.
   */
  public void testDestroy() throws Exception {
    this.cacheManagerFactoryBean.afterPropertiesSet();

    GeneralCacheAdministrator cacheManager = (GeneralCacheAdministrator) this.cacheManagerFactoryBean
        .getObject();

    String key = "javaGuy";
    String entry = "James Gosling";
    cacheManager.putInCache(key, entry);
    Object cachedObject = cacheManager.getFromCache(key);
    assertSame("<Cached object>", entry, cachedObject);

    this.cacheManagerFactoryBean.destroy();

    try {
      cacheManager.getFromCache(key);
      fail("There should not be any cached objects");
    } catch (NeedsRefreshException needsRefreshException) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheManagerFactoryBean#destroy()}</code> does not throw
   * any exception if the cache manager is <code>null</code>.
   */
  public void testDestroyWithCacheManagerEqualToNull() throws Exception {
    // verify that the cache manager is null before calling 'destroy()'
    assertNull("The cache manager should be null", this.cacheManagerFactoryBean
        .getObject());

    try {
      this.cacheManagerFactoryBean.destroy();
    } catch (Throwable throwable) {
      fail("No exception should have been thrown");
    }

    // verify that the cache manager is null after calling 'destroy()'
    assertNull("The cache manager should be null", this.cacheManagerFactoryBean
        .getObject());
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheManagerFactoryBean#getObjectType()}</code> returns
   * the class of the created cache manager.
   */
  public void testGetObjectType() throws Exception {
    Class expectedObjectType = GeneralCacheAdministrator.class;

    // test when the cache manager has been created.
    this.cacheManagerFactoryBean.afterPropertiesSet();

    Class actualObjectType = this.cacheManagerFactoryBean.getObjectType();
    assertEquals("<Object type>", expectedObjectType, actualObjectType);

  }

  /**
   * Verifies that the method
   * <code>{@link OscacheManagerFactoryBean#getObjectType()}</code> returns
   * the class 'com.opensymphony.oscache.general.GeneralCacheAdministrator' if
   * the cache manager has not been created yet.
   */
  public void testGetObjectTypeWhenCacheAdministratorIsNull() throws Exception {

    Class expectedObjectType = GeneralCacheAdministrator.class;

    // test when the cache manager has not been created yet.
    Class actualObjectType = this.cacheManagerFactoryBean.getObjectType();

    assertEquals("<object type>", expectedObjectType, actualObjectType);
  }

  /**
   * Verifies that <code>{@link OscacheManagerFactoryBean}</code> notifies the
   * Spring IoC container that is a singleton.
   */
  public void testIsSingleton() {

    assertTrue("The CacheAdministrator should be a singleton",
        this.cacheManagerFactoryBean.isSingleton());
  }
}