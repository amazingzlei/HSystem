function clean() {
    $("#edit_password_form input:gt(0)").val("");
}

function getExpireMed(obj) {
    $("tbody").empty();
    $(obj).siblings().attr("class","btn btn-info");
    $(obj).attr("class","btn btn-primary");
    $.ajax(
        {
            url:urlPath+'/directorInfo/getExpireMed',
            type:'GET',
            data:{'page':1,'pageSize':10},
            dataType:'JSON',
            success:function (data) {
                if(data.code == 200){
                    if(data.data!= null){
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
                                    url: urlPath+'/directorInfo/getExpireMed',
                                    type: 'GET',
                                    data: {'page': page, 'pageSize': 10},
                                    dataType: 'JSON',
                                    success: function (data) {
                                        if(data.data != null){
                                            $('tbody').empty();
                                            var page_count = data.data.size;
                                            var list = data.data.data;
                                            for(var i=0;i<list.length;i++){
                                                showPageEmployeeData(list[i]);
                                            }
                                            $('#last_page').text(page_count)
                                        }
                                    }
                                })
                            }
                        });
                    }else{
                        clearBootStrap();
                        toastr.info("暂无数据")
                    }
                }else{
                    // $('#last_page').text(0);
                    toastr.error(data.msg)
                }
            }
        }
    );
}

function searchMed(obj) {
    var fun = $("#medFun").val();
    // alert(fun)
    $.ajax({
        url:urlPath+'/directorInfo/getMedByFun',
        data:{'function':fun},
        type:'GET',
        success: function (data) {
            $('tbody').empty();
            var list = data.data;
            for(var i=0;i<list.length;i++){
                showPageEmployeeData(list[i]);
            }
        }
    })
}

function getBlowCount(obj) {
    $("tbody").empty();
    $(obj).siblings().attr("class","btn btn-info");
    $(obj).attr("class","btn btn-primary");
    $.ajax(
        {
            url:urlPath+'/directorInfo/getBlowCount',
            type:'GET',
            data:{'page':1,'pageSize':10},
            dataType:'JSON',
            success:function (data) {
                if(data.code == 200){
                    if(data.data!= null){
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
                                    url: urlPath+'/directorInfo/getBlowCount',
                                    type: 'GET',
                                    data: {'page': page, 'pageSize': 10},
                                    dataType: 'JSON',
                                    success: function (data) {
                                        if(data.data != null){
                                            $('tbody').empty();
                                            var page_count = data.data.size;
                                            var list = data.data.data;
                                            for(var i=0;i<list.length;i++){
                                                showPageEmployeeData(list[i]);
                                            }
                                            $('#last_page').text(page_count)
                                        }
                                    }
                                })
                            }
                        });
                    }else{
                        clearBootStrap();
                        toastr.info("暂无数据！");
                    }
                }else{
                    // $('#last_page').text(0)
                    toastr.error(data.msg)
                }
            }
        }
    )
}

//展示分页数据
function showPageEmployeeData(data) {
    var html = "<tr>";
    html += "<td>"+data.name+"</td>";
    html += "<td>"+data.productTime+"</td>";
    html += "<td>"+data.endTime+"</td>";
    html += "<td>"+data.totalCount+"</td>";
    html += "<td><a href='#' class='btn btn-primary btn-xs' data-toggle='modal' data-target='#customerEditDialog' onclick='purchase(this)'>采购</a>";
    $("tbody").append(html);
}

// 采购
function purchase(obj) {
    $("#addPurchase input").val("");
    $("#addPurchase .error").each(function () {
        if($(this).attr("id")=="medNum-error"){
            $(this).remove();
        }else {
            $(this).removeClass("error")
        }
    })
    var name = $(obj).parent().parent().children().eq(0).text();
    $("#medName").val(name);
}

$(function () {
    $.ajax(
        {
            url:urlPath+'/directorInfo/getExpireMed',
            type:'GET',
            data:{'page':1,'pageSize':10},
            dataType:'JSON',
            success:function (data) {
                if(data.code == 200){
                    if(data.data!= null){
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
                                    url: urlPath+'/directorInfo/getExpireMed',
                                    type: 'GET',
                                    data: {'page': page, 'pageSize': 10},
                                    dataType: 'JSON',
                                    success: function (data) {
                                        if(data.data != null){
                                            $('tbody').empty();
                                            var page_count = data.data.size;
                                            var list = data.data.data;
                                            for(var i=0;i<list.length;i++){
                                                showPageEmployeeData(list[i]);
                                            }
                                            $('#last_page').text(page_count)
                                        }
                                    }
                                })
                            }
                        });
                    }else{
                        toastr.info("暂无数据!");
                    }
                }else{
                    toastr.error(data.msg)
                }
            }
        }
    );
});

// 表单验证
$(function () {
    $("#addPurchase").validate({
        rules:{
            medName:{
                required:true,
            },
            medNum:{
                required:true,
                digits:true,
                min:10,
                max:50000
            }
        },
        messages:{
            medName:{
                required:"这是必填字段!",
            },
            medNum:{
                required:"这是必填字段!",
                digits:"请输入整数!",
                min:"至少采购10份!",
                max:"最多采购50000份!"
            }
        },
    });
    $("#addNewMedPurchase").validate({
        rules:{
            newMedName:{
                required:true,
            },
            newMedNum:{
                required:true,
                digits:true,
                min:10,
                max:50000
            }
        },
        messages:{
            newMedName:{
                required:"这是必填字段!",
            },
            newMedNum:{
                required:"这是必填字段!",
                digits:"请输入整数!",
                min:"至少采购10份!",
                max:"最多采购50000份!"
            }
        },
    })
});

// 提交采购
function doSub() {
    var flag = true;
    if($("#medNum").val()==null || $("#medNum").val()==""){
        toastr.error("请输入数量!");
        flag = false;
    }
    if($("#medNum").hasClass("error")){
        flag = false;
    }
    if(flag){
        $.ajax({
            url:urlPath+"/directorInfo/isPurchaseHas",
            type:"POST",
            data:{medName:$("#medName").val()},
            success:function (data) {
                if(data.code==200){
                    // 隐藏model框
                    $('.modal').map(function() {
                        $(this).modal('hide');
                    });
                    // 判断是否追加
                    swal({
                        title: "操作提示",      //弹出框的title
                        text: "该药品已经添加至采购表中，确定还要采购吗？",   //弹出框里面的提示文本
                        type: "warning",        //弹出框类型
                        showCancelButton: true, //是否显示取消按钮
                        confirmButtonColor: "#DD6B55",//确定按钮颜色
                        cancelButtonText: "取消",//取消按钮文本
                        confirmButtonText: "确定",//确定按钮上面的文档
                        closeOnConfirm: true
                    }, function () {
                        addPurchaseInfo();
                    });
                }else {
                    addPurchaseInfo();
                }
            }
        })
    }
}

// 添加至采购表
function addPurchaseInfo() {
    $.ajax({
        url:urlPath+"/directorInfo/addPurchase",
        data:{medName:$("#medName").val(),medNum:$("#medNum").val()},
        type:"POST",
        success:function (data) {
            if(data.code==200){
                toastr.success("添加成功!");
                setTimeout(function(){ window.location.reload(); }, 1000)
            }else {
                toastr.error("添加失败!")
            }
        }
    })
}

// 添加至采购表
function addNewPurchaseInfo() {
    $.ajax({
        url:urlPath+"/directorInfo/addPurchase",
        data:{medName:$("#newMedName").val(),medNum:$("#newMedNum").val()},
        type:"POST",
        success:function (data) {
            if(data.code==200){
                toastr.success("添加成功!");
                setTimeout(function(){ window.location.reload(); }, 1000)
            }else {
                toastr.error("添加失败!")
            }
        }
    })
}

// 添加新药
function addNewMed(obj) {
    // 清空
    $("#addNewMedPurchase input").val("");
    $("#addNewMedPurchase .error").each(function () {
        if($(this).attr("id")=="newMedNum-error"){
            $(this).remove();
        }else {
            $(this).removeClass("error")
        }
    })
    $('#addNewMedDialog').map(function() {
        $(this).modal('show');
    });
}

// 采购新药
function doNewSub() {
    var flag = true;

    if($("#newMedName").val()==null || $("#newMedName").val()==""){
        toastr.error("请输入药品名称!!");
        flag = false;
        return;
    }

    if($("#newMedNum").val()==null || $("#newMedNum").val()==""){
        toastr.error("请输入数量!");
        flag = false;
        return;
    }
    if($("#newMedNum").hasClass("error") || $("#newMedName").hasClass("error")){
        flag = false;
        return;
    }
    if(flag){
        $.ajax({
            url:urlPath+"/directorInfo/isPurchaseHas",
            type:"POST",
            data:{medName:$("#newMedName").val()},
            success:function (data) {
                if(data.code==200){
                    // 隐藏model框
                    $('.modal').map(function() {
                        $(this).modal('hide');
                    });
                    // 判断是否追加
                    swal({
                        title: "操作提示",      //弹出框的title
                        text: "该药品已经添加至采购表中，确定还要采购吗？",   //弹出框里面的提示文本
                        type: "warning",        //弹出框类型
                        showCancelButton: true, //是否显示取消按钮
                        confirmButtonColor: "#DD6B55",//确定按钮颜色
                        cancelButtonText: "取消",//取消按钮文本
                        confirmButtonText: "确定",//确定按钮上面的文档
                        closeOnConfirm: true
                    }, function () {
                        addNewPurchaseInfo();
                    });
                }else {
                    addNewPurchaseInfo();
                }
            }
        })
    }
}

// 分页清空
function clearBootStrap(){
    $('#pageLimit').empty()
}

// websocket及时通信
var ws = null;
$(function(){
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window) {
        // ws = new WebSocket("ws://192.168.43.213:8090/webSocketOneToOne/"+wid+",123");
        // ws = new WebSocket("ws://localhost:8090/webSocketOneToOne/"+wid+",123");
        ws = new WebSocket("ws://localhost:8080/hsystem/webSocketOneToOne/"+wid+",123");
    } else {
        toastr.error('当前浏览器 Not support websocket')
    }
    /*
     *监听三种状态的变化js会回调
     */
    ws.onopen = function(message) {
    };
    ws.onclose = function(message) {
    };
    //接受消息
    ws.onmessage=function(ev){
        toastr.info(ev.data);
    };
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function() {
        ws.close();
    };
    //关闭连接
    function closeWebSocket() {
        ws.close();
    }
})