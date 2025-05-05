package server.src;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * ThreadClientHandler gestisce la comunicazione con un singolo client TCP.
 * Estende Thread per consentire l'elaborazione concorrente delle richieste.
 */
public class ThreadClientHandler extends Thread {
    /** Socket del client connesso. */
    private final Socket client;
    /** GestoreCSV per eseguire operazioni di ricerca e filtro sui dati. */
    private final GestoreCSV gestore;

    /**
     * Costruisce un handler per il client specificato.
     *
     * @param client  socket del client
     * @param gestore istanza di GestoreCSV per l'accesso ai dati
     */
    public ThreadClientHandler(Socket client, GestoreCSV gestore) {
        this.client  = client;
        this.gestore = gestore;
    }

    /**
     * Punto di esecuzione del thread.
     * Apre gli stream di input/output, invia un messaggio di benvenuto,
     * gestisce i comandi 'help' ed 'exit', e inoltra altri comandi a ServerStrutture.
     */
    @Override
    public void run() {
        try (
                BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter    out = new PrintWriter(client.getOutputStream(), true)
        ) {
            // Messaggio di benvenuto
            out.println("Benvenuto nel server Strutture Ricettive!");
            // Istruzioni di base per l'utente
            out.println("Digita 'help' per l'elenco comandi, 'exit' per chiudere.");

            // Prompt iniziale
            out.print(">>> ");
            out.flush();

            String cmd;
            // Ciclo principale di lettura dei comandi
            while ((cmd = in.readLine()) != null) {
                cmd = cmd.trim();

                // Verifica comando di uscita
                if (cmd.equalsIgnoreCase("exit")) {
                    out.println("Chiusura connessione. Arrivederci!");
                    break;
                }

                String risposta;
                // Comando help: invia il messaggio di aiuto
                if (cmd.equalsIgnoreCase("help")) {
                    risposta = Protocollo.getHelpMessage();
                } else {
                    // Inoltra altri comandi al metodo statico di elaborazione
                    risposta = ServerStrutture.elaboraRichiestaStatic(gestore, cmd);
                }

                // Invia la risposta riga per riga
                for (String line : risposta.split("\\n")) {
                    out.println(line);
                }
                // Nuovo prompt dopo la risposta
                out.print(">>> ");
                out.flush();
            }
        } catch (Exception e) {
            // Gestione generica degli errori di I/O e rete
        } finally {
            try {
                client.close(); // Chiude la connessione socket
            } catch (Exception ignored) {
            }
        }
    }
}
