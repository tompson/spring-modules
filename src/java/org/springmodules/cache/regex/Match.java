/* 
 * Created on Mar 8, 2005
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
 * Represents the result of single regular expression match.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/08/04 04:50:11 $
 */
public final class Match {

  /**
   * Groups contained in the result. This number includes the 0th group. In
   * other words, the result refers to the number of parenthesized subgroups
   * plus the entire match itself.
   */
  private String[] groups;

  /**
   * Indicates whether the match is successful.
   */
  private boolean successful;

  public Match(boolean successful, String[] groups) {
    super();

    this.successful = successful;
    this.groups = groups;
  }

  public String[] getGroups() {
    return this.groups;
  }

  public boolean isSuccessful() {
    return this.successful;
  }

}
