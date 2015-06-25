function VerticalPartition(data, elementSel, configuration, events) {
    var self = this;
    data = data[0];

    this.data = data;
    this.configuration = configuration;
    this.events = events;

    var dimensions = getDimensions();

    var rect_render = new RectRender(dimensions.width, dimensions.height, configuration);

    var partition = d3.layout.partition()
        .value(function(node) {
            return 1;
        });


    var element = d3.select(elementSel);
    var svg, rect;


    this.zoomEvents = null;
    this.clicked = self.data;
    this.stateManager = new StateManager(self);

    self.keyBindings = null;
    var activations = ActivationsInfo(configuration);
    this.render = function() {
        element.html("");

        self.nodeInfoDisplay = new NodeInfoDisplay(elementSel, self.dblclick, configuration);
        self.stateManager.initRender(elementSel);

        var zoomElement = element.append("svg")
            .attr("style","overflow: hidden;")
            .attr("width", dimensions.width)
            .attr("height", dimensions.height)
            .append("g");

        svg = zoomElement.append("g");


        rect = svg.selectAll("rect")
            .data(partition.nodes(self.data))
            .enter().append("rect")
            .attr("x", rect_render.x)
            .attr("y", rect_render.y)
            .attr("width", rect_render.width)
            .attr("height", rect_render.height)
             .attr("title", activations.tooltipContent)
            .style("stroke", "#fff")
            .attr("fill", configuration.gradiante.normal)
            .on("click", self.click)
            .on("dblclick", self.dblclick)
            .on("mouseover", self.nodeInfoDisplay.mouseover)
            .on("mouseleave", self.nodeInfoDisplay.mouseleave);

        self.nodeInfoDisplay.setClicked(self.data);
        self.nodeInfoDisplay.setPath(rect);

        self.zoomEvents = ZoomController(elementSel, zoomElement, svg, self.configuration);

        if (self.keyBindings === null) {
            self.keyBindings = new KeyBindings(self, configuration);
        }
        else
        {
            self.keyBindings.setKeyBindings();
        }

        activations.renderTooltip($('rect'));
    }

    var isMovingNode = false;
    this.dblclick = function(node) {
        if (isMovingNode || node == self.clicked)
            return false;
        self.stateManager.saveState();
        self.zoomEvents.zoomReset();
        self.gotoNode(node, self.configuration.currentConfig.animationTransitionTime);
    }



    this.gotoNode = function(node, animationTime) {
        if (isMovingNode)
            return false;
        isMovingNode = true;
        self.nodeInfoDisplay.setClicked(node);
        rect_render.rectAnimation(rect, node, animationTime).each("end", function() {
            isMovingNode = false;
        });
        self.clicked = node;
        return true;
    }

    this.click = function(node) {
        if (d3.event.hasOwnProperty('zoomed')) return;
        events.click(node);
    }

    this.resize = function() {
        if (self.resizeTimeOut !== undefined) {
            clearTimeout(self.resizeTimeOut);
        }
        self.resizeTimeOut = setTimeout(function() {
            dimensions = getDimensions();
            rect_render = new RectRender(dimensions.width, dimensions.height, self.configuration);
            self.render();
            self.gotoNode(self.clicked, 0);
        }, 250);
    }

}


function RectRender(width, height, configuration) {
    var x = d3.scale.linear()
        .range([0, width]);

    var y = d3.scale.linear()
        .range([0, height]);

    this.x = function(node) {
        return x(node.x);
    }

    this.y = function(node) {
        return y(node.y);
    }

    this.width = function(node) {
        return x(node.dx);
    }

    this.height = function(node) {
        return y(node.dy);
    }

    this.rectAnimation = function(rect, node, time) {
        x.domain([node.x, node.x + node.dx]);
        y.domain([node.y, 1]).range([node.y ? 20 : 0, height]);

        return rect.transition()
            .duration(time)
            .attr("x", function(node) {
                return x(node.x);
            })
            .attr("y", function(node) {
                return y(node.y);
            })
            .attr("width", function(node) {
                return x(node.x + node.dx) - x(node.x);
            })
            .attr("height", function(node) {
                return y(node.y + node.dy) - y(node.y);
            });
    }
}