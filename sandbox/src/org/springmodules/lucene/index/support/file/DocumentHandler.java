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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.lucene.document.Document;

/**
 * This is the base interface to define different implementations
 * in order to index different types of files.
 * 
 * @author Thierry Templier
 */
public interface DocumentHandler {
	public final static String FILENAME="filename";

	/**
	 * This method indexes an InputStream and specifies some additional
	 * properties on the Lucene document basing the description parameter.
	 * 
	 * @param description the description of the resource to index
	 * @param inputStream the input stream which will be used to index
	 */
	public Document getDocument(Map description,InputStream inputStream) throws IOException;
}
