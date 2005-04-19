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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

/**
 * Abstract index factory which gives access to the tuning
 * parameters of the IndexWriter.
 * 
 * @author Thierry Templier
 */
public abstract class AbstractIndexFactory {
	public final static int DEFAULT_MERGE_FACTOR=10;
	public final static int DEFAULT_MAX_MERGE_DOCS=Integer.MAX_VALUE;
	public final static int DEFAULT_MIN_MERGE_DOCS=10;
	public final static int DEFAULT_MAX_FIELD_LENGTH=10000;

	private Directory directory;
	private Analyzer analyzer;

	private boolean useCompoundFile=false;
	private int mergeFactor=DEFAULT_MERGE_FACTOR;
	private int maxMergeDocs=DEFAULT_MAX_MERGE_DOCS;
	private int minMergeDocs=DEFAULT_MIN_MERGE_DOCS;
	private int maxFieldLength=DEFAULT_MAX_FIELD_LENGTH;

	/**
	 * @return
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 */
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	/**
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	protected void setIndexWriterParameters(IndexWriter writer) {
		writer.setUseCompoundFile(useCompoundFile);
		writer.mergeFactor=mergeFactor;
		writer.maxMergeDocs=maxMergeDocs;
		writer.minMergeDocs=minMergeDocs;
		writer.maxFieldLength=maxFieldLength;
	}

	/**
	 * @return
	 */
	public int getMaxFieldLength() {
		return maxFieldLength;
	}

	/**
	 * @return
	 */
	public int getMaxMergeDocs() {
		return maxMergeDocs;
	}

	/**
	 * @return
	 */
	public int getMergeFactor() {
		return mergeFactor;
	}

	/**
	 * @return
	 */
	public int getMinMergeDocs() {
		return minMergeDocs;
	}

	/**
	 * @return
	 */
	public boolean isUseCompoundFile() {
		return useCompoundFile;
	}

	/**
	 * @param i
	 */
	public void setMaxFieldLength(int i) {
		maxFieldLength = i;
	}

	/**
	 * @param i
	 */
	public void setMaxMergeDocs(int i) {
		maxMergeDocs = i;
	}

	/**
	 * @param i
	 */
	public void setMergeFactor(int i) {
		mergeFactor = i;
	}

	/**
	 * @param i
	 */
	public void setMinMergeDocs(int i) {
		minMergeDocs = i;
	}

	/**
	 * @param b
	 */
	public void setUseCompoundFile(boolean b) {
		useCompoundFile = b;
	}

}
