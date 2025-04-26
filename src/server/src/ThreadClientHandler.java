package server.src;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ThreadClientHandler extends Thread {
    // gestisce un singolo client TCP

    private final Socket client;
    private final GestoreCSV gestore;

    public ThreadClientHandler(Socket client, GestoreCSV gestore) {
        this.client  = client;
        this.gestore = gestore;
    }

    @Override
    public void run() {
        try (
                BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter    out = new PrintWriter(client.getOutputStream(), true)
        ) {
            // Messaggio di benvenuto e help iniziale
            out.println("Benvenuto nel server Strutture Ricettive!");
            out.println("Digita 'help' per l'elenco comandi, 'exit' per chiudere.");

            // Invio prompt
            out.print(">>> ");
            out.flush();

            String cmd;
            while ((cmd = in.readLine()) != null) {
                cmd = cmd.trim();

                if (cmd.equalsIgnoreCase("exit")) {
                    out.println("Chiusura connessione. Arrivederci!");
                    break;
                }

                String risposta;
                if (cmd.equalsIgnoreCase("help")) {
                    risposta = Protocollo.getHelpMessage();
                } else {
                    risposta = ServerStrutture.elaboraRichiestaStatic(gestore, cmd);
                }

                // invia risposta riga per riga
                for (String line : risposta.split("\\n")) {
                    out.println(line);
                }
                // nuovo prompt
                out.print(">>> ");
                out.flush();
            }
        } catch (Exception e) {
            // Gestione errori
        } finally {
            try { client.close(); } catch (Exception ignored) {}
        }
    }
}