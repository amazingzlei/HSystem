package cn.xzit.service;

import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.base.Medicinal;
import cn.xzit.entity.base.Repertory;
import cn.xzit.entity.execl.PurchaseExport;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PurchaseService {

    PageVo getPurchase(Page page);

    PageVo getDetailInfo(Page page, String pid);

    List<PurchaseExport> getPurchaseExportInfo(String pid);

    Medicinal getDetailMed(String mid);

    PageVo getReperstory(Page page);

    Integer addReperstory(String position);

    Integer deleteReperstory(String rid);

    Integer updateReperstory(String rid, String position);

    List<Repertory> getAllCanUseRepertory();

    Integer putMed(Medicinal medicinal, String pid);

    Integer putPurchase(String pid, HttpServletRequest request);

    PageVo getDrawMed(Page page);

    Integer cleanRepertory(String mid, String repertoryId, String productTime);

}
