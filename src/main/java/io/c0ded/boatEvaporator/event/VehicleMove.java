package io.c0ded.boatEvaporator.event;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.association.Associables;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import io.c0ded.boatEvaporator.BoatEvaporator;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;

public class VehicleMove implements Listener {

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) {
            return;
        } // only check every block move to reduce checks
        Vehicle vehicle = event.getVehicle();
        RegionAssociable associable = Associables.constant(Association.NON_MEMBER); // boats are usually never members
        RegionContainer container = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer();
        RegionQuery query = container.createQuery();
        if (!query.testState(BukkitAdapter.adapt(to),
                associable,
                BoatEvaporator.VEHICLE_FLAG))
        {
            // this is the fun part
            boolean isLiving = vehicle instanceof LivingEntity;
            if (isLiving) return;
            if (vehicle.getScoreboardTags().contains("ignoreProtection")) return;
            Material vehicleItem = switch (vehicle.getType()) {
                case ACACIA_BOAT -> Material.ACACIA_BOAT;
                case ACACIA_CHEST_BOAT -> Material.ACACIA_CHEST_BOAT;
                case BAMBOO_CHEST_RAFT -> Material.BAMBOO_CHEST_RAFT;
                case BAMBOO_RAFT -> Material.BAMBOO_RAFT;
                case BIRCH_BOAT -> Material.BIRCH_BOAT;
                case BIRCH_CHEST_BOAT -> Material.BIRCH_CHEST_BOAT;
                case CHERRY_BOAT -> Material.CHERRY_BOAT;
                case CHERRY_CHEST_BOAT -> Material.CHERRY_CHEST_BOAT;
                case CHEST_MINECART -> Material.CHEST_MINECART;
                case COMMAND_BLOCK_MINECART, SPAWNER_MINECART -> Material.MINECART; // unobtainable items
                case DARK_OAK_BOAT -> Material.DARK_OAK_BOAT;
                case DARK_OAK_CHEST_BOAT -> Material.DARK_OAK_CHEST_BOAT;
                case FURNACE_MINECART -> Material.FURNACE_MINECART;
                case HOPPER_MINECART -> Material.HOPPER_MINECART;
                case JUNGLE_BOAT -> Material.JUNGLE_BOAT;
                case JUNGLE_CHEST_BOAT -> Material.JUNGLE_CHEST_BOAT;
                case MANGROVE_BOAT -> Material.MANGROVE_BOAT;
                case MANGROVE_CHEST_BOAT -> Material.MANGROVE_CHEST_BOAT;
                case MINECART -> Material.MINECART;
                case OAK_BOAT -> Material.OAK_BOAT;
                case OAK_CHEST_BOAT -> Material.OAK_CHEST_BOAT;
                case PALE_OAK_BOAT -> Material.PALE_OAK_BOAT;
                case PALE_OAK_CHEST_BOAT -> Material.PALE_OAK_CHEST_BOAT;
                case SPRUCE_BOAT -> Material.SPRUCE_BOAT;
                case SPRUCE_CHEST_BOAT -> Material.SPRUCE_CHEST_BOAT;
                case TNT_MINECART -> Material.TNT_MINECART;
                default -> Material.AIR;
            };
            vehicle.eject();
            vehicle.remove();
            to.getWorld().dropItem(to, ItemStack.of(vehicleItem));
            // fun
            to.getWorld().playSound(to, Sound.ENTITY_ZOMBIE_INFECT, 0.25f, 1f);
            to.getWorld().spawnParticle(Particle.SMOKE, to, 25);
        }
    }
}