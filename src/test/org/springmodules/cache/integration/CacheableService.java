/* 
 * Created on Oct 22, 2004
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

package org.springmodules.cache.integration;

/**
 * <p>
 * Interface that simulates a business object. This interface and its
 * implementation(s) are used only for integration tests.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public interface CacheableService {

  /**
   * Returns a name from the list of names.
   * 
   * @param index
   *          the index of the element in the list.
   * @return a name from the list of names.
   */
  String getName(int index);

  /**
   * Updates the name in the specified position in the list.
   * 
   * @param index
   *          the index of the element to update.
   * @param name
   *          the new name.
   */
  void updateName(int index, String name);
}