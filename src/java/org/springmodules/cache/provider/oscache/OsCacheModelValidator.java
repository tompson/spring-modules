/* 
 * Created on Jan 12, 2005
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

package org.springmodules.cache.provider.oscache;

import org.springmodules.cache.provider.AbstractCacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Validates the properties of a <code>{@link OsCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class OsCacheModelValidator extends AbstractCacheModelValidator {

  public OsCacheModelValidator() {
    super();
  }

  /**
   * @see AbstractCacheModelValidator#getCachingModelTargetClass()
   */
  protected Class getCachingModelTargetClass() {
    return OsCacheCachingModel.class;
  }

  /**
   * @see AbstractCacheModelValidator#getFlushingModelTargetClass()
   */
  protected Class getFlushingModelTargetClass() {
    return OsCacheFlushingModel.class;
  }

  /**
   * @see AbstractCacheModelValidator#validateFlushingModelProperties(Object)
   */
  protected void validateFlushingModelProperties(Object flushingModel)
      throws InvalidCacheModelException {
    OsCacheFlushingModel model = (OsCacheFlushingModel) flushingModel;
    if (!ArrayUtils.hasElements(model.getGroups())) {
      throw new InvalidCacheModelException(
          "The model should have at least one group name");
    }
  }
}