package cn.xzit.service.impl;

import cn.xzit.common.CommonCode;
import cn.xzit.dao.PharmacyDao;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.ResultVo;
import cn.xzit.entity.base.*;
import cn.xzit.entity.vo.PrescirptVo;
import cn.xzit.service.PharmacyService;
import cn.xzit.utils.ResultVoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    @Autowired
    private PharmacyDao pharmacyDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private  static Logger log = LoggerFactory.getLogger(PharmacyServiceImpl.class);

    /**
     * 根据药方ID查询药方
     * @param content 药方ID
     * @return 药方
     */
    @Override
    public ResultVo getPrescriptInfo(String content) {
        //return pharmacyDao.getPrescriptInfo(content);

        // 先获取药方
        Prescript prescript = pharmacyDao.getPrescriptInfo(content);

        if(prescript == null){
            return ResultVoUtil.error(404, CommonCode.NO_SUCH_INFO);
        }else{

            try {
                // 然后根据药方ID获取药方药品详情
                List<PrescirptVo> prescirptVos = gerPrescriptDetail(prescript.getPid());
                return ResultVoUtil.success(prescript);
            }catch (Exception e){
                return ResultVoUtil.success2(prescript);
            }
            // 然后根据药方ID获取药方药品详情
            // List<PrescirptVo> prescirptVos = gerPrescriptDetail(prescript.getPid());
            // 遍历是否有过期药品

//            boolean flag = true;
//
//            for (PrescirptVo prescirptVo : prescirptVos){
//                String isSoldOut = pharmacyDao.getIsSoldOut(prescirptVo.getMid(),
//                        prescirptVo.getProductTime());
//                if(isSoldOut == "1"){
//                    flag = false;
//                    break;
//                }
//            }
//
//            if(flag == true){
//                return ResultVoUtil.success(prescript);
//            }else{
//                return ResultVoUtil.success2(prescript);
//            }
        }

    }

    /**
     * 获取药方细节
     * @param pid 药方id
     * @return 药方明细
     */
    @Override
    public List<PrescirptVo> gerPrescriptDetail(String pid) {

        List<PrescirptVo> prescirptVos = new ArrayList<>();

        // 1.先获取药方明细中的药品id、名称、数量
        List<PrescriptInfo> prescriptInfos = pharmacyDao.gerPrescriptDetail(pid);

        // 2.遍历
        for (PrescriptInfo prescriptInfo: prescriptInfos) {

            PrescirptVo prescirptVo = new PrescirptVo();

            int needCount = Integer.valueOf(prescriptInfo.getMnum());

            // 获取最早生产日期的药品
            Medicinal medicinal = pharmacyDao.getEarlyMed(prescriptInfo.getMid());

            medicinal.setCounterId(pharmacyDao.getCounterPositionById(medicinal.getCounterId()));
            medicinal.setRepertoryId(pharmacyDao.getReperstoryPositionById(medicinal.getRepertoryId()));

            int counterLeft, repertoryLeft;

            if(null == medicinal.getCounterLeft()){
                counterLeft = 0;
            }else{
                counterLeft = Integer.valueOf(medicinal.getCounterLeft());
            }

            if(null == medicinal.getRepertoryLeft()){
                repertoryLeft = 0;
            }else {
                repertoryLeft = Integer.valueOf(medicinal.getRepertoryLeft());
            }

            int hasCount = counterLeft+repertoryLeft;

            if(needCount <= hasCount){// 库存足够
                BeanUtils.copyProperties(medicinal, prescirptVo);
                prescirptVo.setNum(prescriptInfo.getMnum());
                prescirptVo.setPid(prescriptInfo.getPid());
                prescirptVos.add(prescirptVo);
            }else{// 库存不够
                BeanUtils.copyProperties(medicinal, prescirptVo);
                prescirptVo.setNum(String.valueOf(hasCount));
                prescirptVo.setPid(prescriptInfo.getPid());
                prescirptVos.add(prescirptVo);

                Medicinal medicinal1 = pharmacyDao.getLateMed(prescriptInfo.getMid());

                medicinal1.setCounterId(pharmacyDao.getCounterPositionById(medicinal1.getCounterId()));
                medicinal1.setRepertoryId(pharmacyDao.getReperstoryPositionById(medicinal1.getRepertoryId()));

                PrescirptVo prescirptVo1 = new PrescirptVo();
                BeanUtils.copyProperties(prescriptInfo, prescirptVo1);
                BeanUtils.copyProperties(medicinal1, prescirptVo1);
                prescirptVo1.setNum(String.valueOf(needCount-hasCount));
                prescirptVo.setPid(prescriptInfo.getPid());
                prescirptVos.add(prescirptVo1);
            }
        }

        return prescirptVos;
    }

    /**
     * 药品出库
     * @param pid 药方ID
     * @param mid 药品ID
     * @param productTime 生产日期
     * @param num 数量
     * @return 受影响的数量
     */
    @Override
    @Transactional
    public Integer shellOut(String pid, String mid, String productTime, String num) {
        try{
            // 判断有没有下架药品
            try{
                List<PrescirptVo> prescirptVos = gerPrescriptDetail(pid);

                Integer result = 0;
                String[] mids = mid.split(",");
                String[] productTimes = productTime.split(",");
                String[] nums = num.split(",");
                // 先判断是否已经出库
                String isShellOut = pharmacyDao.isShellOut(pid);
                if("1".equals(isShellOut)){
                    return -1;
                }else{
                    // 循环遍历
                    for (int i = 0; i < mids.length; i++) {
                        // 通过ID、时间获取药品详情
                        Medicinal medicinal = pharmacyDao.getMedByMidAndProductTime(mids[i],productTimes[i] );
                        Integer shellOutCount = Integer.valueOf(nums[i]);
                        if(shellOutCount<=Integer.valueOf(medicinal.getCounterLeft())){// 如果药台库存足够
                            String updateMed = "UPDATE medicinal set counterLeft = counterLeft-? WHERE mid=? AND productTime = ?";
                            result += jdbcTemplate.update(updateMed, shellOutCount,mids[i],productTimes[i]);
                        }else{
                            Integer left = shellOutCount-Integer.valueOf(medicinal.getCounterLeft());
                            String updateMed = "UPDATE medicinal set counterLeft = 0,repertoryLeft=repertoryLeft-? WHERE mid=? AND productTime = ?";
                            result += jdbcTemplate.update(updateMed, left,mids[i],productTimes[i]);
                        }
                    }
                    if(result != mids.length){
                        return 0;
                    }else{
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String updatePrescript = "UPDATE prescript set isShell=1,shell_time=? where pid = ?";
                        Integer res = jdbcTemplate.update(updatePrescript, sdf.format(date),pid);
                        return res;
                    }
                }
            }catch (Exception e){
                return -3;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 获取药台信息
     * @param page 分页信息
     * @return PageVo
     */
    @Override
    public PageVo getCounter(Page page) {
        try {
            List<Counter> repertories = pharmacyDao.getCounter(
                    (page.getPage() - 1) * page.getPageSize(), page.getPageSize());
            PageVo pageVo = new PageVo();
            if (!CollectionUtils.isEmpty(repertories)) {
                Integer count = pharmacyDao.getCounterCount();
                int size = (count % page.getPageSize()) == 0 ? count / page.getPageSize() : count / page.getPageSize() + 1;
                pageVo.setSize(size);
                pageVo.setData(repertories);
                return pageVo;
            } else {
                return pageVo;
            }
        } catch (Exception e) {
            log.error("PharmacyServiceImpl -> getCounter发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 添加药台
     * @param position 位置
     * @return 受影响的数量
     */
    @Override
    public Integer addCounter(String position) {
        // 通过名称获取药台
        Counter counter = pharmacyDao.getCounterByPosition(position);
        // 判断是否重名
        if(null == counter){
            String sql = "INSERT INTO counter(position,isUse) VALUES(?,0)";
            int res = jdbcTemplate.update(sql, position);
            return res;
        }else{
            return -1;
        }
    }

    /**
     * 删除药台
     * @param cid 药台ID
     * @return 受影响的数量
     */
    @Override
    public Integer deleteCounter(String cid) {
        // 通过id查询药台信息
        Counter counter = pharmacyDao.getCounterByPid(cid);
        // 如药台正在使用则不能删除
        if(counter.getIsUse().equals("1")){
            return -1;
        }else{
            String sql = "DELETE FROM counter WHERE cid = ?";
            int res = jdbcTemplate.update(sql, cid);
            return res;
        }
    }

    /**
     * 更新药台
     * @param cid 药台ID
     * @param position 位置
     * @return 受影响的数量
     */
    @Override
    @Transactional
    public Integer updateCounter(String cid, String position) {
        Counter counter = pharmacyDao.getCounterByPosition(position);
        // 判断是否重名
        if(null == counter){
            String sql = "UPDATE counter SET position=? WHERE cid = ?";
            int res = jdbcTemplate.update(sql, position, cid);
            return res;
        }else{
            return -1;
        }
    }

    /**
     * 获取下架列表
     * @param page 分页
     * @return pageVo
     */
    @Override
    public PageVo getDrawMed(Page page) {
        try{
            List<Medicinal> medicinals =  pharmacyDao.getDrawMed(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize());
            PageVo pageVo = new PageVo();
            if(!CollectionUtils.isEmpty(medicinals)){
                Integer count = pharmacyDao.getDrawMedCount();
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                pageVo.setSize(size);
                pageVo.setData(medicinals);
                return pageVo;
            }else {
                return pageVo;
            }
        }catch (Exception e){
            log.error("PharmacyServiceImpl -> getDrawMed发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 清柜
     * @param mid 药品ID
     * @param counterId 药台ID
     * @param productTime 生产日期
     * @return 受影响的数量
     */
    @Override
    public Integer cleanCounter(String mid, String counterId, String productTime) {
        try{
            // 清仓
            String cleanRep = "UPDATE medicinal set counterLeft = 0 WHERE mid=? and productTime = ?";
            int cleanRepRes = jdbcTemplate.update(cleanRep, mid, productTime);
            // 仓库致0
            String updateRep = "UPDATE counter set isUse = 0 WHERE cid = ?";
            int updateRepRes = jdbcTemplate.update(updateRep, counterId);
            if(cleanRepRes+updateRepRes == 2){
                return 1;
            }else{
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 获取需要作废的药方
     * @param page 分页参数
     * @return PageVo
     */
    @Override
    public PageVo getNeedDrawPrescript(Page page) {
        try{
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Prescript> prescripts =  pharmacyDao.getNeedDrawPrescript(
                    sdf.format(date),(page.getPage()-1)*page.getPageSize(),page.getPageSize());
            PageVo pageVo = new PageVo();
            if(!CollectionUtils.isEmpty(prescripts)){
                Integer count = pharmacyDao.getNeedDrawPrescriptCount(sdf.format(date));
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                pageVo.setSize(size);
                pageVo.setData(prescripts);
                return pageVo;
            }else {
                return pageVo;
            }
        }catch (Exception e){
            log.error("PharmacyServiceImpl -> getNeedDrawPrescript发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 药方作废
     * @param pid 药方ID
     * @return 受影响的数量
     */
    @Override
    @Transactional
    public Integer dropPrescript(String pid) {
        try{
            int count = 0;
            // 通过id获取药方明细
            List<PrescriptInfo> prescriptInfos = pharmacyDao.gerPrescriptDetail(pid);
            if(!CollectionUtils.isEmpty(prescriptInfos)){
                // 遍历
                for (PrescriptInfo prescriptInfo: prescriptInfos) {
                    String mid = prescriptInfo.getMid();
                    Integer num = Integer.valueOf(prescriptInfo.getMnum());

                    // 通过药品id获取生产日期最早的药品
                    Medicinal medicinal = pharmacyDao.getDrawEarlyMed(mid);
                    // 判断药方中的数量是否大于药台库存与仓库库存之和，如果大于则表示有的药品从生产日期最晚的中获取

                    int counterLeft, repertoryLeft;

                    if(null == medicinal.getCounterLeft()){
                        counterLeft = 0;
                    }else{
                        counterLeft = Integer.valueOf(medicinal.getCounterLeft());
                    }

                    if(null == medicinal.getRepertoryLeft()){
                        repertoryLeft = 0;
                    }else {
                        repertoryLeft = Integer.valueOf(medicinal.getRepertoryLeft());
                    }

                    Integer left = counterLeft+repertoryLeft;
                    if(num<=left){
                        // 直接数量回退
                        String sql = "UPDATE medicinal set totalCount = totalCount+? where mid=? and productTime=?";
                        String updatePrescirpt = "update prescript set isCancel=1 where pid=?";

                        int res = jdbcTemplate.update(sql, num, medicinal.getMid(), medicinal.getProductTime())+
                                jdbcTemplate.update(updatePrescirpt, pid);
                        if(res == 2){
                            count += 1;
                        }
                    }else{
                        // 获取生产日期最晚的
                        Medicinal lateMed = pharmacyDao.getDrawLateMed(mid);
                        String updateEarlyMed = "update medicinal set totalCount=totalCount+? where mid=? and productTime=?";
                        String updateLateMed = "update medicinal set totalCount=totalCount+? where mid=? and productTime=?";
                        String updatePrescirpt = "update prescript set isCancel=1 where pid=?";
                        int res = jdbcTemplate.update(updateEarlyMed, left, medicinal.getMid(), medicinal.getProductTime()) +
                                jdbcTemplate.update(updateLateMed, num-left, lateMed.getMid(), lateMed.getProductTime())+
                                jdbcTemplate.update(updatePrescirpt, pid);
                        if(res == 3){
                            count += 1;
                        }
                    }
                }
                if(count == prescriptInfos.size()){
                    return 1;
                }else {
                    return 0;
                }
            }else{
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 获取药台库存不足的药品
     * @param page 分页参数
     * @return pageVo
     */
    @Override
    public PageVo getLowNumMedicinal(Page page) {
        try{
            List<Medicinal> medicinals =  pharmacyDao.getLowNumMedicinal(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize());
            PageVo pageVo = new PageVo();
            if(!CollectionUtils.isEmpty(medicinals)){
                Integer count = pharmacyDao.getLowNumMedicinalCount();
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                pageVo.setSize(size);
                pageVo.setData(medicinals);
                return pageVo;
            }else {
                return pageVo;
            }
        }catch (Exception e){
            log.error("PharmacyServiceImpl -> getLowNumMedicinal发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 获取可用药台
     * @return list
     */
    @Override
    public List<Counter> getCanUseCounter() {
        return pharmacyDao.getCanUseCounter();
    }

    /**
     * 补药
     * @param medicinal 药品信息
     * @return 受影响的记录数
     */
    @Override
    public Integer addMed(Medicinal medicinal) {
        try{
            // 获取药台ID
            String id = pharmacyDao.getCounterIdByName(medicinal.getCounterId());
            if(null == id){
                return -1;
            }else{
                medicinal.setCounterId(id);
                // 补药
                String updateMed = "UPDATE medicinal set counterLeft = counterLeft+?,repertoryLeft " +
                        "= repertoryLeft - ?,counterId = ? where mid = ? and productTime = ?";

                int res = jdbcTemplate.update(updateMed, medicinal.getCounterLeft(), medicinal.getCounterLeft(),
                        id,medicinal.getMid(), medicinal.getProductTime());

                String updateCounter = "UPDATE counter set isUse=1 where cid = ?";
                jdbcTemplate.update(updateCounter, id);

                if(res == 1){
                    return 1;
                }else {
                    return 0;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return -2;
        }
    }
}
