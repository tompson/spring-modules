/* 
 * Created on Nov 16, 2004
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

package org.springmodules.cache.provider;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * Template for singleton factories of cache managers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:11 $
 */
public abstract class AbstractSingletonCacheManagerFactoryBean implements
    FactoryBean, InitializingBean, DisposableBean {

  /**
   * Constructor.
   */
  public AbstractSingletonCacheManagerFactoryBean() {
    super();
  }

  /**
   * Notifies the Spring IoC container that this factory is a singleton bean.
   * 
   * @return <code>true</code>.
   */
  public final boolean isSingleton() {
    return true;
  }

}