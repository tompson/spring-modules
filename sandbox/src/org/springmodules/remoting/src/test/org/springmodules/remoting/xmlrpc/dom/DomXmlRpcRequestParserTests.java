/* 
 * Created on Jun 21, 2005
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
package org.springmodules.remoting.xmlrpc.dom;

import org.springmodules.remoting.xmlrpc.AbstractXmlRpcRequestParserTests;
import org.springmodules.remoting.xmlrpc.XmlRpcRequestParser;

/**
 * <p>
 * Unit Tests for <code>{@link DomXmlRpcRequestParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class DomXmlRpcRequestParserTests extends
    AbstractXmlRpcRequestParserTests {

  /**
   * Primary object that is under test.
   */
  private DomXmlRpcRequestParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public DomXmlRpcRequestParserTests(String name) {
    super(name);
  }

  /**
   * @see AbstractXmlRpcRequestParserTests#getXmlRpcRequestParserImplementation()
   */
  protected XmlRpcRequestParser getXmlRpcRequestParserImplementation() {
    return this.parser;
  }

  /**
   * @see AbstractXmlRpcRequestParserTests#onSetUp()
   */
  protected void onSetUp() throws Exception {
    super.onSetUp();
    this.parser = new DomXmlRpcRequestParser();
  }

}
