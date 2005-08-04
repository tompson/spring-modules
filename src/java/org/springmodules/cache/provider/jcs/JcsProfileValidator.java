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
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Validates the properties of a <code>{@link JcsProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/04 04:46:30 $
 */
public class JcsProfileValidator implements CacheProfileValidator {

  public JcsProfileValidator() {
    super();
  }

  /**
   * Validates the specified cache name.
   * 
   * @param cacheName
   *          the cache name to validate.
   * 
   * @throws IllegalArgumentException
   *           if the cache name is empty or <code>null</code>.
   */
  protected final void validateCacheName(String cacheName) {
    if (!StringUtils.hasText(cacheName)) {
      throw new IllegalArgumentException("Cache name should not be empty");
    }
  }

  /**
   * @see CacheProfileValidator#validateCacheProfile(Object)
   * @see #validateCacheName(String)
   * 
   * @throws IllegalArgumentException
   *           if the cache profile is not an instance of
   *           <code>JcsProfile</code>.
   */
  public final void validateCacheProfile(Object cacheProfile) {
    if (cacheProfile instanceof JcsProfile) {
      JcsProfile jcsCacheProfile = (JcsProfile) cacheProfile;
      String cacheName = jcsCacheProfile.getCacheName();
      this.validateCacheName(cacheName);
    } else {
      throw new IllegalArgumentException(
          "The cache profile should be an instance of 'JcsProfile'");
    }
  }
}