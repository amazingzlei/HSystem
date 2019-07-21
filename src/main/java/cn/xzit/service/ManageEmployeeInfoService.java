package cn.xzit.service;

import cn.xzit.entity.Page;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Depart;
import cn.xzit.entity.base.Employee;

public interface ManageEmployeeInfoService {

    Employee getEmployeeByWid(Employee employee);

    Integer updateEmployeeByWid(Employee employee);

    String selectPasswordByWid(Employee employee);

    Integer updatePassword(Employee employee,String newPassword);

    Integer deleteEmployeeByWid(Employee employee);

    Integer isInsertSuccess(Employee employee);

    ResultVo getPageEmployee(Page page);

    ResultVo searchEmployeeById(String wid,Integer page,Integer pageSize);

    ResultVo addDepart(Depart depart);

    ResultVo getPageDepart(Page page);

    ResultVo getDepartById(Depart depart);

    ResultVo updateDepart(Depart depart);

    ResultVo deleteDepart(Depart depart);

    ResultVo getAllDepart();

    ResultVo getAllType();
}
