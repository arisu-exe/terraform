package io.github.fallOut015.terraform.tool;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.fallOut015.terraform.client.gui.RenderHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.Map;

public class PaintbrushTool extends Tool {
    boolean active;

    public PaintbrushTool() {
        super(80, 0, "paintbrush", Map.of("diameter", 1, "shape", PaintbrushShape.CIRCLE));
    }

    @Override
    public void onPress() {
        this.active = true;
    }
    @Override
    protected void onUpdate() {
        if(this.active) {
            BlockPos pos = new BlockPos(Minecraft.getInstance().hitResult.getLocation());
            for(double i = -(((double) this.get("diameter")) / 2d); i < (double) this.get("diameter") / 2d; ++ i) {
                for(double j = -(((double) this.get("diameter")) / 2d); j < (double) this.get("diameter") / 2d; ++ j) {
                    if(!Minecraft.getInstance().level.isEmptyBlock(pos.offset(i, 0, j))) {
                        Minecraft.getInstance().level.setBlock(pos.offset(i, 0, j), Blocks.STONE.defaultBlockState(), 0);
                    }
                }
            }
        }
    }
    @Override
    public void onRelease() {
        this.active = false;
    }

    @Override
    protected void renderOutline(RenderWorldLastEvent event) {
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
        //Minecraft.getInstance().hitResult = Minecraft.getInstance().level.clip(new ClipContext(mainCamera.getEntity().getEyePosition(1.0f), v, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, mainCamera.getEntity()));

        Minecraft.getInstance().hitResult = Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().pick(distance, 1.0f, false);

        HitResult hitresult = Minecraft.getInstance().hitResult;
        if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
            BlockState blockstate = Minecraft.getInstance().level.getBlockState(blockpos);
            if (true) {
                if (Minecraft.getInstance().level.getWorldBorder().isWithinBounds(blockpos)) {
                    VertexConsumer vertexconsumer2 = multibuffersource$buffersource.getBuffer(RenderType.lines());
                    RenderHelper.renderHitOutline(poseStack, vertexconsumer2, mainCamera.getEntity(), d0, d1, d2, blockpos, blockstate, 0, 0, 1.0f, 0.4f);
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

    public enum PaintbrushShape {
        CIRCLE, SQUARE
    }
}
