
package org.springmodules.examples.workflow.osworkflow.service;

import java.util.List;

import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

/**
 * @author robh
 */
public class OsWorkflowDocumentApprovalWorkflow implements DocumentApprovalWorkflow {

	private static final int UPLOAD_DOCUMENT = 1;
	
	private OsWorkflowTemplate workflowTemplate;

	public void setWorkflowTemplate(OsWorkflowTemplate workflowTemplate) {
		this.workflowTemplate = workflowTemplate;
	}

	public void start() {
		this.workflowTemplate.initialize();
	}

	public void uploadDocument() {
		this.workflowTemplate.doAction(UPLOAD_DOCUMENT);
	}

	public List getHistorySteps() {
		return this.workflowTemplate.getHistorySteps();
	}
}
