package server.src;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * ServerStrutture implementa un server multi-protocollo (TCP e UDP)
 * per gestire richieste relative alle strutture ricettive.
 * Utilizza un thread pool per gestire connessioni concorrenti.
 */
public class ServerStrutture {
    /** Porta di ascolto per connessioni TCP */
    private final int portaTcp;
    /** Porta di ascolto per richieste UDP */
    private final int portaUdp;
    /** GestoreCSV che contiene e filtra i dati delle strutture */
    private final GestoreCSV gestore;
    /** Pool di thread per gestire clienti e richieste in parallelo */
    private final ExecutorService threadPool;

    /**
     * Costruisce un server specificando il percorso del CSV e le porte.
     *
     * @param csvPath percorso del file CSV delle strutture
     * @param portaTcp porta TCP per nuove connessioni
     * @param portaUdp porta UDP per richieste datagram
     */
    public ServerStrutture(String csvPath, int portaTcp, int portaUdp) {
        this.portaTcp = portaTcp;
        this.portaUdp = portaUdp;
        // Inizializza il gestore dei dati
        this.gestore = new GestoreCSV(csvPath);
        // Thread pool con numero variabile di thread
        this.threadPool = Executors.newCachedThreadPool();
    }

    /**
     * Metodo main: avvia il server specificando eventuali argomenti
     * per il file CSV e le porte. Se non forniti, usa valori di default.
     *
     * @param args [0]=path CSV, [1]=porta TCP, [2]=porta UDP
     */
    public static void main(String[] args) {
        String defaultCsv = "src/server/Regione-Piemonte---Elenco-delle-strutture-ricettive.csv";
        // Percorso CSV da argomenti o default
        String csvPath = args.length >= 1 ? args[0] : defaultCsv;
        // Porte da argomenti o costanti del protocollo
        int tcpPort = args.length >= 2 ? Integer.parseInt(args[1]) : Protocollo.TCP_PORT;
        int udpPort = args.length >= 3 ? Integer.parseInt(args[2]) : Protocollo.UDP_PORT;

        System.out.println("CSV: " + csvPath);
        System.out.println("Porte: TCP=" + tcpPort + "  UDP=" + udpPort);

        // Crea e avvia il server
        new ServerStrutture(csvPath, tcpPort, udpPort).avvia();
    }

    /**
     * Avvia i server TCP e UDP in thread separati.
     */
    public void avvia() {
        // Avvia listener TCP
        threadPool.execute(this::avviaServerTCP);
        // Avvia listener UDP
        threadPool.execute(this::avviaServerUDP);
        System.out.println("Server avviato su TCP " + portaTcp + " e UDP " + portaUdp);
    }

    /**
     * Ascolta e gestisce connessioni TCP in modo sincrono,
     * delegando ogni socket a un handler separato.
     */
    private void avviaServerTCP() {
        try (ServerSocket ss = new ServerSocket(portaTcp)) {
            while (true) {
                Socket client = ss.accept();
                // Log di connessione
                System.out.println("[TCP] Connessione ricevuta da "
                        + client.getInetAddress().getHostAddress()
                        + ":" + client.getPort());
                // Gestione in thread separato
                threadPool.execute(new ThreadClientHandler(client, gestore));
            }
        } catch (IOException e) {
            // Errore di I/O sul ServerSocket
            System.err.println("[TCP] Errore: " + e.getMessage());
        }
    }

    /**
     * Ascolta richieste UDP e le elabora in parallelo.
     * Invia risposte in pacchetti frammentati se necessario.
     */
    private void avviaServerUDP() {
        try (DatagramSocket socket = new DatagramSocket(portaUdp)) {
            byte[] buf = new byte[Protocollo.UDP_CHUNK_SIZE];
            while (true) {
                DatagramPacket req = new DatagramPacket(buf, buf.length);
                socket.receive(req);
                // Log della richiesta ricevuta
                System.out.println("[UDP] Richiesta da "
                        + req.getAddress().getHostAddress()
                        + ":" + req.getPort()
                        + "  comando=\""
                        + new String(req.getData(), 0, req.getLength(), StandardCharsets.UTF_8).trim()
                        + "\"");
                // Elabora in un thread separato
                threadPool.execute(() -> handleUdpRequest(socket, req));
            }
        } catch (IOException e) {
            // Errore di I/O sul DatagramSocket
            System.err.println("[UDP] Errore: " + e.getMessage());
        }
    }

    /**
     * Gestisce una singola richiesta UDP: elabora il comando e invia la risposta,
     * frammentandola in chunk di dimensione massima.
     *
     * @param ds riferimento al DatagramSocket per invio
     * @param req pacchetto di richiesta ricevuto
     */
    private void handleUdpRequest(DatagramSocket ds, DatagramPacket req) {
        String cmd = new String(req.getData(), 0, req.getLength(), StandardCharsets.UTF_8).trim();
        String risposta;
        if (cmd.equalsIgnoreCase(Protocollo.HELP)) {
            // Comando di aiuto: messaggio predefinito
            risposta = Protocollo.getHelpMessage();
        } else {
            // Qualsiasi altro comando viene processato staticamente
            risposta = elaboraRichiestaStatic(gestore, cmd);
        }

        byte[] replyBytes = risposta.getBytes(StandardCharsets.UTF_8);
        int offset = 0;
        // Frammentazione della risposta in più pacchetti se troppo lunga
        while (offset < replyBytes.length) {
            int len = Math.min(Protocollo.UDP_CHUNK_SIZE, replyBytes.length - offset);
            DatagramPacket part = new DatagramPacket(
                    replyBytes, offset, len,
                    req.getAddress(), req.getPort()
            );
            try {
                ds.send(part);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            offset += len;
        }
        // Invio del marker di fine
        byte[] endBuf = "__END__".getBytes(StandardCharsets.UTF_8);
        DatagramPacket endPacket = new DatagramPacket(
                endBuf, endBuf.length,
                req.getAddress(), req.getPort()
        );
        try {
            ds.send(endPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Esegue il parsing e l'elaborazione del comando ricevuto,
     * instradandolo ai metodi di GestoreCSV.
     *
     * @param g    gestore dei dati delle strutture
     * @param cmd  comando da elaborare (già trimmato e lowercase)
     * @return stringa di risposta pronta per invio
     */
    public static String elaboraRichiestaStatic(GestoreCSV g, String cmd) {
        cmd = cmd.trim().toLowerCase();
        try {
            if (cmd.equals(Protocollo.TUTTI)) {
                return g.getTutteLeStrutture().stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.equals(Protocollo.NUM_STRUTTURE)) {
                return "Numero strutture: " + g.getNumeroStrutture();
            } else if (cmd.equals(Protocollo.COMUNI)) {
                return String.join(", ", g.getComuniPresenti());
            } else if (cmd.equals(Protocollo.TIPOLOGIE)) {
                return String.join(", ", g.getTipologiePresenti());
            } else if (cmd.startsWith(Protocollo.GET_ROW)) {
                try {
                    int n = Integer.parseInt(cmd.replace(Protocollo.GET_ROW, "").trim());
                    if (n <= 0) {
                        return Protocollo.ERRORE_RIGA;
                    }
                    return g.getRiga(n - 1);
                } catch (NumberFormatException e) {
                    return Protocollo.ERRORE_RIGA;
                }
            } else if (cmd.startsWith(Protocollo.FILTRA_COMUNE)) {
                String c = cmd.replace(Protocollo.FILTRA_COMUNE, "").trim();
                return g.filtraPerComune(c).stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.startsWith(Protocollo.FILTRA_TIPOLOGIA)) {
                String t = cmd.replace(Protocollo.FILTRA_TIPOLOGIA, "").trim();
                return g.filtraPerTipologia(t).stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.startsWith(Protocollo.FILTRA_PROVINCIA)) {
                String p = cmd.replace(Protocollo.FILTRA_PROVINCIA, "").trim();
                return g.filtraPerProvincia(p).stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.startsWith(Protocollo.FILTRA_STELLE)) {
                String s = cmd.replace(Protocollo.FILTRA_STELLE, "").trim();
                return g.filtraPerStelle(s).stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.startsWith(Protocollo.FILTRA_ATL)) {
                String atl = cmd.replace(Protocollo.FILTRA_ATL, "").trim();
                return g.filtraPerATL(atl).stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.startsWith(Protocollo.FILTRA_MARCHIO)) {
                String m = cmd.replace(Protocollo.FILTRA_MARCHIO, "").trim();
                return g.filtraPerMarchio(m).stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.equals(Protocollo.DISABILI)) {
                return g.filtraAccessibiliDisabili().stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.equals(Protocollo.ARIA_CONDIZIONATA)) {
                return g.filtraConAriaCondizionata().stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.equals(Protocollo.CARTE)) {
                return g.filtraCheAccettanoCarte().stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.equals(Protocollo.ANIMALI)) {
                return g.filtraCheAccettanoAnimali().stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.equals(Protocollo.PARCHEGGIO)) {
                return g.filtraCheHannoParcheggio().stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else if (cmd.startsWith(Protocollo.CERCA_NOME)) {
                String kw = cmd.replace(Protocollo.CERCA_NOME, "").trim();
                return g.filtraPerNome(kw).stream()
                        .map(StrutturaRicettiva::toString)
                        .collect(Collectors.joining("\n"));
            } else {
                // Comando non riconosciuto
                return Protocollo.ERRORE_COMANDO;
            }
        } catch (NumberFormatException e) {
            // Errore di parsing numerico
            return Protocollo.ERRORE_RIGA;
        }
    }
}