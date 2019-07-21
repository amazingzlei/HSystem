package cn.xzit.entity.execl;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class PrescriptExport {
    @Excel(name = "药品编号",  orderNum = "0")
    private String mid ;
    @Excel(name = "药品名称",  orderNum = "1")
    private String newMedName ;
    @Excel(name = "药台位置",  orderNum = "2")
    private String counterPosition ;
    @Excel(name = "仓库位置",  orderNum = "3")
    private String repertoryPosition ;
    @Excel(name = "数量",  orderNum = "4")
    private String mnum ;

    public PrescriptExport(String mid, String newMedName, String counterPosition, String repertoryPosition, String mnum) {
        this.mid = mid;
        this.newMedName = newMedName;
        this.counterPosition = counterPosition;
        this.repertoryPosition = repertoryPosition;
        this.mnum = mnum;
    }
}
