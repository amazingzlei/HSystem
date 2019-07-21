package cn.xzit.entity.execl;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class PurchaseExport {
    @Excel(name = "药品编号",  orderNum = "0")
    private String mid ;
    @Excel(name = "药品名称",  orderNum = "1")
    private String newMedName ;
    @Excel(name = "数量",  orderNum = "2")
    private String mnum ;
}
