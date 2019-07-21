package cn.xzit.dao;

import cn.xzit.entity.base.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DirectorDao {

    List<Medicinal> getExpireMed(@Param("current") Integer current, @Param("limit") Integer limit,
                                 @Param("endTime") String endTime);

    Integer getCountOfExpiredMed(String endTime);

    Integer getCountOfBlowCount();

    List<Medicinal> getBlowCount(@Param("current") Integer current, @Param("limit") Integer limit);

    void addPurchase(@Param("mid") String mid,@Param("medName") String medName,
                     @Param("medNum") String medNum, @Param("wid") String wid);

    String getMedByName(String name);

    List<PurchaseInfo> getPurchaseInfoByMid(String mid);

    List<PurchaseInfo> getPurchaseInfoByName(String name);

    void updatePurchaseInfo(@Param("mid") String mid,@Param("medName") String medName,
                            @Param("medNum") String medNum, @Param("wid") String wid);

    List<Purchase> getAllApplication(@Param("current") Integer current, @Param("limit") Integer limit,
                               @Param("wid") Integer wid);

    Integer getAllApplicationCount(Integer wid);

    List<PurchaseInfo> getAllApplicating(Integer wid);

    List<Count> getCountOfMed(String typeId);

    List<Medicinal> getExpireMedInfo(@Param("current") Integer current, @Param("limit") Integer limit,
                                     @Param("endTime") String endTime);

    Integer getExpiredMedInfoCount(String endTime);

    String getHosAdminId();

    String getPurchaseId();

    List<Medicinal> getMedByMid(String mid);

    List<String> getPharmacyIds();

    List<Prescript> getPrescriptByMid(String mid);
}
