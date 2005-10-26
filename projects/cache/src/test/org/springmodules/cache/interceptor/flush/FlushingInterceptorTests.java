/* 
 * Created on Oct 8, 2005
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
package org.springmodules.cache.interceptor.flush;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.mock.MockFlushingModel;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractFlushingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class FlushingInterceptorTests extends TestCase {

  private class MockFlushingInterceptor extends AbstractFlushingInterceptor {

    FlushingModel model;

    boolean onAfterPropertiesSetCalled;

    protected FlushingModel getModel(MethodInvocation methodInvocation) {
      return model;
    }

    protected void onAfterPropertiesSet() throws FatalCacheException {
      onAfterPropertiesSetCalled = true;
    }
  }

  private CacheProviderFacade cacheProviderFacade;

  private MockControl cacheProviderFacadeControl;

  private MockFlushingInterceptor interceptor;

  private MethodInvocation invocation;

  private MockControl invocationControl;

  private CacheModelValidator validator;

  private MockControl validatorControl;

  public FlushingInterceptorTests(String name) {
    super(name);
  }

  private void assertAfterPropertiesSetThrowsException() {
    try {
      interceptor.afterPropertiesSet();
      fail();
    } catch (FatalCacheException exception) {
      // we are expecting this exception.
    }
  }

  private Object expectMethodInvocationCallsProceed() throws Throwable {
    setUpMethodInvocation();

    Object expected = new Object();
    invocationControl.expectAndReturn(invocation.proceed(), expected);
    return expected;
  }

  private void replayMocks() {
    cacheProviderFacadeControl.replay();

    if (invocationControl != null) {
      invocationControl.replay();
    }

    if (validatorControl != null) {
      validatorControl.replay();
    }
  }

  protected void setUp() {
    cacheProviderFacadeControl = MockControl
        .createStrictControl(CacheProviderFacade.class);
    cacheProviderFacade = (CacheProviderFacade) cacheProviderFacadeControl
        .getMock();

    interceptor = new MockFlushingInterceptor();
    interceptor.setCacheProviderFacade(cacheProviderFacade);
  }

  private void setUpMethodInvocation() {
    invocationControl = MockControl.createStrictControl(MethodInvocation.class);
    invocation = (MethodInvocation) invocationControl.getMock();
  }

  private void setUpValidator() {
    validatorControl = MockControl
        .createStrictControl(CacheModelValidator.class);
    validator = (CacheModelValidator) validatorControl.getMock();
  }

  public void testAfterPropertiesSetWithCacheProviderFacadeEqualToNull() {
    interceptor.setCacheProviderFacade(null);
    assertAfterPropertiesSetThrowsException();
  }

  public void testAfterPropertiesSetWithFlushingModelMapBeingProperties() {
    setUpValidator();

    String keyPrefix = "key";
    String valuePrefix = "value";

    Properties models = new Properties();
    for (int i = 0; i < 2; i++) {
      models.setProperty(keyPrefix + i, valuePrefix + i);
    }

    cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
        .getCacheModelValidator(), validator);

    MockControl editorControl = MockControl.createControl(PropertyEditor.class);
    PropertyEditor editor = (PropertyEditor) editorControl.getMock();

    cacheProviderFacade.getFlushingModelEditor();
    cacheProviderFacadeControl.setReturnValue(editor);

    // create a Map of FlushingModels from each of the properties.
    Map expected = new HashMap();
    for (Iterator i = models.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      String value = models.getProperty(key);

      MockFlushingModel model = new MockFlushingModel();

      editor.setAsText(value);
      editor.getValue();
      editorControl.setReturnValue(model);

      validator.validateFlushingModel(model);

      expected.put(key, model);
    }

    replayMocks();
    editorControl.replay();

    interceptor.setFlushingModels(models);
    interceptor.afterPropertiesSet();
    assertEquals(expected, interceptor.getFlushingModels());

    verifyMocks();
    editorControl.verify();
    assertTrue(interceptor.onAfterPropertiesSetCalled);
  }

  public void testAfterPropertiesSetWithNotEmptyCachingModelMapAndKeyGeneratorEqualToNull() {
    setUpValidator();

    Map models = new HashMap();
    for (int i = 0; i < 2; i++) {
      models.put(Integer.toString(i), new MockFlushingModel());
    }

    cacheProviderFacadeControl.expectAndReturn(cacheProviderFacade
        .getCacheModelValidator(), validator);

    for (Iterator i = models.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      FlushingModel model = (FlushingModel) entry.getValue();
      validator.validateFlushingModel(model);
    }

    replayMocks();

    interceptor.setFlushingModels(models);
    interceptor.afterPropertiesSet();

    verifyMocks();
    assertTrue(interceptor.onAfterPropertiesSetCalled);
  }

  public void testInvokeWhenFlushingAfterMethodExecution() throws Throwable {
    MockFlushingModel model = new MockFlushingModel();
    model.setFlushBeforeMethodExecution(true);
    interceptor.model = model;

    Object expected = expectMethodInvocationCallsProceed();
    cacheProviderFacade.flushCache(model);

    replayMocks();

    assertSame(expected, interceptor.invoke(invocation));
    verifyMocks();
  }

  public void testInvokeWhenFlushingBeforeMethodExecution() throws Throwable {
    MockFlushingModel model = new MockFlushingModel();
    model.setFlushBeforeMethodExecution(true);
    interceptor.model = model;

    cacheProviderFacade.flushCache(model);
    Object expected = expectMethodInvocationCallsProceed();

    replayMocks();

    assertSame(expected, interceptor.invoke(invocation));
    verifyMocks();
  }

  public void testInvokeWithReturnedCachingModelEqualToNull() throws Throwable {
    interceptor.model = null;

    Object expected = expectMethodInvocationCallsProceed();
    replayMocks();

    assertSame(expected, interceptor.invoke(invocation));
    verifyMocks();
  }

  private void verifyMocks() {
    cacheProviderFacadeControl.verify();

    if (invocationControl != null) {
      invocationControl.verify();
    }

    if (validatorControl != null) {
      validatorControl.verify();
    }
  }
}
