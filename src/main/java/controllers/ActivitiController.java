package controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import model.ActivitiModel;
import model.HisTaskModel;
import model.TaskModel;
import model.processModel;
import services.MyFormService;

@Controller
public class ActivitiController {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private FormService formService;
	@Autowired
	private MyFormService myFormService;
	@Autowired
	private IdentityService identityService;

	/**
	 * 判断用户是否登陆
	 * 
	 * @author：tuzongxun @Title: isLogin @param @param req @param @return @return
	 *                   Boolean @date Mar 17, 2016 5:36:48 PM @throws
	 */
	private boolean isLogin(HttpServletRequest req) {
		boolean isLogin = false;
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute("userName") != null) {
			isLogin = true;
		}
		return isLogin;
	}

	@RequestMapping(value = "/createFlush.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object createFlush(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			map.put("isLogin", "yes");
			map.put("userName", (String) req.getSession().getAttribute("userName"));
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	/**
	 * 创建模型
	 * 
	 * @author：tuzongxun @Title: create @param @param activiti @param @param
	 *                   request @param @param response @param @return @return
	 *                   Object @date Mar 17, 2016 12:30:29 PM @throws
	 */
	@RequestMapping(value = "/create.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object create(@RequestBody ActivitiModel activiti, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		Boolean isLogin = this.isLogin(request);
		if (isLogin) {
			Model newModel = repositoryService.newModel();
			try {

				ObjectMapper objectMapper = new ObjectMapper();
				ObjectNode modelObjectNode = objectMapper.createObjectNode();
				modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, activiti.getName());
				modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
				modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,
				    StringUtils.defaultString(activiti.getDescription()));
				newModel.setMetaInfo(modelObjectNode.toString());
				newModel.setName(activiti.getName());
				newModel.setKey(StringUtils.defaultString(activiti.getKey()));
				repositoryService.saveModel(newModel);
				ObjectNode editorNode = objectMapper.createObjectNode();
				editorNode.put("id", "canvas");
				editorNode.put("resourceId", "canvas");
				ObjectNode stencilSetNode = objectMapper.createObjectNode();
				stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
				editorNode.put("stencilset", stencilSetNode);
				repositoryService.addModelEditorSource(newModel.getId(), editorNode.toString().getBytes("utf-8"));
			} catch (Exception e) {
				e.getStackTrace();
			}
			// response.sendRedirect(request.getContextPath() +
			// "/service/editor?id="
			// + newModel.getId());
			map.put("isLogin", "yes");
			map.put("userName", (String) request.getSession().getAttribute("userName"));
			map.put("path", "/service/editor?id=");
			map.put("modelId", newModel.getId());
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	/**
	 * 根据模型id部署流程定义
	 * 
	 * @author：tuzongxun @Title: deploye @param @param activitiModel @param @param
	 *                   redirectAttributes @param @return @return Object @date
	 *                   Mar 17, 2016 12:30:05 PM @throws
	 */
	@RequestMapping(value = "/deploye.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object deploye(@RequestBody ActivitiModel activitiModel, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			String modelId = activitiModel.getId();
			try {
				// ////////////////////////////////////////
				// 用编程书写人生，让代码集聚力量，使软件展现价值，以程序推动梦想！
				// 获取forms拿到formname
				Model modelData = repositoryService.getModel(modelId);
				ObjectNode modelNode = (ObjectNode) new ObjectMapper()
				    .readTree(repositoryService.getModelEditorSource(modelData.getId()));
				byte[] bpmnBytes = null;
				BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
				bpmnBytes = new BpmnXMLConverter().convertToXML(model);
				DeploymentBuilder db = repositoryService.createDeployment().name(modelData.getName());

				List<JsonNode> forms = modelNode.findValues("formkeydefinition");
				for (JsonNode node : forms) {
					// aaa.form
					String formName = node.textValue();
					if (!"".equals(formName)) {
						// 就是页面的html代码根据formName找到
						String formContent = myFormService.findFormByFormName(formName);
						ByteArrayInputStream bi = new ByteArrayInputStream(formContent.getBytes());
						db.addInputStream(formName, bi);
						break;
					}
				}
				// ////////////////////////
				Deployment deployment = db.addString(modelData.getName() + ".bpmn20.xml", new String(bpmnBytes)).deploy();
				// ///////////////////////
				if (deployment != null && deployment.getId() != null) {
					map.put("isLogin", "yes");
					map.put("userName", (String) req.getSession().getAttribute("userName"));
					map.put("result", "success");
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	/**
	 * 模型列表
	 * 
	 * @author：tuzongxun @Title: modelList @param @return @return Object @date Mar
	 *                   17, 2016 12:29:52 PM @throws
	 */
	@RequestMapping(value = "/modelList.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object modelList(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			List<ActivitiModel> modelList = new ArrayList<ActivitiModel>();
			try {
				List<Model> modelList1 = repositoryService.createModelQuery().list();
				if (modelList1 != null && modelList1.size() > 0) {
					for (Model model : modelList1) {
						ActivitiModel activitiModel = new ActivitiModel();
						activitiModel.setId(model.getId());
						activitiModel.setCreateTime(model.getCreateTime());
						activitiModel.setDescription(model.getMetaInfo());
						activitiModel.setKey(model.getKey());
						activitiModel.setLastUpdateTime(model.getLastUpdateTime());
						activitiModel.setName(model.getName());
						activitiModel.setVersion(model.getVersion());
						modelList.add(activitiModel);
					}
				}
				map.put("isLogin", "yes");
				map.put("userName", (String) req.getSession().getAttribute("userName"));
				map.put("result", "success");
				map.put("data", modelList);

			} catch (Exception e) {
				e.getStackTrace();
			}
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	/**
	 * 流程实例列表
	 * 
	 * @author：tuzongxun @Title: processList @param @return @return Object @date
	 *                   Mar 17, 2016 12:34:10 PM @throws
	 */
	@RequestMapping(value = "/processList.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object processList(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			List<processModel> processList = new ArrayList<processModel>();
			List<ProcessDefinition> processList1 = repositoryService.createProcessDefinitionQuery().list();
			for (ProcessDefinition pro : processList1) {
				processModel processModel = new processModel();
				processModel.setDeploymentId(pro.getDeploymentId());
				processModel.setId(pro.getId());
				processModel.setKey(pro.getKey());
				processModel.setResourceName(pro.getResourceName());
				processModel.setVersion(pro.getVersion());
				processModel.setName(pro.getName());
				processModel.setDiagramResourceName(pro.getDiagramResourceName());
				processList.add(processModel);

			}
			map.put("isLogin", "yes");
			map.put("userName", (String) req.getSession().getAttribute("userName"));
			map.put("result", "success");
			map.put("data", processList);
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	/**
	 * @throws XMLStreamException
	 *           查询流程节点
	 * 
	 * @author：tuzongxun @Title: findFlow @param @return @return Iterator
	 *                   <FlowElement> @date Mar 21, 2016 9:31:42 AM @throws
	 */
	public Iterator<FlowElement> findFlow(String processDefId) throws XMLStreamException {
		List<ProcessDefinition> lists = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefId)
		    .orderByProcessDefinitionVersion().desc().list();
		ProcessDefinition processDefinition = lists.get(0);
		processDefinition.getCategory();
		String resourceName = processDefinition.getResourceName();
		InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
		BpmnXMLConverter converter = new BpmnXMLConverter();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
		BpmnModel bpmnModel = converter.convertToBpmnModel(reader);
		Process process = bpmnModel.getMainProcess();
		Collection<FlowElement> elements = process.getFlowElements();
		Iterator<FlowElement> iterator = elements.iterator();
		return iterator;
	}

	/**
	 * @throws XMLStreamException
	 *           启动流程
	 * 
	 * @author：tuzongxun @Title: startProcess @param @return @return Object @date
	 *                   Mar 17, 2016 2:06:34 PM @throws
	 */
	@RequestMapping(value = "/getStartFormAndStartProcess.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object startProcess1(HttpServletRequest req) throws XMLStreamException {
		Map<String, String[]> formMap = req.getParameterMap();
		String deploymentId = formMap.get("deploymentId")[0];
		// 拿到第一个data_1设置申请人
		String person1 = (String) formMap.get("data_1")[0];
		Map<String, String> map = new HashMap<String, String>();
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			if (deploymentId != null) {
				HttpSession session = req.getSession();
				String assginee = (String) session.getAttribute("userName");
				// /////////////////////////////////
				// String deploymentId = formMap.get("deploymentId");
				ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId)
				    .singleResult();
				String processDefinitionId = pd.getId();
				Map<String, String> formProperties = new HashMap<String, String>();

				Iterator<FlowElement> iterator1 = this.findFlow(processDefinitionId);
				// 取第一个节点，开始节点的行号
				String row = null;
				while (iterator1.hasNext()) {
					FlowElement flowElement = iterator1.next();
					row = flowElement.getXmlRowNumber() + "";
					break;
				}

				// 从request中读取参数然后转换
				Set<Entry<String, String[]>> entrySet = formMap.entrySet();
				for (Entry<String, String[]> entry : entrySet) {
					String key = entry.getKey();
					String value = entry.getValue()[0];
					if (!key.equals("deploymentId")) {
						String keyString = key + row;
						formProperties.put(keyString, value);
					}
				}
				formProperties.put("deploymentId", deploymentId);

				// //////////////////////////

				Iterator<FlowElement> iterator = this.findFlow(pd.getId());
				int i = 1;
				while (iterator.hasNext()) {
					FlowElement flowElement = iterator.next(); // 申请人
					if (flowElement.getClass().getSimpleName().equals("UserTask") && i == 1) {
						UserTask userTask = (UserTask) flowElement;
						String assignee = userTask.getAssignee();
						int index1 = assignee.indexOf("{");
						int index2 = assignee.indexOf("}");
						formProperties.put(assignee.substring(index1 + 1, index2), person1);
						break;
					}
				}
				identityService.setAuthenticatedUserId(assginee);
				ProcessInstance processInstance = formService.submitStartFormData(processDefinitionId, formProperties);
				// ////////////////////////////////////////
				map.put("userName", (String) req.getSession().getAttribute("userName"));
				map.put("isLogin", "yes");
				map.put("result", "success");
			} else {
				map.put("result", "fail");
			}
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	/**
	 * @throws XMLStreamException
	 *           查询我申请未提交的任务
	 * 
	 * @author：tuzongxun @Title: findTask @param @return @return Object @date Mar
	 *                   17, 2016 2:44:11 PM @throws
	 */
	@RequestMapping(value = "/findFirstTask.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object findFirstTask(HttpServletRequest req) throws XMLStreamException {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			List<TaskModel> taskList = new ArrayList<TaskModel>();
			HttpSession session = req.getSession();
			String assginee = (String) session.getAttribute("userName");
			List<Task> taskList1 = taskService.createTaskQuery().taskAssignee(assginee).list();
			if (taskList1 != null && taskList1.size() > 0) {
				for (Task task : taskList1) {
					TaskModel taskModel = new TaskModel();
					// 获取部署名
					String processdefintionId = task.getProcessDefinitionId();
					ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					    .processDefinitionId(processdefintionId).singleResult();

					// 根据taskname和节点判断是否是第一个
					String taskName = task.getName();
					Iterator<FlowElement> iterator = this.findFlow(processdefintionId);
					String row0 = null;
					String eleName0 = null;
					while (iterator.hasNext()) {
						FlowElement flowElement0 = iterator.next();
						// 下一个节点
						FlowElement flowElement = iterator.next();
						String eleName = flowElement.getName();
						if (taskName.equals(eleName)) {
							row0 = flowElement0.getXmlRowNumber() + "";
							eleName0 = flowElement0.getClass().getSimpleName();
							break;
						}
					}

					// 提交申请时
					if (eleName0.equals("StartEvent")) {
						// /////////////////////////
						// 获取流程变量
						Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
						Set<String> keysSet = variables.keySet();
						Iterator<String> keySet = keysSet.iterator();
						Map<String, String> formData = new HashMap<String, String>();
						taskModel.setLastForm(this.getStartForm1((String) variables.get("deploymentId")));

						taskModel.setAssignee(task.getAssignee());
						taskModel.setCreateTime(task.getCreateTime());
						taskModel.setId(task.getId());
						taskModel.setName(task.getName());
						taskModel.setProcessInstanceId(task.getProcessInstanceId());
						taskModel.setProcessDefId(task.getProcessDefinitionId());
						taskModel.setFormKey(task.getFormKey());
						String deploymentId = processDefinition.getDeploymentId();
						Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
						String deploymentName = deployment.getName();
						taskModel.setDeploymentName(deploymentName);
						while (keySet.hasNext()) {
							String key = keySet.next();
							String value = (String) variables.get(key);
							if (key.contains(row0)) {
								formData.put(key, value);
							}
						}
						taskModel.setFormData(formData);
						taskList.add(taskModel);
					}

				}
			}
			map.put("isLogin", "yes");
			map.put("userName", (String) req.getSession().getAttribute("userName"));
			map.put("result", "success");
			map.put("data", taskList);
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	/**
	 * @throws XMLStreamException
	 *           查询别人提交给我的任务
	 * 
	 * @author：tuzongxun @Title: findTask @param @return @return Object @date Mar
	 *                   17, 2016 2:44:11 PM @throws
	 */
	@RequestMapping(value = "/findTask.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object findTask(HttpServletRequest req) throws XMLStreamException {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			List<TaskModel> taskList = new ArrayList<TaskModel>();
			HttpSession session = req.getSession();
			String assginee = (String) session.getAttribute("userName");
			List<Task> taskList1 = taskService.createTaskQuery().taskAssignee(assginee).list();
			if (taskList1 != null && taskList1.size() > 0) {
				for (Task task : taskList1) {
					TaskModel taskModel = new TaskModel();
					// 获取部署名
					String processdefintionId = task.getProcessDefinitionId();
					ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					    .processDefinitionId(processdefintionId).singleResult();

					// 根据taskname和节点判断是否是第一个
					String taskName = task.getName();
					Iterator<FlowElement> iterator = this.findFlow(processdefintionId);
					String row0 = null;
					String eleName0 = null;
					String rowStart = null;
					// 保存上上一个节点的信息
					FlowElement flowElement00 = null;
					findFole: while (iterator.hasNext()) {
						FlowElement flowElement0 = iterator.next();
						// 注意usertask下一个节点是连线而不是task
						FlowElement flowElement = null;
						String eleName = null;
						// 得到流程启动节点的行号
						if (flowElement0.getClass().getSimpleName().equals("StartEvent")) {
							rowStart = flowElement0.getXmlRowNumber() + "";
							// 如果当前不是连线，则下一个是
						} else if (flowElement0 != null && !(flowElement0.getClass().getSimpleName().equals("SequenceFlow"))
						    && iterator.hasNext()) {
							do {
								iterator.next();
								if (iterator.hasNext()) {
									flowElement = iterator.next();
									eleName = flowElement.getName();
									// 下下一个节点
									if (taskName.equals(eleName)) {
										row0 = flowElement0.getXmlRowNumber() + "";
										eleName0 = flowElement0.getClass().getSimpleName();
										flowElement00 = flowElement0;
										break findFole;
									} else {
										flowElement0 = flowElement;
									}
								}
							} while (true);
						}
						// } else if (iterator.hasNext()) {
						// FlowElement flowElement01 = iterator.next();
						// System.out.println(flowElement01.getClass()
						// .getSimpleName());
						// flowElement00 = flowElement01;
						// if (flowElement01 != null
						// && flowElement01.getClass().getSimpleName()
						// .equals("SequenceFlow")) {
						// flowElement = iterator.next();
						// eleName = flowElement.getName();
						// }
						// }
					}
					// 此处需要修改,怎么判断是别人提交给我的？如果当前节点名是申请，那么当上一个节点类名是StartEvent时，证明是自己的申请
					// System.out.println();
					if (eleName0 != null && !("StartEvent".equals(eleName0))) {
						// 先查询出上一个任务（已完成）,根据流程实例id
						List<HistoricTaskInstance> hisTaskList = historyService.createHistoricTaskInstanceQuery()
						    .processDefinitionId(processdefintionId).finished().orderByHistoricTaskInstanceEndTime().desc().list();
						String formName = null;
						if (hisTaskList != null && hisTaskList.size() > 0) {
							HistoricTaskInstance historicTaskInstance = hisTaskList.get(0);
							formName = historicTaskInstance.getFormKey();
							String form = this.getTaskForm1(formName);
							taskModel.setLastForm(form);
						}
						// 当是别人提交过来的任务时，form就应该是任务相关的form，task里边保存的有formName
						// String formName = task.getFormKey();

						taskModel.setAssignee(task.getAssignee());
						taskModel.setCreateTime(task.getCreateTime());
						taskModel.setId(task.getId());
						taskModel.setName(task.getName());
						taskModel.setProcessInstanceId(task.getProcessInstanceId());
						taskModel.setProcessDefId(task.getProcessDefinitionId());
						taskModel.setFormKey(task.getFormKey());
						String deploymentId = processDefinition.getDeploymentId();
						Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
						String deploymentName = deployment.getName();
						taskModel.setDeploymentName(deploymentName);

						List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
						    .processInstanceId(task.getProcessInstanceId()).list();
						// 获取上一个节点填写的数据
						Map<String, String> formData = new HashMap<String, String>();
						Map<String, String> formData1 = new HashMap<String, String>();
						for (HistoricVariableInstance variableInstance : variables) {
							System.out.println(variableInstance);
							String varName = variableInstance.getVariableName();
							System.out.println(varName);
							System.out.println(variableInstance.getValue());
							if (varName.contains(row0)) {
								formData.put(varName, (String) variableInstance.getValue());
							}
							if (varName.contains(rowStart)) {
								formData1.put(varName, (String) variableInstance.getValue());
							}

						}
						// 获取流程启动时填写的数据，即申请数据

						// 获取流程启动的表单form
						String firstForm = this.getStartForm1(deploymentId);
						// 这里的代码提取出去成为getStartFormAndData
						// this.getStartFormAndData(rowStart, formData1,
						// firstForm);
						// console.log(form);
						StringBuffer firstFormAndData = setFormAndData(rowStart, formData1, firstForm);
						taskModel.setFirstFormAndData(firstFormAndData.toString());
						taskModel.setFormData(formData);
						taskList.add(taskModel);
					}

				}
			}
			map.put("isLogin", "yes");
			map.put("userName", (String) req.getSession().getAttribute("userName"));
			map.put("result", "success");
			map.put("data", taskList);
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	/**
	 * 获取开始节点页面和相关数据
	 * 
	 * @author：tuzongxun @Title: getStartFormAndData @param @param
	 *                   rowStart @param @param formData1 @param @param
	 *                   firstForm @param @return @return StringBuffer @date Apr
	 *                   11, 2016 3:58:37 PM @throws
	 */
	private StringBuffer setFormAndData(String rowStart, Map<String, String> formData1, String firstForm) {
		String[] p = firstForm.split("<p>");
		StringBuffer firstFormAndData = new StringBuffer();
		for (int i = 1; i < p.length; i++) {
			String pName = p[i].substring(0, p[i].indexOf("：") + 1);
			int index1 = p[i].indexOf("name=\"");

			if (index1 != -1) {
				String p0 = p[i].substring(index1, p[i].lastIndexOf(">"));
				int index2 = p0.indexOf('"');
				String keyName = p[i].substring(index1 + 6, index2 + index1 + 7) + rowStart;
				// System.out.println(keyName);
				// String value = null;
				Set<String> keySet = formData1.keySet();
				for (String key : keySet) {
					// System.out.println(key);
					String keyString = key + "";
					if (keyString.equals(keyName)) {
						String value1 = formData1.get(keyString);
						// System.out.println(value1.equals(""));
						// System.out.println(value1 != "");
						if (value1 != null && !value1.equals("")) {
							// firstFormAndData
							// .append(pName
							// +
							// "<input type='text' readonly='readonly'
							// style='background-color:#DEDCDC;margin-top:10px' value='"
							// + value1 + "'></br>");
							firstFormAndData
							    .append(pName + "<font  style='background-color:#DEDCDC;margin-top:10px'>" + value1 + "</font></br>");
						}
					}
				}
			}
		}
		return firstFormAndData;
	}

	/**
	 * @throws XMLStreamException
	 *           完成个人任务
	 * 
	 * @author：tuzongxun @Title: completeTask @param @return @return Object @date
	 *                   Mar 17, 2016 4:55:31 PM @throws
	 */
	@RequestMapping(value = "/completeTask.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object completeTask(HttpServletRequest req) throws XMLStreamException {
		// ///////////////////////////////////////
		Map<String, String[]> formMap = req.getParameterMap();
		String taskId = (String) formMap.get("taskId")[0];
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			if (taskId != null) {
				// 根据taskName和流程节点中的名字判断当前节点之后是否还有任务
				Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
				String taskName = task.getName();
				// System.out.println(taskName);
				// ////////////////////////////////////////
				Iterator<FlowElement> flows = this.findFlow(task.getProcessDefinitionId());
				String row0 = null;
				Map<String, Object> formProperties = new HashMap<String, Object>();
				while (flows.hasNext()) {
					FlowElement flowElement = flows.next();
					// System.out.println(flowElement.getName());
					// System.out.println(flowElement.getClass().getSimpleName());
					// 找到当前节点，查询出下下一个节点是否是结束节点，如果不是则需要设置下一个任务人，否则直接保存相关流程变量结束
					// 同时要查处当前任务的行数用来设置流程变量名称
					if (taskName.equals(flowElement.getName())) {
						// 设置行号
						row0 = flowElement.getXmlRowNumber() + "";
						FlowElement flowElement3 = flows.next();
						System.out.println("SequenceFlow".equals(flowElement3.getClass().getSimpleName()));
						if ("SequenceFlow".equals(flowElement3.getClass().getSimpleName())) {
							SequenceFlow seq = (SequenceFlow) flowElement3;
							System.out.println(seq.getConditionExpression() + "," + seq.getName());
						}
						FlowElement flowElement2 = flows.next();
						// 当前任务不是最后一个任务,流程没有结束,需要设置下一个处理人
						if (flowElement2 != null && !("EndEvent".equals(flowElement2.getClass().getSimpleName())
						    && !("SequenceFlow".equals(flowElement2.getClass().getSimpleName())))) {
							UserTask userTask = (UserTask) flowElement2;
							// 获取userTask中的
							String assignee = userTask.getAssignee();
							int index1 = assignee.indexOf("{");
							int index2 = assignee.indexOf("}");
							String person1 = (String) formMap.get("data_1")[0];
							formProperties.put(assignee.substring(index1 + 1, index2), person1);
						}
						break;
					}
				}
				// 从request中读取参数然后转换0
				Set<Entry<String, String[]>> entrySet = formMap.entrySet();
				for (Entry<String, String[]> entry : entrySet) {
					String key = entry.getKey() + row0;
					String value = entry.getValue()[0];
					if (!key.equals("taskId")) {
						formProperties.put(key, value);
					}
				}

				// // 1、查task
				// Task task = taskService.createTaskQuery().taskId(taskId)
				// .singleResult();
				// // 2、查variables
				// Map<String, Object> variables =
				// runtimeService.getVariables(task
				// .getProcessInstanceId());
				// Set<String> keysSet = variables.keySet();
				// Iterator<String> keySet = keysSet.iterator();
				// Map<String, Object> variables1 = new HashMap<String,
				// Object>();
				// String assignee = task.getAssignee();
				// // 判断之后是否还有任务
				// // ////////////////
				// while (keySet.hasNext()) {
				// String key = keySet.next();
				// if (key.equals("cause") || key.equals("content")
				// || key.equals("taskType")) {
				// continue;
				// } else if (!(assignee.equals(variables.get(key)))) {
				// // ///////////////////////////////////
				// // 3、查flowElement
				// Iterator<FlowElement> iterator = this.findFlow(task
				// .getProcessDefinitionId());
				// while (iterator.hasNext()) {
				// FlowElement flowElement = iterator.next();
				// String classNames = flowElement.getClass()
				// .getSimpleName();
				// if (classNames.equals("UserTask")) {
				// UserTask userTask = (UserTask) flowElement;
				// String assginee11 = userTask.getAssignee();
				// String assginee12 = assginee11.substring(
				// assginee11.indexOf("{") + 1,
				// assginee11.indexOf("}"));
				// String assignee13 = (String) variables
				// .get(assginee12);
				// if (assignee.equals(assignee13)) {
				// // 看下下一个节点是什么
				// iterator.next();
				// FlowElement flowElement2 = iterator.next();
				// String classNames1 = flowElement2.getClass()
				// .getSimpleName();
				// // 设置下一个任务人
				// if (!(classNames1.equals("EndEvent"))) {
				// UserTask userTask2 = (UserTask) flowElement2;
				// String assginee21 = userTask2.getAssignee();
				// String assginee22 = assginee21.substring(
				// assginee21.indexOf("{") + 1,
				// assginee21.indexOf("}"));
				// // String assignee23 = (String) variables
				// // .get(assginee22);
				// // String assignee23 = taskModel
				// // .getNextPerson();
				// // variables1.put(assginee22, assignee23);
				// }
				// }
				// }
				// }
				// // //////////////////////////////////
				// }
				// }
				// taskService.complete(taskId, variables1);
				//

				taskService.complete(taskId, formProperties);
				;
			}
		}
		return null;
	}

	/**
	 * 查询我的历史任务
	 * 
	 * @author：tuzongxun @Title: hisTask @param @param req @param @return @return
	 *                   Object @date Mar 18, 2016 9:12:07 AM @throws
	 */
	@RequestMapping(value = "/hisTask.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object hisTask(HttpServletRequest req) {
		// 整个流程结束，只要我参与了，就要能看整个流程的过程；
		// 或者，只要我完成了我的任务，就算历史，查询我之前所有节点的相关信息；
		// 无论如何，必定是我已经完成了的任务
		// 思路：首先还要获取开始节点的数据，
		// 1、找到任务相关的xml中的所有flow(需要流程定义id)，找到每个任务flow的rownumber，并拿到对应的taskName
		// 2、根据taskName和对应的流程实例id找到对应的task，根据task拿到formKey
		// 3、根据formKey查询出对应的页面
		// 4、根据rownumber和流程实例id查询出任务对应的数据
		// 5、把相对应的页面和数据组合成带有数据的页面
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isLogin = this.isLogin(req);
		if (isLogin) {
			HttpSession session = req.getSession();
			String assignee = (String) session.getAttribute("userName");
			List<HistoricTaskInstance> hisTaskList1 = historyService.createHistoricTaskInstanceQuery().taskAssignee(assignee)
			    .finished().list();
			List<HisTaskModel> hisTaskList = new ArrayList<HisTaskModel>();
			for (HistoricTaskInstance hisTask1 : hisTaskList1) {
				HisTaskModel hisModel = new HisTaskModel();
				String proDefId = hisTask1.getProcessDefinitionId();
				String proInsId = hisTask1.getProcessInstanceId();
				// 根据当前流程实例id查询出该流程下所有的任务
				String rowStart = "";
				String deployName = "";
				String taskRow = "";
				List<HistoricTaskInstance> hisTaskList11 = historyService.createHistoricTaskInstanceQuery()
				    .processInstanceId(proInsId).list();
				ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				    .processDefinitionId(proDefId).singleResult();
				String deploymentId = processDefinition.getDeploymentId();
				Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
				deployName = deployment.getName();
				List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
				    .processInstanceId(proInsId).list();
				// 根据流程实例id获取流程变量
				List<HistoricVariableInstance> hisList = historyService.createHistoricVariableInstanceQuery()
				    .processInstanceId(proInsId).list();
				// StringBuffer dataForm
				StringBuffer startForm_Data = new StringBuffer();

				try {
					Iterator<FlowElement> flows = this.findFlow(proDefId);
					for (HistoricTaskInstance hisTask : hisTaskList11) {
						// 获取任务名称
						String taskName = hisTask.getName();
						// 获取当前任务所在的节点
						while (flows.hasNext()) {
							FlowElement flowElement = flows.next();
							String flowName = flowElement.getName();
							if ("StartEvent".equals(flowElement.getClass().getSimpleName())) {
								rowStart = flowElement.getXmlRowNumber() + "";
								// 获取开始节点填写的数据
								Map<String, String> formData1 = new HashMap<String, String>();
								for (HistoricVariableInstance variableInstance : variables) {
									String varName = variableInstance.getVariableName();
									if (varName.contains(rowStart)) {
										formData1.put(varName, (String) variableInstance.getValue());
									}
								}
								// 获取deploymentId从而查询开始节点对应的form页面
								String firstForm = getStartForm1(deploymentId);
								StringBuffer startForm_Data2 = setFormAndData(rowStart, formData1, firstForm);
								startForm_Data.append(startForm_Data2);
							} else if ("UserTask".equals(flowElement.getClass().getSimpleName()) && taskName.equals(flowName)) {
								// System.out.println("找到当前节点");
								// 获得当前任务节点的行号
								taskRow = flowElement.getXmlRowNumber() + "";
								// System.out.println("当前节点行号：" + taskRow);
								break;
							}
						}

						// hisModel.setAllForm(StartForm_Data.toString());
						// 获取当前任务对应的formKey
						String formKey = hisTask.getFormKey();
						// 获取当前任务对应的form表单
						String taskForm = getTaskForm1(formKey);
						Map<String, String> formData2 = new HashMap<String, String>();
						// 根据流程变量和taskRow和对应的form组合页面
						for (HistoricVariableInstance hisVar : hisList) {
							String varName = hisVar.getVariableName();
							if (varName != null && varName.contains("data") && varName.contains(taskRow)) {
								String var = (String) hisVar.getValue();
								formData2.put(varName, var);
							}
						}
						StringBuffer dataForm1 = setFormAndData(taskRow, formData2, taskForm);
						if (dataForm1 != null) {
							startForm_Data.append("<hr style='height:2px;border:none;border-top:2px dotted #185598;'></hr>");
							startForm_Data.append(dataForm1);
						}
					}
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
				System.out.println("最终内容：" + startForm_Data.toString());
				hisModel.setAllForm(startForm_Data.toString());
				hisModel.setTaskType(deployName);
				hisModel.setAssignee(hisTask1.getAssignee());
				hisModel.setEndTime(hisTask1.getEndTime());
				hisModel.setId(hisTask1.getId());
				hisModel.setName(hisTask1.getName());
				hisModel.setProcessInstanceId(hisTask1.getProcessInstanceId());
				hisModel.setStartTime(hisTask1.getStartTime());
				hisTaskList.add(hisModel);
			}
			map.put("isLogin", "yes");
			map.put("userName", (String) req.getSession().getAttribute("userName"));
			map.put("result", "success");
			map.put("data", hisTaskList);
		} else {
			map.put("isLogin", "no");
		}
		return map;
	}

	@RequestMapping(value = "/getStartForm.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object getStartForm(@RequestBody String deploymentId) {
		Map<String, String> map = new HashMap<String, String>();
		String deString = null;
		deString = deploymentId.replaceAll("=", "");
		String form = this.getStartForm1(deString);
		map.put("form", form);
		return map;
	}

	public String getStartForm1(String deploymentId) {
		String deString = null;
		deString = deploymentId.replaceAll("=", "");
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deString).singleResult();
		String form = (String) formService.getRenderedStartForm(pd.getId());
		return form;
	}

	@RequestMapping(value = "/getTaskForm.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object getTaskForm(@RequestBody String taskFormKey) {
		Map<String, String> map = new HashMap<String, String>();
		// System.out.println(taskFormKey);
		taskFormKey = taskFormKey.replace("=", "");
		String form = myFormService.findFormByFormName(taskFormKey);
		// (String) formService.getRenderedTaskForm(taskFormKey);
		map.put("form", form);
		return map;
	}

	public String getTaskForm1(String taskFormKey) {
		Map<String, String> map = new HashMap<String, String>();
		taskFormKey = taskFormKey.replace("=", "");
		String form = myFormService.findFormByFormName(taskFormKey);
		return form;
	}

}
