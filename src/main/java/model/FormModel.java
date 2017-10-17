package model;

public class FormModel {
	// 'type' : type_value,'formid':formid,'parse_form':parse_form
	private String formId;
	private String type;
	private Integer formid;
	private String parse_form;
	private String formType;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFormid() {
		return formid;
	}

	public String getParse_form() {
		return parse_form;
	}

	public void setFormid(Integer formid) {
		this.formid = formid;
	}

	public void setParse_form(String parse_form) {
		this.parse_form = parse_form;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@Override
	public String toString() {
		return "FormModel [formId=" + formId + ", type=" + type + ", formid="
				+ formid + ", parse_form=" + parse_form + ", formType="
				+ formType + "]";
	}

}
