public class GestioneClient {
    //Thread che gestisce le singole richieste dei client.

    private Socket socket;
    private GestoreCSV gestore;

    public GestioneClient(Socket socket, GestoreCSV gestore) {
        this.socket = socket;
        this.gestore = gestore;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Connessione stabilita con il server. Digita un comando:");

            String comando;
            while ((comando = in.readLine()) != null) {
                comando = comando.trim();
                if (comando.equalsIgnoreCase("EXIT")) {
                    out.println("Connessione chiusa.");
                    break;
                }

                String[] parti = comando.split(" ", 2);
                String azione = parti[0].toUpperCase();

                switch (azione) {
                    case "GET_ALL":
                        inviaLista(gestore.getTutteLeStrutture(), out);
                        break;

                    case "GET_BY_NAME":
                        if (parti.length < 2) {
                            out.println("Nome mancante.");
                        } else {
                            var struttura = gestore.cercaPerNome(parti[1]);
                            out.println(struttura != null ? struttura : "Struttura non trovata.");
                        }
                        break;

                    case "GET_BY_COMUNE":
                        if (parti.length < 2) {
                            out.println("Comune mancante.");
                        } else {
                            inviaLista(gestore.filtraPerComune(parti[1]), out);
                        }
                        break;

                    case "GET_BY_TIPOLOGIA":
                        if (parti.length < 2) {
                            out.println("Tipologia mancante.");
                        } else {
                            inviaLista(gestore.filtraPerTipologia(parti[1]), out);
                        }
                        break;

                    case "COUNT_BY_COMUNE":
                        inviaMappa(gestore.contaPerComune(), out);
                        break;

                    case "COUNT_BY_TIPOLOGIA":
                        inviaMappa(gestore.contaPerTipologia(), out);
                        break;

                    case "LIST_COMUNI":
                        gestore.getComuniPresenti().forEach(out::println);
                        break;

                    case "LIST_TIPOLOGIE":
                        gestore.getTipologiePresenti().forEach(out::println);
                        break;

                    case "GET":
                        if (parti.length < 2) {
                            out.println("Indice mancante.");
                        } else {
                            try {
                                int index = Integer.parseInt(parti[1]);
                                var s = gestore.getStruttura(index);
                                out.println(s != null ? s : "Indice non valido.");
                            } catch (NumberFormatException e) {
                                out.println("Indice non valido.");
                            }
                        }
                        break;

                    case "NUM_STRUTTURE":
                        out.println("Numero totale di strutture: " + gestore.getNumeroStrutture());
                        break;

                    default:
                        out.println("Comando non riconosciuto.");
                }

                out.println("---FINE---");
            }

        } catch (IOException e) {
            System.err.println("Errore nella gestione del client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void inviaLista(List<StrutturaRicettiva> lista, PrintWriter out) {
        if (lista.isEmpty()) {
            out.println("Nessuna struttura trovata.");
        } else {
            for (StrutturaRicettiva s : lista) {
                out.println(s);
                out.println(); // separatore visivo
            }
        }
    }

    private void inviaMappa(Map<String, Long> mappa, PrintWriter out) {
        if (mappa.isEmpty()) {
            out.println("Nessun dato disponibile.");
        } else {
            mappa.forEach((k, v) -> out.println(k + ": " + v));
        }
    }
}
