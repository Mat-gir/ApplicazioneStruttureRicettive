public class ClientStrutture {
    //Applicazione lato client che permette all’utente di inviare richieste e visualizzare i risultati.

    private static final String SERVER_HOST = "localhost";
    private static final int TCP_PORT = 1050;
    private static final int UDP_PORT = 3030;

    private boolean usaTCP = true;

    public void scegliProtocollo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Usare TCP (T) o UDP (U)? ");
        String scelta = scanner.nextLine().trim().toUpperCase();
        usaTCP = scelta.equals("T");
    }

    public void inviaRichiesta(String richiesta) {
        if (usaTCP) {
            inviaTCP(richiesta);
        } else {
            inviaUDP(richiesta);
        }
    }

    public void inviaTCP(String richiesta) {
        try (Socket socket = new Socket(SERVER_HOST, TCP_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(richiesta);
            String risposta;
            while ((risposta = in.readLine()) != null) {
                gestisciRisposta(risposta);
            }
        } catch (IOException e) {
            System.err.println("Errore TCP: " + e.getMessage());
        }
    }

    public void inviaUDP(String richiesta) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = richiesta.getBytes();
            InetAddress address = InetAddress.getByName(SERVER_HOST);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, UDP_PORT);
            socket.send(packet);

            byte[] rispostaBuffer = new byte[8192];
            DatagramPacket rispostaPacket = new DatagramPacket(rispostaBuffer, rispostaBuffer.length);
            socket.receive(rispostaPacket);
            String risposta = new String(rispostaPacket.getData(), 0, rispostaPacket.getLength());
            gestisciRisposta(risposta);
        } catch (IOException e) {
            System.err.println("Errore UDP: " + e.getMessage());
        }
    }

    public void gestisciRisposta(String risposta) {
        System.out.println("Risposta dal server:");
        System.out.println(risposta);
    }

    public void inviaRichiestaSalvataggio(String percorsoCSV) {
        inviaRichiesta("ESPORTA:" + percorsoCSV);
    }

    public void inviaRichiestaAggiornamento() {
        inviaRichiesta("REFRESH");
    }

    public void inviaRichiestaTutteLeStrutture() {
        inviaRichiesta("TUTTE");
    }

    public void inviaRichiestaPerComune(String comune) {
        inviaRichiesta("COMUNE:" + comune);
    }

    public void inviaRichiestaPerTipologia(String tipologia) {
        inviaRichiesta("TIPOLOGIA:" + tipologia);
    }

    public void inviaRichiestaCercaNome(String nome) {
        inviaRichiesta("NOME:" + nome);
    }

    public void inviaRichiestaContaPerComune() {
        inviaRichiesta("CONTA_COMUNE");
    }

    public void inviaRichiestaContaPerTipologia() {
        inviaRichiesta("CONTA_TIPOLOGIA");
    }

    public void ottieniComuni() {
        inviaRichiesta("COMUNI_PRESENTI");
    }

    public void ottieniTipologie() {
        inviaRichiesta("TIPOLOGIE_PRESENTI");
    }

    public static void main(String[] args) {
        ClientStrutture client = new ClientStrutture();
        client.scegliProtocollo();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Inserisci richiesta (es. TUTTE, COMUNE:Torino, fine per uscire): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("fine")) break;
            client.inviaRichiesta(input);
        }
    }
}
