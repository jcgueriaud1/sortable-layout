package org.vaadin.jchristophe;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jcgueriaud
 */
public class SortableGroupConfiguration implements JsonSerializable {

    private String name;
    // put
    private List<String> groupNameDragInAllowed = new ArrayList<>();
    // pull
    private List<String> groupNameDragOutAllowed = new ArrayList<>();
    // pull
    private boolean dragOutAllowed = true;
    // pull
    private boolean clone = false;
    // put
    private boolean dragInAllowed = false;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDragOutAllowed() {
        return dragOutAllowed;
    }

    public void setDragOutAllowed(boolean dragOutAllowed) {
        this.dragOutAllowed = dragOutAllowed;
    }

    public boolean isDragInAllowed() {
        return dragInAllowed;
    }

    public void setDragInAllowed(boolean dragInAllowed) {
        this.dragInAllowed = dragInAllowed;
    }

    public void addDragInGroupName(String name) {
        groupNameDragInAllowed.add(name);
    }

    public void addDragOutGroupName(String name) {
        groupNameDragOutAllowed.add(name);
    }

    public boolean isClone() {
        return clone;
    }

    public void setClone(boolean clone) {
        this.clone = clone;
    }
    @Override
    public JsonObject toJson() {
        JsonObject obj = Json.createObject();
        if (getName() != null) {
            obj.put("name", getName());
        }
        if (groupNameDragInAllowed.isEmpty()) {
            obj.put("put", dragInAllowed);
        } else {
            JsonArray array = Json.createArray();
            for (int i = 0; i < groupNameDragInAllowed.size(); i++) {
                array.set(i, groupNameDragInAllowed.get(i));
            }
            obj.put("put", array);
        }

        if (isClone()) {
            obj.put("pull", "clone");
        } else {
            if (groupNameDragOutAllowed.isEmpty()) {
                obj.put("pull", dragOutAllowed);
            } else {
                JsonArray array = Json.createArray();
                for (int i = 0; i < groupNameDragOutAllowed.size(); i++) {
                    array.set(i, groupNameDragOutAllowed.get(i));
                }
                obj.put("pull", array);
            }
        }
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonObject value) {
        return null;
    }
}
