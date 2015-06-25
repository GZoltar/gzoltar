function NodeInfoDisplay(elementSel, clickFunction, configuration) {
    var self = this;

    var iD = $(elementSel).attr('id');

    var percentageArea, breadcrumbsArea, styleArea;
    this.init = function() {
        breadcrumbsArea = $('<div></div>');
        $(elementSel).append(breadcrumbsArea);

        percentageArea = $('<div></div>');
        $(elementSel).append(percentageArea);

        styleArea = $('<style></style>');
        $('body').append(styleArea);

    };

    this.nodeInfo = function(node) {
        self.updateScore(node);
        self.breadcrumbsRender(getAncestors(node));
    };


    this.breadcrumbsRender = function(nodesArray) {

        function idNodeI(i) {
            return iD + 'node-' + i;
        }

        breadcrumbsArea.html('');
        var renderElement = $('<ol class="breadcrumbs"></ol>');
        breadcrumbsArea.html(renderElement);

        var cssTring = '';
        var lengthN = nodesArray.length;
        for (var i = 0; i < lengthN; i++) {
            var elem = $('<li><a id="' + idNodeI(i) + '"><span>' + (false ? '' : nodesArray[i].n) + '</span></a></li>');
            if (isLastNode(nodesArray[i])) {
                elem.addClass("leaf-node");
            }
            renderElement.append(elem);
            cssTring += '#' + idNodeI(i) + ', #' + idNodeI(i) + ':before, #' + idNodeI(i) + ':after{background-color: ' + configuration.gradiante.normal(nodesArray[i]) + ';}';
            $('a', renderElement).click(function() {
                var id = $(this).attr('id').replace(iD + "node-", "");
                clickFunction(nodesArray[id]);
            })
        };
        styleArea.html(cssTring);
        var i = 1;
        while (renderElement.height() > 30) {
            $('#' + idNodeI(i)).remove();
            ++i;
        }
    };

    this.updateScore = function(node) {
        var color = configuration.gradiante.normal(node);
        // Now move and update the percentage at the end.
        var percentage, displayText;
        if (node.score >= 0) {
            percentage = node.score;
            displayText = percentage + '%';
        } else {
            percentage = 0;
            displayText = 'Uncalculated';
        }
        percentageArea.html('<div class="pace pace-active" style="border-color: ' + color + ';"><div class="pace-progress" data-progress="' + percentage + '" data-progress-text="' + displayText + '" style="width: ' + percentage + '%;   background: ' + color + '; color: ' + color + ';"><div class="pace-progress-inner"></div></div><div class="pace-activity"></div></div>');
    };


    this.updataBreadcumbTimed = function(node) {
        if (self.updateBreadcumbTimeOut !== undefined) {
            clearTimeout(self.updateBreadcumbTimeOut);
        }
        self.updateBreadcumbTimeOut = setTimeout(function() {
            self.nodeInfo(node);
        }, 250);
    };


    this.setClicked = function(node) {
        self.clickedNode = node;
        self.nodeInfo(node);
    };

    this.setPath = function(path) {
        self.path = path;
    };

    this.mouseover = function(node) {
        self.updataBreadcumbTimed(node);
        var sequenceArray = getAncestors(node);
        self.path.style("opacity", 0.3);

        self.path.filter(function(node) {
                return (sequenceArray.indexOf(node) >= 0);
            })
            .style("opacity", 1);
    };


    this.mouseleave = function(node) {
        self.updataBreadcumbTimed(self.clickedNode);
        //path.on("mouseover", null);
        self.path.style("opacity", 1);
        return;
        self.path.transition()
            .duration(1000)
            .style("opacity", 1)
            .each("end", function() {
                d3.select(this).on("mouseover", self.mouseover);
            });
    };

    this.init();

}