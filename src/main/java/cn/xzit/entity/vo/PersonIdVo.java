package cn.xzit.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class PersonIdVo {
    private String purchaseId;
    private List<String> pharmacyId;
}
