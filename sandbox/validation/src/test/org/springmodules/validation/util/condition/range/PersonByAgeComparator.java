/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.util.condition.range;

import java.util.Comparator;

import org.springframework.util.Assert;

/**
 * @author Uri Boness
 */
public class PersonByAgeComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Assert.isInstanceOf(Person.class, o1, getClass().getName() + " can only compare Person objects");
        Assert.isInstanceOf(Person.class, o2, getClass().getName() + " can only compare Person objects");
        return compare((Person)o1, (Person)o2);
    }

    public int compare(Person p1, Person p2) {
        return p1.getAge() - p2.getAge();
    }

}
