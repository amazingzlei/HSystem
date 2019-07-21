$(function () {
    $.ajax(
        {
            url:urlPath+'/directorInfo/getExpireMedInfo',
            type:'GET',
            data:{'page':1,'pageSize':10},
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
                                url: urlPath+'/directorInfo/getExpireMedInfo',
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
                }
            }
        }
    );
});
//展示分页数据
function showPageEmployeeData(data) {
    var html = "<tr>";
    html += "<td>"+data.mid+"</td>";
    html += "<td>"+data.name+"</td>";
    html += "<td>"+data.counterId+"</td>";
    html += "<td>"+data.counterLeft+"</td>";
    html += "<td>"+data.repertoryId+"</td>";
    html += "<td>"+data.repertoryLeft+"</td>";
    html += "<td>"+data.productTime+"</td>";
    html += "<td>"+data.endTime+"</td>";
    html += "<td><a href='#' class='btn btn-primary btn-xs' onclick='draw(this)'>下架</a>";
    $("tbody").append(html);
}

function draw(obj){
    swal({
        title: "操作提示",      //弹出框的title
        text: "确定下架吗?",   //弹出框里面的提示文本
        type: "warning",        //弹出框类型
        showCancelButton: true, //是否显示取消按钮
        confirmButtonColor: "#DD6B55",//确定按钮颜色
        cancelButtonText: "取消",//取消按钮文本
        confirmButtonText: "确定",//确定按钮上面的文档
        closeOnConfirm: true
    }, function () {
        var mid = $(obj).parent().parent().children().eq(0).text();
        var productTime = $(obj).parent().parent().children().eq(6).text();
        // alert(mid+productTime)
        $.ajax({
            url:urlPath+"/directorInfo/drawMed",
            data:{mid:mid,productTime:productTime},
            type:"POST",
            success:function (data) {
                if(data.code==200){
                    toastr.success("下架成功!");
                    ws.send(JSON.stringify({'message':'有药品下架,请查看!','role':data.data.purchaseId,'socketId':"123"}));
                    if(data.data.pharmacyId.length > 0){
                        for(var i = 0;i<data.data.pharmacyId.length;i++){
                            ws.send(JSON.stringify({'message':'有药品下架,请查看!','role':data.data.pharmacyId[i],'socketId':"123"}));
                        }
                    }
                    setTimeout(function(){ window.location.reload(); }, 1000)
                }else{
                    toastr.error(data.msg);
                }
            }
        })
    });
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