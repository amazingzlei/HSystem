<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>科室管理 </title>
    <style>
        .toast-center-center {
            top: 50%;
            left: 50%;
            margin-top: -25px;
            margin-left: -150px;
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
    <link href="<%=path%>/css/toastr.css" rel="stylesheet" type="text/css">
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
</head>

<body>

<div id="wrapper">

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
            <a class="navbar-brand">科室管理 </a>
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
                    <%--<li class="sidebar-search">--%>
                        <%--<div class="input-group custom-search-form">--%>
                            <%--<input type="text" class="form-control" id="searchContent" placeholder="请输入工号...">--%>
                            <%--<span class="input-group-btn">--%>
								<%--<button class="btn btn-default" type="button" onclick="searchEmployee('<%=path%>')">--%>
									<%--<i class="fa fa-search" style="padding: 3px 0 3px 0;"></i>--%>
								<%--</button>--%>
							<%--</span>--%>
                        <%--</div> <!-- /input-group -->--%>
                    <%--</li>--%>
                    <li><a href="<%=path%>/manageEmployeeInfo/listAllEmployee"><i
                            class="fa fa-edit fa-fw"></i> 人员管理</a></li>
                    <li><a href="<%=path%>/manageEmployeeInfo/insertEmployee"><i
                            class="fa fa-dashboard fa-fw"></i> 添加人员</a></li>
                    <li><a href="<%=path%>/manageEmployeeInfo/manageDepart" class="active"><i
                            class="fa fa-edit fa-fw"></i> 科室管理</a></li>
                    <li><a href="<%=path%>/manageEmployeeInfo/insertDepart"><i
                            class="fa fa-dashboard fa-fw"></i> 添加科室</a></li>
                </ul>
            </div>
        </div>
        <!-- /.navbar-static-side --> </nav>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">科室管理</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading">科室信息列表</div>
                    <!-- /.panel-heading -->
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th>科室号</th>
                            <th>科室名称</th>
                            <th>操作</th>
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

<!-- 人员信息编辑对话框 -->
<div class="modal fade" id="customerEditDialog" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改科室信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="edit_employee_form" action="<%=path%>/manageEmployeeInfo/updateDepartInfo">
                    <input type="hidden" id="edit_wid" name="id"/>
                    <div class="form-group">
                        <label for="edit_name" class="col-sm-2 control-label">科室名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_name" placeholder="科室名称"
                                   name="name">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="updateDepart()">保存修改</button>
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

<script src="<%=basePath%>js/jquery.min.js"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/metisMenu.min.js"></script>
<script src="<%=basePath%>js/jquery.dataTables.min.js"></script>
<script src="<%=basePath%>js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/jq/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/public/toastr.min.js"></script>
<%--<script src="<%=basePath%>js/sb-admin-2.js"></script>--%>

<script type="text/javascript" src="<%=path%>/js/manager/manage.js"></script>
<script type="text/javascript" src="<%=path%>/js/bootstrap/bootstrap-paginator.js"></script>

<script type="text/javascript">

    function clean() {
        $("#edit_password_form input:gt(0)").val("");
    }

    // 获取需要编辑科室信息
    function editDepart(wid) {
        $.ajax({
            type: "get",
            url: "<%=path%>/manageEmployeeInfo/getDepartById",
            dataType:"json",
            data: {"id": wid},
            success: function (data) {
                var depart = data.data;
                $("#edit_name").val(depart.name);
                $("#edit_wid").val(depart.id);
            }
        });
    }

    // 更新科室信息
    function updateDepart() {
        if(isNameLegal($("#edit_name").val())){
            $.post("<%=basePath%>/manageEmployeeInfo/updateDepart", $("#edit_employee_form").serialize(), function (data) {
                if(data.code == 200){
                    toastr.success('科室信息更新成功！');
                    // alert("科室信息更新成功！");
                    // window.location.reload();
                    setTimeout(function(){ window.location.reload(); }, 1000)
                }else{
                    toastr.error(data.msg);
                }
            },'json');
        }
    }

    //删除员工信息
    function deleteDepart(id) {
        if (confirm('确实要删除该科室吗?')) {
            $.post("<%=basePath%>/manageEmployeeInfo/deleteDepart", {"id": id}, function (data) {
                if(data.code===200){
                    toastr.success('删除成功!');
                    // alert("删除成功！");
                    // window.location.reload();
                    setTimeout(function(){ window.location.reload(); }, 1000)
                }else{
                    toastr.error(data.msg);
                }
            });
        }

    }

    //展示分页数据
    function showPageDepartData(data) {
        var html = "<tr>";
        html += "<td>"+data.id+"</td>";
        html += "<td>"+data.name+"</td>";
        html += "<td><a href='#' class='btn btn-primary btn-xs' data-toggle='modal' data-target='#customerEditDialog' onclick='editDepart(&apos;"+data.id+"&apos;)'>修改</a>";
        html += "<a href='#' class='btn btn-danger btn-xs' onclick='deleteDepart(&apos;"+data.id+"&apos;)'>删除</a></td></tr>";
        $("tbody").append(html);
    }

    //分页
    $(function () {
        $.ajax(
            {
                url:'<%=path%>/manageEmployeeInfo/getPageDepart',
                type:'POST',
                data:{'page':1,'pageSize':10},
                dataType:'JSON',
                success:function (data) {
                    var page_count = data.data.size;
                    var list = data.data.data;
                    $('tbody').empty();
                    for(var i=0;i<list.length;i++){
                        showPageDepartData(list[i]);
                    }

                    $('#last_page').text(page_count);
                    $('#pageLimit').bootstrapPaginator({
                        currentPage: 1,//当前请求页
                        totalPages: page_count,//一共多少页
                        size: "normal",//应该是页眉的大小。
                        bootstrapMajorVersion: 3,//bootstrap的版本要求。
                        alignment: "right",
                        numberOfPages: 10,//一页列出多少数据。
                        itemTexts: function (type, page, current) {
                            switch (type) {
                                case "first":
                                    return "首页";
                                case "prev":
                                    return "上一页";
                                case "next":
                                    return "下一页";
                                case "last":
                                    return "末页";
                                case "page":
                                    return page;
                            }//默认显示的是第一页。
                        },
                        onPageClicked: function (event, originalEvent, type, page) {//给每个页眉绑定一个事件，其实就是ajax请求，其中page变量为当前点击的页上的数字。
                            $.ajax({
                                url: '<%=path%>/manageEmployeeInfo/getPageDepart',
                                type: 'POST',
                                data: {'page': page, 'pageSize': 10},
                                dataType: 'JSON',
                                success: function (data) {
                                    $('tbody').empty();
                                    var page_count = data.data.size;
                                    var list = data.data.data;
                                    for(var i=0;i<list.length;i++){
                                        showPageDepartData(list[i]);
                                    }
                                    $('#last_page').text(page_count)
                                }
                            })
                        }
                    });
                }
            }
        );

    });
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

            "timeOut": "8640000", //展现时间

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
