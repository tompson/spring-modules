/* 
 * Created on Jun 4, 2005
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
 * Exception thrown when the writer of a XML-RPC request/response encounters an
 * internal error.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcWriterException extends XmlRpcServerException {

  /**
   * Fault code of this exception.
   */
  public static final int FAULT_CODE = -32603;

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3761406417336546864L;

  /**
   * Constructor.
   * 
   * @param msg
   *          the detail message.
   */
  public XmlRpcWriterException(String msg) {
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
  public XmlRpcWriterException(String msg, Throwable nestedException) {
    super(msg, nestedException);
  }

  /**
   * @see XmlRpcException#getCode()
   */
  public int getCode() {
    return FAULT_CODE;
  }
}
