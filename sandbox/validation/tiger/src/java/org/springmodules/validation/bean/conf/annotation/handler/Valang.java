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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import org.springmodules.validation.bean.conf.xml.handler.ValangPropertyValidationElementHandler;

/**
 * Represents a validation rule that is expressed using the Valang expression language.
 *
 * @author Uri Boness
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Valang {

    /**
     * The Valang expression.
     */
    String value();

    /**
     * Defines the scope of the expression evaluation. This attribute only applies when this annotation is
     * defined on a field or a method. For a class annotation the class instance will always be the evaluation scope.
     * The scope is set to {@link ExpressionScope.CONTAINING_OBJECT} by default.
     */
    ExpressionScope scope() default ExpressionScope.CONTAINING_OBJECT;

    /**
     * Returns the error code that represents the error when the validation fails.
     */
    String errorCode() default ValangPropertyValidationElementHandler.DEFAULT_ERROR_CODE;

    /**
     * Returns the default message that represents the error when the validation fails.
     */
    String message() default ValangPropertyValidationElementHandler.DEFAULT_ERROR_CODE;

    /**
     * Comma-delimited list of arguments to be attached to the error code.
     */
    String args() default "";

    /**
     * An condition expressed in an expression language (e.g. OGNL, Valag) that determines when
     * this validation rule should be applied.
     */
    String applyIf() default "";
}
