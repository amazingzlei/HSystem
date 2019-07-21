package cn.xzit.service;

import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.base.Count;
import cn.xzit.entity.base.Medicinal;
import cn.xzit.entity.base.PurchaseInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DirectorService {
    PageVo getExpireMed(Page page);

    PageVo getBlowCount(Page page);

    Integer addPurchase(String medName, String medNum, HttpServletRequest request);

    Integer isPurchaseHas(String medName);

    PageVo getAllApplication(Page page, HttpServletRequest request);

    List<PurchaseInfo> getAllApplicating (HttpServletRequest request);

    Integer updatePurchaseInfo(String medName, String medNum, HttpServletRequest request);

    Integer delPurchaseInfo(String medName, HttpServletRequest request);

    Integer putPurchase(HttpServletRequest request);

    List<Count> getCountOfMed(HttpServletRequest request);

    PageVo getExpireMedInfo(Page page);

    Integer drawMed(String mid, String productTime);

    String getHosAdminId();

    String getPurchaseId();

    List<String> getPharmacyIds();

    List<Medicinal> getMedByFun(String function);
}
