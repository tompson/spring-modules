/* 
 * Created on Jun 15, 2005
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
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.remoting.xmlrpc;

import java.io.InputStream;

import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;

/**
 * <p>
 * Parses a given XML-RPC request.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/07/04 18:42:09 $
 */
public interface XmlRpcRequestParser {

  /**
   * Creates a new XML-RPC request by parsing the given InputStream.
   * 
   * @param inputStream
   *          the InputStream containing the XML-RPC request.
   * @return the created XML-RPC request.
   * @throws XmlRpcException
   *           if there are errors during the parsing.
   */
  XmlRpcRequest parseRequest(InputStream inputStream) throws XmlRpcException;
}
