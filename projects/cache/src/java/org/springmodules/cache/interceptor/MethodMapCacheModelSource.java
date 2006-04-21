/* 
 * Created on Apr 26, 2005
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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springmodules.cache.CacheModel;

/**
 * <p>
 * Binds a <code>{@link CacheModel}</code> to a method signature.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class MethodMapCacheModelSource {

  final Map cacheModels;

  final Map registeredMethodMap;

  private final MethodMatcher methodMatcher;

  public MethodMapCacheModelSource() {
    cacheModels = new HashMap();
    methodMatcher = new MethodMatcher();
    registeredMethodMap = new HashMap();
  }

  public void addModel(CacheModel model, String fullyQualifiedMethodName)
      throws IllegalArgumentException {
    String target = fullyQualifiedMethodName;
    Collection matches = methodMatcher.matchingMethods(target);
    notEmpty(matches, target);
    for (Iterator i = matches.iterator(); i.hasNext();) {
      Method method = (Method) i.next();
      if (methodNotRegisteredOrMoreSpecificMethodFound(method, target))
        addModel(model, method, target);
    }
  }

  public CacheModel model(Method m) {
    return (CacheModel) cacheModels.get(m);
  }

  private void addModel(CacheModel c, Method m, String fullyQualifiedMethodName) {
    registeredMethodMap.put(m, fullyQualifiedMethodName);
    cacheModels.put(m, c);
  }

  private boolean methodNotRegisteredOrMoreSpecificMethodFound(Method m,
      String target) {
    String registered = (String) registeredMethodMap.get(m);
    if (registered == null) return true;
    return !target.equals(registered) && target.length() <= registered.length();
  }

  private void notEmpty(Collection matchingMethods, String targetMethodName)
      throws IllegalArgumentException {
    if (matchingMethods.isEmpty())
      throw new IllegalArgumentException("Couldn't find any method matching '"
          + targetMethodName + "'");
  }
}