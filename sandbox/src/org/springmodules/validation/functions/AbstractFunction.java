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
package org.springmodules.validation.functions;

/**
 * <p>Base class for functions. Function classes should extend this class. 
 * 
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public abstract class AbstractFunction implements Function {

	private Function[] arguments = null;
	private FunctionTemplate template = null;
	
	/**
	 * Sub classes must implement this constructor.
	 */
	public AbstractFunction(Function[] arguments, int line, int column) {
		super();
		setArguments(arguments);
		setTemplate(new FunctionTemplate(line, column));
	}

	protected Function[] getArguments() {
		return arguments;
	}
	
	private void setArguments(Function[] arguments) {
		if (arguments == null) {
			throw new IllegalArgumentException("Function parameters should not be null!");
		}
		this.arguments = arguments;
	}

	private void setTemplate(FunctionTemplate template) {
		this.template = template;
	}
	
	protected FunctionTemplate getTemplate() {
		return this.template;
	}
	
	public final Object getResult(Object target) {
		return getTemplate().execute(target, new FunctionCallback() {
			public Object execute(Object target) throws Exception {
				return doGetResult(target);
			}
		});
	}

	protected abstract Object doGetResult(Object target) throws Exception;
}
