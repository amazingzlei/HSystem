$(function () {
    $.ajax(
        {
            url:urlPath+'/directorInfo/getAllApplicating',
            type:'GET',
            success:function (data) {
                if(data.code==200){
                    if(data.data!= null){
                        var page_count = data.data.size;
                        var list = data.data;
                        $('tbody').empty();
                        for(var i=0;i<list.length;i++){
                            showPageEmployeeData(list[i]);
                        }
                    }
                }else if(data.code==501){
                    toastr.info(data.msg);
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
    if(data.mid==null || data.mid==''){
        html += "<td>"+data.newMedName+"</td>";
    }else{
        html += "<td>"+data.mid+"</td>";
    }
    html += "<td>"+data.mnum+"</td>";
    html += "<td><a href='#' class='btn btn-primary btn-xs' data-toggle='modal' data-target='#customerEditDialog' onclick='edit(this)'>编辑</a>";
    html += "<a href='#' class='btn btn-danger btn-xs' onclick='del(this)'>删除</a></td>";
    html += "</tr>";
    $("tbody").append(html);
}

// 编辑申请药品信息
function edit(obj) {
    var name = $(obj).parent().prev().prev().text();
    var num = $(obj).parent().prev().text();
    $("#medName").val(name);
    $("#medNum").val(num);
}

// 修改药品信息
function doSub() {
    var name = $("#medName").val();
    var num = $("#medNum").val();
    var flag = true;
    if(medNum==null || medNum==''){
        toastr.error("请输入数量!");
        flag = false;
        return;
    }
    if(isNaN(num)){
        toastr.error("请输入数字!");
        flag = false;
        return;
    }
    if(num<10){
        toastr.error("数量必须大于10!");
        flag = false;
        return;
    }
    if(num>50000){
        toastr.error("数量必须小于50000!");
        flag = false;
        return;
    }
    if(flag){
        $.ajax({
            url:urlPath+"/directorInfo/updatePurchaseInfo",
            type:"POST",
            data:{medName:name,medNum:num},
            success:function (data) {
                if(data.code==200){
                    toastr.success("更新成功!");
                    // $('#customerEditDialog').map(function() {
                    //     $(this).modal('hide');
                    // });
                    setTimeout(function(){ window.location.reload(); }, 2000)
                }else{
                    toastr.error(data.msg);
                }
            }
        })
    }
}

//删除
function del(obj) {
    swal({
        title: "操作提示",      //弹出框的title
        text: "确定删除吗？",   //弹出框里面的提示文本
        type: "warning",        //弹出框类型
        showCancelButton: true, //是否显示取消按钮
        confirmButtonColor: "#DD6B55",//确定按钮颜色
        cancelButtonText: "取消",//取消按钮文本
        confirmButtonText: "确定",//确定按钮上面的文档s
        closeOnConfirm: true
    }, function () {
        var name = $(obj).parent().prev().prev().text();
        $.ajax({
            url:urlPath+"/directorInfo/delPurchaseInfo",
            type:"POST",
            data:{medName:name},
            success:function (data) {
                if(data.code==200){
                    toastr.success("删除成功!");
                    setTimeout(function(){ window.location.reload(); }, 2000)
                }else{
                    toastr.error(data.msg);
                }
            }
        })
    });
}

// 提交申请
function subPurchase() {
    if($('tbody tr').size()!=0){
        swal({
            title: "操作提示",      //弹出框的title
            text: "确定提交吗？",   //弹出框里面的提示文本
            type: "warning",        //弹出框类型
            showCancelButton: true, //是否显示取消按钮
            confirmButtonColor: "#DD6B55",//确定按钮颜色
            cancelButtonText: "取消",//取消按钮文本
            confirmButtonText: "确定",//确定按钮上面的文档s
            closeOnConfirm: true
        }, function () {
            $.ajax({
                url:urlPath+"/directorInfo/putPurchase",
                type:"POST",
                success:function (data) {
                    if(data.code==200){
                        toastr.success("提交成功!");
                        // 发送信息
                        ws.send(JSON.stringify({'message':'您有待审核的采购单','role':data.data,'socketId':"123"}));
                        setTimeout(function(){ window.location.reload(); }, 2000)
                    }else{
                        toastr.error(data.msg);
                    }
                }
            })
        });
    }else{
        toastr.error("请添加需要采购的药品!");
    }
}

// websocket及时通信
var ws = null;
$(function(){
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window) {
        ws = new WebSocket("ws://localhost:8080/hsystem/webSocketOneToOne/"+wid+",123");
        // ws = new WebSocket("ws://192.168.43.213:8090/webSocketOneToOne/"+wid+",123");
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