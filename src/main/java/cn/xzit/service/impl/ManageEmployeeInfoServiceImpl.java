package cn.xzit.service.impl;

import cn.xzit.common.CommonCode;
import cn.xzit.dao.ManageDao;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Depart;
import cn.xzit.entity.base.Employee;
import cn.xzit.entity.base.Type;
import cn.xzit.service.ManageEmployeeInfoService;
import cn.xzit.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ManageEmployeeInfoServiceImpl implements ManageEmployeeInfoService{

    private static final String DEFAULT_PASSWORD = "000000";

    @Autowired
    private ManageDao manageDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 根据工号查询员工信息
     * @param employee 员工信息存放员工的工号
     * @return 员工详细信息
     */
    @Override
    public Employee getEmployeeByWid(Employee employee) {
        String sql = "SELECT * FROM employee WHERE wid = ?";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        employee = jdbcTemplate.queryForObject(sql, rowMapper,employee.getWid());
        return employee;
    }

    /**
     * 更新员工信息
     * @param employee 员工信息
     * @return 是否更新成功
     */
    @Override
    public Integer updateEmployeeByWid(Employee employee) {
        String sql = "UPDATE employee SET name=:name,gender=:gender,age=:age,phone=:phone,password=:password,departId=:departId," +
                "type=:type,update_time=:updateTime WHERE wid=:wid";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(employee);
        Integer result = namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return result;
    }

    /**
     * 根据wid查询密码
     * @param employee 员工信息
     * @return 密码
     */
    @Override
    public String selectPasswordByWid(Employee employee) {
        String sql = "SELECT password FROM employee WHERE wid=?";
        String password = jdbcTemplate.queryForObject(sql, String.class,employee.getWid());
        return password;
    }

    /**
     * 更新密码
     * @param employee 员工信息
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    @Override
    public Integer updatePassword(Employee employee, String newPassword) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "UPDATE employee SET password=?,update_time=? WHERE wid=?";
        Integer result = jdbcTemplate.update(sql, newPassword,sdf.format(date), employee.getWid());
        return result;
    }

    /**
     * 根据wid删除员工
     * @param employee 员工信息
     * @return 是否删除成功
     */
    @Override
    public Integer deleteEmployeeByWid(Employee employee) {
        String sql = "DELETE FROM employee WHERE wid=?";
        Integer result = jdbcTemplate.update(sql,employee.getWid());
        return result;
    }

    /**
     * 是否插入成功
     * @param employee 员工信息
     * @return 是否插入成功
     */
    @Override
    public Integer isInsertSuccess(Employee employee) {
        if(CommonCode.ADMIN.equals(employee.getType())){
            // 只能有一个管理员
            return -1;
        }else if(CommonCode.SUPERVISOR.equals(employee.getType())){// 医院主管
            // 判断是否有医院主管
            String wid = manageDao.isHasSupervisor(employee.getType());
            if(StringUtils.isEmpty(wid)){
                return insertEmployee(employee);
            }else{
                return -2;
            }
        }else if(CommonCode.PURCHASE.equals(employee.getType())){// 采购人员
            String wid = manageDao.isHasPurchase(employee.getType());
            if(StringUtils.isEmpty(wid)){
                return insertEmployee(employee);
            }else{
                return -3;
            }
        }else if(CommonCode.DIRECTOR.equals(employee.getType())){// 科室主任
            String wid = manageDao.isHasDirector(employee.getDepartId());
            if(StringUtils.isEmpty(wid)){
                return insertEmployee(employee);
            }else{
                return -4;
            }
        }else {
            return insertEmployee(employee);
        }
    }

    private Integer insertEmployee(Employee employee){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //为员工信息赋值
        employee.setCreateTime(sdf.format(date));
        employee.setUpdateTime(sdf.format(date));
        employee.setPassword(DEFAULT_PASSWORD);//设置默认密码
        String sql = "INSERT INTO employee values(:wid,:name,:gender,:age,:phone,:type,:password,:departId," +
                ":createTime,:updateTime)";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(employee);
        Integer result = namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return result;
    }

    /**
     * 获取员工分页数据
     * @param page 分页参数
     * @return ResultVo
     */
    @Override
    public ResultVo getPageEmployee(Page page) {
        try {
//            StringBuffer sql = new StringBuffer("SELECT wid,e.name,gender,age,phone,password,d.name AS departId,create_time,update_time," +
//                    "if(type=0,'管理员',(if(type=1,'医生',if(type=2,'医院主管'," +
//                    "if(type=3,'采购人员',if(type=4,'药房人员','身份不明')))))) as type FROM employee e LEFT JOIN depart d on e.departId=d.id WHERE wid!=10001 ORDER BY wid DESC ");
            StringBuffer sql = new StringBuffer("SELECT wid,e.name,gender,age,phone,password,d.name AS departId,create_time," +
                    "update_time,et.name AS type FROM employee e LEFT JOIN depart d on e.departId=d.id " +
                    "LEFT JOIN etype et on e.type = et.typeId WHERE wid!=10001 ORDER BY wid DESC ");
            sql.append("limit ");
            sql.append((page.getPage()-1)*page.getPageSize());
            sql.append(",");
            sql.append(page.getPageSize());
            RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
            List<Employee> employeeList = jdbcTemplate.query(sql.toString(),rowMapper );

            //获取总条数
            String sql2 = "SELECT count(1) FROM employee where wid != 10001";
            Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);
            int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
            PageVo pageVo = new PageVo();
            pageVo.setSize(size);
            pageVo.setData(employeeList);
            return ResultVoUtil.success(pageVo);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 根据员工id进行模糊查询
     * @param wid 工号
     * @param page 当前页
     * @param pageSize 页容量
     * @return ResultVo
     */
    @Override
    public ResultVo searchEmployeeById(String wid, Integer page, Integer pageSize) {
        try{
//            StringBuffer sql = new StringBuffer("SELECT wid,e.name,gender,age,phone,password,d.name AS departId,create_time,update_time," +
//                    "if(type=0,'管理员',(if(type=1,'医生',if(type=2,'医院主管'," +
//                    "if(type=3,'采购人员',if(type=4,'药房人员','身份不明')))))) as type FROM employee e LEFT JOIN depart d on e.departId=d.id WHERE wid LIKE '");
            StringBuffer sql = new StringBuffer("SELECT wid,e.name,gender,age,phone,password,d.name AS departId," +
                    "create_time,update_time,et.name AS type FROM employee e LEFT JOIN depart d on e.departId=d.id " +
                    "LEFT JOIN etype et on e.type = et.typeId WHERE wid LIKE '");
            sql.append(wid);
            sql.append("%' AND wid!=10001 ORDER BY wid DESC ");
            sql.append("limit ");
            sql.append((page-1)*pageSize);
            sql.append(",");
            sql.append(pageSize);
            System.out.println(sql.toString());
            RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
            List<Employee> employeeList = jdbcTemplate.query(sql.toString(),rowMapper );

            //获取总条数
            String sql2 = "SELECT count(1) FROM employee WHERE wid LIKE '"+wid+"%'";
            Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);
            int size = (count%pageSize)==0?count/pageSize:count/pageSize+1;
            System.out.println((count/pageSize)==0);
            PageVo pageVo = new PageVo();
            pageVo.setSize(size);
            pageVo.setData(employeeList);
            return ResultVoUtil.success(pageVo);
        }catch (Exception e){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 添加科室
     * @param depart 科室信息
     * @return ResultVo
     */
    @Override
    public ResultVo addDepart(Depart depart) {
        try{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            depart.setCreateTime(timestamp.toString());
            depart.setUpdateTime(timestamp.toString());
            //首先判断科室名是否存在
            String sql = "SELECT name FROM depart WHERE name=?";
            RowMapper rowMapper = new BeanPropertyRowMapper(Depart.class);
            List<Depart> departList = jdbcTemplate.query(sql, rowMapper,depart.getName());
            if(departList.size() == 0){
                String addSql = "INSERT INTO depart(id,name,createTime,updateTime) VALUES(:id,:name,:createTime,:updateTime)";
                SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(depart);
                int result = namedParameterJdbcTemplate.update(addSql, sqlParameterSource);
                if(result == 1){
                    return ResultVoUtil.success();
                }else{
                    return  ResultVoUtil.error(403, CommonCode.ADD_FAIL);
                }
            }else{
                return ResultVoUtil.error(500, CommonCode.HASDEPART);
            }
        }catch (Exception e){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 获取科室分页信息
     * @param page 分页参数
     * @return ResultVo
     */
    @Override
    public ResultVo getPageDepart(Page page) {
        try {
            String sql = "SELECT * FROM depart ORDER BY id LIMIT ?,?";
            RowMapper<Depart> rowMapper = new BeanPropertyRowMapper<>(Depart.class);
            List<Depart> employeeList = jdbcTemplate.query(sql.toString(),rowMapper,
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize() );

            //获取总条数
            String sql2 = "SELECT count(1) FROM depart ";
            Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);
            int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
            PageVo pageVo = new PageVo();
            pageVo.setSize(size);
            pageVo.setData(employeeList);
            return ResultVoUtil.success(pageVo);
        }catch (Exception e){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 根据部门号查询部门信息
     * @param depart 科室信息
     * @return ResultVo
     */
    @Override
    public ResultVo getDepartById(Depart depart) {
        try {
            String sql = "SELECT * FROM depart WHERE id=?";
            RowMapper<Depart> rowMapper = new BeanPropertyRowMapper<>(Depart.class);
            depart = jdbcTemplate.queryForObject(sql, rowMapper,depart.getId());
            return ResultVoUtil.success(depart);
        }catch (Exception e){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 更新科室信息
     * @param depart 科室信息
     * @return ResultVo
     */
    @Override
    public ResultVo updateDepart(Depart depart) {
        try {

            //首先判断科室名是否存在
            String isExist = "SELECT name FROM depart WHERE name=?";
            RowMapper rowMapper = new BeanPropertyRowMapper(Depart.class);
            List<Depart> departList = jdbcTemplate.query(isExist, rowMapper,depart.getName());

            if(CollectionUtils.isEmpty(departList)){
                String sql = "UPDATE depart SET name = ? WHERE id=?";
                int result = jdbcTemplate.update(sql, depart.getName(),depart.getId());
                if(result != 1){
                    return ResultVoUtil.error(404, CommonCode.UPDATE_FAIL);
                }else {
                    return ResultVoUtil.success();
                }
            }else{
                return ResultVoUtil.error(501, CommonCode.HASDEPART);
            }


        }catch (Exception e){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 删除科室信息
     * @param depart 科室信息
     * @return ResultVo
     */
    @Override
    public ResultVo deleteDepart(Depart depart) {
        try {

            // 首先判断该科室是否有人
            List<Employee> employeeList = manageDao.getEmployeeByDepartId(depart.getId());
            if(CollectionUtils.isEmpty(employeeList)){
                String sql = "DELETE FROM  depart WHERE id=?";
                int result = jdbcTemplate.update(sql,depart.getId());
                if(result != 1){
                    return ResultVoUtil.error(404, CommonCode.DELETE_FAIL);
                }else {
                    return ResultVoUtil.success();
                }
            }else{
                return ResultVoUtil.error(501   ,CommonCode.HASEMPLOYEE);
            }

        }catch (Exception e){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 查询所有部门信息
     * @return ResultVo
     */
    @Override
    public ResultVo getAllDepart() {
        try {
            String sql = "SELECT * FROM depart";
            RowMapper<Depart> rowMapper = new BeanPropertyRowMapper<>(Depart.class);
            List<Depart> departList = jdbcTemplate.query(sql,rowMapper );
            return ResultVoUtil.success(departList);
        }catch (Exception e){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 获取所有员工类型
     * @return ResultVo
     */
    @Override
    public ResultVo getAllType() {
        try {
            String sql = "SELECT * FROM etype WHERE typeId!=0";
            RowMapper<Type> rowMapper = new BeanPropertyRowMapper<>(Type.class);
            List<Type> departList = jdbcTemplate.query(sql,rowMapper );
            return ResultVoUtil.success(departList);
        }catch (Exception e){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

}
