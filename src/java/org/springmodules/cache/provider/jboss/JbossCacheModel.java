/* 
 * Created on Sep 1, 2005
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
package org.springmodules.cache.provider.jboss;

import org.springmodules.cache.provider.CacheModel;
import org.springmodules.util.Strings;

/**
 * <p>
 * Configuration options for accessing JBossCache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class JbossCacheModel implements CacheModel {

  private static final long serialVersionUID = -9019322549512783005L;

  /**
   * FQN of the node where to store cache elements.
   */
  private String nodeFqn;

  public JbossCacheModel() {
    super();
  }

  public JbossCacheModel(String fqn) {
    this();
    setNodeFqn(fqn);
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof JbossCacheModel)) {
      return false;
    }

    final JbossCacheModel cacheModel = (JbossCacheModel) obj;

    if (nodeFqn != null ? !nodeFqn.equals(cacheModel.nodeFqn)
        : cacheModel.nodeFqn != null) {
      return false;
    }

    return true;
  }

  public final String getNodeFqn() {
    return nodeFqn;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 17;

    hash = multiplier * hash + (nodeFqn != null ? nodeFqn.hashCode() : 0);

    return hash;
  }

  public final void setNodeFqn(String newNodeFqn) {
    nodeFqn = newNodeFqn;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("nodeFqn=" + Strings.quote(nodeFqn) + "]");

    return buffer.toString();
  }
}
