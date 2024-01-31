/**
 * 
 */

 (function() {

	 var courses, exams, esito, name
	 Pageorchestrator = new PageOrchestrator;

	 window.addEventListener("load", () => {
		 if (sessionStorage.getItem("User") == null)
			 window.location.href = "LoginPage.html";
		 else {
			 Pageorchestrator.start();
			 Pageorchestrator.refresh();
		 }
	 })

	 function getStatusString(status){
		 switch(status) {
			 case "NOT_INSERTED":
				 return "NON INSERITO";
			 case "INSERTED":
				 return "INSERITO";
			 case "PUBLISHED":
				 return "PUBBLICATO";
			 case "REFUSED":
				 return "RIFIUTATO";
			 case "VERBALIZED":
				 return "VERBALIZZATO";
			 default:
				 return "NON INSERITO";
		 }
	 }

	 function Name(_username, messagecontainer) {
		 this.username = _username;
		 this.messagecontainer= messagecontainer;
		 this.show = function () {
			 var self= this;
			 self.messagecontainer.textContent = "Benvenuto "+self.username;
		 }
	 }

	 function Courses(error, table, content) {
		 this.error = error;
		 this.table = table;
		 this.content = content;

		 this.reset = function () {
			 this.table.style.visibility = "hidden";
		 }

		 this.show = function () {
			 var th = this;
			 makeCall("GET", "GoToHome", null,
				 function (req) {
					 if (req.readyState === XMLHttpRequest.DONE) {
						 if (req.status === 200) {
							 var resultdata = JSON.parse(req.responseText);
							 if (resultdata.length === 0) {
								 th.error.textContent = "Non risulta iscritto ad alcun corso";
								 return;
							 }
							 th.update(resultdata);
						 } else
							 Pageorchestrator.refresh();
					 }
				 });
		 }

		 this.update = function (courses) {
			 var self=this;
			 var row, coursecode, coursename, anchor;
			 this.content.innerHTML = "";
			 courses.forEach(function (course) {
				 row = document.createElement("tr");
				 coursecode = document.createElement("td");
				 anchor = document.createElement("a");
				 anchor.setAttribute("corso", course.courseId);
				 text = document.createTextNode(course.courseId);
				 anchor.addEventListener("click", (e) => {
					 Pageorchestrator.refresh();
					 exams.show(e.target.getAttribute("corso"));
				 });
				 anchor.href = "#";
				 anchor.appendChild(text);
				 coursecode.appendChild(anchor);
				 row.appendChild(coursecode);
				 coursename = document.createElement("td");
				 coursename.textContent = course.courseName;
				 row.appendChild((coursename));
				 self.content.appendChild(row);
			 })
			 self.table.style.visibility = "visible";
		 };
	 }

	 function Exams(errore, table, content) {
		 this.error = errore;
		 this.head = table;
		 this.body = content;

		 this.reset = function () {
			 this.head.style.visibility = "hidden";
		 }


		 this.show = function (courseid) {
			 var self = this;
			 makeCall("GET", "/Verbalizzazione_Esami_JS_web_exploded/GetExams?corso=" + courseid, null,
				 function (req) {
					 if (req.readyState === 4) {
						 var message = req.responseText;
						 if (req.status === 200) {
							 var appelliToShow = JSON.parse(req.responseText);
							 if (appelliToShow.length === 0) {
								 self.error.textContent = "Non ci sono appelli per questo corso a cui sei iscritto!";
								 return;
							 }
							 self.update(appelliToShow, courseid);
						 } else {
							 Pageorchestrator.refresh();
						 }
					 }
				 }
			 );

		 };

		 this.update = function (arrayAppelli, courseid) {
			 var row, linkcell, anchor, linkText;
			 this.body.innerHTML = "";
			 var self = this;
			 arrayAppelli.forEach(function (appello) {
				 row = document.createElement("tr");
				 linkcell = document.createElement("td");
				 anchor = document.createElement("a");
				 linkcell.appendChild(anchor);
				 linkText = document.createTextNode(appello);
				 anchor.appendChild(linkText);
				 anchor.setAttribute('appellodata', appello);
				 anchor.setAttribute('corso', courseid);
				 anchor.addEventListener("click", (e) => {
					 esito.show(e.target.getAttribute("appellodata"), e.target.getAttribute("corso"));
				 }, false);
				 anchor.href = "#";
				 row.appendChild(linkcell);
				 self.body.appendChild(row);
			 });
			 self.head.style.visibility = "visible";
		 };

	 }


	 this.Esito = function (error, esito, result, refuse) {
		 this.head = esito;
		 this.body=result
		 this.err = error;
		 this.ref = refuse;
		 this.refuse = function () {
			 this.ref.querySelector("input[type='button']").addEventListener('click', (e) => {
				 var self = this, formToSend = e.target.closest("form");
				 makeCall("POST", "/Verbalizzazione_Esami_JS_web_exploded/Refuse", formToSend,
					 function (req) {
						 if (req.readyState === XMLHttpRequest.DONE) {
							 if (req.status === 200) {
								 self.reset();
								 self.show(formToSend.exd.value,formToSend.exc.value);
							 } else {
								 Pageorchestrator.refresh();
							 }
						 }
					 }
				 );
			 });
		 };
		 this.reset = function () {
			 this.head.style.visibility = "hidden";
			 this.ref.style.visibility = "hidden";
		 }

		 this.show = function (data, corso) {
			 var self = this;
			 makeCall("GET", "/Verbalizzazione_Esami_JS_web_exploded/GetResOrRegs?exc=" + corso + "&&exd=" + data, null, function (req) {
					 if (req.readyState === 4) {
						 var message = req.responseText;
						 if (req.status === 200) {
							 var esitoToShow = JSON.parse(message);
							 if (getStatusString(esitoToShow.status) === 'RIFIUTATO') {
								 self.reset();
								 self.err.textContent = "La valutazione è stata rifiutata";
								 return;
							 }
							 self.update(esitoToShow, data, corso);
						 } else {
							 self.err.textContent="Non sono presenti valutazioni associate a questo appello";
						 }
					 }
				 }
			 );
		 };

		 this.update = function (newEsito, data, corso) {
			 var cell, row,text;
			 var self = this;
			 self.body.innerHTML = "";
			 row = document.createElement("tr");
			 cell = document.createElement("td");
			 text=document.createTextNode("MATRICOLA");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("COGNOME");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("NOME");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("CORSO DI LAUREA");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("E-MAIL");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("CORSO");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("DOCENTE");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("DATA APPELLO");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("STATO VALUTAZIONE");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode("VOTO");
			 cell.appendChild(text);
			 row.appendChild(cell);
			 self.body.appendChild(row);
			 row = document.createElement("tr");
			 cell = document.createElement("td");
			 text=document.createTextNode(newEsito.matr);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(newEsito.surname);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(newEsito.name);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(newEsito.gc);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(newEsito.email);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(newEsito.course);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(newEsito.teacherSurname +" "+newEsito.teacherName);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(data);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(getStatusString(newEsito.status));
			 cell.appendChild(text);
			 row.appendChild(cell);
			 cell = document.createElement("td");
			 text=document.createTextNode(newEsito.vote);
			 cell.appendChild(text);
			 row.appendChild(cell);
			 self.body.appendChild(row);
			 self.head.style.visibility = "visible";
			 if (newEsito.status === 'PUBLISHED') {
				 self.ref.exc.value = corso ;
				 self.ref.exd.value = data;
				 self.ref.style.visibility = "visible";
			 } else {
				 self.ref.style.visibility = "hidden";
			 }
		 };

	 }


	 function PageOrchestrator() {
		 var error = document.getElementById("id_error");

		 this.start = function () {
			 name = new Name(sessionStorage.getItem("User"), document.getElementById("id_welcomemessage"))
			 name.show();

			 courses = new Courses(error, document.getElementById("id_tabellacorsi"),
				 document.getElementById("id_corsi"));

			 exams = new Exams(error, document.getElementById("id_tabellaappelli"),
				 document.getElementById("id_appelli"));

			 esito = new Esito(error, document.getElementById("id_esito"), document.getElementById("id_result"),
				 document.getElementById("id_refuse"));
			 esito.refuse();

			 document.querySelector("a[href='Logout']").addEventListener('click', () => {
				 window.sessionStorage.removeItem('user');
			 })

		 };

		 this.refresh = function () {
			 error.textContent = "";
			 courses.show();
			 exams.reset();
			 esito.reset();
		 };
	 }
 })();
	 
	 
	 