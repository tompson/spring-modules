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

package org.springmodules.lucene.index.core;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;

/**
 * <b>This is the central class in the lucene indexing core package.</b>
 * It simplifies the use of lucene to index documents or datas using
 * index reader and writer. It helps to avoid common errors and to
 * manage these resource in a flexible manner.
 * It executes core Lucene workflow, leaving application code to focus on
 * the way to create Lucene documents and make some operations on the
 * index.
 *
 * <p>This class is based on the IndexFactory abstraction which is a
 * factory to create IndexReader and IndexWriter for the configured
 * Directory. So the template doesn't need to always hold resources and
 * this avoids some locking problems on the index. You can too apply
 * different strategies for managing index resources.
 *
 * <p>Can be used within a service implementation via direct instantiation
 * with a IndexFactory reference, or get prepared in an application context
 * and given to services as bean reference. Note: The IndexFactory should
 * always be configured as a bean in the application context, in the first case
 * given to the service directly, in the second case to the prepared template.
 * 
 * <p>You must be aware that the use of some methods (like undeleteDocuments,
 * isDeleted, hasDeletions, flush) have sense only if you share the Lucene
 * underlying resources across several template method calls. As a matter of
 * fact, when IndexReader and IndexWriter are closed every changes deferred
 * until the closing of these resources. Moreover some Lucene operations are
 * incompatible if you share resources across several calls. 
 * 
 * @author Brian McCallister
 * @author Thierry Templier
 * @see DocumentCreator
 * @see DocumentsCreator
 * @see org.springmodules.lucene.index.factory
 */
public class LuceneIndexTemplate {

	private IndexFactory indexFactory;
	private Analyzer analyzer;

	/**
	 * Construct a new LuceneIndexTemplate for bean usage.
	 * Note: The IndexFactory has to be set before using the instance.
	 * This constructor can be used to prepare a LuceneIndexTemplate via a BeanFactory,
	 * typically setting the IndexFactory via setIndexFactory.
	 * @see #setIndexFactory
	 */
	public LuceneIndexTemplate() {
	}

	/**
	 * Construct a new LuceneIndexTemplate, given an IndexFactory to obtain both
	 * IndexReader and IndexWriter, and an Analyzer to be used unless an other
	 * one is specified as method parameter.
	 * @param indexFactory IndexFactory to obtain both IndexReader and IndexWriter
	 * @param analyzer Lucene analyzer to extract tokens out of the text to index
	 */
	public LuceneIndexTemplate(IndexFactory indexFactory,Analyzer analyzer) {
		setIndexFactory(indexFactory);
		setAnalyzer(analyzer);
		afterPropertiesSet();
	}

	/**
	 * Check if the indexFactory is set. The analyzer could be not set.
	 */
	public void afterPropertiesSet() {
		if (getIndexFactory() == null) {
			throw new IllegalArgumentException("indexFactory is required");
		}
	}

	/**
	 * Set the IndexFactory to obtain both IndexReader and IndexWriter.
	 */
	public void setIndexFactory(IndexFactory factory) {
		indexFactory = factory;
	}

	/**
	 * Return the IndexFactory used by this template.
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * Set the default Lucene Analyzer used to extract tokens out of the
	 * text to index.
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Return the Lucene Analyzer used by this template.
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Delete the document corresponding to its internal document
	 * identifier.
	 * Note: Lucene deletes really this document at the IndexReader
	 * close. By default (if you don't share resources across several
	 * calls) the document is really delete from the index before the
	 * method returns. 
	 * @param internalDocumentId the internal document identifier
	 */
	public void deleteDocument(int internalDocumentId) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.delete(internalDocumentId);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during deleting a document.",ex);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Delete one or more documents corresponding to a specified value
	 * for a field.
	 * Note: Lucene deletes really these documents at the IndexReader
	 * close. By default (if you don't share resources across several
	 * calls) the document is really delete from the index before the
	 * method returns. 
	 * @param term the term to specify a value for a field
	 */
	public void deleteDocuments(Term term) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.delete(term);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during deleting a document.",ex);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Undelete every documents that have been marked as deleted. As
	 * Lucene deletes really these documents at the IndexReader
	 * close, this method is useful only if you don't share resources
	 * across several calls. In the contrary, the call of this method
	 * has no effect on the index.
	 */
	public void undeleteDocuments() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.undeleteAll();
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during undeleting all documents.",ex);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Check if a document corresponding to an internal document identifier
	 * has been marked as deleted. As Lucene deletes really these documents
	 * at the IndexReader close, this method is useful only if you don't
	 * share resources across several calls. In the contrary, the call of
	 * this method will always return false.
	 * @param internalDocumentId the internal document identifier
	 */
	public boolean isDeleted(int internalDocumentId) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.isDeleted(internalDocumentId);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Check if an index has documents marked as deleted. As Lucene deletes
	 * really these documents at the IndexReader close, this method is useful
	 * only if you don't share resources across several calls. In the contrary,
	 * the call of this method will always return false.
	 */
	public boolean hasDeletions() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.hasDeletions();
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Flush every updates in the index. For example, documents marked as
	 * deleted will really be removed and documents really added. As Lucene
	 * deletes really these documents at the IndexReader close and adds really,
	 * these at the IndexWriter close, this method is useful only if you
	 * don't share resources across several calls. In the contrary, the call
	 * of this method has no effect on the index.
	 * Note: At this time, the method is not implemented.
	 */
	public void flush() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			//TODO: implement this method.
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Get the next document number which will be used to internally
	 * identify a document. Be aware that, if you share resources
	 * across several calls, this number is modified at every document
	 * add or document marked as deleted. 
	 * @return the next document number in the index
	 */
	public int getMaxDoc() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.maxDoc();
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Get the number of documents in the index. Be aware that, if you
	 * share resources across several calls, this number represents the
	 * number of documents in the index plus the number of documents which
	 * will be added at the IndexWriter close minus the number of documents
	 * which are marked as deleted.
	 * @return the number of documents in the index
	 */
	public int getNumDocs() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.numDocs();
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Invoke the given DocumentCreator, constructing Lucene document.
	 * @param documentCreator the DocumentCreator to invoke
	 * @return the constructed Query
	 * @see DocumentCreator#createDocument()
	 */
	protected Document createDocument(DocumentCreator documentCreator) {
		try {
			return documentCreator.createDocument();
		} catch (IOException ex) {
			throw new LuceneIndexAccessException("Construction of the desired Document failed", ex);
		}
	}

	/**
	 * Invoke the given DocumentsCreator, constructing Lucene documents.
	 * @param documentsCreator the DocumentsCreator to invoke
	 * @return the constructed Query
	 * @see DocumentsCreator#createDocuments()
	 */
	protected List createDocuments(DocumentsCreator documentsCreator) {
		try {
			return documentsCreator.createDocuments();
		} catch (IOException ex) {
			throw new LuceneIndexAccessException("Construction of the desired Document failed", ex);
		}
	}

	/**
	 * Add a document created outside the template to the index. In this case,
	 * the application needs to manage exceptions.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param document the document to add
	 */
	public void addDocument(Document document) {
		addDocument(document,null);
	}

	/**
	 * Add a document created outside the template to the index, basing
	 * the analyzer parameter. In this case, the application needs to
	 * manage exceptions.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param document the document to add
	 * @param analyzer the Lucene analyzer to use to index
	 */
	public void addDocument(Document document,Analyzer analyzer) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			doAddDocument(writer,document,null);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during adding a document.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

	/**
	 * Add a document thanks to a callback method defined in the DocumentCreator
	 * interface. In this case, the exceptions during the document creation are
	 * managed by the template.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 */
	public void addDocument(DocumentCreator creator) {
		addDocument(createDocument(creator),null);
	}

	/**
	 * Add a document thanks to a callback method defined in the DocumentCreator
	 * interface, basing the analyzer parameter. In this case, the exceptions
	 * during the document creation are managed by the template.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 * @param analyzer the Lucene analyzer to use to index
	 */
	public void addDocument(DocumentCreator documentCreator,Analyzer analyzer) {
		addDocument(createDocument(documentCreator),analyzer);
	}

	/**
	 * Add a list of documents created outside the template to the index. In this case,
	 * the application needs to manage exceptions.
	 * Note: Lucene adds really these documents at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the documents are really added to the index before the
	 * method returns. 
	 * @param documents the list of documents to add
	 */
	public void addDocuments(List documents) {
		addDocuments(documents,null);
	}

	/**
	 * Add a list of documents created outside the template to the index, basing
	 * the analyzer parameter. In this case, the application needs to
	 * manage exceptions.
	 * Note: Lucene adds really these documents at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the documents are really added to the index before the
	 * method returns. 
	 * @param documents the list of documents to add
	 * @param analyzer the Lucene analyzer to use to index
	 */
	public void addDocuments(List documents,Analyzer analyzer) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			for(Iterator i=documents.iterator();i.hasNext();) {
				Document document=(Document)i.next();
				doAddDocument(writer,document,analyzer);
			}
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during adding a document.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

	/**
	 * Add a list of documents thanks to a callback method defined in the
	 * DocumentsCreator interface. In this case, the exceptions during the
	 * document creations are managed by the template.
	 * Note: Lucene adds really these documents at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the documents are really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 */
	public void addDocuments(DocumentsCreator creator) {
		addDocuments(creator,null);
	}

	/**
	 * Add a list of documents thanks to a callback method defined in the
	 * DocumentsCreator interface, basing the analyzer parameter. In this
	 * case, the exceptions during the document creation are managed by the template.
	 * Note: Lucene adds really these documents at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the documents are really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 * @param analyzer the Lucene analyzer to use to index
	 */
	public void addDocuments(DocumentsCreator creator,Analyzer analyzer) {
		addDocuments(createDocuments(creator),analyzer);
	}

	/**
	 * Internal method to add document using the Lucene IndexWriter and Analyzer
	 * parameters. Be aware that, if you share resources across several calls, the
	 * document will be really added when the IndexWriter is closed. In the contrary,
	 * the document is add in the index before the method returns. 
	 * @param writer the IndexWriter to use
	 * @param document the document to add
	 * @param analyzer the analyzer to use to extract tokens out of the text to index
	 * @throws IOException if there is any problem during adding the document
	 */
	private void doAddDocument(IndexWriter writer,Document document,
								Analyzer analyzer) throws IOException {
		if( document!=null ) {
			if( analyzer==null ) {
				writer.addDocument(document);
			} else if( getAnalyzer()==null ) {
				writer.addDocument(document);
			} else if( analyzer!=null ) {
				writer.addDocument(document,analyzer);
			} else if( getAnalyzer()!=null ) {
				writer.addDocument(document,getAnalyzer());
			} else {
				writer.addDocument(document);
			}
		} else {
			throw new LuceneIndexAccessException("The document created is null.");
		}
	}

	/**
	 * Add an index created outside the template to the index. In this case,
	 * the application needs to manage exceptions.
	 * Note: Lucene adds really this index at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the index are really added to the index before the
	 * method returns. 
	 * @param documents the list of documents to add
	 */
	public void addIndex(Directory directory) {
		addIndexes(new Directory[] { directory });
	}

	/**
	 * Add a list of indexes created outside the template to the index.
	 * In this case, the application needs to manage exceptions.
	 * Note: Lucene adds really these indexes at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the indexes are really added to the index before the
	 * method returns. 
	 * @param directories the list of indexes to add
	 */
	public void addIndexes(Directory[] directories) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			writer.addIndexes(directories);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during adding indexes.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

	/**
	 * Index optimize method. We recommend to use this method as the end
	 * of an indexing.
	 */
	public void optimize() {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			writer.optimize();
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during optimize the index.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

	/**
	 * Execute the action specified by the given action object within a
	 * Lucene IndexReader.
	 * Note: if you share resources across several calls, the IndexReader
	 * provides to the callback is the shared instance. A new one is not
	 * created.
	 * @param callback the callback object that exposes the IndexReader
	 */
	public Object read(ReaderCallback callback) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return callback.doWithReader(reader);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during using the IndexReader.",ex);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	/**
	 * Execute the action specified by the given action object within a
	 * Lucene IndexWrier.
	 * Note: if you share resources across several calls, the IndexWriter
	 * provides to the callback is the shared instance. A new one is not
	 * created.
	 * @param callback the callback object that exposes the IndexWriter
	 */
	public Object write(WriterCallback callback) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			return callback.doWithWriter(writer);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during using the IndexWriter.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

}
