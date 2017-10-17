angular.module('activitiApp')  
.controller('hisTaskCtr', ['$rootScope','$scope','$http','$location', function($rootScope,$scope,$http,$location){  
$scope.init=function(){
        $http.post("./hisTask.do").success(function(result) {
        	if(result.isLogin==="yes"){
        	    console.log(result.data); 
        	    $rootScope.userName=result.userName;
    	        $scope.taskList=result.data;
        	}else{
        		$location.path("/login");
        	}
        });
}
  
//查看
$scope.review=function(task){
	console.log(task);
	$('#review').html('').dialog({
		title:'节点名称[' + task.name + ']',
		modal: true,
		//left:300,
		//top:300,
		//position:[$('#handleTemplate').offset().right,500],
		//offset:[400,400],
		//Array : 'center',
		width: $.common.window.getClientWidth() * 0.6,
		height: $.common.window.getClientHeight() * 0.9,	
		open: function() {
			// 获取json格式的表单数据，就是流程定义中的所有field
    		var dialog = this;
    		// 读取启动时的表单
    			// 获取的form是字符行，html格式直接显示在对话框内就可以了，然后用form包裹起来
    			
    			$(dialog).append("<div class='formContent' />");
    			$('.formContent').html('').wrap("<form id='hisTask' class='formkey-form' method='post' />");
    			
    			var $form = $('.formkey-form');

    			// 设置表单action    getStartFormAndStartProcess
    			//$form.attr('action', './completeTask');
    			//设置部署的Id
    			$form.append("任务Id：<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' name='taskId' value="+task.id+"></br>");
    			$form.append("申请时间：<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' value="+new Date(task.startTime).format('yyyy-MM-dd hh:mm:ss')+"></br>");
    			$form.append(task.allForm);
    			
    			//根据formData设置申请页面
    			//处理form字符串
//    			var form=task.lastForm;
//    			console.log(form);
//    			var index0=form.lastIndexOf(">");
//				var p=form.split("<p>");
//				var i0=0;
//				for(var i=1;i<p.length;i++){
//					console.log(p[i]);
//					var pName=p[i].substring(0,p[i].indexOf("：")+1);
//					var index1=p[i].indexOf('name="');
//					var p0=p[i].substring(index1,p[i].lastIndexOf(">"));
//					var index2=p0.indexOf('"');
//					var keyName=p[i].substring(index1+6,index2+index1+7);
//					console.log(keyName);
//					if(keyName==="data_1"){
//						continue;
//					}
//    				var value=null;
//    			    for(var key in task.formData){
//    				   var keyString=key.substring(0,key.length-1);
//    				   console.log("keyString:::"+keyString);
//    				   if(keyString===keyName){
//    				   value=(task.formData)[key];
//    				   }
//    			   }
//    			    if(pName!=null&&value!=null){
//    			    	if(i0==0){
//    			           $form.append("<hr style='height:2px;border:none;border-top:2px dotted #185598;'></hr>");
//    			           i0++;
//    			    	}
//    			    $form.append(pName+"<input type='text' readonly='readonly' style='background-color:#DEDCDC;margin-top:10px' value='"+value+"'></br>");
//    			    }
//				}
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
				$("#review").dialog("close");
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
}])  