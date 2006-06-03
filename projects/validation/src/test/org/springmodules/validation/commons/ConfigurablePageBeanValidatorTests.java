package org.springmodules.validation.commons;

import java.util.Locale;

import junit.framework.TestCase;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;
import org.easymock.MockControl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

/**
 *
 * @author robh
 * @author Uri Boness
 */
public class ConfigurablePageBeanValidatorTests extends TestCase {

     public void testSupports() {
        ConfigurablePageBeanValidator validator = getValidator(0);

        assertTrue("Should support FooBean", validator.supports(FooBean.class));
        assertTrue("Should support String", validator.supports(String.class));
    }

    public void testGetLocale() {
        ConfigurablePageBeanValidator validator = getValidator(0);
        assertEquals(validator.getLocale(), Locale.getDefault());
    }

    public void testValidate_WithPageSetting() {
        ConfigurablePageBeanValidator validator = getValidator(0);

        FooBean bean = new FooBean();
        BindException errors = new BindException(bean, "pagedFooBean");

        validator.validate(bean, errors);
        assertTrue(errors.hasFieldErrors("name"));
        assertTrue(errors.hasFieldErrors("name"));
        assertEquals(1, errors.getErrorCount());
        assertEquals("name", ((FieldError)errors.getAllErrors().iterator().next()).getField());

        errors = new BindException(bean, "pagedFooBean");
        bean.setName("name");
        validator.validate(bean, errors);
        assertFalse(errors.hasFieldErrors("name"));
    }

    public void testValidate() {
        FooBean bean = new FooBean();
        String formName = "myForm";

        MockControl ctl = MockControl.createControl(ValidatorFactory.class);
        ValidatorFactory factory = (ValidatorFactory)ctl.getMock();

        // expect call to get validator with args "fooBean", bean and null
        factory.getValidator(formName, bean, null);
        ctl.setReturnValue(getCommonsValidator());

        ctl.replay();

        ConfigurablePageBeanValidator validator = new ConfigurablePageBeanValidator();
        validator.setValidatorFactory(factory);
        validator.setFormName(formName);
        validator.setPage(0);

        validator.validate(bean, null);

        ctl.verify();
    }

    private ConfigurablePageBeanValidator getValidator(int page) {
        ConfigurablePageBeanValidator validator = new ConfigurablePageBeanValidator();
        validator.setFormName("pagedFooBean");
        validator.setPage(page);
        DefaultValidatorFactory factory = new DefaultValidatorFactory();
        factory.setValidationConfigLocations(new Resource[]{
            new ClassPathResource("testValidation.xml", getClass()),
            new ClassPathResource("validation-rules.xml", getClass())
        });

        validator.setValidatorFactory(factory);
        return validator;
    }

    private Validator getCommonsValidator() {
        ValidatorResources res = new ValidatorResources();
        res.process();
        return new Validator(res);
    }

}
