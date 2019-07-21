<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>登录</title>
    <style type="text/css">
        .toast-center-center {
            top: 50%;
            left: 50%;
            margin-top: -25px;
            margin-left: -150px;
        }
    </style>
    <script type="application/x-javascript"> addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);

    function hideURLbar() {
        window.scrollTo(0, 1);
    } </script>
    <!-- font files  -->
    <link href="<%=path%>/images/title.ico" rel="SHORTCUT ICON"/>
    <link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Nunito:400,300,700' rel='stylesheet' type='text/css'>
    <!-- /font files  -->
    <!-- css files -->
    <link href="<%=path%>/css/style.css" rel='stylesheet' type='text/css' media="all"/>
    <link href="<%=path%>/css/toastr.css" rel="stylesheet" type="text/css">
    <%--<link href="<%=path%>/css/bootstrap.min.css" rel="stylesheet">--%>
    <%--<link href="<%=path%>/css/bootstrap.css" rel="stylesheet" type="text/css">--%>
    <script type="text/javascript" src="<%=path%>/js/jq/jquery-2.0.3.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/public/toastr.min.js"></script>
    <style type="text/css">
        .type {
            padding: 10px 40px 10px 10px;
            width: 80%;
            border: 1px solid white;
            color: #000;
            text-align: left;
            outline: none;
            font-size: 18px;
            background: url(../images/person.png) no-repeat 440px 10px;
            margin-top: 5%;
            font-weight: 300;
            font-family: 'Muli', sans-serif;
            border-radius: 4px;
        }
    </style>

</head>
<body style="background-image: url('<%=path%>/images/back.JPG')">

<%--<h1 style="color: #000;font-weight: bolder">工作人员登录</h1>--%>
<div class="log">
    <div class="content2" style="margin-top: 7%">
        <h2>登录</h2>
        <form action="<%=path%>/userLogin/login" method="post" id="loginForm">
            <input type="text" name="wid" id="id" placeholder="请输入工号">
            <input type="password" name="password" id="password" placeholder="请输入密码">
            <br>
            <%--<input type="hidden" value="0" id="type" name="type">--%>
            <select class="type" name="type" id="edit_type">
                <option value="0">管理员</option>
                <option value="1">医生</option>
                <option value="2">医院主管</option>
                <option value="3">采购人员</option>
                <option value="4">药房出药人员</option>
                <option value="5">科室主任</option>
                <option value="6">药房收费人员</option>
            </select>
            <div class="button-row">
                <input type="button" class="register" value="登录" id="btnSubmit" onclick="doSub()">
                <div class="clear"></div>
            </div>
        </form>
    </div>
</div>
<%--<div style="clear: both"></div>--%>
<%--<div class="footer text-center small">--%>
    <%--作者:zenglei--%>
<%--</div>--%>
</body>
<script type="text/javascript">
    function doSub() {
        var wid = $("#id").val();
        if ($("#id").val() == "") {
            toastr.error("请输入工号!")
        } else if ($("#password").val() == "") {
            toastr.error("请输入密码!")
        } else if(isNaN(wid) || parseInt(wid)<=0 || wid%1!==0){
            toastr.error("请输入正确的工号!")
        }else {
            var url = "<%=path%>/userLogin/isLoginSuccess";
            var data = $("#loginForm").serialize();
            $.ajax({
                url: url,
                type: "post",
                data: data,
                dataType: "json",
                success: function (data) {
                    if (data.code === 200) {
                        //登录成功。跳转页面
                        $("#loginForm").submit();
                    } else {
                        toastr.error(data.msg);
                    }
                },
                error: function () {

                }
            })
        }
    }
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

            "timeOut": "3000", //展现时间

            "extendedTimeOut": "1000",//加长展示时间

            "showEasing": "swing",//显示时的动画缓冲方式

            "hideEasing": "linear",//消失时的动画缓冲方式

            "showMethod": "fadeIn",//显示时的动画方式

            "hideMethod": "fadeOut" //消失时的动画方式

        };
    })
</script>
</html>