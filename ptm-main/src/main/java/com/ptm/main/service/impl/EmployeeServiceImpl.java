package com.ptm.main.service.impl;

import static com.ptm.main.common.constant.ParamConstants.STATE_ID;
import static com.ptm.main.common.constant.StringConstants.EMPLOYEE_SUB_MENU_SAVE;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ptm.common.vo.EmployeeVO;
import com.ptm.common.vo.LoginVO;
import com.ptm.main.common.constant.PropertiesConfig;
import com.ptm.main.common.pojo.SearchParams;
import com.ptm.main.common.util.AppMethodUtils;
import com.ptm.main.dao.EmployeeDAO;
import com.ptm.main.dao.LoginDAO;
import com.ptm.main.entity.Designation;
import com.ptm.main.entity.Employee;
import com.ptm.main.entity.IndiaState;
import com.ptm.main.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDAO employeeDAO;
	@Autowired
	private LoginDAO loginRoleDAO;
	@Autowired
	private PropertiesConfig ptmConfigProperties;

	@Override
	public void saveOrUpdateEmployee(EmployeeVO employeeVO, SearchParams searchParams) {
		Employee employee = BeanUtils.instantiate(Employee.class);
		BeanUtils.copyProperties(employeeVO, employee);
		LocalDate dateOfBirth = AppMethodUtils.stringToDate(employeeVO.getDateOfBirth(), null);
		employee.setDateOfBirth(dateOfBirth);
		// Fetching designation by role n department
		SearchParams designationSearch = BeanUtils.instantiate(SearchParams.class);
		designationSearch.setIntNum1(employeeVO.getRole());
		designationSearch.setIntNum2(employeeVO.getDepartment());
		Designation designation = loginRoleDAO.getDesignationIdByRoleNDepttId(designationSearch);
		employee.setDesignation(designation);
		// Fteching InddiaState entity
		SearchParams indiaStateSearch = BeanUtils.instantiate(SearchParams.class);
		indiaStateSearch.getRequestMap().put(STATE_ID, employeeVO.getState());
		IndiaState indiaState = employeeDAO.getIndiaState(indiaStateSearch);
		employee.setIndiaState(indiaState);
		if (EMPLOYEE_SUB_MENU_SAVE.equals(employeeVO.getAction())) {
			employee.setEnabled(Boolean.parseBoolean(ptmConfigProperties.getEmpDefaultEnabledStatus()));
			employee.setPassword(ptmConfigProperties.getDefaultPassword());
		}
		employeeDAO.saveOrUpdateEmployee(employee);
	}

	@Override
	public EmployeeVO getEmployeeDetailsById(SearchParams searchParams) {
		EmployeeVO employeeVO = employeeDAO.getEmployeeDetailsById(searchParams);
		if (employeeVO == null) {
			return null;
		}
		return employeeVO;
	}

	@Override
	public int deleteEmployeePersonal(SearchParams searchParams) {
		return employeeDAO.deleteEmployee(searchParams);
	}

	@Override
	public Map<Integer, String> getIndiaStateList() {
		Map<Integer, String> indiaStateMap = employeeDAO.getIndiaStateList();
		return indiaStateMap;
	}

	@Override
	public int updatePassword(LoginVO loginVO) {
		return employeeDAO.updatePassword(loginVO);
	}

}
