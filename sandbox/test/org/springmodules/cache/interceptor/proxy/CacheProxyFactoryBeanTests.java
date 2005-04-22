/* 
 * Created on Feb 16, 2005
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

package org.springmodules.cache.interceptor.proxy;

import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.target.EmptyTargetSource;
import org.springmodules.cache.integration.Cacheable;
import org.springmodules.cache.integration.CacheableImpl;

/**
 * <p>
 * Unit Test for <code>{@link CacheProxyFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:18:57 $
 */
public final class CacheProxyFactoryBeanTests extends TestCase {

  /**
   * Attributes for flushing the cache.
   */
  private Properties cacheFlushAttributes;

  /**
   * Primary object (instance of the class to test).
   */
  private CacheProxyFactoryBean cacheProxyFactoryBean;

  /**
   * Attributes for caching.
   */
  private Properties cachingAttributes;

  /**
   * Target of the proxy factory.
   */
  private CacheableImpl target;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public CacheProxyFactoryBeanTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheProxyFactoryBean = new CacheProxyFactoryBean();

    this.cacheFlushAttributes = new Properties();
    this.cacheFlushAttributes.setProperty("update*", "[cacheProfileIds=test]");
    this.cacheProxyFactoryBean
        .setCacheFlushAttributes(this.cacheFlushAttributes);

    this.cachingAttributes = new Properties();
    this.cachingAttributes.setProperty("get*", "[cacheProfileId=main]");
    this.cacheProxyFactoryBean.setCachingAttributes(this.cachingAttributes);

    this.target = new CacheableImpl();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#afterPropertiesSet()}</code> throws an
   * 'IllegalStateException' if the target is not set.
   */
  public void testAfterPropertiesSetWithNullTarget() {
    try {
      this.cacheProxyFactoryBean.afterPropertiesSet();
      fail("An 'IllegalStateException' should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#afterPropertiesSet()}</code> throws an
   * 'AopConfigException' if the proxy interfaces are <code>null</code>, the
   * flag 'proxyTargetClass' is <code>false</code> and the target is an
   * instance of <code>org.springframework.aop.TargetSource</code>.
   */
  public void testAfterPropertiesSetWithProxyInterfacesEqualToNullAndProxyTargetFlagEqualToFalseAndTargetInstanceOfTargetSource()
      throws Exception {
    this.cacheProxyFactoryBean.setProxyTargetClass(false);
    Object targetInstanceOfTargetSource = EmptyTargetSource.INSTANCE;
    this.cacheProxyFactoryBean.setTarget(targetInstanceOfTargetSource);

    try {
      this.cacheProxyFactoryBean.afterPropertiesSet();
      fail("An 'AopConfigException' should have been thrown");
    } catch (AopConfigException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#createTargetSource(Object)}</code>
   * returns the same object sent as argument if such argument is an instance of
   * <code>org.springframework.aop.TargetSource</code>.
   */
  public void testCreateTargetSourceWithTargetObjectInstanceOfTargetSource() {
    Object targetObject = EmptyTargetSource.INSTANCE;

    TargetSource targetSource = this.cacheProxyFactoryBean
        .createTargetSource(targetObject);

    assertSame("<Target source>", targetObject, targetSource);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#createTargetSource(Object)}</code>
   * creates a new <code>org.springframework.aop.TargetSource</code> setting
   * the given object as its target.
   */
  public void testCreateTargetSourceWithTargetObjectNotInstanceOfTargetSource()
      throws Exception {
    Object targetObject = new Object();

    TargetSource targetSource = this.cacheProxyFactoryBean
        .createTargetSource(targetObject);
    Object actualTarget = targetSource.getTarget();

    assertSame("<Target>", targetObject, actualTarget);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObject()}</code> returns the
   * created proxy.
   */
  public void testGetObject() {
    this.cacheProxyFactoryBean.setTarget(this.target);
    this.cacheProxyFactoryBean.afterPropertiesSet();

    Object expectedProxy = this.cacheProxyFactoryBean.getProxy();
    Object actualProxy = this.cacheProxyFactoryBean.getObject();

    assertSame("<Proxy>", expectedProxy, actualProxy);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns the
   * class of the proxy if the proxy is not <code>null</code>.
   */
  public void testGetObjectTypeWhenProxyIsNotNull() {
    this.cacheProxyFactoryBean.setTarget(this.target);
    this.cacheProxyFactoryBean.afterPropertiesSet();

    Class expectedObjectType = this.cacheProxyFactoryBean.getProxy().getClass();
    Class actualObjectType = this.cacheProxyFactoryBean.getObjectType();

    assertEquals("<Object type>", expectedObjectType, actualObjectType);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns the
   * class of the target if:
   * <ul>
   * <li>The proxy is <code>null</code>.</li>
   * <li>The target is not <code>null</code> and it is an instance of
   * <code>org.springframework.aop.TargetSource</code>.</li>
   * </ul>
   */
  public void testGetObjectTypeWhenProxyIsNullAndTargetIsNotNullAndTargetIsInstanceOfTargetSource() {
    TargetSource instanceOfTargetSource = EmptyTargetSource.INSTANCE;
    this.cacheProxyFactoryBean.setTarget(instanceOfTargetSource);

    // verify that the proxy is null before running the method to test.
    assertNull("The proxy should be null", this.cacheProxyFactoryBean
        .getProxy());

    Class expectedObjectType = instanceOfTargetSource.getClass();
    Class actualObjectType = this.cacheProxyFactoryBean.getObjectType();

    assertEquals("<Object type>", expectedObjectType, actualObjectType);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns
   * <code>null</code> if:
   * <ul>
   * <li>The proxy is <code>null</code>.</li>
   * <li>The target is not <code>null</code> and it is not an instance of
   * <code>org.springframework.aop.TargetSource</code>.</li>
   * </ul>
   */
  public void testGetObjectTypeWhenProxyIsNullAndTargetIsNotNullAndTargetIsNotInstanceOfTargetSource() {
    this.cacheProxyFactoryBean.setTarget(this.target);

    // verify that the proxy is null before running the method to test.
    assertNull("The proxy should be null", this.cacheProxyFactoryBean
        .getProxy());

    Class actualObjectType = this.cacheProxyFactoryBean.getObjectType();

    assertNull("The object type should be null", actualObjectType);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#getObjectType()}</code> returns
   * <code>null</code> if both the proxy and the target are <code>null</code>.
   */
  public void testGetObjectTypeWhenProxyIsNullAndTargetIsNull() {
    // verify that the proxy is null before running the method to test.
    assertNull("The proxy should be null", this.cacheProxyFactoryBean
        .getProxy());

    Class actualObjectType = this.cacheProxyFactoryBean.getObjectType();

    assertNull("The object type should be null", actualObjectType);
  }

  /**
   * Verifies that <code>{@link CacheProxyFactoryBean}</code> notifies the
   * Spring IoC container that is a singleton.
   */
  public void testIsSingleton() {
    assertTrue("The proxy factory should be a singleton",
        this.cacheProxyFactoryBean.isSingleton());
  }

  /**
   * Verifies that the method
   * <code>{@link CacheProxyFactoryBean#setProxyInterfaces(String[])}</code>
   * gets the classes of the given names of interfaces.
   */
  public void testSetProxyInterfaces() throws Exception {
    String[] interfaceNames = new String[] { "org.springmodules.cache.integration.Cacheable" };

    this.cacheProxyFactoryBean.setProxyInterfaces(interfaceNames);

    Class[] expectedProxyInterfaces = new Class[] { Cacheable.class };
    Class[] actualProxyInterfaces = this.cacheProxyFactoryBean
        .getProxyInterfaces();

    assertEquals("<Number of proxy interfaces>",
        expectedProxyInterfaces.length, actualProxyInterfaces.length);

    Class expectedProxyInterface = expectedProxyInterfaces[0];
    Class actualProxyInterface = actualProxyInterfaces[0];

    assertEquals("<Proxy interface>", expectedProxyInterface,
        actualProxyInterface);
  }

}