function StateManager(visualization) {
    var self = this;
    var stack = [];
    var currentNumber = 0;
    var maxRedo = 0;

    var interfaceButtons,undoButton,redoButton;
    this.initRender = function(elementSel) {
        interfaceButtons = $('<div class="iButtons"></div>');
        $(elementSel).append(interfaceButtons);
        undoButton = $('<img class="undoButton" style="display: inline" src="report-data/components/stateManager/backButton.png" />');
        redoButton = $('<img class="redoButton" style="display: inline" src="report-data/components/stateManager/redoButton.png" />');
        interfaceButtons.append(undoButton);
        interfaceButtons.append(redoButton);
        undoButton.click(self.undo);
        redoButton.click(self.redo);
        self.setButtonVis();
    }

    this.setButtonVis = function() {
        if (self.undoPossible()) {
            undoButton.css('opacity', '1');
        } else {
            undoButton.css('opacity', '0.3');
        }

        if (self.redoPossible()) {
            redoButton.css('opacity', '1');
        } else {
            redoButton.css('opacity', '0.3');
        }
    }

    this.getLast = function() {
        if (self.undoPossible()) {
            return stack.slice(-1)[0];
        }
        return null;
    }

    this.undoPossible = function() {
        return currentNumber > 0;
    }

    this.redoPossible = function(){
        return maxRedo > 0;
    }

    this.getState = function(){
        return [visualization.clicked, visualization.zoomEvents.getZoom()];
    }

    this.saveStateStack = function(){
        stack[currentNumber] = self.getState();
    }

    this.saveState = function() {
        self.saveStateStack();
        currentNumber++;
        maxRedo = 0;
        self.setButtonVis();
    }

    this.gotoState  = function(state){
        if (visualization.gotoNode(state[0], visualization.configuration.currentConfig.animationTransitionTime) != false) {
            visualization.zoomEvents.setZoom(state[1]);
            return true;
        }
        return false;
    }

    this.undo = function() {
        if (!self.undoPossible()) {
            return;
        }
        if(maxRedo == 0){
            self.saveStateStack();
        }
        if (self.gotoState(stack[currentNumber-1])) {
            currentNumber--;
            maxRedo++;
            self.setButtonVis();
        }
    }

    this.redo = function (){
        if(!self.redoPossible()){
            return;
        }
        if (self.gotoState(stack[currentNumber+1])) {
            currentNumber++;
            maxRedo--;
            self.setButtonVis();
        }
    }
}