function searchPrescript(obj) {
    $("#head").html("<tr>\n" +
        "                            <th>药方ID</th>\n" +
        "                            <th>总价格</th>\n" +
        "                            <th>创建时间</th>\n" +
        "                            <th>操作</th>\n" +
        "                        </tr>");
    var content = $("#searchContent").val();
    if(content==null || content==""){
        toastr.error("请输入药方ID!");
    }else{
        $("#searchContent").val("");
        $.ajax({
            url:urlPath+"/charge/getPrescript",
            type:"POST",
            data:{content:content},
            success:function (data) {
                if(data.code === 200){
                    var html = "<tr>\n" +
                        "                            <th id='pid'>"+data.data.pid+"</th>\n" +
                        "                            <th>"+data.data.totalPrice+"</th>\n" +
                        "                            <th>"+data.data.createTime+"</th>\n";
                    if(data.data.isCharge == "0"){
                        html += "<th><button type=\"button\" class=\"btn btn-primary\" onclick=\"charge(this)\">收费</button></th></tr>"
                    }else{
                        html +="<th><a href='javascript:;' class=\"btn btn-success\">已收费</a></th></tr>"
                    }
                    $("#head").append(html);
                }else{
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

function charge(obj) {
    var pid = $("#pid").text();
    $.ajax({
        url:urlPath+"/charge/chargePrescript",
        type:"POST",
        data:{pid : pid},
        success:function (data) {
            if(data.code == 200){
                $(obj).parent().html("<a href='javascript:;' class=\"btn btn-success\">已收费</a>");
                toastr.success("收费成功!");
            }else {
                toastr.error(data.msg);
            }
        }
    })
}