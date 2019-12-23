layui.use(['code', 'jquery', 'util', 'layer', 'form'], function () { //加载code模块
    var $ = layui.jquery, util = layui.util, layer = layui.layer, form = layui.form;
    layui.code({
        title: " ",
        about: false,
    });
    document.getElementById("save").onclick=function () {
        location.href="/downloadWord?url="+getSearchString("url",window.document.location.href)
    }
    function getSearchString(key, Url) {
        var str = Url;
        str = str.substring(str.indexOf('?')+1, str.length); // 获取URL中?之后的字符（去掉第一位的问号）
        // 以&分隔字符串，获得类似name=xiaoli这样的元素数组
        var arr = str.split("&");
        var obj = new Object();

        // 将每一个数组元素以=分隔并赋给obj对象
        for (var i = 0; i < arr.length; i++) {
            var tmp_arr = arr[i].split("=");
            obj[decodeURIComponent(tmp_arr[0])] = decodeURIComponent(tmp_arr[1]);
        }
        return obj[key];
    }
    form.on('submit(search)', function (data) {
        if ($("#url").val() == "") {
            layer.msg("请先输入swagger.json HTTP请求地址！")
        } else {
            $.ajax({
                type: 'get',
                cache: false,
                url: $("#url").val(),
                dataType: "jsonp", //跨域采用jsonp方式
                processData: false,
                complete: function (data) {
                    if (data.status==200) {
                        location.href = "toWord?url=" + $("#url").val()
                    } else {
                        layer.msg("路径无法正常访问！");
                    }
                }
            });
        }
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    //引用code方法
    $(".layui-code").each(function (index, data) {
        try {
            $(this).text(JSON.stringify(JSON.parse($(this).text()), null, 4))
        } catch (e) {
            console.log(e)
            $(this).html("该接口json解析错误，请检查swagger配置!（<span style='color: #FD482C'>若本无返回值/请求参数值请忽略！</span>）")
        }
    })
    util.fixbar({
        bar1: true,
        click: function (type) {
            if (type === 'bar1') {
                windowsOpen();
            }
        }
    });

    function windowsOpen() {
        layer.open({
            type: 1,
            shade: 0.3,
            closeBtn: 0,
            btn: ['我已了解', '取消'],
            yes: function (index, layero) {
                layer.close(index)
            },
            btn2: function (index, layero) {
                return false
            },
            title: ['标准swagger接口配置(golang)', 'font-size:18px;'],
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
});
