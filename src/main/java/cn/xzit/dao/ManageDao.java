package cn.xzit.dao;

import cn.xzit.entity.base.Employee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ManageDao {

    String isHasSupervisor(String type);

    String isHasPurchase(String type);

    String isHasDirector(String type);

    List<Employee> getEmployeeByDepartId(Integer id);
}
