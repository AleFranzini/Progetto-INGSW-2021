package it.polimi.ingsw.view.cli;

/**
 * CLass UtilsCLI contains useful methods of the cli for general purpose.
 */
public class UtilsCLI {
    private CLI Cli;

    /**
     * Construct a UtilsCLI object with an associated CLI.
     *
     * @param cli to be set
     */
    public UtilsCLI(CLI cli) {
        this.Cli = cli;
    }

    /**
     * Print the logo of the game.
     *
     * @return a string containing the logo
     */
    public String printLogo() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String logo = "\n" +
                "              ███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗ ███████╗             \n" +
                "              ████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗██╔════╝             \n" +
                "              ██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝███████╗             \n" +
                "              ██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗╚════██║             \n" +
                "              ██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║███████║             \n" +
                "              ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝             \n" +
                "                                                                                       \n" +
                "                                     ██████╗ ███████╗                                  \n" +
                "                                    ██╔═══██╗██╔════╝                                  \n" +
                "                                    ██║   ██║█████╗                                    \n" +
                "                                    ██║   ██║██╔══╝                                    \n" +
                "                                    ╚██████╔╝██║                                       \n" +
                "                                     ╚═════╝ ╚═╝                                       \n" +
                "                                                                                       \n" +
                "██████╗ ███████╗███╗   ██╗ █████╗ ██╗███████╗███████╗ █████╗ ███╗   ██╗ ██████╗███████╗\n" +
                "██╔══██╗██╔════╝████╗  ██║██╔══██╗██║██╔════╝██╔════╝██╔══██╗████╗  ██║██╔════╝██╔════╝\n" +
                "██████╔╝█████╗  ██╔██╗ ██║███████║██║███████╗███████╗███████║██╔██╗ ██║██║     █████╗  \n" +
                "██╔══██╗██╔══╝  ██║╚██╗██║██╔══██║██║╚════██║╚════██║██╔══██║██║╚██╗██║██║     ██╔══╝  \n" +
                "██║  ██║███████╗██║ ╚████║██║  ██║██║███████║███████║██║  ██║██║ ╚████║╚██████╗███████╗\n" +
                "╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚══════╝\n" +
                "                                                                                       \n";
        String names = ColorCLI.ANSI_BLUE + "          Alessandro Franzini" + ColorCLI.ANSI_WHITE + "   ━   " + ColorCLI.ANSI_BLUE + "Filippo Galetti" + ColorCLI.ANSI_WHITE + "   ━   " + ColorCLI.ANSI_BLUE + "Federico Grugni\n" + ColorCLI.RESET;
        return logo + names;
    }


    /**
     * Return the correctly formatted ResourceType from the input.
     *
     * @param input contains the string to check
     * @return the ResourceType, or null if the input is malformed
     */
    public String checkResourceType(String input) {
        return switch (input.toUpperCase()) {
            case "C", "CO", "COI", "COIN" -> "COIN";
            case "ST", "STO", "STON", "STONE" -> "STONE";
            case "SH", "SHI", "SHIE", "SHIEL", "SHIELD" -> "SHIELD";
            case "SE", "SER", "SERV", "SERVA", "SERVAN", "SERVANT" -> "SERVANT";
            default -> null;
        };
    }

    /**
     * Select the string to be displayed as an help to proceed in the CLI.
     *
     * @param string is the selector of the help string
     * @return a string containing the help
     */
    public String displayHelp(String string) {
        StringBuilder help = new StringBuilder();
        help.append("\n/------ HELP ------/\n");
        switch (string) {
            case "chooseCardToPurchase" -> help.append("     1  2  3  4\n     5  6  7  8\n     9  10 11 12\n");
            case "marketLine" -> help.append("Market lines are formatted as below:\n")
                    .append("r   1  \no   2  \nw   3  \n")
                    .append("       1  2  3  4\n").append("      c o l u m n\n");
            case "manageWarehouse" -> help.append("WAREHOUSE RULES:\n")
                    .append("Resource1: is the resource in the first depot (max N = 1)\n")
                    .append("Resource2: is the resource in the second depot (max N = 2)\n")
                    .append("Resource3: is the resource in the third depot (max N = 3)\n")
                    .append("Insert the resources in the right order, write '-' if a depot is empty\n")
                    .append("Remember that each depot must have the same resource and all depots must contain different resources!\n");
            case "manageLeaderDepot" -> help.append("LEADER DEPOT RULES:\n")
                    .append("ResourceLeader1: is the resource in the first leader depot (max N = 2)\n")
                    .append("ResourceLeader2: is the resource in the second leader depot (max N = 2)\n")
                    .append("Insert the resources in the right order, write '-' when the leader depot is empty \n")
                    .append("Remember that this special depot can only store the indicated resources\n")
                    .append("You can also store the same type of resource in a basic warehouse depot!\n");
            default -> help.append("Retry");
        }
        help.append("/------------------/\n");
        return help.toString();
    }

    /**
     * Create the string that contains the gameboard with all the updated structures.
     *
     * @return the gameboard as a string
     */
    public String printBoard() {
        StringBuilder game = new StringBuilder();
        String market = Cli.getMarketCLI().printMarket();

        String[][] board = new String[2][];
        board[0] = Cli.getDeckDevelopmentCardCLI().getDevelopmentCardMatrix().split("\n");
        board[1] = market.split("\n");
        game.append("\n");
        if (Cli.getLorenzoFaithTrack() != null)
            game.append(Cli.showLorenzoFaithTrack());

        for (int i = 0; i < 10; i++)
            game.append(board[0][i]).append("            ").append(board[1][i]).append("\n");
        for (int i = 10; i < 26; i++)
            game.append(board[0][i]).append("\n");

        return game.toString();
    }
}
