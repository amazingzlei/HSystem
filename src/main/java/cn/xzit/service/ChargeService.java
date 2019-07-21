package cn.xzit.service;

import cn.xzit.entity.base.Prescript;

public interface ChargeService {

    Prescript selectPrescriptByPid(String pid);

    Integer chargePrescript(String pid);
}
