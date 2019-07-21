package cn.xzit.controller;

import cn.xzit.common.CommonCode;
import cn.xzit.entity.Page;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Depart;
import cn.xzit.entity.base.Employee;
import cn.xzit.service.ManageEmployeeInfoService;
import cn.xzit.utils.ResultVoUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 管理员界面中的操作
 */
@Controller
@RequestMapping("/manageEmployeeInfo")
public class ManageEmployeeController {

    @Autowired
    private ManageEmployeeInfoService manageEmployeeInfoService;

    /**
     * 根据工号获取员工信息，编辑员工信息时调用
     * @param employee 员工信息存放工号
     * @return json数据
     */
    @RequestMapping("/getEmployeeByWid")
    @ResponseBody
    public String getEmployeeByWid(Employee employee){
        JSONObject jsonObject = new JSONObject();
        employee = manageEmployeeInfoService.getEmployeeByWid(employee);
        jsonObject.put("employee", employee);
        return jsonObject.toString();
    }

    /**
     * 更新员工信息
     * @param employee 员工信息
     * @return json数据
     */
    @RequestMapping(value = "/updateEmployee",method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public String updateEmployee(Employee employee){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        employee.setUpdateTime(sdf.format(date));
        Integer result = manageEmployeeInfoService.updateEmployeeByWid(employee);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject.toString();
    }

    /**
     * 更新密码
     * @param employee 员工信息
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return json
     */
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public String updatePassword(Employee employee,String oldPassword,String newPassword){
        JSONObject jsonObject = new JSONObject();
        // 判断原始密码是否正确
        String password = manageEmployeeInfoService.selectPasswordByWid(employee);
        if(oldPassword.equals(password)){
            Integer result = manageEmployeeInfoService.updatePassword(employee, newPassword);
            jsonObject.put("result",result);
            return jsonObject.toString();
        }else{
            jsonObject.put("result", -1);
            return jsonObject.toString();
        }
    }

    /**
     * 删除员工信息
     * @param employee 员工信息，存有员工的工号
     * @return json
     */
    @RequestMapping(value = "/deleteEmployee",method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public String deleteEmployee(Employee employee){
        Integer result = manageEmployeeInfoService.deleteEmployeeByWid(employee);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject.toString();
    }

    /**
     * 展示所有员工界面
     * @return 视图
     */
    @RequestMapping("/listAllEmployee")
    public String listAllEmployee(){
        return "redirect:../jsp/manage/manage.jsp";
    }

    /**
     * 展示添加员工界面
     * @return json
     */
    @RequestMapping("/insertEmployee")
    public String insertEmployee(){
        return "redirect:../jsp/manage/insert.jsp";
    }

    /**
     * 展示科室管理界面
     * @return json
     */
    @RequestMapping("/manageDepart")
    public String manageDepart(){
        return "redirect:../jsp/manage/depart.jsp";
    }

    /**
     * 展示科室管理界面
     * @return json
     */
    @RequestMapping("/insertDepart")
    public String insertDepart(){
        return "redirect:../jsp/manage/insertdepart.jsp";
    }

    /**
     * 插入员工信息
     * @param employee 员工信息
     * @return json
     */
    @RequestMapping(value = "/isInsertSuccess",method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResultVo isInsertSuccess(Employee employee){
        Integer result = manageEmployeeInfoService.isInsertSuccess(employee);
        if(result == -1){
            return ResultVoUtil.error(501, CommonCode.ONLY_ADMIN);
        }else if(result == -2){
            return ResultVoUtil.error(501, CommonCode.ONLY_SUPERVISOR);
        } else if (result == -3) {
            return ResultVoUtil.error(501, CommonCode.ONLY_PURCHASE);
        }else if(result == -4){
            return ResultVoUtil.error(501, CommonCode.ONLY_DIRECTOR);
        }else if(result == 0){
            return ResultVoUtil.error(501, CommonCode.ADD_FAIL);
        }else {
            return ResultVoUtil.success();
        }
    }

    /**
     * 分页查询员工数据
     * @param page 分页
     * @return 结果信息
     */
    @RequestMapping(value = "/getPageEmployee",method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResultVo getPageEmployee(Page page){
        return manageEmployeeInfoService.getPageEmployee(page);
    }

    /**
     * 根据工号查询员工信息
     * @param wid 工号
     * @param page 当前页
     * @param pageSize 页容量
     * @return ResultVo
     */
    @RequestMapping(value = "/searchEmployeeById")
    @ResponseBody
    public ResultVo searchEmployeeById(@RequestParam("wid") String wid, @RequestParam("page") Integer page,
                                       @RequestParam("pageSize") Integer pageSize){
        return manageEmployeeInfoService.searchEmployeeById(wid, page, pageSize);
    }

    /**
     * 添加部门信息
     * @param depart 部门
     * @return ResultVo
     */
    @RequestMapping(value = "/addDepart")
    @ResponseBody
    public ResultVo addDepart(Depart depart){
        return manageEmployeeInfoService.addDepart(depart);
    }

    /**
     * 获取科室分页信息
     * @param page 分页参数
     * @return ResultVo
     */
    @RequestMapping(value = "/getPageDepart",method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResultVo getPageDepart(Page page){
        return manageEmployeeInfoService.getPageDepart(page);
    }

    /**
     * 通过科室id获取科室信息
     * @param depart 科室id
     * @return ResultVo
     */
    @RequestMapping(value = "/getDepartById")
    @ResponseBody
    public ResultVo getDepartById(Depart depart){
        return manageEmployeeInfoService.getDepartById(depart);
    }

    /**
     * 更新科室信息
     * @param depart 科室信息
     * @return ResultVo
     */
    @RequestMapping(value = "/updateDepart")
    @ResponseBody
    public ResultVo updateDepart(Depart depart){
        return manageEmployeeInfoService.updateDepart(depart);
    }

    /**
     * 删除科室信息
     * @param depart 科室信息
     * @return ResultVo
     */
    @RequestMapping(value = "/deleteDepart")
    @ResponseBody
    public ResultVo deleteDepart(Depart depart){
        return manageEmployeeInfoService.deleteDepart(depart);
    }

    /**
     * 获取所有的部门
     * @return ResultVo
     */
    @RequestMapping(value = "/getAllDepart")
    @ResponseBody
    public ResultVo getAllDepart(){
        return manageEmployeeInfoService.getAllDepart();
    }

    /**
     * 获取所有人员类型
     * @return ResultVo
     */
    @RequestMapping(value = "/getAllType")
    @ResponseBody
    public ResultVo getAllType(){
        return manageEmployeeInfoService.getAllType();
    }
}
