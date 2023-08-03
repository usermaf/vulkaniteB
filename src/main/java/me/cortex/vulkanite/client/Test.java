package me.cortex.vulkanite.client;

import me.cortex.vulkanite.acceleration.SharedQuadVkIndexBuffer;
import me.cortex.vulkanite.lib.base.initalizer.VInitializer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPhysicalDeviceAccelerationStructureFeaturesKHR;
import org.lwjgl.vulkan.VkPhysicalDeviceBufferDeviceAddressFeaturesKHR;
import org.lwjgl.vulkan.VkPhysicalDeviceRayQueryFeaturesKHR;
import org.lwjgl.vulkan.VkPhysicalDeviceRayTracingPipelineFeaturesKHR;

import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11C.GL_RGBA8;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.util.vma.Vma.VMA_ALLOCATION_CREATE_HOST_ACCESS_SEQUENTIAL_WRITE_BIT;
import static org.lwjgl.vulkan.EXTDebugUtils.VK_EXT_DEBUG_UTILS_EXTENSION_NAME;
import static org.lwjgl.vulkan.EXTDescriptorIndexing.VK_EXT_DESCRIPTOR_INDEXING_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRAccelerationStructure.VK_ACCELERATION_STRUCTURE_TYPE_BOTTOM_LEVEL_KHR;
import static org.lwjgl.vulkan.KHRAccelerationStructure.VK_KHR_ACCELERATION_STRUCTURE_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRBufferDeviceAddress.VK_KHR_BUFFER_DEVICE_ADDRESS_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRDeferredHostOperations.VK_KHR_DEFERRED_HOST_OPERATIONS_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRExternalFenceCapabilities.VK_KHR_EXTERNAL_FENCE_CAPABILITIES_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRExternalFenceWin32.VK_KHR_EXTERNAL_FENCE_WIN32_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRExternalMemory.VK_KHR_EXTERNAL_MEMORY_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRExternalMemoryCapabilities.VK_KHR_EXTERNAL_MEMORY_CAPABILITIES_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRExternalMemoryWin32.VK_KHR_EXTERNAL_MEMORY_WIN32_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRExternalSemaphore.VK_KHR_EXTERNAL_SEMAPHORE_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRExternalSemaphoreCapabilities.VK_KHR_EXTERNAL_SEMAPHORE_CAPABILITIES_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRExternalSemaphoreWin32.VK_KHR_EXTERNAL_SEMAPHORE_WIN32_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRGetMemoryRequirements2.VK_KHR_GET_MEMORY_REQUIREMENTS_2_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRGetPhysicalDeviceProperties2.VK_KHR_GET_PHYSICAL_DEVICE_PROPERTIES_2_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRRayQuery.VK_KHR_RAY_QUERY_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRRayTracingPipeline.VK_KHR_RAY_TRACING_PIPELINE_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRShaderDrawParameters.VK_KHR_SHADER_DRAW_PARAMETERS_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRSpirv14.VK_KHR_SPIRV_1_4_EXTENSION_NAME;
import static org.lwjgl.vulkan.VK10.*;

public class Test {
    public static void main(String[] args) {
        initGL();


        var init = new VInitializer("Vulkan test", "Vulkanite", 1, 3,
                new String[]{VK_KHR_GET_PHYSICAL_DEVICE_PROPERTIES_2_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_MEMORY_CAPABILITIES_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_SEMAPHORE_CAPABILITIES_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_FENCE_CAPABILITIES_EXTENSION_NAME,
                        VK_EXT_DEBUG_UTILS_EXTENSION_NAME},
                new String[] {
                        "VK_LAYER_KHRONOS_validation",
                });

        //This copies whatever gpu the opengl context is on
        init.findPhysicalDevice();//glGetString(GL_RENDERER).split("/")[0]

        init.createDevice(List.of(
                        VK_KHR_EXTERNAL_MEMORY_WIN32_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_SEMAPHORE_WIN32_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_FENCE_WIN32_EXTENSION_NAME,
                        VK_KHR_GET_MEMORY_REQUIREMENTS_2_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_MEMORY_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_SEMAPHORE_EXTENSION_NAME,
                        VK_EXT_DESCRIPTOR_INDEXING_EXTENSION_NAME,
                        VK_KHR_SPIRV_1_4_EXTENSION_NAME,
                        VK_KHR_SHADER_DRAW_PARAMETERS_EXTENSION_NAME,
                        VK_KHR_BUFFER_DEVICE_ADDRESS_EXTENSION_NAME,

                        VK_KHR_RAY_QUERY_EXTENSION_NAME,

                        VK_KHR_RAY_TRACING_PIPELINE_EXTENSION_NAME,
                        VK_KHR_ACCELERATION_STRUCTURE_EXTENSION_NAME,

                        VK_KHR_DEFERRED_HOST_OPERATIONS_EXTENSION_NAME),
                List.of(),
                new float[]{1.0f, 0.9f},
                features -> features.shaderInt64(true).multiDrawIndirect(true), List.of(
                        stack-> VkPhysicalDeviceBufferDeviceAddressFeaturesKHR.calloc(stack)
                                .sType$Default()
                                .bufferDeviceAddress(true),

                        stack-> VkPhysicalDeviceAccelerationStructureFeaturesKHR.calloc(stack)
                                .sType$Default()
                                .accelerationStructure(true),

                        stack-> VkPhysicalDeviceRayQueryFeaturesKHR.calloc(stack)
                                .sType$Default()
                                .rayQuery(true),

                        stack-> VkPhysicalDeviceRayTracingPipelineFeaturesKHR.calloc(stack)
                                .sType$Default()
                                .rayTracingPipeline(true)
                                .rayTracingPipelineTraceRaysIndirect(true)
                ));

        var context = init.createContext();

        context.memory.createSharedBuffer(1000, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
        context.memory.createSharedBuffer(1000, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
        context.memory.createSharedBuffer(1000, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
        context.memory.createSharedBuffer(1000, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
        context.memory.createSharedImage(128,128, 1, VK_FORMAT_R8G8B8A8_UNORM, GL_RGBA8, VK_IMAGE_USAGE_TRANSFER_DST_BIT | VK_IMAGE_USAGE_SAMPLED_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
        context.memory.createSharedImage(128,128, 1, VK_FORMAT_R8G8B8A8_UNORM, GL_RGBA8, VK_IMAGE_USAGE_TRANSFER_DST_BIT | VK_IMAGE_USAGE_SAMPLED_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
        context.memory.createSharedImage(128,128, 1, VK_FORMAT_R8G8B8A8_UNORM, GL_RGBA8, VK_IMAGE_USAGE_TRANSFER_DST_BIT | VK_IMAGE_USAGE_SAMPLED_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
        context.memory.createAcceleration(100*256,256, 0, VK_ACCELERATION_STRUCTURE_TYPE_BOTTOM_LEVEL_KHR).free();
        context.cmd.singleTimeCommand();

        var mem = context.memory.createBufferGlobal(1024, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, 0, VMA_ALLOCATION_CREATE_HOST_ACCESS_SEQUENTIAL_WRITE_BIT);
        mem.map();
        mem.unmap();
        mem.flush();

        SharedQuadVkIndexBuffer.getIndexBuffer(context, 10000);
    }


    private static long window;
    private static void initGL() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(3840, 2160, "Ugd shader test", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop

        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(window);


        GL.createCapabilities();
    }
}

/*
//For igpus

        init.createDevice(List.of(
                        VK_KHR_EXTERNAL_MEMORY_WIN32_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_SEMAPHORE_WIN32_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_FENCE_WIN32_EXTENSION_NAME,
                        VK_KHR_GET_MEMORY_REQUIREMENTS_2_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_MEMORY_EXTENSION_NAME,
                        VK_KHR_EXTERNAL_SEMAPHORE_EXTENSION_NAME,
                        VK_EXT_DESCRIPTOR_INDEXING_EXTENSION_NAME,
                        VK_KHR_SPIRV_1_4_EXTENSION_NAME,
                        VK_KHR_SHADER_DRAW_PARAMETERS_EXTENSION_NAME),
                List.of(),
                new float[]{1.0f},
                features -> features.multiDrawIndirect(true), List.of(
                ));
 */