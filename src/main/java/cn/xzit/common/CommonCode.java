package cn.xzit.common;

public class CommonCode {
    public static final String ADMIN = "0";
    public static final String DOCOR = "1";
    public static final String SUPERVISOR = "2";
    public static final String PURCHASE = "3";
    public static final String PHARMACY = "4";
    public static final String DIRECTOR = "5";
    public static final String CHARGE = "6";
    public static final String NO_SUCH_INFO = "未查询到信息!";
    public static final String ADD_FAIL = "添加失败!";
    public static final String UPDATE_FAIL = "更新失败!";
    public static final String DELETE_FAIL = "删除失败!";
    public static final String DO_NOT_HAVE_SUCH_INFO = "暂无记录!";
    public static final String ERROR = "系统异常,请联系管理员!";
    public static final String NOINFO = "请输入仓库位置!";
    public static final String NOCOUNTERINFO = "请输入药台位置!";
    public static final String HASPOSITION = "仓库位置重名!";
    public static final String COUNTERHASPOSITION = "药台位置重名!";
    public static final String CANDELTEISUSE = "不能删除正在使用的仓库!";
    public static final String CANCOUNTERDELTEISUSE = "不能删除正在使用的药台!";
    public static final String ERRORPRODUCTTIME = "生产日期不能大于当期日期!";
    public static final String PUTERROR = "入库失败!";
    public static final String MEDNOTPUT = "还有药品未入库!";
    public static final String ONLY_ADMIN = "只能有一个管理员!";
    public static final String ONLY_SUPERVISOR = "只能有一个医院主管!";
    public static final String ONLY_PURCHASE = "只能有一个采购人员!";
    public static final String ONLY_DIRECTOR = "该科室只能有一个科室主任!";
    public static final String HAS_SHELLOUT = "该药方已经出库!";
    public static final String ONLY_ONE_MED = "下架后将无该药品!请先采购!";
    public static final String ERROR_COUNTER = "错误药台位置!";
    public static final String HASDEPART = "科室已存在!";
    public static final String HASEMPLOYEE = "该科室还有工作人员,不能删除!";
    public static final String MEDINPRESCRIPT = "该药品存在已付费的药方中,请联系出药人员!";
    public static final String HASDRAWMED = "存在下架药品不能出库，请作废该药方!";
}
