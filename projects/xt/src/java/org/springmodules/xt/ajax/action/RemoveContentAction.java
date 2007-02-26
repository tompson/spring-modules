/*
 * Copyright 2006 the original author or authors.
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

package org.springmodules.xt.ajax.action;

import java.util.ArrayList;

/**
 * Taconite based ajax action for removing the whole content of a given element.
 *
 * @author Sergio Bossa
 */
public class RemoveContentAction extends AbstractRenderingAction {
    
    private static final long serialVersionUID = 26L;
    
    private static final String OPEN = "<taconite-replace-children contextNodeID=\"$1\" multipleMatch=\"$2\" parseInBrowser=\"true\">";
    private static final String CLOSE = "</taconite-replace-children>";
    
    /** 
     * Construct the action.
     * @param elementId The id of the html element whose content must be removed.
     */
    public RemoveContentAction(String elementId) {
        super(elementId, new ArrayList(0));
    }
    
    protected String getOpeningTag() {
        return OPEN;
    }
    
    protected String getClosingTag() {
        return CLOSE;
    }
}
