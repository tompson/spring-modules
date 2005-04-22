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

package org.springmodules.resource.support;

import org.springmodules.resource.ResourceException;
import org.springmodules.resource.ResourceManager;
import org.springmodules.resource.ResourceStatus;

/**
 * @author Thierry Templier
 */
public class ResourceTemplate {

	private ResourceManager resourceManager = null;

	public Object execute(ResourceCallback callback) throws ResourceException {
		ResourceStatus status=this.resourceManager.open();
		Object result = null;
		try {
			result = callback.doWithResource();
		} finally {
			this.resourceManager.close(status);
		}
		return result;
	}

	/**
	 * @return
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * @param manager
	 */
	public void setResourceManager(ResourceManager manager) {
		resourceManager = manager;
	}

}
