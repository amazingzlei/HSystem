$(function () {
    var medName;
    $.ajax({
        url:path+"/doctor/getAllMedName",
        type:"GET",
        dataType:"json",
        success:function (data) {
            $("#search-platform").typeahead({
                source: data.data,
                items:10000,
                /**
                 * 在选中数据后的操作，这里的返回值代表了输入框的值
                 * @param obj
                 * @return 选中后，最终输入框里的值
                 */
                updater: function (obj) {
                    medName = obj.name;
                },
                afterSelect:function (obj) {
                    var flag = false;
                    $(".medName").each(function () {
                        if($(this).text() == medName){
                            var num = $(this).next().children("span").text();
                            $(this).next().children("span").text(parseInt(num)+1);
                            flag = true;
                        }
                    });
                    if(!flag){
                        $("#head").append("<tr>\n" +
                            "                            <th class='medName'>"+medName+"</th>\n" +
                            "                            <th><a href='javascript:;' class='btn btn-primary btn-xs' onclick=\"dis(this)\">-</a><span>1</span><a href='javascript:;' class='btn btn-primary btn-xs' onclick=\"add(this)\">+</a></th>\n" +
                            "                            <th><a href='javascript:;' class='btn btn-danger btn-xs' onclick=\"del(this)\">删除</a></th>\n" +
                            "                        </tr>");
                    }
                }
            });
        }
    });
});
function dis(obj) {
    var num = parseInt($(obj).next().text());
    if(num > 1){
        $(obj).next().text(num-1);
    }else{
        toastr.error('药品数量不能小于1!');
    }
}

function add(obj){
    var num = parseInt($(obj).prev().text());
    if(num < 100){
        $(obj).prev().text(num+1);
    }else{
        toastr.error('药品数量不能大于100!');
    }
}

function del(obj){
    swal({
        title: "操作提示",      //弹出框的title
        text: "确定删除吗？",   //弹出框里面的提示文本
        type: "warning",        //弹出框类型
        showCancelButton: true, //是否显示取消按钮
        confirmButtonColor: "#DD6B55",//确定按钮颜色
        cancelButtonText: "取消",//取消按钮文本
        confirmButtonText: "确定",//确定按钮上面的文档
        closeOnConfirm: true
    }, function () {
        $(obj).parent().parent().remove();
    });
}

//提交药方按钮
function subDtn() {
    // 判断是否有药
    if($(".medName").size()>0){
        var medNames = new Array();
        var nums = new Array();
        $(".medName").each(function () {
            medNames.push($(this).text());
            nums.push($(this).next().children("span").text());
        });
        $.ajax({
            url:path+"/doctor/addPrescript",
            data:{medNames:medNames.toString(),nums:nums.toString()},
            type:"POST",
            success:function (data) {
                if(data.code==200){
                    toastr.success(data.data);
                    $("#head").html("<tr>\n" +
                        "                            <th>药名</th>\n" +
                        "                            <th>数量</th>\n" +
                        "                            <th>操作</th>\n" +
                        "                        </tr>");
                }else{
                    toastr.error(data.msg);
                }
            }
        })
    }else{
        toastr.error('请选择要开具的药物!');
    }
}

function clean() {
    $("#edit_password_form input:gt(0)").val("");
}
