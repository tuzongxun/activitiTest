angular.module('activitiApp')  
.controller('formListCtr', ['$rootScope','$scope','$http','$location','$state', function($rootScope,$scope,$http,$location,$state){  
	$http.post("createFlush.do").success(function(result){
		if(result.isLogin==="yes"){
			$rootScope.userName=result.userName;
		}else{
			$location.path("/login");
		}
	});
$(document).ready(function(){
$.ajax({
    type: 'POST',
    url : './findForms.do',
    dataType : 'json',
    async:false,
    success : function(data){
    	//alert("保存成功");
    	console.log(data);
    	for(var i=0;i<data.length;i++){
    		var form=data[i];
    		console.log(form);
    			var tables = document.getElementById("ta1");
    			var tr = document.createElement("tr");
    			var td0=document.createElement("td");
    			var td1=document.createElement("td");
    			var td2=document.createElement("td");
    			var td3=document.createElement("td");
    			td0.innerText=form.formId;
    			td1.innerText=form.formType;
    			td2.innerText=form.form;
    			td2.setAttribute("id",i);
    			var a1=document.createElement("a");
    			var node1 = document.createTextNode(" 删除  ");
    			a1.appendChild(node1);
    			a1.setAttribute("href","#");
    			a1.setAttribute("id","a1"+i);
    			a1.onclick=function(){
    				var id=this.id;
    				var id1=id.substring(2);
    				var formId=data[id1].formId;
    				//console.log(document.getElementById(id).innerText); 
    				//console.log();
    				//先删除，后跳转
    				$.ajax({
    				    type: 'POST',
    				    url : './deleteForm.do',
    				    dataType : 'json',
    				    async:false,
    				    data:{"formId":formId},
    				    success : function(){
    				    	window.location.replace(window.location+"/createForm");
    				    	setTimeout(function(){
    				    		window.location.replace(window.location+"/formList");
    				    	},50);
    				    	document.execCommand('Refresh');
    				    }    
    				})
    			};
    			
    			var a2=document.createElement("a");
    			var node2 = document.createTextNode(" 预览  ");
    			a2.appendChild(node2);
    			a2.setAttribute("href","#");
                a2.setAttribute("id","a2"+i);
    			a2.onclick=function(){
    				//console.log();
    				//console.log(data);
    				var id=this.id;
    				var formId=id.substring(2);
    				var form=data[formId].form;
    				form.replace("{","");
    				form.replace("}","");
    				form.replace("|","");
    				//console.log(document.getElementById(id).innerText);
    				win_parse=window.open('','','width=800,height=400,alwaysRaised=yes,top=100,left=200');
    		    	var str='<div style="width:500px;height:300px;border:1px solid grey">'+form+'</div>';
    		        win_parse.document.write(str);
    		        win_parse.focus();     
    		        //document.getElementById("div2").write(str);
    			};
    			
                td3.appendChild(a1);
                td3.appendChild(a2);
                tr.appendChild(td0);
    			tr.appendChild(td1);
    			tr.appendChild(td2);
    			tr.appendChild(td3);
    			tables.appendChild(tr);
    	}
    }
})
});
}]) 