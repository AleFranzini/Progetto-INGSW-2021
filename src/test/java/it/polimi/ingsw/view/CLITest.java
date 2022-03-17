package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientOnline;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.UtilsCLI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLITest {
    private CLI cli;
    private UtilsCLI utils;
    private Client client = new ClientOnline();

    @BeforeEach
    public void setup() {
        cli = new CLI(client);
        utils = new UtilsCLI(cli);
    }

    @Test
    public void testPrintLogo() {
        System.out.println(utils.printLogo());
        assert true;
    }
}