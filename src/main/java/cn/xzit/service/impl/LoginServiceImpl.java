package cn.xzit.service.impl;

import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Employee;
import cn.xzit.service.LoginService;
import cn.xzit.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 判断用户名和密码是否一致
     * @param employee 工作人员信息
     * @return 是否登录成功
     */
    @Override
    public ResultVo checkedIsLoginSuccess(Employee employee) {
//        System.out.println(employee);
        //判断员工是否存在
        String sql = "SELECT * FROM employee WHERE wid=? AND type=?";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        List<Employee> list = jdbcTemplate.query(sql, rowMapper,employee.getWid(),employee.getType());
        if(list.size()==0){
            return ResultVoUtil.error(404, "用户不存在!");
        }else{
            String sql2 = "SELECT password FROM employee WHERE wid=?";
            String password = jdbcTemplate.queryForObject(sql2, String.class,employee.getWid());
            if(password.equals(employee.getPassword())){
                return ResultVoUtil.success(null);
            }else{
                return ResultVoUtil.error(403, "密码错误!");
            }
        }
    }

    /**
     * 根据工号查询姓名
     * @param employee
     * @return 姓名
     */
    @Override
    public Employee selectEmployeeByWId(Employee employee) {
        String sql = "SELECT * FROM employee WHERE wid=?";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        employee = jdbcTemplate.queryForObject(sql, rowMapper,employee.getWid());
        return employee;
    }
}
