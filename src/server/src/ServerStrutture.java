package server.src;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class ServerStrutture {
    // server multithread TCP+UDP che elabora le richieste sul file CSV

    private static final int UDP_CHUNK_SIZE = 1024;
    private static final String UDP_END_MARKER = "__END__";

    private final int portaTcp;
    private final int portaUdp;
    private final GestoreCSV gestore;
    private final ExecutorService threadPool;

    public ServerStrutture(String csvPath, int portaTcp, int portaUdp) {
        this.portaTcp   = portaTcp;
        this.portaUdp   = portaUdp;
        this.gestore    = new GestoreCSV(csvPath);
        this.threadPool = Executors.newCachedThreadPool();
    }

    public static void main(String[] args) {
        String defaultCsv = "src/server/Regione-Piemonte---Elenco-delle-strutture-ricettive.csv";
        String csvPath    = args.length >= 1 ? args[0] : defaultCsv;
        int    tcpPort    = args.length >= 2 ? Integer.parseInt(args[1]) : 1050;
        int    udpPort    = args.length >= 3 ? Integer.parseInt(args[2]) : 3030;

        System.out.println("CSV: " + csvPath);
        System.out.println("Porte: TCP=" + tcpPort + "  UDP=" + udpPort);

        new ServerStrutture(csvPath, tcpPort, udpPort).avvia();
    }

    public void avvia() {
        threadPool.execute(this::avviaServerTCP);
        threadPool.execute(this::avviaServerUDP);
        System.out.println("Server avviato su TCP " + portaTcp + " e UDP " + portaUdp);
    }

    private void avviaServerTCP() {
        try (ServerSocket ss = new ServerSocket(portaTcp)) {
            while (true) {
                Socket client = ss.accept();
                // Log immediato su server
                System.out.println("[TCP] Connessione ricevuta da "
                        + client.getInetAddress().getHostAddress()
                        + ":" + client.getPort());
                threadPool.execute(new ThreadClientHandler(client, gestore));
            }
        } catch (IOException e) {
            System.err.println("[TCP] Errore: " + e.getMessage());
        }
    }

    private void avviaServerUDP() {
        try (DatagramSocket socket = new DatagramSocket(portaUdp)) {
            byte[] buf = new byte[2048];
            while (true) {
                DatagramPacket req = new DatagramPacket(buf, buf.length);
                socket.receive(req);
                // Log immediato su server
                System.out.println("[UDP] Richiesta da "
                        + req.getAddress().getHostAddress()
                        + ":" + req.getPort()
                        + "  comando=\""
                        + new String(req.getData(), 0, req.getLength()).trim()
                        + "\""
                );
                threadPool.execute(() -> gestisciClientUDP(socket, req));
            }
        } catch (IOException e) {
            System.err.println("[UDP] Errore: " + e.getMessage());
        }
    }

    private void gestisciClientUDP(DatagramSocket socket, DatagramPacket req) {
        try {
            String cmd = new String(req.getData(), 0, req.getLength()).trim();
            String risposta = elaboraRichiestaStatic(gestore, cmd);
            byte[] data = risposta.getBytes();
            int offset = 0;

            while (offset < data.length) {
                int len = Math.min(UDP_CHUNK_SIZE, data.length - offset);
                DatagramPacket chunk = new DatagramPacket(
                        data, offset, len,
                        req.getAddress(), req.getPort()
                );
                socket.send(chunk);
                offset += len;
            }

            byte[] end = UDP_END_MARKER.getBytes();
            socket.send(new DatagramPacket(end, end.length, req.getAddress(), req.getPort()));
        } catch (IOException e) {
            System.err.println("Errore comunicazione UDP: " + e.getMessage());
        }
    }

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
                    if (n <= 0) { return Protocollo.ERRORE_RIGA; }
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
                return Protocollo.ERRORE_COMANDO;
            }
        } catch (NumberFormatException e) {
            return Protocollo.ERRORE_RIGA;
        }
    }
}