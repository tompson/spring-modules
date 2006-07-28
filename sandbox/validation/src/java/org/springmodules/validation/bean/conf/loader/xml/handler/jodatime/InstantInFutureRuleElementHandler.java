/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.bean.conf.loader.xml.handler.jodatime;

import java.beans.PropertyDescriptor;

import org.joda.time.Instant;
import org.springmodules.validation.bean.conf.loader.xml.handler.AbstractPropertyValidationElementHandler;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.date.jodatime.IsInTheFutureInstantCondition;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that can handle an element that represents an "in the
 * future" joda-time instant validation rule.
 *
 * @author Uri Boness
 */
public class InstantInFutureRuleElementHandler extends AbstractPropertyValidationElementHandler {

    /**
     * The default error code for the parsed validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = "in.future";

    private static final String ELEMENT_NAME = "in-future";

    /**
     * Constructs a new InstantInFutureRuleElementHandler.
     */
    public InstantInFutureRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    /**
     * In addition to the element name and namespace check, this handler only support properties of type
     * {@link org.joda.time.Instant}.
     *
     * @see org.springmodules.validation.bean.conf.loader.xml.PropertyValidationElementHandler#supports(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Element element, Class clazz, PropertyDescriptor descriptor) {
        return super.supports(element, clazz, descriptor) && Instant.class.isAssignableFrom(descriptor.getPropertyType());
    }

    /**
     * Returns {@link #DEFAULT_ERROR_CODE}.
     *
     * @see AbstractPropertyValidationElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        return InstantInFutureRuleElementHandler.DEFAULT_ERROR_CODE;
    }

    /**
     * Creates and returns a new {@link IsInTheFutureInstantCondition}.
     *
     * @see AbstractPropertyValidationElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        return new IsInTheFutureInstantCondition();
    }

}
