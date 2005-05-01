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
 * @author Rob Harrop
 */
public class OsWorkflowTemplate implements InitializingBean {


	private Configuration configuration = new DefaultConfiguration();

	private Integer initialAction = new Integer(0);

	private String workflowName;

	private WorkflowContextManager contextManager;

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setInitialAction(Integer initialAction) {
		this.initialAction = initialAction;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public void setContextManager(WorkflowContextManager contextManager) {
		this.contextManager = contextManager;
	}

	public WorkflowContextManager getContextManager() {
		return this.contextManager;
	}

	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasText(this.workflowName)) {
			throw new FatalBeanException("Property [workflowName] is required.");
		}

		if (this.contextManager == null) {
			throw new FatalBeanException("Property [contextManager] is required");
		}
	}

	public void initialize() {
		this.initialize(this.initialAction.intValue(), null);
	}

	public void initialize(final Map inputs) {
		this.initialize(this.initialAction.intValue(), inputs);
	}


	public void initialize(final int initialAction) {
		this.initialize(initialAction, null);
	}

	public void initialize(final int initialAction, final Map inputs) {
		this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				long id = workflow.initialize(OsWorkflowTemplate.this.workflowName, initialAction, inputs);
				OsWorkflowTemplate.this.contextManager.setInstanceId(id);
				return null;
			}
		});
	}

	public void doAction(final int actionId) {
		this.doAction(actionId, null);
	}

	public void doAction(final int actionId, Object inputKey, Object inputVal) {
		Map inputs = new HashMap();
		inputs.put(inputKey, inputVal);
		this.doAction(actionId, inputs);
	}

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

	private List convertStepsToStepDescriptors(List steps, Workflow workflow) {
		WorkflowDescriptor descriptor = workflow.getWorkflowDescriptor(OsWorkflowTemplate.this.workflowName);

		List stepDescriptors = new ArrayList();

		for (int i = 0; i < steps.size(); i++) {
			Step step = (Step) steps.get(i);
			stepDescriptors.add(descriptor.getStep(step.getStepId()));
		}

		return Collections.unmodifiableList(stepDescriptors);
	}

	protected Workflow createWorkflow(String caller) throws WorkflowException {
		return new BasicWorkflow(caller);
	}

	protected long getInstanceId() {
		return this.contextManager.getInstanceId();
	}
}
