package tech.klux.kluxcore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import tech.klux.kluxcore.manager.EventManager;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class KluxCore implements ModInitializer, ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogManager().getLogger("Welcome to Klux-Core Event System");
    public static EventManager eventManager;

    @Override
    public void onInitialize() {
        eventManager = new EventManager();
    }

    @Override
    public void onInitializeClient() {
        try {
            eventManager.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
