/**
 * JSR-94 support
 */
package org.springmodules.jsr94.rulesource;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleSession;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetRegisterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Convenience superclass for RuleSource implementation. The implementations must implement the
 * registerRuleExecutionSets() method that registeres the execution sets. This method can execute
 * multiple RuleExecutionSets. 
 * The subclasses can also override the initRuleSource() method to perform additional initialization.
 * 
 * @see org.springmodules.jsr94.rulesource.RuleSource
 * @see #registerRuleExecutionSets()
 * @see #initRuleSource()
 * @author janm
 */
public abstract class AbstractRuleSource implements RuleSource, InitializingBean {

	/**
	 * Shared final Log instance
	 */
	protected static final Log logger = LogFactory.getLog(AbstractRuleSource.class);

	/**
	 * RuleAdministrator instance
	 */
	protected RuleAdministrator ruleAdministrator;

	/**
	 * RuleRuntime instance
	 */
	protected RuleRuntime ruleRuntime;

	/**
	 * RuleServiceProvider implementation
	 */
	private RuleServiceProvider ruleServiceProvider;

	/**
	 * Implementations must override this method to register rule execution sets
	 */
	protected abstract void registerRuleExecutionSets() throws RuleExecutionSetCreateException, RemoteException, IOException, RuleExecutionSetRegisterException;

	/**
	 * Subclasses may override this method to perform additional initialization.
	 * This method is called last in the afterPropertiesSet
	 * @throws Exception If the additional initialization fails
	 * @see AbstractRuleSource#afterPropertiesSet()
	 */
	protected void initRuleSource() throws Exception {
		// noop
	}

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.support.RuleSource#createSession(java.lang.String, java.util.Map, int)
	 */
	public final RuleSession createSession(String bindUri, Map properties, int type) throws RuleExecutionSetNotFoundException, RemoteException, RuleSessionTypeUnsupportedException, RuleSessionCreateException {
		return ruleRuntime.createRuleSession(bindUri, properties, type);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public final void afterPropertiesSet() throws Exception {
		if (ruleServiceProvider != null && ruleAdministrator != null) throw new IllegalArgumentException("ruleServiceProvider and ruleAdministrator set on " + getClass().getName());
		if (ruleServiceProvider != null && ruleRuntime != null) throw new IllegalArgumentException("ruleServiceProvider and ruleRuntime set on " + getClass().getName());
		if (ruleServiceProvider != null /* && ruleAdministrator == null && ruleRuntime == null */) {
			if (logger.isInfoEnabled()) {
				logger.info("Using ruleServiceProvider " + ruleServiceProvider.toString() + " to create ruleAdministrator and ruleRuntime.");
			}
			ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
			ruleRuntime = ruleServiceProvider.getRuleRuntime();
		}

		if (ruleAdministrator == null) throw new IllegalArgumentException("Must set ruleAdministrator on " + getClass().getName());
		if (ruleRuntime == null) throw new IllegalArgumentException("Must set ruleRuntime on " + getClass().getName());
		initRuleSource();
		registerRuleExecutionSets();
	}

	/**
	 * Sets new value for field ruleAdministrator
	 * @param ruleAdministrator The ruleAdministrator to set.
	 */
	public final void setRuleAdministrator(RuleAdministrator ruleAdministrator) {
		this.ruleAdministrator = ruleAdministrator;
	}

	/**
	 * Sets new value for field ruleRuntime
	 * @param ruleRuntime The ruleRuntime to set.
	 */
	public final void setRuleRuntime(RuleRuntime ruleRuntime) {
		this.ruleRuntime = ruleRuntime;
	}

	/**
	 * Sets new value for field ruleServiceProvider
	 * @param ruleServiceProvider The ruleServiceProvider to set.
	 */
	public final void setRuleServiceProvider(RuleServiceProvider ruleServiceProvider) {
		this.ruleServiceProvider = ruleServiceProvider;
	}

}
