import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStrutture {
    // main
    // classe server principale che si mette in ascolto delle connessioni client.

    private static final int PORTA_TCP = 1050;
    private static final int PORTA_UDP = 3030;
    private static final String PERCORSO_CSV = ".\\Regione-Piemonte---Elenco-delle-strutture-ricettive.css";

    private GestoreCSV gestoreCSV;
    private ExecutorService threadPool;

    public static void main(String[] args) {
        new ServerStrutture().avvia();
    }

    public ServerStrutture() {
        this.gestoreCSV = new GestoreCSV(PERCORSO_CSV);
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void avvia() {
        threadPool.execute(this::avviaServerTCP);
        threadPool.execute(this::avviaServerUDP);
        System.out.println("Server avviato su TCP porta " + PORTA_TCP + " e UDP porta " + PORTA_UDP);
    }

    private void avviaServerTCP() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA_TCP)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> gestisciClientTCP(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Errore nel server TCP: " + e.getMessage());
        }
    }

    private void gestisciClientTCP(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String richiesta;
            while ((richiesta = in.readLine()) != null) {
                String risposta = elaboraRichiesta(richiesta);
                out.println(risposta);
            }
        } catch (IOException e) {
            System.err.println("Errore nella comunicazione TCP: " + e.getMessage());
        }
    }

    private void avviaServerUDP() {
        try (DatagramSocket udpSocket = new DatagramSocket(PORTA_UDP)) {
            byte[] buffer = new byte[2048];
            while (true) {
                DatagramPacket richiesta = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(richiesta);
                threadPool.execute(() -> gestisciClientUDP(udpSocket, richiesta));
            }
        } catch (IOException e) {
            System.err.println("Errore nel server UDP: " + e.getMessage());
        }
    }

    private void gestisciClientUDP(DatagramSocket socket, DatagramPacket richiesta) {
        try {
            String comando = new String(richiesta.getData(), 0, richiesta.getLength());
            String risposta = elaboraRichiesta(comando);

            byte[] rispostaBytes = risposta.getBytes();
            DatagramPacket pacchettoRisposta = new DatagramPacket(
                    rispostaBytes,
                    rispostaBytes.length,
                    richiesta.getAddress(),
                    richiesta.getPort()
            );
            socket.send(pacchettoRisposta);
        } catch (IOException e) {
            System.err.println("Errore nella comunicazione UDP: " + e.getMessage());
        }
    }

    private String elaboraRichiesta(String comando) {
        comando = comando.trim().toLowerCase();
        switch (comando) {
            case "tutti":
                return String.join("\n", gestoreCSV.getTutteLeStrutture().stream()
                        .map(StrutturaRicettiva::toString)
                        .toList());
            case "num_strutture":
                return "Numero strutture: " + gestoreCSV.getNumeroStrutture();
            case "comuni":
                return String.join(", ", gestoreCSV.getComuniPresenti());
            case "tipologie":
                return String.join(", ", gestoreCSV.getTipologiePresenti());
            default:
                if (comando.startsWith("filtra comune:")) {
                    String comune = comando.replace("filtra comune:", "").trim();
                    return String.join("\n", gestoreCSV.filtraPerComune(comune).stream()
                            .map(StrutturaRicettiva::toString)
                            .toList());
                } else if (comando.startsWith("filtra tipologia:")) {
                    String tipo = comando.replace("filtra tipologia:", "").trim();
                    return String.join("\n", gestoreCSV.filtraPerTipologia(tipo).stream()
                            .map(StrutturaRicettiva::toString)
                            .toList());
                }
                return "Comando non riconosciuto.";
        }
    }

}
