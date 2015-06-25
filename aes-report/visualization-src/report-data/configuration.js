function Configuration() {
    var self = this;

    this.CONFIG_STORE = CONFIG_VERSION;


    this.resetConfig = function() {
        self.currentConfig = jQuery.extend(DEFAULT_CONFIG, {
            lastViewed: DEFAULT_CONFIG.defaultView
        });
        self.saveGradiante();
    };

    this.loadConfig = function() {
        var localStorageConf;
        if (localStorage === undefined) {
            localStorageConf = null;
        } else {
            localStorageConf = localStorage.getItem(this.CONFIG_STORE);
        }
        if (localStorageConf === null || !(localStorageConf = JSON.parse(localStorageConf))) {
            self.resetConfig();
        } else {
            this.currentConfig = localStorageConf;
        }
        this.gradiante = new Gradiant(this.currentConfig.normalGradiante);
    };

    this.saveConfig = function() {
        if (localStorage !== undefined) {
            localStorage.setItem(this.CONFIG_STORE, JSON.stringify(self.currentConfig));
        }
    };

    this.saveGradiante = function() {
        self.saveConfig();
        self.gradiante = new Gradiant(self.currentConfig.normalGradiante);
    };

    this.saveAnimationTime = function(time){
        self.currentConfig.animationTransitionTime = time;
        self.saveConfig();
    };

    this.saveZoomAnimationTime = function(time){
        self.currentConfig.zoomAnimationTime = time;
        self.saveConfig();
    };

    this.saveMostRelevantFilter = function (value){
        self.currentConfig.filterMostRelevamtNodes = value;
        self.saveConfig(); 
    };

    this.saveProbabilityFilter = function (value){
        self.currentConfig.filterMinProbability = value;
        self.saveConfig(); 
    };



    self.loadConfig();
}