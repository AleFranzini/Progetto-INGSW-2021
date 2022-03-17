package it.polimi.ingsw;

import it.polimi.ingsw.network.client.ClientOffline;
import it.polimi.ingsw.network.client.ClientOnline;
import it.polimi.ingsw.network.server.Server;

import java.util.Scanner;

public class MasterOfRenaissance {
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        int input;
        System.out.println("Choose what to start:\n1) Server.\n2) Client Online.\n3) Client Offline.\n");
        do {
            System.out.print("Your choice > ");
            input = in.nextInt();
        } while (input != 1 && input != 2 && input != 3);
        switch (input) {
            case 1 -> Server.main(args);
            case 2 -> ClientOnline.main(null);
            case 3 -> ClientOffline.main(args);
        }
    }
}
