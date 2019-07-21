package cn.xzit.controller;

import cn.xzit.common.CommonCode;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Count;
import cn.xzit.entity.base.Medicinal;
import cn.xzit.entity.base.PurchaseInfo;
import cn.xzit.entity.vo.PersonIdVo;
import cn.xzit.service.DirectorService;
import cn.xzit.utils.ResultVoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "directorInfo")
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    private  static Logger log = LoggerFactory.getLogger(DirectorController.class);

    /**
     * 查询即将过期的药品
     * @param page 分页参数
     * @return ResultVo
     */
    @RequestMapping(value = "getExpireMed", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getExpireMed(Page page){
        try{
            PageVo pageVo = directorService.getExpireMed(page);
            if(pageVo != null){
                if(null != pageVo.getData()){
                    return ResultVoUtil.success(pageVo);
                }else{
                    return ResultVoUtil.success();
                }
            }else {
                return ResultVoUtil.error(404, CommonCode.ERROR);
            }
        }catch (Exception e){
            log.error("DirectorController - > getExpireMed异常");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 查询库存不足
     * @param page 分页参数
     * @return ResultVo
     */
    @RequestMapping(value = "getBlowCount", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getBlowCount(Page page){
        try{
            PageVo pageVo = directorService.getBlowCount(page);
            if(pageVo != null){
                if(null != pageVo.getData()){
                    return ResultVoUtil.success(pageVo);
                }else{
                    return ResultVoUtil.success();
                }
            }else {
                return ResultVoUtil.error(404, CommonCode.ERROR);
            }
        }catch (Exception e){
            log.error("DirectorController - > getExpireMed异常");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 添加采购明细表
     * @param medName 药名
     * @param medNum 数量
     * @return ResultVo
     */
    @RequestMapping(value = "addPurchase", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo addPurchase(String medName, String medNum , HttpServletRequest request){
        try {
            Integer res = directorService.addPurchase(medName, medNum, request);
            if(res == 1){
                return ResultVoUtil.success();
            }else{
                return ResultVoUtil.error(500, CommonCode.ADD_FAIL);
            }

        }catch (Exception e){
            log.error("DirectorController - > addPurchase异常");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 判断采购表是否有
     * @param medName 药名
     * @return ResultVo
     */
    @RequestMapping(value = "isPurchaseHas", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo isPurchaseHas(String medName){
        try {
            Integer res = directorService.isPurchaseHas(medName);
            if(res==1){
                return ResultVoUtil.success();
            }else {
                return ResultVoUtil.error(500, CommonCode.ADD_FAIL);
            }
        }catch (Exception e){
            log.error("DirectorController - > isPurchaseHas异常");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 通过员工id获取全部申请
     * @param page 分页参数
     * @param request HttpServletRequest请求
     * @return ResultVo
     */
    @RequestMapping(value = "getAllApplication", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getAllApplication(Page page,HttpServletRequest request){
        try {
            PageVo pageVo = directorService.getAllApplication(page, request);
            if(null != pageVo){
                return ResultVoUtil.success(pageVo);
            }else {
                return ResultVoUtil.error(501, CommonCode.DO_NOT_HAVE_SUCH_INFO);
            }
        }catch (Exception e){
            log.error("DirectorController - > getAllApplication异常");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 通过id获取全部待申请的药品
     * @param request HttpServletRequest请求
     * @return ResultVo
     */
    @RequestMapping(value = "getAllApplicating", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getAllApplicating(HttpServletRequest request){
        try {
            List<PurchaseInfo> purchaseInfos = directorService.getAllApplicating(request);
            if(!CollectionUtils.isEmpty(purchaseInfos)){
                return ResultVoUtil.success(purchaseInfos);
            }else {
                return ResultVoUtil.error(501, CommonCode.DO_NOT_HAVE_SUCH_INFO);
            }
        }catch (Exception e){
            log.error("DirectorController - > getAllApplication异常");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 更新采购药品信息
     * @param medName 药名
     * @param medNum 数量
     * @param request HttpServletRequest请求
     * @return ResultVo
     */
    @RequestMapping(value = "updatePurchaseInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo updatePurchaseInfo(String medName, String medNum, HttpServletRequest request){
        try {
            Integer res = directorService.updatePurchaseInfo(medName, medNum, request);
            if(res == 1){
                return ResultVoUtil.success();
            }else {
                return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
            }
        }catch (Exception e){
            log.error("DirectorController - > updatePurchaseInfo");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 删除采购明细
     * @param medName 药品名称
     * @param request HttpServletRequest请求
     * @return ResultVo
     */
    @RequestMapping(value = "delPurchaseInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo delPurchaseInfo(String medName, HttpServletRequest request){
        try {
            Integer res = directorService.delPurchaseInfo(medName, request);
            if(res == 1){
                return ResultVoUtil.success();
            }else {
                return ResultVoUtil.error(500, CommonCode.DELETE_FAIL);
            }
        }catch (Exception e){
            log.error("DirectorController - > updatePurchaseInfo");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 提交采购申请
     * @param request HttpServletRequest请求
     * @return ResultVo
     */
    @RequestMapping(value = "putPurchase", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo putPurchase(HttpServletRequest request){
        try {
            Integer res = directorService.putPurchase(request);
            if(res == 1){
                // 获取医院主管的id
                String id = directorService.getHosAdminId();
                return ResultVoUtil.success(id);
            }else if(res == -1){
                return ResultVoUtil.error(501, CommonCode.ADD_FAIL);
            }else{
                return ResultVoUtil.error(500, CommonCode.ERROR);
            }
        }catch (Exception e){
            log.error("DirectorController - > updatePurchaseInfo");
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 统计数量
     * @param request HttpServletRequest请求
     * @return ResultVo
     */
    @RequestMapping(value = "getCountOfMed", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo getCountOfMed(HttpServletRequest request){
        List<Count> counts = directorService.getCountOfMed(request);
        if(!CollectionUtils.isEmpty(counts)){
            return ResultVoUtil.success(counts);
        }else{
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 获取即将过期药品
     * @param page 分页参数
     * @return ResultVo
     */
    @RequestMapping(value = "getExpireMedInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getExpireMedInfo(Page page){
        PageVo pageVo = directorService.getExpireMedInfo(page);
        if(page != null){
            return ResultVoUtil.success(pageVo);
        }else{
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 下架药品
     * @param mid 药品ID
     * @param productTime 生产日期
     * @return ResultVo
     */
    @RequestMapping(value = "drawMed", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo drawMed(String mid, String productTime){
        Integer res = directorService.drawMed(mid, productTime);
        if(res==1){
            String id = directorService.getPurchaseId();
            List<String> pharmacyIds = directorService.getPharmacyIds();
            PersonIdVo personIdVo = new PersonIdVo();
            personIdVo.setPurchaseId(id);
            personIdVo.setPharmacyId(pharmacyIds);
            return ResultVoUtil.success(personIdVo);
        }else if(res==0){
            return ResultVoUtil.error(500, CommonCode.DELETE_FAIL);
        }else if(res == -2){
            return ResultVoUtil.error(502, CommonCode.ONLY_ONE_MED);
        }else if(res == -3){
            return ResultVoUtil.error(503, CommonCode.MEDINPRESCRIPT);
        }else{
            return ResultVoUtil.error(501, CommonCode.ERROR);
        }
    }


    /**
     * 页面跳转
     * @param viewName 视图名称
     * @return 页面
     */
    @RequestMapping(value = {"getView/{name}/{data}","getView/{name}"})
    public String getView(@PathVariable("name") String viewName, @PathVariable(required = false,value = "data")
            String data, Map map){
        map.put("data",data );
        return "/jsp/director/"+viewName;
    }

    @RequestMapping(value = "getMedByFun", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getMedByFun(String function){
        List<Medicinal> medicinals = directorService.getMedByFun(function);
        return ResultVoUtil.success(medicinals);
    }
}
