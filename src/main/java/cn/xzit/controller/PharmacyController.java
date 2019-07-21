package cn.xzit.controller;

import cn.xzit.common.CommonCode;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Counter;
import cn.xzit.entity.base.Medicinal;
import cn.xzit.entity.base.Prescript;
import cn.xzit.entity.execl.PrescriptExport;
import cn.xzit.entity.vo.PrescirptVo;
import cn.xzit.service.PharmacyService;
import cn.xzit.utils.ResultVoUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
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

@Controller
@RequestMapping(value = "pharmacyInfo")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    /**
     * 页面跳转
     * @param viewName 视图名称
     * @return ResultVo
     */
    @RequestMapping(value = {"getView/{name}/{data}","getView/{name}"})
    public String getView(@PathVariable("name") String viewName, @PathVariable(required = false,value = "data")
            String data, Map map){
        map.put("data",data );
        return "/jsp/pharmacy/"+viewName;
    }

    /**
     * 根据药方id查询药方
     * @param content 药方ID
     * @return ResultVo
     */
    @RequestMapping(value = "getPrescriptInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getPrescriptInfo(String content){
        return pharmacyService.getPrescriptInfo(content);
    }

    /**
     * 获取药方明细
     * @param pid 药方ID
     * @return ResultVo
     */
    @RequestMapping(value = "gerPrescriptDetail", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo gerPrescriptDetail(String pid){
        try {
            List<PrescirptVo> prescirptVoList = pharmacyService.gerPrescriptDetail(pid);
            if(!CollectionUtils.isEmpty(prescirptVoList)){
                return ResultVoUtil.success(prescirptVoList);
            }else{
                return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
            }
        }catch (Exception e){
            e.getStackTrace();
            return ResultVoUtil.error(500, CommonCode.HASDRAWMED);
        }
    }

    /**
     * 药品出库
     * @param pid 药方ID
     * @param mids 药品ID
     * @param productTimes 生产日期
     * @param nums 数量
     * @return ResultVo
     */
    @RequestMapping(value = "shellOut", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo shellOut(String pid, String mids, String productTimes, String nums){
        Integer result = pharmacyService.shellOut(pid, mids, productTimes, nums);
        if(result == -2){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }else if(result == -1){
            return ResultVoUtil.error(500, CommonCode.HAS_SHELLOUT);
        }else if(result == 0){
            return ResultVoUtil.error(501, CommonCode.UPDATE_FAIL);
        }else if(result == -3){
            return ResultVoUtil.error(502, CommonCode.HASDRAWMED);
        }else {
            return ResultVoUtil.success();
        }
    }

    /**
     * 获取药台信息
     * @param page 分页参数
     * @return ResultVo
     */
    @RequestMapping(value = "getCounter", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getReperstory(Page page){
        PageVo pageVo = pharmacyService.getCounter(page);
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
     * 添加药台
     * @param position 位置
     * @return ResultVo
     */
    @RequestMapping(value = "addCounter", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo addReperstory(String position){
        if(!StringUtils.isEmpty(position)){
            Integer res = pharmacyService.addCounter(position);
            if(res == 1){
                return ResultVoUtil.success();
            }else if(res == -1){
                return ResultVoUtil.error(500, CommonCode.COUNTERHASPOSITION);
            }else {
                return ResultVoUtil.error(500, CommonCode.ADD_FAIL);
            }
        }else {
            return ResultVoUtil.error(500, CommonCode.NOCOUNTERINFO);
        }
    }

    /**
     * 删除药台
     * @param rid 药台ID
     * @return ResultVo
     */
    @RequestMapping(value = "deleteCounter", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo deleteReperstory(String rid){
        Integer res = pharmacyService.deleteCounter(rid);
        if(res == -1){
            return ResultVoUtil.error(500, CommonCode.CANCOUNTERDELTEISUSE);
        }else if(res == 1){
            return ResultVoUtil.success();
        }else{
            return ResultVoUtil.error(500, CommonCode.DELETE_FAIL);
        }
    }

    /**
     * 修改仓库
     * @param rid 药台ID
     * @param position 位置
     * @return ResultVo
     */
    @RequestMapping(value = "/updateCounter", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo updateReperstory(String rid, String position){
        Integer res = pharmacyService.updateCounter(rid,position);
        if(res == 1){
            return ResultVoUtil.success();
        }else if(res == -1){
            return ResultVoUtil.error(500, CommonCode.COUNTERHASPOSITION);
        }else {
            return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
        }
    }

    /**
     * 获取下架的药品
     * @param page 分页参数
     * @return ResultVo
     */
    @RequestMapping(value = "getDrawMed", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getDrawMed(Page page){
        PageVo pageVo = pharmacyService.getDrawMed(page);
        if(pageVo != null){
            return ResultVoUtil.success(pageVo);
        }else{
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 清柜
     * @param mid 药品id
     * @param counterId 药台id
     * @param productTime 生产日期
     * @return ResultVo
     */
    @RequestMapping(value = "cleanCounter", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo cleanCounter(String mid, String counterId, String productTime){
        Integer result = pharmacyService.cleanCounter(mid, counterId, productTime);
        if(result == 1){
            return ResultVoUtil.success();
        }else if(result == 0){
            return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
        }else{
            return ResultVoUtil.error(501, CommonCode.ERROR);
        }
    }

    /**
     * 获取需要下架的药方
     * @param page 分页
     * @return ResultVo
     */
    @RequestMapping(value = "getNeedDrawPrescript", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getNeedDrawPrescript(Page page){
        PageVo pageVo = pharmacyService.getNeedDrawPrescript(page);
        if(pageVo != null){
            return ResultVoUtil.success(pageVo);
        }else{
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 药方作废
     * @param pid 药方ID
     * @return ResultVo
     */
    @RequestMapping(value = "dropPrescript", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo dropPrescript(String pid){
        Integer res = pharmacyService.dropPrescript(pid);
        if(res == -2){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }else if(res == 0){
            return ResultVoUtil.error(501, CommonCode.UPDATE_FAIL);
        }else {
            return ResultVoUtil.success();
        }
    }

    /**
     * 获取药台库存不足的药品
     * @param page 分页
     * @return ResultVo
     */
    @RequestMapping(value = "getLowNumMedicinal", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getLowNumMedicinal(Page page){
        PageVo pageVo = pharmacyService.getLowNumMedicinal(page);
        if(pageVo != null){
            return ResultVoUtil.success(pageVo);
        }else{
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }
    }

    /**
     * 获取可用药台
     * @return ResultVo
     */
    @RequestMapping(value = "getCanUseCounter", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getCanUseCounter(){
        List<Counter> counters = pharmacyService.getCanUseCounter();
        return ResultVoUtil.success(counters);
    }

    /**
     * 补药
     * @param medicinal 药品信息
     * @return ResultVo
     */
    @RequestMapping(value = "addMed", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo addMed(Medicinal medicinal){
        Integer res = pharmacyService.addMed(medicinal);
        if(res == -2){
            return ResultVoUtil.error(500, CommonCode.ERROR);
        }else if(res == -1){
            return ResultVoUtil.error(501, CommonCode.ERROR_COUNTER);
        }else if(res == 0){
            return ResultVoUtil.error(502, CommonCode.ADD_FAIL);
        }else{
            return ResultVoUtil.success();
        }
    }

    /**
     * 导出
     * @param mids 药品id
     * @param newMedNames 药品名称
     * @param mnums 数量
     * @param request HttpServletRequest请求
     * @param response HttpServletResponse响应
     * @param map 数据集合
     * @return 文件
     */
    @RequestMapping(value = "prescriptExport", method = RequestMethod.GET)
    public String purchaseExport(String mids, String newMedNames, String counterPositions, String repertoryPositions,
                                 String mnums, HttpServletRequest request, HttpServletResponse response	, ModelMap map){
        List<PrescriptExport> dataResult =  new ArrayList<PrescriptExport>();
        String[] mid = mids.split(",");
        String[] newMedName = newMedNames.split(",");
        String[] mnum = mnums.split(",");
        String[] counterPosition = counterPositions.split(",");
        String[] repertoryPosition = repertoryPositions.split(",");
        for(int i=0;i<mnum.length;i++){
            dataResult.add(new PrescriptExport(mid[i],newMedName[i],counterPosition[i],repertoryPosition[i],mnum[i]));
        }
        /*
         * NormalExcelConstants.FILE_NAME    就是文件名称
         * NormalExcelConstants.CLASS    就是导出的类型
         * NormalExcelConstants.PARAMS    这个是 可以设置一些参数
         * NormalExcelConstants.DATA_LIST   这个就是要导出的数据
         * NormalExcelConstants.JEECG_EXCEL_VIEW   导出 excel
         */
        map.put(NormalExcelConstants.FILE_NAME,"药方明细单");
        map.put(NormalExcelConstants.CLASS,PrescriptExport.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams());
        map.put(NormalExcelConstants.DATA_LIST,dataResult);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }
}
