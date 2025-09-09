package org.vaadin.jchristophe;

import elemental.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SortableConfigTest {

    private SortableConfig sortableConfig;

    @BeforeEach
    void setUp() {
        sortableConfig = new SortableConfig();
    }

    @Test
    void testDefaultValues() {
        assertThat(sortableConfig.isSort()).isTrue();
        assertThat(sortableConfig.getDelay()).isEqualTo(0);
        assertThat(sortableConfig.isDelayOnTouchOnly()).isFalse();
        assertThat(sortableConfig.getTouchStartThreshold()).isEqualTo(0);
        assertThat(sortableConfig.isDisabled()).isFalse();
        assertThat(sortableConfig.getAnimation()).isEqualTo(0);
        assertThat(sortableConfig.getGhostClass()).isNull();
        assertThat(sortableConfig.getChosenClass()).isNull();
        assertThat(sortableConfig.getDragClass()).isNull();
        assertThat(sortableConfig.isMultiDrag()).isFalse();
        assertThat(sortableConfig.getSelectedClass()).isNull();
    }

    @Test
    void testSetters() {
        sortableConfig.setSort(false);
        assertThat(sortableConfig.isSort()).isFalse();

        sortableConfig.setDelay(100);
        assertThat(sortableConfig.getDelay()).isEqualTo(100);

        sortableConfig.setDelayOnTouchOnly(true);
        assertThat(sortableConfig.isDelayOnTouchOnly()).isTrue();

        sortableConfig.setTouchStartThreshold(5);
        assertThat(sortableConfig.getTouchStartThreshold()).isEqualTo(5);

        sortableConfig.setDisabled(true);
        assertThat(sortableConfig.isDisabled()).isTrue();

        sortableConfig.setAnimation(200);
        assertThat(sortableConfig.getAnimation()).isEqualTo(200);

        sortableConfig.setGhostClass("ghost");
        assertThat(sortableConfig.getGhostClass()).isEqualTo("ghost");

        sortableConfig.setChosenClass("chosen");
        assertThat(sortableConfig.getChosenClass()).isEqualTo("chosen");

        sortableConfig.setDragClass("drag");
        assertThat(sortableConfig.getDragClass()).isEqualTo("drag");

        sortableConfig.setMultiDrag(true);
        assertThat(sortableConfig.isMultiDrag()).isTrue();

        sortableConfig.setSelectedClass("selected");
        assertThat(sortableConfig.getSelectedClass()).isEqualTo("selected");
    }

    @Test
    void testFilter() {
        sortableConfig.addFilter("filter1");
        sortableConfig.addFilter("filter2");

        JsonObject json = sortableConfig.toJson();
        assertThat(json.getString("filter")).isEqualTo(".filter1 .filter2");

        sortableConfig.clearFilter();
        json = sortableConfig.toJson();
        assertThat(json.hasKey("filter")).isFalse();
    }

    @Test
    void testToJson() {
        sortableConfig.setAnimation(150);
        sortableConfig.setSort(true);
        sortableConfig.setDelay(50);
        sortableConfig.setDelayOnTouchOnly(true);
        sortableConfig.setTouchStartThreshold(10);
        sortableConfig.setMultiDrag(true);
        sortableConfig.setGhostClass("ghost-class");
        sortableConfig.setChosenClass("chosen-class");
        sortableConfig.setDragClass("drag-class");
        sortableConfig.setSelectedClass("selected-class");
        sortableConfig.addFilter("filter-class");

        JsonObject json = sortableConfig.toJson();

        assertThat(json.getNumber("animation")).isEqualTo(150);
        assertThat(json.getBoolean("sort")).isTrue();
        assertThat(json.getNumber("delay")).isEqualTo(50);
        assertThat(json.getBoolean("delayOnTouchOnly")).isTrue();
        assertThat(json.getNumber("touchStartThreshold")).isEqualTo(10);
        assertThat(json.getBoolean("multiDrag")).isTrue();
        assertThat(json.getString("ghostClass")).isEqualTo("ghost-class");
        assertThat(json.getString("chosenClass")).isEqualTo("chosen-class");
        assertThat(json.getString("dragClass")).isEqualTo("drag-class");
        assertThat(json.getString("selectedClass")).isEqualTo("selected-class");
        assertThat(json.getString("filter")).isEqualTo(".filter-class");
    }

    @Test
    void requireGroupStore() {
        assertThat(sortableConfig.requireGroupStore()).isFalse();
        sortableConfig.setGroupName("my-group");
        assertThat(sortableConfig.requireGroupStore()).isTrue();
    }

    @Test
    void requireCloneFunction() {
        assertThat(sortableConfig.requireCloneFunction()).isFalse();
        sortableConfig.cloneOnDragOut(true);
        assertThat(sortableConfig.requireCloneFunction()).isTrue();
    }

    @Test
    void testDragOut() {
        sortableConfig.allowDragOut(false);
        JsonObject json = sortableConfig.toJson();
        JsonObject group = json.getObject("group");
        assertThat(group.getBoolean("pull")).isFalse();
    }

    @Test
    void testDragOutGroupName() {
        sortableConfig.addDragOutGroupName("group1");
        JsonObject json = sortableConfig.toJson();
        JsonObject group = json.getObject("group");
        elemental.json.JsonArray pull = group.getArray("pull");
        assertThat(pull.length()).isEqualTo(1);
        assertThat(pull.getString(0)).isEqualTo("group1");
    }

    @Test
    void testDragIn() {
        sortableConfig.allowDragIn(true);
        JsonObject json = sortableConfig.toJson();
        JsonObject group = json.getObject("group");
        assertThat(group.getBoolean("put")).isTrue();
    }

    @Test
    void testDragInGroupName() {
        sortableConfig.addDragInGroupName("group1");
        JsonObject json = sortableConfig.toJson();
        JsonObject group = json.getObject("group");
        elemental.json.JsonArray put = group.getArray("put");
        assertThat(put.length()).isEqualTo(1);
        assertThat(put.getString(0)).isEqualTo("group1");
    }
}
