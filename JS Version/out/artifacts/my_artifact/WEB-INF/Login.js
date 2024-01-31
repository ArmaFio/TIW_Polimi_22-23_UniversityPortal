/**
 * 
 */

 (function(){
	 document.getElementById(loginbutton).addEventListener("click",(e)=>{
		 var form=e.target.closest("form");
		 if (form.checkValidity()){
			 makeCall("GET", 'CheckLogin', e.target.closest("form"),
			 function(req){
				 if (req.readyState==XMLHttpRequest.DONE){
					 var message= JSON.parse(req.responseText)
					 switch(req.status){
						 case(200):
							 sessionStorage.setItem("user",user);
							 if(user.status==STUDENT){
							 	window.location.href= "StudentHome.html";
							 }
							 else{
							 	window.location.href= "TeacherHome.html";
							 }
							 break;
					     case 400: // bad request
		                	 document.getElementById("errormessage").textContent = message;
		                     break;
		              	 case 401: // unauthorized
		                 	document.getElementById("errormessage").textContent = message;
		                 	 break;
		                 case 500: // server error
		            	 	document.getElementById("errormessage").textContent = message;
		                    break;
		              }
            
                    }
                  }
                );
              } else {
    	          form.reportValidity();
			    }
			  });
			
			})();
								 
					 	