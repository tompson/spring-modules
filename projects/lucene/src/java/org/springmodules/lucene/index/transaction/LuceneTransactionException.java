/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.lucene.index.transaction;

import org.springmodules.lucene.index.LuceneIndexingException;

/**
 * This exception encapsulates errors during transactionnal operation on an
 * index.
 * 
 * @author Thierry Templier
 */
public class LuceneTransactionException extends LuceneIndexingException {

	/**
	 * Constructor for LuceneIndexingException.
	 * @param msg message
	 */
	public LuceneTransactionException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for LuceneIndexingException.
	 * @param msg message
	 * @param ex root cause
	 */
	public LuceneTransactionException(String msg, Exception ex) {
		super(msg, ex);
	}

}
