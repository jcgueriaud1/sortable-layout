import { Sortable, MultiDrag } from 'sortablejs';

Sortable.mount(new MultiDrag());

window.Vaadin.Flow.sortableConnector = {
    initLazy: function (customConfig, c, layout) {
        // Check whether the connector was already initialized
        if (c.$connector) {
            return;
        }

        c.$connector = {
            //// functions
            setOption : function(optionName, optionValue) {
                this.sortable.options[optionName] = optionValue;
            },

            clearClone : function() {
                this.clonedElements.forEach(clone => {
                    if (clone.parentNode) {
                        clone.parentNode.removeChild(clone);
                    }
                });
                this.clonedElements = [];
            }
        };

        c.$connector.sortable = Sortable.create(layout, customConfig);

        c.$connector.clonedElements = [];

        c.$connector.sortable.options.onEnd = function (/**Event*/evt) {
            let oldIndexes = [evt.oldIndex];
            let newIndexes = [evt.newIndex];
            if (evt.newIndicies && evt.oldIndicies && evt.newIndicies.length > 0) {
                newIndexes = evt.newIndicies.map(newIndex => newIndex.index);
                oldIndexes = evt.oldIndicies.map(oldIndex => oldIndex.index);
            }
            if (evt.to === evt.from) {
                c.$server.onReorderListener(oldIndexes, newIndexes);
                //evt.oldDraggableIndex; // element's old index within old parent, only counting draggable elements
                //evt.newDraggableIndex; // element's new index within new parent, only counting draggable elements

                //c.$server.onReorderListenerElement(evt.oldDraggableIndex, evt.newDraggableIndex);
            } else {
                const clone = (evt.pullMode === 'clone');
                evt.from.parentElement.$server.onRemoveListener(oldIndexes, clone);
                evt.to.parentElement.$server.onAddListener(newIndexes, clone);
                if (clone) {
                    // store the clone(s) generated by the client-side
                    // and delete later (to avoid the element to disappear/reappear)
                    if (evt.clone) {
                        c.$connector.clonedElements = [evt.clone];
                    } else {
                        c.$connector.clonedElements = [...evt.clones];
                    }
                }

            }
        }


        c.$connector.sortable.options.onChoose = function (/**Event*/evt) {
            c.dispatchEvent(new CustomEvent('on-choose'));
        }

        c.$connector.sortable.options.onUnchoose = function (/**Event*/evt) {
            c.dispatchEvent(new CustomEvent('on-unchoose'));
        }
        c.$connector.sortable.options.onChange = function (/**Event*/evt) {
            c.dispatchEvent(new CustomEvent('on-change'));
        }
    }
}