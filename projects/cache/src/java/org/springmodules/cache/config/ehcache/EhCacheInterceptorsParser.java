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
package org.springmodules.cache.config.ehcache;

import org.springmodules.cache.config.AbstractInterceptorsParser;
import org.springmodules.cache.config.CacheModelParser;
import org.springmodules.cache.config.CacheProviderFacadeDefinitionValidator;

/**
 * <p>
 * Parsing of the XML tag "interceptors" when using the XML namespace "ehcache".
 * Creates and registers instances of
 * <code>{@link org.springmodules.cache.interceptor.caching.MethodMapCachingInterceptor}</code>
 * and
 * <code>{@link org.springmodules.cache.interceptor.flush.MethodMapFlushingInterceptor}</code>
 * which can be used with
 * <code>{@link org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator}</code>
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheInterceptorsParser extends AbstractInterceptorsParser {

  private CacheModelParser modelParser;

  private CacheProviderFacadeDefinitionValidator validator;

  /**
   * Constructor.
   */
  public EhCacheInterceptorsParser() {
    super();
    modelParser = new EhCacheModelParser();
    validator = new EhCacheFacadeDefinitionValidator();
  }

  /**
   * @see org.springmodules.cache.config.AbstractCacheSetupStrategyParser#getCacheModelParser()
   */
  protected CacheModelParser getCacheModelParser() {
    return modelParser;
  }

  /**
   * @see org.springmodules.cache.config.AbstractCacheSetupStrategyParser#getCacheProviderFacadeValidator()
   */
  protected CacheProviderFacadeDefinitionValidator getCacheProviderFacadeValidator() {
    return validator;
  }

}
