package cn.xzit.filter;


import cn.xzit.entity.base.Employee;
import cn.xzit.utils.FilterUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//实现 Filter 类
public class JspFilter implements Filter {
//    private static final String ADMIN = "0";//管理员
//    private static final String DOCTOR = "1";//医生
//    private static final String SUPERVISOR = "2";//医院主管
//    private static final String PURSHASE = "3";//采购
//    private static final String PHARMACY = "4";//药房
//    private static final String DIRECTOR = "5";//科室主任
//    private static final String CHARGE = "6";//收费人员

    public void  init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) arg0;
        HttpServletResponse response=(HttpServletResponse) arg1;
        HttpSession session=request.getSession();
        String path=request.getRequestURI();
        Employee member=(Employee)session.getAttribute("employee");
        if(path.indexOf("/login.jsp")>-1){//登录页面不过滤
            chain.doFilter(request,response);//递交给下一个过滤器
            return;
        }
        if(member!=null){//已经登录
//            if(ADMIN.equals(member.getType())&&path.indexOf("/manage/")>-1){//管理员只能访问管理员界面
//                chain.doFilter(request,response);//递交给下一个过滤器
//                return;
//            }else if (DOCTOR.equals(member.getType())&&path.indexOf("/doctor/")>-1){
//                chain.doFilter(request,response);//递交给下一个过滤器
//                return;
//            }else if(SUPERVISOR.equals(member.getType())&&path.indexOf("/supervisor/")>-1){
//                chain.doFilter(request,response);//递交给下一个过滤器
//                return;
//            }else if(PURSHASE.equals(member.getType())&&path.indexOf("/purshase/")>-1){
//                chain.doFilter(request,response);//递交给下一个过滤器
//                return;
//            }else if(PHARMACY.equals(member.getType())&&path.indexOf("/pharmacy/")>-1){
//                chain.doFilter(request,response);//递交给下一个过滤器
//                return;
//            }else if(DIRECTOR.equals(member.getType())&&path.indexOf("/director/")>-1){
//                chain.doFilter(request,response);//递交给下一个过滤器
//                return;
//            }else if(DIRECTOR.equals(member.getType())&&path.indexOf("/charge/")>-1){
//                chain.doFilter(request,response);//递交给下一个过滤器
//                return;
//            }else{
//                response.sendRedirect("/hsystem/jsp/login.jsp");
//            }
            if(path.indexOf(FilterUtil.getUrl(member.getType()))>-1){
                chain.doFilter(request,response);//递交给下一个过滤器
                return;
            }else{
                response.sendRedirect("/hsystem/jsp/login.jsp");
            }
        }else{
            response.sendRedirect("/hsystem/jsp/login.jsp");
        }
    }

    public void destroy( ){
        /* 在 Filter 实例被 Web 容器从服务移除之前调用 */
    }
}