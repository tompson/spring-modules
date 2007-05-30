/*
 * Created on Sep 22, 2004
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2007 the original author or authors.
 */
package org.springmodules.cache.integration.tangosol;

import com.tangosol.net.NamedCache;

import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.provider.tangosol.CoherenceCachingModel;
import org.springmodules.cache.util.TangosolUtils;

/**
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using Tangosol Coherence as the cache
 * provider.
 * <p/>
 * <Strong>Note:</Strong> This class requires
 * <a href="http://www.tangosol.com">Tangosol Coherence</a> jars in the
 * classpath to work.
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public class CoherenceIntegrationTests extends
		AbstractIntegrationTests {

	/**
	 * @see AbstractIntegrationTests#assertCacheWasFlushed()
	 */
	protected final void assertCacheWasFlushed() throws Exception {
		int index = 0;
		Object entry = getCacheElement(index);
		assertCacheEntryFromCacheIsNull(entry, getKeyAndModel(index).key);
	}

	/**
	 * @see AbstractIntegrationTests#assertObjectWasCached(Object,int)
	 */
	protected final void assertObjectWasCached(Object expectedCachedObject,
											   int keyAndModelIndex) throws Exception {
		Object entry = getCacheElement(keyAndModelIndex);
		assertEquals(expectedCachedObject, entry);
	}

	private Object getCacheElement(int keyAndModelIndex) throws Exception {
		KeyAndModel keyAndModel = getKeyAndModel(keyAndModelIndex);
		CoherenceCachingModel model = (CoherenceCachingModel) keyAndModel.model;
		NamedCache cache = TangosolUtils.getNamedCache(model.getCacheName());
		return cache.get(keyAndModel.key);
	}

}