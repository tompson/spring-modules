/* 
 * Created on Sep 5, 2005
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
package org.springmodules.util;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.util.ObjectUtils;

/**
 * <p>
 * Utility methods for Strings.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class Strings {

  /**
   * Surrounds the given String in single quotes. Returns <code>null</code> if
   * the given String is <code>null</code>.
   * 
   * @param str
   *          the given String
   * @return the given String surrounded by single quotes.
   */
  public static String quote(String str) {
    return (str != null) ? "'" + str + "'" : str;
  }

  /**
   * Surrounds the given Object in single quotes only if it is a String.
   * 
   * @param obj
   *          the given Object
   * @return the given Object surrounded by single quotes (if it is a String.)
   * 
   * @see #quote(String)
   */
  public static Object quoteIfString(Object obj) {
    return (obj instanceof String) ? quote((String) obj) : obj;
  }

  /**
   * Removes duplicates from the given String array.
   * 
   * @param array
   *          the given array
   * @return a new String array without duplicated entries
   */
  public static String[] removeDuplicates(String[] array) {
    if (ObjectUtils.isEmpty(array)) {
      return array;
    }
    Set set = new TreeSet();
    int length = array.length;
    for (int i = 0; i < length; i++) {
      set.add(array[i]);
    }
    return (String[]) set.toArray(new String[set.size()]);
  }
}
