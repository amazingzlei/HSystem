package cn.xzit.service.impl;

import cn.xzit.dao.PurchaseDao;
import cn.xzit.entity.Page;
import cn.xzit.entity.PageVo;
import cn.xzit.entity.base.*;
import cn.xzit.entity.execl.PurchaseExport;
import cn.xzit.service.PurchaseService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private  static Logger log = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    /**
     * 获取采购订单
     * @param page 分页
     * @return PageVo
     */
    @Override
    public PageVo getPurchase(Page page) {
        try{
            List<Purchase> purchases =  purchaseDao.getPurchase(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize());
            if(!CollectionUtils.isEmpty(purchases)){
                Integer count = purchaseDao.getPurchaseCount();
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                PageVo pageVo = new PageVo();
                pageVo.setSize(size);
                pageVo.setData(purchases);
                return pageVo;
            }else {
                return null;
            }
        }catch (Exception e){
            log.error("PurchaseServiceImpl -> getPurchase发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 获取采购明细
     * @param page 分页
     * @param pid 采购ID
     * @return PageVo
     */
    @Override
    public PageVo getDetailInfo(Page page, String pid) {
        try{
            List<PurchaseInfo> purchaseInfos =  purchaseDao.getPurchaseInfo(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize(), pid);
            if(!CollectionUtils.isEmpty(purchaseInfos)){
                Integer count = purchaseDao.getPurchaseInfoCount(pid);
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                PageVo pageVo = new PageVo();
                pageVo.setSize(size);
                pageVo.setData(purchaseInfos);
                return pageVo;
            }else {
                return null;
            }
        }catch (Exception e){
            log.error("PurchaseServiceImpl -> getPurchase发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 获取采购明细 用于导出
     * @param pid 采购ID
     * @return List
     */
    @Override
    public List<PurchaseExport> getPurchaseExportInfo(String pid) {
        return purchaseDao.getPurchaseExportInfo(pid);
    }

    /**
     * 获取药品详情
     * @param mid 药品ID
     * @return 药品信息
     */
    @Override
    public Medicinal getDetailMed(String mid) {
        return purchaseDao.getDetailMed(mid);
    }

    /**
     * 获取仓库信息
     * @param page 分页
     * @return PageVo
     */
    @Override
    public PageVo getReperstory(Page page) {
        try{
            List<Repertory> repertories =  purchaseDao.getReperstory(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize());
            PageVo pageVo = new PageVo();
            if(!CollectionUtils.isEmpty(repertories)){
                Integer count = purchaseDao.getReperstoryCount();
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                pageVo.setSize(size);
                pageVo.setData(repertories);
                return pageVo;
            }else {
                return pageVo;
            }
        }catch (Exception e){
            log.error("PurchaseServiceImpl -> getPurchase发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 添加仓库
     * @param position 位置
     * @return 受影响的条数
     */
    @Override
    public Integer addReperstory(String position) {
        Repertory repertory = purchaseDao.getRepertoryByPosition(position);
        // 判断是否重名
        if(null == repertory){
            String sql = "INSERT INTO repertory(position,isUse) VALUES(?,0)";
            int res = jdbcTemplate.update(sql, position);
            return res;
        }else{
            return -1;
        }
    }

    /**
     * 删除仓库
     * @param rid 仓库ID
     * @return 受影响的条数
     */
    @Override
    public Integer deleteReperstory(String rid) {
        // 通过id查询仓库信息
        Repertory repertory = purchaseDao.getRepertoryByPid(rid);
        // 如果仓库正在使用则不能删除
        if(repertory.getIsUse().equals("1")){
            return -1;
        }else{
            String sql = "DELETE FROM repertory WHERE rid = ?";
            int res = jdbcTemplate.update(sql, rid);
            return res;
        }
    }

    /**
     * 更新仓库
     * @param rid 仓库ID
     * @param position 位置
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer updateReperstory(String rid, String position) {
        Repertory repertory = purchaseDao.getRepertoryByPosition(position);
        // 判断是否重名
        if(null == repertory){
            String sql = "UPDATE repertory SET position=? WHERE rid = ?";
            int res = jdbcTemplate.update(sql, position, rid);
            return res;
        }else{
            return -1;
        }
    }

    /**
     * 获取所以可用仓库
     * @return list
     */
    @Override
    public List<Repertory> getAllCanUseRepertory() {
        return purchaseDao.getAllCanUseRepertory();
    }

    /**
     * 药品入库
     * @param medicinal 药品信息
     * @param pid 采购单号
     * @return 受影响的条数
     */
    @Override
    @Transactional
    public Integer putMed(Medicinal medicinal,  String pid) {
        try {
            // 先判断生产日期是否大于当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date productTime = sdf.parse(medicinal.getProductTime());

            if(date.getTime() > productTime.getTime()){

                // 根据id、生产日期判断药品是否存在
                Medicinal med = purchaseDao.getMedByIdAndProductTime(medicinal.getMid(), medicinal.getProductTime());

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                medicinal.setAddTime(sdf1.format(date));// 设置添加时间
                medicinal.setUpdateTime(sdf1.format(date));// 设置更新时间
                medicinal.setTotalCount(medicinal.getRepertoryLeft());// 设置总数量

                String product= medicinal.getProductTime();
                product.replaceAll("/", "-");

                // 处理过期时间的计算
                String[] dateDivide = "2019-04-03".toString().split("-");
                int year = Integer.parseInt(dateDivide[0].trim());//去掉空格
                int month = Integer.parseInt(dateDivide[1].trim());
                int day = Integer.parseInt(dateDivide[2].trim());
                Calendar c = Calendar.getInstance();//获取一个日历实例
                c.set(year, month - 1, day);//设定日历的日期
                c.add(Calendar.MONDAY, Integer.parseInt(medicinal.getSaveTime()));

                medicinal.setEndTime(sdf.format(c.getTime()));// 设置过期时间

                if(null == med){
                    // 药品不存在 则进行插入
                    String insert = "INSERT INTO medicinal(mid,name,function,shellPrice,bidPrice,counterLeft,repertoryId,repertoryLeft,totalCount,isSoldOut,productTime,saveTime,addTime,endTime,updateTime)" +
                            "VALUES(:mid,:name,:function,:shellPrice,:bidPrice,0,:repertoryId,:totalCount,:totalCount,0,:productTime,:saveTime,:addTime,:endTime,:updateTime)";
                    SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(medicinal);
                    int InsertRes = namedParameterJdbcTemplate.update(insert, sqlParameterSource);

                    // 更新仓库
                    String updateRep = "UPDATE repertory set isUse = 1 where rid = ?";
                    int updateRes = jdbcTemplate.update(updateRep, medicinal.getRepertoryId());

                    String updatePurchaseInfo = "UPDATE purchaseInfo set isPut = 1 where pid=? and (mid = ? or newMedName = ?)";
                    int updatePurchaseInfoRes = jdbcTemplate.update(updatePurchaseInfo, pid, medicinal.getMid(), medicinal.getName());
                    if(InsertRes+updateRes+updatePurchaseInfoRes == 3){
                        return 1;
                    }else{
                        return 0;
                    }
                }else{
                    // 更新
                    String update = "UPDATE medicinal set repertoryLeft=repertoryLeft+?,totalCount=totalCount+? where mid=? and productTime=?";
                    int updateRes = jdbcTemplate.update(update, medicinal.getRepertoryLeft(),medicinal.getRepertoryLeft(),medicinal.getMid(),
                            medicinal.getProductTime());
                    String updatePurchaseInfo = "UPDATE purchaseInfo set isPut = 1 where pid=? and (mid = ? or newMedName = ?)";
                    int updatePurchaseInfoRes = jdbcTemplate.update(updatePurchaseInfo, pid, medicinal.getMid(), medicinal.getName());
                    if(updateRes+updatePurchaseInfoRes == 2){
                        return 2;
                    }else{
                        return 0;
                    }
                }
            }else{
                // 生产日期不合格
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // 异常
            return -2;
        }
    }

    /**
     * 采购单入库更新
     * @param pid 采购单号
     * @return 受影响的条数
     */
    @Override
    public Integer putPurchase(String pid, HttpServletRequest request) {
        // 获取员工id
        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute("employee");
        Integer count = purchaseDao.getCountOfIsNotPut(pid);
        if(count == 0){
            String update = "UPDATE purchase set wid=? ,isPut=1, status=3 WHERE pid=?";
            int res = jdbcTemplate.update(update, employee.getWid(),pid);
            return res;
        }else{
            return -1;
        }
    }

    /**
     * 获取下架列表
     * @param page 分页
     * @return PageVo
     */
    @Override
    public PageVo getDrawMed(Page page) {
        try{
            List<Medicinal> medicinals =  purchaseDao.getDrawMed(
                    (page.getPage()-1)*page.getPageSize(),page.getPageSize());
            PageVo pageVo = new PageVo();
            if(!CollectionUtils.isEmpty(medicinals)){
                Integer count = purchaseDao.getDrawMedCount();
                int size = (count%page.getPageSize())==0?count/page.getPageSize():count/page.getPageSize()+1;
                pageVo.setSize(size);
                pageVo.setData(medicinals);
                return pageVo;
            }else {
                return pageVo;
            }
        }catch (Exception e){
            log.error("PurchaseServiceImpl -> getPurchase发生异常!");
            e.getStackTrace();
            return null;
        }
    }

    /**
     * 清仓
     * @param mid 药品ID
     * @param repertoryId 仓库ID
     * @param productTime 生产日期
     * @return 受影响的条数
     */
    @Override
    public Integer cleanRepertory(String mid, String repertoryId, String productTime) {
        try{
            // 清仓
            String cleanRep = "UPDATE medicinal set repertoryLeft = 0 WHERE mid=? and productTime = ?";
            int cleanRepRes = jdbcTemplate.update(cleanRep, mid, productTime);
            // 仓库致0
            String updateRep = "UPDATE repertory set isUse = 0 WHERE rid = ?";
            int updateRepRes = jdbcTemplate.update(updateRep, repertoryId);
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


}
