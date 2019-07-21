package cn.xzit.entity.base;

import lombok.Data;

/**
 * 采购表
 */
@Data
public class Purchase {
    private String pid ;//id
    private String wId ;//采购人
    private String isPut ;//是否入库
    private String isAccess ;//是否审核
    private String assessor ;//审核人
    private String assessorName ;//审核人姓名
    private String applicant ;//申请人
    private String applicantName ;//申请人姓名
    private String status;//状态
    private String createTime ;//采购时间
}
