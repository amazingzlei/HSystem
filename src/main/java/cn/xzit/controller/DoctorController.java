package cn.xzit.controller;

import cn.xzit.common.CommonCode;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Medicinal;
import cn.xzit.service.DoctorService;
import cn.xzit.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    private  static Logger log = LoggerFactory.getLogger(DoctorController.class);

    /**
     * 获取所有药名
     * @return ResultVo
     */
    @RequestMapping(value = "getAllMedName", method = RequestMethod.GET)
    @ResponseBody
    public ResultVo getAllMedName(){
        try{
            List<Medicinal> medicinals = doctorService.getAllMedName();
            if(!CollectionUtils.isEmpty(medicinals)){
                return ResultVoUtil.success(medicinals);
            }else {
                log.info("DoctorController -> getAllMedName发生异常:medicinals为空");
                return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
            }
        }catch (Exception e){
            log.error("DoctorController -> getAllMedName发生异常:");
            e.printStackTrace();
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 开具药方
     * @param medNames 药名
     * @param nums 数量
     * @return ResultVo
     */
    @RequestMapping(value = "addPrescript", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo addPrescript(String medNames, String nums, HttpServletRequest request){
        try{
            String res = doctorService.addPrescript(medNames, nums, request);
            if("添加成功!".equals(res)){
                return ResultVoUtil.success(res);
            }else {
                return ResultVoUtil.error(500, res);
            }
        }catch (Exception e){
            log.error("DoctorController -> addPrescript发生异常:");
            e.printStackTrace();
            return ResultVoUtil.error(500, CommonCode.ADD_FAIL);
        }
    }
}
