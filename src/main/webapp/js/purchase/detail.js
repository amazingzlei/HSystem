$(function () {
    var url = location.search;
    var pid = $("#hiddenData").val();
    $.ajax(
        {
            url:urlPath+'/purchaseInfo/getDetailInfo',
            type:'GET',
            data:{'page':1,'pageSize':10,pid:pid},
            dataType:'JSON',
            success:function (data) {
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
                                url: urlPath+'/purchaseInfo/getDetailInfo',
                                type: 'GET',
                                data: {'page': page, 'pageSize': 10,pid:pid},
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
                                    }else {
                                        toastr.info(data.msg)
                                    }
                                }
                            })
                        }
                    });
                }else {
                    toastr.info(data.msg)
                }
            }
        }
    );
});

//展示分页数据
function showPageEmployeeData(data) {
    var html = "<tr>";
    html += "<td name='pid'>"+data.pid+"</td>";
    if(data.mid != null){
        html += "<td name='mid'>"+data.mid+"</td>";
    }else{
        html += "<td name='mid'>"+''+"</td>";
    }
    html += "<td name='newMedName'>"+data.newMedName+"</td>";
    html += "<td name='mnum'>"+data.mnum+"</td>";
    if(data.isPut=="0"){
        html += "<td><a href='#' class='btn btn-primary btn-xs' data-toggle='modal' data-target='#customerEditDialog' onclick='editMed(this)'>入库</a></tr>";
    }else{
        html += "<td><a href='javascript:;' class='btn btn-info btn-xs'>已入库</a></tr>";
    }

    $("tbody").append(html);
}

// 导出
function purchaseExport(){
    var pid = $("td[name='pid']").eq(0).html();
    var mids = new Array();
    var newMedNames = new Array();
    var mnums = new Array();
    $("td[name='mid']").each(function () {
        if($(this).text()=='' || $(this).text()==null){
            mids.push(' ');
        }else{
            mids.push($(this).text());
        }
    });
    $("td[name='newMedName']").each(function () {
        newMedNames.push($(this).text());
    });
    $("td[name='mnum']").each(function () {
        mnums.push($(this).text());
    });
    $.ajax({
        url:urlPath+"/purchaseInfo/purchaseExport",
        type:"POST",
        data:{pid:pid,mids:mids.toString(),newMedNames:newMedNames.toString(),mnums:mnums.toString()},
        success:function (data) {
            return data
        },
        error:function (data) {
            var a = data;
            alert(data)
            toastr.error("导出失败!")
        }
    })
}

// 编辑药品
function editMed(obj) {

    $("#edit_repertoryId").empty();

    // 初始化仓库
    $.ajax({
        url:urlPath+"/purchaseInfo/getAllCanUseRepertory",
        type:"GET",
        success:function (data) {
            if(data.code == 200){
                var list = data.data;
                var html = "";
                for(var i=0;i<list.length;i++){
                    html += "<option value='"+list[i].rid+"'>"+list[i].position+"</option>"
                }
                $("#edit_repertoryId").append(html.trim());
            }else if(data.code == 404){
                toastr.info("无可用仓库,请添加!");
            }
        }
    })

    $("#edit_medicial_form input").val("");
    var pid = $(obj).parent().parent().children().eq(0).text();
    var mid = $(obj).parent().parent().children().eq(1).text();
    var name = $(obj).parent().parent().children().eq(2).text();
    var num = $(obj).parent().parent().children().eq(3).text();
    if(mid != null && mid != ''){
        $.ajax({
            url:urlPath+"/purchaseInfo/getDetailMed",
            type:"GET",
            data:{mid:mid},
            success:function (data) {
                if(data.code == 200){
                    var name = data.data.name;
                    var fun = data.data.function;
                    var shellPrice = data.data.shellPrice;
                    var bidPrice = data.data.bidPrice;
                    var bidPrice = data.data.bidPrice;
                    var saveTime = data.data.saveTime;
                    $("#edit_name").val(name);
                    $("#edit_mid").val(mid);
                    $("#edit_mid").attr("readonly","true");
                    $("#edit_repertoryLeft").val(num);
                    $("#edit_function").val(fun);
                    $("#edit_function").attr("readonly","true");
                    $("#edit_shellPrice").val(shellPrice);
                    $("#edit_shellPrice").attr("readonly","true");
                    $("#edit_bidPrice").val(bidPrice);
                    $("#edit_bidPrice").attr("readonly","true");
                    $("#edit_saveTime").val(saveTime);
                    $("#edit_saveTime").attr("readonly","true");
                }
            }
        })
    }else{
        $("#edit_mid").removeAttr("readonly");
        $("#edit_function").removeAttr("readonly");
        $("#edit_shellPrice").removeAttr("readonly");
        $("#edit_bidPrice").removeAttr("readonly");
        $("#edit_saveTime").removeAttr("readonly");
        $("#edit_name").val(name);
        $("#edit_repertoryLeft").val(num);
    }
}

// 入库
function put() {
    var flag = true;
    var mid = $("#edit_mid").val();
    var fun = $("#edit_function").val();
    var shellPrice = $("#edit_shellPrice").val();
    var bidPrice = $("#edit_bidPrice").val();
    var repertoryId = $("#edit_repertoryId").val();
    var productTime = $("#edit_productTime").val();
    var saveTime = $("#edit_saveTime").val();
    if(mid==null || mid == ''){
        flag = false;
        toastr.error("请输入药品编号!");
        return;
    }
    if(fun==null || fun == ''){
        flag = false;
        toastr.error("请输入功能!");
        return;
    }
    if(shellPrice==null || shellPrice == ''){
        flag = false;
        toastr.error("请输入售价!");
        return;
    }
    if(isNaN(shellPrice) || parseInt(shellPrice)<=0){
        flag = false;
        toastr.error("请输入正确的售价!");
        return;
    }

    if(bidPrice==null || bidPrice == ''){
        flag = false;
        toastr.error("请输入进价!");
        return;
    }
    if(isNaN(bidPrice) || parseInt(bidPrice)<=0){
        flag = false;
        toastr.error("请输入正确的进价!");
        return;
    }

    if(repertoryId==null || repertoryId == ''){
        flag = false;
        toastr.error("请选择仓库!");
        return;
    }

    if(productTime==null || productTime == ''){
        flag = false;
        toastr.error("请选择生产日期!");
        return;
    }

    if(saveTime==null || saveTime == ''){
        flag = false;
        toastr.error("请输入保质期!");
        return;
    }

    if(isNaN(saveTime) || parseInt(saveTime)<=0 || saveTime%1!==0){
        flag = false;
        toastr.error("请输入正确的保质期");
        return;
    }

    if(flag){
        $.ajax({
            url:urlPath+"/purchaseInfo/putMed",
            type:"POST",
            data:$("#edit_medicial_form").serialize()+"&pid="+$("#hiddenData").val(),
            success:function (data) {
                if(data.code==200){
                    if(data.data == null){
                        toastr.success("入库成功!");
                        setTimeout(function(){ window.location.reload(); }, 2000)
                    }else{
                        toastr.success("入库成功!由于该药品已存在，因此仓库不变!");
                        setTimeout(function(){ window.location.reload(); }, 2000)
                    }
                }else{
                    toastr.error(data.msg);
                }
            }
        })
    }
}

var ws = null;
$(function(){
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window) {
        ws = new WebSocket("ws://localhost:8080/hsystem/webSocketOneToOne/"+wid+",123");
        // ws = new WebSocket("ws://192.168.43.213:8090/webSocketOneToOne/"+wid+",123");
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