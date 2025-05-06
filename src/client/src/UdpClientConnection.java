package client.src;

import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

/**
 * UdpClientConnection implementa l'interfaccia Connection per comunicare
 * con il server in modalità UDP. Invia comandi tramite DatagramPacket
 * e riceve la risposta in pacchetti multipli fino al marker di fine.
 */
public class UdpClientConnection implements Connection {
    /** Logger per messaggi di diagnostica e warning */
    private static final Logger LOGGER = Logger.getLogger(UdpClientConnection.class.getName());
    /** Timeout di default in millisecondi per operazioni di receive */
    private static final int DEFAULT_TIMEOUT_MS = 2000;
    /** Dimensione massima del buffer UDP per pacchetto */
    private static final int MAX_PACKET_SIZE = 8192;
    /** Marker che indica la fine della trasmissione UDP */
    private static final String UDP_END_MARKER = "__END__";

    private final InetAddress address;   // Indirizzo IP del server
    private final int port;              // Porta UDP del server
    private final DatagramSocket socket; // Socket UDP utilizzato per invio e ricezione

    /**
     * Costruisce una nuova connessione UDP verso il server specificato.
     * Imposta il timeout di ricezione su DEFAULT_TIMEOUT_MS.
     *
     * @param serverIP indirizzo IP o hostname del server
     * @param port porta UDP del server
     * @throws SocketException se il socket non può essere creato
     * @throws UnknownHostException se l'indirizzo serverIP non è risolvibile
     */
    public UdpClientConnection(String serverIP, int port) throws SocketException, UnknownHostException {
        this.address = InetAddress.getByName(serverIP);
        this.port    = port;
        this.socket  = new DatagramSocket();
        this.socket.setSoTimeout(DEFAULT_TIMEOUT_MS);
    }

    /**
     * Invia un comando al server e riceve la risposta completa.
     * Il comando viene inviato in un pacchetto UDP. Riceve pacchetti
     * in loop fino a quando non incontra il marker __END__.
     * Il comando 'exit' chiude immediatamente il socket.
     *
     * @param cmd comando da inviare al server
     * @return risposta ricevuta dal server come stringa concatenata
     * @throws IOException se il socket è chiuso o si verifica errore di I/O
     */
    @Override
    public synchronized String sendCommand(String cmd) throws IOException {
        if (socket.isClosed()) {
            throw new IOException("UDP socket is closed");
        }

        // 1) Invio del comando in UTF-8
        byte[] outBuf = cmd.getBytes("UTF-8");
        socket.send(new DatagramPacket(outBuf, outBuf.length, address, port));

        // 2) Se il comando è 'exit', chiude la connessione
        if ("exit".equalsIgnoreCase(cmd.trim())) {
            close();
            return "[Disconnected]";
        }

        // 3) Riceve i pacchetti finché non trova UDP_END_MARKER
        StringBuilder sb = new StringBuilder();
        byte[] inBuf = new byte[MAX_PACKET_SIZE];
        while (true) {
            DatagramPacket packet = new DatagramPacket(inBuf, inBuf.length);
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException e) {
                LOGGER.warning("UDP receive timed out");
                break; // Termina se scade il timeout di ricezione
            }
            String chunk = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
            if (UDP_END_MARKER.equals(chunk)) {
                break; // Marker di fine raggiunto
            }
            sb.append(chunk);
        }

        return sb.toString();
    }

    /**
     * Restituisce true se il socket UDP è ancora aperto.
     *
     * @return true se connesso, false se chiuso
     */
    @Override
    public boolean isConnected() {
        return !socket.isClosed();
    }

    /**
     * Chiude il socket UDP se ancora aperto.
     */
    @Override
    public void close() {
        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
