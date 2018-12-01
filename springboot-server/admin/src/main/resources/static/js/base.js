
function baseGet(url,data,success,async,deal) {
    baseAjax(url,"GET",data,success,async,deal);
}

function basePost(url,data,success,async,deal) {
    baseAjax(url,"POST",data,success,async,deal);
}

function baseAjax(url,type,data,success,async,deal) {
    $.ajax({
        url:url,    //请求的url地址
        dataType:"json",   //返回格式为json
        async:async==undefined?true:async,//请求是否异步，默认为异步，这也是ajax重要特性
        data:(deal==undefined || deal) ? dealElement(data) : data,    //参数值
        type:type,   //请求方式
        success:function(req){
            if(req.code == 200){
                success(req.data,req.page);
            }else if(req.code==10001){
                layer.confirm('您的登录凭证已经过期请重新登录？', {
                    btn: ['重新登录'] //按钮
                }, function(){
                    window.location.href="/login";//跳转到登录界面
                });
            }else {//服务器错误
                layer.alert(req.msg, {
                    icon: 2,
                });
            }
        },
        error:function(){
            //请求出错处理
            layer.alert('服务器异常，请联系开发人员', {
                icon: 2,
            })
        }
    });
}

function dealElement(obj){
    var param = {};
    if ( obj === null || obj === undefined || obj === "" ) return param;
    for ( var key in obj ){
        if ( obj[key] !== null && obj[key] !== undefined && obj[key] !== "" ){
            param[key] = obj[key];
        }
    }
    return param;
}


if($.jgrid && $.jgrid.defaults){
    $.jgrid.defaults.width = 1000;
    $.jgrid.defaults.responsive = true;
    $.jgrid.defaults.styleUI = 'Bootstrap';
    $.jgrid.defaults.datatype ='json';
    $.jgrid.defaults.viewrecords= true;
    $.jgrid.defaults.rowList = [15,20,30,50];
    $.jgrid.defaults.rownumbers = true;
    $.jgrid.defaults.ajaxoptions={'contentType':'application/json; charset=utf-8'};
    $.jgrid.defaults.rowNum =15;
    $.jgrid.defaults.autowidth = true;
    $.jgrid.defaults.multiselect = true;
    $.jgrid.defaults.rownumWidth = '20%' ;
    $.jgrid.defaults.height = 550;
    $.jgrid.defaults.pager = "#jqGridPager";
    $.jgrid.defaults.jsonReader  = {
        root: "data.records",
        page: "data.current",
        total: "data.pages",
        records: "data.total"
    };
    $.jgrid.defaults.prmNames = {
        page:"currentPage",
        rows:"pageSize",
        order: "sortType"
    };
    $.jgrid.defaults.loadBeforeSend = function(xhr,settings){
        xhr.setRequestHeader("authentication", localStorage.getItem("authentication"));
    };
    $.jgrid.defaults.loadComplete = function(xhr){
        if(xhr.code==10001){
            layer.confirm('您的登录凭证已经过期请重新登录？', {
                btn: ['重新登录'] //按钮
            }, function(){
                window.location.href="/login";//跳转到登录界面
            });
        }
    };
    $.jgrid.defaults.loadError=function(xhr,status,error){
        layer.alert('服务器异常，请联系开发人员', {
            icon: 2,
        })
    };
    $.jgrid.defaults.gridComplete = function () {
        $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
    };
}

function authority_buttons() {
    //界面按钮权限控制
    //获取全部带有authority属性的元素
    var authority_buttons = $("[authority]");
    if(authority_buttons.length >0){
        var gimii_authority_buttons = localStorage.getItem("gimii_authority_buttons");  //获取存在localStorage权限按钮集合
        var buttons = JSON.parse(gimii_authority_buttons);
        for(var i=0;i<authority_buttons.length;i++){//循环
            var authority = authority_buttons.eq(i); //获取对于的元素
            var str = authority.attr("authority");  //获取ID
            var flag = false;
            for (var j = 0; j < buttons.length; j++) {
                if(buttons[j]==str){
                    flag = true;
                }
            }
            if(!flag){
                authority.remove();//从界面删除
            }else{
                authority.show();
            }
        }
    }
};
authority_buttons();