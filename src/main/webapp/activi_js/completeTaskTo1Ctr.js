angular.module('activitiApp')  
.controller('completeTask1Ctr', ['$rootScope','$scope','$http','$location','$state', function($rootScope,$scope,$http,$location,$state){  

	$http.post("createFlush.do").success(function(result){
		if(result.isLogin==="yes"){
			$rootScope.userName=result.userName;
			if($rootScope.task==null||$rootScope.task.id==null){
				$location.path("/taskList");
			}else{
				$scope.task=$rootScope.task;
				$http.post("./getTaskForm.do",$rootScope.task.id).success(function(data){
	        		var p=document.getElementById("comDiv");
	        		p.innerHTML=data.form;
	        	});
			}
		}else{
			$location.path("/login");
		}
	});

        $scope.completeTask=function(task){
        	console.log(task);
        	$rootScope.task=task;
        	$http.post("./completeTask.do",task).success(function(taskResult){
        		$location.path("/taskList");
        	});
        }
      
  
}])  