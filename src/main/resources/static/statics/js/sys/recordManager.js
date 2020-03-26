/**
 * 会议记录
 */
var layer;
layui.use('layer', function () {
    layer = layui.layer;
});
/*会议记录管理表格*/
layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#recordDataTable'
        , url: '/v1/api/sys/record/recordListData'
        , method: 'post'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [[
            {type: 'checkbox'}
            /*{field:'id', width:80, title: 'ID', sort: true}*/
            /*,{ title: '序号',templet:'#tableIndex', sort: true,align:'center'}*/
            , {field: 'recordName', title: '会议名称', align: 'center'}
            , {field: 'host', title: '主持人', sort: true, align: 'center'}
            , {field: 'startTime', title: '会议开始时间',sort: true, align: 'center'}
            , {field: 'endTime', title: '会议结束时间',sort: true, align: 'center'}
            , {field: 'roomName', title: '会议室', minWidth: 100, align: 'center'} //minWidth：局部定义当前单元格的最小宽度，layui 2.2.1 新增
            , {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
        ]]
        , page: true
        , id: 'recordLayerDataTable'
        , done: function (res, curr, count) {
        }
    });

    /*删除行*/
    table.on('tool(recordDataFilter)', function (obj) {
        var eventData = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'del') {
            layer.confirm('真的删除行么', function (index) {
                //删除对应行（tr）的DOM结构，并更新缓存
                //向服务端发送删除指令
                layer.close(index);
                var recordId = eventData.id;
                $.ajax({
                    url: '/v1/api/sys/record/delRecordData',
                    data: {recordId: recordId},
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        obj.del();
                        layer.msg(data.msg);
                    }
                });
            });
        }
        /*编辑会议记录*/
        else if (layEvent === 'edit') {
            layer.open({
                type: 2,
                title: '修改会议记录',
                area: ['700px', '700px'],
                shadeClose: true,
                shade: [0.8, '#808283'],
                content: ['/v1/api/sys/record/editRecord?id=' + eventData.id, 'no'],
            });
        }
        /*查看详情*/
        else if (layEvent ==='detail'){
            layer.open({
                type: 2,
                title: '会议记录详情',
                area: ['700px', '630px'],
                shadeClose: true,
                shade: [0.8, '#808283'],
                content: ['/v1/api/sys/record/detail?id=' + eventData.id, 'no'],
            });
        }

    });
    /*搜索后重载表格*/
    var $ = layui.$, active = {
        reload: function () {
            var condition = $('#condition');

            //执行重载
            table.reload('recordLayerDataTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    recordCondition: condition.val()
                }
            }, 'data');
        }
    };

    $('#recordSeach').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


/*新增会议记录*/
function addRecord() {
    var index = layer.open({
        type: 2,
        title: '新增会议记录',
        area: ['700px', '700px'],
        shadeClose: true,
        shade: [0.8, '#808283'],
        content: ['/v1/api/sys/record/addRecord', 'no'],
    });
}

/*提交表单*/
function submitRecordDataForm() {
    var recordData = $("#recordData");
    recordData.bootstrapValidator('validate');//提交验证
    if (recordData.data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
        $.ajax({
            url: '/v1/api/sys/record/addRecordDo',
            data: $("#recordData").serialize(),
            type: 'post',
            dataType: 'json',
            success: function (data) {
                layer.msg(data.msg);
                if (data.code === 0) {
                    //layer.close(layer.index);
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    //关闭layer弹窗之后重新加载layui表格
                    parent.layer.close(index);
                    parent.location.reload('recordLayerDataTable', {page: {curr: 1}});
                }
            }
        });
    }
}

/**
 * 修改会议记录信息
 */
function editRecordDo() {
    var recordData = $("#editRecordData");
    recordData.bootstrapValidator('validate');//提交验证
    if (recordData.data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
        $.ajax({
            url: '/v1/api/sys/record/editRecordDo',
            type: 'post',
            dataType: 'json',
            data: $("#editRecordData").serialize(),
            success: function (data) {
                layer.msg(data.msg);
                if (data.code===0){
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index);
                    //parent.layer.table.reload('recordLayerDataTable',{page:{curr:1}})
                    parent.location.reload('recordLayerDataTable', {page: {curr: 1}});
                }
            }
        });
    }
}

/*表单验证*/
function validetorrecord(obj) {
    obj.bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            recordName: {
                //threshold: 6,//当用户名超过6位时才发送ajax请求进行用户名校验
                message: 'The recordName is not valid',
                validators: {
                    notEmpty: {
                        message: '会议名称不能为空'
                    }
                }
            },
            roomNameComfirm: {
                validators: {
                    notEmpty: {
                        message: '会议室不能为空'
                    }
                }
            },
            commissionerComfirm: {
                validators: {
                    notEmpty: {
                        message: '会务专员不能为空'
                    }
                }
            },
            attendComfirm: {
                //threshold: 11,//当邮箱超过6位时才发送ajax请求进行邮箱校验
                validators: {
                    notEmpty: {
                        message: '参会人员不能为空'
                    }
                }
            },
            startTime:{
                validators: {
                    notEmpty: {
                        message: '会议时长不能为空'
                    }
                }
            },
            approverComfirm:{
                validators: {
                    notEmpty: {
                        message: '主持人不能为空'
                    }
                }
            },
            content:{
                validators: {
                    notEmpty: {
                        message: '会议内容不能为空'
                    }
                }
            }
        },
    });
}

/**
 * 选择会议主持人、会务专员、参会人员
 */
function selectRecordUser(obj,nameObj,objId,type) {
    var recordData = {};
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
            '<div style="padding: 2px 10px 2px 10px"><div id="principalDataTable" lay-filter="recordDataFilter"></div></div> ',
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
                    {type: type}
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
            table.on('toolbar(recordDataFilter)', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id);
                switch (obj.event) {
                    case 'getCheckData':
                        var data = checkStatus.data;
                        var title=new Array();
                        var id=new Array();
                        if (data.length>1){
                            for (var i = 0; i < data.length; i++) {
                                title[i]=data[i].nickname;
                                id[i]=data[i].id;
                            }
                            recordData = {title: title.join(','), id: id.join(',')};
                        }else{
                            recordData = {title: data[0].nickname, id: data[0].id};
                        }
                        layer.close(index);
                        break;
                }
            });
        },
        end: function () {
            pidInput.val(recordData.title);
            principalIdInput.val(recordData.id);
            nameInput.val(recordData.title);
            pidInput.val(recordData.title).trigger('input');
        }
    });
}

/**
 * 选择申请通过的会议室
 * @param obj
 * @param nameObj
 * @param objId
 * @param type
 */
function selectApplyed(obj,nameObj,objId,type) {
    var recordData = {};
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
            '<div style="padding: 2px 10px 2px 10px"><div id="principalDataTable" lay-filter="recordDataFilter"></div></div> ',
        success: function () {
            var table = layui.table;
            table.render({
                elem: '#principalDataTable'
                , url: '/v1/api/sys/apply/applyedListData'
                , toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
                , page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
                    layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
                    //,curr: 5 //设定初始在第 5 页
                    , groups: 1 //只显示 1 个连续页码
                    , first: false //不显示首页
                    , last: false //不显示尾页
                }
                , limit: 10 //每页默认显示的数量
                , method: 'post'
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    {type: type}
                    /*{field:'id', width:80, title: 'ID', sort: true}*/
                    /*,{ title: '序号',templet:'#tableIndex', sort: true,align:'center'}*/
                    , {field: 'roomName', title: '会议室', align: 'center'}
                    , {field: 'roomNo', title: '会议室编号', sort: true, align: 'center'}
                ]]
                //, page: true
                , id: 'applyLayerDataTable'
                , done: function (res, curr, count) {
                }
            });
            //头工具栏事件
            table.on('toolbar(recordDataFilter)', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id);
                switch (obj.event) {
                    case 'getCheckData':
                        var data = checkStatus.data;
                        recordData = {title: data[0].roomName, id: data[0].roomId};
                        layer.close(index);
                        break;
                }
            });
        },
        end: function () {
            pidInput.val(recordData.title);
            principalIdInput.val(recordData.id);
            nameInput.val(recordData.title);
            pidInput.val(recordData.title).trigger('input');
        }
    });
}

/*时间选择*/
layui.use('laydate',function () {
    var laydate = layui.laydate;
    //日期范围
    laydate.render({
        elem: '#_startTime'
        ,type:'datetime'
        //,format: 'yyyy-MM-dd HH:mm:ss|yyyy-MM-dd HH:mm:ss'
        ,range: true
        ,change:function (value) {
            $("#_startTime").val(value).trigger('input');
        }
    });
});