package controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UserModel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	/**
	 * 用户登陆
	 * 
	 * @author：tuzongxun
	 * @Title: login
	 * @param @return
	 * @return Object
	 * @date Mar 17, 2016 5:17:55 PM
	 * @throws
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object login(@RequestBody UserModel user, HttpServletRequest req,
			HttpServletResponse res) {
		Map<String, String> map = new HashMap<String, String>();
		if (user != null
				&& (user.getUserName().equals("zhangsan")
						|| user.getUserName().equals("lisi")
						|| user.getUserName().equals("wangwu")
						|| user.getUserName().equals("zhaoliu") || user
						.getUserName().equals("tianqi"))) {
			HttpSession session = req.getSession();
			session.setAttribute("userName", user.getUserName());
			map.put("result", "success");
			map.put("userName", user.getUserName());
		} else {
			map.put("result", "fail");
		}
		return map;
	}

	/**
	 * 退出登陆
	 * 
	 * @author：tuzongxun
	 * @Title: loginOut
	 * @param @param req
	 * @param @return
	 * @return Object
	 * @date Mar 18, 2016 9:46:13 AM
	 * @throws
	 */
	@RequestMapping(value = "/loginOut.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object loginOut(HttpServletRequest req) {
		Map<String, String> map = new HashMap<String, String>();
		req.getSession().removeAttribute("userName");
		return map;
	}
}
