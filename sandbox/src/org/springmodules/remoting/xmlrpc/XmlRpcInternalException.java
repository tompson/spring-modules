/* 
 * Created on Jul 4, 2005
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

/**
 * <p>
 * Internal error thrown by the XML-RPC server.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcInternalException extends XmlRpcServerException {

  /**
   * Fault code of this exception.
   */
  public static final int FAULT_CODE = -32603;

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 1204941186970267542L;

  /**
   * Constructor.
   * 
   * @param msg
   *          the detail message.
   */
  public XmlRpcInternalException(String msg) {
    super(msg);
  }

  /**
   * Constructor.
   * 
   * @param msg
   *          the detail message.
   * @param nestedException
   *          the nested exception.
   */
  public XmlRpcInternalException(String msg, Throwable nestedException) {
    super(msg, nestedException);
  }

  /**
   * @see XmlRpcException#getCode()
   */
  public int getCode() {
    return FAULT_CODE;
  }

}
