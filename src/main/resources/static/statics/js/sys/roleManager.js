/**
 * 角色管理
 */
var layer;
layui.use('layer',function () {
    layer= layui.layer;
});

/*角色列表*/
layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#roleDataTable'
        , url: '/v1/api/sys/role/roleListData'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [[
            {type: 'checkbox'}
            /*{field:'id', width:80, title: 'ID', sort: true}*/
            /*,{ title: '序号',templet:'#tableIndex', sort: true,align:'center'}*/
            , {field: 'rolename', title: '角色名称', align: 'center'}
            , {field: 'role', title: '角色标识', sort: true, align: 'center'}
            , {field: 'description', title: '角色说明', align: 'center'}
            , {field: 'disabled', title: '是否禁用', align: 'center',templet:function (data) {
                    if (data.disabled){
                        return '<span class="layui-badge layui-bg-red">是</span>'
                    }else{
                        return '<span class="layui-badge layui-bg-green">否</span>'
                    }
                }}
            , {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
        ]]
        , page: true
        , id: 'roleLayerDataTable'
    });
    table.on('tool(roleDataFilter)', function (obj) {
        var eventData = obj.data;
        var layEvent = obj.event;
        /*删除行*/
        if (layEvent === 'del') {
            layer.confirm('真的删除该角色么', function (index) {
                //删除对应行（tr）的DOM结构，并更新缓存
                //向服务端发送删除指令
                layer.close(index);
                var roleId = eventData.id;
                $.ajax({
                    url: '/v1/api/sys/role/delRole',
                    data: {roleId: roleId},
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code==0){
                            obj.del();
                        }
                        layer.msg(data.msg);
                    }
                });
            });
        }
        /*编辑角色信息*/
        else if (layEvent === 'edit') {
            layer.open({
                type: 2,
                title: '修改角色',
                area: ['600px', '500px'],
                shadeClose: true,
                shade: [0.8, '#808283'],
                content: ['/v1/api/sys/role/editRole?roleId=' + eventData.id, 'no'],
            });
        }
        /*分配用户*/
        else if (layEvent==='assUser'){
            layer.open({
                type: 2,
                title: '分配用户',
                area: ['1100px', '600px'],
                shadeClose: true,
                shade: [0.8, '#808283'],
                content: ['/v1/api/sys/role/assUser?roleId=' + eventData.id, 'no'],
            });
        }
        /*分配权限*/
        else if(layEvent==='assPerm'){
            layer.open({
                type: 2,
                title: '分配权限',
                area: ['600px', '500px'],
                shadeClose: true,
                shade: [0.8, '#808283'],
                content: ['/v1/api/sys/role/assPerm?roleId=' + eventData.id, 'no'],
            });
        }
    });
    /*搜索后重载表格*/
    var $ = layui.$, active = {
        reload: function () {
            var condition = $('#condition');

            //执行重载
            table.reload('roleLayerDataTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    roleCondition: condition.val()
                }
            }, 'data');
        }
    };

    $('#roleSeach').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});

/*跳转新增角色界面*/
function addRole() {
    layer.open({
        type: 2,
        title: '新增用户',
        area: ['600px', '500px'],
        shadeClose: true,
        shade: [0.8, '#808283'],
        content: ['/v1/api/sys/role/addRole', 'no'],
    });
}


/*新增角色*/
function submitRoleDataForm() {
    $.ajax({
        url:'/v1/api/sys/role/addRoleDo',
        type:'post',
        data:$("#roleData").serialize(),
        dataType: 'json',
        success:function (data) {
            layer.msg(data.msg);
            if (data.code==0){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                //关闭layer弹窗之后重新加载layui表格
                parent.layer.close(index);
                parent.location.reload('roleLayerDataTable',{page:{curr:1}});
            }

        }

    })
}

/*跳转编辑角色界面*/
function sysRole() {
    $.ajax({
       url:'/v1/api/sys/role/editRole',
       success:function (data) {

           
       } 
    });
}
/*提交修改角色表单*/
function editRoleDo() {
    $.ajax({
       url:'/v1/api/sys/role/editRoleDo',
       type:'post',
       dataType:'json',
       data:$("#editRoleForm").serialize(),
       success:function (data) {
           layer.msg(data.msg);
           if (data.code==0){
               //关闭弹窗，刷新表格
               var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
               //关闭layer弹窗之后重新加载layui表格
               parent.layer.close(index);
               parent.location.reload('roleLayerDataTable',{page:{curr:1}});
           }
       },
       error:function (data) {
           layer.msg(data.msg);
       }
    });
}


