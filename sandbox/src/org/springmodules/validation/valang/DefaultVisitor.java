/*
 * Copyright 2004-2005 the original author or authors.
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
package org.springmodules.validation.valang;

import org.apache.commons.collections.Predicate;
import org.springmodules.util.dateparser.DefaultDateParser;
import org.springmodules.validation.functions.Function;
import org.springmodules.validation.functions.LengthOfFunction;
import org.springmodules.validation.functions.LowerCaseFunction;
import org.springmodules.validation.functions.NotFunction;
import org.springmodules.validation.functions.UpperCaseFunction;
import org.springmodules.validation.predicates.GenericTestPredicate;
import org.springmodules.validation.predicates.Operator;

/**
 * <p>Allows registration of custom functions. Custom functions can overwrite default functions.
 * 
 * <p>Default functions are:
 * 
 * <ul>
 * 	<li>len, length, size
 * 	<li>upper
 * 	<li>lower
 * 	<li>!
 * </ul> 
 * 
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public class DefaultVisitor implements ValangVisitor {

	private ValangVisitor visitor = null;
	private DefaultDateParser dateParser = new DefaultDateParser();
	
	public DefaultVisitor() {
		super();
	}

	public Function getFunction(String name, Function function, int line, int column) {
		if (getVisitor() != null) {
			Function tmpFunction = getVisitor().getFunction(name, function, line, column);
			if (tmpFunction != null) {
				return tmpFunction;
			}
		}
		
		if ("len".equals(name)) {
			return new LengthOfFunction(function, line, column);
		} else if ("length".equals(name)) {
			return new LengthOfFunction(function, line, column);
		} else if ("size".equals(name)) {
			return new LengthOfFunction(function, line, column);
		} else if ("upper".equals(name)) {
			return new UpperCaseFunction(function, line, column);
		} else if ("lower".equals(name)) {
			return new LowerCaseFunction(function, line, column);
		} else if ("!".equals(name)) {
			return new NotFunction(function, line, column);
		}

		throw new IllegalArgumentException("Could not find function [" + name + "]!");
	}

	/**
	 * <p>Register a custom visitor to look up custom functions. Lookup of functions
	 * will first be delegated to this visitor. If no function has been returned (null)
	 * lookup will be handled by DefaultVisitor.
	 * 
	 * @param visitor the custom visitor
	 */
	public void setVisitor(ValangVisitor visitor) {
		this.visitor = visitor;
	}
	
	public ValangVisitor getVisitor() {
		return this.visitor;
	}
	
	public Predicate getPredicate(Function leftFunction, Operator operator, Function rightFunction) {
		return new GenericTestPredicate(leftFunction, operator, rightFunction);
	}
	
	public DefaultDateParser getDateParser() {
		return this.dateParser;
	}
}
