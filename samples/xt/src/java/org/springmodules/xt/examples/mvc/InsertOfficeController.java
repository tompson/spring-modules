package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springmodules.xt.examples.domain.BusinessException;
import org.springmodules.xt.examples.domain.Error;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.Office;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springmodules.xt.utils.mvc.controller.EnhancedSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Form controller for inserting an office.
 *
 * @author Sergio Bossa
 */
public class InsertOfficeController extends EnhancedSimpleFormController {
    
    private MemoryRepository store;
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new Office();
    }
    
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Collection emps = store.getEmployees();
        Map result = new HashMap();
        
        result.put("employees", emps);
        
        return result;
    }
    
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        IOffice office = (IOffice) command;
        
        try {
            this.store.addOffice(office);
        }
        catch(BusinessException ex) {
            for (Error error : ex.getErrors()) {
                errors.rejectValue(error.getPropertyName(), error.getCode(), error.getMessage());
            }
            return this.showForm(request, response, errors);
        }
        
        return new ModelAndView(this.getSuccessView());
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
