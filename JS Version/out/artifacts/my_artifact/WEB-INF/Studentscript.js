/**
 * 
 */

 (function(){
	 
	 var courses, exams, esito
	 Pageorchestrator= new PageOrchestrator;
	 
	 window.addEventListener("load", ()=>{
		 if(sessionStorage.getItem("User")==null)
		 	window.location.href="LoginPage.html";
		 else{
			 Pageorchestrator.start();
			 Pageorchestrator.update();
		 }
	 })
	 
	 function Name(_username, messagecontainer) {
	    this.username = _username;
	    this.show = function() {
	      messagecontainer.textContent = this.username;
	    }
	  }
	 
	 function Courses(error,table,content){
		 this.error=error;
		 this.table=table;
		 this.content=content;
		 
		 this.reset= function(){
			 this.table.style.visibility= "hidden";
		 }
		 
		 this.show= function(){
			 var th = this;
			 makeCall("GET", "GoToHome", null, 
			 	function(req){
					 if (req.readystate === XMLHttpRequest.DONE) {
						 if (req.status === 200) {
							 var resultdata = JSON.parse(req.responseText);
							 if (resultdata.length === 0) {
								 th.error.textContent = "Non risulta iscritto ad alcun corso";
								 return;
							 }
							 th.update(resultdata);
						 } else
							 Pageorchestrator.refresh();

					 }});
		 }

		 this.update= function(courses){
			 var row,coursecode,coursename,anchor;
			 this.content.innerHTML="";
			 courses.forEach(function(course) {
				 row = document.createElement("tr");
				 coursecode = document.createElement("td");
				 anchor = document.createElement("a");
				 anchor.setAttribute("corso", courses.courseId);
				 text = document.createTextNode(course.courseId);
				 anchor.addEventListener("click", (e) => {
					 Pageorchestrator.refresh();
					 Appelli.show(e.target.getAttribute("corso"));
				 });
				 anchor.href = "#";
				 anchor.appendChild(text);
				 coursecode.appendChild(anchor);
				 row.appendChild(data);
				 coursename = document.createElement("td");
				 coursename.textContent = course.courseName;
				 row.appendChild((coursename));
				 content.appendChild(row);
			 })
			 this.table.style.visibility = "visible";
		 }
	 }

	 function Appelli(errore,table,content) {
		 this.error = errore;
		 this.head = table;
		 this.body = content;

		 this.reset = function () {
			 this.head.style.visibility = "hidden";
		 }


		 this.show = function (courseid) {
			 var self = this;
			 makeCall("GET", "GetExams?corso=" + courseid, null,
				 function (req) {
					 if (req.readyState == 4) {
						 var message = req.responseText;
						 if (req.status == 200) {
							 var appelliToShow = JSON.parse(req.responseText);
							 if (appelliToShow.length == 0) {
								 self.error.textContent = "Non ci sono appelli per questo corso a cui sei iscritto!";
								 return;
							 }
							 self.update(appelliToShow);
						 } else {
							 pageOrchestrator.refresh();
						 }
					 }
				 }
			 );

		 };

		 this.update = function(arrayAppelli) {
			 var row, linkcell, anchor;
			 this.body.innerHTML = "";
			 var self = this;
			 arrayAppelli.forEach(function(appello) {
				 row = document.createElement("tr");
				 linkcell = document.createElement("td");
				 anchor = document.createElement("a");
				 linkcell.appendChild(anchor);
				 linkText = document.createTextNode(appello.date);
				 anchor.appendChild(linkText);
				 anchor.setAttribute('appellodata', appello.date);
				 anchor.setAttribute('corso',appello.courseCode);
				 anchor.addEventListener("click", (e) => {
					 esito.show(e.target.getAttribute("appellodata"),e.target.getAttribute('corso'));
				 }, false);
				 anchor.href = "#";
				 row.appendChild(linkcell);
				 self.body.appendChild(row);
			 });
			 this.listcontainer.style.visibility = "visible";
		 }

	 }


	 this.Esito= function (error, esito, refuse){
		 this.res=esito;
		 this.err=error;
		 this.ref=refuse;
		 this.event=function (){
			 this.form.querySelector("input[type='button']").addEventListener('click', (e) => {
				 var self = this, formToSend = e.target.closest("form");
				 makeCall("POST", 'Rifiuta', formToSend,
					 function(req) {
						 if (req.readyState == XMLHttpRequest.DONE) {
							 if (req.status == 200) {
								 self.reset();
							 } else {
								 pageOrchestrator.refresh();
							 }
						 }
					 }
				 );
			 });
		 };
		 this.reset= function (){
			 this.res.style.visibility="hidden";
			 this.ref.style.visibility="hidden";
		 }

		 this.show= function(data,corso){
			 var self= this;
			 makeCall("GET", "GetResOrRegs?exc="+corso+"&exd="+data,   function(req) {
					 if (req.readyState == 4) {
						 var message = req.responseText;
						 if (req.status == 200) {
							 var esitoToShow = JSON.parse(message);
							 if (esitoToShow.state === 'NON INSERITO' || esitoToShow.state === 'INSERITO') {
								 self.reset();
								 self.err.textContent = "Voto non ancora definito!";
								 return;
							 } else if (esitoToShow.state === 'RIFIUTATO'){
								 self.reset();
								 self.err.textContent = "La valutazione è stata rifiutata";

							 }
							 self.update(esitoToShow,corso);
						 } else {
							 pageOrchestrator.refresh();
						 }
					 }
				 }
			 );
		 };

		 this.update = function(newEsito,corso) {
			 var cell,row;
			 this.res.innerHTML = "";
			 var self = this;
			 cell=document.createElement("td");
			 cell.textContent= "Matricola"
			 row=document.createElement()




			 if(newEsito.status==='PUBLISHED'){
				 self.ref.exc.value = newEsito.date;
				 self.ref.exd.value= corso;
				 self.ref.style.visibility = "visible";

			} else {
				 self.form.style.visibility = "hidden";
			}

			this.container.style.visibility = "visible";
		}

		}

		 }
	 }
	 }










	 

	 function PageOrchestrator(){
		 var error= document.getElementById("id_error");
		 
		 this.start= function(){
			 name= new Name(sessionStorage.getItem("User"), document.getElementById(id_namesurname))
			 name.show();
			 
			 courses= new Courses(error, document.getElementById("id_tabellacorsi"), 
			 document.getElementById("id_corsi"));
			 
			 exams= new Exams(error, document.getElementById("id_tabellaappelli"),
			 document.getElementById("id_appelli"));
			 
			 esito= new Esito(error, document.getElementById("id_esito"),
			 document.getElementById("id_refuse"));
			 esito.event();
			 
			 document.querySelector("a[href='Logout']").addEventListener('click', () => {
	         window.sessionStorage.removeItem('username');})
	        
		 }
		 
		 this.refresh= function(){
			 error.textContent="";
			 courses.update();
			 exams.update();
			 esito.update();
			 courses.show();
		}
	 }
	 
	 
	 