import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import Sortable from 'sortablejs';

class SortablejsWrapper extends PolymerElement {
    static get template() {
        return html`<slot></slot>`;
    }

    static get is() {
        return 'sortable-js'
    }

    static get properties() {
        return {
        }
    }

    constructor() {
        super();
        this.sortables = [];
    }

    connectedCallback() {
        super.ready();
        this.initialize();
    }

    disconnectedCallback() {
        this.destroy();
    }

    initialize() {
        const slot = this.shadowRoot.querySelector('slot');
        for (const node of slot.assignedNodes()) {
            console.log(node);
            this.sortables.push(Sortable.create(node));
        }
    }

    destroy() {
        for (const sortable of this.sortables) {
            this.sortable.destroy();
        }
    }
}

customElements.define(SortablejsWrapper.is, SortablejsWrapper);
export {SortablejsWrapper};