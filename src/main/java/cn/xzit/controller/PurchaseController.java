package cn.xzit.controller;

import cn.xzit.common.CommonCode;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Medicinal;
import cn.xzit.entity.base.Repertory;
import cn.xzit.entity.execl.PurchaseExport;
import cn.xzit.service.PurchaseService;
import cn.xzit.utils.ResultVoUtil;
import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "purchaseInfo")
@Controller
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    /**
     * 获取采购订单
     * @param page 分页
     * @return  ResultVo
     */
    @RequestMapping(value = "getPurchase", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getPurchase(Page page){
        PageVo pageVo = purchaseService.getPurchase(page);
        if(null != pageVo){
            return ResultVoUtil.success(pageVo);
        }else{
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 获取采购细节
     * @param page 分页
     * @param pid 采购ID
     * @return ResultVo
     */
    @RequestMapping(value = "getDetailInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getDetailInfo(Page page, String pid){
        PageVo pageVo = purchaseService.getDetailInfo(page, pid);
        if(null != pageVo){
            return ResultVoUtil.success(pageVo);
        }else{
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 导出execl
     * @param pid 采购ID
     * @param mids 药品ID
     * @param newMedNames 药品名称
     * @param mnums 数量
     * @param request HttpServletRequest请求
     * @param response HttpServletResponse响应
     * @param map 数据集合
     * @return 文件
     */
    @RequestMapping(value = "purchaseExport", method = RequestMethod.GET)
    public String purchaseExport(String pid, String mids, String newMedNames, String mnums,
                                   HttpServletRequest request, HttpServletResponse response	, ModelMap map){
        List<PurchaseExport> dataResult =  new ArrayList<PurchaseExport>();
//        String[] mid = mids.split(",");
//        String[] newMedName = newMedNames.split(",");
//        String[] mnum = mnums.split(",");
//        for(int i=0;i<mnum.length;i++){
//            dataResult.add(new PurchaseExport(mid[i],newMedName[i],mnum[i]));
//        }
        dataResult = purchaseService.getPurchaseExportInfo(pid);
        /*
         * NormalExcelConstants.FILE_NAME    就是文件名称
         * NormalExcelConstants.CLASS    就是导出的类型
         * NormalExcelConstants.PARAMS    这个是 可以设置一些参数
         * NormalExcelConstants.DATA_LIST   这个就是要导出的数据
         * NormalExcelConstants.JEECG_EXCEL_VIEW   导出 excel
         */
        map.put(NormalExcelConstants.FILE_NAME,pid+"采购单");
        map.put(NormalExcelConstants.CLASS,PurchaseExport.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams());
        map.put(NormalExcelConstants.DATA_LIST,dataResult);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 获取药品详情
     * @param mid 药品ID
     * @return ResultVo
     */
    @RequestMapping(value = "getDetailMed", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getDetailMed(String mid){
        Medicinal medicinal = purchaseService.getDetailMed(mid);
        if(medicinal != null){
            return ResultVoUtil.success(medicinal);
        }else{
            return ResultVoUtil.error(404,CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 获取仓库信息
     * @param page 分页
     * @return ResultVo
     */
    @RequestMapping(value = "getReperstory", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getReperstory(Page page){
        PageVo pageVo = purchaseService.getReperstory(page);
        if(null != pageVo){
            if(null != pageVo.getData()){
                return ResultVoUtil.success(pageVo);
            }else{
                return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
            }
        }else{
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 添加仓库
     * @param position 位置
     * @return ResultVo
     */
    @RequestMapping(value = "addReperstory", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo addReperstory(String position){
        if(!StringUtils.isEmpty(position)){
            Integer res = purchaseService.addReperstory(position);
            if(res == 1){
                return ResultVoUtil.success();
            }else if(res == -1){
                return ResultVoUtil.error(500, CommonCode.HASPOSITION);
            }else {
                return ResultVoUtil.error(500, CommonCode.ADD_FAIL);
            }
        }else {
            return ResultVoUtil.error(500, CommonCode.NOINFO);
        }
    }

    /**
     * 删除仓库
     * @param rid 仓库ID
     * @return ResultVo
     */
    @RequestMapping(value = "deleteReperstory", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo deleteReperstory(String rid){
        Integer res = purchaseService.deleteReperstory(rid);
        if(res == -1){
           return ResultVoUtil.error(500, CommonCode.CANDELTEISUSE);
        }else if(res == 1){
            return ResultVoUtil.success();
        }else{
            return ResultVoUtil.error(500, CommonCode.DELETE_FAIL);
        }
    }

    /**
     * 修改仓库
     * @param rid 仓库ID
     * @param position 位置
     * @return ResultVo
     */
    @RequestMapping(value = "/updateReperstory", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo updateReperstory(String rid, String position){
        Integer res = purchaseService.updateReperstory(rid,position);
        if(res == 1){
            return ResultVoUtil.success();
        }else if(res == -1){
            return ResultVoUtil.error(500, CommonCode.HASPOSITION);
        }else {
            return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
        }
    }

    /**
     * 获取所以可用仓库信息
     * @return ResultVo
     */
    @RequestMapping(value = "/getAllCanUseRepertory", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getAllCanUseRepertory(){
        List<Repertory> repertories = purchaseService.getAllCanUseRepertory();
        if(!CollectionUtils.isEmpty(repertories)){
            return ResultVoUtil.success(repertories);
        }else{
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 采购的药品入库
     * @param medicinal 药品信息
     * @return ResultVo
     */
    @RequestMapping(value = "/putMed", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo putMed(Medicinal medicinal, String pid){
        Integer res = purchaseService.putMed(medicinal, pid);
        if(res == -2){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }else if(res == -1){
            return ResultVoUtil.error(500, CommonCode.ERRORPRODUCTTIME);
        }else if(res == 0){
            return ResultVoUtil.error(500, CommonCode.PUTERROR);
        }else if(res == 2){
            return ResultVoUtil.success(2);
        }else{
            return ResultVoUtil.success();
        }
    }

    /**
     * 将采购单入库
     * @param pid 采购ID
     * @param request HttpServletRequest请求
     * @return ResultVo
     */
    @RequestMapping(value = "/putPurchase", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo putPurchase(String pid, HttpServletRequest request){
        Integer count = purchaseService.putPurchase(pid, request);
        if(count == 1){
            return ResultVoUtil.success();
        }else if(count == 0){
            return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
        }else{
            return ResultVoUtil.error(500, CommonCode.MEDNOTPUT);
        }
    }

    /**
     * 获取下架的药品
     * @param page 分页
     * @return ResultVo
     */
    @RequestMapping(value = "getDrawMed", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getDrawMed(Page page){
        PageVo pageVo = purchaseService.getDrawMed(page);
        if(pageVo != null){
            return ResultVoUtil.success(pageVo);
        }else{
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 清仓
     * @param mid 药品ID
     * @param repertoryId 仓库ID
     * @param productTime 生产日期
     * @return ResultVo
     */
    @RequestMapping(value = "cleanRepertory", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo cleanRepertory(String mid, String repertoryId, String productTime){
        Integer result = purchaseService.cleanRepertory(mid, repertoryId, productTime);
        if(result == 1){
            return ResultVoUtil.success();
        }else if(result == 0){
            return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
        }else{
            return ResultVoUtil.error(501, CommonCode.ERROR);
        }
    }

    /**
     * 页面跳转
     * @param viewName 视图名
     * @return 视图
     */
    @RequestMapping(value = {"getView/{name}/{data}","getView/{name}"})
    public String getView(@PathVariable("name") String viewName,@PathVariable(required = false,value = "data")
            String data, Map map){
        map.put("data",data );
        return "/jsp/purshase/"+viewName;
    }
}
