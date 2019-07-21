function searchPrescript(obj) {
    $("#head").html("<tr>\n" +
        "                            <th>药方ID</th>\n" +
        "                            <th>是否付费</th>\n" +
        "                            <th>是否出库</th>\n" +
        "                            <th>创建时间</th>\n" +
        "                            <th>操作</th>\n" +
        "                        </tr>");
    var content = $("#searchContent").val();
    if(content==null || content==""){
        toastr.error("请输入药方ID!");
    }else{
        $("#searchContent").val("");
        $.ajax({
            url:urlPath+"/pharmacyInfo/getPrescriptInfo",
            type:"GET",
            data:{content:content},
            success:function (data) {
                if(data.code === 200){
                    var html;
                    if(data.data.isCharge==1){
                        html = "<tr>"
                        html += "<th id='pid'>"+data.data.pid+"</th>"
                        html += "<th>已付费</th>" ;
                        if(data.data.isShell==0){
                            html += " <th>未出库</th>";
                            html += " <th>"+data.data.createTime+"</th>";
                            html += "<th><a type=\"button\" class=\"btn btn-info\" href='"+urlPath+"/pharmacyInfo/getView/pharmacydetail/"+data.data.pid+"'>查看详情</a></th></tr>"
                        }else{
                            html += " <th>已出库</th>";
                            html += " <th>"+data.data.createTime+"</th>";
                            html += "<th><a type=\"button\" class=\"btn btn-info\" href='javascript:;'>已出库</a></th></tr>"
                        }
                    }else{
                        html = "<tr>\n" +
                            "                            <th id='pid'>"+data.data.pid+"</th>\n" +
                            "                            <th>未付费</th>\n" +
                            "                            <th>未出库</th>\n" +
                            "                            <th>"+data.data.createTime+"</th>\n";
                        html += "<th><a href='javascript:;' class=\"btn btn-danger\" >请先付费</a></th></tr>"
                    }
                    $("#head").append(html);
                }else if(data.code === 201 ){

                    var html;

                    html = "<tr>"
                    html += "<th id='pid'>"+data.data.pid+"</th>"
                    html += "<th>已付费</th>" ;
                    if(data.data.isShell==0){
                        html += " <th>未出库</th>";
                        html += " <th>"+data.data.createTime+"</th>";
                        html += "<th><a type=\"button\" class=\"btn btn-info\" href='"+urlPath+"/pharmacyInfo/getView/pharmacydetail/"+data.data.pid+"'>查看详情</a></th></tr>"
                    }else{
                        html += " <th>已出库</th>";
                        html += " <th>"+data.data.createTime+"</th>";
                        html += "<th><a type=\"button\" class=\"btn btn-info\" href='javascript:;'>已出库</a></th></tr>"
                    }

                    html = "<tr>\n" +
                        "                            <th id='pid'>"+data.data.pid+"</th>\n" +
                        "                            <th>未付费</th>\n" +
                        "                            <th>未出库</th>\n" +
                        "                            <th>"+data.data.createTime+"</th>\n";
                    html += "<th><a href='javascript:;' class=\"btn btn-danger\" onclick='dropPrescript(this)'>作废</a></th></tr>"

                    $("#head").append(html);
                    toastr.error("该药方存在下架药品，请作废!");
                }else {
                    toastr.error(data.msg);
                }
            }
        });
    }
}

function keyup(event) {
    if(event.keyCode == '13')
    {
        searchPrescript();
    }
}
$(document).keydown(function(event){
    if(event.keyCode==13){
        searchPrescript();
    }
});

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