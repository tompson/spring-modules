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

package org.springmodules.validation.bean.conf.xml.parser.jodatime;

import org.springmodules.validation.bean.conf.xml.parser.AbstractValidationRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.DefaultXmBeanValidationConfigurationlLoaderConstants;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.date.jodatime.IsInTheFutureInstantCondition;
import org.w3c.dom.Element;

/**
 * An {@link AbstractValidationRuleElementHandler} implementation that can handle an element that represents an "in the
 * future" joda-time instant validation rule.
 *
 * @author Uri Boness
 */
public class InstantInFutureRuleElementHandler extends AbstractValidationRuleElementHandler
    implements DefaultXmBeanValidationConfigurationlLoaderConstants {

    /**
     * The default error code for the parsed validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = "in.future";

    private static final String ELEMENT_NAME = "in-future";

    /**
     * Constructs a new InstantInFutureRuleElementHandler.
     */
    public InstantInFutureRuleElementHandler() {
        super(InstantInFutureRuleElementHandler.ELEMENT_NAME, DEFAULT_NAMESPACE_URL);
    }

    /**
     * Returns {@link #DEFAULT_ERROR_CODE}.
     *
     * @see AbstractValidationRuleElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        return InstantInFutureRuleElementHandler.DEFAULT_ERROR_CODE;
    }

    /**
     * Creates and returns a new {@link IsInTheFutureInstantCondition}.
     *
     * @see AbstractValidationRuleElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        return new IsInTheFutureInstantCondition();
    }

}
