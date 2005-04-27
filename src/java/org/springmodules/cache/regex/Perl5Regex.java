/* 
 * Created on Mar 8, 2005
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
package org.springmodules.cache.regex;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * <p>
 * Implementation of <code>{@link Regex}</code> that uses ORO Perl5 regular
 * expression matcher.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/04/27 01:41:25 $
 */
public class Perl5Regex implements Regex {

  /**
   * Compiled representation of a regular expression.
   */
  private final Pattern pattern;

  /**
   * Constructor.
   * 
   * @param regex
   *          the regular expression pattern to compile.
   * @throws PatternInvalidSyntaxException
   *           if the regular expression's syntax is invalid.
   */
  public Perl5Regex(String regex) {
    super();

    Perl5Compiler perl5Compiler = new Perl5Compiler();

    try {
      this.pattern = perl5Compiler.compile(regex);
    } catch (MalformedPatternException malformedPatternException) {
      throw new PatternInvalidSyntaxException(malformedPatternException
          .getMessage());
    }
  }

  /**
   * @see Regex#match(String)
   */
  public Match match(String input) {
    Perl5Matcher matcher = new Perl5Matcher();
    boolean matches = matcher.matches(input, this.pattern);

    String[] groups = null;
    if (matches) {
      MatchResult matchResult = matcher.getMatch();
      int groupCount = matchResult.groups();
      
      groups = new String[groupCount];
      for (int i = 0; i < groupCount; i++) {
        groups[i] = matchResult.group(i);
      }
      
    }

    Match match = new Match(matches, groups);
    return match;
  }
}
