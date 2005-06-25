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

import org.springmodules.cache.provider.CacheProfile;

/**
 * <p>
 * Set of configuration options needed for:
 * <ul>
 * <li>Retrieving an entry from a EHCache cache</li>
 * <li>Storing an object in a EHCache cache</li>
 * <li>Flushing one EHCache cache</li>
 * </ul>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/25 06:53:16 $
 */
public class EhCacheProfile implements CacheProfile {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3762529035888112945L;

  /**
   * Name of the cache.
   */
  private String cacheName;

  /**
   * Constructor.
   */
  public EhCacheProfile() {
    super();
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof EhCacheProfile)) {
      return false;
    }

    final EhCacheProfile ehCacheProfile = (EhCacheProfile) obj;

    if (this.cacheName != null ? !this.cacheName
        .equals(ehCacheProfile.cacheName) : ehCacheProfile.cacheName != null) {
      return false;
    }

    return true;
  }

  /**
   * Getter for field <code>{@link #cacheName}</code>.
   * 
   * @return the field <code>cacheName</code>.
   */
  public final String getCacheName() {
    return this.cacheName;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>{@link java.util.Hashtable}</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    return (this.cacheName != null ? this.cacheName.hashCode() : 0);
  }

  /**
   * Setter for the field <code>{@link #cacheName}</code>.
   * 
   * @param cacheName
   *          the new value to set.
   */
  public final void setCacheName(String cacheName) {
    this.cacheName = cacheName;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   * 
   * @see Object#toString()
   */
  public String toString() {
    return "EhCacheProfile: cacheName='" + this.cacheName + "'";
  }
}