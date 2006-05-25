package org.springmodules.validation;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.ServletContextAware;
import org.springmodules.util.dateparser.DefaultDateParser;
import org.springmodules.validation.functions.Function;
import org.springmodules.validation.predicates.ValidationRule;
import org.springmodules.validation.valang.ParseException;
import org.springmodules.validation.valang.ValangParser;
import org.springmodules.validation.valang.ValangVisitor;

/**
 * <p>
 * ValangValidatorFactoryBean takes a Valang syntax and returns a
 * org.springframework.validation.Validator instance. This instance is a
 * singleton and is thread-safe.
 * <p>
 * The syntax of a Valang instruction is:
 * 
 * <pre>
 * 
 *  { &lt;key&gt; : &lt;expression&gt; : &lt;error_message&gt; [ : &lt;error_key&gt; [ : &lt;error_args&gt; ] ] }
 *  
 * </pre>
 * 
 * <p>
 * These instructions can be repeated and will be combined in a Validator
 * instance. Each instruction will execute the expression on a target bean. If
 * the expression fails the key will be rejected with the error message, error
 * key and error arguments. If no error key is provided the key will be used as
 * error key.
 * <p>
 * Some examples of the Valang syntax:
 * 
 * <pre>
 * 
 *  	&lt;bean id=&quot;myValidator&quot; class=&quot;org.springmodules.validation.ValangValidatorFactoryBean&quot;&gt;
 *  		&lt;property name=&quot;valang&quot;&gt;&lt;value&gt;&lt;![CDATA[
 *  		{ age : ? is not null : 'Age is a required field.' : 'age_required' }
 *  		{ age : ? is null or ? &gt;= minAge : 'Customers must be {0} years or older.' : 'not_old_enough' : minAge }
 *  		{ valueDate : ? is not null : 'Value date is a required field.' : 'valueDate_required' }
 *  		{ valueDate : ? is null or (? &gt;= [T&lt;d] and [T&gt;d] &gt; ?) :
 *  				'Value date must be today.' : 'valueDate_today' }
 *  		{ firstName : ? has text : 'First name is a required field.' : 'firstName_required' }
 *  		{ firstName : ? has no text or length(firstName) &lt;= 50 : 
 *  				'First name must be no longer than {0} characters.' : 'firstName_length' : 50 }
 *  		{ size : ? has length : 'Size is a required field.' }
 *  		{ size : ? has no length or upper(?) in 'S', 'M', 'L', 'XL' :
 *  				'Size must be either {0}, {1}, {2} or {3}.' : 'size_error' : 'S', 'M', 'L', 'XL' }
 *  		{ lastName : ? has text and !(false) = true :
 *  				'Last name is required and not false must be true.' }
 *  		]]&gt;&lt;/value&gt;&lt;/property&gt;
 *  	&lt;/bean&gt;
 *  
 * </pre>
 * 
 * <p>
 * Custom property editors can be registered using
 * org.springmodules.validation.CustomPropertyEditor.
 * <p>
 * A custom visitor can be registered to use custom functions in the Valang
 * syntax.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.springmodules.util.dateparser.DefaultDateParser
 * @see org.springframework.validation.Validator
 */

public class ValangValidatorFactoryBean implements FactoryBean,
		InitializingBean, ApplicationContextAware, BeanFactoryAware, ResourceLoaderAware, MessageSourceAware, ServletContextAware, ApplicationEventPublisherAware {

	private String valang = null;

	private Validator validator = null;

	private ValangVisitor visitor = null;

	private Collection customPropertyEditors = null;

	private Map dateParserRegistrations = null;

	private Map customFunctions = null;
	
	private ApplicationContext applicationContext = null;
	private BeanFactory beanFactory = null;
	private ResourceLoader resourceLoader = null;
	private MessageSource messageSource = null;
	private ServletContext servletContext = null;
	private ApplicationEventPublisher applicationEventPublisher = null;
	
	public ValangValidatorFactoryBean() {

		super();

	}

	/**
	 * <p>
	 * This property sets the Valang syntax.
	 * 
	 * @param valang
	 *            the Valang syntax
	 */

	public void setValang(String valang) {

		this.valang = valang;

	}

	/**
	 * <p>
	 * This property takes a custom visitor with custom functions.
	 * 
	 * @param visitor
	 *            the custom visitor;
	 * @see org.springmodules.validation.valang.DefaultVisitor#setVisitor(ValangVisitor)
	 */

	public void setVisitor(ValangVisitor visitor) {

		this.visitor = visitor;

	}

	private ValangVisitor getVisitor() {
		return this.visitor;
	}
	
	/**
	 * <p>
	 * Sets custom property editors on BeanWrapper instances (optional).
	 * 
	 * @param customPropertyEditors
	 *            the custom editors.
	 * @see BeanWrapper#registerCustomEditor(java.lang.Class, java.lang.String,
	 *      java.beans.PropertyEditor)
	 * @see BeanWrapper#registerCustomEditor(java.lang.Class,
	 *      java.beans.PropertyEditor)
	 */

	public void setCustomPropertyEditors(Collection customPropertyEditors) {

		this.customPropertyEditors = customPropertyEditors;

	}

	/**
	 * <p>
	 * Sets date parser registrations (formats and modifiers) on
	 * DefaultDateParser (optional).
	 * 
	 * @param dateParserRegistrations
	 *            the date parser registrations
	 * @see org.springmodules.util.dateparser.DefaultDateParser#register(String,
	 *      String)
	 * @see org.springmodules.util.dateparser.DefaultDateParser#register(String, org.springmodules.util.dateparser.DefaultDateParser.DateModifier)
	 */
	public void setDateParserRegistrations(Map dateParserRegistrations) {
		this.dateParserRegistrations = dateParserRegistrations;
	}
	
	private Map getDateParserRegistrations() {
		return this.dateParserRegistrations;
	}

	/**
	 * <p>
	 * Takes a map with function names and function class names. Function classes
	 * must have a public constructor with a single org.springmodules.validation.functions.Function
	 * parameter.
	 * 
	 * <p>
	 * These custom functions can be combined with a separate visitor. A function will first be looked
	 * up in this map if present, then in the custom visitor if present and then in the default functions.
	 * 
	 * @param customFunctions map with custom functions
	 */
	public void setCustomFunctions(Map customFunctions) {
		this.customFunctions = customFunctions;
	}
	
	private Map getCustomFunctions() {
		return this.customFunctions;
	}
	
	private Collection getCustomPropertyEditors() {

		return this.customPropertyEditors;

	}

	private String getValang() {

		return this.valang;

	}

	public Object getObject() throws Exception {

		return this.validator;

	}

	public Class getObjectType() {

		return Validator.class;

	}

	public boolean isSingleton() {

		return true;

	}

	public void afterPropertiesSet() throws Exception {

		if (!StringUtils.hasLength(getValang())) {

			throw new IllegalArgumentException("[valang] property must be set!");

		}

		this.validator = new Validator() {

			private Collection rules = null;

			{

				try {

					ValangParser parser = new ValangParser(new StringReader(
							getValang()));

					parser.getVisitor().setApplicationContext(applicationContext);
					parser.getVisitor().setBeanFactory(beanFactory);
					parser.getVisitor().setApplicationEventPublisher(applicationEventPublisher);
					parser.getVisitor().setMessageSource(messageSource);
					parser.getVisitor().setResourceLoader(resourceLoader);
					parser.getVisitor().setServletContext(servletContext);
					
					if (getDateParserRegistrations() != null) {
						for (Iterator iter = getDateParserRegistrations()
								.keySet().iterator(); iter.hasNext();) {
							String regexp = (String) iter.next();
							Object value = getDateParserRegistrations().get(
									regexp);

							if (value instanceof String) {
								parser.getVisitor().getDateParser().register(
										regexp, (String) value);
							} else if (value instanceof DefaultDateParser.DateModifier) {
								parser.getVisitor().getDateParser().register(
										regexp,
										(DefaultDateParser.DateModifier) value);
							} else {
								throw new ClassCastException(
										"Could not register instance [" + value
												+ "] with date parser!");
							}
						}
					}

					if (getCustomFunctions() != null) {
						final Map constructorMap = new HashMap();
						for (Iterator iter = getCustomFunctions().keySet().iterator(); iter.hasNext();) {
							Object stringNameObject = iter.next();
							if (!(stringNameObject instanceof String)) {
								throw new IllegalArgumentException("Key for custom functions map must be a string value!");
							}
							String functionName = (String)stringNameObject;
							Object functionClassNameObject = getCustomFunctions().get(functionName);
							if (!(functionClassNameObject instanceof String)) {
								throw new IllegalArgumentException("Value for custom function map must be a string!");
							}
							String functionClassName = (String)functionClassNameObject;
							Class functionClass = Class.forName(functionClassName);							
							if (!(Function.class.isAssignableFrom(functionClass))) {
								throw new IllegalArgumentException("Custom function classes must implement org.springmodules.validation.functions.Function!");
							}
							Constructor constructor = null;
							try {
								constructor = functionClass.getConstructor(new Class[] { Function[].class, int.class, int.class });
							} catch (NoSuchMethodException e) {
								throw new IllegalArgumentException("Class [" + functionClass.getName() + "] has no constructor with one org.springmodules.validation.functions.Function parameter!");
							}
							constructorMap.put(functionName, constructor);
						}
						parser.getVisitor().setVisitor(new ValangVisitor() {
							public Function getFunction(String name, Function[] functions, int line, int column) {
								if (constructorMap.containsKey(name)) {
									Constructor functionConstructor = (Constructor)constructorMap.get(name);
									return (Function)BeanUtils.instantiateClass(functionConstructor, new Object[] { functions, new Integer(line), new Integer(column) });
								} else if (getVisitor() != null) {
									return getVisitor().getFunction(name, functions, line, column);
								}
								return null;
							}
						});
					} else {
						parser.getVisitor().setVisitor(visitor);
					}
					
					rules = parser.parseValidation();

				} catch (ParseException e) {

					throw new RuntimeException(e);

				}

			}

			public boolean supports(Class clazz) {

				return true;

			}

			public void validate(Object target, Errors errors) {

				BeanWrapper beanWrapper = null;

				if (target instanceof BeanWrapper) {

					beanWrapper = (BeanWrapper) target;

				} else {

					beanWrapper = new BeanWrapperImpl(target);

				}

				if (getCustomPropertyEditors() != null) {

					for (Iterator iter = getCustomPropertyEditors().iterator(); iter
							.hasNext();) {

						CustomPropertyEditor customPropertyEditor = (CustomPropertyEditor) iter
								.next();

						if (customPropertyEditor.getRequiredType() == null) {

							throw new IllegalArgumentException(
									"[requiredType] is required on CustomPropertyEditor instances!");

						} else if (customPropertyEditor.getPropertyEditor() == null) {

							throw new IllegalArgumentException(
									"[propertyEditor] is required on CustomPropertyEditor instances!");

						}

						if (StringUtils.hasLength(customPropertyEditor
								.getPropertyPath())) {

							beanWrapper.registerCustomEditor(
									customPropertyEditor.getRequiredType(),
									customPropertyEditor.getPropertyPath(),
									customPropertyEditor.getPropertyEditor());

						} else {

							beanWrapper.registerCustomEditor(
									customPropertyEditor.getRequiredType(),
									customPropertyEditor.getPropertyEditor());

						}

					}

				}

				for (Iterator iter = rules.iterator(); iter.hasNext();) {

					ValidationRule rule = (ValidationRule) iter.next();

					rule.validate(beanWrapper, errors);

				}

			}

		};

	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

}