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

package org.springmodules.workflow.osworkflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.query.WorkflowExpressionQuery;
import com.opensymphony.workflow.spi.Step;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * Template class that simplifies interaction with the <a href="http://www.opensymphony.com/osworkflow">OSWorkflow</a>
 * workflow engine by hiding the management of context parameters such as the caller and workflow ID.
 * <p/>
 * Translates all checked <code>com.opensymphony.workflow.WorkflowException</code>s into unchecked
 * <code>org.springmodules.workflow.WorkflowException</code>s.
 * <p/>
 * Most operations on the <code>Workflow</code> instance are mirrored here, but for operations that require
 * lengthy interaction with a <code>Workflow</code> a custom <code>OsWorkflowCallback</code> can be used in conjunction
 * with the <code>execute(OsWorkflowCallback)</code> method.
 * <p/>
 * It is intended that a single <code>OsWorkflowTemplate</code> will be used to manage all instances of a single workflow
 * within your application. Because of this, the workflow name is not a parameter on any of operations exposed by this
 * class. Instead, the workflow name is set via the required <code>workflowName</code> property. The same workflow name
 * is used for all operations.
 * <p/>
 * Workflow context parameters such as the caller name and current instance ID are managed by an implementation of
 * <code>WorkflowContextManager</code>. Out of the box, the <code>ThreadLocalWorkflowContextManager</code> maintains
 * workflow context information on a per-thread basis, which is ideal for web applications. It is intended that context
 * information be set externally to the core application code. For this purpose, Spring Modules provides the
 * <code>AbstractWorkflowContextHandlerInterceptor</code> (and its implementations) which allows for context information
 * to be managed transparently in a web environment.
 * <p/>
 * Both the <code>workflowName</code> and <code>contextManager</code> parameters are required.
 *
 * @author Rob Harrop
 * @see WorkflowException
 * @see org.springmodules.workflow.WorkflowException
 * @see #execute(OsWorkflowCallback)
 * @see WorkflowContextManager
 * @see org.springmodules.workflow.osworkflow.web.AbstractWorkflowContextHandlerInterceptor
 * @see #setContextManager(WorkflowContextManager)
 * @see #setWorkflowName(String)
 * @since 0.2
 */
public class OsWorkflowTemplate implements InitializingBean {

	/**
	 * The <code>Configuration</code> used to load workflow definitions. Uses the OSWorkflow <code>DefaultConfiguration</code>
	 * class by default.
	 */
	private Configuration configuration = new DefaultConfiguration();

	/**
	 * The ID of the initial action to call when initializing a workflow instance. Defaults to <code>0</code>.
	 */
	private Integer initialAction = new Integer(0);

	/**
	 * The name of the workflow definition to use.
	 */
	private String workflowName;

	/**
	 * The <code>WorkflowContextManager</code> used to manage caller and instance ID context.
	 */
	private WorkflowContextManager contextManager;

	/**
	 * Sets the <code>Configuration<code> used to load workflow definitions.
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Sets the inital action used when initializing a workflow instance.
	 */
	public void setInitialAction(Integer initialAction) {
		this.initialAction = initialAction;
	}

	/**
	 * Sets the name of the workflow definition to use. Required.
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	/**
	 * Gets the workflow name.
	 */
	public String getWorkflowName() {
		return this.workflowName;
	}

	/**
	 * Sets the <code>WorkflowContextManager</code> to use. Required.
	 */
	public void setContextManager(WorkflowContextManager contextManager) {
		this.contextManager = contextManager;
	}

	/**
	 * Gets the <code>WorkflowContextManager</code>.
	 */
	public WorkflowContextManager getContextManager() {
		return this.contextManager;
	}

	/**
	 * Checks that both the <code>workflowName</code> and <code>contextManager</code> properties have both been specified.
	 *
	 * @throws FatalBeanException if any of the required properties is not set.
	 */
	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasText(this.workflowName)) {
			throw new FatalBeanException("Property [workflowName] is required.");
		}

		if (this.contextManager == null) {
			throw new FatalBeanException("Property [contextManager] is required");
		}
	}

	/**
	 * Initialize a workflow instance using the default initial action.
	 *
	 * @see #setInitialAction(Integer)
	 * @see #initialize(int, java.util.Map)
	 */
	public void initialize() {
		this.initialize(this.initialAction.intValue(), null);
	}

	/**
	 * Initialize a workflow instance using the default initial action and the supplied inputs.
	 *
	 * @see #setInitialAction(Integer)
	 * @see #initialize(int, java.util.Map)
	 */
	public void initialize(final Map inputs) {
		this.initialize(this.initialAction.intValue(), inputs);
	}

	/**
	 * Initialize a workflow instance using the supplied inital action.
	 *
	 * @see #initialize(int, java.util.Map)
	 */
	public void initialize(final int initialAction) {
		this.initialize(initialAction, null);
	}

	/**
	 * Initialize a workflow instance using the supplied initial action and inputs. The caller's identity is retreived
	 * from the <code>WorkflowContextManager</code>. The resulting workflow instance ID is stored using the
	 * <code>WorkflowContextManager</code>.
	 *
	 * @see WorkflowContextManager#getCaller()
	 * @see WorkflowContextManager#setInstanceId(long)
	 */
	public void initialize(final int initialAction, final Map inputs) {
		this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				long id = workflow.initialize(OsWorkflowTemplate.this.workflowName, initialAction, inputs);
				OsWorkflowTemplate.this.contextManager.setInstanceId(id);
				return null;
			}
		});
	}

	/**
	 * Do the workflow action specified by the given action ID.
	 *
	 * @see #doAction(int, java.util.Map)
	 */
	public void doAction(final int actionId) {
		this.doAction(actionId, null);
	}

	/**
	 * Do the workflow action specified by the given action ID. Passes in a single input under the specified key.
	 *
	 * @param actionId the ID of the action to execute
	 * @param inputKey the key of the input
	 * @param inputVal the value of the input
	 * @see #doAction(int, java.util.Map)
	 */
	public void doAction(final int actionId, Object inputKey, Object inputVal) {
		Map inputs = new HashMap();
		inputs.put(inputKey, inputVal);
		this.doAction(actionId, inputs);
	}

	/**
	 * Do the workflow action specified by the given action ID passing in the supplied inputs. The workflow instance ID
	 * to execute the action against is retreived from the <code>WorkflowContextManager</code>.
	 *
	 * @see WorkflowContextManager#getInstanceId()
	 */
	public void doAction(final int actionId, final Map inputs) {
		this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				workflow.doAction(getInstanceId(), actionId, inputs);
				return null;
			}
		});
	}


	public WorkflowDescriptor getWorkflowDescriptor() {
		return (WorkflowDescriptor) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getWorkflowDescriptor(OsWorkflowTemplate.this.workflowName);
			}
		});
	}

	public List getHistorySteps() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getHistorySteps(getInstanceId());
			}
		});
	}

	public List getCurrentSteps() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getCurrentSteps(getInstanceId());
			}
		});
	}

	public List getHistoryStepDescriptors() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				List steps = workflow.getHistorySteps(getInstanceId());
				return convertStepsToStepDescriptors(steps, workflow);
			}
		});
	}

	public List getCurrentStepDescriptors() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				List steps = workflow.getCurrentSteps(getInstanceId());
				return convertStepsToStepDescriptors(steps, workflow);
			}
		});
	}

	public int[] getAvailableActions() {
		return this.getAvailableActions(null);
	}

	public int[] getAvailableActions(final Map inputs) {
		return (int[]) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getAvailableActions(getInstanceId(), inputs);
			}
		});
	}


	public List getAvailableActionDescriptors() {
		return this.getAvailableActionDescriptors(null);
	}

	public List getAvailableActionDescriptors(final Map inputs) {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				WorkflowDescriptor descriptor = workflow.getWorkflowDescriptor(OsWorkflowTemplate.this.workflowName);

				int[] availableActions = workflow.getAvailableActions(getInstanceId(), inputs);
				List actionDescriptors = new ArrayList(availableActions.length);

				for (int i = 0; i < availableActions.length; i++) {
					actionDescriptors.add(descriptor.getAction(availableActions[i]));
				}

				return actionDescriptors;
			}
		});
	}

	public int getEntryState() {
		Integer state = (Integer) this.execute(new OsWorkflowCallback() {
					public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
						return new Integer(workflow.getEntryState(getInstanceId()));
					}
				});

		return state.intValue();
	}

	public PropertySet getPropertySet() {
		return (PropertySet) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getPropertySet(getInstanceId());
			}
		});
	}

	public boolean canInitialize(final String workflowName, final int initialStep) {
		return this.canInitialize(workflowName, initialStep, null);
	}

	public boolean canInitialize(final String workflowName, final int initialStep, final Map inputs) {
		Boolean returnValue = (Boolean) this.execute(new OsWorkflowCallback() {
					public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
						boolean canInit = workflow.canInitialize(workflowName, initialStep, inputs);
						return (canInit) ? Boolean.TRUE : Boolean.FALSE;
					}
				});
		return returnValue.booleanValue();
	}

	public boolean canModifyEntryState(final int newState) {
		Boolean returnValue = (Boolean) this.execute(new OsWorkflowCallback() {
					public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
						boolean canModify = workflow.canModifyEntryState(getInstanceId(), newState);
						return (canModify) ? Boolean.TRUE : Boolean.FALSE;
					}
				});

		return returnValue.booleanValue();
	}

	public void changeEntryState(final int newState) {
		this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				workflow.changeEntryState(getInstanceId(), newState);
				return null;
			}
		});
	}

	public List query(final WorkflowExpressionQuery query) {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.query(query);
			}
		});
	}

	public Object execute(OsWorkflowCallback callback) {
		try {
			Workflow workflow = createWorkflow(OsWorkflowTemplate.this.contextManager.getCaller());
			workflow.setConfiguration(this.configuration);
			return callback.doWithWorkflow(workflow);
		}
		catch (WorkflowException ex) {
			// TODO: proper exception translation
			throw new org.springmodules.workflow.WorkflowException("", ex);
		}
	}


	protected Workflow createWorkflow(String caller) throws WorkflowException {
		return new BasicWorkflow(caller);
	}

	protected long getInstanceId() {
		return this.contextManager.getInstanceId();
	}

	private List convertStepsToStepDescriptors(List steps, Workflow workflow) {
		WorkflowDescriptor descriptor = workflow.getWorkflowDescriptor(OsWorkflowTemplate.this.workflowName);

		List stepDescriptors = new ArrayList();

		for (int i = 0; i < steps.size(); i++) {
			Step step = (Step) steps.get(i);
			stepDescriptors.add(descriptor.getStep(step.getStepId()));
		}

		return Collections.unmodifiableList(stepDescriptors);
	}
}
