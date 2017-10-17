angular.module('activitiApp')  
.controller('updateFormCtr', ['$rootScope','$scope','$http','$location','$state', function($rootScope,$scope,$http,$location,$state){  
	$http.post("createFlush.do").success(function(result){
		if(result.isLogin==="yes"){
			$rootScope.userName=result.userName;
			
		}else{
			$location.path("/login");
		}
	});
	
	//预览
	UE.registerUI('button_preview',function(editor,uiName){
	    if(!this.options.toolleipi)
	    {
	        return false;
	    }
	    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
	    editor.registerCommand(uiName,{
	        execCommand:function(){
	            try {
	                $scope.leipiFormDesign.fnReview();
	            } catch ( e ) {
	                alert('leipiFormDesign.fnReview 预览异常');
	            }
	        }
	    });
	    //创建一个button
	    var btn = new UE.ui.Button({
	        //按钮的名字
	        name:uiName,
	        //提示
	        title:"预览",
	        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
	        cssRules :'background-position: -420px -19px;',
	        //点击时执行的命令
	        onclick:function () {
	            //这里可以不用执行命令,做你自己的操作也可
	           editor.execCommand(uiName);
	        }
	    });

	    //因为你是添加button,所以需要返回这个button
	    return btn;
	});
	//保存
	UE.registerUI('button_save',function(editor,uiName){
	    if(!this.options.toolleipi)
	    {
	        return false;
	    }
	    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
	    editor.registerCommand(uiName,{
	        execCommand:function(){
	            try {
	                $scope.leipiFormDesign.fnCheckForm('save');
	            } catch ( e ) {
	                alert('leipiFormDesign.fnCheckForm("save") 保存异常');
	            }
	            
	        }
	    });
	    //创建一个button
	    var btn = new UE.ui.Button({
	        //按钮的名字
	        name:uiName,
	        //提示
	        title:"保存表单",
	        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
	        cssRules :'background-position: -481px -20px;',
	        //点击时执行的命令
	        onclick:function () {
	            //这里可以不用执行命令,做你自己的操作也可
	           editor.execCommand(uiName);
	        }
	    });

	    //因为你是添加button,所以需要返回这个button
	    return btn;
	});

	
	
//整合设计器
	 var leipiEditor = UE.getEditor('myFormDesign',{
         toolleipi:true,//是否显示，设计器的 toolbars
         textarea: 'design_content',   
         //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
        toolbars:[[
         ]],
         //关闭字数统计
         wordCount:false,
         //关闭elementPath
         elementPathEnabled:false,
         //默认的编辑区域高度
         initialFrameHeight:260
         //,iframeCssUrl:"css/bootstrap/css/bootstrap.css" //引入自身 css使编辑器兼容你网站css
         //更多其他参数，请参考ueditor.config.js中的配置项
     });

   $scope.leipiFormDesign = {
	findForm : function(){
		window.location.href="my_views/formList.html";
	},
    /*执行控件*/
    exec : function (method) {
        leipiEditor.execCommand(method);
    },
    /*
        Javascript 解析表单
        template 表单设计器里的Html内容
        fields 字段总数
    */
   parse_form:function(template,fields)
    {
        //正则  radios|checkboxs|select 匹配的边界 |--|  因为当使用 {} 时js报错
        var preg =  /(\|-<span(((?!<span).)*leipiplugins=\"(radios|checkboxs|select)\".*?)>(.*?)<\/span>-\||<(img|input|textarea|select).*?(<\/select>|<\/textarea>|\/>))/gi,preg_attr =/(\w+)=\"(.?|.+?)\"/gi,preg_group =/<input.*?\/>/gi;
        if(!fields) fields = 0;
        var template_parse = template,template_data = new Array(),add_fields=new Object(),checkboxs=0;
        var pno = 0;
        template.replace(preg, function(plugin,p1,p2,p3,p4,p5,p6){
            var parse_attr = new Array(),attr_arr_all = new Object(),name = '', select_dot = '' , is_new=false;
            var p0 = plugin;
            var tag = p6 ? p6 : p4;
            //alert(tag + " \n- t1 - "+p1 +" \n-2- " +p2+" \n-3- " +p3+" \n-4- " +p4+" \n-5- " +p5+" \n-6- " +p6);
            if(tag == 'radios' || tag == 'checkboxs')
            {
                plugin = p2;
            }else if(tag == 'select')
            {
                plugin = plugin.replace('|-','');
                plugin = plugin.replace('-|','');
            }
            plugin.replace(preg_attr, function(str0,attr,val) {
                    if(attr=='name')
                    {
                        if(val=='leipiNewField')
                        {
                            is_new=true;
                            fields++;
                            val = 'data_'+fields;
                        }
                        name = val;
                    }
                    
                    if(tag=='select' && attr=='value')
                    {
                        if(!attr_arr_all[attr]) attr_arr_all[attr] = '';
                        attr_arr_all[attr] += select_dot + val;
                        select_dot = ',';
                    }else
                    {
                        attr_arr_all[attr] = val;
                    }
                    var oField = new Object();
                    oField[attr] = val;
                    parse_attr.push(oField);
            }) 
            /*alert(JSON.stringify(parse_attr));return;*/
             if(tag =='checkboxs') /*复选组  多个字段 */
             {
                plugin = p0;
                plugin = plugin.replace('|-','');
                plugin = plugin.replace('-|','');
                var name = 'checkboxs_'+checkboxs;
                attr_arr_all['parse_name'] = name;
                attr_arr_all['name'] = '';
                attr_arr_all['value'] = '';
                
                attr_arr_all['content'] = '<span leipiplugins="checkboxs"  title="'+attr_arr_all['title']+'">';
                var dot_name ='', dot_value = '';
                p5.replace(preg_group, function(parse_group) {
                    var is_new=false,option = new Object();
                    parse_group.replace(preg_attr, function(str0,k,val) {
                        if(k=='name')
                        {
                            if(val=='leipiNewField')
                            {
                                is_new=true;
                                fields++;
                                val = 'data_'+fields;
                            }

                            attr_arr_all['name'] += dot_name + val;
                            dot_name = ',';

                        }
                        else if(k=='value')
                        {
                            attr_arr_all['value'] += dot_value + val;
                            dot_value = ',';

                        }
                        option[k] = val;    
                    });
                    
                    if(!attr_arr_all['options']) attr_arr_all['options'] = new Array();
                    attr_arr_all['options'].push(option);
                    //if(!option['checked']) option['checked'] = '';
                    var checked = option['checked'] !=undefined ? 'checked="checked"' : '';
                    attr_arr_all['content'] +='<input type="checkbox" name="'+option['name']+'" value="'+option['value']+'"  '+checked+'/>'+option['value']+'&nbsp;';

                    if(is_new)
                    {
                        var arr = new Object();
                        arr['name'] = option['name'];
                        arr['leipiplugins'] = attr_arr_all['leipiplugins'];
                        add_fields[option['name']] = arr;
                    }
                });
                attr_arr_all['content'] += '</span>';

                //parse
                template = template.replace(plugin,attr_arr_all['content']);
                template_parse = template_parse.replace(plugin,'{'+name+'}');
                template_parse = template_parse.replace('{|-','');
                template_parse = template_parse.replace('-|}','');
                template_data[pno] = attr_arr_all;
                checkboxs++;

             }else if(name)
            {
                if(tag =='radios') /*单选组  一个字段*/
                {
                    plugin = p0;
                    plugin = plugin.replace('|-','');
                    plugin = plugin.replace('-|','');
                    attr_arr_all['value'] = '';
                    attr_arr_all['content'] = '<span leipiplugins="radios" name="'+attr_arr_all['name']+'" title="'+attr_arr_all['title']+'">';
                    var dot='';
                    p5.replace(preg_group, function(parse_group) {
                        var option = new Object();
                        parse_group.replace(preg_attr, function(str0,k,val) {
                            if(k=='value')
                            {
                                attr_arr_all['value'] += dot + val;
                                dot = ',';
                            }
                            option[k] = val;    
                        });
                        option['name'] = attr_arr_all['name'];
                        if(!attr_arr_all['options']) attr_arr_all['options'] = new Array();
                        attr_arr_all['options'].push(option);
                        //if(!option['checked']) option['checked'] = '';
                        var checked = option['checked'] !=undefined ? 'checked="checked"' : '';
                        attr_arr_all['content'] +='<input type="radio" name="'+attr_arr_all['name']+'" value="'+option['value']+'"  '+checked+'/>'+option['value']+'&nbsp;';

                    });
                    attr_arr_all['content'] += '</span>';

                }else
                {
                    attr_arr_all['content'] = is_new ? plugin.replace(/leipiNewField/,name) : plugin;
                }
                template = template.replace(plugin,attr_arr_all['content']);
                template_parse = template_parse.replace(plugin,'{'+name+'}');
                template_parse = template_parse.replace('{|-','');
                template_parse = template_parse.replace('-|}','');
                if(is_new)
                {
                    var arr = new Object();
                    arr['name'] = name;
                    arr['leipiplugins'] = attr_arr_all['leipiplugins'];
                    add_fields[arr['name']] = arr;
                }
                template_data[pno] = attr_arr_all;   
            }
            pno++;
        })
        var parse_form = new Object({
            'fields':fields,//总字段数
            'template':template,//完整html
            'parse':template_parse,//控件替换为{data_1}的html
            'data':template_data,//控件属性
            'add_fields':add_fields//新增控件
        });
        return JSON.stringify(parse_form);
    },
    /*type  =  save 保存设计 versions 保存版本  close关闭 */
    fnCheckForm : function ( type ) {
    	var formType=document.getElementById("formType").value;
        if(leipiEditor.queryCommandState( 'source' ))
            leipiEditor.execCommand('source');//切换到编辑模式才提交，否则有bug   
        if(leipiEditor.hasContents()){
            leipiEditor.sync();/*同步内容*/
            //--------------以下仅参考-----------------------------------------------------------------------------------------------------
            var type_value='',formid=0,fields=$("#fields").val(),formeditor='';

            if( typeof type!=='undefined' ){
                type_value = type;
            }
            console.log(document.getElementById("formType"));
            //获取表单设计器里的内容
            formeditor=leipiEditor.getContent();
            //解析表单设计器控件
            var parse_form = this.parse_form(formeditor,fields);
             //异步提交数据
             $.ajax({
                type: 'POST',
                url : './addForm.do',
                dataType : 'html',
                async:false,
                //contentType: 'application/json;charset=utf-8',
                data : {'type' : type_value,'formid':formid,'parse_form':parse_form,"formType":formType},      
                success : function(data){
                	alert("保存成功");
                	window.location.href ="#/formList";
                }
            });
            
        } else {
            alert('表单内容不能为空！')
            $('#submitbtn').button('reset');
            return false;
        }
    } ,
    /*预览表单*/
    fnReview : function (){
    	console.log("111");
        if(leipiEditor.queryCommandState( 'source' ))
            leipiEditor.execCommand('source');/*切换到编辑模式才提交，否则部分浏览器有bug*/
        if(leipiEditor.hasContents()){
        	console.log("222");
            leipiEditor.sync();       /*同步内容*/
            //--------------以下仅参考-------------------------------------------------------------------
            /*设计form的target 然后提交至一个新的窗口进行预览*/
            var type_value='',formid=0,fields=$("#fields").val(),formeditor='';
            var formType=document.getElementById("formType").value;
            if( typeof type!=='undefined' ){
                type_value = type;
            }
            console.log("333");
            //获取表单设计器里的内容
            formeditor=leipiEditor.getContent();
            //解析表单设计器控件
            var parse_form = this.parse_form(formeditor,fields);
            var forms=JSON.parse(parse_form);
            console.log(forms);
            console.log(typeof forms);
            console.log(forms.template);
            var forms1=forms.template;
            	win_parse=window.open('','','width=800,height=400,alwaysRaised=yes,top=100,left=200');
            	//win_parse=window.open('','mywin',"menubar=0,toolbar=0,status=0,resizable=1,left=0,top=0,scrollbars=1,width=" +(screen.availWidth-10) + ",height=" + (screen.availHeight-50) + "\"");
            	var str='<div style="width:500px;height:300px;border:1px solid grey">'+forms1+'</div>';
                win_parse.document.write(forms1);
                win_parse.focus();     
        } else {
            alert('表单内容不能为空！');
            return false;
        }
    }
  };
 }]) 