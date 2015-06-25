function renderKeyCodesArray(keyCodes) {
    var renderStr = '';
    for (var i = 0; i < keyCodes.length; i++) {
        renderStr += KeyboardJS.key.name(keyCodes[i])[0] + ' ';
    };
    return renderStr;
}

function renderKeyBindingsDisplay(configuration, renderTry) {

    function keyBindingsRender(arrayKeyBindings, modeID) {
        var renderStr = '<table>';
        for (var i = 0; i < arrayKeyBindings.length; i++) {
            renderStr += '<small><tr><td>' + arrayKeyBindings[i].name + '</td>';
            renderStr += '<td>'+renderKeyCodesArray(arrayKeyBindings[i].keyCodes)+'</td>';
            renderStr += '<td><a id="mode_' + modeID + '_' + i + '" href="#" class="keyChange">Change</a></td>';
            if (renderTry) {
                renderStr += '<td><a id="modeTry_' + modeID + '_' + i + '" href="#" class="keyTry">Try</a></td>';
            }
            renderStr += '</tr></small>';
        }
        renderStr += '</table>';
        return renderStr;
    }

    function modesRender(modes) {
        var str = '';
        for (var i = 0; i < modes.length; i++) {
            str += '<br /><small>' + modes[i].name + ' Mode</small>';
            str += keyBindingsRender(modes[i].keyBindings, i);
        }
        return str;
    }


    return keyBindingsRender(configuration.currentConfig.keyBindings.allwaysActive, -1) +
        modesRender(configuration.currentConfig.keyBindings.modes);
}


function showKeybindingsPopup(configuration) {
    $("#dialogKeybindings").remove();
    $('body').append('<div id="dialogKeybindings" title="Key Bindings"></div>');
    $("#dialogKeybindings").dialog({
        autoOpen: true,
        modal: false,
        height: "200",
    });
    var conf_view = new ConfigurationView(null, "#dialogKeybindings", configuration, null);
    conf_view.renderKeyBindings(true);
    $('.keyBindingsp').remove();

}