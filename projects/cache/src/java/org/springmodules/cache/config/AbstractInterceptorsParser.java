/* 
 * Created on Feb 26, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.config;

import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import org.springmodules.cache.interceptor.caching.MethodMapCachingInterceptor;
import org.springmodules.cache.interceptor.flush.MethodMapFlushingInterceptor;

/**
 * <p>
 * Template that handles the parsing of the XML tag "interceptors". Creates and
 * registers instances of <code>{@link MethodMapCachingInterceptor}</code> and
 * <code>{@link MethodMapFlushingInterceptor}</code> which can be used with
 * <code>{@link org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator}</code>
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractInterceptorsParser extends
    AbstractCacheSetupStrategyParser {

  /**
   * Creates and registers instances
   * <code>{@link MethodMapCachingInterceptor}</code> and
   * <code>{@link MethodMapFlushingInterceptor}</code> by parsing the given
   * XML element.
   * 
   * @param element
   *          the XML element to parse
   * @param registry
   *          the registry of bean definitions
   * @param propertySource
   *          contains common properties for the different cache setup
   *          strategies
   * 
   * @see AbstractCacheSetupStrategyParser#parseCacheSetupStrategy(Element,
   *      BeanDefinitionRegistry, CacheSetupStrategyPropertySource)
   */
  protected void parseCacheSetupStrategy(Element element,
      BeanDefinitionRegistry registry,
      CacheSetupStrategyPropertySource propertySource) {

    String cachingInterceptorId = element.getAttribute("cachingInterceptorId");
    registerCachingInterceptor(cachingInterceptorId, registry, propertySource);

    String flushingInterceptorId = element
        .getAttribute("flushingInterceptorId");
    registerFlushingInterceptor(flushingInterceptorId, registry, propertySource);
  }

  private void registerCachingInterceptor(String cachingInterceptorId,
      BeanDefinitionRegistry registry,
      CacheSetupStrategyPropertySource propertySource) {

    MutablePropertyValues propertyValues = new MutablePropertyValues();

    RootBeanDefinition cachingInterceptor = new RootBeanDefinition(
        MethodMapCachingInterceptor.class, propertyValues);

    propertyValues.addPropertyValue(propertySource.getCacheProviderFacade());
    propertyValues.addPropertyValue(propertySource.getCachingListeners());
    propertyValues.addPropertyValue(propertySource.getCachingModels());

    registry.registerBeanDefinition(cachingInterceptorId, cachingInterceptor);
  }

  private void registerFlushingInterceptor(String flushingInterceptorId,
      BeanDefinitionRegistry registry,
      CacheSetupStrategyPropertySource propertySource) {

    MutablePropertyValues propertyValues = new MutablePropertyValues();

    RootBeanDefinition flushingInterceptor = new RootBeanDefinition(
        MethodMapFlushingInterceptor.class, propertyValues);

    propertyValues.addPropertyValue(propertySource.getCacheProviderFacade());
    propertyValues.addPropertyValue(propertySource.getFlushingModels());

    registry.registerBeanDefinition(flushingInterceptorId, flushingInterceptor);
  }
}
