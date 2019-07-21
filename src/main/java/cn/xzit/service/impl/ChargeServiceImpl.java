package cn.xzit.service.impl;

import cn.xzit.dao.ChargeDao;
import cn.xzit.entity.base.Prescript;
import cn.xzit.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ChargeServiceImpl implements ChargeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ChargeDao chargeDao;

    /**
     * 根据药方id查询药方
     * @param pid 药方ID
     * @return 药方信息
     */
    @Override
    public Prescript selectPrescriptByPid(String pid) {
        return chargeDao.selectPrescriptByPid(pid);
    }

    /**
     * 药方付费
     * @param pid 药方ID
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer chargePrescript(String pid) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "update prescript set isCharge=1,charge_time = ? where pid = ?";
        return jdbcTemplate.update(sql, sdf.format(date), pid);
    }
}
