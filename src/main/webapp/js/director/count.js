$(function () {
    $.ajax({
        url:urlPath+"/directorInfo/getCountOfMed",
        type:"POST",
        success:function (result) {
            var data1 = new Array();
            var data2 = new Array();
            if(result.code==200){
                if(result.data!=null){
                    var list = result.data;
                    for(var i=0;i<list.length;i++){
                        data1.push(list[i].name);
                        data2.push({value:list[i].count, name:list[i].name})
                    }

                    // 基于准备好的dom，初始化echarts图表
                    var myChart = echarts.init(document.getElementById('echarts'));
                    option = {
                        title : {
                            text: '南丁格尔玫瑰图',
                            subtext: '近三个月的销量',
                            x:'center'
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        legend: {
                            x : 'center',
                            y : 'bottom',
                            data:data1
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                dataView : {show: true, readOnly: false},
                                magicType : {
                                    show: true,
                                    type: ['pie', 'funnel']
                                },
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        series : [
                            {
                                name:'半径模式',
                                type:'pie',
                                radius : [20, 110],
                                center : ['25%', 200],
                                roseType : 'radius',
                                width: '40%',       // for funnel
                                max: 40,            // for funnel
                                itemStyle : {
                                    normal : {
                                        label : {
                                            show : false
                                        },
                                        labelLine : {
                                            show : false
                                        }
                                    },
                                    emphasis : {
                                        label : {
                                            show : true
                                        },
                                        labelLine : {
                                            show : true
                                        }
                                    }
                                },
                                data:data2
                            },
                            {
                                name:'面积模式',
                                type:'pie',
                                radius : [30, 110],
                                center : ['75%', 200],
                                roseType : 'area',
                                x: '50%',               // for funnel
                                max: 40,                // for funnel
                                sort : 'ascending',     // for funnel
                                data:data2
                            }
                        ]
                    };
                    // 为echarts对象加载数据
                    myChart.setOption(option);
                }else {
                    toastr.info("暂无数据!");
                }
            }else{
                toastr.info(result.msg);
            }
        }
    })

    // option = {
    //     title : {
    //         text: '南丁格尔玫瑰图',
    //         subtext: '纯属虚构',
    //         x:'center'
    //     },
    //     tooltip : {
    //         trigger: 'item',
    //         formatter: "{a} <br/>{b} : {c} ({d}%)"
    //     },
    //     legend: {
    //         x : 'center',
    //         y : 'bottom',
    //         data:['rose1','rose2','rose3','rose4','rose5','rose6','rose7','rose8']
    //     },
    //     toolbox: {
    //         show : true,
    //         feature : {
    //             mark : {show: true},
    //             dataView : {show: true, readOnly: false},
    //             magicType : {
    //                 show: true,
    //                 type: ['pie', 'funnel']
    //             },
    //             restore : {show: true},
    //             saveAsImage : {show: true}
    //         }
    //     },
    //     calculable : true,
    //     series : [
    //         {
    //             name:'半径模式',
    //             type:'pie',
    //             radius : [20, 110],
    //             center : ['25%', 200],
    //             roseType : 'radius',
    //             width: '40%',       // for funnel
    //             max: 40,            // for funnel
    //             itemStyle : {
    //                 normal : {
    //                     label : {
    //                         show : false
    //                     },
    //                     labelLine : {
    //                         show : false
    //                     }
    //                 },
    //                 emphasis : {
    //                     label : {
    //                         show : true
    //                     },
    //                     labelLine : {
    //                         show : true
    //                     }
    //                 }
    //             },
    //             data:[
    //                 {value:10, name:'rose1'},
    //                 {value:5, name:'rose2'},
    //                 {value:15, name:'rose3'},
    //                 {value:25, name:'rose4'},
    //                 {value:20, name:'rose5'},
    //                 {value:35, name:'rose6'},
    //                 {value:30, name:'rose7'},
    //                 {value:40, name:'rose8'}
    //             ]
    //         },
    //         {
    //             name:'面积模式',
    //             type:'pie',
    //             radius : [30, 110],
    //             center : ['75%', 200],
    //             roseType : 'area',
    //             x: '50%',               // for funnel
    //             max: 40,                // for funnel
    //             sort : 'ascending',     // for funnel
    //             data:[
    //                 {value:10, name:'rose1'},
    //                 {value:5, name:'rose2'},
    //                 {value:15, name:'rose3'},
    //                 {value:25, name:'rose4'},
    //                 {value:20, name:'rose5'},
    //                 {value:35, name:'rose6'},
    //                 {value:30, name:'rose7'},
    //                 {value:40, name:'rose8'}
    //             ]
    //         }
    //     ]
    // };


})

// websocket及时通信
var ws = null;
$(function(){
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window) {
        // ws = new WebSocket("ws://localhost:8090/webSocketOneToOne/"+wid+",123");
        // ws = new WebSocket("ws://192.168.43.213:8090/webSocketOneToOne/"+wid+",123");
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