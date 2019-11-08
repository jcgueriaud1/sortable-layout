package org.vaadin.jchristophe;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SortableConfig implements JsonSerializable {

    private SortableGroup group = new SortableGroup();
    private boolean sort = true;
    private int delay = 0;
    private boolean delayOnTouchOnly;
    private int touchStartThreshold = 0;
    private boolean disabled = false;
    //store
    private int animation = 0;
    private List<String> filterClassNames = new ArrayList<>();

    private String ghostClass; // Class name for the drop placeholder
    private String chosenClass; // Class name for the chosen item
    private String dragClass; // Class name for the dragging item

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

    public void setGroupName(String name) {
        group.setName(name);
    }

    public void addFilter(String filter) {
        filterClassNames.add(filter);
    }
    public void clearFilter() {
        filterClassNames.clear();
    }

    public void allowDragIn(boolean allowed) {
        group.setDragInAllowed(allowed);
    }

    //pull
    public void allowDragOut(boolean allowed) {
        group.setDragOutAllowed(allowed);
    }

    public void addDragInGroupName(String name) {
        group.addDragInGroupName(name);
    }

    public void addDragOutGroupName(String name) {
        group.addDragOutGroupName(name);
    }

    public String getGhostClass() {
        return ghostClass;
    }

    /**
     * default sortable-ghost
     * @param ghostClass
     */
    public void setGhostClass(String ghostClass) {
        this.ghostClass = ghostClass;
    }

    public String getChosenClass() {
        return chosenClass;
    }

    /**
     * default sortable-chosen
     * @param chosenClass
     */
    public void setChosenClass(String chosenClass) {
        this.chosenClass = chosenClass;
    }

    public String getDragClass() {
        return dragClass;
    }


    /**
     * default sortable-drag
     * @param dragClass
     */
    public void setDragClass(String dragClass) {
        this.dragClass = dragClass;
    }

    @Override
    public JsonObject toJson() {

        JsonObject obj = Json.createObject();
        obj.put("animation", getAnimation());
        obj.put("sort", isSort());
        obj.put("delay", getDelay());
        obj.put("delayOnTouchOnly", isDelayOnTouchOnly());
        obj.put("touchStartThreshold", getTouchStartThreshold());
        if (getGhostClass() != null) {
            obj.put("ghostClass", getGhostClass());
        }
        if (getChosenClass() != null) {
            obj.put("chosenClass", getChosenClass());
        }
        if (getDragClass() != null) {
            obj.put("dragClass", getDragClass());
        }
        if (!filterClassNames.isEmpty()) {
            obj.put("filter", filterClassNames.stream().map(
                    filterClassName -> "."+filterClassName)
                    .collect(Collectors.joining(" "))
            );
        }
        obj.put("group", group.toJson());
        return obj;
    }

    @Deprecated
    @Override
    public JsonSerializable readJson(JsonObject value) {

        return null;
    }

    private class SortableGroup  implements JsonSerializable {

        private String name;
        // put
        private List<String> groupNameDragInAllowed = new ArrayList<>();
        // pull
        private List<String> groupNameDragOutAllowed = new ArrayList<>();
        // pull
        private boolean dragOutAllowed = true;
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

            if (groupNameDragOutAllowed.isEmpty()) {
                obj.put("pull", dragOutAllowed);
            } else {
                JsonArray array = Json.createArray();
                for (int i = 0; i < groupNameDragOutAllowed.size(); i++) {
                    array.set(i,groupNameDragOutAllowed.get(i));
                }
                obj.put("pull", array);
            }
            return obj;
        }

        @Override
        public JsonSerializable readJson(JsonObject value) {
            return null;
        }
    }
}
