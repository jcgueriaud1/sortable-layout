package org.vaadin.jchristophe;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SortableConfig implements JsonSerializable {

    private final SortableGroupConfiguration group = new SortableGroupConfiguration();
    private boolean sort = true;
    private int delay = 0;
    private boolean delayOnTouchOnly;
    private int touchStartThreshold = 0;
    private boolean disabled = false;
    //store
    private int animation = 0;
    private final List<String> filterClassNames = new ArrayList<>();

    private String ghostClass; // Class name for the drop placeholder
    private String chosenClass; // Class name for the chosen item
    private String dragClass; // Class name for the dragging item

    private boolean multiDrag = false;

    private String selectedClass;

    public boolean isSort() {
        return sort;
    }

    /**
     * Enable sorting in this list, default true
     *
     * @param sort true to enable reordering
     */
    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public int getDelay() {
        return delay;
    }

    /**
     * time in milliseconds to define when the sorting should start
     * Default 0
     * @param delay time in milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isDelayOnTouchOnly() {
        return delayOnTouchOnly;
    }

    /**
     * only delay if user is using touch
     * default false
     * @param delayOnTouchOnly true to delay on touch
     */
    public void setDelayOnTouchOnly(boolean delayOnTouchOnly) {
        this.delayOnTouchOnly = delayOnTouchOnly;
    }

    public int getTouchStartThreshold() {
        return touchStartThreshold;
    }

    /**
     * px, how many pixels the point should move before cancelling a delayed drag event
     * @param touchStartThreshold value in px
     */
    public void setTouchStartThreshold(int touchStartThreshold) {
        this.touchStartThreshold = touchStartThreshold;
    }

    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Disables the sortable if set to true.
     * default false
     * @param disabled true to disable the sort
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getAnimation() {
        return animation;
    }

    /**
     * animation speed moving items when sorting, `0` — without animation
     * default 150
     * @param animation time of the animation in ms
     */
    public void setAnimation(int animation) {
        this.animation = animation;
    }

    public void addFilter(String filter) {
        filterClassNames.add(filter);
    }
    public void clearFilter() {
        filterClassNames.clear();
    }

    /**
     * set the group name
     *
     * @param name group name
     */
    public void setGroupName(String name) {
        group.setName(name);
    }

    /**
     * whether elements can be added from other lists.
     * - true to be able to move in the list
     * - false to allow only reorder
     * @see #addDragInGroupName(String) to restrict the group from which elements can be added.
     *
     * @param allowed Defaults to true.
     */
    public void allowDragIn(boolean allowed) {
        group.setDragInAllowed(allowed);
    }

    /**
     * Ability to move from the list.
     * - true to be able to move the items from the list
     * - false to allow only reorder
     * @see #addDragOutGroupName(String) to restrict the group which the elements may be put in
     *
     * See 'pull' in SortableJS
     *
     * @param allowed  Defaults to true.
     */
    public void allowDragOut(boolean allowed) {
        group.setDragOutAllowed(allowed);
    }

    /**
     * restrict the group from which elements can be added.
     *
     * @param name group name
     */
    public void addDragInGroupName(String name) {
        group.addDragInGroupName(name);
    }

    /**
     * restrict the group which the elements may be put in
     *
     * @param name group name
     */
    public void addDragOutGroupName(String name) {
        group.addDragOutGroupName(name);
    }


    /**
     * Whether the previous list keeps a copy of the element
     * Warning: Can't be used with {@link #addDragOutGroupName(String)}
     * @param clone true to clone the item
     */
    public void cloneOnDragOut(boolean clone) {
        group.setClone(clone);
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

    public boolean isMultiDrag() {
        return multiDrag;
    }

    public void setMultiDrag(boolean multiDrag) {
        this.multiDrag = multiDrag;
    }

    /**
     * Set the Class name for the chosen item
     * default sortable-chosen
     * @param chosenClass Class name for the chosen item
     */
    public void setChosenClass(String chosenClass) {
        this.chosenClass = chosenClass;
    }

    public String getDragClass() {
        return dragClass;
    }


    /**
     * Set Class name for the dragging item
     * default sortable-drag
     * @param dragClass Class name for the dragging item
     */
    public void setDragClass(String dragClass) {
        this.dragClass = dragClass;
    }

    public String getSelectedClass() {
        return selectedClass;
    }

    /**
     *
     * @param selectedClass
     */
    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    @Override
    public JsonObject toJson() {

        JsonObject obj = Json.createObject();
        obj.put("animation", getAnimation());
        obj.put("sort", isSort());
        obj.put("delay", getDelay());
        obj.put("delayOnTouchOnly", isDelayOnTouchOnly());
        obj.put("touchStartThreshold", getTouchStartThreshold());
        obj.put("multiDrag", isMultiDrag());
        if (getGhostClass() != null) {
            obj.put("ghostClass", getGhostClass());
        }
        if (getChosenClass() != null) {
            obj.put("chosenClass", getChosenClass());
        }
        if (getDragClass() != null) {
            obj.put("dragClass", getDragClass());
        }
        if (getSelectedClass() != null) {
            obj.put("selectedClass", getSelectedClass());
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

    boolean requireGroupStore() {
        return group.getName() != null;
    }

    boolean requireCloneFunction() {
        return group.isClone();
    }
}
