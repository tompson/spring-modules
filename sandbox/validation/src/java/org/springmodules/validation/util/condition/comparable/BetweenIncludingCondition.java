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

package org.springmodules.validation.util.condition.comparable;

/**
 * An {@link AbstractComparableCondition} implementation that checks whether the checked comparable is between two
 * specific values (including).
 *
 * @author Uri Boness
 */
public class BetweenIncludingCondition extends AbstractBetweenCondition {

    /**
     * Constructs a new BetweenCondition with the given bounds (upper and lower) the checked comparable will be
     * compared with.
     *
     * @param lowerBound The lower bound the checked object will be compared with.
     * @param upperBound The upper bound the checked object will be compared with.
     */
    public BetweenIncludingCondition(Comparable lowerBound, Comparable upperBound) {
        super(lowerBound, upperBound);
    }

    /**
     * Checks whether the given comparable is between the bounds associated with this instantCondition.
     *
     * @param comparable The comparable to be checked.
     * @return <code>true</code> if the given comparable is greater than or equals the lower bound and smaller than
     *         or equals the upper bound, <code>false</code> otherwise.
     */
    protected boolean check(Comparable comparable) {
        return comparable.compareTo(getLowerBound()) >= 0 && comparable.compareTo(getUpperBound()) <= 0;
    }

}
