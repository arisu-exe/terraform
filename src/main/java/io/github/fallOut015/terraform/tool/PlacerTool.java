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
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.Map;

public class PlacerTool extends Tool {
    boolean active;

    public PlacerTool() {
        super(0, 0, "placer", Map.of());
    }

    @Override
    public void onPress() {
        this.active = true;
    }
    @Override
    protected void calculatePointer() {
        double x = Minecraft.getInstance().mouseHandler.xpos() - Minecraft.getInstance().getWindow().getWidth() / 2.0;
        double y = Minecraft.getInstance().mouseHandler.ypos() - Minecraft.getInstance().getWindow().getHeight() / 2.0;
        Camera mainCamera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 angle = mainCamera.getEntity().getViewVector(1.0f).add(new Vec3(0, y / -16d, 0));
        double distance = Minecraft.getInstance().options.renderDistance * 22.627417d;
        Vec3 v = mainCamera.getEntity().getEyePosition(1.0f).add(angle.x * distance, angle.y * distance, angle.z * distance);
        Minecraft.getInstance().hitResult = Minecraft.getInstance().level.clip(new ClipContext(mainCamera.getEntity().getEyePosition(1.0f), v, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, mainCamera.getEntity()));
    }
    @Override
    protected void postUpdate() {
        if(this.active) {
            Minecraft.getInstance().level.setBlock(new BlockPos(Minecraft.getInstance().hitResult.getLocation()), Blocks.STONE.defaultBlockState(), 0);
        }
    }
    @Override
    public void onRelease() {
        this.active = false;
    }

    @Override
    protected void renderOutline(final RenderWorldLastEvent event) {
        PoseStack poseStack = event.getMatrixStack();
        poseStack.pushPose();
        Camera mainCamera = Minecraft.getInstance().gameRenderer.getMainCamera();

        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 vec3 = mainCamera.getPosition();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();

        // TODO Get block at pointer pos

        HitResult hitresult = Minecraft.getInstance().hitResult;
        if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
            Direction face = ((BlockHitResult) hitresult).getDirection();
            BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos().relative(face);
            BlockState blockstate = Minecraft.getInstance().level.getBlockState(blockpos);
            if (Minecraft.getInstance().level.getWorldBorder().isWithinBounds(blockpos)) {
                VertexConsumer vertexconsumer2 = multibuffersource$buffersource.getBuffer(RenderType.lines());
                RenderHelper.renderHitOutline(poseStack, vertexconsumer2, mainCamera.getEntity(), d0, d1, d2, blockpos, blockstate, 1.0f, 1.0f, 0.0f, 0.4f);
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

        posestack.pushPose();
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Items.STONE), ItemTransforms.TransformType.GUI, 16, 16, poseStack, multibuffersource$buffersource, 0xFFFFFF);
        posestack.popPose();
        //Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.STONE.defaultBlockState(), posestack, multibuffersource$buffersource, 32, 32, );
    }
}