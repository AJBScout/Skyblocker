package me.xmrvizzy.skyblocker;

import me.xmrvizzy.skyblocker.chat.ChatMessageListener;
import me.xmrvizzy.skyblocker.config.SkyblockerConfig;
import me.xmrvizzy.skyblocker.discord.DiscordRPCManager;
import me.xmrvizzy.skyblocker.gui.ContainerSolverManager;
import me.xmrvizzy.skyblocker.skyblock.BackpackPreview;
import me.xmrvizzy.skyblocker.skyblock.FishingHelper;
import me.xmrvizzy.skyblocker.skyblock.HotbarSlotLock;
import me.xmrvizzy.skyblocker.skyblock.StatusBarTracker;
import me.xmrvizzy.skyblocker.skyblock.api.RepositoryUpdate;
import me.xmrvizzy.skyblocker.skyblock.api.StatsCommand;
import me.xmrvizzy.skyblocker.skyblock.dungeon.DungeonBlaze;
import me.xmrvizzy.skyblocker.skyblock.dungeon.DungeonMap;
import me.xmrvizzy.skyblocker.skyblock.dungeon.LividColor;
import me.xmrvizzy.skyblocker.skyblock.dwarven.DwarvenHud;
import me.xmrvizzy.skyblocker.skyblock.item.PriceInfoTooltip;
import me.xmrvizzy.skyblocker.skyblock.item.WikiLookup;
import me.xmrvizzy.skyblocker.skyblock.itemlist.ItemRegistry;
import me.xmrvizzy.skyblocker.skyblock.quicknav.QuickNav;
import me.xmrvizzy.skyblocker.skyblock.tabhud.TabHud;
import me.xmrvizzy.skyblocker.skyblock.tabhud.util.PlayerListMgr;
import me.xmrvizzy.skyblocker.utils.MessageScheduler;
import me.xmrvizzy.skyblocker.utils.Scheduler;
import me.xmrvizzy.skyblocker.utils.UpdateChecker;
import me.xmrvizzy.skyblocker.utils.Utils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Main class for Skyblocker which initializes features, registers events, and
 * manages ticks. This class will be instantiated by Fabric. Do not instantiate
 * this class.
 */
public class SkyblockerMod implements ClientModInitializer {
    public static final String NAMESPACE = "skyblocker";
    private static SkyblockerMod INSTANCE;

    @SuppressWarnings("deprecation")
    public final Scheduler scheduler = new Scheduler();
    public final MessageScheduler messageScheduler = new MessageScheduler();
    public final ContainerSolverManager containerSolverManager = new ContainerSolverManager();
    public final StatusBarTracker statusBarTracker = new StatusBarTracker();

    /**
     * Do not instantiate this class. Use {@link #getInstance()} instead.
     */
    @Deprecated
    public SkyblockerMod() {
        INSTANCE = this;
    }

    public static SkyblockerMod getInstance() {
        return INSTANCE;
    }

    /**
     * Register {@link #tick(MinecraftClient)} to
     * {@link ClientTickEvents#END_CLIENT_TICK}, initialize all features, and
     * schedule tick events.
     */
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        HotbarSlotLock.init();
        SkyblockerConfig.init();
        PriceInfoTooltip.init();
        WikiLookup.init();
        ItemRegistry.init();
        RepositoryUpdate.init();
        BackpackPreview.init();
        QuickNav.init();
        StatsCommand.init();
        DwarvenHud.init();
        ChatMessageListener.init();
        UpdateChecker.init();
        DiscordRPCManager.init();
        LividColor.init();
        FishingHelper.init();
        TabHud.init();
        containerSolverManager.init();
        DungeonMap.init();
        scheduler.scheduleCyclic(Utils::sbChecker, 20);
        scheduler.scheduleCyclic(DiscordRPCManager::update, 100);
        scheduler.scheduleCyclic(DungeonBlaze::update, 4);
        scheduler.scheduleCyclic(LividColor::update, 10);
        scheduler.scheduleCyclic(BackpackPreview::tick, 50);
        scheduler.scheduleCyclic(DwarvenHud::update, 40);
        scheduler.scheduleCyclic(PlayerListMgr::updateList, 20);
    }

    /**
     * Ticks the scheduler. Called once at the end of every client tick through
     * {@link ClientTickEvents#END_CLIENT_TICK}.
     *
     * @param client the Minecraft client.
     */
    public void tick(MinecraftClient client) {
        scheduler.tick();
        messageScheduler.tick();
    }
}
