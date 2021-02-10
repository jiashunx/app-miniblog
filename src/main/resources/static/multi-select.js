/*
*
* 方法名	参数	说明
selectMultip.register()	无	将全局下所有具有multip属性的select标签注册成为具有多选功能的下拉选。
selectMultip.reload(id, data,setData)

id：select标签上的id值

data：渲染的数据

格式如下：[{
        value: "",
        text: "---请选择---"
    }, {
        value: 1,
        text: "薯片"
    }, {
        value: 2,
        text: "大豆油"
    }, {
        value: 3,
        text: "花生"
    }]

setData:设置默认值，可选。具体格式可参照selectMultip.selVal()方法的第二个参数


重新渲染，使用于联动或者动态渲染select标签的场景，从后台服务器获得数据之后可调用此方法。
selectMultip.setVal(id, data)

id:select标签上的id值

data: 可以为字符串，如：“1,2,3”或者“a,b,c”

 也可以为数组，如[1,2,3]或者["a","b","c"]
	给标签赋值
selectMultip.getVal(id)	id:select标签上的id值	取值
*
* */
(function() {
    selectMultip = {
        register: function(id) {
            //大致思路是：为下拉选创建一个隐藏的子选项，每次单选之后将单选的值追加到隐藏的子选项中，并将子选项选中显示即可
            //全局查找所有标记multip的select
            document.querySelectorAll("[multip]").forEach(function(e) {
                render(e);
            })
        },
        reload: function(id, data, setData) {
            var htm = "";
            for(var i = 0; i < data.length; i++) {
                htm += '<option value="' + data[i].value + '">' + data[i].text + '</option>'
            }
            var e = document.getElementById(id);
            e.innerHTML = htm;
            render(e);
            this.setVal(id, setData);
        },
        setVal: function(id, str) {
            var type = Object.prototype.toString.call(str);
            switch(type) {
                case "[object String]":
                    document.getElementById(id).val = str;
                    break;
                case "[object Array]":
                    document.getElementById(id).val = str.toString();
                    break;
                default:
                    break;
            }
        },
        getVal: function(id) {
            return document.getElementById(id).val;
        },

    }

    function render(e) {
        e.param = {
            arr: [],
            valarr: [],
            opts: []
        };
        var choosevalue = "",
            op;

        for(var i = 0; i < e.length; i++) {
            op = e.item(i);
            e.param.opts.push(op);
            if(op.hasAttribute("choose")) {
                if(choosevalue == "") {
                    choosevalue = op.value
                } else {
                    choosevalue += "," + op.value;
                }

            }
        }

        //创建一个隐藏的option标签用来存储多选的值，其中的值为一个数组
        var option = document.createElement("option");
        option.hidden = true;
        e.appendChild(option);
        e.removeEventListener("input", selchange);
        e.addEventListener("input", selchange);

        //重新定义标签基础属性的get和set方法，实现取值和赋值的功能
        Object.defineProperty(e, "val", {
            get: function() {
                return this.querySelector("[hidden]").value;
            },
            set: function(value) {
                e.param.valarr = [];
                var valrealarr = value == "" ? [] : value.split(",");
                e.param.arr = [];
                e.param.opts.filter(function(o) {
                    o.style = "";
                });
                if(valrealarr.toString()) {
                    for(var i = 0; i < valrealarr.length; i++) {
                        e.param.opts.filter(function(o) {
                            if(o.value == valrealarr[i]) {
                                o.style = "color: blue;";
                                e.param.arr.push(o.text);
                                e.param.valarr.push(o.value)
                            }
                        });
                    }
                    this.options[e.length - 1].text = e.param.arr.toString();
                    this.options[e.length - 1].value = e.param.valarr.toString();
                    this.options[e.length - 1].selected = true;
                } else {
                    this.options[0].selected = true;
                }

            },
            configurable: true
        })
        //添加属性choose 此属性添加到option中用来指定默认值
        e.val = choosevalue;
        //添加属性tip 此属性添加到select标签上
        if(e.hasAttribute("tip") && !e.tiped) {
            e.tiped = true;
            e.insertAdjacentHTML('afterend', '<i style="color: red;font-size: 12px">*可多选</i>');
        }
    }

    function selchange() {
        var text = this.options[this.selectedIndex].text;
        var value = this.options[this.selectedIndex].value;
        this.options[this.selectedIndex].style = "color: blue;";
        var ind = this.param.arr.indexOf(text);
        if(ind > -1) {
            this.param.arr.splice(ind, 1);
            this.param.valarr.splice(ind, 1);
            this.param.opts.filter(function(o) {
                if(o.value == value) {
                    o.style = "";
                }
            });
        } else {
            this.param.arr.push(text);
            this.param.valarr.push(value);
        }
        this.options[this.length - 1].text = this.param.arr.toString();
        this.options[this.length - 1].value = this.param.valarr.toString();
        if(this.param.arr.length > 0) {
            this.options[this.length - 1].selected = true;
        } else {
            this.options[0].selected = true;
        }
    }
})();
