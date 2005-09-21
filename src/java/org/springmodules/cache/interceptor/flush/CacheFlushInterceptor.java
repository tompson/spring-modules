/* 
 * Created on Oct 21, 2004
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

package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.cache.provider.CacheProviderFacadeStatus;

/**
 * <p>
 * Flushes part(s) of the cache when the intercepted method is executed.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.7 $ $Date: 2005/09/21 02:45:49 $
 */
public final class CacheFlushInterceptor extends CacheFlushAspectSupport
    implements MethodInterceptor {

  private static Log logger = LogFactory.getLog(CacheFlushInterceptor.class);

  private CacheProviderFacade cacheProviderFacade;

  public CacheFlushInterceptor() {
    super();
  }

  /**
   * Returns the metadata attribute of the intercepted method.
   * 
   * @param methodInvocation
   *          the description of an invocation to the method.
   * @return the metadata attribute of the intercepted method.
   */
  protected FlushCache getCacheFlushAttribute(MethodInvocation methodInvocation) {
    Method method = methodInvocation.getMethod();

    Object thisObject = methodInvocation.getThis();
    Class targetClass = (thisObject != null) ? thisObject.getClass() : null;

    CacheFlushAttributeSource attributeSource = getCacheFlushAttributeSource();
    FlushCache attribute = attributeSource.getCacheFlushAttribute(method,
        targetClass);
    return attribute;
  }

  /**
   * Flushes the part(s) of the cache.
   * 
   * @param methodInvocation
   *          the description of the intercepted method.
   * @return the return value of the intercepted method.
   */
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    if (cacheProviderFacade.getStatus() != CacheProviderFacadeStatus.READY) {
      logger.info(cacheProviderFacade.getStatus());
      return methodInvocation.proceed();
    }

    FlushCache attribute = getCacheFlushAttribute(methodInvocation);

    if (null == attribute) {
      return methodInvocation.proceed();
    }

    String[] cacheProfileIds = attribute.getCacheProfileIds();
    Object proceedReturnValue = null;

    boolean flushedBeforeExecution = attribute.isFlushBeforeExecution();
    if (flushedBeforeExecution) {
      cacheProviderFacade.flushCache(cacheProfileIds);
      proceedReturnValue = methodInvocation.proceed();
    } else {
      proceedReturnValue = methodInvocation.proceed();
      cacheProviderFacade.flushCache(cacheProfileIds);
    }

    return proceedReturnValue;
  }

  /**
   * Set properties with method names as keys and caching-attribute descriptors
   * (parsed via <code>{@link CacheFlushAttributeEditor}</code>) as values.
   * <p>
   * Note: Method names are always applied to the target class, no matter if
   * defined in an interface or the class itself.
   * <p>
   * Internally, a <code>{@link NameMatchCacheFlushAttributeSource}</code>
   * will be created from the given properties.
   * 
   * @see NameMatchCacheFlushAttributeSource
   */
  public void setCacheFlushAttributes(Properties cacheFlushAttributes) {
    NameMatchCacheFlushAttributeSource attributeSource = new NameMatchCacheFlushAttributeSource();
    attributeSource.setProperties(cacheFlushAttributes);
    setCacheFlushAttributeSource(attributeSource);
  }

  public final void setCacheProviderFacade(
      CacheProviderFacade newCacheProviderFacade) {
    cacheProviderFacade = newCacheProviderFacade;
  }

}