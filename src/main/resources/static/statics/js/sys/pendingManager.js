var layer;
layui.use('layer', function () {
    layer = layui.layer;
});
/*用户管理表格*/
layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#pendingDataTable'
        , url: '/v1/api/sys/pending/pendingListData'
        , method: 'post'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [[
            {type: 'checkbox'}
            /*{field:'id', width:80, title: 'ID', sort: true}*/
            /*,{ title: '序号',templet:'#tableIndex', sort: true,align:'center'}*/
            , {field: 'roomName', title: '会议室名称', align: 'center'}
            , {field: 'roomNo', title: '会议室编号', sort: true, align: 'center'}
            , {field: 'startTime', title: '开始时间', align: 'center'}
            , {field: 'endTime', title: '结束时间', align: 'center'}
            , {field: 'applicant', title: '申请人', minWidth: 100, align: 'center'} //minWidth：局部定义当前单元格的最小宽度，layui 2.2.1 新增
            , {field: 'appTime', title: '申请时间', align: 'center'}
            , {
                field: 'status', title: '状态', sort: true, align: 'center', templet: function (obj) {
                    if (0 === obj.status) {
                        return '待审批';
                    } else if (1 === obj.status) {
                        return '已审批';
                    } else if (2 === obj.status) {
                        return '已审批';
                    } else if (3 === obj.status) {
                        return '申请已过期';
                    } else if (4 === obj.status) {
                        return '已取消';
                    } else if (5 === obj.status) {
                        return '会议室已被删除';
                    } else {
                        return '已审批';
                    }
                }
            }
            , {
                field: 'status', title: '审批结果', sort: true, align: 'center', templet: function (obj) {
                    if (1 === obj.status || 6 === obj.status) {
                        return '通过';
                    } else if (2 === obj.status) {
                        return '未通过';
                    } else {
                        return '';
                    }
                }
            }
            , {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
        ]]
        , page: true
        , id: 'pendingLayerDataTable'
        , done: function (res, curr, count) {
        }
    });

    /*删除行*/
    table.on('tool(pendingDataFilter)', function (obj) {
        var eventData = obj.data;
        var layEvent = obj.event;
        var applyId = eventData.id;
        var roomId = eventData.roomId;
        if (layEvent === 'del') {
            layer.confirm('真的删除行么', function (index) {
                //删除对应行（tr）的DOM结构，并更新缓存
                //向服务端发送删除指令
                layer.close(index);
                var applyId = eventData.id;
                var roomId = eventData.roomId;
                $.ajax({
                    url: '/v1/api/sys/apply/delApply',
                    data: {applyId: applyId, roomId: roomId},
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        obj.del();
                        layer.msg(data.msg);
                    }
                });
            });
        }
        /*审批申请*/
        else if (layEvent === 'approval') {
            if (eventData.status === 1) {
                layer.msg('是否重新审批', {
                    time: 0
                    , btn: ['确认', '取消'],
                    yes: function (index) {
                        layer.close(index);
                        layer.msg('是否通过该申请？', {
                            time: 0,
                            btn: ['通过', '不通过', '取消'], //按钮
                            yes: function (index) {
                                layer.close(index);
                                accApply(applyId, roomId);
                            },
                            btn2: function (index) {
                                layer.close(index);
                                failApply(applyId, roomId);
                            },
                            btn3: function (index) {
                                layer.close(index);
                            }
                        });
                    }
                });
            } else if (eventData.status === 2) {
                layer.msg('是否重新审批', {
                    time: 0
                    , btn: ['确认', '取消'],
                    yes: function (index) {
                        layer.close(index);
                        layer.msg('是否通过该申请？', {
                            time: 0,
                            btn: ['通过', '不通过', '取消'], //按钮
                            yes: function (index) {
                                layer.close(index);
                                accApply(applyId, roomId);
                            },
                            btn2: function (index) {
                                layer.close(index);
                                failApply(applyId, roomId);
                            },
                            btn3: function (index) {
                                layer.close(index);
                            }
                        });
                    }
                });
            } else if (eventData.status === 3) {
                layer.msg('此申请已过期', {icon: 0});
            } else if (eventData.status === 4) {
                layer.msg('此申请已取消', {icon: 0});
            } else if (eventData.status === 5) {
                layer.msg('该会议室已经被删除', {icon: 0});
            } else {
                layer.msg('是否通过该申请？', {
                    time: 0,
                    btn: ['通过', '不通过', '取消'], //按钮
                    yes: function (index) {
                        layer.close(index);
                        accApply(applyId, roomId);
                    },
                    btn2: function (index) {
                        layer.close(index);
                        failApply(applyId, roomId);
                    },
                    btn3: function (index) {
                        layer.close(index);
                    }
                });
            }

        }
    });
    /*搜索后重载表格*/
    var $ = layui.$, active = {
        reload: function () {
            var condition = $('#condition');

            //执行重载
            table.reload('pendingLayerDataTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    pendingCondition: condition.val()
                }
            }, 'data');
        }
    };

    $('#pendingSeach').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});

function accApply(applyId, roomId) {
    $.ajax({
        url: '/v1/api/sys/pending/approvalApply',
        type: 'post',
        dataType: 'json',
        data: {applyId: applyId, roomId: roomId, flag: true},
        success: function (data) {
            if (data.code === 0) {
                layer.msg('审批成功', {icon: 1});
                parent.location.reload('pendingLayerDataTable', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                });
            }
        }
    });
}

function failApply(applyId, roomId) {
    $.ajax({
        url: '/v1/api/sys/pending/approvalApply',
        type: 'post',
        dataType: 'json',
        data: {applyId: applyId, roomId: roomId, flag: false},
        success: function (data) {
            if (data.code === 0) {
                layer.msg('审批成功', {icon: 1});
                parent.location.reload('pendingLayerDataTable', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                });
            }
        }
    });
}