var layer;
layui.use('layer', function () {
    layer = layui.layer;
});
layui.use([ 'table', 'treetable'], function () {
    var permTreetable = layui.treetable;
    var permTable = layui.table;
    // 渲染表格
    var renderTable = function () {
        //layer.load(2);
        permTreetable.render({
            id:'permTable',
            treeColIndex: 1,
            treeSpid: -1,
            treeIdName: 'id',
            treePidName: 'pid',
            height:'750px',
            treeDefaultClose: false,
            treeLinkage: false,
            elem: '#permTreeTable',
            url: '/v1/api/sys/perm/permTreeTable',
            page: false,
            cols: [[
                {type: 'numbers',title:'序号',singleLine:true},
                {field: 'name', title: '名称',singleLine:true},
                {field: 'permission', title: '权限标识',singleLine:true},
                {field: 'url', title: '路径',singleLine:true},
                {
                    field: 'resourceType', title: '类型', align: 'center',singleLine:true, templet: function (data) {
                        if (data.resourceType == -1) {//菜单
                            return '<span class="layui-badge layui-bg-blue">目录</span>'
                        } else if (data.resourceType == 0) {//按钮
                            return '<span class="layui-badge layui-bg-gray">按钮</span>'
                        } else if (data.resourceType == 1) {//目录
                            return '<span class="layui-badge layui-bg-orange">菜单</span>'
                        }
                    }
                },
                {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
            ]],
            done: function () {
                //layer.closeAll('loading');
            }
        });
        permTable.on('tool(permTreeTableFilter)', function (obj) {
            var data = obj.data;
            var event = obj.event;
            //删除行
            if (event === 'del') {
                layer.confirm('真的删除行么', function (index) {
                    //删除对应行（tr）的DOM结构，并更新缓存
                    //向服务端发送删除指令
                    layer.close(index);
                    var permId = data.id;
                    $.ajax({
                        url: '/v1/api/sys/perm/delPerm',
                        data: {permId: permId},
                        type: 'POST',
                        dataType: 'json',
                        success: function (data) {
                            renderTable();
                            layer.msg(data.msg);
                        }
                    });
                });
            }
            //修改
            else if (event === 'edit') {
                layer.open({
                    type: 2,
                    title: '修改权限',
                    area: ['600px', '500px'],
                    shadeClose: true,
                    shade: [0.8, '#808283'],
                    content: ['/v1/api/sys/perm/edit?permId=' + data.id, 'no'],
                });
            }
        })

    };
    renderTable();
    /*搜索*/
    $('#permSeach').click(function () {
        var keyword = $('#condition').val();
        var searchCount = 0;
        $('#permTreeTable').next('.treeTable').find('.layui-table-body tbody tr td').each(function () {
            $(this).css('background-color', 'transparent');
            var text = $(this).text();
            if (keyword != '' && text.indexOf(keyword) >= 0) {
                $(this).css('background-color', 'rgba(20,250,140,0.96)');
                if (searchCount == 0) {
                    permTreetable.expandAll('#permTreeTable');
                    $('html,body').stop(true);
                    $('html,body').animate({scrollTop: $(this).offset().top - 150}, 500);
                }
                searchCount++;
            }
        });
        if (keyword == '') {
            layer.msg("请输入搜索内容", {icon: 5});
        } else if (searchCount == 0) {
            layer.msg("没有匹配结果", {icon: 5});
        }
    });
    /*全部展开*/
    $("#btn_expand").click(function () {
        permTreetable.expandAll("#permTreeTable");
    });
    /*全部折叠*/
    $("#btn_fold").click(function () {
        permTreetable.foldAll("#permTreeTable");
    });
    /*刷新表格*/
    $("#btn_refresh").click(function () {
        renderTable();
    });
});
/**
 * 打开新增权限弹框
 */
function addPerm() {
    var index = layer.open({
        type: 2,
        title: '新增权限',
        area: ['600px', '500px'],
        shadeClose: true,
        shade: [0.8, '#808283'],
        content: ['/v1/api/sys/perm/addPerm', 'no'],
    });
}

/**
 * 新增权限
 */
function submitPermDataForm() {
    $.ajax({
        url: '/v1/api/sys/perm/addPermDo',
        type: 'post',
        data: $("#permData").serialize(),
        dataType: 'json',
        success: function (data) {
            layer.msg(data.mag);
            if (data.code==0){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                //关闭layer弹窗之后重新加载layui表格
                parent.layer.close(index);
                parent.location.reload('permTable', {page: {curr: 1}});
            }
        }

    });
}

/**
 * 修改权限
 */
function submitEditPermForm() {
    $.ajax({
        url: '/v1/api/sys/perm/editDo',
        type: 'post',
        data: $("#permData").serialize(),
        dataType: 'json',
        success: function (data) {
            layer.msg(data.mag);
            if (data.code==0){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                //关闭layer弹窗之后重新加载layui表格
                parent.layer.close(index);
                parent.location.reload('permTable', {page: {curr: 1}});
            }
        }
    });
}

/**
 * 选择权限界面
 */
function selectPerm() {
    var permData={};
    var pidInput=$('#pid');
    var nameInput=$('input[name=pid]');
    var index = layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        shade: [0.8, '#808283'],
        skin: 'selectPermArea',
        area: ['400px', '300px'],
        content: '<div id="permTree"></div>',
        success: function () {
            layui.use('tree', function () {
                var tree = layui.tree;
                tree.render({
                    elem: '#permTree' //默认是点击节点可进行收缩
                    , data: [treeData]
                    //, accordion: true
                    ,spread:true
                    ,showLine:false
                    , click: function (obj) {

                        permData={title:obj.data.title,id:obj.data.id};
                        layer.close(index);
                    }
                })
            })
        },
        end: function () {
            pidInput.val(permData.title);
            nameInput.val(permData.id);
        }
    });
}
