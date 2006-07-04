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

package org.springmodules.xt.utils.mvc.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.mvc.AbstractUrlViewController;

/**
 * Modified {@link org.springframework.web.servlet.mvc.UrlFilenameViewController} for preserving full view path.<br>
 * For example, given the path "/foo/index.html", this controller will point to the view "foo/index".
 *
 * @author The Spring Team
 * @author Sergio Bossa (sergio.bossa(at)gmail.com)
 */
public class FullPathUrlFilenameViewController extends AbstractUrlViewController {
    
    private String prefix = "";
    private String suffix = "";

    /** Request URL path String --> view name String */
    private final Map viewNameCache = Collections.synchronizedMap(new HashMap());

    /**
     * Set the prefix to prepend to the request URL filename
     * to build a view name.
     */
    public void setPrefix(String prefix) {
        this.prefix = (prefix != null ? prefix : "");
    }

    /**
     * Return the prefix to prepend to the request URL filename.
     */
    protected String getPrefix() {
        return prefix;
    }

    /**
     * Set the suffix to append to the request URL filename
     * to build a view name.
     */
    public void setSuffix(String suffix) {
        this.suffix = (suffix != null ? suffix : "");
    }

    /**
     * Return the suffix to append to the request URL filename.
     */
    protected String getSuffix() {
        return suffix;
    }

    /**
     * Returns view name based on the URL filename,
     * with prefix/suffix applied when appropriate.
     * @see #extractViewNameFromUrlPath
     * @see #setPrefix
     * @see #setSuffix
     */
    protected String getViewNameForUrlPath(String urlPath) {
        String viewName = (String) this.viewNameCache.get(urlPath);
        if (viewName == null) {
            viewName = extractViewNameFromUrlPath(urlPath);
            viewName = postProcessViewName(viewName);
            this.viewNameCache.put(urlPath, viewName);
        }
        return viewName;
    }

    /**
     * @param uri the request URI (e.g. "/foo/index.html")
     * @return the extracted URI filename (e.g. "foo/index")
     */
    protected String extractViewNameFromUrlPath(String targetUrl) {
        if (targetUrl.charAt(0) == '/') targetUrl = targetUrl.substring(1);
        if (targetUrl.lastIndexOf('.') != -1) targetUrl = targetUrl.substring(0, targetUrl.lastIndexOf('.'));
        return targetUrl;
    }

    /**
     * Build the full view name based on the given view name
     * as indicated by the URL path.
     * <p>The default implementation simply applies prefix and suffix.
     * This can be overridden, for example, to manipulate upper case
     * / lower case, etc.
     * @param viewName the original view name, as indicated by the URL path
     * @return the full view name to use
     * @see #getPrefix()
     * @see #getSuffix()
     */
    protected String postProcessViewName(String viewName) {
        return getPrefix() + viewName + getSuffix();
    }
}
