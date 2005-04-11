/* 
 * Created on Jan 19, 2005
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

package org.springmodules.cache.interceptor;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.util.TextMatcher;

/**
 * <p>
 * Template for classes that allow attributes to be matched by registered name.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:21 $
 */
public abstract class AbstractNameMatchCacheAttributeSource {

  /**
   * Message Logger.
   */
  private static Log logger = LogFactory
      .getLog(AbstractNameMatchCacheAttributeSource.class);

  /**
   * Map containing instances of <code>{@link CacheAttribute}</code>. The key
   * of each entry is the name of the method to attach the attribute to.
   */
  private Map attributeMap;

  /**
   * Constructor.
   */
  public AbstractNameMatchCacheAttributeSource() {
    super();
    this.attributeMap = new HashMap();
  }

  /**
   * Add an attribute for a caching method. Method names can end with "*" for
   * matching multiple methods.
   * 
   * @param methodName
   *          the name of the method
   * @param cacheAttribute
   *          attribute associated with the method
   */
  protected final void addAttribute(String methodName,
      CacheAttribute cacheAttribute) {
    if (logger.isDebugEnabled()) {
      logger
          .debug("Method 'addMethod(String, CacheAttribute)'. Adding method ["
              + methodName + "] with attribute [" + cacheAttribute + "]");
    }
    this.attributeMap.put(methodName, cacheAttribute);
  }

  /**
   * Returns an unmodifiable view of <code>{@link #attributeMap}</code>.
   * 
   * @return an unmodifiable view of the member variable
   *         <code>attributeMap</code>.
   */
  protected final Map getAttributeMap() {
    return Collections.unmodifiableMap(this.attributeMap);
  }

  /**
   * Returns a property editor that creates instances of
   * <code>{@link CacheAttribute}</code>.
   * 
   * @return a property editor for cache attributes.
   */
  protected abstract PropertyEditor getCacheAttributeEditor();

  /**
   * Returns <code>true</code> if the given method name matches the mapped
   * name. The default implementation checks for "xxx*" and "*xxx" matches.
   * 
   * @param methodName
   *          the method name of the class.
   * @param mappedName
   *          the name in the descriptor.
   * @return <code>true</code> if the names match.
   */
  protected boolean isMatch(String methodName, String mappedName) {
    return TextMatcher.isMatch(methodName, mappedName);
  }

  /**
   * Parses the given properties into a name/attribute map. Expects method names
   * as keys and String attributes definitions as values, parsable into
   * <code>{@link CacheAttribute}</code>..
   * 
   * @param cacheAttributes
   *          the given properties.
   */
  public final void setProperties(Properties cacheAttributes) {
    PropertyEditor propertyEditor = this.getCacheAttributeEditor();

    Iterator keySetIterator = cacheAttributes.keySet().iterator();
    while (keySetIterator.hasNext()) {
      String methodName = (String) keySetIterator.next();
      String cacheAttributeProperties = cacheAttributes.getProperty(methodName);

      propertyEditor.setAsText(cacheAttributeProperties);
      CacheAttribute cacheAttribute = (CacheAttribute) propertyEditor
          .getValue();
      if (cacheAttribute != null) {
        this.addAttribute(methodName, cacheAttribute);
      }
    }
  }

  /**
   * Returns an instance of <code>{@link CacheAttribute}</code> for the
   * intercepted method.
   * 
   * @param method
   *          the definition of the intercepted method.
   * @return a metadata attribute from the intercepted method.
   */
  protected final CacheAttribute getCacheAttribute(Method method) {
    String methodName = method.getName();
    CacheAttribute cacheAttribute = (CacheAttribute) this.attributeMap
        .get(methodName);

    if (cacheAttribute == null) {
      // look up most specific name match
      String bestNameMatch = null;

      Iterator keySetIterator = this.attributeMap.keySet().iterator();
      while (keySetIterator.hasNext()) {
        String mappedMethodName = (String) keySetIterator.next();

        if (this.isMatch(methodName, mappedMethodName)
            && (bestNameMatch == null || bestNameMatch.length() <= mappedMethodName
                .length())) {
          cacheAttribute = (CacheAttribute) this.attributeMap
              .get(mappedMethodName);
          bestNameMatch = mappedMethodName;
        } // end 'if (this.isMatch(methodName, mappedMethodName)'
      } // end 'while (keySetIterator.hasNext())'
    } // end 'if (cacheAttribute == null)'

    return cacheAttribute;
  }

}