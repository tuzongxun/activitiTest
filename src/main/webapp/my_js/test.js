function toAdd(){
	window.location.href="../"; 
};
function formList(){
	window.location.href="./formList.html"; 
};
function toTest(){
	window.location.href="./test.html"; 
};
var i=false;
var j=false;
var k=false;
function showDiv1(){
	var div=document.getElementById("naDiv1"); 
	var ul=document.getElementById("naUl1"); 
	div.style.display="";
	div.style.zIndex=1;
	k=true;
};

function showDiv11(){
	i=true;
};
function hideDiv1(){
	setTimeout(function(){
    k=false;
	var div=document.getElementById("naDiv1"); 
	div.style.zIndex=0;
	if(div.style.display===""&&i==false&&j==false&&k==false){
			div.style.display="none";;
	}	
	},500);
};
function hideDiv11(){
	setTimeout(function(){
	i=false;
	var div=document.getElementById("naDiv1"); 
	div.style.zIndex=0;
	if(div.style.display===""&&i==false&&j==false){
			div.style.display="none";;
	}	
	},500);
};
function showDiv111(){
	j=true;
};
function hideDiv111(){
	setTimeout(function(){
	j=false;
	var div=document.getElementById("naDiv1"); 
	div.style.zIndex=0;
	if(div.style.display===""&&i==false&&j==false){
			div.style.display="none";;
	}	
	},500);
};
////
var i2=false;
var j2=false;
var k2=false;
function showDiv2(){
	k2=true;
	var div=document.getElementById("naDiv2"); 
	var ul=document.getElementById("naUl2"); 
	div.style.display="";
	div.style.zIndex=1;
};
function hideDiv2(){
	setTimeout(function(){
	k2=false;
	var div=document.getElementById("naDiv2"); 
	div.style.zIndex=0;
	if(div.style.display===""&&i2==false&&j2==false&&k2==false){
			div.style.display="none";;
	}	
	},500);
};

function showDiv22(){
	i2=true;
};
function hideDiv22(){
	setTimeout(function(){
	i2=false;
	var div=document.getElementById("naDiv2"); 
	div.style.zIndex=0;
	if(div.style.display===""&&i2==false&&j2==false){
			div.style.display="none";;
	}	
	},500);
};
function showDiv222(){
	j2=true;
};
function hideDiv222(){
	setTimeout(function(){
	j2=false;
	var div=document.getElementById("naDiv2"); 
	div.style.zIndex=0;
	if(div.style.display===""&&i2==false&&j2==false){
			div.style.display="none";;
	}	
	},500);
};

