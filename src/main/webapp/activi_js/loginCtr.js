angular.module('activitiApp')  
.controller('loginCtr', ['$rootScope','$scope','$http','$location','$state', function($rootScope,$scope,$http,$location,$state){  
    //创建模型
	$rootScope.logOut=function(){
		$http.post("./loginOut.do").success(function(loginOutResult){
			$rootScope.userName=undefined;
			$location.path("/login");
		});
	}
    $scope.loginTo=function(user){
        //向后台提交数据
      $http.post("./login.do",user,{headers:'Content-Type:application/json'}).success(function(loginResult){
    	  console.log(loginResult);
    	  if(loginResult.result==="success"){
    		  console.log(loginResult.userName);
    		  $rootScope.userName=loginResult.userName;
    		  $location.path("/taskList");
    	  }else{
    		  $location.path("/login");
    	  }
    	     	
      });

    }  
  
}])  