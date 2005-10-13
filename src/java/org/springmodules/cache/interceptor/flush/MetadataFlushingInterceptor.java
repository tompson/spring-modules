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
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.metadata.Attributes;
import org.springframework.util.StringUtils;
import org.springmodules.cache.FlushingModel;

/**
 * <p>
 * Flushes part(s) of the cache when the intercepted method is executed. The
 * methods that trigger cache flush are marked using metadata attributes.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class MetadataFlushingInterceptor extends AbstractFlushingInterceptor {

  /**
   * Retrieves source-level metadata attributes from class methods.
   */
  private FlushingAttributeSource flushingAttributeSource;

  public MetadataFlushingInterceptor() {
    super();
  }

  /**
   * Returns the metadata attribute of the intercepted method.
   * 
   * @param methodInvocation
   *          the description of an invocation to the method.
   * @return the metadata attribute of the intercepted method.
   */
  protected FlushCache getFlushingAttribute(MethodInvocation methodInvocation) {
    Object thisObject = methodInvocation.getThis();
    Class targetClass = (thisObject != null) ? thisObject.getClass() : null;

    Method method = methodInvocation.getMethod();

    FlushingAttributeSource attributeSource = getFlushingAttributeSource();
    FlushCache attribute = attributeSource.getFlushingAttribute(method,
        targetClass);
    return attribute;
  }

  public final FlushingAttributeSource getFlushingAttributeSource() {
    return flushingAttributeSource;
  }

  /**
   * @see AbstractFlushingInterceptor#getModel(MethodInvocation)
   */
  protected final FlushingModel getModel(
      MethodInvocation methodInvocation) {
    Map flushingModels = getFlushingModels();
    FlushingModel model = null;

    if (flushingModels != null && !flushingModels.isEmpty()) {
      FlushCache attribute = getFlushingAttribute(methodInvocation);
      if (attribute != null) {
        String id = attribute.getModelId();
        if (StringUtils.hasText(id)) {
          model = (FlushingModel) flushingModels.get(id);
        }
      }
    }

    return model;
  }

  /**
   * Sets the underlying implementation of attributes to use.
   * 
   * @param attributes
   *          the new implementation of attributes to use.
   */
  public final void setAttributes(Attributes attributes) {
    MetadataFlushingAttributeSource source = new MetadataFlushingAttributeSource();
    source.setAttributes(attributes);
    setFlushingAttributeSource(source);
  }

  public final void setFlushingAttributeSource(
      FlushingAttributeSource newFlushingAttributeSource) {
    flushingAttributeSource = newFlushingAttributeSource;
  }

}