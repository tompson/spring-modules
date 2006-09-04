/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springmodules.orm.orbroker;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.orbroker.Broker;
import net.sourceforge.orbroker.BrokerException;
import net.sourceforge.orbroker.Executable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

/**
 * Helper class that simplifies O/R Broker data access code, and converts
 * checked BrokerException into unchecked DataAccessExceptions,
 * following the <code>org.springframework.dao</code> exception hierarchy.
 * Uses the same SQLExceptionTranslator mechanism as JdbcTemplate.
 * <p/>
 * <p>Typically used to implement data access or business logic services that
 * use O/R Broker within their implementation but are O/R Broker-agnostic in their
 * interface. The latter or code calling the latter only have to deal with
 * domain objects, query objects, and <code>org.springframework.dao</code> exceptions.
 * <p/>
 * <p>The central method is <code>execute</code>, supporting O/R Broker code
 * implementing the BrokerCallback interface. It provides O/R Broker Executable
 * handling such that neither the BrokerCallback implementation nor the calling
 * code needs to explicitly care about retrieving/closing O/R Broker Executables.
 * <p/>
 * <p>Can be used within a service implementation via direct instantiation
 * with a Broker reference, or get prepared in an application context
 * and given to services as bean reference.
 *
 * @author Omar Irbouh
 * @see BrokerAccessor
 * @see BrokerOperations
 * @since 2005.06.02
 */
public class BrokerTemplate extends BrokerAccessor implements BrokerOperations {

	/**
	 * Create a new BrokerTemplate instance.
	 */
	public BrokerTemplate() {
	}

	/**
	 * Create a new BrokerTemplate instance.
	 *
	 * @param broker Broker to use for creating executables
	 */
	public BrokerTemplate(Broker broker) {
		setBroker(broker);
		afterPropertiesSet();
	}

	// Helper methods
	public Object execute(BrokerCallback action) throws DataAccessException {
		Assert.notNull(getBroker(), "broker is required");

		//obtain a JDBC Connection
		Connection connection = DataSourceUtils.getConnection(getDataSource());
		//obtain a new Executable
		Executable executable = newExecutable(getBroker(), connection);
		try {
			return action.doInBroker(executable);
		} catch (BrokerException e) {
			throw convertBrokerException(e);
		} catch (RuntimeException e) {
			// callback code threw application exception
			throw e;
		} finally {
			// release the Executable
			releaseExecutable(getBroker(), executable);
			// release the JDBC Connection
			DataSourceUtils.releaseConnection(connection, getDataSource());
		}
	}

	public List executeWithListResult(BrokerCallback action) throws DataAccessException {
		return (List) execute(action);
	}

	public int executeWithIntResult(BrokerCallback action) throws DataAccessException {
		return ((Integer) execute(action)).intValue();
	}

	public boolean executeWithBooleanResult(BrokerCallback action) throws DataAccessException {
		return ((Boolean) execute(action)).booleanValue();
	}

	// BrokerAccessor Methods implementation
	public int execute(final String statementID) throws DataAccessException {
		return execute(statementID, new String[0], new Object[0]);
	}

	public int execute(final String statementID, final String paramName, final Object value) throws DataAccessException {
		return execute(statementID, new String[]{paramName}, new Object[]{value});
	}

	public int execute(final String statementID, final String[] paramNames, final Object[] values) throws DataAccessException {
		return executeWithIntResult(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return new Integer(executable.execute(statementID));
			}
		});
	}

	public int executeBatch(final String statementID, final String batchParameterName,
													final Collection batchParameters) throws DataAccessException {
		return executeWithIntResult(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				return new Integer(executable.executeBatch(statementID, batchParameterName, batchParameters));
			}
		});
	}

	public int[] executeBatch(final String statementID, final String batchParameterName,
														final Object[] batchParameters) throws DataAccessException {
		return (int[]) execute(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				return executable.executeBatch(statementID, batchParameterName, batchParameters);
			}
		});
	}

	public Object selectOne(final String statementID) throws DataAccessException {
		return selectOne(statementID, new String[0], new Object[0]);
	}

	public Object selectOne(final String statementID, final String paramName, final Object value) throws DataAccessException {
		return selectOne(statementID, new String[]{paramName}, new Object[]{value});
	}

	public Object selectOne(final String statementID, final String[] paramNames, final Object[] values) throws DataAccessException {
		return execute(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return executable.selectOne(statementID);
			}
		});
	}

	public boolean selectOne(final String statementID, final Object resultObject) throws DataAccessException {
		return selectOne(statementID, new String[0], new Object[0], resultObject);
	}

	public boolean selectOne(final String statementID, final String paramName, final Object value,
													 final Object resultObject) throws DataAccessException {
		return selectOne(statementID, new String[]{paramName}, new Object[]{value}, resultObject);
	}

	public boolean selectOne(final String statementID, final String[] paramNames, final Object[] values,
													 final Object resultObject) throws DataAccessException {
		return executeWithBooleanResult(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return new Boolean(executable.selectOne(statementID, resultObject));
			}
		});
	}

	public Object selectOneFromMany(final String statementID, final int fromRow) throws DataAccessException {
		return selectOneFromMany(statementID, fromRow, new String[0], new Object[0]);
	}

	public Object selectOneFromMany(final String statementID, final int fromRow,
																	final String paramName, final Object value) throws DataAccessException {
		return selectOneFromMany(statementID, fromRow, new String[]{paramName}, new Object[]{value});
	}

	public Object selectOneFromMany(final String statementID, final int fromRow,
																	final String[] paramNames, final Object[] values) throws DataAccessException {
		return execute(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return executable.selectOneFromMany(statementID, fromRow);
			}
		});
	}

	public List selectMany(final String statementID) throws DataAccessException {
		return selectMany(statementID, new String[0], new Object[0]);
	}

	public List selectMany(final String statementID, final String paramName, final Object value) throws DataAccessException {
		return selectMany(statementID, new String[]{paramName}, new Object[]{value});
	}

	public List selectMany(final String statementID, final String[] paramNames, final Object[] values) throws DataAccessException {
		return executeWithListResult(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return executable.selectMany(statementID);
			}
		});
	}

	public int selectMany(final String statementID, final Collection resultCollection) throws DataAccessException {
		return selectMany(statementID, new String[0], new Object[0], resultCollection);
	}

	public int selectMany(final String statementID, final String paramName, final Object value,
												final Collection resultCollection) throws DataAccessException {
		return selectMany(statementID, new String[]{paramName}, new Object[]{value}, resultCollection);
	}

	public int selectMany(final String statementID, final String[] paramNames, final Object[] values,
												final Collection resultCollection) throws DataAccessException {
		return executeWithIntResult(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return new Integer(executable.selectMany(statementID, resultCollection));
			}
		});
	}

	public List selectMany(final String statementID, final int startRow, final int rowCount) throws DataAccessException {
		return selectMany(statementID, new String[0], new Object[0], startRow, rowCount);
	}

	public List selectMany(final String statementID, final String paramName, final Object value,
												 final int startRow, final int rowCount) throws DataAccessException {
		return selectMany(statementID, new String[]{paramName}, new Object[]{value}, startRow, rowCount);
	}

	public List selectMany(final String statementID, final String[] paramNames, final Object[] values,
												 final int startRow, final int rowCount) throws DataAccessException {
		return executeWithListResult(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return executable.selectMany(statementID, startRow, rowCount);
			}
		});
	}

	public int selectMany(final String statementID, final Collection resultCollection,
												final int startRow, final int rowCount) throws DataAccessException {
		return selectMany(statementID, new String[0], new Object[0], resultCollection,
				startRow, rowCount);
	}

	public int selectMany(final String statementID, final String paramName, final Object value,
												final Collection resultCollection, final int startRow, final int rowCount) throws DataAccessException {
		return selectMany(statementID, new String[]{paramName}, new Object[]{value}, resultCollection,
				startRow, rowCount);
	}

	public int selectMany(final String statementID, final String[] paramNames, final Object[] values,
												final Collection resultCollection, final int startRow, final int rowCount) throws DataAccessException {
		return executeWithIntResult(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return new Integer(executable.selectMany(statementID, resultCollection, startRow, rowCount));
			}
		});
	}

	public Iterator iterate(final String statementID, final int fetchSize) throws DataAccessException {
		return iterate(statementID, fetchSize, new String[0], new Object[0]);
	}

	public Iterator iterate(final String statementID, final int fetchSize,
													final String paramName, final Object value) throws DataAccessException {
		return iterate(statementID, fetchSize, new String[]{paramName}, new Object[]{value});
	}

	public Iterator iterate(final String statementID, final int fetchSize,
													final String[] paramNames, final Object[] values) throws DataAccessException {
		return (Iterator) execute(new BrokerCallback() {
			public Object doInBroker(Executable executable) throws BrokerException {
				applyNamedParamsToExecutable(executable, paramNames, values);
				return executable.iterate(statementID, fetchSize);
			}
		});
	}

	protected void applyNamedParamsToExecutable(Executable executable, final String[] paramNames, final Object[] values) {
		if (paramNames != null && values != null && paramNames.length != values.length)
		{
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		for (int i = 0; i < paramNames.length; i++) {
			String paramName = paramNames[i];
			Object value = values[i];
			executable.setParameter(paramName, value);
		}
	}

}