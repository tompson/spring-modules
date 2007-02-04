package org.springmodules.xt.examples.ajax;

import java.util.Locale;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springframework.context.MessageSource;
import org.springframework.validation.ObjectError;
import org.springmodules.xt.ajax.validation.support.DefaultErrorRenderingCallback;

/**
 * Callback for rendering errors on forms.
 *
 * @author Sergio Bossa
 */
public class FormErrorRenderingCallback extends DefaultErrorRenderingCallback { 
    
    public Component getRenderingComponent(ObjectError error, MessageSource messageSource, Locale locale) {
        TaggedText text = new TaggedText(messageSource.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), locale), TaggedText.Tag.DIV);
        text.addAttribute("style","color : red;");
        return text;
    }
}
