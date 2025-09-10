package org.vaadin.jchristophe;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SortableGroupConfigurationTest {

    private SortableGroupConfiguration sortableGroupConfiguration;

    @BeforeEach
    void setUp() {
        sortableGroupConfiguration = new SortableGroupConfiguration();
    }

    @Test
    void testDefaultValues() {
        assertThat(sortableGroupConfiguration.getName()).isNull();
        assertThat(sortableGroupConfiguration.isDragOutAllowed()).isTrue();
        assertThat(sortableGroupConfiguration.isClone()).isFalse();
        assertThat(sortableGroupConfiguration.isDragInAllowed()).isFalse();
    }

    @Test
    void testSetters() {
        sortableGroupConfiguration.setName("test-group");
        assertThat(sortableGroupConfiguration.getName()).isEqualTo("test-group");

        sortableGroupConfiguration.setDragOutAllowed(false);
        assertThat(sortableGroupConfiguration.isDragOutAllowed()).isFalse();

        sortableGroupConfiguration.setClone(true);
        assertThat(sortableGroupConfiguration.isClone()).isTrue();

        sortableGroupConfiguration.setDragInAllowed(true);
        assertThat(sortableGroupConfiguration.isDragInAllowed()).isTrue();
    }

    @Test
    void testToJson() {
        sortableGroupConfiguration.setName("test-group");
        sortableGroupConfiguration.setDragOutAllowed(true);
        sortableGroupConfiguration.setClone(false);
        sortableGroupConfiguration.setDragInAllowed(true);

        JsonObject json = sortableGroupConfiguration.toJson();

        assertThat(json.getString("name")).isEqualTo("test-group");
        assertThat(json.getBoolean("pull")).isTrue();
        assertThat(json.getBoolean("put")).isTrue();
    }

    @Test
    void testToJsonWithClone() {
        sortableGroupConfiguration.setClone(true);

        JsonObject json = sortableGroupConfiguration.toJson();

        assertThat(json.getString("pull")).isEqualTo("clone");
    }

    @Test
    void testToJsonWithDragInGroupNames() {
        sortableGroupConfiguration.addDragInGroupName("group1");
        sortableGroupConfiguration.addDragInGroupName("group2");

        JsonObject json = sortableGroupConfiguration.toJson();

        JsonArray putArray = json.getArray("put");
        assertThat(putArray.length()).isEqualTo(2);
        assertThat(putArray.get(0).asString()).isEqualTo("group1");
        assertThat(putArray.get(1).asString()).isEqualTo("group2");
    }

    @Test
    void testToJsonWithDragOutGroupNames() {
        sortableGroupConfiguration.addDragOutGroupName("group1");
        sortableGroupConfiguration.addDragOutGroupName("group2");

        JsonObject json = sortableGroupConfiguration.toJson();

        JsonArray pullArray = json.getArray("pull");
        assertThat(pullArray.length()).isEqualTo(2);
        assertThat(pullArray.get(0).asString()).isEqualTo("group1");
        assertThat(pullArray.get(1).asString()).isEqualTo("group2");
    }
}
