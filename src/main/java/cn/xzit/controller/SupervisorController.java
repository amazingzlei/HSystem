package cn.xzit.controller;

import cn.xzit.common.CommonCode;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.ResultVo;
import cn.xzit.service.DirectorService;
import cn.xzit.service.SupervisorService;
import cn.xzit.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "/supervisorInfo")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private DirectorService directorService;

    /**
     * 获取待审核的采购
     * @param page 分页
     * @return ResultVo
     */
    @RequestMapping(value = "getReqApplicatePur", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getReqApplicatePur(Page page){
        PageVo pageVo = supervisorService.getReqApplicatePur(page);
        if(pageVo != null){
            return ResultVoUtil.success(pageVo);
        }else{
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 审核通过
     * @param pid 采购ID
     * @return ResultVo
     */
    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo confirm(String pid, HttpServletRequest request){
        Integer res = supervisorService.confirm(pid, request);
        if(res == 1 ){
            String id = directorService.getPurchaseId();
            return ResultVoUtil.success(id);
        }else if(res == 0){
            return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
        }else{
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 驳回审核
     * @param pid 采购ID
     * @return ResultVo
     */
    @RequestMapping(value = "refuse", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo refuse(String pid, HttpServletRequest request){
        Integer res = supervisorService.refuse(pid, request);
        if(res == 1 ){
            return ResultVoUtil.success();
        }else if(res == 0){
            return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
        }else{
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 页面跳转
     * @param viewName 视图名称
     * @return 视图
     */
    @RequestMapping(value = {"getView/{name}/{data}","getView/{name}"})
    public String getView(@PathVariable("name") String viewName,@PathVariable(required = false,value = "data") String data, Map map){
        map.put("data",data );
        return "/jsp/supervisor/"+viewName;
    }
}
