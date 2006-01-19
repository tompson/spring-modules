/* 
 * Created on Sep 21, 2004
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
import java.util.Collection;
import java.util.Iterator;

import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.interceptor.AbstractSingleMetadataCacheAttributeSource;

/**
 * <p>
 * Template for implementations of <code>{@link CachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCachingAttributeSource extends
    AbstractSingleMetadataCacheAttributeSource implements
    CachingAttributeSource {

  /**
   * @see CachingAttributeSource#getCachingAttribute(Method, Class)
   */
  public final Cached getCachingAttribute(Method method, Class targetClass) {
    Cached attribute = null;
    if (CachingUtils.isCacheable(method)) {
      attribute = (Cached) getAttribute(method, targetClass);
    }
    return attribute;
  }

  /**
   * @see AbstractSingleMetadataCacheAttributeSource#findAttribute(Collection)
   */
  protected CacheAttribute findAttribute(Collection attributes) {
    CacheAttribute attribute = null;
    if (attributes != null && !attributes.isEmpty()) {
      for (Iterator i = attributes.iterator(); i.hasNext();) {
        Object object = i.next();
        if (object instanceof Cached) {
          attribute = (CacheAttribute) object;
        }
      }
    }
    return attribute;
  }
}