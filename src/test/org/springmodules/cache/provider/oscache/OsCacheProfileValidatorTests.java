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

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/08/05 02:18:56 $
 */
public final class OsCacheProfileValidatorTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private OsCacheProfileValidator cacheProfileValidator;

  public OsCacheProfileValidatorTests(String name) {
    super(name);
  }

  protected void setUp() {
    this.cacheProfileValidator = new OsCacheProfileValidator();
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheProfileValidator#validateCacheProfile(Object)}</code>
   * does not throw any exception if the profile to validate is an instance of
   * <code>{@link OsCacheProfile}</code>.
   */
  public void testValidateCacheProfileWithInstanceOfOscacheCacheProfile() {
    OsCacheProfile cacheProfile = new OsCacheProfile();
    Object object = cacheProfile;

    this.cacheProfileValidator.validateCacheProfile(object);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheProfileValidator#validateCacheProfile(Object)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified argument
   * is not an instance of <code>{@link OsCacheProfile}</code>.
   */
  public void testValidateCacheProfileObjectWithObjectNotInstanceOfOscacheCacheProfile() {
    try {
      this.cacheProfileValidator.validateCacheProfile(new Object());
      fail("An 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }
}