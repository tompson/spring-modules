/* 
 * Created on March 2, 2005
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
package org.springmodules.cache.interceptor.caching;

import java.beans.PropertyEditorSupport;
import java.util.Iterator;
import java.util.Properties;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Property editor that creates a new instance of
 * <code>{@link MethodMapCachingAttributeSource}</code> by parsing a given
 * <code>String</code>. The caching attribute <code>String</code> must be
 * parseable by the <code>{@link CachingAttributeEditor}</code> in this
 * package.
 * </p>
 * <p>
 * Strings are in property syntax, with the form: <br>
 * <code>FQCN.methodName=&lt;caching attribute string&gt;</code>
 * </p>
 * <p>
 * For example: <br>
 * <code>com.mycompany.mycode.MyClass.myMethod=[cacheModelId=myCache]</code>
 * </p>
 * <p>
 * <b>NOTE: </b> The specified class must be the one where the methods are
 * defined; in case of implementing an interface, the interface class name.
 * </p>
 * <p>
 * Note: Will register all overloaded methods for a given name. Does not support
 * explicit registration of certain overloaded methods. Supports "xxx*"
 * mappings, e.g. "notify*" for "notify" and "notifyAll".
 * </p>
 * 
 * @author Xavier Dury
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.7 $ $Date: 2005/09/29 01:21:46 $
 */
public class CachingAttributeSourceEditor extends PropertyEditorSupport {

  public CachingAttributeSourceEditor() {
    super();
  }

  /**
   * Creates a new instance of
   * <code>{@link MethodMapCachingAttributeSource}</code> from the specified
   * <code>String</code>.
   * 
   * @param text
   *          the <code>String</code> to be parsed.
   */
  public void setAsText(String text) {
    if (StringUtils.hasText(text)) {
      MethodMapCachingAttributeSource cachingAttributeSource = new MethodMapCachingAttributeSource();

      PropertiesEditor propertiesEditor = new PropertiesEditor();
      propertiesEditor.setAsText(text);
      Properties properties = (Properties) propertiesEditor.getValue();

      // Now we have properties, process each one individually.
      CachingAttributeEditor cachingAttributeEditor = new CachingAttributeEditor();

      Iterator keySetIterator = properties.keySet().iterator();
      while (keySetIterator.hasNext()) {
        String key = (String) keySetIterator.next();
        String value = properties.getProperty(key);

        // Convert value to a caching attribute.
        cachingAttributeEditor.setAsText(value);
        Cached cachingAttribute = (Cached) cachingAttributeEditor.getValue();

        // Register name and attribute.
        cachingAttributeSource.addCachingAttribute(key, cachingAttribute);
      }

      setValue(cachingAttributeSource);
    }
  }
}