package it.polimi.ingsw.messages;

public enum MessageType {
    // Network messages
    /**
     * Used to start the login process.
     */
    LOGIN,
    /**
     * Used to confirm the login success.
     */
    LOGIN_SUCCESS,
    /**
     * Used to start the process of choosing the number of players for the game.
     */
    CHOOSE_NUM_PLAYERS,
    /**
     * Used to notify the client the login failure.
     */
    LOGIN_FAIL,
    /**
     * Used to show to the client the contained message.
     */
    DISPLAY_MESSAGE,
    /**
     * Used to check in the backup if there are previously saved games that matches the requirements.
     */
    CHECK_OLD_GAMES,
    /**
     * Used to restore a previously saved game from the backup file.
     */
    RESTORE_GAME,
    /**
     * Used to notify a client disconnection.
     */
    PLAYER_DISCONNECTION,

    /**
     * Used to communicate to client which leader card player has chosen.
     */
    LEADERCARD_INITIAL_INDEX,

    /**
     * Used to let the client choose the initial resource/s.
     */
    INITIAL_RESOURCES_SELECTION,

    /**
     * Used to communicate to client which card have been activated.
     */
    LEADERCARD_ACTIVATED,

    /**
     * Used to send to client the four initial leader card.
     */
    LEADERCARD_SELECTION,

    /**
     * Used to send which actions player can do.
     */
    ACTION_OPTIONS,

    /**
     * Used as a ping message to notify that the deliver is still alive.
     */
    HEARTBEAT,

    /**
     * USed to set action completed.
     */
    END_ACTION,
    /**
     * Used to notify the end of the turn of the current player.
     */
    END_TURN,
    /**
     * Used to notify the end of the game and to announce the winner.
     */
    END_GAME,

    // Game initialization messages
    /**
     * Used to update the deck of development cards after a change.
     */
    DEVELOPMENTCARD_DECK_UPDATE,
    /**
     * Used to notify the client he's playing in single player mode.
     */
    LORENZO_TRACK_INITIALIZATION,
    /**
     * Used to update the state of a pope tile after a papal report.
     */
    POPE_TILES_UPDATE,
    /**
     * Used to update the position of the player's faith marker.
     */
    FAITH_MARKER_UPDATE,
    /**
     * Used to update the current state of the market after a change.
     */
    MARKET_UPDATE,
    /**
     * Used to update the player's warehouse after a change.
     */
    WAREHOUSE_UPDATE,
    /**
     * Used to update the player's strongbox after a change.
     */
    STRONGBOX_UPDATE,
    /**
     * Used to update the player's leader card depots after a change.
     */
    LEADERDEPOT_UPDATE,
    /**
     * Used to update the player's slot stack after the purchase of a development card.
     */
    SLOTSTACK_CARDS_UPDATE,
    /**
     * Used to update the slot stack flag counter after the purchase of a development card.
     */
    SLOTSTACK_FLAGS_UPDATE,

    /**
     * Used to update leader card after disconnection.
     */
    LEADERCARD_UPDATE,

    // Production messages
    /**
     * Used to ask/provide the available options during the production action.
     */
    PRODUCTION_OPTIONS,

    /**
     * Used to make the cli go back from the current action to the options choice.
     */
    PRODUCTION_ACK,

    /**
     * Used to activate the development card production.
     */
    PRODUCTION_DEVELOPMENTCARD,

    /**
     * Used to activate the development card production.
     */
    PRODUCTION_LEADERCARD,

    /**
     * Used to ask/provide the leader card index in case of two leader cards present in the list.
     */
    PRODUCTION_LEADERCARD_INDEX,

    /**
     * Used to ask/provide the resourceType of the produced resource.
     */
    PRODUCTION_LEADERCARD_RESOURCE_INPUT,

    /**
     * Used to ask/provide the basic production activation.
     */
    PRODUCTION_BASICPRODUCTION,

    /**
     * Used to ask/provide the resourceType of the basic production.
     */
    PRODUCTION_BASIC_RESOURCES_INPUT,

    /**
     * Used to indicate the activation and end of the production action.
     */
    PRODUCTION_ACTIVATION,

    /**
     * Used when there's no card in a slot of the slotstack.
     */
    PRODUCTION_ERROR_EMPTY_SLOT,


    //Purchase development card messages
    /**
     * Used to send the list of buyable cards.
     */
    PURCHASE_CARD_LIST,

    /**
     * Used to send the selected card.
     */
    PURCHASED_CARD,

    /**
     * Used to ask/send the chosen slotstack slot for the purchased card.
     */
    PURCHASE_CHOOSE_SLOT,

    //Leader card message
    /**
     * used to send which action player can do [activate or discard].
     */

    //Leader card message
    LEADERCARD_OPTION,

    /**
     * used to send ID of card that player can activate.
     */
    LEADERCARD_ACTIVATION_INDEX,

    /**
     * used to send ID of card that player can discard.
     */
    LEADERCARD_DISCARD_INDEX,

    /**
     * Used to ask leader cards that player can activate from server.
     */
    LEADERCARD_SHOW_ACTIVATION_OPTION,

    /**
     * Used to ask leader cards that player can discard from server.
     */
    LEADERCARD_SHOW_DISCARD_OPTION,

    /**
     * discard card that player has choosen.
     */
    LEADERCARD_DISCARD_ACTION,

    /**
     * Used to send card to activate.
     * Communicate to client that leader card can be discard.
     */
    LEADERCARD_DISCARD,

    /**
     * Used to ask if player would show only activable card or all.
     */
    LEADERCARD_ASK_ACTIVABLE_CARD,

    /**
     * Activate card that player has choosen.
     */
    LEADERCARD_SELECTED_CARD,
    LEADERCARD_ERROR_NOT_ENOUGH_RESOURCES,
    LEADERCARD_ERROR_NOT_ENOUGH_COLOR_CARD_QUANTITY,
    LEADERCARD_ALREADY_ACTIVATED,

    /**
     * Used to ask if player would activate another leader card.
     * Was send after error.
     */
    LEADERCARD_ANOTHER_ACTIVATE,

    /**
     * Used to ask if player would discard another leader card.
     * Was send after error.
     */
    LEADERCARD_ANOTHER_DISCARD,

    /**
     * Used to send permission to ask if player would activate another leader card.
     */
    LEADERCARD_PERMISSION_TRY_AGAIN_ACTIVATE_CARD,
    LEADERCARD_END_ACTION,

    // Take Resources from Market messages
    /**
     * Used to let the player choose which row or column to buy.
     */
    MARKET_CHOICE,
    /**
     * Used to let the player choose which leader card ability to use for each white marble bought from the market
     * in case the player has both leader cards with the resource ability and are both activated .
     */
    BLANKMARBLE_CHOICE,
    /**
     * Used to let the player insert the purchased resources in the stores (warehouse and in case the leader depots) or change their order.
     */
    ARRANGED_RESOURCES,
    /**
     * Used to notify the player the discarded resources.
     */
    DISCARDED_RESOURCES,
    /**
     * Used to notify the player the purchased resources.
     */
    PURCHASED_RESOURCES,

    // Action Token
    /**
     * Used to show the player which action token has been peeked from the deck.
     */
    ACTION_TOKEN,
    /**
     * Used to update the position of the black cross on Lorenzo's faith track.
     */
    LORENZO_TRACK,

    // Error messages
    ERROR_NOT_INITIALIZED_ACTION,
    /**
     * Used to notify the client entered a invalid username.
     */
    ERROR_USERNAME,
    ERROR_TOO_LEADERCARDS,

}