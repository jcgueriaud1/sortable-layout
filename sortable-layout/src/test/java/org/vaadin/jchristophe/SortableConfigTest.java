package org.vaadin.jchristophe;

import elemental.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

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
        assertThat(sortableConfig.getDelay()).isZero();
        assertThat(sortableConfig.isDelayOnTouchOnly()).isFalse();
        assertThat(sortableConfig.getTouchStartThreshold()).isZero();
        assertThat(sortableConfig.isDisabled()).isFalse();
        assertThat(sortableConfig.getAnimation()).isZero();
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

        ObjectNode json = sortableConfig.toJson();
        assertThat(json.get("filter").asString()).isEqualTo(".filter1 .filter2");

        sortableConfig.clearFilter();
        json = sortableConfig.toJson();
        assertThat(json.get("filter")).isNull();
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

        ObjectNode json = sortableConfig.toJson();

        assertThat(json.get("animation").asInt()).isEqualTo(150);
        assertThat(json.get("sort").asBoolean()).isTrue();
        assertThat(json.get("delay").asInt()).isEqualTo(50);
        assertThat(json.get("delayOnTouchOnly").asBoolean()).isTrue();
        assertThat(json.get("touchStartThreshold").asInt()).isEqualTo(10);
        assertThat(json.get("multiDrag").asBoolean()).isTrue();
        assertThat(json.get("ghostClass").asString()).isEqualTo("ghost-class");
        assertThat(json.get("chosenClass").asString()).isEqualTo("chosen-class");
        assertThat(json.get("dragClass").asString()).isEqualTo("drag-class");
        assertThat(json.get("selectedClass").asString()).isEqualTo("selected-class");
        assertThat(json.get("filter").asString()).isEqualTo(".filter-class");
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
        ObjectNode json = sortableConfig.toJson();
        ObjectNode group = json.withObject("group");
        assertThat(group.get("pull").asBoolean()).isFalse();
    }

    @Test
    void testDragOutGroupName() {
        sortableConfig.addDragOutGroupName("group1");
        ObjectNode json = sortableConfig.toJson();
        ObjectNode group = json.withObject("group");
        ArrayNode pull = group.withArray("pull");
        assertThat(pull.size()).isEqualTo(1);
        assertThat(pull.get(0).asString()).isEqualTo("group1");
    }

    @Test
    void testDragIn() {
        sortableConfig.allowDragIn(true);
        ObjectNode json = sortableConfig.toJson();
        ObjectNode group = json.withObject("group");
        assertThat(group.get("put").asBoolean()).isTrue();
    }

    @Test
    void testDragInGroupName() {
        sortableConfig.addDragInGroupName("group1");
        ObjectNode json = sortableConfig.toJson();
        ObjectNode group = json.withObject("group");
        ArrayNode put = group.withArray("put");
        assertThat(put.size()).isEqualTo(1);
        assertThat(put.get(0).asString()).isEqualTo("group1");
    }
}
