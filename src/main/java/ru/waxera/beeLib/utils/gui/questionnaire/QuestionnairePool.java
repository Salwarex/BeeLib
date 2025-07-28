package ru.waxera.beeLib.utils.gui.questionnaire;

import org.bukkit.entity.Player;
import ru.waxera.beeLib.utils.data.pools.map.IrreplaceableMapPool;

/**
 * An implementation of a IrreplaceableMapPool designed to store data
 * about currently open {@link Questionnaire Questionnaires}.
 *
 * @see IrreplaceableMapPool
 * @see ru.waxera.beeLib.utils.data.pools.map.MapPool MapPool
 * @see ru.waxera.beeLib.utils.data.pools.Pool Pool Interface
 * @version 2
 * @since v1.2 (QuestionnaireHandler), v1.4 (QuestionnairePool)
 * @author Salwarex
 */

public class QuestionnairePool extends IrreplaceableMapPool<Player, Questionnaire> {
    private static QuestionnairePool instance = null;
    public static QuestionnairePool getInstance() {
        return instance == null ? instance = new QuestionnairePool() : instance;
    }
}
