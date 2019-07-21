package cn.xzit.interceptor;

import cn.xzit.entity.base.Employee;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object arg) throws Exception {
           Employee member=(Employee)request.getSession().getAttribute("employee");
        if(member==null){
            request.getRequestDispatcher("/jsp/login.jsp").forward(request,response);
            return false;
        };
        return true;
    }
}
