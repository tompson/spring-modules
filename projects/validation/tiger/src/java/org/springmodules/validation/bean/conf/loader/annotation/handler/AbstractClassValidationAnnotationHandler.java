/*
 * Copyright 2002-2005 the original author or authors.
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

import java.lang.annotation.Annotation;

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver;
import org.springmodules.validation.bean.rule.resolver.FunctionErrorArgumentsResolver;
import org.springmodules.validation.util.cel.ConditionExpressionBased;
import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.fel.FunctionExpressionBased;
import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.parser.ValangFunctionExpressionParser;

/**
 * A parent for all common {@link PropertyValidationAnnotationHandler} implementations that represent validation rules.
 * The following attributes are searched for in the handled annotations:
 * <ul>
 * <li>{@link #ERROR_CODE_ATTR} - A string indicating the error code for the validation rule</li>
 * <li>{@link #MESSAGE_ATTR} - A string indicating default error message for the validation rule</li>
 * <li>{@link #ARGS_ATTR} - A comma-separated string indicating error arguments (may be expressions to be
 * resolved against the validated object)</li>
 * <li>{@link #APPLY_IF_ATTR} - A condition expression that represents the applicability condition</li>
 * </ul>
 *
 * @author Uri Boness
 */
public abstract class AbstractClassValidationAnnotationHandler implements ClassValidationAnnotationHandler,
    ConditionExpressionBased, FunctionExpressionBased {

    public final static String APPLY_IF_ATTR = "applyIf";

    public final static String ERROR_CODE_ATTR = "errorCode";

    public final static String MESSAGE_ATTR = "message";

    public final static String ARGS_ATTR = "args";

    public final static String CONTEXTS_ATTR = "contexts";

    private Class[] supportedAnnotationTypes;

    private ConditionExpressionParser conditionExpressionParser;

    private FunctionExpressionParser functionExpressionParser;

    /**
     * Constructs a new AbstractPropertyValidationAnnotationHandler with a given supported annotation types.
     *
     * @param supportedAnnotationTypes The types of the supported annotations.
     */
    public AbstractClassValidationAnnotationHandler(Class... supportedAnnotationTypes) {
        this.supportedAnnotationTypes = supportedAnnotationTypes;
        conditionExpressionParser = new ValangConditionExpressionParser();
        functionExpressionParser = new ValangFunctionExpressionParser();
    }

    /**
     * @see org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class)
     */
    public boolean supports(Annotation annotation, Class clazz) {
        for (Class supportedType : supportedAnnotationTypes) {
            if (supportedType.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a validation rule out of the given class validation annoation and adds it to the given
     * bean validation configuration.
     *
     * @see org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler#handleAnnotation(java.lang.annotation.Annotation, Class, org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration)
     */
    public void handleAnnotation(
        Annotation annotation,
        Class clazz,
        MutableBeanValidationConfiguration configuration) {

        AbstractValidationRule rule = createValidationRule(annotation, clazz);

        String errorCode = extractErrorCode(annotation);
        if (errorCode != null) {
            rule.setErrorCode(errorCode);
        }

        String message = extractDefaultMessage(annotation);
        if (message != null) {
            rule.setDefaultErrorMessage(message);
        }

        ErrorArgumentsResolver argumentsResolver = extractArgumentsResolver(annotation);
        if (argumentsResolver != null) {
            rule.setErrorArgumentsResolver(argumentsResolver);
        }

        String[] applicableContexts = extractApplicableContexts(annotation);
        if (applicableContexts != null) {
            rule.setContextTokens(applicableContexts);
        }

        Condition applicabilityCondition = extractApplicabilityContidion(annotation);
        if (applicabilityCondition != null) {
            rule.setApplicabilityCondition(applicabilityCondition);
        }

        configuration.addGlobalRule(rule);
    }

    /**
     * Extracts the validation rule error code from the given annotation. Expects a {@link #ERROR_CODE_ATTR} to be
     * present.
     *
     * @param annotation The property validation annotation.
     * @return The error code for the represented validation rule.
     */
    protected String extractErrorCode(Annotation annotation) {
        return (String) extractAnnotationAttribute(annotation, ERROR_CODE_ATTR);
    }

    /**
     * Extracts the validation rule default message from the given annotation. Expects a {@link #MESSAGE_ATTR} to be
     * present.
     *
     * @param annotation The property validation annotation.
     * @return The default message for the represented validation rule.
     */
    protected String extractDefaultMessage(Annotation annotation) {
        return (String) extractAnnotationAttribute(annotation, MESSAGE_ATTR);
    }

    /**
     * Extracts the {@link org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver} for the validation rule represented by the given annotation.
     * Expects a {@link #ARGS_ATTR} to be present.
     *
     * @param annotation The property validation annotation.
     * @return The {@link org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver} for the represented validation rule.
     */
    protected ErrorArgumentsResolver extractArgumentsResolver(Annotation annotation) {
        String argsAsString = (String) extractAnnotationAttribute(annotation, ARGS_ATTR);
        argsAsString = (argsAsString == null) ? "" : argsAsString;
        String[] argsExpressions = StringUtils.commaDelimitedListToStringArray(argsAsString);
        if (argsExpressions.length == 0) {
            return null;
        }
        return new FunctionErrorArgumentsResolver(argsExpressions, functionExpressionParser);
    }

    /**
     * Extracts the validation rule applicability condition from the given annotation. Expects a {@link #APPLY_IF_ATTR}
     * to be present - the value of this attribute is parsed using the {@link org.springmodules.validation.util.cel.ConditionExpressionParser} associated with this
     * handler. If the attribute is an empty string or <code>null</code> it is treated as if the validation rule is
     * always applicable (that is, the applicability condition is {@link org.springmodules.validation.util.condition.common.AlwaysTrueCondition}).
     *
     * @param annotation The property validation annotation.
     * @return The applicability condition for the represented validation rule.
     */
    protected Condition extractApplicabilityContidion(Annotation annotation) {
        String expression = (String) extractAnnotationAttribute(annotation, APPLY_IF_ATTR);
        return (StringUtils.hasText(expression)) ? conditionExpressionParser.parse(expression) : null;
    }

    /**
     * Extracts the names of the validation context in which the valiation rule is applicable. Expects a "contexts"
     * attribute to hold an array of context names. If it holds an empty array, <code>null </code> is returned.
     * As the contract of {@link AbstractValidationRule#setContextTokens(String[])}
     * defines <code>null</code> means that the rule always applies regardless of the context.
     *
     * @param annotation The validation rule annoation.
     * @return The names of the validation contexts in which the
     */
    protected String[] extractApplicableContexts(Annotation annotation) {
        String[] contexts = (String[]) extractAnnotationAttribute(annotation, CONTEXTS_ATTR);
        return (contexts.length > 0) ? contexts : null;
    }

    /**
     * Creates and returns the validation rule that is represented and initialized by and with the given annotation.
     *
     * @param annotation The annotation from which the validation rule should be created.
     * @param clazz The class of the valiated object.
     * @return The created validation rule.
     */
    protected abstract AbstractValidationRule createValidationRule(Annotation annotation, Class clazz);

    //=============================================== Setter/Getter ====================================================

    /**
     * @see ConditionExpressionBased#setConditionExpressionParser(org.springmodules.validation.util.cel.ConditionExpressionParser)
     */
    public void setConditionExpressionParser(ConditionExpressionParser conditionExpressionParser) {
        this.conditionExpressionParser = conditionExpressionParser;
    }

    /**
     * Returns the condition expression parser associated with this handler.
     *
     * @return The condition expression parser associated with this handler.
     */
    protected ConditionExpressionParser getConditionExpressionParser() {
        return conditionExpressionParser;
    }

    /**
     * @see FunctionExpressionBased#setFunctionExpressionParser(org.springmodules.validation.util.fel.FunctionExpressionParser)
     */
    public void setFunctionExpressionParser(FunctionExpressionParser functionExpressionParser) {
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * Returns the function expression parser associated with this handler.
     *
     * @return The function expression parser associated with this handler.
     */
    protected FunctionExpressionParser getFunctionExpressionParser() {
        return functionExpressionParser;
    }

    //================================================== Helper Methods ================================================

    protected Object extractAnnotationAttribute(Annotation annotation, String attributeName) {
        try {
            return annotation.getClass().getMethod(attributeName).invoke(annotation);
        } catch (Exception e) {
            throw new IllegalArgumentException("Expecting attribute '" + attributeName +
                "' in annotation '" + annotation.getClass().getName() + "'", e);
        }
    }

}
