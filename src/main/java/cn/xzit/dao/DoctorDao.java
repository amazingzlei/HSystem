package cn.xzit.dao;

import cn.xzit.entity.base.Medicinal;
import cn.xzit.entity.base.Prescript;
import cn.xzit.entity.base.PrescriptInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface DoctorDao {

    // 获取最早过期的药品
    Medicinal selectMedByName(String name);

    // 插入药方明细
    Integer insertPrescriptInfo(PrescriptInfo prescriptInfo);

    // 出入药方表
    Integer insertPrescript(Prescript prescript);

    Integer disMed(@Param("name") String name, @Param("endTime") String endTime, @Param("num") String num);

    // 查询库存
    Integer selectCountByMedName(String name);

    // 查询最晚过期的
    String selectLateMed(String name);
}
