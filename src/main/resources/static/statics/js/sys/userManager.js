var layer;
layui.use('layer', function () {
    layer = layui.layer;
});
/*用户管理表格*/
layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#userDataTable'
        , url: '/v1/api/sys/user/userListData'
        , method: 'post'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [[
            {type: 'checkbox'}
            /*{field:'id', width:80, title: 'ID', sort: true}*/
            /*,{ title: '序号',templet:'#tableIndex', sort: true,align:'center'}*/
            , {field: 'username', title: '用户名', align: 'center'}
            , {field: 'nickname', title: '姓名', sort: true, align: 'center'}
            , {field: 'tellphone', title: '电话', align: 'center'}
            , {field: 'email', title: '邮箱', align: 'center'}
            , {field: 'loginIp', title: '登录IP', minWidth: 100, align: 'center'} //minWidth：局部定义当前单元格的最小宽度，layui 2.2.1 新增
            , {field: 'loginCount', title: '登录次数', sort: true, align: 'center'}
            , {
                field: 'loginAt', title: '登录时间', sort: true, align: 'center', templet: function (obj) {
                    if (null == obj.loginAt) {
                        return '';
                    } else {
                        return layui.util.toDateString((obj.loginAt) * 1000, "yyyy-MM-dd HH:mm:ss");
                    }
                }
            }
            , {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
        ]]
        , page: true
        , id: 'userLayerDataTable'
        , done: function (res, curr, count) {

        }
    });

    /*删除行*/
    table.on('tool(userDataFilter)', function (obj) {
        var eventData = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'del') {
            layer.confirm('真的删除该用户么', function (index) {
                //删除对应行（tr）的DOM结构，并更新缓存
                //向服务端发送删除指令
                layer.close(index);
                var userId = eventData.id;
                $.ajax({
                    url: '/v1/api/sys/user/delUserData',
                    data: {userId: userId},
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code===0){
                            obj.del();
                        }
                        layer.msg(data.msg);
                    }
                });
            });
        }
        /*编辑用户信息*/
        else if (layEvent === 'edit') {
            layer.open({
                type: 2,
                title: '修改用户',
                area: ['600px', '500px'],
                shadeClose: true,
                shade: [0.8, '#808283'],
                content: ['/v1/api/sys/user/editUser?userId=' + eventData.id, 'no'],
            });
        }

    });
    /*搜索后重载表格*/
    var $ = layui.$, active = {
        reload: function () {
            var condition = $('#condition');

            //执行重载
            table.reload('userLayerDataTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    userCondition: condition.val()
                }
            }, 'data');
        }
    };

    $('#userSeach').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});


/*新增用户*/
function addUser() {
    var index = layer.open({
        type: 2,
        title: '新增用户',
        area: ['600px', '500px'],
        shadeClose: true,
        shade: [0.8, '#808283'],
        content: ['/v1/api/sys/user/addUser', 'no'],
    });
}

/*提交表单*/
function submitUserDataForm() {
    var userData = $("#userData");
    userData.bootstrapValidator('validate');//提交验证
    if (userData.data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
        $.ajax({
            url: '/v1/api/sys/user/addUserDo',
            data: $("#userData").serialize(),
            type: 'post',
            dataType: 'json',
            success: function (data) {
                layer.msg(data.msg);
                if (data.code == 0) {
                    //layer.close(layer.index);
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    //关闭layer弹窗之后重新加载layui表格
                    parent.layer.close(index);
                    parent.location.reload('userLayerDataTable', {page: {curr: 1}});
                }
            }
        });
    }

}

/**
 * 修改用户
 */
function editUserDo() {
    $.ajax({
        url: '/v1/api/sys/user/editUserDo',
        type: 'post',
        dataType: 'json',
        data: $("#editUserForm").serialize(),
        success: function (data) {
            layer.msg(data.msg);
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index);
            //parent.layer.table.reload('userLayerDataTable',{page:{curr:1}})
            parent.location.reload('userLayerDataTable', {page: {curr: 1}});
        }
    });
}

/*表单验证*/
function validetorUser(obj) {
    obj.bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                //threshold: 6,//当用户名超过6位时才发送ajax请求进行用户名校验
                message: 'The username is not valid',
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 13,
                        message: '用户名长度为2至12位'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9]+$/,
                        message: '用户名不能包含中文和特殊字符'
                    },
                    remote: {
                        url: '/register/checkUsername',
                        message: '该用户名已存在'
                    },
                    different: {
                        field: 'password',
                        message: '用户名不能与密码一致'
                    }
                }
            },
            password: {
                message: 'password not null',
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    },
                    identical: {
                        field: 'confirmPassword',
                        message: '两次密码不一致'
                    },
                    different: {
                        field: 'username',
                        message: '密码不能与用户名相同'
                    }
                },
            },
            confirmPassword: {
                validators: {
                    notEmpty: {
                        message: '两次密码不一致'
                    },
                    identical: {
                        field: 'password',
                        message: '两次密码不一致'
                    },
                    different: {
                        field: 'username',
                        message: '两次密码不一致'
                    }
                }
            },
            email: {
                //threshold: 6,//当邮箱超过6位时才发送ajax请求进行邮箱校验
                validators: {
                    emailAddress: {
                        message: '邮箱格式不正确'
                    },
                    remote: {
                        url: '/register/checkEmail',
                        message: '该邮箱已存在'
                    },
                }
            },
            nickname: {
                validators: {
                    notEmpty: {
                        message: '姓名不能为空'
                    }
                }
            },
            tellphone: {
                //threshold: 11,//当邮箱超过6位时才发送ajax请求进行邮箱校验
                validators: {
                    notEmpty: {
                        message: '手机号不能为空'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '手机号格式不正确'
                    },
                    regexp: {
                        regexp: /^[0-9]+/,
                        message: '手机号码格式不正确'
                    },
                    remote: {
                        url: '/register/checkPhone',
                        message: '该手机号码已存在'
                    },
                },
            },
        },
    });
}
