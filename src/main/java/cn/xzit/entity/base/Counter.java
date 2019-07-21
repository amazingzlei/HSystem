package cn.xzit.entity.base;

import lombok.Data;

/**
 * 药台表
 */
@Data
public class Counter {
    private String cid ;//药台id
    private String position ;//药台位置
    private String isUse;//是否使用
}
