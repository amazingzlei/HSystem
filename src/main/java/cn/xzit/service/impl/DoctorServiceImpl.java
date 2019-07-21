package cn.xzit.service.impl;

import cn.xzit.dao.DoctorDao;
import cn.xzit.entity.base.Employee;
import cn.xzit.entity.base.Medicinal;
import cn.xzit.entity.base.Prescript;
import cn.xzit.entity.base.PrescriptInfo;
import cn.xzit.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DoctorDao doctorDao;

    private  static Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    /**
     * 获取所有药品名称
     * @return 集合
     */
    @Override
    public List<Medicinal> getAllMedName() {
        try{
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String endTime = sdf.format(date);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select DISTINCT name from medicinal " +
                    "where repertoryLeft > 0 and isSoldOut = 0 and endTime > ");
            stringBuffer.append(endTime);
            RowMapper rowMapper = new BeanPropertyRowMapper(Medicinal.class);
            List<Medicinal> medicinals = jdbcTemplate.query(stringBuffer.toString(),rowMapper);
            return medicinals;
        }catch (Exception e){
            log.error("DoctorServiceImpl -> getAllMedName发生异常:");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据药名查询所有库存
     * @param name 药名
     * @return 数量
     */
    private Integer selectCountByMedName(String name) {
        return doctorDao.selectCountByMedName(name);
    }

    /***
     * 添加药方表
     * @param medNames 药名
     * @param medNums 数量
     * @param request HttpServletRequest
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addPrescript(String medNames, String medNums, HttpServletRequest request) {
        try{
            String[] names = medNames.split(",");
            String[] nums = medNums.split(",");
            String pid = String.valueOf(System.currentTimeMillis());
            Double totalPrice = 0.0;
            for (int i = 0; i < names.length; i++) {

                // 查询最早过期的药品
                Medicinal medicinal = doctorDao.selectMedByName(names[i]);

                // 查询总库存
                Integer countLeft = doctorDao.selectCountByMedName(names[i]);

                // 判断需要的数量是否大于库存
                if(Integer.parseInt(nums[i])>countLeft){
                    return names[i]+"数量不足!";
                }else{
                    PrescriptInfo prescriptInfo = new PrescriptInfo();
                    prescriptInfo.setMid(medicinal.getMid());
                    prescriptInfo.setPid(pid);
                    prescriptInfo.setMnum(nums[i]);
                    prescriptInfo.setMprice(medicinal.getShellPrice());
                    Double total = Integer.parseInt(nums[i])*Double.parseDouble(medicinal.getShellPrice());
                    totalPrice += total;
                    prescriptInfo.setTotal(total.toString());
                    // 添加药方明细表
                    Integer res = doctorDao.insertPrescriptInfo(prescriptInfo);
                    if(res != 1){
                        return "添加失败!";
                    }else{
                        // 将对应的药品数量减去
                        // 如果即将过期的药品数量不够，则需要再将最后过期的药品数量也要做修改
                        if(Integer.valueOf(nums[i])<=Integer.valueOf(medicinal.getTotalCount())){
                            doctorDao.disMed(medicinal.getName(), medicinal.getEndTime(), nums[i]);
                        }else{
                            // 先减去即将过期的
                            doctorDao.disMed(medicinal.getName(), medicinal.getEndTime(), medicinal.getTotalCount());
                            // 再减去晚过期的
                            String maxEndTime = doctorDao.selectLateMed(names[i]);
                            doctorDao.disMed(medicinal.getName(), maxEndTime, String.valueOf(
                                    Integer.valueOf(nums[i])-Integer.valueOf(medicinal.getTotalCount())
                            ));
                        }
                    }
                }
            }
            // 添加药方表
            Prescript prescript = new Prescript();
            prescript.setPid(pid);
            HttpSession session = request.getSession();
            Employee employee = (Employee) session.getAttribute("employee");
            prescript.setTypeId(employee.getDepartId());
            prescript.setTotalPrice(totalPrice.toString());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            prescript.setCreateTime(sdf.format(date));
            Integer res = doctorDao.insertPrescript(prescript);
            if(res != 1){
                return "添加失败!";
            }else {
                return "添加成功!";
            }
        }catch (Exception e){
            log.error("DoctorServiceImpl -> addPrescript发生异常:");
            e.printStackTrace();
            return "添加失败!";
        }
    }

}
