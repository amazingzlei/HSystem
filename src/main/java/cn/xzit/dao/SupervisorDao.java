package cn.xzit.dao;

import cn.xzit.entity.base.Purchase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SupervisorDao {

    List<Purchase> getReqApplicatePur(@Param("current") Integer current, @Param("limit") Integer limit);

    Integer getReqApplicatePurCount();
}
