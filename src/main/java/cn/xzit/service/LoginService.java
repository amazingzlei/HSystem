package cn.xzit.service;


import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Employee;


public interface LoginService {

    ResultVo checkedIsLoginSuccess(Employee employee);

    Employee selectEmployeeByWId(Employee employee);
}
