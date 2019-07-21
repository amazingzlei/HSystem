package cn.xzit.entity.base;

import lombok.Data;

@Data
public class Medicinal {
    private String mid ;//药品id
    private String name ;//药品名称
    private String function ;//药品功能
    private String shellPrice ;//售价
    private String bidPrice ;//进价
    private String counterId ;//药台id
    private String counterLeft ;//药台库存
    private String repertoryLeft ;//仓库库存
    private String repertoryId ;//仓库id
    private String isSoldOut;//是否下架
    private String productTime ;//生产日期
    private String saveTime ;//保质期
    private String addTime ;//上架时间
    private String endTime;//到期时间
    private String updateTime ;//更新时间
    private String maxEndTime;// 最大过期时间
    private String totalCount;// 总库存
}
