/* 
 * Created on Jan 13, 2005
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

package org.springmodules.cache.provider.jcs;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractCacheProfileValidator;
import org.springmodules.cache.provider.InvalidCacheProfileException;

/**
 * <p>
 * Validates the properties of a <code>{@link JcsProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/08/11 04:28:37 $
 */
public class JcsProfileValidator extends AbstractCacheProfileValidator {

  public JcsProfileValidator() {
    super();
  }

  /**
   * @see AbstractCacheProfileValidator#getTargetClass()
   */
  protected Class getTargetClass() {
    return JcsProfile.class;
  }

  /**
   * @see AbstractCacheProfileValidator#validateCacheProfileProperties(java.lang.Object)
   */
  protected void validateCacheProfileProperties(Object cacheProfile)
      throws InvalidCacheProfileException {
    JcsProfile jcsProfile = (JcsProfile) cacheProfile;

    if (!StringUtils.hasText(jcsProfile.getCacheName())) {
      throw new InvalidCacheProfileException(
          "Cache name should not be empty");
    }
  }
}