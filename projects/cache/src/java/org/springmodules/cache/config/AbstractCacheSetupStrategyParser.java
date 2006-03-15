/* 
 * Created on Feb 19, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.xml.DomUtils;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * <p>
 * Template that handles the parsing of setup strategy for declarative caching
 * services.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheSetupStrategyParser implements
    BeanDefinitionParser {

  /**
   * Contains the names of the XML attributes used in this parser.
   */
  private static class XmlAttribute {

    static final String TARGET = "target";
  }

  private BeanReferenceParser beanReferenceParser;

  /**
   * Constructor.
   */
  public AbstractCacheSetupStrategyParser() {
    super();
    beanReferenceParser = new BeanReferenceParserImpl();
  }

  /**
   * Parses the given XML element containing the properties and/or sub-elements
   * necessary to configure a strategy for setting up declarative caching
   * services.
   * 
   * @param element
   *          the XML element to parse
   * @param parserContext
   *          the parser context
   * @throws NoSuchBeanDefinitionException
   *           if the cache provider facade is <code>null</code>
   * @throws IllegalStateException
   *           if the cache provider facade is in invalid state
   * @throws NoSuchBeanDefinitionException
   *           if any of the caching listeners does not exist in the registry
   * @throws IllegalStateException
   *           if any of the caching listeners is not an instance of
   *           <code>CachingListener</code>
   * 
   * @see BeanDefinitionParser#parse(Element, ParserContext)
   * @see CacheProviderFacadeDefinitionValidator#validate(AbstractBeanDefinition)
   */
  public final BeanDefinition parse(Element element, ParserContext parserContext)
      throws NoSuchBeanDefinitionException, IllegalStateException {
    String cacheProviderFacadeId = element.getAttribute("providerId");

    BeanDefinitionRegistry registry = parserContext.getRegistry();

    BeanDefinition cacheProviderFacade = registry
        .getBeanDefinition(cacheProviderFacadeId);
    getCacheProviderFacadeDefinitionValidator().validate(
        (AbstractBeanDefinition) cacheProviderFacade);

    RuntimeBeanReference cacheProviderFacadeReference = new RuntimeBeanReference(
        cacheProviderFacadeId);

    Object cacheKeyGenerator = parseCacheKeyGenerator(element, parserContext);
    List cachingListeners = parseCachingListeners(element, parserContext);
    Map cachingModels = parseCachingModels(element);
    Map flushingModels = parseFlushingModels(element);

    CacheSetupStrategyPropertySource ps = new CacheSetupStrategyPropertySource(
        cacheKeyGenerator, cacheProviderFacadeReference, cachingListeners,
        cachingModels, flushingModels);

    parseCacheSetupStrategy(element, parserContext, ps);
    return null;
  }

  protected final BeanReferenceParser getBeanReferenceParser() {
    return beanReferenceParser;
  }

  /**
   * @return the parser for caching and flushing models
   */
  protected abstract CacheModelParser getCacheModelParser();

  /**
   * @return the validator of the properties of the
   *         <code>CacheProviderFacade</code>
   */
  protected abstract CacheProviderFacadeDefinitionValidator getCacheProviderFacadeDefinitionValidator();

  /**
   * Parses the given XML element to create the strategy for setting up
   * declarative caching services.
   * 
   * @param element
   *          the XML element to parse
   * @param parserContext
   *          the parser context
   * @param propertySource
   *          contains common properties for the different cache setup
   *          strategies
   */
  protected abstract void parseCacheSetupStrategy(Element element,
      ParserContext parserContext,
      CacheSetupStrategyPropertySource propertySource);

  protected final void setBeanReferenceParser(
      BeanReferenceParser newBeanReferenceParser) {
    beanReferenceParser = newBeanReferenceParser;
  }

  private Object parseCacheKeyGenerator(Element element,
      ParserContext parserContext) {
    Object keyGenerator = null;

    List cacheKeyGeneratorElements = DomUtils.getChildElementsByTagName(
        element, "cacheKeyGenerator", true);
    if (!CollectionUtils.isEmpty(cacheKeyGeneratorElements)) {
      Element cacheKeyGeneratorElement = (Element) cacheKeyGeneratorElements
          .get(0);
      keyGenerator = beanReferenceParser.parse(cacheKeyGeneratorElement,
          parserContext);
    }

    return keyGenerator;
  }

  /**
   * Parses the given XML element containing a caching listener to be added to
   * the caching setup strategy.
   * 
   * @param element
   *          the XML element to parse
   * @param parserContext
   *          the parser context
   * @param index
   *          the index of the given element
   * @return a reference to a caching listener already registered in the given
   *         registry or a new definition of a caching listener, depending how
   *         the given element is configured
   * @throws IllegalStateException
   *           if the given id references a caching listener that is not an
   *           instance of <code>CachingListener</code>
   * @throws IllegalStateException
   *           if the given element does not contain a reference to an existing
   *           caching listener and does not contain an inner definition of a
   *           caching listener
   * 
   * @see BeanReferenceParser#parse(Element, ParserContext, boolean)
   */
  private Object parseCachingListener(Element element,
      ParserContext parserContext, int index) throws IllegalStateException {

    Object cachingListener = beanReferenceParser.parse(element, parserContext,
        true);

    BeanDefinitionRegistry registry = parserContext.getRegistry();
    BeanDefinition beanDefinition = null;

    if (cachingListener instanceof RuntimeBeanReference) {
      String beanName = ((RuntimeBeanReference) cachingListener).getBeanName();
      beanDefinition = registry.getBeanDefinition(beanName);

    } else if (cachingListener instanceof BeanDefinitionHolder) {
      beanDefinition = ((BeanDefinitionHolder) cachingListener)
          .getBeanDefinition();
    }

    validateCachingListenerDefinition((AbstractBeanDefinition) beanDefinition);
    return cachingListener;
  }

  /**
   * Parses the given XML element containing references to the caching listeners
   * to be added to the caching setup strategy.
   * 
   * @param element
   *          the XML element to parse
   * @param parserContext
   *          the parser context
   * @return a list containing references to caching listeners already
   *         registered in the given register
   * @throws IllegalStateException
   *           if any of the given ids reference a caching listener that does
   *           not exist in the registry
   * @throws IllegalStateException
   *           if the given id references a caching listener that is not an
   *           instance of <code>CachingListener</code>
   * @throws IllegalStateException
   *           if the caching listener elements does not contain a reference to
   *           an existing caching listener and does not contain an inner
   *           definition of a caching listener
   */
  private List parseCachingListeners(Element element,
      ParserContext parserContext) throws IllegalStateException {

    List listenersElements = DomUtils.getChildElementsByTagName(element,
        "cachingListeners", true);

    if (CollectionUtils.isEmpty(listenersElements)) {
      return null;
    }

    Element listenersElement = (Element) listenersElements.get(0);
    List listenerElements = DomUtils.getChildElementsByTagName(
        listenersElement, "cachingListener", true);

    ManagedList listeners = new ManagedList();
    int listenerCount = listenerElements.size();

    for (int i = 0; i < listenerCount; i++) {
      Element listenerElement = (Element) listenerElements.get(i);
      Object listener = parseCachingListener(listenerElement, parserContext, i);
      listeners.add(listener);
    }

    return listeners;
  }

  /**
   * Parses the given XML element which sub-elements containing the properties
   * of the caching models to create.
   * 
   * @param element
   *          the XML element to parse
   * @return a map containing the parsed caching models.The key of each element
   *         is the value of the XML attribute <code>target</code> (a String)
   *         and the value is the caching model (an instance of
   *         <code>CachingModel</code>)
   */
  private Map parseCachingModels(Element element) {
    List modelElements = DomUtils.getChildElementsByTagName(element, "caching",
        true);
    if (CollectionUtils.isEmpty(modelElements)) {
      return null;
    }

    int modelElementCount = modelElements.size();
    CacheModelParser modelParser = getCacheModelParser();

    Map models = new HashMap();
    for (int i = 0; i < modelElementCount; i++) {
      Element modelElement = (Element) modelElements.get(i);
      String key = modelElement.getAttribute(XmlAttribute.TARGET);

      CachingModel model = modelParser.parseCachingModel(modelElement);
      models.put(key, model);
    }

    return models;
  }

  /**
   * Parses the given XML element which sub-elements containing the properties
   * of the flushing models to create.
   * 
   * @param element
   *          the XML element to parse
   * @return a map containing the parsed flushing models.The key of each element
   *         is the value of the XML attribute <code>target</code> (a String)
   *         and the value is the flushing model (an instance of
   *         <code>FlushingModel</code>)
   */
  private Map parseFlushingModels(Element element) {
    List modelElements = DomUtils.getChildElementsByTagName(element,
        "flushing", true);
    if (CollectionUtils.isEmpty(modelElements)) {
      return null;
    }

    int modelElementCount = modelElements.size();
    CacheModelParser modelParser = getCacheModelParser();

    Map models = new HashMap();
    for (int i = 0; i < modelElementCount; i++) {
      Element modelElement = (Element) modelElements.get(i);
      String key = modelElement.getAttribute(XmlAttribute.TARGET);

      FlushingModel model = modelParser.parseFlushingModel(modelElement);
      models.put(key, model);
    }

    return models;
  }

  private void validateCachingListenerDefinition(
      AbstractBeanDefinition beanDefinition) {
    Class expectedClass = CachingListener.class;

    if (beanDefinition == null
        || !expectedClass.isAssignableFrom(beanDefinition.getBeanClass())) {
      throw new IllegalStateException(
          "Caching listeners should be instances of <"
              + expectedClass.getName() + ">");
    }
  }
}
