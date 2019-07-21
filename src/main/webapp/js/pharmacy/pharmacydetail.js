$(function () {
    var pid = $("#hiddenData").val();
    $.ajax(
        {
            url:urlPath+'/pharmacyInfo/gerPrescriptDetail',
            type:'GET',
            data:{pid:pid},
            success:function (data) {
                if(data.code == 200){
                    if(data.data != null){
                        for(var i=0;i<data.data.length;i++){
                            var html ="<tr name='med'>";
                            html += "<td name='pid'>"+data.data[i].pid+"</td>";
                            html += "<td name='mid'>"+data.data[i].mid+"</td>";
                            html += "<td>"+data.data[i].name+"</td>";
                            html += "<td name='productTime'>"+data.data[i].productTime+"</td>";
                            html += "<td>"+data.data[i].counterId+"</td>";
                            html += "<td>"+data.data[i].counterLeft+"</td>";
                            html += "<td>"+data.data[i].repertoryId+"</td>";
                            html += "<td>"+data.data[i].repertoryLeft+"</td>";
                            html += "<td name='num'>"+data.data[i].num+"</td>";
                            html += "</tr>";
                            $("tbody").append(html);
                        }
                    }
                }else{
                    toastr.error(data.msg);
                }
            }
        }
    );
});

// 药品出库
function shellOut() {
    var pid = $("#hiddenData").val();
    var mids = new Array();
    var productTimes = new Array();
    var nums = new Array();
    $("tr[name='med']").each(function () {
        mids.push($(this).children().eq(1).text());
        productTimes.push($(this).children().eq(3).text());
        nums.push($(this).children().eq(8).text());
    });

    $.ajax({
        url:urlPath+"/pharmacyInfo/shellOut",
        data:{pid:pid,mids:mids.toString(),productTimes:productTimes.toString(),nums:nums.toString()},
        type:"POST",
        success:function (data) {
            if(data.code==200){
                toastr.success("出库成功!");
            }else if(data.code == 501){
                toastr.error("出库失败!");
            }else{
                toastr.error(data.msg);
            }
        }
    })
}

function exportExcel() {
    var mids = new Array();
    var newMedNames = new Array();
    var counterPositions = new Array();
    var repertoryPositions = new Array();
    var mnums = new Array();
    $("tbody tr").each(function () {
        mids.push($(this).children().eq(1).text());
        newMedNames.push($(this).children().eq(2).text());
        counterPositions.push($(this).children().eq(4).text());
        repertoryPositions.push($(this).children().eq(6).text());
        mnums.push($(this).children().eq(8).text());
    })
    $.ajax({
        url:urlPath+"/pharmacyInfo/prescriptExport",
        type:"GET",
        data:{mids:mids.toString(),newMedNames:newMedNames.toString(),counterPositions:counterPositions.toString(),repertoryPositions:repertoryPositions.toString(),mnums:mnums.toString()},
        success:function (data) {
            return data
        },
        error:function (data) {
            toastr.error("导出失败!")
        }
    })
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