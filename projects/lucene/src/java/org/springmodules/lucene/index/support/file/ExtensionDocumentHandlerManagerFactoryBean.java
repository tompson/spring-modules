/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.lucene.index.support.file;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * This factory bean class is used to declaratively configured
 * a DocumentHandlerManager instance which can be used by template
 * or indexers.
 * 
 * <p>The configured DocumentHandlerManager will be based on file
 * extensions because the factory bean uses the DocumentExtensionMatching
 * class to register the specified DocumentHandlers with the documentHandlers
 * property.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.support.file.DocumentHandlerManager
 * @see 
 */
public class ExtensionDocumentHandlerManagerFactoryBean implements FactoryBean,InitializingBean {

	private DocumentHandlerManager documentHandlerManager;
	private Map documentHandlers;

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return documentHandlerManager;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return DocumentHandlerManager.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		documentHandlerManager=new ExtensionDocumentHandlerManager();
		documentHandlerManager.registerDefautHandlers();
		if( documentHandlers!=null ) {
			Set documentHandlersKeys=documentHandlers.keySet();
			for(Iterator i=documentHandlersKeys.iterator();i.hasNext();) {
				String extension=(String)i.next();
				documentHandlerManager.registerDocumentHandler(
								new DocumentExtensionMatching(extension),
								(DocumentHandler)documentHandlers.get(extension));
			}
		}
	}

	public Map getDocumentHandlers() {
		return documentHandlers;
	}

	public void setDocumentHandlers(Map map) {
		documentHandlers = map;
	}

}
