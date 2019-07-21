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

    $("tbody").append(html);
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