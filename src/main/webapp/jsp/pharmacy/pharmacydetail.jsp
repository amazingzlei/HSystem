<%@ page import="cn.xzit.entity.base.Employee" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    Integer wid = ((Employee) request.getSession().getAttribute("employee")).getWid();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>出药人员端 </title>
    <style>
        .toast-center-center {
            top: 50%;
            left: 50%;
            margin-top: -25px;
            margin-left: -150px;
        }
        #medNum-error,#newMedNum-error{
            color:red;
            font-weight: normal;
            margin-left: 10px;
        }
        table{
            text-align: center;
        }
    </style>
    <link href="<%=path%>/images/title.ico" rel="SHORTCUT ICON" />
    <link href="<%=basePath%>css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=basePath%>css/metisMenu.min.css" rel="stylesheet">
    <link href="<%=basePath%>css/dataTables.bootstrap.css" rel="stylesheet">
    <link href="<%=basePath%>css/sb-admin-2.css" rel="stylesheet">
    <link href="<%=basePath%>css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="<%=basePath%>css/boot-crm.css" rel="stylesheet" type="text/css">
    <link href="<%=path%>/css/bootstrap.css" rel="stylesheet" type="text/css">
    <%--<link href="<%=path%>/css/director/director.css" rel="stylesheet" type="text/css">--%>
    <link href="<%=path%>/css/sweetalert/sweetalert.css" rel="stylesheet" type="text/css"/>
    <%--引入toastr插件，用于消息框弹出--%>
    <link href="<%=path%>/css/toastr.css" rel="stylesheet" type="text/css">
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
</head>

<body>

<!-- 药品信息编辑对话框 -->
<div class="modal fade" id="customerEditDialog" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">编辑信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="edit_medicial_form">
                    <%--<input type="hidden" id="edit_wid" name="wid"/>--%>
                    <div class="form-group">
                        <label for="edit_mid" class="col-sm-2 control-label">药品编号</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_mid" placeholder="药品编号"
                                   name="mid" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_name" class="col-sm-2 control-label">名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_name" placeholder="名称"
                                   name="name" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_function" class="col-sm-2 control-label">功能</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_function" placeholder="功能"
                                   name="function" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_shellPrice" class="col-sm-2 control-label">售价</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_shellPrice" placeholder="售价"
                                   name="shellPrice" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_bidPrice" class="col-sm-2 control-label">进价</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_bidPrice" placeholder="进价"
                                   name="bidPrice" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_repertoryId" class="col-sm-2 control-label">库房位置</label>
                        <div class="col-sm-10">
                            <select type="text" class="form-control" id="edit_repertoryId"
                                    name="repertoryId"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_repertoryLeft" class="col-sm-2 control-label">数量</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_repertoryLeft" placeholder="数量"
                                   name="repertoryLeft" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_productTime" class="col-sm-2 control-label">生产日期</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="edit_productTime" placeholder="生产日期"
                                   name="productTime">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_saveTime" class="col-sm-2 control-label">保质期</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_saveTime" placeholder="保质期"
                                   name="saveTime">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="put()">入库</button>
            </div>
        </div>
    </div>
</div>

<div id="wrapper">
    <input type="hidden" value="${data}" id="hiddenData"/>
    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation"
         style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span> <span
                    class="icon-bar"></span> <span class="icon-bar"></span> <span
                    class="icon-bar"></span>
            </button>
            <a class="navbar-brand">出药人员 </a>
        </div>
        <!-- /.navbar-header -->

        <ul class="nav navbar-top-links navbar-right">
            <li class="dropdown"><a class="dropdown-toggle"
                                    data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
                <i class="fa fa-caret-down"></i>
            </a>
                <ul class="dropdown-menu dropdown-user">

                    <li><a href="#"><i class="fa fa-user fa-fw"></i> 工号:${sessionScope.get("employee").wid}</a></li>
                    <li><a href="#"><i class="fa fa-user fa-fw"></i> 姓名:${sessionScope.get("employee").name}</a></li>
                    <li><a href="#" data-toggle="modal" data-target="#updatePassword" onclick="clean()"><i class="fa fa-user fa-fw"></i> 修改密码</a></li>
                    <li class="divider"></li>
                    <li><a href="<%=path%>/userLogin/logout"><i class="fa fa-sign-out fa-fw"></i>
                        退出登录</a></li>
                </ul> <!-- /.dropdown-user --></li>
        </ul>
        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <li><a href="<%=path%>/pharmacyInfo/getView/pharmacy" class="active"><i
                            class="fa fa-edit fa-fw"></i> 药品出库</a></li>
                    <li><a href="<%=path%>/pharmacyInfo/getView/counter"><i
                            class="fa fa-edit fa-fw"></i> 仓库添加</a></li>
                    <li><a href="<%=path%>/pharmacyInfo/getView/cleancounter"><i
                            class="fa fa-edit fa-fw"></i> 清空药台</a></li>
                    <li><a href="<%=path%>/pharmacyInfo/getView/cancellation"><i
                            class="fa fa-edit fa-fw"></i> 药方作废</a></li>
                    <li><a href="<%=path%>/pharmacyInfo/getView/addmed"><i
                            class="fa fa-edit fa-fw"></i> 药台补药</a></li>
                </ul>
            </div>
        </div>
        <!-- /.navbar-static-side --> </nav>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <%--<button type="button" class="btn btn-primary" onclick="getExpireMed(this)">查看待审核</button>--%>
                <%--<button type="button" class="btn btn-info" onclick="getBlowCount(this)">查看已审核</button>--%>
                <h1 class="page-header">药方明细</h1>

            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        药方明细列表
                        <%--<a href="<%=path%>/purchaseInfo/purchaseExport?pid=${data}" class="btn btn-primary" style="margin-left: 65%;font-size: 12px">返回</a>--%>
                        <div style="display: inline-block;margin-left: 90%">
                            <a href="<%=path%>/pharmacyInfo/getView/pharmacy" class="btn btn-danger" style="font-size: 12px">返回</a>
                            <a href="javascript:;" class="btn btn-primary" style="font-size: 12px" onclick="shellOut()">出库</a>
                            <%--<a href="javascript:;" class="btn btn-info" style="font-size: 12px" onclick="exportExcel()">导出</a>--%>
                        </div>
                    </div>

                    <!-- /.panel-heading -->
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th>药方ID</th>
                            <th>药品ID</th>
                            <th>药品名称</th>
                            <th>生产日期</th>
                            <th>药台ID</th>
                            <th>药台库存</th>
                            <th>仓库ID</th>
                            <th>仓库库存</th>
                            <th>数量</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    <div id="example" style="text-align: center">
                        <ul id="pageLimit"></ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<%--修改密码编辑框--%>
<div class="modal fade" id="updatePassword" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="changePassword">修改密码</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="edit_password_form">
                    <input type="hidden" id="edit__password_wid" name="wid" value="${sessionScope.get("employee").wid}"/>
                    <div class="form-group">
                        <label for="edit_old_password" class="col-sm-2 control-label">原始密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="edit_old_password" placeholder="原始密码"
                                   name="oldPassword">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_new_password" class="col-sm-2 control-label">新密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="edit_new_password" placeholder="新密码"
                                   name="newPassword">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_confirm_password" class="col-sm-2 control-label">确认密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="edit_confirm_password" placeholder="确认密码">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="updatePassword()">修改密码</button>
            </div>
        </div>
    </div>
</div>
<div class="footer text-center small">
    @徐州工程学院2019级毕业设计 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;作者:zenglei &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;邮箱:amazingzlei@163.com
</div>

<script type="text/javascript" src="<%=path%>/js/jq/jquery-2.0.3.min.js"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/metisMenu.min.js"></script>
<script src="<%=basePath%>js/jquery.dataTables.min.js"></script>
<script src="<%=basePath%>js/dataTables.bootstrap.min.js"></script>
<%--<script src="<%=basePath%>js/sb-admin-2.js"></script>--%>

<script type="text/javascript" src="<%=path%>/js/manager/manage.js"></script>
<script type="text/javascript" src="<%=path%>/js/pharmacy/pharmacydetail.js"></script>
<%--<script type="text/javascript" src="<%=path%>/js/supervisor/supervisor.js"></script>--%>
<%--<script type="text/javascript" src="<%=path%>/js/director/draw.js"></script>--%>
<script type="text/javascript" src="<%=path%>/js/public/toastr.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/bootstrap/bootstrap-paginator.js"></script>
<script type="text/javascript" src="<%=path%>/js/sweetalert/sweetalert.js"></script>
<script type="text/javascript">
    var urlPath = "<%=path%>";
    var wid = "<%=wid%>";
    var pid ;
</script>
<script type="text/javascript">
    <%--初始化toastr--%>
    $(function () {
        toastr.options = {

            "closeButton": true, //是否显示关闭按钮

            "debug": false, //是否使用debug模式

            "positionClass": "toast-center-center",//弹出窗的位置

            "showDuration": "300",//显示的动画时间

            "hideDuration": "1000",//消失的动画时间

            "timeOut": "86400000", //展现时间

            "extendedTimeOut": "1000",//加长展示时间

            "showEasing": "swing",//显示时的动画缓冲方式

            "hideEasing": "linear",//消失时的动画缓冲方式

            "showMethod": "fadeIn",//显示时的动画方式

            "hideMethod": "fadeOut" //消失时的动画方式

        };
    })
</script>

</body>

</html>
