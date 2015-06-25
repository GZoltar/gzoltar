//Program entry point
function init() {
    $('body').disableSelection();

    var configuration = new Configuration();
    if (window.location.hash == "#reset") {
        configuration.resetConfig();
    }
    if (window.data_ex == undefined) {
        if (window.location.hash.indexOf("#srcLoad_") >= 0) {
            var script = window.location.hash.replace('#srcLoad_', '');
            $.ajax({
                async: false,
                url: script,
                dataType: "script"
            });
        } else if (window.location.hash.indexOf("#jsonLoad_") >= 0) {
            var src = window.location.hash.replace('#jsonLoad_', '');
            $.ajax({
                url:src,
                dataType:'text',
                success:initializeVisualization
            });
        } else if (window.location.hash.indexOf("#rest:") >= 0) {
            var src = window.location.hash.replace('#rest:', '');
            src = src.replace(':', '/')
            src = '/reports/' + src + '/visualization/'
            $.ajax({
                url:src,
                dataType:'text',
                success:initializeVisualizationREST
            });
        }
        else {
            $.each(configuration.currentConfig.scriptsLoad, function(index, script) {
                $.ajax({
                    async: false,
                    url: script,
                    dataType: "script"
                });
            });
        }
    }

    var dataManager = new DataManager(window.data_ex, configuration);
    dataManager.applyFilters();

    var visualization = new Visualizations(configuration, dataManager);
    window.addEventListener("resize", visualization.resize);
    renderButtonsHtml(visualization.setVisualization, visualization.getInitVisN());



    visualization.init();


}

function getVizID(vizNo) {
    return 'tabs-' + vizNo;
}

//Render html of the buttons
function renderButtonsHtml(visActivationCallBack, defaultTab) {
    $("body").append('<div id="tabs"><ul></ul></div>');
    $.each(visualizations, function(index, visualization) {
        $("#tabs ul").append('<li><a href="#' + getVizID(index) + '">' + visualization.displayName + '</a></li>');
        $("#tabs").append('<div id="' + getVizID(index) + '"></div>');
    });

    $("#tabs").tabs({
        activate: function(event, ui) {
            var visualizationIndex = ui.newTab.children("a").attr("href").replace("#tabs-", "");
            visActivationCallBack(visualizationIndex);
            event.currentTarget.blur();
        },
        active: defaultTab
    });

}


function getDimensions() {
    return {
        width: $(window).width() - 5,
        height: $(window).height() - 90
    }
}


//Function called when a node is clicked on the visualization Now is just shows an alert
function visClickEv(node) {
    var anscestors = getAncestors(node);
    var str = "";
    var len = anscestors.length;
    for (var i = 0; i < len; i++) {
        str += anscestors[i].name + " -> ";
    };
    str += node.name;
    //alert(str);
}

function validRegex(str) {
    return str != undefined && str != null && str.length > 0 && str != " ";
}


jQuery.fn.extend({
    disableSelection: function() {
        document.body.style.webkitUserSelect = "none";
        document.body.style.MozUserSelect = "none";
        return this.each(function() {
            this.onselectstart = function() {
                return false;
            };
            this.unselectable = "on";
            jQuery(this).css('user-select', 'none');
            jQuery(this).css('-o-user-select', 'none');
            jQuery(this).css('-moz-user-select', 'none');
            jQuery(this).css('-khtml-user-select', 'none');
            jQuery(this).css('-webkit-user-select', 'none');
        });
    }
});
