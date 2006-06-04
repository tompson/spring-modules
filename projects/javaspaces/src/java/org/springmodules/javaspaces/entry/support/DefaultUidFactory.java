/*
 * Copyright 2002-2006 the original author or authors.
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
package org.springmodules.javaspaces.entry.support;

import java.io.Serializable;

import net.jini.id.UuidFactory;

import org.springmodules.javaspaces.entry.UidFactory;

/**
 * Default UidFactory that uses the Uid generator part of Jini 2.0.
 * 
 * @author Costin Leau
 * 
 */
public class DefaultUidFactory implements UidFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.javaspaces.entry.UidFactory#generateUid()
	 */
	public Serializable generateUid() {
		return UuidFactory.generate();
	}

}
