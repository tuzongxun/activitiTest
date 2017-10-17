package model;

public class processModel {
	private String id;
	private String deploymentId;
	private String key;
	private String resourceName;
	private int version;
	private String name;
	private String diagramResourceName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiagramResourceName() {
		return diagramResourceName;
	}

	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "processModel [id=" + id + ", deploymentId=" + deploymentId
				+ ", key=" + key + ", resourceName=" + resourceName
				+ ", version=" + version + ", name=" + name
				+ ", diagramResourceName=" + diagramResourceName + "]";
	}

}
