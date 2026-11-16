package pl.purpleclient.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pl.purpleclient.client.PurpleClientScreen;
import pl.purpleclient.client.PurpleClientSettingsScreen;

@Mod.EventBusSubscriber(modid = PurpleClientForge.MOD_ID, value = Dist.CLIENT)
public final class PurpleClientClientEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (PurpleClientKeybinds.OPEN_MENU.consumeClick()) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.screen instanceof PurpleClientScreen
                    || minecraft.screen instanceof PurpleClientSettingsScreen) {
                    minecraft.setScreen(null);
                } else if (minecraft.player != null) {
                    minecraft.setScreen(new PurpleClientScreen());
                }
            }
        }
    }
}