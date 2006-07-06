package org.springmodules.validation.bean.conf.annotation.handler;

import java.lang.annotation.Annotation;

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.annotation.ClassValidationAnnotationHandler;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.bean.rule.ErrorArgumentsResolver;
import org.springmodules.validation.bean.rule.resolver.ExpressionErrorArgumentsResolver;
import org.springmodules.validation.util.bel.BeanExpressionResolver;
import org.springmodules.validation.util.bel.BeanExpressionResolverAware;
import org.springmodules.validation.util.bel.resolver.ValangFunctionExpressionResolver;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;
import org.springmodules.validation.util.condition.parser.ConditionParser;
import org.springmodules.validation.util.condition.parser.ConditionParserAware;
import org.springmodules.validation.util.condition.parser.valang.ValangConditionParser;

/**
 * A parent for all common {@link org.springmodules.validation.bean.conf.annotation.PropertyValidationAnnotationHandler} implementations that represent validation rules.
 * The following attributes are searched for in the handled annotations:
 * <ul>
 *  <li>{@link #ERROR_CODE_ATTR} - A string indicating the error code for the validation rule</li>
 *  <li>{@link #MESSAGE_ATTR} - A string indicating default error message for the validation rule</li>
 *  <li>{@link #ARGS_ATTR} - A comma-separated string indicating error arguments (may be expressions to be
 *                           resolved against the validated object)</li>
 *  <li>{@link #APPLY_IF_ATTR} - A condition expression that represents the applicability condition</li>
 * </ul>
 *
 * @author Uri Boness
 */
public abstract class AbstractClassValidationAnnotationHandler implements ClassValidationAnnotationHandler,
    ConditionParserAware, BeanExpressionResolverAware {

    public final static String APPLY_IF_ATTR = "applyIf";
    public final static String ERROR_CODE_ATTR = "errorCode";
    public final static String MESSAGE_ATTR = "message";
    public final static String ARGS_ATTR = "args";

    private Class[] supportedAnnotationTypes;
    private ConditionParser conditionParser;
    private BeanExpressionResolver beanExpressionResolver;

    /**
     * Constructs a new AbstractPropertyValidationAnnotationHandler with a given supported annotation types.
     *
     * @param supportedAnnotationTypes The types of the supported annotations.
     */
    public AbstractClassValidationAnnotationHandler(Class... supportedAnnotationTypes) {
        this.supportedAnnotationTypes = supportedAnnotationTypes;
        conditionParser = new ValangConditionParser();
        beanExpressionResolver = new ValangFunctionExpressionResolver();
    }

    /**
     * @see org.springmodules.validation.bean.conf.annotation.ClassValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class)
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
     * @see org.springmodules.validation.bean.conf.annotation.ClassValidationAnnotationHandler#handleAnnotation(java.lang.annotation.Annotation, Class, org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration)
     */
    public void handleAnnotation(
        Annotation annotation,
        Class clazz,
        MutableBeanValidationConfiguration configuration) {

        Condition condition = extractCondition(annotation, clazz);
        String errorCode = extractErrorCode(annotation);
        String message = extractDefaultMessage(annotation);
        ErrorArgumentsResolver argumentsResolver = extractArgumentsResolver(annotation);
        Condition applicabilityCondition = extractApplicabilityContidion(annotation);
        DefaultValidationRule rule = new DefaultValidationRule(condition, applicabilityCondition, errorCode, message, argumentsResolver);
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
        try {
            return (String) annotation.getClass().getMethod(AbstractClassValidationAnnotationHandler.ERROR_CODE_ATTR).invoke(annotation);
        } catch (Exception e) {
            throw new IllegalArgumentException("Expecting attribute '" + AbstractClassValidationAnnotationHandler.ERROR_CODE_ATTR + "' in annotation '" + annotation.getClass().getName());
        }
    }

    /**
     * Extracts the validation rule default message from the given annotation. Expects a {@link #MESSAGE_ATTR} to be
     * present.
     *
     * @param annotation The property validation annotation.
     * @return The default message for the represented validation rule.
     */
    protected String extractDefaultMessage(Annotation annotation) {
        try {
            return (String) annotation.getClass().getMethod(AbstractClassValidationAnnotationHandler.MESSAGE_ATTR).invoke(annotation);
        } catch (Exception e) {
            throw new IllegalArgumentException("Expecting attribute '" + AbstractClassValidationAnnotationHandler.MESSAGE_ATTR + "' in annotation '" + annotation.getClass().getName());
        }
    }

    /**
     * Extracts the {@link org.springmodules.validation.bean.rule.ErrorArgumentsResolver} for the validation rule represented by the given annotation.
     * Expects a {@link #ARGS_ATTR} to be present.
     *
     * @param annotation The property validation annotation.
     * @return The {@link org.springmodules.validation.bean.rule.ErrorArgumentsResolver} for the represented validation rule.
     */
    protected ErrorArgumentsResolver extractArgumentsResolver(Annotation annotation) {
        try {
            String argsAsString = (String) annotation.getClass().getMethod(AbstractClassValidationAnnotationHandler.ARGS_ATTR).invoke(annotation);
            argsAsString = (argsAsString == null) ? "" : argsAsString;
            String[] argsExpressions = StringUtils.commaDelimitedListToStringArray(argsAsString);
            return new ExpressionErrorArgumentsResolver(argsExpressions, beanExpressionResolver);
        } catch (Exception e) {
            throw new IllegalArgumentException("Expecting attribute '" + AbstractClassValidationAnnotationHandler.ARGS_ATTR + "' in annotation '" + annotation.getClass().getName());
        }
    }

    /**
     * Extracts the validation rule applicability condition from the given annotation. Expects a {@link #APPLY_IF_ATTR}
     * to be present - the value of this attribute is parsed using the {@link org.springmodules.validation.util.condition.parser.ConditionParser} associated with this
     * handler. If the attribute is an empty string or <code>null</code> it is treated as if the validation rule is
     * always applicable (that is, the applicability condition is {@link org.springmodules.validation.util.condition.common.AlwaysTrueCondition}).
     *
     * @param annotation The property validation annotation.
     * @return The applicability condition for the represented validation rule.
     */
    protected Condition extractApplicabilityContidion(Annotation annotation) {
        try {
            String expression = (String)annotation.getClass().getMethod(AbstractClassValidationAnnotationHandler.APPLY_IF_ATTR).invoke(annotation);
            return (StringUtils.hasText(expression)) ? conditionParser.parse(expression) : new AlwaysTrueCondition();
        } catch (Exception e) {
            throw new IllegalArgumentException("Expecting attribute '" + AbstractClassValidationAnnotationHandler.APPLY_IF_ATTR + "' in annotation '" + annotation.getClass().getName());
        }
    }

    /**
     * Extracts the condition of the validation rule represented by the given annotation.
     *
     * @param annotation The annotation from which the condition should be extracted.
     * @param clazz The annotated class.
     * @return The extracted condition.
     */
    protected abstract Condition extractCondition(Annotation annotation, Class clazz);

    //=============================================== Setter/Getter ====================================================

    /**
     * Sets the {@link org.springmodules.validation.util.condition.parser.ConditionParser} to be used. This parser will
     * be used to parse the applicability condition expressions.
     *
     * @param conditionParser The condition parser to be used.
     */
    public void setConditionParser(ConditionParser conditionParser) {
        this.conditionParser = conditionParser;
    }

    /**
     * Returns the used {@link org.springmodules.validation.util.condition.parser.ConditionParser}.
     *
     * @return The used condition parser.
     * @see #setConditionParser(org.springmodules.validation.util.condition.parser.ConditionParser)
     */
    public ConditionParser getConditionParser() {
        return conditionParser;
    }

    /**
     * Sets the {@link org.springmodules.validation.util.bel.BeanExpressionResolver} to be used. This resolver will
     * be used to resolve the values of the error arguments.
     *
     * @param beanExpressionResolver The bean expression resolver to be used.
     */
    public void setBeanExpressionResolver(BeanExpressionResolver beanExpressionResolver) {
        this.beanExpressionResolver = beanExpressionResolver;
    }

    /**
     * Returns the used {@link org.springmodules.validation.util.bel.BeanExpressionResolver}.
     *
     * @return The used bean expression resolver.
     * @see #setBeanExpressionResolver(org.springmodules.validation.util.bel.BeanExpressionResolver)
     */
    public BeanExpressionResolver getBeanExpressionResolver() {
        return beanExpressionResolver;
    }

}
