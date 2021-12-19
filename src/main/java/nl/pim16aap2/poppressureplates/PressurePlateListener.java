package nl.pim16aap2.poppressureplates;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Piston;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class PressurePlateListener implements Listener
{
    private final Set<Material> pressurePlates;
    private final PopPressurePlates popPressurePlates;

    PressurePlateListener(PopPressurePlates popPressurePlates)
    {
        this.popPressurePlates = popPressurePlates;
        pressurePlates = Collections.unmodifiableSet(findPressurePlates());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onRedstoneEvent(BlockRedstoneEvent event)
    {
        // We only want to do anything when the pressure plate resets to 0 after a non-zero activation.
        if (event.getOldCurrent() == 0 || event.getNewCurrent() != 0)
            return;

        final Block block = event.getBlock();
        if (!pressurePlates.contains(block.getType()))
            return;

        final Location underPressurePlate = block.getLocation().add(0, -1, 0);
        final BlockData blockBelow = underPressurePlate.getBlock().getBlockData();
        if (!(blockBelow instanceof Piston piston) || piston.getFacing() != BlockFace.DOWN)
            return;

        // Break the block after a delay of 1 tick. Breaking it immediately will cause a pressure plate
        // to be dropped while not breaking the actual block. Perhaps because it is mid-update?
        Bukkit.getScheduler().scheduleSyncDelayedTask(popPressurePlates, block::breakNaturally, 1);
    }

    private static Set<Material> findPressurePlates()
    {
        final Set<Material> ret = new HashSet<>();
        for (final Material material : Material.values())
            if (material.name().contains("PRESSURE_PLATE"))
                ret.add(material);
        return ret;
    }
}
