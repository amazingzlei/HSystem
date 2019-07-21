package cn.xzit.dao;

import cn.xzit.entity.base.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public interface PharmacyDao {

    Prescript getPrescriptInfo(String content);

    List<PrescriptInfo> gerPrescriptDetail(String pid);

    Medicinal getEarlyMed(String mid);

    Medicinal getLateMed(String mid);

    Medicinal getDrawEarlyMed(String mid);

    Medicinal getDrawLateMed(String mid);

    /**
     * 根据ID判断是否已经入库
     * @param pid
     * @return
     */
    String isShellOut(String pid);

    Medicinal getMedByMidAndProductTime(@Param("mid") String mid, @Param("productTime") String productTime);

    List<Counter> getCounter(@Param("current") Integer current, @Param("limit") Integer limit);

    Integer getCounterCount();

    Counter getCounterByPosition(String position);

    Counter getCounterByPid(String pid);

    List<Medicinal> getDrawMed(@Param("current") Integer current, @Param("limit") Integer limit);

    Integer getDrawMedCount();

    List<Prescript> getNeedDrawPrescript(@Param("time") String time, @Param("current") Integer current,
                                         @Param("limit") Integer limit);

    Integer getNeedDrawPrescriptCount(String time);

    List<Medicinal> getLowNumMedicinal(@Param("current") Integer current, @Param("limit") Integer limit);

    Integer getLowNumMedicinalCount();

    List<Counter> getCanUseCounter();

    String getCounterIdByName(String name);

    String getCounterPositionById(String id);

    String getReperstoryPositionById(String id);

    String getIsSoldOut(@Param("mid") String mid, @Param("productTime") String productTime);
}
