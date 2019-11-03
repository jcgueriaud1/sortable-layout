package org.vaadin.jchristophe;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonObject;

public class SortableConfig implements JsonSerializable {

    private String group;
    private boolean sort = true;
    private int delay = 0;
    private boolean delayOnTouchOnly;
    private int touchStartThreshold = 0;
    private boolean disabled = false;
    //store
    private int animation = 0;


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isDelayOnTouchOnly() {
        return delayOnTouchOnly;
    }

    public void setDelayOnTouchOnly(boolean delayOnTouchOnly) {
        this.delayOnTouchOnly = delayOnTouchOnly;
    }

    public int getTouchStartThreshold() {
        return touchStartThreshold;
    }

    public void setTouchStartThreshold(int touchStartThreshold) {
        this.touchStartThreshold = touchStartThreshold;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getAnimation() {
        return animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
    }

    @Override
    public JsonObject toJson() {

        JsonObject obj = Json.createObject();
        obj.put("animation", getAnimation());
        return obj;
    }

    @Deprecated
    @Override
    public JsonSerializable readJson(JsonObject value) {

        return null;
    }
}
