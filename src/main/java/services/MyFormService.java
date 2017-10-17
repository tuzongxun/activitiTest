package services;

import java.util.List;
import java.util.Map;

public interface MyFormService {
	public List<Map<String, String>> findForms();

	public Object addForm(String formType, String string);

	public void deleteForm(String formId);

	public String findFormByFormName(String formName);

}
