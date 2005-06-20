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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Represents a XML-RPC date/time value in IS0 8601 format.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/20 10:30:31 $
 */
public class XmlRpcDateTime implements XmlRpcScalar {

  /**
   * Pattern for ISO 8601, the date and time representation standard.
   */
  public static final String PATTERN = "yyyyMMdd'T'HH:mm:ss";

  /**
   * Internal <code>java.text.DateFormat</code> wrapped by this class.
   */
  private DateFormat dateFormat = new SimpleDateFormat(PATTERN);

  /**
   * The value of this scalar.
   */
  private Date value;

  /**
   * Constructor.
   */
  public XmlRpcDateTime() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcDateTime(Date value) {
    this();
    this.value = value;
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   * @throws XmlRpcParsingException
   *           if the given value is not a parsable date.
   */
  public XmlRpcDateTime(String value) {
    this();

    try {
      this.value = this.dateFormat.parse(value);

    } catch (ParseException exception) {
      throw new XmlRpcParsingException("'" + value
          + "' is not a date in ISO 8601 format", exception);
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
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (Date.class.equals(targetType)) {
      matchingValue = this.value;
    }
    return matchingValue;
  }
}
