function Visualizations(configuration, dataManager) {
    var self = this;
    var nodeToRender = null;
    var currentVisualizationN, currentVisualization;
    var visualizationsStructure = [];

    var events = {
        click: sendClickEvent,
        switchToViz: function(visN, node) {
            nodeToRender = node;
            $('#tabs').tabs({
                active: visN
            });
        },
        filtersUpdate: function() {
            dataManager.updatefilter();
            if (currentVisualizationN < 3) {
                visualizationsStructure = [];
                currentVisualization.data = dataManager.getData().tree[0];
                self.setVisualization(currentVisualizationN);
            }
        }
    };



    this.createVisualization = function(visN) {
        if (dataManager.getData() == undefined) {
            return new visualizations[visN].obj(null, '#' + getVizID(visN), configuration, events);
        }
        return new visualizations[visN].obj(dataManager.getData().tree, '#' + getVizID(visN), configuration, events);

    }


    this.prepareVisualization = function(visN) {
        if(currentVisualization !== undefined && currentVisualization.hasOwnProperty('onExit')){
            currentVisualization.onExit();
        }
        configuration.currentConfig.lastViewed = visN;
        configuration.saveConfig();
        currentVisualizationN = visN;
        if (visualizationsStructure[visN] === undefined) {
            visualizationsStructure[visN] = self.createVisualization(visN);
            visualizationsStructure[visN].render();
        }
        currentVisualization = visualizationsStructure[visN];
    }

    this.setVisualization = function(visN) {
        if (dataManager.getData() != undefined || visN == 3) {
            self.prepareVisualization(visN);
            if (nodeToRender != null) {
                currentVisualization.dblclick(nodeToRender);
                nodeToRender = null;
            }
            if (currentVisualization.hasOwnProperty('keyBindings')) {
                currentVisualization.keyBindings.setKeyBindings();
                document.onkeydown = currentVisualization.keyBindings.keyPress;
            } else {
                document.onkeydown = null;
            }

            if(currentVisualization.hasOwnProperty('onEnter')){
                currentVisualization.onEnter();
            }
        }
    }

    this.getInitVisN = function() {
        if (configuration.currentConfig.defaultView >= 0) {
            return configuration.currentConfig.defaultView;
        }
        return configuration.currentConfig.lastViewed;
    }

    this.resize = function() {
        visualizationsStructure.forEach(function(visualization) {
            if (visualization.hasOwnProperty('resize')) {
                visualization.resize();
            }
        });
    }

    this.init = function() {
        self.setVisualization(self.getInitVisN());
    }

}