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

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 * Represents a base64-encoded binary.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.9 $ $Date: 2005/07/15 18:55:58 $
 */
public final class XmlRpcBase64 implements XmlRpcScalar {

  /**
   * The value of this scalar.
   */
  private byte[] value;

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcBase64(byte[] value) {
    super();
    this.value = value;
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcBase64(String value) {
    this(Base64.decodeBase64(value.getBytes()));
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcBase64)) {
      return false;
    }

    final XmlRpcBase64 xmlRpcBase64 = (XmlRpcBase64) obj;

    if (!Arrays.equals(this.value, xmlRpcBase64.value)) {
      return false;
    }

    return true;
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (targetType.isArray() && targetType.getComponentType().equals(Byte.TYPE)) {
      matchingValue = this.value;
    }
    return matchingValue;
  }

  /**
   * @see XmlRpcScalar#getValue()
   */
  public Object getValue() {
    return this.value;
  }

  /**
   * @see XmlRpcScalar#getValueAsString()
   */
  public String getValueAsString() {
    byte[] encodedValue = Base64.encodeBase64(this.value);
    return new String(encodedValue, 0, encodedValue.length);
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (this.value != null ? this.value.hashCode() : 0);
    return hash;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   * 
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("value=");

    if (this.value == null) {
      buffer.append("null]");
    } else {
      int elementCount = this.value.length;
      for (int i = 0; i < elementCount; i++) {
        if (i == 0) {
          buffer.append("[");
        } else {
          buffer.append(", ");
        }

        buffer.append(this.value[i]);
      }
      buffer.append("]");
    }

    return buffer.toString();
  }
}
