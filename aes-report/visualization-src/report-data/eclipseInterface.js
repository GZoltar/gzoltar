var showTrial = false;
function initializeVisualizationREST(jsonData) {
    $('body').html('<div id="preview"><img src="watermark.png" /></div>');
    if(showTrial == true){
        showTrialWatermark();
    }
    tmp = JSON.parse(jsonData);

    window.data_ex = tmp.response
    init();
}

function initializeVisualization(jsonData) {
    $('body').html('<div id="preview"><img src="watermark.png" /></div>');
    if(showTrial == true){
        showTrialWatermark();
    }
    window.data_ex = JSON.parse(jsonData);
    init();
}


function sendClickEvent(node) {
    if("triggerEvent" in window){
        triggerEvent(JSON.stringify({
            "type": "click",
            "nodeId": node.id
        }));
    }
}


function showTrialWatermark(){
    showTrial = true;
    $('#preview').show();
}