var layer;
layui.use('layer', function () {
    layer = layui.layer;
})

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
