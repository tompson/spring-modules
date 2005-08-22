/* 
 * Created on Aug 22, 2005
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
package org.springmodules.cache.regex;

/**
 * <p>
 * Unit Tests for <code>{@link Perl5Regex}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class Perl5RegexTests extends AbstractRegexTestCases {

  public Perl5RegexTests(String name) {
    super(name);
  }

  /**
   * @see AbstractRegexTestCases#getRegexToTest(java.lang.String)
   */
  protected Regex getRegexToTest(String pattern) {
    return new Perl5Regex(pattern);
  }

}
