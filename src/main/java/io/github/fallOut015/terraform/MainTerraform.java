package io.github.fallOut015.terraform;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.fallOut015.terraform.client.gui.components.ToolButton;
import io.github.fallOut015.terraform.tool.Tool;
import io.github.fallOut015.terraform.tool.Tools;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

// TODO Clip in edit mode, like in Spectator Mode

@Mod(MainTerraform.MODID)
public class MainTerraform  {
    public static final String MODID = "terraform";
    public static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("terraform", "textures/gui/widgets.png");

    static boolean editing;

    static Tool leftTool;
    static Tool middleTool;
    static Tool rightTool;

    static final Button TOOL_PLACER_BUTTON = new ToolButton(10, 10, Tools.PLACER);
    static final Button TOOL_SELECTOR_BUTTON = new ToolButton(10 + 30, 10, Tools.SELECTOR);
    static final Button TOOL_MOVER_BUTTON = new ToolButton(10, 10 + 30, Tools.MOVER);
    static final Button TOOL_ROTATOR_BUTTON = new ToolButton(10 + 30, 10 + 30, Tools.ROTATOR);

    static {
        editing = false;

        leftTool = Tools.PLACER;
        middleTool = Tools.MOVER;
        rightTool = Tools.ROTATOR;
    }

    public static void setTool(int mouseButton, Tool tool) {
        MainTerraform.leftTool = tool;
    }

    public MainTerraform() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }
    private void enqueueIMC(final InterModEnqueueEvent event) {
    }
    private void processIMC(final InterModProcessEvent event) {
    }

    public static void renderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, Entity mainCamera, double cameraX, double cameraY, double cameraZ, BlockPos blockPos, BlockState blockState) {
        renderShape(poseStack, vertexConsumer, Shapes.block(), (double)blockPos.getX() - cameraX, (double)blockPos.getY() - cameraY, (double)blockPos.getZ() - cameraZ, 1.0F, 1.0F, 0.0F, 0.4F);
    }
    public static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, float p_109789_, float p_109790_, float p_109791_, float p_109792_) {
        PoseStack.Pose posestack$pose = poseStack.last();
        voxelShape.forAllEdges((p_172987_, p_172988_, p_172989_, p_172990_, p_172991_, p_172992_) -> {
            float f = (float)(p_172990_ - p_172987_);
            float f1 = (float)(p_172991_ - p_172988_);
            float f2 = (float)(p_172992_ - p_172989_);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f = f / f3;
            f1 = f1 / f3;
            f2 = f2 / f3;
            vertexConsumer.vertex(posestack$pose.pose(), (float)(p_172987_ + x), (float)(p_172988_ + y), (float)(p_172989_ + z)).color(p_109789_, p_109790_, p_109791_, p_109792_).normal(posestack$pose.normal(), f, f1, f2).endVertex();
            vertexConsumer.vertex(posestack$pose.pose(), (float)(p_172990_ + x), (float)(p_172991_ + y), (float)(p_172992_ + z)).color(p_109789_, p_109790_, p_109791_, p_109792_).normal(posestack$pose.normal(), f, f1, f2).endVertex();
        });
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber
    public static class Events {
        @SubscribeEvent
        public static void onKeyInput(final InputEvent.KeyInputEvent event) {
            if(/*TODO not if on title*/!Minecraft.getInstance().isPaused() && Minecraft.getInstance().screen == null && Minecraft.getInstance().player.isCreative()) {
                if(event.getKey() == Minecraft.getInstance().options.keyInventory.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS) {
                    if(MainTerraform.editing) {
                        // TODO prevent from opening inventory. Instead, open block catalogue
                    }
                } else if (event.getKey() == GLFW.GLFW_KEY_V && event.getAction() == GLFW.GLFW_PRESS) { // TODO Make key mapping
                    MainTerraform.editing = !MainTerraform.editing;
                }
                if (MainTerraform.editing) {
                    Minecraft.getInstance().mouseHandler.releaseMouse();
                } else {
                    Minecraft.getInstance().mouseHandler.grabMouse();
                }
            }

            // TODO Move player to bird's eye view
            // TODO Unlock mouse
            // TODO Highlight blocks differently
        }
        @SubscribeEvent
        public static void onRawMouse(final InputEvent.RawMouseEvent event) {
            if(MainTerraform.editing && Minecraft.getInstance().screen == null) {
                event.setCanceled(true);

                if(MainTerraform.TOOL_MOVER_BUTTON.isHovered()) {
                    ((ToolButton) MainTerraform.TOOL_MOVER_BUTTON).setMouseButton(event.getButton());
                    MainTerraform.TOOL_MOVER_BUTTON.onClick(Minecraft.getInstance().mouseHandler.xpos(), Minecraft.getInstance().mouseHandler.ypos());
                } else if(MainTerraform.TOOL_ROTATOR_BUTTON.isHovered()) {
                    ((ToolButton) MainTerraform.TOOL_ROTATOR_BUTTON).setMouseButton(event.getButton());
                    MainTerraform.TOOL_ROTATOR_BUTTON.onClick(Minecraft.getInstance().mouseHandler.xpos(), Minecraft.getInstance().mouseHandler.ypos());
                } else if(MainTerraform.TOOL_PLACER_BUTTON.isHovered()) {
                    ((ToolButton) MainTerraform.TOOL_PLACER_BUTTON).setMouseButton(event.getButton());
                    MainTerraform.TOOL_PLACER_BUTTON.onClick(Minecraft.getInstance().mouseHandler.xpos(), Minecraft.getInstance().mouseHandler.ypos());
                } else if(MainTerraform.TOOL_SELECTOR_BUTTON.isHovered()) {
                    ((ToolButton) MainTerraform.TOOL_SELECTOR_BUTTON).setMouseButton(event.getButton());
                    MainTerraform.TOOL_SELECTOR_BUTTON.onClick(Minecraft.getInstance().mouseHandler.xpos(), Minecraft.getInstance().mouseHandler.ypos());
                } else {
                    if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_1) {
                        if(event.getAction() == GLFW.GLFW_PRESS) {
                            MainTerraform.leftTool.onPress();
                        } else if(event.getAction() == GLFW.GLFW_RELEASE) {
                            MainTerraform.leftTool.onRelease();
                        }
                    } else if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_2) {
                        if(event.getAction() == GLFW.GLFW_PRESS) {
                            MainTerraform.rightTool.onPress();
                        } else if(event.getAction() == GLFW.GLFW_RELEASE) {
                            MainTerraform.rightTool.onRelease();
                        }
                    } else if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_3) {
                        if(event.getAction() == GLFW.GLFW_PRESS) {
                            MainTerraform.middleTool.onPress();
                        } else if(event.getAction() == GLFW.GLFW_RELEASE) {
                            MainTerraform.middleTool.onRelease();
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onRenderGameOverlay(final RenderGameOverlayEvent event) {
            if (MainTerraform.editing) {
                if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER) {
                    event.setCanceled(true);
                } else if (MainTerraform.editing && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                    MainTerraform.TOOL_PLACER_BUTTON.render(event.getMatrixStack(), (int) Minecraft.getInstance().mouseHandler.xpos(), (int) Minecraft.getInstance().mouseHandler.ypos(), event.getPartialTicks());
                    MainTerraform.TOOL_SELECTOR_BUTTON.render(event.getMatrixStack(), (int) Minecraft.getInstance().mouseHandler.xpos(), (int) Minecraft.getInstance().mouseHandler.ypos(), event.getPartialTicks());
                    MainTerraform.TOOL_MOVER_BUTTON.render(event.getMatrixStack(), (int) Minecraft.getInstance().mouseHandler.xpos(), (int) Minecraft.getInstance().mouseHandler.ypos(), event.getPartialTicks());
                    MainTerraform.TOOL_ROTATOR_BUTTON.render(event.getMatrixStack(), (int) Minecraft.getInstance().mouseHandler.xpos(), (int) Minecraft.getInstance().mouseHandler.ypos(), event.getPartialTicks());
                }
            }
        }
        @SubscribeEvent
        public static void onRenderHand(final RenderHandEvent event) {
            if (MainTerraform.editing) {
                event.setCanceled(true);
            }
        }
        @SubscribeEvent
        public static void onRenderWorldLast(final RenderWorldLastEvent event) {
            if(MainTerraform.editing) {
                PoseStack poseStack = event.getMatrixStack();
                poseStack.pushPose();
                Camera mainCamera = Minecraft.getInstance().gameRenderer.getMainCamera();

                MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
                Vec3 vec3 = mainCamera.getPosition();
                double d0 = vec3.x();
                double d1 = vec3.y();
                double d2 = vec3.z();

                double x = Minecraft.getInstance().mouseHandler.xpos() - Minecraft.getInstance().getWindow().getWidth() / 2.0;
                double y = Minecraft.getInstance().mouseHandler.ypos() - Minecraft.getInstance().getWindow().getHeight() / 2.0;

                //Minecraft.getInstance().hitResult = mainCamera.getEntity().pick((double) Minecraft.getInstance().options.renderDistance * 22.627417d, 1.0f, false);

                // TODO Get block at pointer pos

                Vec3 angle = mainCamera.getEntity().getViewVector(1.0f).add(new Vec3(0, y / -16d, 0));
                double distance = Minecraft.getInstance().options.renderDistance * 22.627417d;

                Vec3 v = mainCamera.getEntity().getEyePosition(1.0f).add(angle.x * distance, angle.y * distance, angle.z * distance);
                Minecraft.getInstance().hitResult = Minecraft.getInstance().level.clip(new ClipContext(mainCamera.getEntity().getEyePosition(1.0f), v, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, mainCamera.getEntity()));

                HitResult hitresult = Minecraft.getInstance().hitResult;
                if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
                    Direction face = ((BlockHitResult) hitresult).getDirection();
                    BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos().relative(face);
                    BlockState blockstate = Minecraft.getInstance().level.getBlockState(blockpos);
                    if (true) {
                        if (Minecraft.getInstance().level.getWorldBorder().isWithinBounds(blockpos)) {
                            VertexConsumer vertexconsumer2 = multibuffersource$buffersource.getBuffer(RenderType.lines());
                            MainTerraform.renderHitOutline(poseStack, vertexconsumer2, mainCamera.getEntity(), d0, d1, d2, blockpos, blockstate);
                        }
                    }
                }

                poseStack.popPose();

                PoseStack posestack = RenderSystem.getModelViewStack();
                posestack.pushPose();
                posestack.mulPoseMatrix(poseStack.last().pose());
                RenderSystem.applyModelViewMatrix();
                Minecraft.getInstance().debugRenderer.render(poseStack, multibuffersource$buffersource, d0, d1, d2);
                posestack.popPose();
                RenderSystem.applyModelViewMatrix();
                multibuffersource$buffersource.endBatch(Sheets.translucentCullBlockSheet());
            }
        }
        @SubscribeEvent
        public static void onDrawSelection(final DrawSelectionEvent event) {
            if(MainTerraform.editing) {
                event.setCanceled(true);
            }
        }
    }
}