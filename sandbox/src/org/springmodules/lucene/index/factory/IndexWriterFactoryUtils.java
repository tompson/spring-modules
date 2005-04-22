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

package org.springmodules.lucene.index.factory;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexWriter;
import org.springmodules.lucene.index.LuceneCloseIndexException;
import org.springmodules.lucene.search.LuceneCannotGetSearcherException;
import org.springmodules.resource.support.ResourceSynchronizationManager;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public abstract class IndexWriterFactoryUtils {

	private static final Log logger = LogFactory.getLog(IndexWriterFactoryUtils.class);

    /**
	 * @param indexFactory
	 * @return
	 */
	public static IndexWriter getIndexWriter(IndexFactory indexFactory) {
    	return getIndexWriter(indexFactory,true);
    }

	/**
	 * @param indexFactory
	 * @param allowSynchronization
	 * @return
	 */
	public static IndexWriter getIndexWriter(IndexFactory indexFactory,boolean allowSynchronization) {
		try {
			return doGetIndexWriter(indexFactory, allowSynchronization);
		} catch (IOException ex) {
			throw new LuceneCannotGetSearcherException("Could not get Lucene reader", ex);
		}
	}

	/**
	 * @param indexFactory
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter doGetIndexWriter(IndexFactory indexFactory) throws IOException {
		return doGetIndexWriter(indexFactory,true);
	}

	/**
	 * @param indexFactory
	 * @param allowSynchronization
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter doGetIndexWriter(IndexFactory indexFactory,boolean allowSynchronization) throws IOException {
		IndexHolder indexHolder = (IndexHolder) ResourceSynchronizationManager.getResource(indexFactory);
		if (indexHolder != null && indexHolder.getIndexWriter()!=null ) {
			System.err.println("!! writer = "+indexHolder.getIndexWriter()+" !!");
			return indexHolder.getIndexWriter();
		}

		boolean bindHolder=true;
		if( indexHolder!=null ) {
			bindHolder=false;
		}

		IndexWriter writer = indexFactory.getIndexWriter();
		if (allowSynchronization && ResourceSynchronizationManager.isSynchronizationActive()) {
			logger.debug("Registering reader synchronization for Lucene index write");
			if( indexHolder==null ) {
				indexHolder = new IndexHolder(null,writer);
			} else {
				indexHolder.setIndexWriter(writer);
			}

			if( bindHolder ) {
				ResourceSynchronizationManager.bindResource(indexFactory, indexHolder);
			}

			ResourceSynchronizationManager.registerSynchronization(new IndexSynchronization(indexHolder, indexFactory));
		}
		System.err.println("!! writer = "+writer+" !!");
		return writer;
    }

	/**
	 * @param indexFactory
	 * @param indexReader
	 */
	public static void closeIndexWriterIfNecessary(IndexFactory indexFactory,IndexWriter indexReader) {
		try {
			doCloseIndexWriterIfNecessary(indexFactory,indexReader);
		} catch(IOException ex) {
			throw new LuceneCloseIndexException("Unable to close index writer",ex);
		}
	}

	/**
	 * @param indexFactory
	 * @param indexReader
	 * @throws IOException
	 */
	public static void doCloseIndexWriterIfNecessary(IndexFactory indexFactory,IndexWriter indexWriter) throws IOException {
		if (indexWriter == null || ResourceSynchronizationManager.hasResource(indexFactory)) {
			return;
		}

		indexWriter.close();
	}

}