package cn.xzit.entity.base;

import lombok.Data;

/**
 * 药方表
 */
@Data
public class Prescript {
    private String pid ;//药方id
    private String isCharge ;//是否收费
    private String isShell ;//是否出库
    private String isCancel ;//是否作废
    private String totalPrice ;//总价
    private String typeId;//科室id
    private String chargeTime ;//收费日期
    private String createTime;//创建时间
    private String shellTime ;//出口日期
}
