$(function () {
    $.ajax(
        {
            url:urlPath+'/directorInfo/getAllApplication',
            type:'GET',
            data:{'page':1,'pageSize':10},
            dataType:'JSON',
            success:function (data) {
                if(data.code==200){
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
                                    url: urlPath+'/directorInfo/getAllApplication',
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
                }else if(data.code==501){
                    toastr.info(data.msg)
                }else{
                    toastr.error("系统异常，请联系管理员!")
                }
            }
        }
    );
});

//展示分页数据
function showPageEmployeeData(data) {
    var html = "<tr>";
    html += "<td>"+data.pid+"</td>";
    html += "<td>"+data.isPut+"</td>";
    html += "<td>"+data.status+"</td>";
    html += "<td>"+data.assessor+"</td>";
    html += "<td>"+data.wid+"</td>";
    html += "<td>"+data.createTime+"</td>";
    html += "<td><a class='btn btn-info btn-xs' href='"+urlPath+"/directorInfo/getView/applicationinfo/"+data.pid+"'>查看明细</a></td>";
    $("tbody").append(html);
}


// websocket及时通信
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