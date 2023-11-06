package com.opalsmile.fnc.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.opalsmile.fnc.platform.FnCServices;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class FnCClient {

    public static final KeyMapping ANTLER_KEYBIND = new KeyMapping("key.featuresandcreatures.antler_headdress", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.gameplay");
    private static final ResourceLocation JUMP_BAR_BACKGROUND_SPRITE = new ResourceLocation("hud/jump_bar_background");
    private static final ResourceLocation JUMP_BAR_COOLDOWN_SPRITE = new ResourceLocation("hud/jump_bar_cooldown");
    private static final ResourceLocation JUMP_BAR_PROGRESS_SPRITE = new ResourceLocation("hud/jump_bar_progress");

    public static float dashScale = 0;
    private static boolean sameLevel = false;
    private static BlockPos jockeyPosition;

    private static boolean keybindPressed;

    public static void handleClientTick() {
        if (keybindPressed && !ANTLER_KEYBIND.isDown()) {
            keybindPressed = false;
            onKeyPress(InputConstants.RELEASE);
        }
        if (ANTLER_KEYBIND.isDown()) {
            ANTLER_KEYBIND.consumeClick();
            keybindPressed = true;
            onKeyPress(InputConstants.PRESS);
        }
    }

    public static void onKeyPress(int action) {
       boolean release = action == InputConstants.RELEASE;
       if (release) {
           dashScale = 0;
       }
       else {
           if (dashScale < 1 && !Minecraft.getInstance().player.getCooldowns().isOnCooldown(FnCServices.PLATFORM.getAntlerHeaddress().get())) {
               dashScale += 0.02f;
           }
       }
       FnCServices.NETWORK.sendAntlerKeypress(release);
    }

    public static boolean renderAntlerHeaddress(final GuiGraphics graphics, int position, int screenHeight, ItemStack item) {
        int j = (int)(dashScale * 183.0F);
        if (j < 2) return false;
        int k = screenHeight - 29;
        graphics.blitSprite(JUMP_BAR_BACKGROUND_SPRITE, position, k, 182, 5);
        if (Minecraft.getInstance().player.getCooldowns().isOnCooldown(item.getItem())) {
            graphics.blitSprite(JUMP_BAR_COOLDOWN_SPRITE, position, k, 182, 5);
        } else {
            graphics.blitSprite(JUMP_BAR_PROGRESS_SPRITE, 182, 5, 0, 0, position, k, j, 5);
        }
        return true;
    }

    public static boolean isOnJockeyLevel() {
        return sameLevel;
    }

    @Nullable
    public static BlockPos getJockeyPosition() {
        return jockeyPosition;
    }

    public static void setSameJockeyLevel(boolean value) {
        sameLevel = value;
    }

    public static void setSameJockeyLevel(BlockPos value) {
        jockeyPosition = value;
    }



}
