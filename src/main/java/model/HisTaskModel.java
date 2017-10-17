package model;

import java.util.Date;

public class HisTaskModel {
	private String id;
	private String name;
	private String assignee;
	private String processInstanceId;
	private Date startTime;
	private Date endTime;
	private String taskType;
	private String allForm;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

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

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAllForm() {
		return allForm;
	}

	public void setAllForm(String allForm) {
		this.allForm = allForm;
	}

	@Override
	public String toString() {
		return "HisTaskModel [id=" + id + ", name=" + name + ", assignee="
				+ assignee + ", processInstanceId=" + processInstanceId
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", taskType=" + taskType + ", allForm=" + allForm + "]";
	}

}
