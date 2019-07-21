$(function () {
    var url = location.search;
    var pid = $("#hiddenData").val();
    $.ajax(
        {
            url:urlPath+'/pharmacyInfo/getCounter',
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
                                url: urlPath+'/pharmacyInfo/getCounter',
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
    html += "<td>"+data.cid+"</td>";
    html += "<td>"+data.position+"</td>";
    if(data.isUse=="0"){
        html += "<td><a href=javascript:;' class='btn btn-primary btn-xs'>未使用</a></td>";
    }else{
        html += "<td><a href='javascript:;' class='btn btn-info btn-xs'>正在使用</a></td>";
    }
    html += "<td><a href='#' class='btn btn-primary btn-xs' data-toggle=\"modal\" data-target=\"#editRepertory\" onclick='editReperstory(this)'> 编辑</a>"
    html += "<a href='#' style='margin-left: 10px' class='btn btn-danger btn-xs' onclick='deleteReperstory(this)'> 删除</a></td></tr>"
    $("tbody").append(html);
}

// 添加药台
function addReperstory() {
    var position = $("#position").val();
    if(null == position || position==''){
        toastr.error("请输入药台位置!");
    }else{
        $.ajax({
            url:urlPath+"/pharmacyInfo/addCounter",
            type:"POST",
            data:{position:position},
            success:function (data) {
                if(data.code == 200){
                    toastr.success("添加成功！");
                    setTimeout(function(){ window.location.reload(); }, 1000);
                }else{
                    toastr.error(data.msg);
                }
            }
        })
    }
}

// 删除药台
function deleteReperstory(obj) {
    var rid = $(obj).parent().parent().children().eq(0).text();

    swal({
        title: "操作提示",      //弹出框的title
        text: "确认删除吗？",   //弹出框里面的提示文本
        type: "warning",        //弹出框类型
        showCancelButton: true, //是否显示取消按钮
        confirmButtonColor: "#DD6B55",//确定按钮颜色
        cancelButtonText: "取消",//取消按钮文本
        confirmButtonText: "确定",//确定按钮上面的文档
        closeOnConfirm: true
    }, function () {
        $.ajax({
            url:urlPath+"/pharmacyInfo/deleteCounter",
            type:"POST",
            data:{rid:rid},
            success:function (data) {
                if(data.code == 200){
                    toastr.success("删除成功！");
                    setTimeout(function(){ window.location.reload(); }, 1000);
                }else{
                    toastr.error(data.msg);
                }
            }
        })
    });

}

// 编辑药台
function editReperstory(obj) {
    var rid = $(obj).parent().parent().children().eq(0).text();
    var position = $(obj).parent().parent().children().eq(1).text();
    $("#edit_rid").val(rid);
    $("#edit_position").val(position);
}

// 修改药台
function updateReperstory() {
    var pid = $("#edit_rid").val();
    var position = $("#edit_position").val();
    if(null == position || position==''){
        toastr.error("请输入药台位置!");
    }else{
        $.ajax({
            url:urlPath+"/pharmacyInfo/updateCounter",
            type:"POST",
            data:{rid:pid,position:position},
            success:function (data) {
                if(data.code == 200){
                    toastr.success("修改成功！");
                    setTimeout(function(){ window.location.reload(); }, 1000);
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