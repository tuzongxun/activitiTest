package controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import services.MyFormService;

@Controller
public class FormController {
	@Autowired
	MyFormService myFormService;

	@RequestMapping(value = "/addForm.do", method = RequestMethod.POST)
	@ResponseBody
	public Object addForm(HttpServletRequest request) {
		// String type_value = request.getParameter("type_value");
		// String formid = request.getParameter("formid");
		String parse_form = request.getParameter("parse_form");
		String formType = request.getParameter("formType");
		System.out.println(formType);
		int index1 = parse_form.indexOf("\"template\":");
		int index2 = parse_form.indexOf("\"parse\":");
		String string = parse_form.substring(index1 + 12, index2 - 6);
		string = string.replace("\\", "");
		string = string.replace("{", "");
		string = string.replace("}", "");
		string = string.replace("|", "");
		string = string.replace("-", "");
		myFormService.addForm(formType, string);
		return string;
	}

	@RequestMapping(value = "/findForms.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findForms() {
		List<Map<String, String>> list = myFormService.findForms();
		return list;
	}

	@RequestMapping(value = "/deleteForm.do", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteForm(HttpServletRequest request) {
		String formIde = request.getParameter("formId");
		myFormService.deleteForm(formIde);
		return null;
	}
}
