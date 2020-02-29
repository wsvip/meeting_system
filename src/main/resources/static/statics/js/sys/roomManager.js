var layer;
layui.use('layer', function () {
    layer = layui.layer;
});
/*用户管理表格*/
layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#roomDataTable'
        , url: '/v1/api/sys/room/roomListData'
        , method: 'post'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [[
            {type: 'checkbox'}
            /*{field:'id', width:80, title: 'ID', sort: true}*/
            /*,{ title: '序号',templet:'#tableIndex', sort: true,align:'center'}*/
            , {field: 'roomName', title: '会议室名称', align: 'center'}
            , {field: 'roomNo', title: '会议室编号', sort: true, align: 'center'}
            , {field: 'createTime', title: '创建时间', align: 'center'}
            , {field: 'capacity', title: '可容纳人数', align: 'center'}
            , {field: 'principal', title: '负责人', minWidth: 100, align: 'center'} //minWidth：局部定义当前单元格的最小宽度，layui 2.2.1 新增
            , {
                field: 'status', title: '状态', sort: true, align: 'center', templet: function (obj) {
                    if (0 === obj.status) {
                        return '申请中';
                    } else if (1 === obj.status) {
                        return '使用中';
                    } else {
                        return '空闲';
                    }
                }
            }
            , {field: 'remark', title: '备注', align: 'center'}
            , {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
        ]]
        , page: true
        , id: 'roomLayerDataTable'
        , done: function (res, curr, count) {
            console.log(res);
            console.log(curr);
            console.log(count);
        }
    });

    /*删除行*/
    table.on('tool(roomDataFilter)', function (obj) {
        var eventData = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'del') {
            layer.confirm('真的删除行么', function (index) {
                //删除对应行（tr）的DOM结构，并更新缓存
                //向服务端发送删除指令
                layer.close(index);
                var roomId = eventData.id;
                $.ajax({
                    url: '/v1/api/sys/room/delRoomData',
                    data: {roomId: roomId},
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        obj.del();
                        layer.msg(data.msg);
                    }
                });
            });
        }
        /*编辑会议室信息*/
        else if (layEvent === 'edit') {
            layer.open({
                type: 2,
                title: '修改会议室',
                area: ['600px', '500px'],
                shadeClose: true,
                shade: [0.8, '#808283'],
                content: ['/v1/api/sys/room/editRoom?id=' + eventData.id, 'no'],
            });
        }
        /*申请会议室*/
        else if(layEvent==='apply'){
            if (eventData.status!==2){
                layer.msg("该会议室未空闲");
            }else{
                layer.open({
                    type: 2,
                    title: '申请会议室',
                    area: ['650px', '580px'],
                    shadeClose: true,
                    shade: [0.8, '#808283'],
                    content: ['/v1/api/sys/room/applyRoom?id=' + eventData.id, 'no'],
                });
            }
        }

    });
    /*搜索后重载表格*/
    var $ = layui.$, active = {
        reload: function () {
            var condition = $('#condition');

            //执行重载
            table.reload('roomLayerDataTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    roomCondition: condition.val()
                }
            }, 'data');
        }
    };

    $('#roomSeach').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


/*新增会议室*/
function addRoom() {
    var index = layer.open({
        type: 2,
        title: '新增会议室',
        area: ['600px', '400px'],
        shadeClose: true,
        shade: [0.8, '#808283'],
        content: ['/v1/api/sys/room/addRoom', 'no'],
    });
}

/*提交表单*/
function submitRoomDataForm() {
    var roomData = $("#roomData");
    roomData.bootstrapValidator('validate');//提交验证
    if (roomData.data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
        $.ajax({
            url: '/v1/api/sys/room/addRoomDo',
            data: $("#roomData").serialize(),
            type: 'post',
            dataType: 'json',
            success: function (data) {
                layer.msg(data.msg);
                if (data.code === 0) {
                    //layer.close(layer.index);
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    //关闭layer弹窗之后重新加载layui表格
                    parent.layer.close(index);
                    parent.location.reload('roomLayerDataTable', {page: {curr: 1}});
                }
            }
        });
    }
}

/**
 * 修改会议室信息
 */
function editRoomDo() {
    var roomData = $("#editRoomForm");
    roomData.bootstrapValidator('validate');//提交验证
    if (roomData.data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
        $.ajax({
            url: '/v1/api/sys/room/editRoomDo',
            type: 'post',
            dataType: 'json',
            data: $("#editRoomForm").serialize(),
            success: function (data) {
                layer.msg(data.msg);
                if (data.code===0){
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index);
                    //parent.layer.table.reload('roomLayerDataTable',{page:{curr:1}})
                    parent.location.reload('roomLayerDataTable', {page: {curr: 1}});
                }
            }
        });
    }
}
/**
 * 申请会议室
 */
function applyRoomDo() {
    var roomData = $("#applyRoomForm");
    roomData.bootstrapValidator('validate');//提交验证
    if (roomData.data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
        $.ajax({
            url: '/v1/api/sys/room/applyRoomDo',
            type: 'post',
            dataType: 'json',
            data: $("#applyRoomForm").serialize(),
            success: function (data) {
                layer.msg(data.msg);
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index);
                //parent.layer.table.reload('roomLayerDataTable',{page:{curr:1}})
                parent.location.reload('roomLayerDataTable', {page: {curr: 1}});
            }
        });
    }
}

/*表单验证*/
function validetorRoom(obj) {
    obj.bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            roomName: {
                //threshold: 6,//当用户名超过6位时才发送ajax请求进行用户名校验
                message: 'The roomName is not valid',
                validators: {
                    notEmpty: {
                        message: '会议名称不能为空'
                    }
                }
            },
            roomNo: {
                message: 'The roomNo is not valid',
                validators: {
                    notEmpty: {
                        message: '会议室编号不能为空'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9]+$/,
                        message: '会议室编号不能包含中文和特殊字符'
                    }
                },
            },
            principalComfirm: {
                validators: {
                    notEmpty: {
                        message: '会议室负责人不能为空'
                    }
                }
            },
            capacity: {
                //threshold: 11,//当邮箱超过6位时才发送ajax请求进行邮箱校验
                validators: {
                    notEmpty: {
                        message: '可容纳人数不能为空'
                    },
                    regexp: {
                        regexp: /^[0-9]+$/,
                        message: '可容纳人数需填写正整数'
                    }
                }
            },
            startTime:{
                validators: {
                    notEmpty: {
                        message: '开始时间不能为空'
                    }
                }
            },
            approverComfirm:{
                validators: {
                    notEmpty: {
                        message: '审批人不能为空'
                    }
                }
            }
        },
    });
}

/**
 * 选择负责人
 */
function selectUser(obj,nameObj,objId) {
    var roomData = {};
    var pidInput = obj;//$('#principal');
    var nameInput = nameObj;//$('input[name=principal]');
    var principalIdInput = objId;//$('#principalId');
    var index = layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        shade: [0.8, '#808283'],
        skin: 'selectPermArea',
        area: ['500px', '350px'],
        content: '<script type="text/html" id="toolbar">' +
            '  <div class="layui-btn-container">' +
            '    <button class="layui-btn layui-btn-sm" lay-event="getCheckData">确认</button>' +
            '  </div>' +
            '</script>' +
            '<div style="padding: 2px 10px 2px 10px"><div id="principalDataTable" lay-filter="roomDataFilter"></div></div> ',
        success: function () {
            var table = layui.table;
            table.render({
                elem: '#principalDataTable'
                , url: '/v1/api/sys/user/userListData'
                , toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
                , page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
                    layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
                    //,curr: 5 //设定初始在第 5 页
                    , groups: 1 //只显示 1 个连续页码
                    , first: false //不显示首页
                    , last: false //不显示尾页

                }
                , limit: 5 //每页默认显示的数量
                , method: 'post'
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    {type: 'radio'}
                    /*{field:'id', width:80, title: 'ID', sort: true}*/
                    /*,{ title: '序号',templet:'#tableIndex', sort: true,align:'center'}*/
                    , {field: 'username', title: '用户名', align: 'center'}
                    , {field: 'nickname', title: '姓名', sort: true, align: 'center'}
                ]]
                //, page: true
                , id: 'userLayerDataTable'
                , done: function (res, curr, count) {
                }
            });
            //头工具栏事件
            table.on('toolbar(roomDataFilter)', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id);
                switch (obj.event) {
                    case 'getCheckData':
                        var data = checkStatus.data;
                        roomData = {title: data[0].nickname, id: data[0].id};
                        layer.close(index);
                        break;
                }
            });
        },
        end: function () {
            pidInput.val(roomData.title);
            principalIdInput.val(roomData.id);
            nameInput.val(roomData.title);
        }
    });
}

/*时间选择*/
layui.use('laydate',function () {
    var laydate = layui.laydate;
    //日期范围
    laydate.render({
        elem: '#startTime'
        ,type:'datetime'
        //,format: 'yyyy-MM-dd HH:mm:ss|yyyy-MM-dd HH:mm:ss'
        ,range: true
        ,min: 0
        ,max: '2099-12-31 00:00:00'
    });
});