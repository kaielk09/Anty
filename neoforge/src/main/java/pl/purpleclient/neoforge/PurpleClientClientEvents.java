package pl.purpleclient.neoforge;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import pl.purpleclient.client.PurpleClientScreen;

@EventBusSubscriber(modid = PurpleClientNeoForge.MOD_ID, value = Dist.CLIENT)
public final class PurpleClientClientEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (PurpleClientKeybinds.OPEN_MENU.consumeClick()) {
            Minecraft.getInstance().setScreen(new PurpleClientScreen());
        }
    }
}
