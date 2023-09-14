package me.cortex.vulkanite.lib.shader.reflection;

import static org.lwjgl.util.spvc.Spvc.*;

public enum ResourceType {
    UNIFORM_BUFFER(SPVC_RESOURCE_TYPE_UNIFORM_BUFFER),
    STORAGE_BUFFER(SPVC_RESOURCE_TYPE_STORAGE_BUFFER),
    STAGE_INPUT(SPVC_RESOURCE_TYPE_STAGE_INPUT),
    STAGE_OUTPUT(SPVC_RESOURCE_TYPE_STAGE_OUTPUT),
    SUBPASS_INPUT(SPVC_RESOURCE_TYPE_SUBPASS_INPUT),
    STORAGE_IMAGE(SPVC_RESOURCE_TYPE_STORAGE_IMAGE),
    SAMPLED_IMAGE(SPVC_RESOURCE_TYPE_SAMPLED_IMAGE),
    ATOMIC_COUNTER(SPVC_RESOURCE_TYPE_ATOMIC_COUNTER),
    PUSH_CONSTANT(SPVC_RESOURCE_TYPE_PUSH_CONSTANT),
    SEPARATE_IMAGE(SPVC_RESOURCE_TYPE_SEPARATE_IMAGE),
    SEPARATE_SAMPLERS(SPVC_RESOURCE_TYPE_SEPARATE_SAMPLERS),
    ACCELERATION_STRUCTURE(SPVC_RESOURCE_TYPE_ACCELERATION_STRUCTURE);
    public final int id;

    ResourceType(int id) {
        this.id = id;
    }
}