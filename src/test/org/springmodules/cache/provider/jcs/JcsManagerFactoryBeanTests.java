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

package org.springmodules.cache.provider.jcs;

import java.io.InputStream;
import java.util.Properties;

import org.apache.jcs.engine.behavior.ICompositeCacheAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.cache.provider.AbstractCacheManagerFactoryBeanTests;

/**
 * <p>
 * Unit Tests for <code>{@link JcsManagerFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/09 02:18:55 $
 */
public final class JcsManagerFactoryBeanTests extends
    AbstractCacheManagerFactoryBeanTests {

  private String alternativeCacheConfigurationFilePath;

  private JcsManagerFactoryBean cacheManagerFactoryBean;

  /**
   * Contains the location of the configuration file for
   * <code>{@link #cacheManagerFactoryBean}</code>.
   */
  private Resource configLocation;

  /**
   * Configuration properties read from <code>{@link #configLocation}</code>.
   */
  private Properties configProperties;

  public JcsManagerFactoryBeanTests(String name) {
    super(name);
  }

  private void assertCacheManagerWasConfigured(String cacheName)
      throws Exception {
    // verify that the cache manager was configured.
    CompositeCacheManager cacheManager = getCacheManager();

    assertNotNull(cacheManager);

    // verify that the properties of the configuration file are the same as the
    // ones of the cache manager.
    String maxObjects = configProperties.getProperty("jcs.region." + cacheName
        + ".cacheattributes.MaxObjects");
    int expectedMaxObjects = Integer.parseInt(maxObjects);

    CompositeCache cache = cacheManager.getCache(cacheName);
    ICompositeCacheAttributes cacheAttributes = cache.getCacheAttributes();
    int actualMaxObjects = cacheAttributes.getMaxObjects();

    assertEqualsPropertyMaxObjects(expectedMaxObjects, actualMaxObjects);
  }

  private void assertEqualsPropertyMaxObjects(int expected, int actual) {
    assertEquals("<Property 'maxObjects'>", expected, actual);
  }

  private void assertObjectTypeIsCorrect() {
    Class actualObjectType = cacheManagerFactoryBean.getObjectType();
    assertEquals(CompositeCacheManager.class, actualObjectType);
  }

  private CompositeCacheManager getCacheManager() {
    return (CompositeCacheManager) cacheManagerFactoryBean.getObject();
  }

  protected void setUp() throws Exception {
    super.setUp();
    alternativeCacheConfigurationFilePath = getPackageNameAsPath()
        + "/jcs-config.ccf";
    cacheManagerFactoryBean = new JcsManagerFactoryBean();
  }

  private void setUpConfigurationProperties() throws Exception {
    this.setUpConfigurationProperties(alternativeCacheConfigurationFilePath);
  }

  private void setUpConfigurationProperties(String configurationFileName)
      throws Exception {

    configLocation = new ClassPathResource(configurationFileName);
    cacheManagerFactoryBean.setConfigLocation(configLocation);

    InputStream inputStream = configLocation.getInputStream();
    configProperties = new Properties();
    configProperties.load(inputStream);
  }

  protected void tearDown() {
    if (cacheManagerFactoryBean != null) {
      try {
        cacheManagerFactoryBean.destroy();
      } catch (Exception exception) {
        // ignore the exception.
      }
    }
  }

  /**
   * Verifies that the method
   * <code>{@link JcsManagerFactoryBean#afterPropertiesSet()}</code> creates a
   * new cache manager which properties are loaded from a configuration file.
   */
  public void testAfterPropertiesSet() throws Exception {
    setUpConfigurationProperties();
    cacheManagerFactoryBean.afterPropertiesSet();
    assertCacheManagerWasConfigured("altTestCache");
  }

  /**
   * Verifies that the method
   * <code>{@link JcsManagerFactoryBean#afterPropertiesSet()}</code> creates a
   * new cache manager using the default configuration file, "cache.ccf", if
   * there is not any configuration file specified.
   */
  public void testAfterPropertiesSetWithConfigLocationEqualToNull()
      throws Exception {
    setUpConfigurationProperties("cache.ccf");
    cacheManagerFactoryBean.setConfigLocation(null);

    cacheManagerFactoryBean.afterPropertiesSet();

    assertCacheManagerWasConfigured("testCache");
  }

  public void testDestroy() throws Exception {
    setUpConfigurationProperties();

    cacheManagerFactoryBean.afterPropertiesSet();

    CompositeCacheManager cacheManager = getCacheManager();

    assertTrue("There should be at least one cache in the cache manager",
        cacheManager.getCacheNames().length > 0);

    cacheManagerFactoryBean.destroy();

    assertTrue("There should not be any cache in the cache manager",
        cacheManager.getCacheNames().length == 0);
  }

  public void testDestroyWithCacheManagerEqualToNull() throws Exception {
    assertNull(cacheManagerFactoryBean.getObject());

    try {
      cacheManagerFactoryBean.destroy();
    } catch (Throwable throwable) {
      fail();
    }

    assertNull(cacheManagerFactoryBean.getObject());
  }

  public void testGetObjectType() throws Exception {
    // test when the cache manager has been created.
    cacheManagerFactoryBean.afterPropertiesSet();

    assertObjectTypeIsCorrect();
  }

  public void testGetObjectTypeWhenCacheManagerIsNull() {
    // test when the cache manager has not been created yet.
    assertObjectTypeIsCorrect();
  }

  public void testIsSingleton() {
    assertTrue(cacheManagerFactoryBean.isSingleton());
  }

}