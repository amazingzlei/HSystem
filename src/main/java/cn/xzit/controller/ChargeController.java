package cn.xzit.controller;

import cn.xzit.common.CommonCode;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Prescript;
import cn.xzit.service.ChargeService;
import cn.xzit.utils.ResultVoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "charge")
public class ChargeController {
    @Autowired
    private ChargeService chargeService;

    private  static Logger log = LoggerFactory.getLogger(ChargeController.class);

    /**
     * 根据id查询药方
     * @param content 药方ID
     * @return ResultVo
     */
    @RequestMapping(value = "getPrescript", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo getPrescript(String content){
        try{
            Prescript prescript = chargeService.selectPrescriptByPid(content);
            if(null == prescript){
                return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
            }else{
                return ResultVoUtil.success(prescript);
            }
        }catch (Exception e){
            log.error("ChargeController -> getPrescript异常");
            e.getStackTrace();
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }
    }

    /**
     * 药方收费
     * @param pid 药方id
     * @return ResultVo
     */
    @RequestMapping(value = "chargePrescript", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo chargePrescript(String pid){
       try{
           Integer result = chargeService.chargePrescript(pid);
           if(result == 1){
               return ResultVoUtil.success();
           }else{
               return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
           }
       }catch (Exception e){
           log.error("ChargeController -> chargePrescript异常");
           e.getStackTrace();
           return ResultVoUtil.error(500, CommonCode.UPDATE_FAIL);
       }
    }
}
