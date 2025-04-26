package client.src;

import java.util.Scanner;

public class ClientStrutture {
    // entry point per il client

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("IP server [default 127.0.0.1]: ");
        String ip = scanner.nextLine().trim();
        if (ip.isEmpty()) ip = "127.0.0.1";

        System.out.print("Protocollo (tcp/udp) [default tcp]: ");
        String proto = scanner.nextLine().trim().toLowerCase();
        if (proto.isEmpty()) proto = "tcp";

        int portaTcp = 1050;
        int portaUdp = 3030;
        // possibile modifica per chiedere all'utente le porte

        GestioneClient client = new GestioneClient(ip, portaTcp, portaUdp, proto);
        client.avvia();
    }
}



