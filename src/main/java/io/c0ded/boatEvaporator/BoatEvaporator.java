package io.c0ded.boatEvaporator;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import io.c0ded.boatEvaporator.event.VehicleMove;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class BoatEvaporator extends JavaPlugin {
    public static BoatEvaporator PLUGIN;
    public static Server SERVER;
    public static StateFlag VEHICLE_FLAG;
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new VehicleMove(), this);
        PLUGIN = this;
        SERVER = this.getServer();
    }
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("allow-vehicle-existence", true);
            registry.register(flag);
            VEHICLE_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("allow-vehicle-existence");
            if (existing instanceof StateFlag) {
                VEHICLE_FLAG = (StateFlag) existing;
            } else {
                this.getLogger().warning("Something has gone catastrophically wrong! Disabling plugin.");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
