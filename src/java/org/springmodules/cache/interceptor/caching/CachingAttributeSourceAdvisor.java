/* 
 * Created on Sep 22, 2004
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

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;

import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * <p>
 * Advisor driven by a <code>{@link CachingAttributeSource}</code> that points
 * to <code>{@link CachingInterceptor}</code> which methods should be
 * intercepted.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/09 02:18:57 $
 */
public class CachingAttributeSourceAdvisor extends
    StaticMethodMatcherPointcutAdvisor {

  private static final long serialVersionUID = 3832897771492356663L;

  /**
   * Retrieves instances of <code>{@link Cached}</code> for intercepted
   * methods.
   */
  private CachingAttributeSource cachingAttributeSource;

  /**
   * @param cacheInterceptor
   *          Advice that caches the returned value of intercepted methods.
   * @throws AopConfigException
   *           if the <code>CachingAttributeSource</code> of
   *           <code>cacheInterceptor</code> is <code>null</code>.
   */
  public CachingAttributeSourceAdvisor(CachingInterceptor cacheInterceptor) {
    super(cacheInterceptor);

    CachingAttributeSource tempAttributeSource = cacheInterceptor
        .getCachingAttributeSource();

    if (tempAttributeSource == null) {
      String message = "Constructor 'CachingAttributeSourceAdvisor'. "
          + "CacheFlushInterceptor has no CachingAttributeSource configured";

      throw new AopConfigException(message);
    }

    cachingAttributeSource = tempAttributeSource;
  }

  /**
   * Returns <code>true</code> if the intercepted method should be cached.
   * 
   * @param method
   *          the intercepted method to verify.
   * @param targetClass
   *          the class declaring the method.
   * @return <code>true</code> if the specified method should be cached.
   */
  public final boolean matches(Method method, Class targetClass) {
    Cached attribute = cachingAttributeSource.getCachingAttribute(method,
        targetClass);

    boolean matches = (attribute != null);
    return matches;
  }
}