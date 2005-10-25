/* 
 * Created on Sep 29, 2005
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
package org.springmodules.cache.provider.oscache;

import java.util.Arrays;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractFlushingModel;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Configuration options needed to flush one or more caches from OSCache.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class OsCacheFlushingModel extends AbstractFlushingModel {

  private static final long serialVersionUID = 7299844898815952890L;

  /**
   * Names of the groups to flush.
   */
  private String[] groups;

  public OsCacheFlushingModel() {
    super();
  }
  
  public OsCacheFlushingModel(String csvGroups) {
    this();
    setGroups(csvGroups);
  }
  
  public OsCacheFlushingModel(String[] groups) {
    this();
    setGroups(groups);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof OsCacheFlushingModel)) {
      return false;
    }
    OsCacheFlushingModel flushingModel = (OsCacheFlushingModel) obj;
    if (!Arrays.equals(groups, flushingModel.groups)) {
      return false;
    }
    return true;
  }

  public String[] getGroups() {
    return groups;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + ArrayUtils.hashCode(groups);
    return hash;
  }

  public void setGroups(String csvGroups) {
    String[] newGroups = null;
    if (StringUtils.hasText(csvGroups)) {
      newGroups = StringUtils.commaDelimitedListToStringArray(csvGroups);
    }
    setGroups(newGroups);
  }

  public void setGroups(String[] newGroups) {
    groups = newGroups;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("groups=" + ArrayUtils.toString(groups) + ", ");
    buffer.append("flushBeforeMethodExecution="
        + isFlushBeforeMethodExecution() + "]");
    return buffer.toString();
  }
}
