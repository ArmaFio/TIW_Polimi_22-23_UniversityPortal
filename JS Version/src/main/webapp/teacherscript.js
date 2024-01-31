(function () {
    var courses, exams, registrations, verbal, name
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

    function Name(username, messagecontainer) {
        this.username = username;
        this.messagecontainer= messagecontainer;
        this.show = function () {
            var self=this;
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
            makeCall("GET", 'GoToHome', null,
                function (req) {
                    if (req.readyState === XMLHttpRequest.DONE) {
                        if (req.status === 200) {
                            var resultdata = JSON.parse(req.responseText);
                            if (resultdata.length === 0) {
                                th.error.textContent = "Non risulta docente di alcun corso";
                                return;
                            }
                            th.update(resultdata);
                        } else
                            Pageorchestrator.refresh();

                    }
                });
        }

        this.update = function (courses) {
            var row, coursecode, coursename, anchor, text;
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
                content.appendChild(row);
            })
            this.table.style.visibility = "visible";
        };
    }

    function Exams(error, table, content) {
        this.error = error;
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
                                self.error.textContent = "Non ci sono appelli per questo corso";
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

        this.update = function (arrayAppelli, corso) {
            var row, linkcell, anchor, linktext;
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
                anchor.setAttribute('corso', corso );
                anchor.addEventListener("click", (e) => {
                    registrations.show(e.target.getAttribute("appellodata"), e.target.getAttribute('corso'));
                }, false);
                anchor.href = "#";
                row.appendChild(linkcell);
                self.body.appendChild(row);
            });
            this.head.style.visibility = "visible";
        };

    }

    function Registrations(error, table, content, student, insert, publish, verbalize, multi, conferma){
        this.err= error;
        this.tab= table;
        this.cont= content;
        this.student=student;
        this.form=insert;
        this.pub= publish;
        this.verb= verbalize;
        this.button= multi;
        this.confirm= conferma;

        this.reset= function (){
            this.tab.style.visibility="hidden";
            this.pub.style.visibility="hidden";
            this.form.style.visibility="hidden";
            this.verb.style.visibility="hidden";
            this.button.style.visibility="hidden";
            this.confirm.style.visibility="hidden";
        }

        this.show= function (date, course){
            var self= this;
            makeCall("GET", "/Verbalizzazione_Esami_JS_web_exploded/GetResOrRegs?exc=" + course + "&&exd=" + date, null, function (req) {
                if (req.readyState === 4) {
                    var message = req.responseText;
                    if (req.status === 200) {
                        var registrations = JSON.parse(message);
                        Pageorchestrator.refresh();
                        self.update(registrations, course, date);
                    } else
                        self.err.textContent = "Non ci sono esiti associati a questo corso";
                }
            },)
        }

        this.update= function (regs, course, date){
            var self= this;
            var row,cell, anchor, text; 
            self.cont.innerHTML="";
            regs.forEach(function(iscr) {
                row = document.createElement("tr");
                cell = document.createElement("td");
                text = document.createTextNode(iscr.matr);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.name);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.surname);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.gc);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.email);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(getStatusString(iscr.status));
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                if(getStatusString(iscr.status)!=="NON INSERITO")
                    text = document.createTextNode(iscr.vote);
                else
                    text = document.createTextNode("");
                cell.appendChild(text);
                row.appendChild(cell);
                if (getStatusString(iscr.status) === "INSERITO" || getStatusString(iscr.status) === "NON INSERITO") {
                    cell = document.createElement("td");
                    anchor = document.createElement("a");
                    text = document.createTextNode("Modifica");
                    anchor.appendChild(text);
                    anchor.setAttribute('exd', date);
                    anchor.setAttribute('exc', course);
                    anchor.setAttribute('matr', iscr.matr);
                    anchor.addEventListener("click", (e) => {
                        self.modify(e.target.getAttribute("exd"), e.target.getAttribute("exc"), e.target.getAttribute("matr"));
                    }, false);
                    anchor.href = "#";
                    cell.appendChild(anchor);
                    row.appendChild(cell);
                }
                self.cont.appendChild(row);
            })
            self.tab.style.visibility="visible";
            self.pub.exc.value=course;
            self.pub.exd.value=date;
            self.verb.exc.value=course;
            self.verb.exd.value=date;
            self.pub.querySelector("input[type='button']").addEventListener('click', (e) => {
                var self = this, formToSend = e.target.closest("form");
                makeCall("POST", 'Publish', formToSend,
                    function (req) {
                        if (req.readyState === 4) {
                            if (req.status === 200) {
                                self.reset();
                                self.show(date, course);
                            } else {
                                Pageorchestrator.refresh();
                            }
                        }
                    }
                );
            })
            self.verb.querySelector("input[type='button']").addEventListener('click', (e) => {
                verbal.verbalize(e.target.closest("form"));
            })
            self.pub.style.visibility="visible";
            self.verb.style.visibility="visible";
            self.button.addEventListener('click', ()=>{
                self.multipleinsert(regs, course, date);
            });
            self.button.style.visibility="visible";
        }


        this.multipleinsert= function(regs, course, date){
            var self= this;
            var row,cell, anchor, text, form , label, input;
            self.cont.innerHTML="";
            regs.forEach(function(iscr) {
                if(getStatusString(iscr.status)==="NON INSERITO") {
                    row = document.createElement("tr");
                    cell = document.createElement("td");
                    text = document.createTextNode(iscr.matr);
                    cell.appendChild(text);
                    row.appendChild(cell);
                    cell = document.createElement("td");
                    text = document.createTextNode(iscr.name);
                    cell.appendChild(text);
                    row.appendChild(cell);
                    cell = document.createElement("td");
                    text = document.createTextNode(iscr.surname);
                    cell.appendChild(text);
                    row.appendChild(cell);
                    cell = document.createElement("td");
                    text = document.createTextNode(iscr.gc);
                    cell.appendChild(text);
                    row.appendChild(cell);
                    cell = document.createElement("td");
                    text = document.createTextNode(iscr.email);
                    cell.appendChild(text);
                    row.appendChild(cell);
                    cell = document.createElement("td");
                    text = document.createTextNode(getStatusString(iscr.status));
                    cell.appendChild(text);
                    row.appendChild(cell);
                    cell = document.createElement("td");
                    form= document.createElement("form");
                    input=document.createElement("input");
                    input.setAttribute("type", "hidden");
                    input.setAttribute("name", "matr");
                    input.setAttribute("value", iscr.matr);
                    form.appendChild(input);
                    input=document.createElement("input");
                    input.setAttribute("type", "hidden");
                    input.setAttribute("name", "exc");
                    input.setAttribute("value", course);
                    form.appendChild(input);
                    input=document.createElement("input");
                    input.setAttribute("type", "hidden");
                    input.setAttribute("name", "exd");
                    input.setAttribute("value", date);
                    form.appendChild(input);
                    input=document.createElement("input");
                    input.setAttribute("type", "text");
                    input.setAttribute("name", "vote");
                    form.appendChild(input);
                    cell.appendChild(form);
                    row.appendChild(cell);
                    self.cont.appendChild(row);
                }
            })
            self.confirm.addEventListener('click', ()=>{
                var i=0;
                var rows= self.tab.getElementsByTagName("tr");
                for(var j=1; j< rows.length; j++) {
                    var formtosend = (rows[j].getElementsByTagName("td")[6]).querySelector("form");
                    if (formtosend.checkValidity()) {
                        makeCall("POST", 'Insert', formtosend, function (req) {
                            if (req.readyState===4) {
                                if (req.status===200)
                                    i++;
                            }
                        })
                    }
                }
                self.show(date,course);
                self.err.textContent= i+" voti inseriti";
                self.err.style.visibility= "visible";
            })
            self.confirm.style.visibility="visible";
            self.cont.style.visibility="visible";
        }

        this.modify= function (appellodata, corso, matr){
            var self= this;
            self.form.matr.value=matr;
            self.form.exc.value=corso;
            self.form.exd.value=appellodata;
            self.student.textContent="Matricola "+matr;
            self.form.querySelector("input[type='button']").addEventListener('click', (e)=>{
                var form=e.target.closest("form");
                if(form.checkValidity()) {
                    makeCall("POST", 'Insert', form, function (req) {
                        if (req.readyState === 4) {
                            var message = req.responseText;
                            if (req.status === 200) {
                                self.student.textContent = "";
                                self.reset();
                                self.show(appellodata, corso);
                                return;
                            } else {
                                self.err.textContent = "Impossible inserire la valutazione";
                            }
                        }
                    })
                }
            })
            self.form.style.visibility="visible";
        }

    }

    function Verbal(error, table, list, head){
        this.err= error;
        this.table=table;
        this.list= list;
        this.head= head;
        this.verbalize= function (form){
            var self= this;
            if(form.checkValidity()) {
                makeCall("POST", 'Verbalize', form, function (req) {
                    if (req.readyState === XMLHttpRequest.DONE) {
                        if (req.status === 200) {
                            var message = req.responseText;
                            var info = JSON.parse(message);
                            Pageorchestrator.refresh();
                            self.show(info);
                        }
                        else
                            self.err.textContent = "Nessuna valutazione verbalizzata";
                    }
                })
            }
        }
        this.show= function (info){
            var self=this;
            var row, cell,text;
            self.list.innerHTML="";
            row = document.createElement("tr");
            cell = document.createElement("td");
            text = document.createTextNode("Verbale N°"+info.id);
            cell.appendChild(text);
            row.appendChild(cell);
            cell = document.createElement("td");
            text = document.createTextNode(Date(info.dateTime));
            cell.appendChild(text);
            row.appendChild(cell);
            head.appendChild(row);
            row = document.createElement("tr");
            cell = document.createElement("td");
            text = document.createTextNode("MATRICOLA");
            cell.appendChild(text);
            row.appendChild(cell);
            cell = document.createElement("td");
            text = document.createTextNode("NOME");
            cell.appendChild(text);
            row.appendChild(cell);
            cell = document.createElement("td");
            text = document.createTextNode("COGNOME");
            cell.appendChild(text);
            row.appendChild(cell);
            cell = document.createElement("td");
            text = document.createTextNode("CORSO DI LAUREA");
            cell.appendChild(text);
            row.appendChild(cell);
            cell = document.createElement("td");
            text = document.createTextNode("E_MAIL");
            cell.appendChild(text);
            row.appendChild(cell);
            cell = document.createElement("td");
            text = document.createTextNode("VOTO");
            cell.appendChild(text);
            row.appendChild(cell);
            self.list.appendChild(row);
            info.registrations.forEach(function(iscr) {
                row = document.createElement("tr");
                cell = document.createElement("td");
                text = document.createTextNode(iscr.matr);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.name);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.surname);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.gc);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.email);
                cell.appendChild(text);
                row.appendChild(cell);
                cell = document.createElement("td");
                text = document.createTextNode(iscr.vote);
                cell.appendChild(text);
                row.appendChild(cell);
                self.list.appendChild(row)
            })
            self.table.style.visibility="visible";
        }

        this.reset= function(){
            this.table.style.visibility="hidden";
        }
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

            registrations = new Registrations(error, document.getElementById("id_tabellaesiti"),
                document.getElementById("id_esiti"), document.getElementById("id_matricola"),
                document.getElementById("id_insert"), document.getElementById("id_publish"),
                document.getElementById("id_verbalize"), document.getElementById("id_multipleinsert"),
                document.getElementById("id_conferma"));

            verbal = new Verbal(error, document.getElementById("id_verbalized"), document.getElementById("id_list"),
                document.getElementById("id_dativerbale"));


            document.querySelector("a[href='Logout']").addEventListener('click', () => {
                window.sessionStorage.removeItem('user');
            })
        };

        this.refresh = function () {
            error.textContent = "";
            courses.show();
            exams.reset();
            registrations.reset();
            verbal.reset();
        }

    }

})();