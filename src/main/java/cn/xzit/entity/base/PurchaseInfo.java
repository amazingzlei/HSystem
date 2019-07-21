package cn.xzit.entity.base;

import lombok.Data;

/**
 * 采购明细表
 */
@Data
public class PurchaseInfo {
    private String pid ;//id
    private String mid ;//药品id
    private String mnum ;//数量
    private String wid;// 工号
    private String isPut;//是否入库
    private String newMedName;// 新药名称
}
