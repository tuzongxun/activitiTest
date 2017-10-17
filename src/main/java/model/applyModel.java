package model;

public class applyModel {
	private String proDefId;
	private String key;
	private String name;
	private String appPerson;
	private String cause;
	private String content;
	private String proPerson;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAppPerson() {
		return appPerson;
	}

	public void setAppPerson(String appPerson) {
		this.appPerson = appPerson;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProPerson() {
		return proPerson;
	}

	public void setProPerson(String proPerson) {
		this.proPerson = proPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProDefId() {
		return proDefId;
	}

	public void setProDefId(String proDefId) {
		this.proDefId = proDefId;
	}

	@Override
	public String toString() {
		return "applyModel [proDefId=" + proDefId + ", key=" + key + ", name="
				+ name + ", appPerson=" + appPerson + ", cause=" + cause
				+ ", content=" + content + ", proPerson=" + proPerson + "]";
	}

}
