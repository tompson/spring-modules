/* 
 * Created on Oct 29, 2004
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

package org.springmodules.cache.provider.ehcache;

import org.springframework.util.ObjectUtils;
import org.springmodules.cache.CachingModel;
import org.springmodules.util.Objects;
import org.springmodules.util.Strings;

/**
 * <p>
 * Configuration options needed for:
 * <ul>
 * <li>storing objects in EHCache</li>
 * <li>retrieving objects from EHCache</li>
 * <li>removing objects from EHCache</li>
 * </ul>
 * 
 * @author Alex Ruiz
 */
public class EhCacheCachingModel implements CachingModel {

  private static final long serialVersionUID = 3762529035888112945L;

  private String cacheName;

  /**
   * Constructor.
   */
  public EhCacheCachingModel() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param cacheName
   *          the name of the EHCache cache to use
   */
  public EhCacheCachingModel(String cacheName) {
    this();
    setCacheName(cacheName);
  }

  /**
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof EhCacheCachingModel)) {
      return false;
    }

    EhCacheCachingModel cachingModel = (EhCacheCachingModel) obj;

    if (!ObjectUtils.nullSafeEquals(cacheName, cachingModel.cacheName)) {
      return false;
    }

    return true;
  }

  /**
   * @return the name of the EHCache cache to use
   */
  public final String getCacheName() {
    return cacheName;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (Objects.nullSafeHashCode(cacheName));
    return hash;
  }

  /**
   * Sets the name of the EHCache cache to use.
   * 
   * @param newCacheName
   *          the new name of the EHCache cache
   */
  public final void setCacheName(String newCacheName) {
    cacheName = newCacheName;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = Objects.identityToString(this);
    buffer.append("[cacheName=" + Strings.quote(cacheName) + "]");
    return buffer.toString();
  }
}