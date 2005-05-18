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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.LuceneIndexAccessException;

/**
 * <p>This is the simplier factory to get reader and writer instances
 * to work on a Lucene index. 
 * 
 * <p>This factory only constructs IndexReader and IndexWriter instances.
 * There is no control on currency use of the different methods of the
 * reader and the writer.
 * 
 * <p>Before creating an IndexWriter, this implementation checks if the
 * index already exists. If not, it sets a flag to notify the IndexWriter
 * to create it.
 * 
 * @author Brian McCallister
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.factory.IndexFactory
 * @see org.springmodules.lucene.index.factory.AbstractIndexFactory
 * @see org.springmodules.lucene.index.factory.AbstractIndexFactory#setIndexWriterParameters(IndexWriter)
 * @see org.apache.lucene.index.IndexReader
 * @see org.apache.lucene.index.IndexWriter
 */
public class SimpleIndexFactory extends AbstractIndexFactory implements IndexFactory {

	/**
	 * Construct a new SimpleIndexFactory for bean usage.
	 * Note: The Directory and the Analyzer have to be set before using the instance.
	 * @see #setDirectory
	 * @see #setAnalyzer
	 */
	public SimpleIndexFactory() {
	}

	/**
	 * Construct a new SimpleIndexFactory, given a Directory and an Analyzer to
	 * obtain both IndexReader and IndexWriter.
	 * @param directory Lucene directoy which represents an index
	 * @param analyzer Lucene analyzer to construct an IndexWriter
	 */
	public SimpleIndexFactory(Directory directory,Analyzer analyzer) {
		setDirectory(directory);
		setAnalyzer(analyzer);
	}

	/**
	 * Contruct a new IndexReader instance based on the directory property. This
	 * instance will be used by the IndexTemplate to get informations about the
	 * index and make delete operations on the index. 
	 * @return a new reader instance on the index
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexReader()
	 */
	public IndexReader getIndexReader() {
		try {
			return IndexReader.open(getDirectory());
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during opening the reader",ex);
		}
	}

	/**
	 * Contruct a new IndexWriter instance based on the directory and analyzer
	 * properties. This instance will be used by both the IndexTemplate and
	 * every indexers to add documents and optimize it.
	 * <p>Before creating an IndexWriter, this implementation checks if the
 	 * index already exists. If not, it sets a flag to notify the IndexWriter
	 * to create it.
	 * @return a new writer instance on the index
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexWriter()
	 * @see org.springmodules.lucene.index.factory.AbstractIndexFactory#setIndexWriterParameters(IndexWriter)
	 * @see IndexReader#indexExists(org.apache.lucene.store.Directory)
	 */
	public IndexWriter getIndexWriter() {
		try {
			boolean create = !IndexReader.indexExists(getDirectory());
			IndexWriter writer = new IndexWriter(getDirectory(),getAnalyzer(),create);
			setIndexWriterParameters(writer);
			return writer;
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during creating the writer",ex);
		}
	}

}
