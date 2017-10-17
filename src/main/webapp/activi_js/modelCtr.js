angular.module('activitiApp')  
.controller('modelCtr', ['$rootScope','$scope','$http','$location', function($rootScope,$scope,$http,$location){  
$scope.init=function(){
        $http.post("./modelList.do").success(function(result) {
        	if(result.isLogin==="yes"){
        		$rootScope.userName=result.userName;
        	    console.log(result.data); 
    	        $scope.modelList=result.data;
        	}else{
        		$location.path("/login");
        	}
        });
}  
        $scope.deploye=function(model){
        	console.log(model);
        	$http.post("./deploye.do",model).success(function(deployResult){
        		$location.path("/processList");
        	});
        }
        
        $scope.update=function(modelId){
        	window.open("http://localhost:8080/activitiTest1/service/editor?id="+modelId);
        }
      
  
}])  