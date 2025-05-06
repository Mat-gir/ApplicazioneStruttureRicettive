package server.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GestoreCSV si occupa di caricare e gestire i dati delle strutture ricettive
 * da un file CSV. Fornisce metodi per filtrare, raggruppare e contare le strutture
 * in base a vari criteri, oltre a esportare i dati nuovamente in formato CSV.
 */
public class GestoreCSV {
    /**
     * Lista interna delle strutture lette dal CSV.
     */
    private final List<StrutturaRicettiva> strutture;

    /**
     * Costruisce un nuovo GestoreCSV e carica i dati dal file CSV specificato.
     *
     * @param csvPath percorso del file CSV contenente i dati delle strutture
     */
    public GestoreCSV(String csvPath) {
        strutture = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            int lineNum = 0;
            // Legge ogni riga del file CSV (incluse eventuali intestazioni)
            while ((line = br.readLine()) != null) {
                lineNum++;
                String[] campi = splitCSV(line);
                if (campi.length >= 28) {
                    // Crea e aggiunge una nuova StrutturaRicettiva con i campi letti
                    strutture.add(new StrutturaRicettiva(
                            campi[0],  campi[1],  campi[2],  campi[3],  campi[4],
                            campi[5],  campi[6],  campi[7],  campi[8],  campi[9],
                            campi[10], campi[11], campi[12], campi[13], campi[14],
                            campi[15], campi[16], campi[17], campi[18], campi[19],
                            campi[20], campi[21], campi[22], campi[23], campi[24],
                            campi[25], campi[26], campi[27]
                    ));
                } else {
                    // Logga righe con numero di campi non corretto
                    System.err.printf(
                            "Riga %d saltata: trovati %d campi anziché 28%n",
                            lineNum, campi.length
                    );
                }
            }
        } catch (IOException e) {
            // Gestione dell'eccezione in caso di errore di I/O sul file
            System.err.println("Errore durante la lettura del CSV: " + e.getMessage());
        }
    }

    /**
     * Splitta una riga CSV in campi, gestendo correttamente i valori tra virgolette
     * e il delimitatore ';'. I campi vuoti vengono sostituiti con "NON_PRESENTE".
     *
     * @param line riga del CSV da elaborare
     * @return array di stringhe contenente i campi
     */
    private String[] splitCSV(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ';' && !inQuotes) {
                String field = cur.toString().trim();
                result.add(field.isEmpty() ? "NON_PRESENTE" : field);
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        // Aggiunge l'ultimo campo residuo
        String last = cur.toString().trim();
        result.add(last.isEmpty() ? "NON_PRESENTE" : last);
        return result.toArray(new String[0]);
    }

    /**
     * Restituisce la rappresentazione formattata (toString) della riga alla posizione indicata.
     *
     * @param idx indice (0-based) della struttura
     * @return stringa CSV della struttura o codice di errore se indice non valido
     */
    public String getRiga(int idx) {
        if (idx >= 0 && idx < strutture.size()) {
            return strutture.get(idx).toString();
        } else {
            return Protocollo.ERRORE_RIGA;
        }
    }

    /**
     * Restituisce il numero totale di strutture caricate.
     *
     * @return conteggio delle strutture
     */
    public int getNumeroStrutture() {
        return strutture.size();
    }

    /**
     * Restituisce una copia della lista di tutte le strutture caricate.
     *
     * @return lista di StrutturaRicettiva
     */
    public List<StrutturaRicettiva> getTutteLeStrutture() {
        return new ArrayList<>(strutture);
    }

    /**
     * Filtra le strutture in base al comune.
     *
     * @param comune nome o chiave del comune
     * @return lista di strutture corrispondenti
     */
    public List<StrutturaRicettiva> filtraPerComune(String comune) {
        return strutture.stream()
                .filter(s -> s.haComune(comune))
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture in base alla provincia.
     *
     * @param provincia nome o chiave della provincia
     * @return lista di strutture corrispondenti
     */
    public List<StrutturaRicettiva> filtraPerProvincia(String provincia) {
        return strutture.stream()
                .filter(s -> s.haProvincia(provincia))
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture in base al numero di stelle.
     *
     * @param stelle valore delle stelle (es. "3")
     * @return lista di strutture con il rating specificato
     */
    public List<StrutturaRicettiva> filtraPerStelle(String stelle) {
        return strutture.stream()
                .filter(s -> s.haStelle(stelle))
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture in base all'ATL (Azienda Turistica Locale).
     *
     * @param atl denominazione dell'ATL
     * @return lista di strutture affiliate
     */
    public List<StrutturaRicettiva> filtraPerATL(String atl) {
        return strutture.stream()
                .filter(s -> atl.equalsIgnoreCase(s.getDenominazioneATL()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture in base alla tipologia.
     *
     * @param tipologia chiave della tipologia (es. "Hotel", "B&B")
     * @return lista di strutture corrispondenti
     */
    public List<StrutturaRicettiva> filtraPerTipologia(String tipologia) {
        return strutture.stream()
                .filter(s -> s.getChiaveTipologia().equalsIgnoreCase(tipologia))
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture in base al marchio.
     *
     * @param marchio marchio da ricercare
     * @return lista di strutture con il marchio indicato
     */
    public List<StrutturaRicettiva> filtraPerMarchio(String marchio) {
        return strutture.stream()
                .filter(s -> s.haMarchio(marchio))
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture accessibili ai disabili.
     *
     * @return lista di strutture con idoneità disabili
     */
    public List<StrutturaRicettiva> filtraAccessibiliDisabili() {
        return strutture.stream()
                .filter(StrutturaRicettiva::isIdoneitaDisabili)
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture dotate di aria condizionata in camere o appartamenti.
     *
     * @return lista di strutture con aria condizionata
     */
    public List<StrutturaRicettiva> filtraConAriaCondizionata() {
        return strutture.stream()
                .filter(s -> "SI".equalsIgnoreCase(s.getAriaCondizionataCamere())
                        || "SI".equalsIgnoreCase(s.getAriaCondizionataAppartamenti()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture che accettano carta di credito.
     *
     * @return lista di strutture che accettano carte
     */
    public List<StrutturaRicettiva> filtraCheAccettanoCarte() {
        return strutture.stream()
                .filter(StrutturaRicettiva::accettaCartaCredito)
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture pet-friendly.
     *
     * @return lista di strutture che accettano animali
     */
    public List<StrutturaRicettiva> filtraCheAccettanoAnimali() {
        return strutture.stream()
                .filter(StrutturaRicettiva::accettaAnimali)
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture dotate di parcheggio.
     *
     * @return lista di strutture con parcheggio
     */
    public List<StrutturaRicettiva> filtraCheHannoParcheggio() {
        return strutture.stream()
                .filter(StrutturaRicettiva::haParcheggio)
                .collect(Collectors.toList());
    }

    /**
     * Filtra le strutture il cui nome contiene la parola chiave fornita.
     *
     * @param keyword sottostringa da cercare nel nome
     * @return lista di strutture corrispondenti
     */
    public List<StrutturaRicettiva> filtraPerNome(String keyword) {
        return strutture.stream()
                .filter(s -> s.contieneNome(keyword))
                .collect(Collectors.toList());
    }

    /**
     * Restituisce l'insieme di tutti i comuni presenti nei dati.
     *
     * @return insieme dei nomi dei comuni
     */
    public Set<String> getComuniPresenti() {
        return strutture.stream()
                .map(StrutturaRicettiva::getChiaveComune)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Restituisce l'insieme di tutte le tipologie di struttura presenti.
     *
     * @return insieme delle tipologie
     */
    public Set<String> getTipologiePresenti() {
        return strutture.stream()
                .map(StrutturaRicettiva::getChiaveTipologia)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Raggruppa le strutture per comune.
     *
     * @return mappa da comune a lista di strutture
     */
    public Map<String, List<StrutturaRicettiva>> raggruppaPerComune() {
        return strutture.stream()
                .collect(Collectors.groupingBy(StrutturaRicettiva::getChiaveComune));
    }

    /**
     * Raggruppa le strutture per tipologia.
     *
     * @return mappa da tipologia a lista di strutture
     */
    public Map<String, List<StrutturaRicettiva>> raggruppaPerTipologia() {
        return strutture.stream()
                .collect(Collectors.groupingBy(StrutturaRicettiva::getChiaveTipologia));
    }

    /**
     * Conta il numero di strutture per comune.
     *
     * @return mappa da comune a conteggio
     */
    public Map<String, Long> contaPerComune() {
        return strutture.stream()
                .collect(Collectors.groupingBy(StrutturaRicettiva::getChiaveComune, Collectors.counting()));
    }

    /**
     * Conta il numero di strutture per tipologia.
     *
     * @return mappa da tipologia a conteggio
     */
    public Map<String, Long> contaPerTipologia() {
        return strutture.stream()
                .collect(Collectors.groupingBy(StrutturaRicettiva::getChiaveTipologia, Collectors.counting()));
    }

    /**
     * Esporta tutte le strutture in formato CSV stringhe.
     *
     * @return lista di righe CSV, una per struttura
     */
    public List<String> esportaCSV() {
        return strutture.stream()
                .map(StrutturaRicettiva::toCSV)
                .collect(Collectors.toList());
    }
}
