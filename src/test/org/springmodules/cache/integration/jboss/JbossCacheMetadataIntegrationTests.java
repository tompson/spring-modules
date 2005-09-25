/* 
 * Created on Jan 19, 2005
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

package org.springmodules.cache.integration.jboss;

/**
 * <p>
 * Verifies that the caching module works correctly when using JBossCache as the
 * cache provider and the caching services use metadata attributes to identify
 * their target(s).
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JbossCacheMetadataIntegrationTests extends
    AbstractJbossCacheIntegrationTestCases {

  public JbossCacheMetadataIntegrationTests() {
    super();
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
   */
  protected String[] getConfigLocations() {
    String[] configFileNames = new String[] { CACHE_APPLICATION_CONTEXT_PATH,
        METADATA_APPLICATION_CONTEXT_PATH };

    return configFileNames;
  }
}