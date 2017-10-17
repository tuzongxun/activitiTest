package model;

import java.util.Date;
import java.util.Map;

public class TaskModel {
	private String id;
	private String name;
	private String processInstanceId;
	private String assignee;
	private Date createTime;
	private String processDefId;
	/**
	 * 上一个节点提交的数据
	 */
	private Map<String, String> formData;
	/**
	 * 上一个节点对应的form表单
	 */
	private String lastForm;
	/**
	 * 当前任务对应的formkey
	 */
	private String formKey;
	private String deploymentName;
	/**
	 * 启动流程（申请）时提交的数据以及对应的form，组合起来的html
	 */
	private String firstFormAndData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}

	public Map<String, String> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, String> formData) {
		this.formData = formData;
	}

	public String getLastForm() {
		return lastForm;
	}

	public void setLastForm(String lastForm) {
		this.lastForm = lastForm;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getDeploymentName() {
		return deploymentName;
	}

	public void setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
	}

	public String getFirstFormAndData() {
		return firstFormAndData;
	}

	public void setFirstFormAndData(String firstFormAndData) {
		this.firstFormAndData = firstFormAndData;
	}

	@Override
	public String toString() {
		return "TaskModel [id=" + id + ", name=" + name
				+ ", processInstanceId=" + processInstanceId + ", assignee="
				+ assignee + ", createTime=" + createTime + ", processDefId="
				+ processDefId + ", formData=" + formData + ", lastForm="
				+ lastForm + ", formKey=" + formKey + ", deploymentName="
				+ deploymentName + ", firstFormAndData=" + firstFormAndData
				+ "]";
	}

}
