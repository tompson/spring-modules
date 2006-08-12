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

import java.util.Arrays;
import junit.framework.*;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class AppendAsFirstContentActionTest extends XMLEnhancedTestCase {
    
    public AppendAsFirstContentActionTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AppendAsFirstContentActionTest.class);
        
        return suite;
    }
    
    public void testExecute() throws Exception {
        AjaxAction action = new AppendAsFirstContentAction("testId", Arrays.asList(new Component[]{new TaggedText("Test Component 1", TaggedText.Tag.DIV), new TaggedText("Test Component 2", TaggedText.Tag.DIV)}));
        
        String result = action.execute();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component 1", "/taconite-append-as-first-child/div[position()=1]", result);
        assertXpathEvaluatesTo("Test Component 2", "/taconite-append-as-first-child/div[position()=2]", result);
        assertXpathEvaluatesTo("testId", "/taconite-append-as-first-child/@contextNodeID", result);
    }
}
