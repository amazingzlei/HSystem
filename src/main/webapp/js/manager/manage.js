// 判断年龄是否合法
function isAgeLegal(){
    var flag = true ;
    if(isNaN($("#edit_age").val())){
        flag = false;
        toastr.error('请输入正确年龄');
        // alert("请输入正确年龄")
    }else{
        if($("#edit_age").val()< 20||$("#edit_age").val()>80){
            flag = false ;
            toastr.error('年龄应该在20-80之间');
            // alert("年龄应该在20-80之间") ;
        }
    }
    return flag;
}

// 判断电话号码是否合法
function isPoneAvailable(tel) {
    var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
    if (!myreg.test(tel)) {
        toastr.error('请输入正确的手机号');
        // alert("请输入正确的手机号");
        return false;
    } else {
        return true;
    }
}

// 密码是否合法
function isPasswordLegal(password) {
    if(password.length<=4||password.length>=16){
        toastr.error('密码长度需在5-15位长度!');
        // alert("密码长度需在5-15位长度!");
        return false;
    }else {
        return true;
    }
}

// 判断姓名是否合法
function isNameLegal(name){
    if(name==""){
        toastr.error('请输入名字!');
        // alert("请输入名字!");
        return false;
    }else{
        // var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
        var myreg= /^[\u4e00-\u9fa5 ]{2,20}$/;
        if(!myreg.test(name)){
            toastr.error('非法中文名!');
            // alert("非法中文名!");
            return false;
        }else{
            return true;
        }
    }
}

var path = "/hsystem";

//更新密码
function updatePassword() {
    //原始密码不能为空
    if($("#edit_old_password").val()===""){
        toastr.error('请输入原始密码!');
        // alert("请输入原始密码!");
    }else {
        if(isPasswordLegal($("#edit_new_password").val())&&isPasswordLegal($("#edit_confirm_password").val())){
            // 判断新密码和确认密码是否一样
            if($("#edit_new_password").val()===$("#edit_confirm_password").val()){
                //判断原密码是否正确
                $.ajax({
                    url : path+"/manageEmployeeInfo/updatePassword",
                    data :  $("#edit_password_form").serialize(),
                    type : 'post',
                    dataType : 'json',
                    success:function (data) {
                        if(data.result===1){
                            toastr.success('修改成功');
                            // window.location.reload();
                            setTimeout(function(){ window.location.reload(); }, 1000)
                        }else if(data.result===-1){
                            toastr.error('原密码不正确!');
                            // alert("原密码不正确!");
                        }else {
                            toastr.error('修改失败!');
                            // alert("修改失败!");
                        }
                    }
                });
            }else{
                toastr.error('两次密码输入不一致!');
                // alert("两次密码输入不一致!");
            }
        }
    }
}

//人员搜索
function searchEmployee(path) {
    var wid = $("#searchContent").val();
    $.ajax({
        url:path+"/manageEmployeeInfo/searchEmployeeById",
        type: "get",
        dataType:"json",
        data: {"wid": wid,'page':1,'pageSize':10},
        success:function (data) {
            $("#searchContent").val("");
            var page_count = data.data.size;
            var list = data.data.data;
            $('tbody').empty();
            for (var i = 0; i < list.length; i++) {
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
                        url: path + "/manageEmployeeInfo/searchEmployeeById",
                        type: 'get',
                        data: {'wid': wid, 'page': page, 'pageSize': 10},
                        dataType: 'JSON',
                        success: function (data) {
                            $('tbody').empty();
                            var page_count = data.data.size;
                            var list = data.data.data;
                            for (var i = 0; i < list.length; i++) {
                                showPageEmployeeData(list[i]);
                            }
                            $('#last_page').text(page_count)
                        }
                    })
                }
            })
        }
    })
}

//判断是否是医生
function isDepartMentShow(){
    var value = $("#edit_type").val();
    if(value==1 || value==5){
        $("#depart_type").attr("name","departId");
        $("#department").css("display","block");
    }else{
        $("#depart_type").attr("name","");
        $("#department").css("display","none");
    }
}

//初始化人员类型
function initEmployeeType(path) {
    $.ajax({
        url:path+'/manageEmployeeInfo/getAllType',
        type:'GET',
        dataType:'JSON',
        success:function (data) {
            // $("#edit_type").empty();
            var list = data.data;
            for(var i=0;i<list.length;i++){
                var html = "<option value="+list[i].typeId+">"+list[i].name+"</option>";
                $("#edit_type").append(html);
            }
        }
    })
}
