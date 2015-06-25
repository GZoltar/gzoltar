function Gradiant(normal) {
    var normalGradient = this.gradientInit(normal);

    this.normal = function(node) {
        if (node.score >= 0) {
            return normalGradient[Math.round(node.score)];
        }
        return "grey";
    };
}

Gradiant.prototype.colorToRga =  function (color) {
        colorObj = tinycolor(color).toRgb();
        return [colorObj.r, colorObj.g, colorObj.b, colorObj.a];
};


Gradiant.prototype.convertGradiantStopColors = function(gradient){
        var ret = {
            stops: [],
            colors: []
        };
        for (i = 0, len = gradient.length; i < len; i++) {
            ret.stops.push(gradient[i].position / 100);
            ret.colors.push(Gradiant.prototype.colorToRga(gradient[i].color));
        }
        return ret;
};

Gradiant.prototype.gradientCalculatorToArray = function(gradianteCalculator){
        var grandianteComputed = [];
        var rgba;
        for (var i = 0; i <= 100; i++) {
            rgba = gradianteCalculator.get(i / 100);
            grandianteComputed.push("rgba(" + rgba[0] + ", " + rgba[1] + ", " + rgba[2] + ", " + rgba[3] + ")");
        }
        return grandianteComputed;
};

Gradiant.prototype.gradientInit = function(gradient) {
    var gradientStopsColors = this.convertGradiantStopColors(gradient);
    var gradianteCalculator = this.gradientCalc(gradientStopsColors);
    return this.gradientCalculatorToArray(gradianteCalculator);
};

Gradiant.prototype.gradientCalc = function(stopsColorObj) {
    obj = stopsColorObj;
    obj.get = function(n) {
        var n_stops = this.stops.length;
        var start = 0;
        var end = 0;
        var perc = 0;
        if (n <= this.stops[0]) end = start = this.colors[0];
        else if (n >= this.stops[n_stops - 1]) end = start = this.colors[n_stops - 1];
        else {
            for (var i = 1; i < n_stops; i++)
                if (n <= this.stops[i]) break;
            start = this.colors[i - 1];
            end = this.colors[i];
            perc = (n - this.stops[i - 1]) / (this.stops[i] - this.stops[i - 1]);
        }
        var ret = [];
        for (var j = 0; j < start.length; j++) {
            ret[j] = start[j] + (end[j] - start[j]) * perc;
            if (j != 3) ret[j] = Math.round(ret[j]);
        } 
        return ret;
    };
    return obj;
};
