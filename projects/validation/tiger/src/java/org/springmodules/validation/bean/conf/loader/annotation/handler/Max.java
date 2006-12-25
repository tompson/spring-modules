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

package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.*;

import org.springmodules.validation.bean.rule.MaxValidationRule;

/**
 * Represents a validation rule that checks whether the a numeric instance is less then (or equals) a specific upper
 * bound.
 *
 * @author Uri Boness
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@ValidationRule
public @interface Max {

    /**
     * The upper bound of the numeric instance value
     */
    double value();

    /**
     * Indicates whether the upper bound is a legal value of the checked value.
     */
    boolean inclusive() default true;

    /**
     * Returns the error code that represents the error when the validation fails.
     */
    String errorCode() default MaxValidationRule.DEFAULT_ERROR_CODE;

    /**
     * Returns the default message that represents the error when the validation fails.
     */
    String message() default MaxValidationRule.DEFAULT_ERROR_CODE;

    /**
     * Comma-delimited list of arguments to be attached to the error code
     */
    String args() default "";

    /**
     * An condition expressed in an expression language (e.g. OGNL, Valag) that determines when
     * this validation rule should be applied.
     */
    String applyIf() default "";

    /**
     * A list of context in which this validation rule is applicable. Empty list means this rule is always applicable
     * regardless the validation context.
     */
    String[] contexts() default {};

}
