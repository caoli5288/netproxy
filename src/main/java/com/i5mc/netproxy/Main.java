package com.i5mc.netproxy;

import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by on 2017/7/9.
 */
public class Main extends JavaPlugin {

    private MXProxySelector p;

    @Override
    public void onLoad() {
        for (val p : getServer().getPluginManager().getPlugins()) {
            p.getDescription().getDepend().add(getName());
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        val sel = Selector.build((List<Map<String, ?>>) getConfig().getList("proxy"));
        p = new MXProxySelector(this, sel);
        p.hook();
    }

    @Override
    public void onDisable() {
        p.close();
    }

    public void log(String line) {
        getLogger().log(Level.INFO, line);
    }

}
