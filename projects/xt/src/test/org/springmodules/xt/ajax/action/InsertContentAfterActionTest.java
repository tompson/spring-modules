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
import org.springmodules.xt.ajax.action.matcher.ElementMatcher;
import org.springmodules.xt.ajax.action.matcher.ListMatcher;
import org.springmodules.xt.ajax.action.matcher.SelectorMatcher;
import org.springmodules.xt.ajax.action.matcher.WildcardMatcher;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class InsertContentAfterActionTest extends XMLEnhancedTestCase {
    
    public InsertContentAfterActionTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void testExecute() throws Exception {
        AjaxAction action = new InsertContentAfterAction("testId", Arrays.asList(new Component[]{new TaggedText("Test Component 1", TaggedText.Tag.DIV), new TaggedText("Test Component 2", TaggedText.Tag.DIV)}));
        
        String result = action.execute();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component 1", "/taconite-insert-after/div[position()=1]", result);
        assertXpathEvaluatesTo("Test Component 2", "/taconite-insert-after/div[position()=2]", result);
        assertXpathEvaluatesTo("testId", "/taconite-insert-after/@contextNodeID", result);
    }
    
    public void testExecuteWithWildcardMatcher() throws Exception {
        ElementMatcher matcher = new WildcardMatcher("testId");
        AjaxAction action = new InsertContentAfterAction(matcher, Arrays.asList(new Component[]{new TaggedText("Test Component 1", TaggedText.Tag.DIV), new TaggedText("Test Component 2", TaggedText.Tag.DIV)}));
        
        String result = action.execute();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component 1", "/taconite-insert-after/div[position()=1]", result);
        assertXpathEvaluatesTo("Test Component 2", "/taconite-insert-after/div[position()=2]", result);
        assertXpathEvaluatesTo("wildcard", "/taconite-insert-after/@matchMode", result);
        assertXpathEvaluatesTo("testId", "/taconite-insert-after/@contextNodeID", result);
    }
    
    public void testExecuteWithListMatcher() throws Exception {
        ElementMatcher matcher = new ListMatcher(Arrays.asList("testId1", "testId2"));
        AjaxAction action = new InsertContentAfterAction(matcher, Arrays.asList(new Component[]{new TaggedText("Test Component 1", TaggedText.Tag.DIV), new TaggedText("Test Component 2", TaggedText.Tag.DIV)}));
        
        String result = action.execute();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component 1", "/taconite-insert-after/div[position()=1]", result);
        assertXpathEvaluatesTo("Test Component 2", "/taconite-insert-after/div[position()=2]", result);
        assertXpathEvaluatesTo("plain", "/taconite-insert-after/@matchMode", result);
        assertXpathEvaluatesTo("testId1,testId2", "/taconite-insert-after/@contextNodeID", result);
    }
    
    public void testExecuteWithSelectorMatcher() throws Exception {
        ElementMatcher matcher = new SelectorMatcher(Arrays.asList("#testId1", "#testId2"));
        AjaxAction action = new InsertContentAfterAction(matcher, Arrays.asList(new Component[]{new TaggedText("Test Component 1", TaggedText.Tag.DIV), new TaggedText("Test Component 2", TaggedText.Tag.DIV)}));
        
        String result = action.execute();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component 1", "/taconite-insert-after/div[position()=1]", result);
        assertXpathEvaluatesTo("Test Component 2", "/taconite-insert-after/div[position()=2]", result);
        assertXpathEvaluatesTo("selector", "/taconite-insert-after/@matchMode", result);
        assertXpathEvaluatesTo("#testId1,#testId2", "/taconite-insert-after/@contextNodeSelector", result);
    }
}
