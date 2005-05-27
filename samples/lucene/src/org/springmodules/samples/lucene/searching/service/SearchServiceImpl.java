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

package org.springmodules.samples.lucene.searching.service;

import java.util.List;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.search.core.HitExtractor;
import org.springmodules.lucene.search.core.ParsedQueryCreator;
import org.springmodules.lucene.search.core.ParsedQueryCreator.QueryParams;
import org.springmodules.lucene.search.support.LuceneSearchSupport;
import org.springmodules.samples.lucene.searching.domain.SearchResult;

/**
 * @author Thierry Templier
 */
public class SearchServiceImpl extends LuceneSearchSupport implements SearchService {

	public List search(final String textToSearch) {
		List results=getTemplate().search(new ParsedQueryCreator() {
			public QueryParams configureQuery() {
				return new QueryParams("contents",textToSearch);
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				if( document.get("request")!=null ) {
					return new SearchResult(document.get("request"),score);
				} else {
					return new SearchResult(document.get("filename"),score);
				}
			}
		});
		return results;
	}
}
