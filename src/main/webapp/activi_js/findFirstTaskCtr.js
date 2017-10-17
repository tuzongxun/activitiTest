angular.module('activitiApp')  
.controller('findFirstTaskCtr', ['$rootScope','$scope','$http','$location','$state', function($rootScope,$scope,$http,$location,$state){  
$scope.init=function(){
        $http.post("./findFirstTask.do").success(function(result) {
        	if(result.isLogin==="yes"){
        	$rootScope.userName=result.userName;
    	    $scope.taskList=result.data;
        	}else{
        		$location.path("/login");
        	}
        });
}    
        //查看findDetail(task)
        $scope.findDetail=function(task){
        	console.log(task);
        	$('#findDetail').html('').dialog({
        		title:'节点名称[' + task.name + ']',
    			modal: true,
    			width: $.common.window.getClientWidth() * 0.6,
    			height: $.common.window.getClientHeight() * 0.9,	
    			open: function() {
    				// 获取json格式的表单数据，就是流程定义中的所有field
    	    		var dialog = this;
    	    		// 读取启动时的表单
    	    			// 获取的form是字符行，html格式直接显示在对话框内就可以了，然后用form包裹起来
    	    			$(dialog).append("<div class='formContent' />");
    	    			$('.formContent').html('').wrap("<form id='findDetailForm' class='formkey-form1' method='post' />");	
    	    			var $form = $('.formkey-form1');
    	    			//设置部署的Id
    	    			$form.append("任务Id：<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' name='taskId' value="+task.id+"></br>");
    	    			$form.append("申请时间：<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' name='createTime' value="+new Date(task.createTime).format('yyyy-MM-dd hh:mm:ss')+"></br>");
    	    			//根据formData设置申请页面
    	    			//处理form字符串
    	    			var form=task.lastForm;
    	    			//console.log(form);
    	    			var index0=form.lastIndexOf(">");
	    				var p=form.split("<p>");
	    				for(var i=1;i<p.length;i++){
	    					var pName=p[i].substring(0,p[i].indexOf("：")+1);
	    					var index1=p[i].indexOf('name="');
	    					var p0=p[i].substring(index1,p[i].lastIndexOf(">"));
	    					var index2=p0.indexOf('"');
	    					var keyName=p[i].substring(index1+6,index2+index1+7);
		    				var value=null;
    	    			    for(var key in task.formData){
//    	    				   var keyString=key+"";
//    	    				   if(keyString===keyName){
//    	    				   value=(task.formData)[key];
    	    				   var keyString=key.substring(0,key.length-1);
    	    				   if(keyString===keyName){
    	    				   value=(task.formData)[key];
    	    				   }
    	    			   }
    	    			   
    	    			    $form.append(pName+"<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' name='createTime' value='"+value+"'></br>");
	    				}
	    				
    	    			// 初始化日期组件
    	    			$form.find('.datetime').datetimepicker({
    	    		           stepMinute: 5
    	    		     });
    	    			$form.find('.date').datepicker();
    	    			
    	    			// 表单验证
    	    			$form.validate($.extend({}, $.common.plugin.validator));
    	    		
    			},
    			buttons: [{
    				text: '关闭',
    				click: function() {
    					$("#findDetail").dialog("close");
    					//sendStartupRequest();
    				}
    			}]
    		}).position({
    			   //my: "center",
    			   //at: "center",
    			offset:'300 300',
    			   of: window,
    			   collision:"fit"
    			});
        }
      
        //完成任务
        $scope.completeTaskTo=function(task){
        	console.log(task);
        	$('#comTask').html('').dialog({
        		title:'节点名称[' + task.name + ']',
    			modal: true,
    			width: $.common.window.getClientWidth() * 0.6,
    			height: $.common.window.getClientHeight() * 0.9,	
    			open: function() {
    				// 获取json格式的表单数据，就是流程定义中的所有field
    	    		var dialog = this;
    	    		// 读取启动时的表单
    	    			// 获取的form是字符行，html格式直接显示在对话框内就可以了，然后用form包裹起来
    	    			$(dialog).append("<div class='formContent' />");
    	    			$('.formContent').html('').wrap("<form id='completeTask' class='formkey-form' method='post' />");		
    	    			var $form = $('.formkey-form');

    	    			// 设置表单action    getStartFormAndStartProcess
    	    			$form.attr('action', './completeTask');
    	    			//设置部署的Id
    	    			$form.append("任务Id：<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' name='taskId' value="+task.id+"></br>");
    	    			$form.append("申请时间：<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' value="+new Date(task.createTime).format('yyyy-MM-dd hh:mm:ss')+"></br>");
    	    			//根据formData设置申请页面
    	    			//处理form字符串
    	    			var form=task.lastForm;
    	    			//console.log(form);
    	    			var index0=form.lastIndexOf(">");
	    				var p=form.split("<p>");
	    				for(var i=1;i<p.length;i++){
	    					var pName=p[i].substring(0,p[i].indexOf("：")+1);
	    					var index1=p[i].indexOf('name="');
	    					var p0=p[i].substring(index1,p[i].lastIndexOf(">"));
	    					var index2=p0.indexOf('"');
	    					var keyName=p[i].substring(index1+6,index2+index1+7);
		    				var value=null;
    	    			    for(var key in task.formData){
    	    				   var keyString=key.substring(0,key.length-1);
    	    				   if(keyString===keyName){
    	    				   value=(task.formData)[key];
    	    				   }
    	    			   }
    	    			    $form.append(pName+"<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' value='"+value+"'></br>");
	    				}	
                        ///////////////////////////////////////    
	    			    $.post('./getTaskForm.do',task.formKey, function(result) {
	    	    			//设置部署的Id
	    	    			//$form.append("<input type='hidden' name='deploymentId' value="+deploymentId+">");
	    	    			$form.append(result.form);    	    	
	    			    });    
	    			    ////////////////////////////////////////
    	    			// 初始化日期组件
    	    			$form.find('.datetime').datetimepicker({
    	    		           stepMinute: 5
    	    		     });
    	    			$form.find('.date').datepicker();
    	    			// 表单验证
    	    			$form.validate($.extend({}, $.common.plugin.validator));
    	    		
    			},
    			buttons: [{
    				text: '提交',
    				click: function() {
    					$("#comTask").dialog("close");
    					sendStartupRequest();
    				}
    			}]
    		}).position({
    			   //my: "center",
    			   //at: "center",
    			offset:'300 300',
    			   of: window,
    			   collision:"fit"
    			});
        }
        
        /**
    	 * 提交表单
    	 * @return {[type]} [description]
    	 */
    	function sendStartupRequest() {
    		if ($(".formkey-form").valid()) {
    			var url = './completeTask.do';
    			var args = $('#completeTask').serialize();
    			$.post(url, args, function(data){
    				$("#comTask").dialog("close");
    				//$location.path("/processList");
    				window.location.href =("#/processList");
			    	setTimeout(function(){
			    		//$location.path("/findFirstTask");
			    		window.location.href =("#/findFirstTask");
			    	},1500);
    			});
    		}
    	}
  
}])  