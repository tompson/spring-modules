package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.OptionList;
import org.springmodules.xt.examples.domain.MemoryRepository;

/**
 * Ajax handler for loading offices.
 *
 * @author Sergio Bossa
 */
public class LoadOfficesHandler extends AbstractAjaxHandler {
    
    private MemoryRepository store;
    
    public AjaxResponse loadOffices(AjaxActionEvent event) {
        // Load offices:
        Collection offices = store.getOffices();
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create the component to render (a list of html option element):
        OptionList options = new OptionList(offices.toArray(), null, "officeId", "name");
        options.setFirstTextOption("-1", "Select one ...");
        // Create an ajax action for replacing the content of the "offices" element with the component just created: 
        ReplaceContentAction action = new ReplaceContentAction("offices", options);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }

    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
