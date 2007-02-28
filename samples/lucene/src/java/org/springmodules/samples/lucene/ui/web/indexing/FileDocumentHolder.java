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

package org.springmodules.samples.lucene.ui.web.indexing;

/**
 * @author Thierry Templier
 */
public class FileDocumentHolder {
	private String id;
	private String filename;
	private byte[] file;
	private String category;

	public FileDocumentHolder() {
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] bs) {
		file = bs;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String string) {
		filename = string;
	}

	public String getCategory() {
		return category;
	}

	public String getId() {
		return id;
	}

	public void setCategory(String string) {
		category = string;
	}

	public void setId(String string) {
		id = string;
	}

}
