package nl.pim16aap2.poppressureplates;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class PopPressurePlates extends JavaPlugin
{
    private final PressurePlateListener pressurePlateListener;

    public PopPressurePlates()
    {
        pressurePlateListener = new PressurePlateListener(this);
    }

    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(pressurePlateListener, this);
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(pressurePlateListener);
    }
}
