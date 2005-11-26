/* 
 * Created on Oct 14, 2005
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
package org.springmodules;

import java.util.Arrays;

import junit.framework.Assert;

import org.springmodules.util.Objects;

/**
 * <p>
 * Assert methods for arrays.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class ArrayAssert {

  public static void assertEquals(Object[] expected, Object[] actual) {
    Assert.assertTrue("expected:<" + Objects.nullSafeToString(expected) + "> but was:<"
        + Objects.nullSafeToString(actual) + ">", Arrays.equals(expected, actual));
  }
}
