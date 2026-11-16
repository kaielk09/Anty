package pl.purpleclient.forge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = PurpleClientForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PurpleClientKeybinds {
    public static final KeyMapping OPEN_MENU = new KeyMapping(
        "key.purpleclient.open",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_RIGHT_SHIFT,
        "key.categories.purpleclient"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_MENU);
    }
}
