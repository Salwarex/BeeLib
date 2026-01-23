package ru.waxera.beeLib.utils.gui.hotbar;

import org.bukkit.entity.Player;
import ru.waxera.beeLib.utils.data.pools.map.ReplaceableMapPool;

/**
 * An implementation of a ReplaceableMapPool designed to store data
 * about currently open {@link HotbarInterface HotbarInterface's}.
 *
 * @see ReplaceableMapPool
 * @see ru.waxera.beeLib.utils.data.pools.map.MapPool MapPool
 * @see ru.waxera.beeLib.utils.data.pools.Pool Pool Interface
 * @version 2
 * @since v1.1.1
 * @author Salwarex
 */

public class HotbarInterfaceOpenedPool extends ReplaceableMapPool<Player, HotbarInterface> {
    private static HotbarInterfaceOpenedPool instance = null;
    public static HotbarInterfaceOpenedPool getInstance(){
        return instance == null ? instance = new HotbarInterfaceOpenedPool() : instance;
    }
}
