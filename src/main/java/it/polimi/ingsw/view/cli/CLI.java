package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.IllegalInputException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientOnline;
import it.polimi.ingsw.view.View;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLI implements View {
    private static final PrintWriter out = new PrintWriter(System.out, true);
    private static final Scanner in = new Scanner(System.in);
    private final Client client;
    private String response;
    private int command;
    private boolean[] legalAction;
    private boolean actionCompleted;
    private final UtilsCLI utils = new UtilsCLI(this);
    private final GameBoardCLI gameboardCLI;
    private final Gson gson = new Gson();
    private final MarketCLI marketCLI = new MarketCLI();
    private final DevelopmentCardShopCLI developmentCardShopCLI = new DevelopmentCardShopCLI();
    private final LeaderCardCLI leaderCardCLI = new LeaderCardCLI();
    private FaithTrackCLI lorenzoFaithTrack;

    /**
     * constructor of CLI
     *
     * @param client is the client that runs CLI
     */
    public CLI(Client client) {
        this.client = client;
        gameboardCLI = new GameBoardCLI(this);
    }

    @Override
    public void setActionCompleted(boolean actionCompleted) {
        this.actionCompleted = actionCompleted;
    }

    public FaithTrackCLI getLorenzoFaithTrack() {
        return lorenzoFaithTrack;
    }

    public MarketCLI getMarketCLI() {
        return marketCLI;
    }

    public GameBoardCLI getGameboardCLI() {
        return gameboardCLI;
    }

    public DevelopmentCardShopCLI getDeckDevelopmentCardCLI() {
        return developmentCardShopCLI;
    }

    public LeaderCardCLI getLeaderCardCLI() {
        return leaderCardCLI;
    }

    //UPDATE METHODS
    @Override
    public void updatePopeTiles(Message message) {
        getGameboardCLI().getFaithTrackCLI().setPopeTiles(Arrays.asList(gson.fromJson(message.getMessage(), Integer[].class)));
    }

    @Override
    public void updateFaithMarker(Message message) {
        getGameboardCLI().getFaithTrackCLI().setFaithMarkerPosition(message);
    }

    @Override
    public void updateWarehouse(Message message) {
        getGameboardCLI().setWarehouse(message.getMessage());
    }

    @Override
    public void updateStrongbox(Message message) {
        getGameboardCLI().setStrongbox(message.getMessage());
    }

    @Override
    public void updateLeaderDepot(Message message) {
        StringBuilder stringBuilder = new StringBuilder();
        if (message.getMessage().equals("null"))
            stringBuilder.append("-\n-");
        else {
            JSONArray jsonArray = new JSONArray(message.getMessage());
            for (int i = 0; i < 2; i++) {
                if (jsonArray.get(i).toString().equals("null")) {
                    stringBuilder.append("-\n");
                    continue;
                }
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                stringBuilder.append(jsonObject.getInt("quantity")).append(" ").append(jsonObject.getString("resourceType")).append("\n");
            }
        }
        getGameboardCLI().setLeaderDepot(stringBuilder.toString());
    }

    @Override
    public void updateSlotStack(List<Integer> slotStackIDs) {
        getGameboardCLI().updateSlotStack(slotStackIDs);
    }

    @Override
    public void updateFlagsCounter(Message message) {
        getGameboardCLI().setFlagsCounter(gson.fromJson(message.getMessage(), int[][].class));
    }

    @Override
    public void updateMarket(Message message) {
        marketCLI.setMarketCLI(message.getMessage());
        if (actionCompleted) {
            out.println(utils.printBoard());
        }
    }

    @Override
    public void updateLeaderCard(int[] indexes){
        gameboardCLI.updateLeaderCardsSlot(indexes);
//        for(int i = 0; i<2;i++){
//            if(indexes[i] == -1){
//                gameboardCLI.setDiscardLeaderCard(i);
//            }else{
//                if(indexes[i+2] == 1){
//                    gameboardCLI.setActivatedLeaderCard(i);
//                }
//            }
//        }
    }

    @Override
    public void updateDevelopmentCardDeck(Integer[] cardShop) {
        List<Integer> cardList = Arrays.asList(cardShop);
        developmentCardShopCLI.setCardList(cardList);
        if (actionCompleted) {
            out.println(utils.printBoard());
        }
    }

    @Override
    public void updateLorenzoFaithTrack(Message message) {
        lorenzoFaithTrack.setFaithMarkerPosition(message);
    }

    @Override
    public void printGameboard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        out.println(gameboardCLI.printGameBoard());
    }

    @Override
    public void setupProcess() {
        System.out.println("Insert Server IP: ");
        ((ClientOnline) client).setServerIP(in.nextLine());
        System.out.println("Insert Server port: ");
        ((ClientOnline) client).setServerPort(Integer.parseInt(in.nextLine()));
        ((ClientOnline) client).connectToServer();
        actionCompleted = false;
    }

    /**
     * Method insertUsername is used to ask the client to write its username.
     */
    @Override
    public void insertUsername() {
        System.out.println(utils.printLogo());
        System.out.print("\n\n" + ColorCLI.ANSI_YELLOW + "Insert Username" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        client.sendMessage(new Message(MessageType.LOGIN, in.nextLine()));
    }

    /**
     * Method insertNumberOfPlayers is used to ask the client to set the number of players for this match.
     */
    @Override
    public void insertNumberOfPlayers() {
        System.out.print(ColorCLI.ANSI_YELLOW + "Insert number of players " + ColorCLI.ANSI_BLUE + "[1-4]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        client.sendMessage(new Message(MessageType.CHOOSE_NUM_PLAYERS, validateInput(1, 4, "")));
    }

    @Override
    public void askRestoreGame(String games) {
        String[] dates = games.split("\n");
        String chosenGame = null;
        while (true) {
            out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Do you want to restore a previous game? " + ColorCLI.ANSI_BLUE + "[y/n]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            String input = in.nextLine();
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                if (dates.length == 1) {
                    chosenGame = dates[0];
                } else {
                    out.println("On the server there are " + dates.length + " games previously saved:");
                    for (int i = 0; i < dates.length; i++) {
                        out.println(i + 1 + ") " + dates[i]);
                    }
                    out.printf("Select the number of the game to restore [1-" + dates.length + "] > ");
                    int choice = validateInput(1, dates.length, "") - 1;
                    chosenGame = dates[choice];
                }
                break;
            } else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
                out.println("Starting a new game ...");
                break;
            }
        }
        client.sendMessage(new Message(MessageType.RESTORE_GAME, chosenGame));
    }

    @Override
    public void checkEndTurn() {
        actionCompleted = false;
        client.sendMessage(new Message(MessageType.END_TURN));
    }

    @Override
    public void selectLeaderCardsFromDeck(int[] indexes) {
        out.println("\n" + leaderCardCLI.getLeaderCardArray(indexes, true) + "\n");
        out.println(ColorCLI.ANSI_YELLOW + "\u1405 Choose two leader card: " + ColorCLI.ANSI_BLUE + "[1/2/3/4]" + ColorCLI.RESET);
        out.printf(ColorCLI.ANSI_YELLOW + "\u1405 First card" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        int card = validateInput(1, 4, "");
        int app;
        do {
            out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Second card" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            app = validateInput(1, 4, "");
        } while (card == app);
        out.println("");
        int[] selectedIndexes = new int[]{indexes[card - 1], indexes[app - 1]};
        client.sendMessage(new Message(MessageType.LEADERCARD_SELECTION, selectedIndexes));
    }

    @Override
    public void setInitialLeaderCard(int[] indexesCardInitial) {
        gameboardCLI.updateLeaderCardsSlot(indexesCardInitial);
    }

    @Override
    public void setActivatedLeaderCard(int indexCardActivated) {
        gameboardCLI.setActivatedLeaderCard(indexCardActivated);
    }

    @Override
    public void setDiscardLeaderCard(int index) {
        gameboardCLI.setDiscardLeaderCard(index);
    }

    @Override
    public void chooseInitialResources(int numResources) {
        StringBuilder choice = new StringBuilder();
        out.print(ColorCLI.ANSI_YELLOW + "\u1405 You have to choose " + numResources + " resource");
        if (numResources == 2)
            out.print("s");
        out.println();
        String resourceType;
        for (int i = 0; i < numResources; i++) {
            do {
                out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Insert resource" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                resourceType = in.nextLine();
            } while (utils.checkResourceType(resourceType) == null);
            choice.append(utils.checkResourceType(resourceType)).append(" ");
        }
        client.sendMessage(new Message(MessageType.INITIAL_RESOURCES_SELECTION, choice.toString()));
    }


    /**
     * This method is called by the handleMessage in the Client to print simple messages.
     *
     * @param message contains the string to print
     */
    @Override
    public void displayMessage(String message) {
        out.println(message);
    }

    @Override
    public void chooseInitialAction(boolean fromEndAction, boolean fromLeaderCardAction, Message message) { //first time that chooseInitialAction is called fromLeaderCard is false
        int input;
        printGameboard();
        String string0 = "Take resources from the market.";
        String string1 = "Buy development card.";
        String string2 = "Activate the production.";
        String string3 = "Play a leader card.";

        if (actionCompleted) {
            lastAction(fromLeaderCardAction);
        } else {

            legalAction = gson.fromJson(message.getMessage(), boolean[].class);
            StringBuilder app = new StringBuilder();
            app.append(ColorCLI.ANSI_YELLOW).append("\n\u1405 Choose the action to perform: ");
            if (!fromEndAction)
                app.append(ColorCLI.ANSI_BLUE).append("\n1) ").append(ColorCLI.RESET).append(string0);
            else
                app.append(ColorCLI.ANSI_BLUE).append("\n1) ").append(ColorCLI.RESET).append(string0.replaceAll("", "\u0336"));
            if (legalAction[0])
                app.append(ColorCLI.ANSI_BLUE).append("\n2) ").append(ColorCLI.RESET).append(string1);
            else
                app.append(ColorCLI.ANSI_BLUE).append("\n2) ").append(ColorCLI.RESET).append(string1.replaceAll("", "\u0336"));
            if (legalAction[1])
                app.append(ColorCLI.ANSI_BLUE).append("\n3) ").append(ColorCLI.RESET).append(string2);
            else
                app.append(ColorCLI.ANSI_BLUE).append("\n3) ").append(ColorCLI.RESET).append(string2.replaceAll("", "\u0336"));
            if (!fromLeaderCardAction)
                app.append(ColorCLI.ANSI_BLUE).append("\n4) ").append(ColorCLI.RESET).append(string3);
            else
                app.append(ColorCLI.ANSI_BLUE).append("\n4) ").append(ColorCLI.RESET).append(string3.replaceAll("", "\u0336"));
            app.append(ColorCLI.ANSI_BLUE).append("\n5) ").append(ColorCLI.RESET).append("Show common plank.");
            app.append(ColorCLI.RESET);
            out.println(app);
            while (true) {
                out.printf(ColorCLI.ANSI_YELLOW + "Your choice" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                input = validateInput(1, 5, "");
                if (input == 1 && !fromEndAction) {
                    break;
                } else if (input == 2 && legalAction[0]) {
                    break;
                } else if (input == 3 && legalAction[1]) {
                    break;
                } else if (input == 4 && !fromLeaderCardAction) {
                    break;
                } else if (input == 5) {
                    out.println(utils.printBoard());
                    out.println(app);
                }
            }
            client.sendMessage(new Message(MessageType.ACTION_OPTIONS, input));
        }
    }

    @Override
    public void lastAction(boolean fromLeaderCardAction) {
        int input;
        StringBuilder app = new StringBuilder();
        app.append(ColorCLI.ANSI_YELLOW).append("\n\u1405 Choose the action to perform: ");
        app.append(ColorCLI.ANSI_BLUE).append("\n1) ").append(ColorCLI.RESET).append("End turn.");
        app.append(ColorCLI.ANSI_BLUE).append("\n2) ").append(ColorCLI.RESET).append("Show common plank.");
        if (!fromLeaderCardAction)
            app.append(ColorCLI.ANSI_BLUE).append("\n3) ").append(ColorCLI.RESET).append("Play a leader card.");
        app.append(ColorCLI.RESET);
        out.println(app);
        while (true) {
            out.printf(ColorCLI.ANSI_YELLOW + "Your choice" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            input = validateInput(1, 3, "");
            if (input == 1) {
                checkEndTurn();
                break;
            } else if (input == 2) {
                out.println(utils.printBoard());
                out.println(app);
            } else if (input == 3 && !fromLeaderCardAction) {
                client.sendMessage(new Message(MessageType.ACTION_OPTIONS, 4));
                break;
            }
        }
    }

    // LEADER CARDS METHODS

    /**
     * check if input is valid
     *
     * @return response the correct string
     */
    private String checkInputYn(String messageToShow) {
        String response;
        do {
            out.printf(messageToShow);
            response = in.nextLine();
            while (!response.equalsIgnoreCase("Y") && !response.equals("y") && !response.equals("N") && !response.equals("n") && !response.equals("q") && !response.equals("quit") && !response.equals("h") && !response.equals("help")) {
                out.printf("Please enter a valid response.\nIf you need help type \"help\".\n" + ColorCLI.ANSI_YELLOW + "Your choice. " + ColorCLI.ANSI_BLUE + "[Y/n]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                response = in.nextLine();
            }
            if (response.equals("help")) {
                utils.displayHelp("");
            }
        } while (response.equals("help"));
        return response;
    }

    /**
     * check if int input is valid
     *
     * @param min  range
     * @param max  range
     * @param help message to show
     * @return correct input
     */
    public int validateInput(int min, int max, String help) {
        int input;
        String str;
        while (true) {
            str = in.nextLine();
            if (str.equals("q") || str.equals("quit")) {
                return -1;
            } else if (str.equals("h") || str.equals("help")) {
                out.println(utils.displayHelp(help));
                out.printf(ColorCLI.ANSI_YELLOW + "Your choice" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                return validateInput(min, max, help);
            } else if (str.matches("-?\\d+(\\d+)?")) {
                input = Integer.parseInt(str);
                if (input >= min && input <= max)
                    return input;
            }
            out.println("Input " + ColorCLI.ANSI_RED + " error" + ColorCLI.RESET + "! Value must be between " + min + " and " + max);
            out.printf(ColorCLI.ANSI_YELLOW + "Your choice" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        }
    }

    @Override
    public void displayLeaderCardOption(boolean[] message) {
        StringBuilder display = new StringBuilder();

        String string1 = "to activate leader card.\n";
        String string2 = "to discard leader card.\n";

        display.append(ColorCLI.ANSI_YELLOW).append("\u1405 Which action do you want to choose?\n").append(ColorCLI.RESET);
        display.append(ColorCLI.ANSI_BLUE).append("1) ").append(ColorCLI.RESET);
        if (message[0])
            display.append(ColorCLI.RESET).append(string1);
        else
            display.append(string1.replaceAll("", "\u0336"));
        display.append(ColorCLI.ANSI_BLUE).append("2) ").append(ColorCLI.RESET);
        if (message[1])
            display.append(string2);
        else
            display.append(string2.replaceAll("", "\u0336"));
        display.append(ColorCLI.ANSI_YELLOW).append("Your choice").append(ColorCLI.ANSI_RED).append(" > ").append(ColorCLI.RESET);
        out.printf("\n" + display);
        int input = validateInput(1, 2, "");
        if (input == -1)
            chooseInitialAction(actionCompleted, false, new Message(legalAction));
        else if (input == 1 && !message[0]) {
            out.println("\nThis action is enabled. You don't have any card to activate!\n");
            displayLeaderCardOption(message);
        } else if (input == 2 && !message[1]) {
            out.println("\nThis action is enabled. You don't have any card to discard!\n");
            displayLeaderCardOption(message);
        } else
            switch (input) {
                case 1 -> askActivableCard();
                case 2 -> client.sendMessage(new Message(MessageType.LEADERCARD_SHOW_DISCARD_OPTION));
            }
    }

    @Override
    public void askActivableCard() {
        response = checkInputYn("\nY if you want to see only activables cards, n to see them all. " + ColorCLI.ANSI_BLUE + "[Y/n]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        switch (response) {
            case "Y":
            case "y":
                client.sendMessage(new Message(MessageType.LEADERCARD_SHOW_ACTIVATION_OPTION, true));
                break;
            case "N":
            case "n":
                client.sendMessage(new Message(MessageType.LEADERCARD_SHOW_ACTIVATION_OPTION, false));
                break;
            case "quit":
                chooseInitialAction(false, false, new Message(legalAction));
                break;
            default:
                break;
        }
    }

    @Override
    public void showActivationLeaderCard(int[] cardsID) {
        switch (cardsID.length) {
            case 0 -> out.println("\nYou don't have any leader card.\n");
            case 1 -> {
                response = checkInputYn("This is your card:\n" + leaderCardCLI.printLeaderCardByID(cardsID[0]) + "\nDo you want to activate it? " + ColorCLI.ANSI_BLUE + "[Y/n]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                switch (response) {
                    case "Y", "y" -> {
                        gameboardCLI.setSelectedLeaderCard(cardsID[0]);
                        client.sendMessage(new Message(MessageType.LEADERCARD_SELECTED_CARD, 0));
                    }
                    default -> leaderCardEndAction(null);
                }
            }
            case 2 -> {
                out.println("\n" + leaderCardCLI.getLeaderCardArray(cardsID, true));
                out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Choose which card do you want to activate:\n" + ColorCLI.ANSI_BLUE + "1)" + ColorCLI.RESET + " the first one.\n" + ColorCLI.ANSI_BLUE + "2)" + ColorCLI.RESET + " the second one.\n" + ColorCLI.ANSI_YELLOW + "Your choice " + ColorCLI.ANSI_BLUE + "[1/2]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                command = validateInput(1, 2, "");
                if (command == -1)
                    leaderCardEndAction(null);
                else
                    gameboardCLI.setSelectedLeaderCard(cardsID[command - 1]);
                client.sendMessage(new Message(MessageType.LEADERCARD_SELECTED_CARD, command - 1));
            }
        }

    }

    @Override
    public void leaderCardAlreadyActivated(boolean action) {
        out.println("\nLeader card has already been activated.\n");
        if (action)
            client.sendMessage(new Message(MessageType.LEADERCARD_ANOTHER_ACTIVATE));
        else
            askAnotherDiscardLeaderCard();
    }

    @Override
    public void leaderCardNotEnoughResources() {
        out.println("\nYou don't have enough resources for activate leader card.\n");
        client.sendMessage(new Message(MessageType.LEADERCARD_ANOTHER_ACTIVATE));
    }

    @Override
    public void leaderCardNotEnoughColorCardQuantity() {
        out.println("\nYou don't have enough card of the required color.\n");
        client.sendMessage(new Message(MessageType.LEADERCARD_ANOTHER_ACTIVATE));
    }

    @Override
    public void askActivateAnotherLeaderCard() {
        response = checkInputYn(ColorCLI.ANSI_YELLOW + "\u1405 Do you want to activate another card? " + ColorCLI.ANSI_BLUE + "[Y/n]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        if (response.equals("Y") || response.equals("y")) {
            askActivableCard();
        } else {
            if (this.actionCompleted) {
                chooseInitialAction(true, true, new Message(new boolean[]{false, false}));
            } else {
                chooseInitialAction(false, true, new Message(legalAction));
            }
        }
    }

    @Override
    public void leaderCardDiscardAction(int[] cardsID) {
        switch (cardsID.length) {
            case 0 -> out.println("You don't have any leader card.\n");
            case 1 -> {
                response = checkInputYn("This is your card:\n" + leaderCardCLI.printLeaderCardByID(cardsID[0]) + ColorCLI.ANSI_YELLOW + "\nDo you want to discard it? " + ColorCLI.ANSI_BLUE + "[Y/n]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                switch (response) {
                    case "Y", "y" -> {
                        gameboardCLI.setSelectedLeaderCard(cardsID[0]);
                        client.sendMessage(new Message(MessageType.LEADERCARD_DISCARD_ACTION, 0));
                    }
                    default -> leaderCardEndAction(null);
                }
            }
            case 2 -> {
                out.println("\n" + leaderCardCLI.getLeaderCardArray(cardsID, true));
                out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Choose which card do you want to discard:\n" + ColorCLI.ANSI_BLUE + "1)" + ColorCLI.RESET + " the first one.\n" + ColorCLI.ANSI_BLUE + "2)" + ColorCLI.RESET + " the second one.\n" + ColorCLI.ANSI_YELLOW + "Your choice " + ColorCLI.ANSI_BLUE + "[1/2]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                command = validateInput(1, 2, "");
                if (command == -1)
                    chooseInitialAction(actionCompleted, false, new Message(legalAction));
                else
                    gameboardCLI.setSelectedLeaderCard(cardsID[command - 1]);
                client.sendMessage(new Message(MessageType.LEADERCARD_DISCARD_ACTION, command - 1));
            }
        }
    }

    @Override
    public void askAnotherDiscardLeaderCard() {
        response = checkInputYn(ColorCLI.ANSI_YELLOW + "\n\u1405 Do you want to discard another leader card? " + ColorCLI.ANSI_BLUE + "[Y/n]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        if (response.equals("Y") || response.equals("y")) {
            client.sendMessage(new Message(MessageType.LEADERCARD_SHOW_DISCARD_OPTION));
        } else if (response.equals("quit")) {
            chooseInitialAction(actionCompleted, false, new Message(legalAction));
        } else {
            if (this.actionCompleted) {
                chooseInitialAction(true, true, new Message(new boolean[]{false, false}));
            } else {
                chooseInitialAction(false, true, new Message(legalAction));
            }
        }
    }

    @Override
    public void leaderCardEndAction(String message) {
        if (message != null)
            out.println(message);
        chooseInitialAction(actionCompleted, true, new Message(legalAction));
    }


    // PRODUCTION METHODS

    @Override
    public void callProductionAction() {
        client.sendMessage(new Message(MessageType.PRODUCTION_OPTIONS));
    }

    @Override
    public void displayProductionAction(boolean[] legalAction) {
        int input;
        printGameboard();
        String string1 = "Activate card production";
        String string2 = "Activate leader card production";
        String string3 = "Activate basic production";

        StringBuilder displayedTest = new StringBuilder();
        displayedTest.append(ColorCLI.ANSI_YELLOW).append("\u1405 Choose which action to make by writing the associated number:").append(ColorCLI.RESET);
        if (legalAction[0])
            displayedTest.append(ColorCLI.ANSI_BLUE).append("\n1) ").append(ColorCLI.RESET).append(string1);
        else
            displayedTest.append(ColorCLI.ANSI_BLUE).append("\n1) ").append(ColorCLI.RESET).append(string1.replaceAll("", "\u0336"));

        if (legalAction[1])
            displayedTest.append(ColorCLI.ANSI_BLUE).append("\n2) ").append(ColorCLI.RESET).append(string2);
        else
            displayedTest.append(ColorCLI.ANSI_BLUE).append("\n2) ").append(ColorCLI.RESET).append(string2.replaceAll("", "\u0336"));

        if (legalAction[2])
            displayedTest.append(ColorCLI.ANSI_BLUE).append("\n3) ").append(ColorCLI.RESET).append(string3);
        else
            displayedTest.append(ColorCLI.ANSI_BLUE).append("\n3) ").append(ColorCLI.RESET).append(string3.replaceAll("", "\u0336"));

        displayedTest.append(ColorCLI.ANSI_BLUE).append("\n4) ").append(ColorCLI.RESET).append("Terminate production action");
        displayedTest.append("\nYour choice > ");

        out.printf(displayedTest.toString());

        while (true) {
            input = validateInput(0, 4, "");
            if (input == 1 && legalAction[0]) {
                checkDevelopmentCardProductionSlot();
                break;
            } else if (input == 2 && legalAction[1]) {
                client.sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD));
                break;
            } else if (input == 3 && legalAction[2]) {
                client.sendMessage(new Message(MessageType.PRODUCTION_BASICPRODUCTION));
                break;
            } else if (input == 4) {
                client.sendMessage(new Message(MessageType.PRODUCTION_ACTIVATION));
                break;
            } else {
                out.println("Invalid action, please insert a number corresponding to a white action");
            }
        }
    }

    @Override
    public void checkDevelopmentCardProductionSlot() {
        int input;
        out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Choose the slot of the card to activate" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        input = validateInput(1, 3, "");
        client.sendMessage(new Message(MessageType.PRODUCTION_DEVELOPMENTCARD, input - 1));
    }

    @Override
    public void chooseLeaderCardProductionIndex() {
        int input;
        out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Choose which leader card to activate" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        input = validateInput(1, 2, "");
        client.sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD_INDEX, input - 1));
    }

    @Override
    public void chooseLeaderCardProductionResourceType() {
        String input;
        out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Choose the type of resource which will be produced" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        input = in.nextLine();
        while (utils.checkResourceType(input) == null) {
            out.println("Please enter a valid input.\nChoose the type of resource which will be produced");
            input = in.nextLine();
        }
        input = utils.checkResourceType(input);
        client.sendMessage(new Message(MessageType.PRODUCTION_LEADERCARD_RESOURCE_INPUT, input));
    }

    @Override
    public void chooseBasicProductionResourceType() {
        String input;
        out.printf("Insert the 2 types of the resources in input and the 1 desired type in output. [format: Resource1, Resource2, Resource3] > ");
        while (true) {
            try {
                input = basicProductionResourceTypeInputCheck();
                break;
            } catch (IllegalInputException e) {
                out.printf("Input error! The format must be [Resource1, Resource2, Resource3]. Please, try again:");
            }
        }
        client.sendMessage(new Message(MessageType.PRODUCTION_BASIC_RESOURCES_INPUT, input));
    }

    private String basicProductionResourceTypeInputCheck() throws IllegalInputException {
        String input;
        StringBuilder resources = new StringBuilder();
        input = in.nextLine();
        List<String> resourceTypes = Arrays.asList(input.split(",[ ]*"));
        if (resourceTypes.size() != 3) {
            throw new IllegalInputException();
        }
        for (String resourceType : resourceTypes) {
            if (utils.checkResourceType(resourceType) == null) {
                throw new IllegalInputException();
            }
            resources.append(utils.checkResourceType(resourceType)).append(" ");
        }
        return resources.toString();
    }

    // PURCHASE METHODS

    @Override
    public void chooseCardToPurchase(Integer[] cards) {
        int input = 0;
        boolean condition = true;
        List<Integer> cardList = Arrays.asList(cards);
        printGameboard();
        out.println(developmentCardShopCLI.getDevelopmentCardMatrix(cardList));
        out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Choose one of the displayed card by inserting the correct number, for more information write \"help\"" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        while (condition) {
            input = validateInput(1, 12, "chooseCardToPurchase") - 1;
            // to discuss how to handle quit
            if (input == -2) {
                out.printf("Input error! You must write a number > ");
            } else {
                if (!cardList.get(input).equals(-1) && !cardList.get(input).equals(0)) {
                    condition = false;
                }
                if (condition) {
                    out.printf("Input error! The card must be one of the displayed ones > ");
                }
            }
        }
        client.sendMessage(new Message(MessageType.PURCHASED_CARD, cardList.get(input)));
    }

    @Override
    public void chooseCardSlot(List<Integer> slotList) {
        int input = 0;
        boolean condition = true;
        out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Choose in which of these slots to put the purchased development card: " + ColorCLI.ANSI_BLUE + slotList + ColorCLI.ANSI_YELLOW + "\nYour choice" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        while (condition) {
            input = validateInput(1, 3, "");
            // to discuss how to handle quit
            for (Integer n : slotList) {
                if (n == input) {
                    condition = false;
                    break;
                }
            }
            if (condition) {
                out.println("Input error! The value must be one of the number listed " + slotList);
            }
        }
        client.sendMessage(new Message(MessageType.PURCHASE_CHOOSE_SLOT, input - 1));
    }


    /* Take Resources from Market actions*/
    @Override
    public void showMarket() {
        out.println(marketCLI.printMarket());
    }

    @Override
    public void chooseMarketLine() {
        printGameboard();
        showMarket();
        out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Choose if you want to buy a row or a column of the market: " + ColorCLI.ANSI_BLUE + "[r/c]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        String line = in.nextLine();
        while (!line.equals("r") && !line.equals("R") && !line.equals("c") && !line.equals("C")) {
            out.printf("Error! Please enter a valid input. [r/c] > ");
            line = in.nextLine();
        }
        int number;
        if (line.equals("r") || line.equals("R")) {
            out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Select the row you want to buy. " + ColorCLI.ANSI_BLUE + "[1-3]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            number = validateInput(1, 3, "marketLine");
        } else {
            out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Select the column you want to buy." + ColorCLI.ANSI_BLUE + " [1-4]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            number = validateInput(1, 4, "marketLine");
        }
        number--;
        client.sendMessage(new Message(MessageType.MARKET_CHOICE, line + " " + number));
    }

    @Override
    public void chooseBlankAbility(int numBlank) {
        int[] leader = new int[numBlank];
        for (int i = 0; i < numBlank; i++) {
            out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Select the Leader Card to use for this blank marble." + ColorCLI.ANSI_BLUE + " [1-2]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            leader[i] = validateInput(1, 2, "");
        }
        client.sendMessage(new Message(MessageType.BLANKMARBLE_CHOICE, leader));
    }

    /**
     * Check input from player for insert resources in store.
     *
     * @param n number of resources to check.
     *          1 with one leader card depot ability.
     *          2 with one leader card depot ability.
     *          3 with warehouse.
     * @return the resources string in the correct format.
     * @throws IllegalInputException
     */
    public String checkStoreInput(int n) throws IllegalInputException {
        String input;
        StringBuilder resources = new StringBuilder();
        input = in.nextLine();
        if (input.equalsIgnoreCase("h") || input.equalsIgnoreCase("help")) {
            if (n == 3) {
                out.println(utils.displayHelp("manageWarehouse"));
                out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Insert the new warehouse. " + ColorCLI.ANSI_BLUE + "[format: N Resource1, N Resource2, N Resource3]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            } else {
                out.println(utils.displayHelp("manageLeaderDepot"));
                if (n == 1)
                    out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Insert the new leader depot: " + ColorCLI.ANSI_BLUE + "[format: N ResourceLeader]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
                else
                    out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Insert the new leader depots:" + ColorCLI.ANSI_BLUE + " [format: N ResourceLeader1, N ResourceLeader2]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            }
            input = in.nextLine();
        }
        List<String> resource = Arrays.asList(input.split(","));
        if (resource.size() != n) {
            throw new IllegalInputException();
        }
        int numberResource;
        for (String res : resource) {
            if (res.contains("-") || res.toLowerCase().contains("null"))
                resources.append("null").append(" ");
            else {
                if (!res.matches(".*\\d.*"))
                    throw new IllegalInputException();
                numberResource = Integer.parseInt(res.replaceAll("[^0-9]", ""));
                String resourceType = res.replaceAll("[^a-zA-Z]", "");
                if (utils.checkResourceType(resourceType) == null || numberResource < 1 || numberResource > 3)
                    throw new IllegalInputException();
                resources.append(numberResource).append(utils.checkResourceType(resourceType)).append(" ");
            }
        }
        return resources.toString();
    }

    @Override
    public void manageStores(int numDepotLeader) {
        out.println(ColorCLI.ANSI_YELLOW + "\u1405 Please put the purchased resources in the stores.\nWrite HELP for more information.");
        String input;
        out.printf("Insert the new warehouse." + ColorCLI.ANSI_BLUE + " [format: N Resource1, N Resource2, N Resource3]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
        while (true) {
            try {
                input = checkStoreInput(3);
                break;
            } catch (IllegalInputException e) {
                out.printf("Input error! The format must be [N Resource1, N Resource2, N Resource3]. Please, try again > ");
            }
        }
        if (numDepotLeader > 0) {
            if (numDepotLeader == 1)
                out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Insert the new leader depot:" + ColorCLI.ANSI_BLUE + " [format: N ResourceLeader]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            else
                out.printf(ColorCLI.ANSI_YELLOW + "\u1405 Insert the new leader depots: " + ColorCLI.ANSI_BLUE + "[format: N ResourceLeader1, N ResourceLeader2]" + ColorCLI.ANSI_RED + " > " + ColorCLI.RESET);
            while (true) {
                try {
                    input += checkStoreInput(numDepotLeader);
                    break;
                } catch (IllegalInputException e) {
                    if (numDepotLeader == 1)
                        out.printf("Input error! The format must be [N ResourceLeader]. Please, try again > ");
                    else
                        out.printf("Input error! The format must be [N ResourceLeader1, N ResourceLeader2]. Please, try again > ");
                }
            }
        }
        client.sendMessage(new Message(MessageType.ARRANGED_RESOURCES, input));
    }

    @Override
    public void displayPurchasedResources(String purchasedResource) {
        StringBuilder print = new StringBuilder();
        String[] resources = purchasedResource.split(" ");
        for (String resource : resources)
            print.append(ColorCLI.setColorResource(resource)).append(" ");
        out.println("Purchased resources: " + print);
    }

    @Override
    public void displayDiscardedResources(String discardedResource) {
        StringBuilder print = new StringBuilder();
        String[] resources = discardedResource.split(" ");
        for (String resource : resources)
            print.append(ColorCLI.setColorResource(resource)).append(" ");
        out.println("Discarded resources: " + print);
    }

    /* Single Player */
    @Override
    public void showActionToken(String token) {
        out.print("\nAction token revealed: ");
        if (token.equals("1"))
            out.println("(+1 ♰\uD83D\uDD04)");
        else if (token.equals("2"))
            out.println("(+2 ♰)");
        else
            out.println("(-2 " + ColorCLI.setColorFlag("0", token.replaceAll("\"", "")) + ")");

        out.println("\nYour turn is ended");
    }

    public String showLorenzoFaithTrack() {
        return "      ─────────────────────────────────────────────────────────── FAITH TRACK OF LORENZO IL MAGNIFICO ────────────────────────────────────────────────────────\n" + lorenzoFaithTrack.printFaithTrack(true) + "\n";
    }

    @Override
    public void setLorenzoFaithTrack() {
        lorenzoFaithTrack = new FaithTrackCLI();
    }

    @Override
    public void endGame(String leaderBoard) {
        out.println(leaderBoard);
        client.sendMessage(new Message(MessageType.END_GAME));
        System.exit(0);
    }
}


