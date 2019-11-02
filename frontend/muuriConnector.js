window.Vaadin.Flow.muuriConnector = {
    initLazy: function (customConfig, c) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }

        c.$connector = {
            //// functions
            setOption : function(optionName, optionValue) {
                this.muuriGrid._settings.dragEnabled = optionValue;
            }
        };

        c.$connector.muuriGrid = new Muuri('.dnd-layout', {
            layoutDuration: 400,
            layoutEasing: 'ease',
            dragEnabled: true,
            dragSortInterval: 0,
            dragReleaseDuration: 400,
            dragReleaseEasing: 'ease'
        })
            .on('dragEnd', function (item, event) {
                console.log("dragReleaseEnd"+item);
            });

        //
        // c.$connector.muuriGrid = new Muuri('.board', {
        //     layoutDuration: 400,
        //     layoutEasing: 'ease',
        //     dragEnabled: true,
        //     dragSortInterval: 0,
        //     dragStartPredicate: {
        //         handle: '.board-column-header'
        //     },
        //     dragReleaseDuration: 400,
        //     dragReleaseEasing: 'ease'
        // })
        //     .on('dragEnd', function (item, event) {
        //         console.log("dragEnd in position"+c.$connector.muuriGrid._items.indexOf(item));
        //     });

        //
        // var currentValue = "";
        //
        // const pushChanges = function() {
        //     c.$server.updateValue(currentValue)
        // }
        //
        // var baseconfig =  JSON.parse(customConfig) || {
        //     height: 500,
        //     plugins: [
        //         'advlist autolink lists link image charmap print preview anchor',
        //         'searchreplace visualblocks code fullscreen',
        //         'insertdatetime media table contextmenu paste code'
        //     ],
        //     toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
        // }
        // baseconfig['selector'] =  "#" + c.firstChild.id;
        // baseconfig['setup'] = function(ed) {
        //     c.$connector.editor = ed;
        //     ed.on('setContent', function(e) {
        //         console.error('Editor content was set');
        //         currentValue = ed.getContent();
        //     });
        //     ed.on('change', function(e) {
        //         console.error('Editor was changed');
        //         currentValue = ed.getContent();
        //     });
        //     ed.on('blur', function(e) {
        //         console.error('Editor was blurred');
        //         currentValue = ed.getContent();
        //         pushChanges();
        //     });
        // };
        // tinymce.init(baseconfig);
    }
}