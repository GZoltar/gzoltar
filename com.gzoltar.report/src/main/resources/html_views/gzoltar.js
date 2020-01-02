/*
  Copyright (C) 2020 GZoltar contributors.

  This file is part of GZoltar.

  GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
  Lesser General Public License as published by the Free Software Foundation, either version 3 of
  the License, or (at your option) any later version.

  GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
  General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
  not, see <https://www.gnu.org/licenses/>.
*/

/*
 * Javascript functions for GZoltar's html-based fault localization
 * reports.
 */

/**
 * 
 */
function probabilities(o) {
  var j = 0;
  var maxProb = 0;
  for (var i in o) {
    if (o.hasOwnProperty(i) && typeof o.children !== "undefined") {
      if (o.probability > maxProb) {
        maxProb = o.probability;
      }
      probabilities(o.children[j])
    }
    j++;
  }
  return maxProb;
};

/**
 * 
 */
function noAllLevels(nodes) {
  for (var i = 0; i < nodes.length; i++) {
    if (nodes[i].children && nodes[i].children.length == 1) {
      nodes.splice(i, 1);
      i--;
    }
  }
  return nodes;
};

/**
 * 
 */
function gradient(stops, colors) {
  obj = new Object();
  obj.stops = stops;
  obj.colors = colors;
  obj.get = function(n) {
    var n_stops = this.stops.length;
    var start = 0;
    var end = 0;
    var perc = 0;
    if (n <= this.stops[0]) {
      end = start = this.colors[0];
    } else if (n >= this.stops[n_stops - 1]) {
      end = start = this.colors[n_stops - 1];
    } else {
      for (var i = 1; i < n_stops; i++) {
        if (n <= this.stops[i]) {
          break;
        }
      }
      start = this.colors[i - 1];
      end = this.colors[i];
      perc = (n - this.stops[i - 1]) / (this.stops[i] - this.stops[i - 1]);
    }
    var ret = new Array();
    for (var i = 0; i < start.length; i++) {
      ret[i] = start[i] + (end[i] - start[i]) * perc;
      if (i != 3) {
        ret[i] = Math.round(ret[i]);
      }
    }
    return ret;
  };
  return obj;
};

function rgba(r, g, b, a) {
  return new Array(r, g, b, a);
};

g = gradient([0, 0.5, 1], [rgba(0, 255, 0, 1), rgba(255, 255, 0, 1), rgba(255, 0, 0, 1)]);
gt = gradient([0, 0.5, 1], [rgba(0, 255, 0, 0.6), rgba(255, 255, 0, 0.6), rgba(255, 0, 0, 0.6)]);
gt2 = gradient([0, 0.5, 1], [rgba(100, 255, 100, 1), rgba(255, 255, 100, 1), rgba(255, 100, 100, 1)]);

/**
 * 
 */
function getG() {
  return g;
};

/**
 * 
 */
function getGt() {
  return gt;
};

/**
 * 
 */
function fill(d) {
  colors = g.get(maxProb == 0 ? 0 : d.probability / maxProb);
  return "rgba(" + colors[0] + ", " + colors[1] + ", " + colors[2] + ", " + colors[3] + ")";
};

/**
 * 
 */
function mouseover() {
  d3.select(this).style("fill", function(d) {
    colors = gt.get(maxProb == 0 ? 0 : d.probability / maxProb);
    return "rgba(" + colors[0] + ", " + colors[1] + ", " + colors[2] + ", " + colors[3] + ")";
  });
};

/**
 * 
 */
function mouseover2() {
  d3.select(this).style("fill", function(d) {
    colors = gt2.get(maxProb == 0 ? 0 : d.probability / maxProb);
    return "rgba(" + colors[0] + ", " + colors[1] + ", " + colors[2] + ", " + colors[3] + ")";
  });
};

/**
 * 
 */
function mouseout() {
  d3.select(this).style("fill", fill);
};

/**
 * 
 */
function resizeSvg() {
  d3.select("svg").attr("width", w).attr("height", h);
};

/**
 * 
 */
function resizeBody() {
  d3.select("body").attr("width", w).attr("height", h);
};

/**
 * 
 */
function onResize() {
  d3.select(window).on("resize", resize);
};

/**
 * 
 */
function onRightClick() {
  d3.select(window).on("contextmenu", function() {
    clicked = root;
    click(root);
    d3.event.preventDefault();
  });
};

/**
 * 
 */
function zoomRedraw() {
  if (d3.event) {
    svg.attr("transform", "translate(" + d3.event.translate + ")" + " scale(" + d3.event.scale + ")");
  }
};

