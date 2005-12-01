/* 
 * Created on Aug 2, 2005
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
package org.springmodules.cache;

import org.springframework.core.NestedRuntimeException;

/**
 * <p>
 * Superclass of all exceptions thrown when an unexpected error takes place
 * while working with a cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class CacheException extends NestedRuntimeException {

  /**
   * Construct a <code>CacheException</code> with the specified detail
   * message.
   * 
   * @param msg
   *          the detail message
   */
  public CacheException(String msg) {
    super(msg);
  }

  /**
   * Construct a <code>CacheException</code> with the specified detail message
   * and nested exception.
   * 
   * @param msg
   *          the detail message
   * @param ex
   *          the nested exception
   */
  public CacheException(String msg, Throwable ex) {
    super(msg, ex);
  }

}
