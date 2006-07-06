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

package org.springmodules.validation.bean.conf.annotation.handler;

import java.lang.annotation.Annotation;
import java.beans.PropertyDescriptor;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that handles {@link @NotNull} annotations
 * for which it creates a validation rule that validates that the validated instance is not null.
 *
 * @author Uri Boness
 */
public class NotNullValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new NotNullValidationAnnotationHandler.
     */
    public NotNullValidationAnnotationHandler() {
        super(NotNull.class);
    }

    /**
     * Creates a condition that checks thtat the given object is not null.
     */
    protected Condition extractCondition(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return Conditions.notNull();
    }

}
