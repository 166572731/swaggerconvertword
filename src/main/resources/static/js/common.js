layui.use(['code', 'jquery', 'util', 'layer', 'form'], function () {
    var $ = layui.jquery, util = layui.util, layer = layui.layer, form = layui.form;
    var example1 = new Vue({
        el: '#example-1',
        data: {
            title: "API自动化文档",
            basePath: "basePath",
            version: "1.0.0",
            items: {}
        },
        created: function () {
            var loadIndex = layer.load(3, {shade: 0.1});
            var _this = this;
            $.ajax({
                url: "/load",
                type: 'post',
                dataType: "JSON",
                data: {'url': sessionStorage.getItem("url"), 'refresh': false},
                success: function (data) {
                    layer.close(loadIndex);
                    if (data.thisUrl==='null'){
                        layer.msg(data.message);
                    }else{
                        $("#url").val(data.thisUrl);
                        sessionStorage.setItem("url", data.thisUrl);
                        _this.title = data.info.title;
                        _this.basePath = data.basePath;
                        _this.version = data.info.version;
                        _this.items = data.tableMap;
                    }
                }
            });
            util.fixbar({
                bar1: true,
                click: function (type) {
                    if (type === 'bar1') {
                        layer.open({
                            type: 1,
                            shade: 0.3,
                            closeBtn: 0,
                            anim:5,
                            resize:false,
                            btn: ['我已了解', '取消'],
                            yes: function (index, layero) {
                                layer.close(index)
                            },
                            btn2: function (index, layero) {
                                return false
                            },
                            title: ['标准swagger接口配置(golang)', 'font-size:15px;'],
                            area: ['550px', '360px'],
                            content: '<pre class="layui-elem-quote layui-quote-nm" style="text-align:left;color: white;background-color: #2F4056;margin-right: 7px;" lay-skin="notepad">' +
                                '// @Summary Insert map information\n' +
                                '// @Description 新增地图信息\n' +
                                '// @Param body formData db.MapInfo true "新增地图信息"\n' +
                                '// @Param image formData file true "地图图片"\n' +
                                '// @Param Authorization header string false "根据头信息获取token"\n' +
                                '// @Success 200 {object} resp.Result Successfully\n' +
                                '// @Failure 400 bad request\n' +
                                '// @Failure 401 Unauthorized\n' +
                                '// @Failure 403 Forbidden as quota exceeded.\n' +
                                '// @Failure 500 internal error' +
                                '</pre>'
                        });
                    }
                }
            });
        },
        updated: function () {
            //引用code方法
            layui.code({
                title: " ",
                about: false,
            });
            $(".layui-code").each(function (index, data) {
                try {
                    $(this).text(JSON.stringify(JSON.parse($(this).text()), null, 4))
                } catch (e) {
                    console.log(e);
                    $(this).html("该接口json解析错误，请检查swagger配置或刷新页面!（<span style='color: #FD482C'>若本无返回值/请求参数值请忽略！</span>）")
                }
            });
        },
        methods: {
            save: function () {
               location.href='/downloadWord?url='+sessionStorage.getItem("url");
            },
            synchronization: function () {
                var index = layer.load(3, {shade: 0.1});
                $.ajax({
                    type: 'post',
                    url: "/toWord",
                    dataType: "JSON",
                    data: {'url': sessionStorage.getItem("url"), 'refresh': true},
                    success: function (data) {
                        layer.close(index);
                        if (data.thisUrl==='null'){
                            layer.msg(data.message);
                        }else{
                            layer.msg("数据同步成功!")
                        }
                    }
                });
            },
            changDbAdd: function () {
                layer.open({
                    type: 2,
                    anim:5,
                    resize:false,
                    title: ['创建新的连接', 'font-size:15px;'],
                    area: ['550px', '360px'],
                    content: "/addDb.html"
                });
            },
            search: function () {
                if ($("#url").val() == "") {
                    layer.msg("请先输入swagger.json HTTP请求地址！")
                } else {
                    var searchIndex = layer.load(3, {shade: 0.1});
                    $.ajax({
                        type: 'post',
                        url: "/toWord",
                        dataType: "JSON",
                        timeout: 5000,
                        data: {'url': $("#url").val(), 'refresh': false},
                        success:function(data){
                            layer.close(searchIndex);
                            if (data.thisUrl==='null'){
                                layer.msg(data.message);
                            }else{
                                $("#url").val(data.thisUrl);
                                sessionStorage.setItem("url", data.thisUrl);
                                example1.title = data.info.title;
                                example1.basePath = data.basePath;
                                example1.version = data.info.version;
                                example1.items = data.tableMap;
                                console.log(example1.items)
                            }
                        }
                    });
                }
            }
        }
    });
});