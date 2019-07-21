package cn.xzit.dao;

import cn.xzit.entity.base.Prescript;
import org.springframework.stereotype.Component;

@Component
public interface ChargeDao {
    Prescript selectPrescriptByPid(String pid);
}
