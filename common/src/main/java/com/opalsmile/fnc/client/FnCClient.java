package com.opalsmile.fnc.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.FnCServices;
import com.opalsmile.fnc.registries.FnCItems;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
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
               //Forge and Fabric call this from separate places, so this method is not called at the same rate in both platforms.
               dashScale += FnCServices.PLATFORM.getHeaddressSpeed();
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

    public static void setJockeyPosition(BlockPos value) {
        jockeyPosition = value;
    }

    public static void registerItemProperties() {
        ItemProperties.register(
                FnCItems.DOWSING_ROD.get(), FnCConstants.resourceLocation("dowsing"), (itemStack, clientLevel, livingEntity, seed) -> {

                    if (!(livingEntity instanceof Player player)) return 0.0f;

                    //if selected and second hand is empty.
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND) != itemStack || !player.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty()) {return 0.0f;}

                    if (!FnCClient.isOnJockeyLevel()) {
                        return 0.0f;
                    }
                    else {
                        BlockPos pos = livingEntity.blockPosition();
                        BlockPos jockeyPos = FnCClient.getJockeyPosition();
                        int x = jockeyPos.getX() - pos.getX();
                        int z = jockeyPos.getZ() - pos.getZ();
                        Vec3 viewVector = player.getViewVector(1.0f).scale(100);
                        double dot = x * viewVector.x + z * viewVector.z;
                        double modView = Math.sqrt(viewVector.x * viewVector.x +  viewVector.z * viewVector.z);
                        double modDistance = Math.sqrt(x * x + z * z);
                        double angle = Math.acos(dot / (modView * modDistance)); // 0.0 < angle < PI
                        if (angle < 0.14) {
                            return 0.2f;
                        } else if(angle < 1.179) {
                            return 0.1f;
                        }
                        return 0.0f;
                    }
                });
    }

}
