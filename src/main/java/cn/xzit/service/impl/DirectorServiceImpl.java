package cn.xzit.service.impl;

import cn.xzit.dao.DirectorDao;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.base.*;
import cn.xzit.service.DirectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DirectorServiceImpl implements DirectorService{

    @Autowired
    private DirectorDao directorDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private  static Logger log = LoggerFactory.getLogger(DirectorServiceImpl.class);
    /**
     * 获取即将过期的分页药品
     * @param page 分页
     * @return PageVo
     */
    @Override
    public PageVo getExpireMed(Page page) {
        try{
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Medicinal> medicinals =  directorDao.getExpireMed(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize(), sdf.format(date) );
            PageVo pageVo = new PageVo();
            if(!CollectionUtils.isEmpty(medicinals)){
                Integer count = directorDao.getCountOfExpiredMed(sdf.format(date));
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                pageVo.setSize(size);
                pageVo.setData(medicinals);
                return pageVo;
            }else {
                return pageVo;
            }
        }catch (Exception e){
            log.error("DirectorServiceImpl -> getExpireMed发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 获取库存不足的药品
     * @param page 分页
     * @return PageVo
     */
    @Override
    public PageVo getBlowCount(Page page) {
        try{
            List<Medicinal> medicinals =  directorDao.getBlowCount(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize());
            PageVo pageVo = new PageVo();
            if(!CollectionUtils.isEmpty(medicinals)){
                Integer count = directorDao.getCountOfBlowCount();
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                pageVo.setSize(size);
                pageVo.setData(medicinals);
                return pageVo;
            }else {
                return pageVo;
            }
        }catch (Exception e){
            log.error("DirectorServiceImpl -> getExpireMed发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 添加采购
     * @param medName 药名
     * @param medNum 数量
     * @param request 请求
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer addPurchase(String medName, String medNum, HttpServletRequest request) {
        try {

            // 通过药品名称获取药品id
            String mid = directorDao.getMedByName(medName);

            // 获取员工id
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");

            //如果id为空，表示添加新药
            if(StringUtils.isEmpty(mid)){
                // 判断是否以及采购过
                Integer res = this.isPurchaseHas(medName);
                if(res == 1){// 如果已经采购过 就更新 否则添加
                    directorDao.updatePurchaseInfo(null,medName, medNum, String.valueOf(employee.getWid()));
                }else{
                    directorDao.addPurchase(null,medName, medNum, String.valueOf(employee.getWid()));
                }
            }else{
                // 判断是否以及采购过
                Integer res = this.isPurchaseHas(medName);
                if(res == 1){// 如果已经采购过 就更新 否则添加
                    directorDao.updatePurchaseInfo(mid, null,medNum, String.valueOf(employee.getWid()));
                }else{
                    directorDao.addPurchase(mid,null, medNum, String.valueOf(employee.getWid()));
                }
            }
            return 1;
        }catch (Exception e){
            log.error("DirectorServiceImpl -> addPurchase发生异常!");
            e.getStackTrace();
            return -1;
        }
    }

    /**
     * 判断采购表中是否有该数据
     * @param medName 药品名称
     * @return 受影响的条数
     */
    @Override
    public Integer isPurchaseHas(String medName) {

        // 通过药品名称获取药品id
        String mid = directorDao.getMedByName(medName);

        List<PurchaseInfo> purchaseInfos = null;

        // 如果药品id为空，表示采购的是新药
        if(StringUtils.isEmpty(mid)){
            // 通过名称查询是否存在
            purchaseInfos = directorDao.getPurchaseInfoByName(medName);
        }else{
            // 通过id查询采购明细表
            purchaseInfos= directorDao.getPurchaseInfoByMid(mid);
        }
        if(!CollectionUtils.isEmpty(purchaseInfos)){
            return 1;
        }else {
            return 0;
        }
    }

    /**
     * 根据工号获取所有申请
     * @param page 分页
     * @return PageVo
     */
    @Override
    public PageVo getAllApplication(Page page, HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");
            List<Purchase> purchases = directorDao.getAllApplication(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize(), employee.getWid());
            if(!CollectionUtils.isEmpty(purchases)){
                Integer count = directorDao.getAllApplicationCount(employee.getWid());
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                PageVo pageVo = new PageVo();
                pageVo.setSize(size);
                pageVo.setData(purchases);
                return pageVo;
            }else {
                return null;
            }
        }catch (Exception e){
            log.error("DirectorServiceImpl -> getAllApplication异常");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 通过员工id获取待申请的药品
     * @param request HttpServletRequest请求
     * @return List
     */
    @Override
    public List<PurchaseInfo> getAllApplicating(HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");
            List<PurchaseInfo> purchaseInfos = directorDao.getAllApplicating(employee.getWid());
            return purchaseInfos;
        }catch (Exception e){
            log.error("DirectorServiceImpl -> getAllApplication异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 更新采购明细中的信息
     * @param medName 药品名称
     * @param medNum 数量
     * @param request  HttpServletRequest请求
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer updatePurchaseInfo(String medName, String medNum, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");

            // 首先判断是否为新药
            String mid = directorDao.getMedByName(medName);

            if(StringUtils.isEmpty(mid)){// mid为空表示新药
                String sql = "update purchaseinfo set mnum=? where newMedName=? and wid=?";
                int res = jdbcTemplate.update(sql,medNum,medName,employee.getWid() );
                return res;
            }else{
                String sql = "update purchaseinfo set mnum=? where mid=? and wid=?";
                int res = jdbcTemplate.update(sql,medNum,mid,employee.getWid() );
                return res;
            }
        }catch (Exception e){
            log.error("DirectorServiceImpl -> updatePurchaseInfo异常!");
            e.getStackTrace();
            return -1;
        }
    }

    /**
     * 删除采购明细
     * @param medName 药品名称
     * @param request HttpServletRequest请求
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer delPurchaseInfo(String medName, HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");

            // 首先判断是否为新药
            String mid = directorDao.getMedByName(medName);

            if(StringUtils.isEmpty(mid)){// mid为空表示新药
                String sql = "delete from purchaseinfo where newMedName=? and wid=?";
                int res = jdbcTemplate.update(sql,medName,employee.getWid() );
                return res;
            }else{
                String sql = "delete from purchaseinfo where mid=? and wid=?";
                int res = jdbcTemplate.update(sql,mid,employee.getWid() );
                return res;
            }
        }catch (Exception e){
            log.error("DirectorServiceImpl -> delPurchaseInfo!");
            e.getStackTrace();
            return -1;
        }
    }

    /**
     * 提交申请
     * @param request HttpServletRequest请求
     * @return 结果
     */
    @Override
    @Transactional
    public Integer putPurchase(HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");
            Long time = System.currentTimeMillis();
            // 首先修改采购明细表
            String sql1 = "update purchaseinfo set pid=? WHERE wid=? and pid=''";
            int res = jdbcTemplate.update(sql1, time.toString(), employee.getWid());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(res!=0){
                //添加采购
                String sql2 = "insert into purchase(pid,isPut,isAccess,applicant,status,createTime) " +
                        "values(?,?,?,?,?,?)";
                Integer count = jdbcTemplate.update(sql2, time.toString(), 0, 0, employee.getWid(), 0, sdf.format(date));
                if(count!=0){
                    return 1;
                }else {
                    return -1;
                }
            }else{
                return -1;
            }

        }catch (Exception e){
            log.error("DirectorServiceImpl -> addPurchase异常!");
            e.getStackTrace();
            return -2;
        }
    }

    /**
     * 获取销量
     * @param request HttpServletRequest请求
     * @return List
     */
    @Override
    public List<Count> getCountOfMed(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute("employee");
        return directorDao.getCountOfMed(employee.getDepartId());
    }

    /**
     * 获取即将过期的药品
     * @param page 分页
     * @return PageVo
     */
    @Override
    public PageVo getExpireMedInfo(Page page) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Medicinal> medicinals = directorDao.getExpireMedInfo(
                (page.getPage()-1)*page.getPageSize(),page.getPageSize(), sdf.format(date));
        if(!CollectionUtils.isEmpty(medicinals)){
            Integer count = directorDao.getExpiredMedInfoCount(sdf.format(date));
            int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
            PageVo pageVo = new PageVo();
            pageVo.setSize(size);
            pageVo.setData(medicinals);
            return pageVo;
        }else{
            return null;
        }
    }

    /**
     * 下架药品
     * @param mid 药品ID
     * @param productTime 生产日期
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer drawMed(String mid, String productTime) {
        try{

            // 首先判断是否可以下架
//            List<Prescript> prescripts = directorDao.getPrescriptByMid(mid);
//            if(!CollectionUtils.isEmpty(prescripts)){
//
//                boolean flag = true;
//
//                // 判断已收费但未出药的药方中有没有该药品
//                for (Prescript prescript:prescripts
//                     ) {
//                    if ("1".equals(prescript.getIsCharge())&&"0".equals(prescript.getIsCancel())){
//                        flag = false;
//                        break;
//                    }
//                }
//                if(flag == false){
//                    return -3;
//                }else{
//                    return draw(mid, productTime);
//                }
//            }else{
//                return draw(mid, productTime);
//            }
            return draw(mid, productTime);
        }catch (Exception e){
            log.error("DirectorServiceImpl -> drawMed异常!");
            e.getStackTrace();
            return -1;
        }
    }

    /**
     * 获取医院主管id
     * @return 医院主管id
     */
    @Override
    public String getHosAdminId() {
        return directorDao.getHosAdminId();
    }

    /**
     * 获取采购id
     * @return 采购id
     */
    @Override
    public String getPurchaseId() {
        return directorDao.getPurchaseId();
    }

    /**
     * 获取出药人员ID
     * @return List
     */
    @Override
    public List<String> getPharmacyIds() {
        return directorDao.getPharmacyIds();
    }

    @Override
    public List<Medicinal> getMedByFun(String function) {
        String sql = "select * from medicinal where function like '%"+function+"%'";
        RowMapper<Medicinal> medicinalRowMapper = new BeanPropertyRowMapper<>(Medicinal.class);
        List<Medicinal> medicinals = jdbcTemplate.query(sql, medicinalRowMapper);
        return medicinals;
    }

    /**
     * 下架
     * @param mid 药品ID
     * @param productTime 生产日期
     * @return 结果
     */
    private Integer draw(String mid, String productTime){
        List<Medicinal> medicinals = directorDao.getMedByMid(mid);
        if(medicinals.size()==1){
            return -2;
        }else{
            String sql = "update medicinal set isSoldOut=1 where mid=? and productTime=?";
            Integer count = jdbcTemplate.update(sql, mid, productTime);
            return count;
        }
    }
}
