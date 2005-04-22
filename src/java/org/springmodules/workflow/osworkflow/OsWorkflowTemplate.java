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

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

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

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setInitialAction(Integer initialAction) {
		this.initialAction = initialAction;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasText(this.workflowName)) {
			throw new FatalBeanException("Property [workflowName] is required.");
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
				WorkflowContext.setInstanceId(id);
				return null;
			}
		});
	}

	public void doAction(final int actionId) {
		this.doAction(actionId, null);
	}

	public void doAction(final int actionId, final Map inputs) {
		this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				workflow.doAction(WorkflowContext.getInstanceId(), actionId, inputs);
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
				return workflow.getHistorySteps(WorkflowContext.getInstanceId());
			}
		});
	}

	public List getCurrentSteps() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getCurrentSteps(WorkflowContext.getInstanceId());
			}
		});
	}

	public List getHistoryStepDescriptors() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				List steps = workflow.getHistorySteps(WorkflowContext.getInstanceId());
		    return convertStepsToStepDescriptors(steps, workflow);
			}
		});
	}

	public List getCurrentStepDescriptors() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				List steps = workflow.getCurrentSteps(WorkflowContext.getInstanceId());
		    return convertStepsToStepDescriptors(steps, workflow);
			}
		});
	}

	public int[] getAvailableActions() {
		return this.getAvailableActions(null);
	}

	public int[] getAvailableActions(final Map inputs) {
		return (int[])this.execute(new OsWorkflowCallback(){
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getAvailableActions(WorkflowContext.getInstanceId(), inputs);
			}
		});
	}

	public int getEntryState() {
		Integer state = (Integer) this.execute(new OsWorkflowCallback() {
					public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
						return new Integer(workflow.getEntryState(WorkflowContext.getInstanceId()));
					}
				});

		return state.intValue();
	}

	public Object execute(OsWorkflowCallback callback) {
		try {
			Workflow workflow = createWorkflow(WorkflowContext.getCaller());
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

}
