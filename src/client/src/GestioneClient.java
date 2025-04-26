package client.src;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GestioneClient {
    // client TCP/UDP per inviare comandi al ServerStrutture

    private final String serverIP;
    private final int portaTcp;
    private final int portaUdp;
    private final String protocollo;
    private static final int UDP_BUFFER = 1024;
    private static final String UDP_END_MARKER = "__END__";

    public GestioneClient(String serverIP, int portaTcp, int portaUdp, String protocollo) {
        this.serverIP   = serverIP;
        this.portaTcp   = portaTcp;
        this.portaUdp   = portaUdp;
        this.protocollo = protocollo;
    }

    public void avvia() {
        if (protocollo.equalsIgnoreCase("tcp")) {
            gestisciTCP();
        } else {
            gestisciUDP();
        }
    }


    private void gestisciTCP() {
        try (
                Socket socket = new Socket(serverIP, portaTcp);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("[TCP] Connesso a " + serverIP + ":" + portaTcp);
            stampaHelp();
            while (true) {
                System.out.print(">>> ");
                String cmd = scanner.nextLine().trim();
                if ("exit".equalsIgnoreCase(cmd)) break;
                if ("help".equalsIgnoreCase(cmd)) { stampaHelp(); continue; }

                out.println(cmd);
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    if (!in.ready()) break;
                }
            }
            System.out.println("[TCP] Disconnesso.");
        } catch (IOException e) {
            System.err.println("[TCP] Errore: " + e.getMessage());
        }
    }

    private void gestisciUDP() {
        try (
                DatagramSocket socket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in)
        ) {
            InetAddress indirizzo = InetAddress.getByName(serverIP);
            byte[] buffer = new byte[UDP_BUFFER];

            System.out.println("[UDP] Socket pronto su " + serverIP + ":" + portaUdp);
            stampaHelp();

            // Invia help iniziale per far loggare la connessione al server
            byte[] initial = "help".getBytes();
            socket.send(new DatagramPacket(initial, initial.length, indirizzo, portaUdp));

            while (true) {
                System.out.print(">>> ");
                String cmd = scanner.nextLine().trim();
                if (cmd.equalsIgnoreCase("exit")) break;
                if (cmd.equalsIgnoreCase("help")) {
                    stampaHelp();
                    continue;
                }

                // invia comando
                byte[] outData = cmd.getBytes();
                socket.send(new DatagramPacket(outData, outData.length, indirizzo, portaUdp));

                // riceve risposta frammentata
                while (true) {
                    DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
                    socket.receive(resp);
                    String part = new String(resp.getData(), 0, resp.getLength());
                    if (UDP_END_MARKER.equals(part)) {
                        break;
                    }
                    System.out.print(part);
                }
                System.out.println();
            }
            System.out.println("[UDP] Disconnesso.");
        } catch (IOException e) {
            System.err.println("[UDP] Errore: " + e.getMessage());
        }
    }

    private void stampaHelp() {
        System.out.println("""
            Comandi disponibili:
            - tutti, num_strutture, get_row <n>
            - comuni, tipologie
            - filtra comune:<nome>
            - filtra tipologia:<tipo>
            - filtra provincia:<sigla>
            - filtra stelle:<n>
            - filtra atl:<atl>
            - filtra marchio:<Q/Yes/Ecolabel>
            - disabili, aria condizionata, carte, animali, parcheggio
            - cerca nome:<parola>
            - help → Mostra questo elenco
            - exit → Termina la connessione
            """);
    }
}
