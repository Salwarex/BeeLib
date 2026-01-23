package ru.waxera.beeLib.utils.gui.container;

import org.bukkit.entity.Player;
import ru.waxera.beeLib.utils.data.pools.map.ReplaceableMapPool;

/**
 * An implementation of a ReplaceableMapPool designed to store data
 * about currently open {@link ContainerInterface ContainerInterface's}.
 *
 * @see ReplaceableMapPool
 * @see ru.waxera.beeLib.utils.data.pools.map.MapPool MapPool
 * @see ru.waxera.beeLib.utils.data.pools.Pool Pool Interface
 * @version 2
 * @since v1.0.10
 * @author Salwarex
 */

public class InterfaceOpenedPool extends ReplaceableMapPool<Player, ContainerInterface> {
    private static InterfaceOpenedPool instance = null;

    public static InterfaceOpenedPool getInstance() {
        return instance == null ? instance = new InterfaceOpenedPool() : instance;
    }
}
