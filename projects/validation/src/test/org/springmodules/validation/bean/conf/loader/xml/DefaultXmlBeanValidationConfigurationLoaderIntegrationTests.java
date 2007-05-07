package org.springmodules.validation.bean.conf.loader.xml;

import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springmodules.validation.bean.BeanValidator;
import org.springmodules.validation.bean.context.ValidationContextUtils;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.ResourceConfigurationLoadingException;
import org.springmodules.validation.util.cel.ognl.OgnlConditionExpressionParser;
import org.springmodules.validation.util.fel.parser.OgnlFunctionExpressionParser;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.xml.DefaultXmlBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class DefaultXmlBeanValidationConfigurationLoaderIntegrationTests extends TestCase {

    public void testLoadConfiguration() throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("Person.vld.xml");
        BeanValidationConfiguration config = loader.loadConfiguration(Person.class);
        assertEquals(2, config.getGlobalRules().length);

        Person person = new Person();
        person.setFirstName("Uri");
        person.setLastName("Boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        BindException errors = new BindException(person, "person");
        BeanValidator validator = new BeanValidator(loader);
        validator.validate(person, errors);

        assertEquals(2, errors.getGlobalErrorCount());
        assertEquals(1, errors.getFieldErrorCount("smallInteger"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));
        assertEquals("Person.lastName[validateLastNameIsLongerThanTen()]", errors.getFieldError("lastName").getCode());

    }

    public void testValidateWithContext() throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("PersonWithContext.vld.xml");
        BeanValidationConfiguration config = loader.loadConfiguration(Person.class);
        assertEquals(2, config.getGlobalRules().length);

        BeanValidator validator = new BeanValidator(loader);

        Person person = new Person();
        person.setFirstName("Uri");
        person.setLastName("Boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        ValidationContextUtils.setContext("ctx1");

        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertEquals(1, errors.getGlobalErrorCount());
        assertFalse(errors.hasFieldErrors("smallInteger"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));

        ValidationContextUtils.clearContext();

        ValidationContextUtils.setContext("ctx2");

        errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals(1, errors.getFieldErrorCount("smallInteger"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));
        assertEquals("Person.lastName[validateLastNameIsLongerThanTen()]", errors.getFieldError("lastName").getCode());

        ValidationContextUtils.clearContext();

    }

    public void testLoadDefaultConfiguration() throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = new DefaultXmlBeanValidationConfigurationLoader();
        loader.afterPropertiesSet();
        BeanValidationConfiguration conf = loader.loadConfiguration(Person.class);
        assertNotNull(conf);
    }

    public void testValidationBean_WhenDeployedInApplicationContext() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("appCtxt.xml", getClass());
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("TestBean.vld.xml", context);

        BeanValidator validator = new BeanValidator(loader);
        
        TestBean bean = new TestBean();
        BindException errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("name"));

        bean = new TestBean("name");
        errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidationBean_WhenNotDeployedInApplicationContext() throws Exception {
        try {
            DefaultXmlBeanValidationConfigurationLoader loader = createLoader("TestBean.vld.xml");
            fail("Expecting an UnsupportedOperationException for the configuration loader is not deployed in " +
                "an application context");
        } catch (ResourceConfigurationLoadingException rcle) {
            // expected
        }
    }

    protected DefaultXmlBeanValidationConfigurationLoader createLoader(String resource) throws Exception {
        return createLoader(resource, null);
    }

    protected DefaultXmlBeanValidationConfigurationLoader createLoader(String resource, ApplicationContext context) throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = new DefaultXmlBeanValidationConfigurationLoader();
        loader.setResource(new ClassPathResource(resource, getClass()));
        loader.setConditionExpressionParser(new OgnlConditionExpressionParser());
        loader.setFunctionExpressionParser(new OgnlFunctionExpressionParser());
        loader.setApplicationContext(context);
        loader.afterPropertiesSet();
        return loader;
    }

}