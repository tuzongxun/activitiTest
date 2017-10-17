package serviceImp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import services.MyFormService;

@Service("FormService")
public class FormServiceImp implements MyFormService {

	/**
	 * 新增表单
	 * 
	 * @author：tuzongxun
	 * @Title: addForm
	 * @Description: TODO
	 * @param @param formType
	 * @param @param string
	 * @param @return
	 * @date Mar 28, 2016 4:30:18 PM
	 * @throws
	 */
	public Object addForm(String formType, String string) {
		System.out.println(string);
		try {
			Connection connection = this.getDb();
			PreparedStatement ps = connection
					.prepareStatement("insert into  formtest(formId,formType,form) values(?,?,?)");
			String formId = new Date().getTime() + "";
			ps.setString(1, formId);
			ps.setString(2, formType);
			ps.setString(3, string);
			ps.executeUpdate();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}

	public Connection getDb() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/testtu", "root", "123456");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 查询表单列表
	 * 
	 * @author：tuzongxun
	 * @Title: findForms
	 * @Description: TODO
	 * @param @return
	 * @date Mar 28, 2016 4:29:58 PM
	 * @throws
	 */
	public List<Map<String, String>> findForms() {
		Connection connection = this.getDb();
		Statement statement;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select * from formtest");
			while (resultSet.next()) {
				Map<String, String> map = new HashMap<String, String>();
				String formString = resultSet.getString(2);
				// resultSet.getString(2);
				System.out.println("formString:" + formString);
				map.put("formId", resultSet.getString(1));
				map.put("formType", resultSet.getString(2));
				map.put("form", resultSet.getString(3));
				list.add(map);
			}
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 删除表单
	 * 
	 * @author：tuzongxun
	 * @Title: addForm
	 * @Description: TODO
	 * @param @param formType
	 * @param @param string
	 * @param @return
	 * @date Mar 28, 2016 4:30:18 PM
	 * @throws
	 */
	public void deleteForm(String formId) {
		try {
			Connection connection = this.getDb();
			PreparedStatement ps = connection
					.prepareStatement("delete from  formtest where formId=?");
			ps.setString(1, formId);
			ps.executeUpdate();

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Override
	public String findFormByFormName(String formName) {
		String formString = null;
		Connection connection = this.getDb();
		Statement statement;
		try {
			statement = connection.createStatement();
			PreparedStatement ps = connection
					.prepareStatement("select * from formtest where formType=?");
			ps.setString(1, formName);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				formString = resultSet.getString(3);
				// resultSet.getString(2);
				// System.out.println("formString:" + formString);
			}
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formString;
	}

}
