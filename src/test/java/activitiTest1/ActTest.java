package activitiTest1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.MyFormService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(SpringJUnit4ClassRunner.class)
// @WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring.xml" })
public class ActTest {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private FormService formService;
	@Autowired
	private MyFormService myFormService;

	/**
	 * 部署流程定义
	 * 
	 * @author：tuzongxun
	 * @Title: deployeTest
	 * @param
	 * @return void
	 * @date Mar 16, 2016 11:16:37 AM
	 * @throws
	 */
	@Test
	public void deployeTest() {
		String modelId = "40001";
		try {
			Model modelData = repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData
							.getId()));
			byte[] bpmnBytes = null;
			BpmnModel model = new BpmnJsonConverter()
					.convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = repositoryService.createDeployment()
					.name(modelData.getName())
					.addString(processName, new String(bpmnBytes)).deploy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询流程定义
	 * 
	 * @author：tuzongxun
	 * @Title: startProcess
	 * @param
	 * @return void
	 * @date Mar 16, 2016 11:16:49 AM
	 * @throws
	 */
	@Test
	public void findDeploye() {
		List<Deployment> lists = repositoryService.createDeploymentQuery()
				.list();
		for (Deployment de : lists) {
			ProcessDefinition pro = repositoryService
					.createProcessDefinitionQuery().deploymentId(de.getId())
					.singleResult();
			System.out.println(pro.getDeploymentId() + "," + pro.getKey() + ","
					+ pro.getVersion());
		}
	}

	/**
	 * @throws XMLStreamException
	 *             根据key启动流程
	 * 
	 * @author：tuzongxun
	 * @Title: startPro
	 * @param
	 * @return void
	 * @date Mar 16, 2016 11:24:03 AM
	 * @throws
	 */
	@Test
	public void startPro() throws XMLStreamException {
		// String keyString = "process";
		// Map<String, Object> variables = new HashMap<String, Object>();
		// variables.put("person1", "zhangsan");
		// runtimeService.startProcessInstanceByKey(keyString, variables);
		List<ProcessDefinition> lists = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId("qingjia:2:115004")
				.orderByProcessDefinitionVersion().desc().list();
		ProcessDefinition processDefinition = lists.get(0);
		processDefinition.getCategory();
		String resourceName = processDefinition.getResourceName();
		InputStream inputStream = repositoryService.getResourceAsStream(
				processDefinition.getDeploymentId(), resourceName);
		BpmnXMLConverter converter = new BpmnXMLConverter();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
		BpmnModel bpmnModel = converter.convertToBpmnModel(reader);
		Process process = bpmnModel.getMainProcess();
		Collection<FlowElement> elements = process.getFlowElements();
		Iterator<FlowElement> iterator = elements.iterator();
		String assginee1 = "lisi";
		while (iterator.hasNext()) {
			FlowElement flowElement = iterator.next();
			// System.out.println(flowElement.getClass().getSimpleName());
			String classNames = flowElement.getClass().getSimpleName();
			System.out.println(classNames);
			if (classNames.equals("UserTask")) {
				UserTask userTask = (UserTask) flowElement;
				String assginee11 = userTask.getAssignee();
				System.out.println(assginee1);
				System.out.println(assginee11);
				if (assginee1.equals(assginee11)) {
					iterator.next();
					FlowElement flowElement2 = iterator.next();
					String classNames1 = flowElement2.getClass()
							.getSimpleName();
					// 结束
					if (classNames1.equals("EndEvent")) {

					} else {
						// 继续分配下一个任务
					}
				}

			}
			// if (flowElement.getClass().getSimpleName().equals("UserTask")) {
			// // System.out.println(flowElement.getClass().getSimpleName());
			// UserTask userTask = (UserTask) flowElement;
			// String assignee = userTask.getAssignee();
			// // System.out.println("assignee:" + assignee);
			// int index1 = assignee.indexOf("{");
			// int index2 = assignee.indexOf("}");
			// // System.out.println("assigneeeeeeeeeeeee:"
			// // + assignee.substring(index1 + 1, index2));
			// // System.out.println("name:" + userTask.getName());
			//
			// }
		}
	}

	/**
	 * 模型列表
	 * 
	 * @author：tuzongxun
	 * @Title: modelList
	 * @param
	 * @return void
	 * @date Mar 16, 2016 8:07:41 PM
	 * @throws
	 */
	@Test
	public void modelList() {
		List<Model> modelList = null;
		modelList = repositoryService.createModelQuery().list();
		for (Model model : modelList) {
			System.out.println(model.getCreateTime() + "," + model.getId()
					+ "," + model.getKey() + "," + model.getName() + ","
					+ model.getVersion());
		}
	}

	@Test
	public void hisList() {
		List<HistoricVariableInstance> hisList = historyService
				.createHistoricVariableInstanceQuery()
				.processInstanceId("100001").list();
		for (HistoricVariableInstance hisVariable : hisList) {
			System.out.println(hisVariable.getVariableName() + ":"
					+ hisVariable.getValue());
		}
	}

	/**
	 * 查询流程定义是否有表单
	 * 
	 * @author：tuzongxun
	 * @Title: findForm
	 * @param
	 * @return void
	 * @date Mar 23, 2016 3:27:14 PM
	 * @throws
	 */
	@Test
	public void findForm() {
		String processDefId = "process:2:132611";
		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put("formKey", "aaa.form");
		formService.submitStartFormData(processDefId, formMap);
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefId).singleResult();
		boolean hasStartFormKey = processDefinition.hasStartFormKey();
		System.out.println(hasStartFormKey);
		StartFormData formData = formService.getStartFormData(processDefId);
	}

	@Test
	public void findFormByName() {
		String formName = "qingjiadan";
		myFormService.findFormByFormName(formName);
	}

	@Test
	public void findform() {
		String defIdString = "qingjiadan:1:155008";
		String form = (String) formService
				.getRenderedStartForm("qingjiadan:1:152505");
		StartFormData form1 = formService
				.getStartFormData("qingjiadan:1:152505");
		System.out.println(form);
		System.out.println(form1.getFormKey());
		System.out.println(form1.getFormProperties());
	}

	@Test
	public void findForms() throws JsonProcessingException, IOException {
		String modelId = "177748";
		Model modelData = repositoryService.getModel(modelId);

		ObjectNode modelNode = (ObjectNode) new ObjectMapper()
				.readTree(repositoryService.getModelEditorSource(modelData
						.getId()));
		byte[] bpmnBytes = null;
		BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
		bpmnBytes = new BpmnXMLConverter().convertToXML(model);

		List<JsonNode> forms = modelNode.findValues("formkeydefinition");
		for (JsonNode node : forms) {
			// aaa.form
			String formName = node.textValue();
			if (!"".equals(formName)) {
				// 就是页面的html代码根据formName找到
				String formContent = myFormService.findFormByFormName(formName);
				ByteArrayInputStream bi = new ByteArrayInputStream(
						formContent.getBytes());
				// db.addInputStream(formName, bi);
				break;
			}
		}
		// ///////////////////////
		// DeploymentBuilder db = repositoryService.createDeployment()
		// .name(modelData.getName());

	}

	@Test
	public void getTaskForm() {
		String taskId = "185004";
		System.out.println(formService.getRenderedTaskForm(taskId));
		System.out.println(formService.getTaskFormData(taskId));
	}
}
