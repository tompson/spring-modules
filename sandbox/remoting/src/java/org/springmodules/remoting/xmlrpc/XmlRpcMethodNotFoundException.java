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

/**
 * <p>
 * Exception thrown when a XML-RPC request specifies a method that does not
 * exist.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcMethodNotFoundException extends XmlRpcServerException {

  public static final int FAULT_CODE = -32601;

  private static final long serialVersionUID = 3257005449604510518L;

  public XmlRpcMethodNotFoundException(String msg) {
    super(msg);
  }

  public XmlRpcMethodNotFoundException(String msg, Throwable nestedException) {
    super(msg, nestedException);
  }

  public int getCode() {
    return FAULT_CODE;
  }

}
