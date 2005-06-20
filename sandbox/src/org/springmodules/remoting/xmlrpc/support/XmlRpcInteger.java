/* 
 * Created on Jun 14, 2005
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
package org.springmodules.remoting.xmlrpc.support;

import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Represents a four-byte signed integer.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/20 04:42:04 $
 */
public class XmlRpcInteger implements XmlRpcScalar {

  /**
   * The value of this scalar.
   */
  private Integer value;

  /**
   * Constructor.
   */
  public XmlRpcInteger() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcInteger(Integer value) {
    this();
    this.value = value;
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcInteger(String value) {
    super();
    try {
      this.value = new Integer(value);

    } catch (NumberFormatException exception) {
      throw new XmlRpcParsingException("'" + value
          + "' is not an integer value", exception);
    }
  }

  /**
   * @see XmlRpcScalar#getValue()
   */
  public Object getValue() {
    return this.value;
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class type) {
    Object matchingValue = NOT_MATCHING;

    if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
      matchingValue = this.value;
    } else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
      matchingValue = new Long(this.value.longValue());
    }
    return matchingValue;
  }
}
