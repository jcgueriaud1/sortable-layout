package org.vaadin.jchristophe;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

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

        ObjectNode json = sortableGroupConfiguration.toJson();

        assertThat(json.get("name").asString()).isEqualTo("test-group");
        assertThat(json.get("pull").asBoolean()).isTrue();
        assertThat(json.get("put").asBoolean()).isTrue();
    }

    @Test
    void testToJsonWithClone() {
        sortableGroupConfiguration.setClone(true);

        ObjectNode json = sortableGroupConfiguration.toJson();

        assertThat(json.get("pull").asString()).isEqualTo("clone");
    }

    @Test
    void testToJsonWithDragInGroupNames() {
        sortableGroupConfiguration.addDragInGroupName("group1");
        sortableGroupConfiguration.addDragInGroupName("group2");

        ObjectNode json = sortableGroupConfiguration.toJson();

        ArrayNode putArray = json.withArray("put");
        assertThat(putArray.size()).isEqualTo(2);
        assertThat(putArray.get(0).asString()).isEqualTo("group1");
        assertThat(putArray.get(1).asString()).isEqualTo("group2");
    }

    @Test
    void testToJsonWithDragOutGroupNames() {
        sortableGroupConfiguration.addDragOutGroupName("group1");
        sortableGroupConfiguration.addDragOutGroupName("group2");

        ObjectNode json = sortableGroupConfiguration.toJson();

        ArrayNode pullArray = json.withArray("pull");
        assertThat(pullArray.size()).isEqualTo(2);
        assertThat(pullArray.get(0).asString()).isEqualTo("group1");
        assertThat(pullArray.get(1).asString()).isEqualTo("group2");
    }
}
