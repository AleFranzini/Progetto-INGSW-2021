package it.polimi.ingsw.view.cli;

import java.util.ArrayList;
import java.util.List;

/**
 * Class GameBoardCLI contains methods to update the gameboard.
 */
public class GameBoardCLI {
    private final CLI cli;
    List<Slot> slotStack;
    List<Slot> leaderCardSlot;
    private int[][] flagsCounter;
    private String[] warehouse;
    private String[] strongbox;
    private String leaderDepot = "";
    private FaithTrackCLI faithTrackCLI;
    private int selectedLeaderCard;

    /**
     * Construct the gameboard with all its structures and associated it with a CLI.
     * @param cli to be set
     */
    public GameBoardCLI(CLI cli) {
        this.cli = cli;
        String emptySlot = cli.getDeckDevelopmentCardCLI().printDevelopmentCardByID(-1);
        slotStack = new ArrayList<>();
        leaderCardSlot = new ArrayList<>();
        leaderCardSlot.add(new Slot(-1, cli.getLeaderCardCLI().printLeaderCardByID(-1), false));
        leaderCardSlot.add(new Slot(-1, cli.getLeaderCardCLI().printLeaderCardByID(-1), false));
        slotStack.add(new Slot(-1, emptySlot));
        slotStack.add(new Slot(-1, emptySlot));
        slotStack.add(new Slot(-1, emptySlot));
        flagsCounter = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        faithTrackCLI = new FaithTrackCLI();
        setWarehouse("-\n-\n-");
        setStrongbox("0 COIN\n0 SERVANT\n0 SHIELD\n0 STONE");
        setLeaderDepot("-\n-");
    }

    public void setActivatedLeaderCard(int index) {
        for(Slot slot :leaderCardSlot){
            if(slot.getId() == selectedLeaderCard && !slot.isDiscarded()){
                slot.setActivated();
            }
        }
    }

    public void setDiscardLeaderCard(int index) {
        for(Slot slot :leaderCardSlot){
            if(slot.getId() == selectedLeaderCard){
                slot.setDiscarded();
                slot.setString(cli.getLeaderCardCLI().printLeaderCardByID(-1));
            }
        }
    }

    public void updateLeaderCardsSlot(int[] leaderCardIDs) {
        leaderCardSlot.set(0, new Slot(leaderCardIDs[0], cli.getLeaderCardCLI().printLeaderCardByID(leaderCardIDs[0]), false));
        leaderCardSlot.set(1, new Slot(leaderCardIDs[1], cli.getLeaderCardCLI().printLeaderCardByID(leaderCardIDs[1]), false));
    }

    public int getLeaderPositionById(int id) {
        if (leaderCardSlot.get(0).getId() == id)
            return 0;
        else if (leaderCardSlot.get(1).getId() == id)
            return 1;
        return -1;
    }

    /**
     * Get the faithtrack of the gameboard.
     * @return the faithtrack object
     */
    public FaithTrackCLI getFaithTrackCLI() {
        return faithTrackCLI;
    }

    /**
     * Update which development cards are contained into the slotstack.
     * @param topCardsIDs is the list of IDs of top cards in the slotstack
     */
    public void updateSlotStack(List<Integer> topCardsIDs) {
        for (int i = 0; i < 3; i++) {
            if (!topCardsIDs.get(i).equals(slotStack.get(i).getId())) {
                slotStack.get(i).setId(topCardsIDs.get(i));
                slotStack.get(i).setString(cli.getDeckDevelopmentCardCLI().printDevelopmentCardByID(topCardsIDs.get(i)));
            }
        }
    }

    /**
     * Set the counter of the flags of the development cards.
     * @param flagsCounter is new counter
     */
    public void setFlagsCounter(int[][] flagsCounter) {
        this.flagsCounter = flagsCounter;
    }

    /**
     * Update the resources in the warehouse.
     * @param message contains the resources of the warehouse
     */
    public void setWarehouse(String message) {
        String[] warehouse = message.split("\n");
        StringBuilder set = new StringBuilder();
        for (int i = 0; i < warehouse.length; i++) {
            if (!warehouse[i].contains("-")) {
                String[] depot = warehouse[i].split(" ");
                int num = Integer.parseInt(depot[0]);
                for (int j = 0; j < num; j++)
                    set.append(" ").append(ColorCLI.setColorResource(depot[1])).append(" ");
                set.append(" ━ ".repeat(Math.max(0, i + 1 - num)));
            } else {
                set.append(" ━ ".repeat(i + 1));
            }
            set.append("\n");
        }
        this.warehouse = set.toString().split("\n");
    }

    /**
     * Update the resources in the warehouse.
     * @param message contains the resources of the strongbox
     */
    public void setStrongbox(String message) {
        String[] strongbox = message.split("\n");
        StringBuilder set = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String[] parts = strongbox[i].split(" ");
            int quantity = Integer.parseInt(parts[0]);
            set.append(ColorCLI.setColorResource(parts[1]));
            if (quantity < 10) {
                set.append("  :  ").append(quantity).append(" \n");
            } else
                set.append("  :  ").append(quantity).append("\n");
        }
        this.strongbox = set.toString().split("\n");
    }

    /**
     * Update the resources in the leader card depots.
     * @param message contains the resources of the depots
     */
    public void setLeaderDepot(String message) {
        String[] depot = message.split("\n");
        leaderDepot = "";
        for (int i = 0; i < 2; i++) {
            String line = "━  ━ ";
            if (!depot[i].equals("-")) {
                String[] parts = depot[i].split(" ");
                int quantity = Integer.parseInt(parts[0]);
                if (quantity == 1)
                    line = line.replaceFirst("━", "" + ColorCLI.setColorResource(parts[1]));
                else if (quantity == 2)
                    line = line.replaceAll("━", "" + ColorCLI.setColorResource(parts[1]));
            }
            leaderDepot += line;
            if (i == 0)
                leaderDepot += " ││ ";
        }
    }

    /**
     * Create the string that contains all the updated gameboard structures.
     * @return the gameboard as a string
     */
    public String printGameBoard() {
        StringBuilder gameboard = new StringBuilder();
        StringBuilder stores = new StringBuilder();
        String slots;
        String leaderCards;
        gameboard.append(faithTrackCLI.printFaithTrack(false));
        stores.append("       ┏━━━━━━ WAREHOUSE ━━━━━┓\n").append("       ┃        ┌────┐        ┃\n").append("       ┃      ┌─┘ ").append(warehouse[0]).append("└─┐      ┃\n").append("       ┃    ┌─┘ ").append(warehouse[1]).append(" └─┐    ┃\n").append("       ┃  ┌─┘ ").append(warehouse[2]).append("  └─┐  ┃\n").append("       ┃  └────────────────┘  ┃\n");

        stores.append("       ┃  ┌───────┐┌───────┐  ┃\n       ┃  │ ").append(leaderDepot).append(" │  ┃\n       ┃  └───────┘└───────┘  ┃\n");

        stores.append("       ┣━━━━━━ STRONGBOX ━━━━━┫\n").append("       ┃  ┌────────────────┐  ┃\n");
        for (int i = 0; i < 4; i++)
            if (strongbox[i].contains(""))
                stores.append("       ┃  │    ").append(strongbox[i]).append("    │  ┃\n");
            else
                stores.append("       ┃  │    ").append(strongbox[i]).append("    │  ┃\n");
        stores.append("       ┃  └────────────────┘  ┃\n").append("       ┗━━━━━━━━━━━━━━━━━━━━━━┛\n");

        slots = "╭──── Green ─────┬───── Blue ────┬──── Yellow ───┬──── Purple ────╮\n" +
                "│  " + printFlag("LEVEL_1", "GREEN", 0, 0) + "    " + printFlag("LEVEL_2", "GREEN", 1, 0) + "  │  " + printFlag("LEVEL_1", "BLUE", 0, 1) + "   " + printFlag("LEVEL_2", "BLUE", 1, 1) + "  │  " + printFlag("LEVEL_1", "YELLOW", 0, 2) + "   " + printFlag("LEVEL_2", "YELLOW", 1, 2) + "  │  " + printFlag("LEVEL_1", "PURPLE", 0, 3) + "    " + printFlag("LEVEL_2", "PURPLE", 1, 3) + "  │\n" +
                "│      " + printFlag("LEVEL_3", "GREEN", 2, 0) + "      │     " + printFlag("LEVEL_3", "BLUE", 2, 1) + "      │     " + printFlag("LEVEL_3", "YELLOW", 2, 2) + "      │      " + printFlag("LEVEL_3", "PURPLE", 2, 3) + "      │\n" +
                "╰────────────────┴──────────── FLAGS ────────────┴────────────────╯\n\n" +
                "┏━━━━━━━━━━━━━━━━━━━━━━━ DEVELOPMENT CARDS ━━━━━━━━━━━━━━━━━━━━━━━┓\n" +
                "┃                                                                 ┃\n" +
                printSlotStack() +
                "┃                                                                 ┃\n" +
                "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n";

        leaderCards = "\n\n\n┏━━━━━━━━━━━━━━━ LEADER CARDS ━━━━━━━━━━━━━━━━┓\n" +
                "                                                                   ┃                                             ┃\n" +
                printLeaderCards() +
                "┃                                             ┃\n" +
                "┃     ┃" + leaderCardSlot.get(0).isActivated() + "┃     ┃" + leaderCardSlot.get(1).isActivated() + "┃     ┃\n" +
                "┃                                             ┃\n" +
                "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n";

        String[][] board = new String[3][];
        board[0] = stores.toString().split("\n");
        board[1] = slots.split("\n");
        board[2] = leaderCards.split("\n");

        for (int i = 0; i < 17; i++) {
            gameboard.append(board[0][i]).append("            ").append(board[1][i]).append("            ").append(board[2][i]).append("\n");
        }

        return gameboard.toString();
    }

    /**
     * Create the string that contains the flag and his quantity.
     * @param level is the level of the flag
     * @param color is the color of the flag
     * @param x is the x coordinate of the flag quantity in the flag counter
     * @param y is the y coordinate of the flag quantity in the flag counter
     * @return the flag and his quantity as a string
     */
    private String printFlag(String level, String color, int x, int y) {
        return ColorCLI.setColorFlag(level, color) + "×" + flagsCounter[x][y];
    }

    /**
     * Create the string that contains the development cards of the slotstack.
     * @return the slotstack as a string
     */
    private String printSlotStack() {
        String[][] stacks = new String[3][];
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (Slot slot : slotStack) {
            stacks[i] = slot.getString().split("\n");
            i++;
        }
        for (i = 0; i < 8; i++) {
            out.append("┃").append(stacks[0][i]).append(stacks[1][i]).append(stacks[2][i]).append("     ┃\n");
        }
        return out.toString();
    }

    /**
     * Create the string that contains the leader cards of the gameboard.
     * @return the leader cards as a string
     */
    private String printLeaderCards() {
        String[][] stacks = new String[2][];
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (Slot slot : leaderCardSlot) {
            stacks[i] = slot.getString().split("\n");
            i++;
        }
        for (i = 0; i < 8; i++) {
            out.append("┃").append(stacks[0][i]).append(stacks[1][i]).append("     ┃\n");
        }
        return out.toString();
    }

    public int getSelectedLeaderCard() {
        return selectedLeaderCard;
    }

    public void setSelectedLeaderCard(int selectedLeaderCard) {
        this.selectedLeaderCard = selectedLeaderCard;
    }

    /**
     * Class Slot represent a type used as a card slot.
     */
    private class Slot {
        private int id;
        private String string;
        private boolean activated;
        private boolean discarded;

        /**
         * Construct a Slot with a defined id and card.
         * @param id is the id of the card
         * @param string is the string that represent the card
         */
        public Slot(int id, String string) {
            this.id = id;
            this.string = string;
        }

        /**
         * Construct a Slot with a defined id, card and activation.
         * @param id is the id of the card
         * @param string is the string that represent the card
         * @param activated is a boolean that defines if a card has been activated
         */
        public Slot(int id, String string, boolean activated) {
            this.id = id;
            this.string = string;
            this.activated = activated;
        }

        /**
         * Get the id of the slot.
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * Set the id of the slot.
         * @param id to be set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * Get the string of the slot.
         * @return the string
         */
        public String getString() {
            return string;
        }

        /**
         * Set the string of the slot.
         * @param string to be set
         */
        public void setString(String string) {
            this.string = string;
        }

        /**
         * Tell if the slot has been activated and return the corresponding string.
         * @return the string corresponding to the state of the slot
         */
        public String isActivated() {
            if(discarded)
                return "  DISCARDED  ";
            else if (!activated)
                return "NOT ACTIVATED";
            return "  ACTIVATED  ";
        }

        /**
         * Set the slot as activated.
         */
        public void setActivated() {
            this.activated = true;
        }

        /**
         * Tell id the slot has been discarded.
         * @return true if the slot is discarded, false otherwise
         */
        public boolean isDiscarded() {
            return discarded;
        }

        /**
         * Set the slot as discarded.
         */
        public void setDiscarded() {
            this.discarded = true;
        }
    }
}
