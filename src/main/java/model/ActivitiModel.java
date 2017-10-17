package model;

import java.util.Date;

public class ActivitiModel {
	private String id;
	private String name;
	private String key;
	private String description;
	private Date createTime;
	private Date lastUpdateTime;
	private int version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ActivitiModel [id=" + id + ", name=" + name + ", key=" + key
				+ ", description=" + description + ", createTime=" + createTime
				+ ", lastUpdateTime=" + lastUpdateTime + ", version=" + version
				+ "]";
	}

}
