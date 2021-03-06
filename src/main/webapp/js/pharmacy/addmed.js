$(function () {
    $.ajax(
        {
            url:urlPath+'/pharmacyInfo/getLowNumMedicinal',
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
                                    url: urlPath+'/pharmacyInfo/getLowNumMedicinal',
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
    html += "<td>"+data.mid+"</td>";
    html += "<td>"+data.name+"</td>";
    html += "<td>"+data.productTime+"</td>";
    if(data.counterId == null || data.counterId==''){
        html += "<td></td>";
    }else{
        html += "<td>"+data.counterId+"</td>";
    }
    html += "<td>"+data.counterLeft+"</td>";
    html += "<td>"+data.repertoryId+"</td>";
    html += "<td>"+data.repertoryLeft+"</td>";
    html += "<td>" +
        "<a href='#' class='btn btn-primary btn-xs' data-toggle=\"modal\" data-target=\"#customerEditDialog\" onclick='addMed(this)'>添加</a></tr>";
    $("tbody").append(html);
}
var repertoryLeft;
function addMed(obj) {
    $("#addPurchase input").each(function () {
        $(this).val("");
    });
    $("#counterId").empty();
    $("#counterId").removeAttr("readonly");
    var mid = $(obj).parent().parent().children().eq(0).text();
    var name = $(obj).parent().parent().children().eq(1).text();
    var productTime = $(obj).parent().parent().children().eq(2).text();
    var counterId = $(obj).parent().parent().children().eq(3).text();
    repertoryLeft = $(obj).parent().parent().children().eq(6).text();
    $("#mid").val(mid);
    $("#name").val(name);
    $("#productTime").val(productTime);
    if(counterId==''){
        $.ajax({
            url:urlPath+"/pharmacyInfo/getCanUseCounter",
            type:"GET",
            success:function (data) {
                if(data.code==200){
                    if(data.data.length != 0){
                        for(var i=0;i<data.data.length;i++){
                            var option = "<option value='"+data.data[i].position+"'>"+data.data[i].position+"</option>";
                            $("#counterId").append(option);
                        }
                    }else{
                        toastr.info("药台不足，请添加!")
                    }
                }else {
                    toastr.error(data.msg);
                }
            }
        })
    }else{
        var option = "<option value='"+counterId+"'>"+counterId+"</option>";
        $("#counterId").append(option);
        $("#counterId").attr("readonly","true");
    }
}

function doSub() {
    var counterId = $("#counterId").val();
    var num = $("#medNum").val();
    var flag = true;
    if(isNaN(num) || num==null || num=='' || num%1!==0 || num<=0){
        flag = false;
        toastr.error("请输入正确的数字!");
        return;
    }
    if(counterId == null || counterId == ''){
        flag = false;
        toastr.error("请选择药台!");
    }

    if(parseInt(repertoryLeft)<parseInt(num)){
        flag = false;
        toastr.error("仓库库存不足!");
    }

    if(flag){
        $.ajax({
            url:urlPath+"/pharmacyInfo/addMed",
            type:"POST",
            data:$("#addPurchase").serialize(),
            success:function (data) {
                if(data.code == 200){
                    toastr.success("补药成功!");
                    setTimeout(function(){ window.location.reload(); }, 2000);
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

