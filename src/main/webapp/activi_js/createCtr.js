angular.module('activitiApp')  
.controller('createCtr', ['$rootScope','$scope','$http','$location','$state', function($rootScope,$scope,$http,$location,$state){  
    //创建模型
	$http.post("createFlush.do").success(function(result){
		if(result.isLogin==="yes"){
			$rootScope.userName=result.userName;
		}else{
			$location.path("/login");
		}
	});
    $scope.createTo=function(activiti){
        //向后台提交数据
      $http.post("./create.do",activiti,{headers:'Content-Type:application/json'}).success(function(createResult){
    	  console.log(createResult);
    	  $location.path("/modelList");
    	window.open("http://localhost:8080/activitiTest1"+createResult.path+createResult.modelId);
      });

    }  
  
}])  