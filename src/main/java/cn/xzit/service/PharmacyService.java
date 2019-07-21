package cn.xzit.service;

import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.Counter;
import cn.xzit.entity.base.Medicinal;
import cn.xzit.entity.base.Prescript;
import cn.xzit.entity.vo.PrescirptVo;

import java.util.List;

public interface PharmacyService {

    //Prescript getPrescriptInfo(String content);
    ResultVo getPrescriptInfo(String content);

    List<PrescirptVo> gerPrescriptDetail(String pid);

    Integer shellOut(String pid, String mids, String productTimes, String nums);

    PageVo getCounter(Page page);

    Integer updateCounter(String rid, String position);

    Integer deleteCounter(String rid);

    Integer addCounter(String position);

    PageVo getDrawMed(Page page);

    Integer cleanCounter(String mid, String counterId, String productTime);

    PageVo getNeedDrawPrescript(Page page);

    Integer dropPrescript(String pid);

    PageVo getLowNumMedicinal(Page page);

    List<Counter> getCanUseCounter();

    Integer addMed(Medicinal medicinal);
}
