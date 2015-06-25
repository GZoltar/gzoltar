var ZoomController_HTML;

function ZoomController(elementSel, zoomElement, svg, configuration) {
    var zoomListener = d3.behavior.zoom().scaleExtent([1, 10]).on("zoom",
        function() {
            if (d3.event) {
                d3.event.zoomed = true;
                svg.attr("transform", "translate(" + d3.event.translate + ")" + "scale(" + d3.event.scale + ")");
            }
        });


    zoomListener(zoomElement);
    zoomElement.on("dblclick.zoom", null)
    zoomListener.event(zoomElement);
    zoomElement.on("mousedown", function() {
        d3.event.preventDefault();
    });


    function translate(vec) {
        var curTranslate = zoomListener.translate();
        curTranslate[0] += vec[0];
        curTranslate[1] += vec[1];
        setZoom([curTranslate, zoomListener.scale()]);
    }

    var eventsBlocked = false;

    function setZoom(zoom) {
        if (eventsBlocked)
            return;
        eventsBlocked = true;
        zoomListener.translate(zoom[0]);
        zoomListener.scale(zoom[1]);
        zoomListener.event(zoomElement.transition().duration(configuration.currentConfig.zoomAnimationTime).each("end", function() {
            eventsBlocked = false;;
        }));
    }

    var zoomContainerElem;
    var events = {
        up: function() {
            translate([0, 100]);
        },
        down: function() {
            translate([0, -100]);
        },
        right: function() {
            translate([-100, 0]);
        },
        left: function() {
            translate([100, 0]);
        },
        zoomIn: function() {
            setZoom([zoomListener.translate(), Math.min(10, zoomListener.scale() + 1)]);
        },
        zoomOut: function() {
            setZoom([zoomListener.translate(), Math.max(1, zoomListener.scale() - 1)]);
        },
        zoomReset: function() {
            setZoom([
                [0, 0], 1
            ]);
        },

        setZoom: function(zoom) {
            setZoom(zoom);
        },

        getZoom: function() {
            return [zoomListener.translate(), zoomListener.scale()];
        },

        zoomBlock: function() {
            eventsBlocked = true;
        },

        zoomUnlock: function() {
            eventsBlocked = false;
        },

        showZoom: function() {
            zoomContainerElem.show();
        },

        hideZoom: function() {
            zoomContainerElem.hide();
        },

    };

    zoomContainerElem = $(ZoomController_HTML);
    $(elementSel).prepend(zoomContainerElem);
    continuousClick($('.panUp', zoomContainerElem), events.up);
    continuousClick($('.panDown', zoomContainerElem), events.down);
    continuousClick($('.panLeft', zoomContainerElem), events.left);
    continuousClick($('.panRight', zoomContainerElem), events.right);
    continuousClick($('.zoomIn', zoomContainerElem), events.zoomIn);
    continuousClick($('.zoomOut', zoomContainerElem), events.zoomOut);
    continuousClick($('.zoomReset', zoomContainerElem), events.zoomReset);
    zoomContainerElem.hide();

    $(document).click(function(e) {
        if (e.which == 2) {
            events.zoomReset();
        }
    });

    return events;
}

function continuousClick(element, func) {
    var timeout;
    element.mousedown(function() {
        func();
        timeout = setInterval(function() {
            func();
        }, 100);

        return false;
    });
    element.mouseup(function() {
        clearInterval(timeout);
        return false;
    });
    element.mouseout(function() {
        clearInterval(timeout);
        return false;
    });
}

function multiline(f) {
    return f.toString().
    replace(/^[^\/]+\/\*!?/, '').
    replace(/\*\/[^\/]+$/, '');
}



ZoomController_HTML = multiline(function() {
    /*!
          <div class="leaflet-control-container">
            <div class="zoomInside">
            <div class="leaflet-top leaflet-left has-leaflet-pan-control">
              <div class="leaflet-control-pan leaflet-control">
                <div class="leaflet-control-pan-up-wrap">
                  <a class="panUp leaflet-control-pan-up" title="Up"></a>
                </div>
                <div class="leaflet-control-pan-left-wrap">
                  <a class="panLeft leaflet-control-pan-left" title="Left"></a>
                </div>
                <div class="leaflet-control-pan-right-wrap">
                  <a class="panRight leaflet-control-pan-right" title="Right"></a>
                </div>
                <div class="leaflet-control-pan-down-wrap">
                  <a class="panDown leaflet-control-pan-down" title="Down"></a>
                </div>

                <div class="leaflet-control-pan-center-wrap">
                  <a class="zoomReset leaflet-control-pan-center" title="Left"></a>
                </div>
              </div>
              <div class="leaflet-control-zoom leaflet-bar leaflet-control">
                <a class="zoomIn leaflet-control-zoom-in" title="Zoom in">+</a>
                <a class="zoomOut leaflet-control-zoom-out" title="Zoom out">-</a>
              </div>
            </div>
          </div>
          </div>
          */
});