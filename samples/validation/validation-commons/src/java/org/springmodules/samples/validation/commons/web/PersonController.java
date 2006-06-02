package org.springmodules.samples.validation.commons.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author Thierry Templier
 */
public class PersonController extends SimpleFormController {

    protected ModelAndView onSubmit(HttpServletRequest request,
    		HttpServletResponse response, Object command, BindException errors) throws Exception { 

        return showForm(request,response,errors);
    }
}
