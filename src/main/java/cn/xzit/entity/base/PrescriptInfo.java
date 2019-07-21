package cn.xzit.entity.base;

import lombok.Data;

/**
 * 药方明细
 */
@Data
public class PrescriptInfo {
    private String pid ;//id
    private String mid ;//药品id
    private String mnum ;//药品数量
    private String mprice;//单价
    private String total;//总价
}
