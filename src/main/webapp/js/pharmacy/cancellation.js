$(function () {
    $.ajax(
        {
            url:urlPath+'/pharmacyInfo/getNeedDrawPrescript',
            type:'GET',
            data:{'page':1,'pageSize':10},
            dataType:'JSON',
            success:function (data) {
                if(data.code == 200){
                    if(data.data.data!= null){
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
                                    url: urlPath+'/pharmacyInfo/getNeedDrawPrescript',
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
                    }else {
                        toastr.info("暂无记录!")
                    }
                }else{
                    toastr.error(data.msg);
                }
            }
        }
    );
});

//展示分页数据
function showPageEmployeeData(data) {
    var html = "<tr>";
    html += "<td>"+data.pid+"</td>";
    if(data.isCharge=="1"){
        html += "<td>已收费</td>";
    }else {
        html += "<td>未收费</td>";
    }

    if(data.chargeTime==null){
        html += "<td></td>";
    }else{
        html += "<td>"+data.chargeTime+"</td>";
    }

    if(data.isShell == "1"){
        html += "<td>已出库</td>";
    }else {
        html += "<td>未出库</td>";
    }
    html += "<td>"+data.totalPrice+"</td>";
    html += "<td>"+data.createTime+"</td>";
    html += "<td>" +
        "<a href='#' class='btn btn-danger btn-xs' onclick='dropPrescript(this)'>作废</a></tr>";
    $("tbody").append(html);
}

function dropPrescript(obj) {
    var pid = $(obj).parent().parent().children().eq(0).text();

    swal({
        title: "操作提示",      //弹出框的title
        text: "确定作废吗？",   //弹出框里面的提示文本
        type: "warning",        //弹出框类型
        showCancelButton: true, //是否显示取消按钮
        confirmButtonColor: "#DD6B55",//确定按钮颜色
        cancelButtonText: "取消",//取消按钮文本
        confirmButtonText: "确定",//确定按钮上面的文档s
        closeOnConfirm: true
    }, function () {
        $.ajax({
            url: urlPath+'/pharmacyInfo/dropPrescript',
            type: 'POST',
            data: {pid:pid},
            success: function (data) {
                if(data.code==200){
                    toastr.success("执行成功!");
                    setTimeout(function(){ window.location.reload(); }, 2000);
                }else if(data.code == 0){
                    toastr.error("执行失败!");
                }else{
                    toastr.error(data.msg);
                }
            }
        })
    });


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

