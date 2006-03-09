/* 
 * Created on Mar 8, 2006
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

import org.springmodules.cache.config.CacheModelParser;
import org.springmodules.cache.config.CacheProviderFacadeDefinitionValidator;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheCommonsAttributesParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class EhCacheCommonsAttributesParserTests extends TestCase {

  private EhCacheCommonsAttributesParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public EhCacheCommonsAttributesParserTests(String name) {
    super(name);
  }

  public void testGetCacheModelParser() {
    CacheModelParser cacheModelParser = parser.getCacheModelParser();
    EhCacheConfigAssert.assertCacheModelParserIsCorrect(cacheModelParser);
  }

  public void testGetCacheProviderFacadeDefinitionValidator() {
    CacheProviderFacadeDefinitionValidator validator = parser
        .getCacheProviderFacadeDefinitionValidator();
    EhCacheConfigAssert
        .assertCacheProviderFacadeDefinitionValidatorIsCorrect(validator);
  }

  protected void setUp() {
    parser = new EhCacheCommonsAttributesParser();
  }

}
