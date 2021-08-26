package io.github.fallOut015.terraform.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fallOut015.terraform.MainTerraform;
import io.github.fallOut015.terraform.tool.Tool;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;

public class ToolButton extends ImageButton {
    final Tool tool;
    final int u, v;
    boolean depressed;

    public ToolButton(int x, int y, final Tool tool) {
        super(x, y, 20, 20, tool.getU(), tool.getV(), 20, MainTerraform.WIDGETS_TEXTURE, 256, 256, button -> {}, Button.NO_TOOLTIP, tool.getTranslated());
        this.tool = tool;
        this.depressed = false;
        this.u = tool.getU();
        this.v = tool.getV();
    }

    @Override
    public void onPress() {
        MainTerraform.setTool(this.tool);
        this.depressed = true;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MainTerraform.WIDGETS_TEXTURE);
        int v = this.depressed ? this.v + 40 : (this.isHovered() ? this.v + 20 : this.v);

        RenderSystem.enableDepthTest();
        blit(poseStack, this.x, this.y, this.width, this.height, (float) this.u, (float) v, 20, 20, 256, 256);
        if (this.isHovered()) {
            this.renderToolTip(poseStack, mx, my);
        }
    }
}