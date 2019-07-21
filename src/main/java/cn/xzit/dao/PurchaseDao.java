package cn.xzit.dao;

import cn.xzit.entity.base.*;
import cn.xzit.entity.execl.PurchaseExport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PurchaseDao {

    List<Purchase> getPurchase(@Param("current") Integer current, @Param("limit") Integer limit);

    Integer getPurchaseCount();

    List<PurchaseInfo> getPurchaseInfo(@Param("current") Integer current, @Param("limit") Integer limit,
                                   @Param("pid") String pid);

    Integer getPurchaseInfoCount(String pid);

    List<PurchaseExport> getPurchaseExportInfo(String pid);

    Medicinal getDetailMed(String mid);

    List<Repertory> getReperstory(@Param("current") Integer current, @Param("limit") Integer limit);

    Integer getReperstoryCount();

    Repertory getRepertoryByPosition(String position);

    Repertory getRepertoryByPid(String pid);

    List<Repertory> getAllCanUseRepertory();

    Medicinal getMedByIdAndProductTime(@Param("mid") String mid, @Param("productTime") String productTime);

    Integer getCountOfIsNotPut(String pid);

    List<Medicinal> getDrawMed(@Param("current") Integer current, @Param("limit") Integer limit);

    Integer getDrawMedCount();

}
