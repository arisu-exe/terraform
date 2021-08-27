package io.github.fallOut015.terraform.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fallOut015.terraform.MainTerraform;
import io.github.fallOut015.terraform.client.gui.RenderHelper;
import io.github.fallOut015.terraform.tool.Tool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fmlclient.gui.GuiUtils;

import java.util.Arrays;
import java.util.List;

public class ToolButton extends ImageButton {
    private static final Component[] MOUSE_BUTTONS = {
        new TranslatableComponent("gui.left_mouse_button"),
        new TranslatableComponent("gui.right_mouse_button"),
        new TranslatableComponent("gui.middle_mouse_button")
    };

    final Tool tool;
    final int u, v;
    int mouseButton;

    public ToolButton(int x, int y, final Tool tool) {
        super(x, y, 30, 30, tool.getU(), tool.getV(), 20, RenderHelper.WIDGETS_TEXTURE, 256, 256, button -> {}, Button.NO_TOOLTIP, tool.getTranslated());
        this.tool = tool;
        this.u = tool.getU();
        this.v = tool.getV();
    }

    @Override
    public void onPress() {
        MainTerraform.setTool(this.mouseButton, this.tool);
    }
    @Override
    public void renderButton(PoseStack poseStack, int mx, int my, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, RenderHelper.WIDGETS_TEXTURE);

        boolean depressed = Arrays.stream(MainTerraform.tools).anyMatch(tool -> tool.equals(this.tool));
        int v = depressed ? this.v + 40 : (this.isHovered() ? this.v + 20 : this.v);

        RenderSystem.enableDepthTest();
        blit(poseStack, this.x, this.y, this.width, this.height, (float) this.u, (float) v, 20, 20, 256, 256);

        if(depressed) {
            Gui.drawString(poseStack, Minecraft.getInstance().font, MOUSE_BUTTONS[this.getMouseButton()], this.x + 3, this.y + 20, 0xFFFFFF);
        }

        if (this.isHovered()) {
            GuiUtils.drawHoveringText(poseStack, List.of(this.tool.getTranslated()), mx, my, Minecraft.getInstance().getWindow().getScreenWidth(), Minecraft.getInstance().getWindow().getScreenHeight(), -1, Minecraft.getInstance().font);
        }
    }

    public void setMouseButton(int mouseButton) {
        this.mouseButton = mouseButton;
    }
    public int getMouseButton() {
        for(int i = 0; i < MainTerraform.tools.length; ++ i) {
            if(MainTerraform.tools[i].equals(this.tool)) {
                return i;
            }
        }
        return -1;
    }
}