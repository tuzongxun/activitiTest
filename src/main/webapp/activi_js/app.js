var app=angular.module('activitiApp',['ui.router']);  
app.run(function($rootScope) {
	$rootScope.logOut=function(){
		$http.post("./loginOut.do").success(function(loginOutResult){
			$rootScope.userName=undefined;
			$location.path("/login");
		});
	}
});
app.config(function($stateProvider,$urlRouterProvider){  
    $urlRouterProvider.otherwise('/login');  
    $stateProvider  
    .state('create', {  
    url: "/create",  
    views: {  
       'view': {  
        templateUrl: 'activi_views/create.html',  
        controller: 'createCtr'  
       }  
    }  
   });  
   $stateProvider  
    .state('modelList', {  
    url: "/modelList",  
    views: {  
       'view': {  
        templateUrl: 'activi_views/modelList.html',  
        controller: 'modelCtr'  
       }  
    }  
   });  
   $stateProvider  
   .state('processList', {  
   url: "/processList",  
   views: {  
      'view': {  
       templateUrl: 'activi_views/processList.html',  
       controller: 'processCtr'  
      }  
   }  
  });  
   $stateProvider  
   .state('taskList', {  
   url: "/taskList",  
   views: {  
      'view': {  
       templateUrl: 'activi_views/taskList.html',  
       controller: 'taskCtr'  
      }  
   }  
  });  
   $stateProvider  
   .state('findFirstTask', {  
   url: "/findFirstTask",  
   views: {  
      'view': {  
       templateUrl: 'activi_views/firstTaskList.html',  
       controller: 'findFirstTaskCtr'  
      }  
   }  
  });  
   $stateProvider  
   .state('login', {  
   url: "/login",  
   views: {  
      'view': {  
       templateUrl: 'activi_views/login.html',  
       controller: 'loginCtr'  
      }  
   }  
  });  
   $stateProvider  
   .state('hisTask', {  
   url: "/hisTask",  
   views: {  
      'view': {  
       templateUrl: 'activi_views/hisTask.html',  
       controller: 'hisTaskCtr'  
      }  
   }  
  });  
   
   $stateProvider  
   .state('completeTaskTo', {  
   url: "/completeTaskTo",  
   views: {  
      'view': {  
       templateUrl: 'activi_views/completeTask.html',  
       controller: 'completeTaskCtr'  
      }  
   }  
  });  
   
   $stateProvider  
   .state('completeTaskTo1', {  
   url: "/completeTaskTo1",  
   views: {  
      'view': {  
       templateUrl: 'activi_views/completeTaskTo1.html',  
       controller: 'completeTask1Ctr'  
      }  
   }  
  });  
   $stateProvider  
   .state('createForm', {  
   url: "/createForm",  
   views: {  
      'view': {  
       templateUrl: 'my_views/updateForm.html',  
       controller: 'updateFormCtr'  
      }  
   }  
  });  
   $stateProvider  
   .state('formList', {  
   url: "/formList",  
   views: {  
      'view': {  
       templateUrl: 'my_views/formList.html',  
       controller: 'formListCtr'  
      }  
   }  
  });  
  
});  