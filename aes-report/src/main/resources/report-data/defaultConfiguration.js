DEFAULT_CONFIG = {
    defaultView: 0,
    animationTransitionTime: 700,
    zoomAnimationTime: 500,
    filterMostRelevamtNodes: 400,
    filterMinProbability: 0,
    normalGradiante: [{
        color: "rgba(93,220,74,1)",
        position: 1
    }, {
        color: "rgba(218,196,113,1)",
        position: 50
    }, {
        color: "rgba(211,49,49,1)",
        position: 99
    }],
    scriptsLoad: [],
    scriptsLoadAutoComplete: ["data_ex.js", "data_ex2.js"],
    regexFilter: "",
    defaultTableEntries: 100,
    keyBindings: {
        allwaysActive: [{
                name: "Show Key bindings",
                keyCodes: [72]
            }, {
                name: "Move Left",
                keyCodes: [37]
            }, {
                name: "Move Up",
                keyCodes: [38]
            }, {
                name: "Move Right",
                keyCodes: [39]
            }, {
                name: "Move Down",
                keyCodes: [40]
            }, {
                name: "Undo",
                keyCodes: [66]
            }, {
                name: "Redo",
                keyCodes: [82]
            }, {
                name: "Goto Sunburst",
                keyCodes: [49]
            }, {
                name: "Goto Vertical Partition",
                keyCodes: [50]
            }, {
                name: "Goto Table",
                keyCodes: [51]
            }, {
                name: "Goto Configurations",
                keyCodes: [52]
            }, {
                name: "Goto starting mode",
                keyCodes: [27]
            }, {
                name: "Zoom Mode",
                keyCodes: [90]
            }, {
                name: "Filtering Mode",
                keyCodes: [70]
            }

        ],
        modes: [{
                name: "Zoom",
                keyBindings: [{
                    name: "Zoom In",
                    keyCodes: [73]
                }, {
                    name: "Zoom Reset",
                    keyCodes: [82]
                }, {
                    name: "Zoom Out",
                    keyCodes: [79]
                }, ]
            }, {
                name: "Filtering",
                keyBindings: [{
                    name: "Most Relevant",
                    keyCodes: [77]
                }, {
                    name: "Probability",
                    keyCodes: [80]
                }, {
                    name: "Regular Expression",
                    keyCodes: [82]
                }]
            }

        ]
    }
};

var CONFIG_VERSION = 'configv40';