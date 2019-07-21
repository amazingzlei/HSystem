package cn.xzit.service;

import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;

import javax.servlet.http.HttpServletRequest;

public interface SupervisorService {

    PageVo getReqApplicatePur(Page page);

    Integer confirm(String pid, HttpServletRequest request);

    Integer refuse(String pid, HttpServletRequest request);
}
