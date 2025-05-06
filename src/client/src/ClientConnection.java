package client.src;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

/**
 * ClientConnection gestisce la connessione TCP al server delle strutture ricettive.
 * Implementa l'interfaccia Connection, supporta timeout configurabili e invio/ricezione
 * di comandi e risposte tramite socket.
 */
public class ClientConnection implements Connection {
    // Logger per messaggi di diagnostica e warning
    private static final Logger LOGGER = Logger.getLogger(ClientConnection.class.getName());
    // Timeout di default in millisecondi per connessione e lettura
    private static final int DEFAULT_TIMEOUT_MS = 2000;

    private final String serverIP;         // Indirizzo IP o hostname del server
    private final int port;                // Porta di connessione TCP
    private Socket socket;                 // Socket TCP aperto
    private PrintWriter writer;            // Stream in uscita verso il server
    private BufferedReader reader;         // Stream in entrata dal server

    /**
     * Costruisce una connessione TCP al server con timeout di default.
     *
     * @param serverIP indirizzo IP o hostname del server
     * @param port porta del server
     * @throws IOException se la connessione o l'inizializzazione degli stream fallisce
     */
    public ClientConnection(String serverIP, int port) throws IOException {
        this(serverIP, port, DEFAULT_TIMEOUT_MS);
    }

    /**
     * Costruisce una connessione TCP al server con timeout specificato.
     *
     * @param serverIP indirizzo IP o hostname del server
     * @param port porta del server
     * @param timeoutMillis timeout di connessione e lettura in millisecondi
     * @throws IOException se la connessione o l'inizializzazione degli stream fallisce
     */
    public ClientConnection(String serverIP, int port, int timeoutMillis) throws IOException {
        this.serverIP = serverIP;
        this.port = port;
        // Avvia la connessione con il timeout indicato
        connect(timeoutMillis);
    }

    /**
     * Inizializza il socket, applica timeout e configura gli stream UTF-8.
     *
     * @param timeoutMillis timeout di connessione e lettura in millisecondi
     * @throws IOException se si verifica un errore di I/O
     */
    private void connect(int timeoutMillis) throws IOException {
        socket = new Socket();
        // Connessione al server con timeout per connect
        socket.connect(new InetSocketAddress(serverIP, port), timeoutMillis);
        // Imposta timeout per operazioni di lettura
        socket.setSoTimeout(timeoutMillis);
        // Configura lo stream in uscita con codifica UTF-8 e autoflush
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        // Configura lo stream in entrata con codifica UTF-8
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    }

    /**
     * Invia un comando al server e riceve la risposta completa.
     * Il comando 'exit' chiude la connessione.
     *
     * @param cmd comando da inviare
     * @return risposta ricevuta dal server (stringa multilinea)
     * @throws IOException se la connessione non è attiva o ci sono errori di I/O
     */
    @Override
    public synchronized String sendCommand(String cmd) throws IOException {
        // Verifica connessione
        if (!isConnected()) {
            throw new IOException("Not connected to server");
        }
        // Invia il comando al server
        writer.println(cmd);
        // Gestione chiusura su comando 'exit'
        if ("exit".equalsIgnoreCase(cmd.trim())) {
            close();
            return "[Disconnected]";
        }
        // Accumula la risposta riga per riga
        StringBuilder response = new StringBuilder();
        String line;
        while (true) {
            try {
                line = reader.readLine();
                // Fine se stream chiuso o riga vuota (delimitatore)
                if (line == null || line.isEmpty()) {
                    break;
                }
                response.append(line).append(System.lineSeparator());
            } catch (SocketTimeoutException e) {
                // Timeout di lettura: consideriamo terminata la risposta
                break;
            }
        }
        return response.toString();
    }

    /**
     * Controlla se il socket è connesso e aperto.
     *
     * @return true se connesso, false altrimenti
     */
    @Override
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    /**
     * Chiude socket e stream, loggando eventuali errori.
     */
    @Override
    public void close() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            LOGGER.warning("Error closing TCP connection: " + e.getMessage());
        }
    }
}