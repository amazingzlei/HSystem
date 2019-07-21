package cn.xzit.controller;

import cn.xzit.common.CommonCode;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Employee;
import cn.xzit.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/userLogin")
public class UserLoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录成功之后跳转页面
     * @param employee 员工信息
     * @param request HttpServletRequest
     * @return 视图
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String userLogin(Employee employee,HttpServletRequest request){
        //将用户信息放入session中
        HttpSession session = request.getSession();
        employee = loginService.selectEmployeeByWId(employee);
        session.setAttribute("employee", employee);
        if(CommonCode.ADMIN.equals(employee.getType())){//如果0 表示管理员
            return "redirect:../jsp/manage/manage.jsp";
        }else if(CommonCode.DOCOR.equals(employee.getType())){
            return "redirect:../jsp/doctor/doctor.jsp";
        }else if(CommonCode.CHARGE.equals(employee.getType())){
            return "redirect:../jsp/charge/charge.jsp";
        }else if(CommonCode.DIRECTOR.equals(employee.getType())){
            return "redirect:../jsp/director/director.jsp";
        }else if(CommonCode.SUPERVISOR.equals(employee.getType())){
            return "redirect:../jsp/supervisor/supervisor.jsp";
        }else if(CommonCode.PURCHASE.equals(employee.getType())){
            return "redirect:../jsp/purshase/purchase.jsp";
        }else if(CommonCode.PHARMACY.equals(employee.getType())){
            return "redirect:../jsp/pharmacy/pharmacy.jsp";
        }else{
            return null;
        }
    }

    @RequestMapping(value = "/logout")
    public String userLogout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        request.getSession().invalidate();
        return "redirect:../jsp/login.jsp";
    }

    /**
     * 是否登录成功
     * @param employee 员工信息
     * @return json
     */
    @RequestMapping(value = "/isLoginSuccess",method = RequestMethod.POST)
    @ResponseBody
    public ResultVo checkedIsLoginSuccess(Employee employee){
        return loginService.checkedIsLoginSuccess(employee);
    }
}
