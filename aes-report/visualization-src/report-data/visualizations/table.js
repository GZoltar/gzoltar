function Table(data, elementSel, configuration, events) {
    var self = this;

    var scrollState = 0;
    var rendered = false;
    this.onEnter = function(){
        if(rendered){
            $('.dataTables_scrollBody').scrollTop(scrollState);
            table.columns.adjust();
        }
    };


    this.getTableRows = function() {
        var ret = [];
        for (len = data.length, i = 0; i < len; i++) {
            var node = data[i];
            if (node !== null && node !== undefined && isLastNode(node) && node.parent != undefined && node.parent.parent != undefined) {
                ret.push([getClass(node), getFunction(node), getName(node), node.score, i]);
            }
        }
        return ret;
    }


    this.resize = function() {
        self.render();
    }
    var activations = ActivationsInfo(configuration);
    var table;
    var tableData = this.getTableRows();
    this.render = function() {
        rendered = true;

        $(elementSel).html('<table cellpadding="0" cellspacing="0" border="0" class="display" id="tableData"></table>');

        table = $('#tableData').DataTable({
            "scrollY": $(window).height() - 160 + "px",
            "retrieve": true,
            "scrollCollapse": true,
            "data": tableData,
            "columns": [{
                "title": "Class"
            }, {
                "title": "Function"
            }, {
                "title": "Line",
                "class": "center"
            }, {
                "title": "E. S.",
                "class": "center"
            }],
            "order": [3, "desc"],
            "createdRow": function(row, tdata, index) {
                var node = data[tdata[4]];
                $(row).attr('title',getPackage(node));
                $('td', row).eq(0).prepend('<div class="tableCircle" style="background-color: ' + configuration.gradiante.normal(node) + ';">+</div>');
            },
            "iDisplayLength": configuration.currentConfig.defaultTableEntries
        });

        table.on('click', 'tr', function() {
            var tr = $(this).closest('tr');
            var row = table.row(tr);
            var node = data[row.data()[4]];
            events.click(node);
            if ($(this).hasClass('selected')) {
                $(this).removeClass('selected');
            } else {
                table.$('tr.selected').removeClass('selected');
                $(this).addClass('selected');
            }
        });


        function format(rowData) {
            var node = data[rowData[4]];
            return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
            '<tr>'+
            '<td>Package:</td>'+
            '<td>'+getPackage(node)+'</td>'+
            '</tr>'+ activations.renderTableLines(node) +
            '</table><button class="vizb" id="vizb0">Go to Sunburst</button><button class="vizb" id="vizb1">Go to Vertical Partition</button>';

        }

        table.on('click', '.tableCircle', function() {
            var tr = $(this).closest('tr');
            var row = table.row(tr);

            if (row.child.isShown()) {
                // This row is already open - close it
                $(this).html('+');
                row.child.hide();
                tr.removeClass('shown');
            } else {
                $(this).html('-');
                // Open this row
                row.child(format(row.data())).show();
                tr.addClass('shown');

                $('.vizb').button().click(function(e) {
                    var rawElement = this;
                    var $element = $(this);
                    var vizN = $element.attr('id').replace('vizb', '');
                    events.switchToViz(vizN, data[row.data()[4]]);
                    return false;
                    /* Do stuff with the element that fired the event */
                });
            }
        });

        $('.dataTables_scrollBody').scroll(function(){
            scrollState = $('.dataTables_scrollBody').scrollTop();
        });

    }

}