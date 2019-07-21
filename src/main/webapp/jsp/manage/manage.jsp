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
    <title>人员管理 </title>
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
    <%--引入toastr插件，用于消息框弹出--%>
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
            <a class="navbar-brand">人员管理 </a>
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
                    <li class="sidebar-search">
                        <div class="input-group custom-search-form">
                            <input type="text" class="form-control" id="searchContent" placeholder="请输入工号...">
                            <span class="input-group-btn">
								<button class="btn btn-default" type="button" onclick="searchEmployee('<%=path%>')">
									<i class="fa fa-search" style="padding: 3px 0 3px 0;"></i>
								</button>
							</span>
                        </div> <!-- /input-group -->
                    </li>
                    <li><a href="<%=path%>/manageEmployeeInfo/listAllEmployee" class="active"><i
                            class="fa fa-edit fa-fw"></i> 人员管理</a></li>
                    <li><a href="<%=path%>/manageEmployeeInfo/insertEmployee"><i
                            class="fa fa-dashboard fa-fw"></i> 添加人员</a></li>
                    <li><a href="<%=path%>/manageEmployeeInfo/manageDepart"><i
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
                <h1 class="page-header">人员管理</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading">人员信息列表</div>
                    <!-- /.panel-heading -->
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th>工号</th>
                            <th>姓名</th>
                            <th>性别</th>
                            <th>年龄</th>
                            <th>电话</th>
                            <th>类型</th>
                            <th>密码</th>
                            <th>科室</th>
                            <th>创建时间</th>
                            <th>更新时间</th>
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
                <h4 class="modal-title" id="myModalLabel">修改个人信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="edit_employee_form" action="<%=path%>/manageEmployeeInfo/updateEmployeeInfo">
                    <input type="hidden" id="edit_wid" name="wid"/>
                    <div class="form-group">
                        <label for="edit_name" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_name" placeholder="姓名"
                                   name="name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">性别</label>
                        <div class="col-sm-10">
                            <input type="radio"  value="男" id="male"
                                   name="gender"><label for="male" >男</label>
                            <input type="radio"  value="女" id="famale"
                                   name="gender"><label for="famale" >女</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_age" class="col-sm-2 control-label">年龄</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_age" placeholder="年龄"
                                   name="age">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_phone" class="col-sm-2 control-label">电话</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_phone" placeholder="电话"
                                   name="phone">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_password" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="edit_password" placeholder="密码"
                                   name="password">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit_type" class="col-sm-2 control-label">类型</label>
                        <div class="col-sm-10">
                            <input name="type" type="hidden" id="employe_type"/>
                            <select class="form-control" name="type" id="edit_type" onchange="isDepartMentShow()" disabled>
                                <%--<option value="0">管理员</option>--%>
                                <%--<option value="1">医生</option>--%>
                                <%--<option value="2">医院主管</option>--%>
                                <%--<option value="3">采购人员</option>--%>
                                <%--<option value="4">药房出药人员</option>--%>
                                <%--<option value="5">科室主任</option>--%>
                                <%--<option value="6">药房收费人员</option>--%>
                            </select>
                        </div>
                    </div>
                    <div class="form-group" style="display: none" id="department">
                        <label for="edit_type" class="col-sm-2 control-label">科室</label>
                        <div class="col-sm-10">
                            <input name="departId" type="hidden" id="employe_departId"/>
                            <select class="form-control"  id="depart_type" disabled>

                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="updateEmployee()">保存修改</button>
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
<%--<script src="<%=basePath%>js/sb-admin-2.js"></script>--%>

<script type="text/javascript" src="<%=path%>/js/manager/manage.js"></script>
<script type="text/javascript" src="<%=path%>/js/jq/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/public/toastr.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/bootstrap/bootstrap-paginator.js"></script>

<script type="text/javascript">

    // 编辑员工信息
    function editCustomer(wid) {
        $.ajax({
            type: "post",
            url: "<%=path%>/manageEmployeeInfo/getEmployeeByWid",
            dataType:"json",
            data: {"wid": wid},
            success: function (data) {
                var employee = data.employee;
                $("#edit_name").val(employee.name);
                $("#edit_wid").val(employee.wid);
                // $("#edit_gender").val(employee.gender)
                if(employee.gender==="男"){
                    $("input[value='男']").attr("checked","checked");
                }else{
                    $("input[value='女']").attr("checked","checked");
                }
                $("#edit_age").val(employee.age);
                $("#edit_phone").val(employee.phone);
                $("#edit_type").val(employee.type);
                $("#employe_type").val(employee.type);
                if(employee.type==1 || employee.type==5){
                    $("#department").css("display","block");
                    $("#employe_departId").val(employee.departId)
                }else{
                    $("#department").css("display","none");
                }
                $("#edit_password").val(employee.password);
                //获取所有部门信息
                $.ajax({
                    url:"<%=path%>/manageEmployeeInfo/getAllDepart",
                    type:"GET",
                    dataType:"json",
                    success:function (data) {
                        $("#depart_type").empty();
                        var list = data.data;
                        for(var i=0;i<list.length;i++){
                            var html = "<option value="+list[i].id+">"+list[i].name+"</option>";
                            $("#depart_type").append(html);
                        }
                        if(employee.departId!=null){
                            $("#depart_type").val(employee.departId);
                        }
                    }
                });
            }
        });
    }

    // 更新员工信息
    function updateEmployee() {
        if(isAgeLegal()&&isPoneAvailable($("#edit_phone").val())&&
            isPasswordLegal($("#edit_password").val())&&isNameLegal($("#edit_name").val())){
            $.post("<%=basePath%>/manageEmployeeInfo/updateEmployee", $("#edit_employee_form").serialize(), function (data) {
                if(data.result == 1){
                    toastr.success('员工信息更新成功！');
                    // alert("员工信息更新成功！");
                    setTimeout(function(){ window.location.reload(); }, 1000)
                    // window.location.reload();
                }else{
                    toastr.error('员工信息更新失败!');
                    // alert("员工信息更新失败!")
                }
            },'json');
        }
    }

    //删除员工信息
    function deleteCustomer(id) {
        if(id==="10001"){
            toastr.error('管理员不能删除!');
        }else {
            if (confirm('确实要删除该员工吗?')) {
                $.post("<%=basePath%>/manageEmployeeInfo/deleteEmployee", {"wid": id}, function (data) {
                    if(data.result===1){
                        toastr.success('员工删除成功！');
                        // alert("员工删除成功！");
                        // window.location.reload();
                        setTimeout(function(){ window.location.reload(); }, 1000)
                    }else{
                        toastr.error('员工删除失败！');
                        // alert("员工删除失败！");
                        // window.location.reload();
                        setTimeout(function(){ window.location.reload(); }, 1000)
                    }
                });
            }
        }
    }

    //展示分页数据
    function showPageEmployeeData(data) {
        var html = "<tr>";
        html += "<td>"+data.wid+"</td>";
        html += "<td>"+data.name+"</td>";
        html += "<td>"+data.gender+"</td>";
        html += "<td>"+data.age+"</td>";
        html += "<td>"+data.phone+"</td>";
        html += "<td>"+data.type+"</td>";
        html += "<td>"+data.password+"</td>";
        if(data.departId==null){
            html += "<td></td>";
        }else{
            html += "<td>"+data.departId+"</td>";
        }
        html += "<td>"+data.createTime+"</td>";
        html += "<td>"+data.updateTime+"</td>";
        html += "<td><a href='#' class='btn btn-primary btn-xs' data-toggle='modal' data-target='#customerEditDialog' onclick='editCustomer(&apos;"+data.wid+"&apos;)'>修改</a>";
        html += "<a href='#' class='btn btn-danger btn-xs' onclick='deleteCustomer(&apos;"+data.wid+"&apos;)'>删除</a></td></tr>";
        $("tbody").append(html);
    }

    function clean() {
        $("#edit_password_form input:gt(0)").val("");
    }

    $(function () {
        $.ajax(
            {
                url:'<%=path%>/manageEmployeeInfo/getPageEmployee',
                type:'POST',
                data:{'page':1,'pageSize':10},
                dataType:'JSON',
                success:function (data) {
                    var page_count = data.data.size;
                    var list = data.data.data;
                    $('tbody').empty();
                    for(var i=0;i<list.length;i++){
                        showPageEmployeeData(list[i]);
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
                                url: '<%=path%>/manageEmployeeInfo/getPageEmployee',
                                type: 'POST',
                                data: {'page': page, 'pageSize': 10},
                                dataType: 'JSON',
                                success: function (data) {
                                    $('tbody').empty();
                                    var page_count = data.data.size;
                                    var list = data.data.data;
                                    for(var i=0;i<list.length;i++){
                                        showPageEmployeeData(list[i]);
                                    }
                                    $('#last_page').text(page_count)
                                }
                            })
                        }
                    });
                }
            }
        );
        initEmployeeType("<%=path%>");
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

            "timeOut": "5000", //展现时间

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
