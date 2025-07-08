package org.vaadin.jchristophe;

public interface HasTestView {


    String getUrl();

    default String getView() {
        return "";
    }
}