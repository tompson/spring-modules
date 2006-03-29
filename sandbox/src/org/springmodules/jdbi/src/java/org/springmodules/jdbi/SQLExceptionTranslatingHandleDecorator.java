/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.jdbi;

import org.skife.jdbi.Handle;
import org.skife.jdbi.IDBI;
import org.skife.jdbi.unstable.decorator.HandleDecorator;
import org.springframework.aop.framework.ProxyFactory;

import javax.sql.DataSource;

/**
 * Implementation of the HandleDecorator interface.  It provides a decorator implemented
 * as a Spring AOP proxy.  This decorator will, via an AOP advice, provide translation
 * of SQLExceptions reported by DBI into Spring's DataAccessException hierarchy.
 *
 * @author Thomas Risberg
 */
public class SQLExceptionTranslatingHandleDecorator implements HandleDecorator {

    private DataSource dataSource;

    public Handle decorate(IDBI idbi, Handle handle) {

        ProxyFactory pf = new ProxyFactory();
        pf.setProxyTargetClass(false);
        pf.setInterfaces(new Class[] {Handle.class});
        pf.setTarget(handle);
        pf.addAdvice(new SQLExceptionTranslatingThrowsAdvice(dataSource));

        Handle proxy = (Handle)pf.getProxy();

        return proxy;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
