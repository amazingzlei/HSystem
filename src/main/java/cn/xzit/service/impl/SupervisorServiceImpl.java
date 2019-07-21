package cn.xzit.service.impl;

import cn.xzit.dao.SupervisorDao;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.base.Employee;
import cn.xzit.entity.base.Purchase;
import cn.xzit.service.SupervisorService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class SupervisorServiceImpl implements SupervisorService{

    @Autowired
    private SupervisorDao supervisorDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private  static Logger log = LoggerFactory.getLogger(SupervisorServiceImpl.class);

    /**
     * 获取待审核的采购订单
     * @param page 分页
     * @return PageVo
     */
    @Override
    public PageVo getReqApplicatePur(Page page) {
        List<Purchase> purchases =  supervisorDao.getReqApplicatePur(
                (page.getPage()-1)*page.getPageSize(),page.getPageSize());
        if(!CollectionUtils.isEmpty(purchases)){
            Integer count = supervisorDao.getReqApplicatePurCount();
            int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
            PageVo pageVo = new PageVo();
            pageVo.setSize(size);
            pageVo.setData(purchases);
            return pageVo;
        }else {
            return null;
        }
    }

    /**
     * 审核通过
     * @param pid 采购单
     * @param request HttpServletRequest请求
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer confirm(String pid, HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");
            String sql = "UPDATE purchase SET isAccess=1,status =1,accessor=? WHERE pid=?";
            return jdbcTemplate.update(sql, employee.getWid(), pid);
        }catch (Exception e){
            log.error("SupervisorServiceImpl -> confirm异常!");
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 采购驳回
     * @param pid 采购ID
     * @param request HttpServletRequest请求
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer refuse(String pid, HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");
            String sql = "UPDATE purchase SET isAccess=1 ,status = 2 ,accessor = ? WHERE pid=?";
            return jdbcTemplate.update(sql, employee.getWid(), pid);
        }catch (Exception e){
            log.error("SupervisorServiceImpl -> confirm异常!");
            e.printStackTrace();
            return -2;
        }
    }
}
