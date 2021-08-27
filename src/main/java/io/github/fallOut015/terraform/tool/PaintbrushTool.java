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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.Map;
import java.util.function.BiConsumer;

public class PaintbrushTool extends Tool {
    boolean active;

    public PaintbrushTool() {
        super(80, 0, "paintbrush", Map.of("diameter", 10, "shape", PaintbrushShape.SQUARE));
    }

    @Override
    public void onPress() {
        this.active = true;
    }
    @Override
    protected void calculatePointer() {
        double distance = Minecraft.getInstance().options.renderDistance * 22.627417d;
        Minecraft.getInstance().hitResult = Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().pick(distance, 1.0f, false);
    }
    @Override
    protected void postUpdate() {
        if(this.active) {
            BlockPos pos = new BlockPos(Minecraft.getInstance().hitResult.getLocation()).below();
            ((PaintbrushShape) this.get("shape")).accept(pos, (Integer) this.get("diameter"));
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

    public enum PaintbrushShape implements BiConsumer<BlockPos, Integer> {
        CIRCLE((pos, diameter) -> {
            // TODO
        }),
        SQUARE((pos, diameter) -> {
            for(double i = -(((double) ((int) diameter)) / 2d); i < (((double) ((int) diameter)) / 2d); ++ i) {
                for(double j = -(((double) ((int) diameter)) / 2d); j < (((double) ((int) diameter)) / 2d); ++ j) {
                    if(!Minecraft.getInstance().level.isEmptyBlock(pos.offset(i, 0, j))) {
                        Minecraft.getInstance().level.setBlock(pos.offset(i, 0, j), Blocks.STONE.defaultBlockState(), 0);
                    }
                }
            }
        });

        final BiConsumer<BlockPos, Integer> placeMethod;

        PaintbrushShape(BiConsumer<BlockPos, Integer> placeMethod) {
            this.placeMethod = placeMethod;
        }

        @Override
        public void accept(BlockPos blockPos, Integer integer) {
            this.placeMethod.accept(blockPos, integer);
        }
    }
}