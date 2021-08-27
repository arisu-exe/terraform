package io.github.fallOut015.terraform;

import io.github.fallOut015.terraform.client.gui.components.ToolButton;
import io.github.fallOut015.terraform.tool.Tool;
import io.github.fallOut015.terraform.tool.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.MoverType;
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

    static boolean editing;

    public static Tool[] tools;
    public static Button[] buttons;

    static {
        editing = false;

        tools = new Tool[] {
            Tools.PLACER, Tools.ROTATOR, Tools.MOVER
        };
        buttons = new Button[] {
            new ToolButton(15, 15, Tools.PLACER),
            new ToolButton(15 + 30, 15, Tools.SELECTOR),
            new ToolButton(15, 15 + 30, Tools.MOVER),
            new ToolButton(15 + 30, 15 + 30, Tools.ROTATOR),
            new ToolButton(15, 15 + 60, Tools.PAINTBRUSH)
        };
    }

    public static void setTool(int mouseButton, Tool tool) {
        MainTerraform.tools[mouseButton] = tool;
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
        }
        @SubscribeEvent
        public static void onRawMouse(final InputEvent.RawMouseEvent event) {
            if(MainTerraform.editing && Minecraft.getInstance().screen == null) {
                event.setCanceled(true);

                boolean pressed = false;
                int i = (int) (Minecraft.getInstance().mouseHandler.xpos() * (double) Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double) Minecraft.getInstance().getWindow().getScreenWidth());
                int j = (int) (Minecraft.getInstance().mouseHandler.ypos() * (double) Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double) Minecraft.getInstance().getWindow().getScreenHeight());

                for(Button button : MainTerraform.buttons) {
                    if(button.isHovered()) {
                        ((ToolButton) button).setMouseButton(event.getButton());
                        button.onClick(i, j);
                        pressed = true;
                        break;
                    }
                }
                if(!pressed) {
                    if(event.getAction() == GLFW.GLFW_PRESS) {
                        MainTerraform.tools[event.getButton()].onPress();
                    } else if(event.getAction() == GLFW.GLFW_RELEASE) {
                        MainTerraform.tools[event.getButton()].onRelease();
                    }
                }
            }
        }
        @SubscribeEvent
        public static void onMouseScroll(final InputEvent.MouseScrollEvent event) {
            if(MainTerraform.editing && Minecraft.getInstance().screen == null) {
                event.setCanceled(true);
                Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().move(MoverType.SELF, Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().getViewVector(1.0f).scale(2 * event.getScrollDelta()));
            }
        }

        @SubscribeEvent
        public static void onRenderGameOverlay(final RenderGameOverlayEvent event) {
            if (MainTerraform.editing) {
                if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER) {
                    event.setCanceled(true);
                } else if (MainTerraform.editing && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                    for(Button button : MainTerraform.buttons) {
                        int i = (int) (Minecraft.getInstance().mouseHandler.xpos() * (double) Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double) Minecraft.getInstance().getWindow().getScreenWidth());
                        int j = (int) (Minecraft.getInstance().mouseHandler.ypos() * (double) Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double) Minecraft.getInstance().getWindow().getScreenHeight());

                        button.render(event.getMatrixStack(), i, j, event.getPartialTicks());
                    }
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
                for(Tool tool : MainTerraform.tools) {
                    tool.onRenderUpdate(event);
                }
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