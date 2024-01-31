(function() {

    function getCellValue(tr, idx) {
        return tr.children[idx].textContent;
    }

    function compareGrades(first, second) {
        var list = [ "assente", "rimandato", "riprovato", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "30L"];
        return list.indexOf(first) - list.indexOf(second);
    }

    function compareStates(first, second) {
        var list = ["NON INSERITO" , "INSERITO", "PUBBLICATO" , "RIFIUTATO", "VERBALIZZATO"];
        return list.indexOf(first) - list.indexOf(second);
    }

    function createComparer(idx, asc) {
        return function(a, b) {
            var v1 = getCellValue(asc ? a : b, idx),
                v2 = getCellValue(asc ? b : a, idx);
            if(idx === 6){
                return compareGrades(v1, v2);
            }
            else if(idx===5){
                return compareStates(v1, v2);
            }
            else {
                if (v1 === '' || v2 === '' || isNaN(v1) || isNaN(v2)) {
                    return v1.toString().localeCompare(v2);
                }
                return v1 - v2;
            }

        };
    }


    document.querySelectorAll('th.sortable').forEach(function(th) {
        th.addEventListener('click', function () {
            var table = th.closest('table');
            Array.from(table.querySelectorAll('tbody > tr'))
                .sort(createComparer(Array.from(th.parentNode.children).indexOf(th), this.asc = !this.asc))
                .forEach(function(tr) {
                    table.querySelector('tbody').appendChild(tr)
                });
        });
    });
})();