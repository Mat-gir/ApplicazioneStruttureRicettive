package server.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GestoreCSV {
    // carica il CSV e fornisce metodi di ricerca e filtro

    private final List<StrutturaRicettiva> strutture;

    public GestoreCSV(String csvPath) {
        strutture = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            // Legge e logga intestazione

            String line;
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                lineNum++;
                String[] campi = splitCSV(line);
                if (campi.length >= 28) {
                    strutture.add(new StrutturaRicettiva(
                            campi[0],  campi[1],  campi[2],  campi[3],  campi[4],
                            campi[5],  campi[6],  campi[7],  campi[8],  campi[9],
                            campi[10], campi[11], campi[12], campi[13], campi[14],
                            campi[15], campi[16], campi[17], campi[18], campi[19],
                            campi[20], campi[21], campi[22], campi[23], campi[24],
                            campi[25], campi[26], campi[27]
                    ));
                } else {
                    System.err.printf("Riga %d saltata: trovati %d campi anziché 28%n", lineNum, campi.length);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del CSV: " + e.getMessage());
        }
    }

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
        // ultimo campo
        String last = cur.toString().trim();
        result.add(last.isEmpty() ? "NON_PRESENTE" : last);
        return result.toArray(new String[0]);
    }

    public String getRiga(int idx) {
        return (idx >= 0 && idx < strutture.size())
                ? strutture.get(idx).toString()
                : Protocollo.ERRORE_RIGA;
    }

    public int getNumeroStrutture() {
        return strutture.size();
    }

    public List<StrutturaRicettiva> getTutteLeStrutture() {
        return new ArrayList<>(strutture);
    }

    public List<StrutturaRicettiva> filtraPerComune(String c) {
        return strutture.stream()
                .filter(s -> s.haComune(c))
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraPerProvincia(String p) {
        return strutture.stream()
                .filter(s -> s.haProvincia(p))
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraPerStelle(String s) {
        return strutture.stream()
                .filter(str -> str.haStelle(s))
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraPerATL(String atl) {
        return strutture.stream()
                .filter(str -> atl.equalsIgnoreCase(str.getDenominazioneATL()))
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraPerTipologia(String t) {
        return strutture.stream()
                .filter(str -> str.getChiaveTipologia().equalsIgnoreCase(t))
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraPerMarchio(String m) {
        return strutture.stream()
                .filter(str -> str.haMarchio(m))
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraAccessibiliDisabili() {
        return strutture.stream()
                .filter(StrutturaRicettiva::isIdoneitaDisabili)
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraConAriaCondizionata() {
        return strutture.stream()
                .filter(str -> "SI".equalsIgnoreCase(str.getAriaCondizionataCamere())
                        || "SI".equalsIgnoreCase(str.getAriaCondizionataAppartamenti()))
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraCheAccettanoCarte() {
        return strutture.stream()
                .filter(StrutturaRicettiva::accettaCartaCredito)
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraCheAccettanoAnimali() {
        return strutture.stream()
                .filter(StrutturaRicettiva::accettaAnimali)
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraCheHannoParcheggio() {
        return strutture.stream()
                .filter(StrutturaRicettiva::haParcheggio)
                .collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraPerNome(String kw) {
        return strutture.stream()
                .filter(str -> str.contieneNome(kw))
                .collect(Collectors.toList());
    }

    public Set<String> getComuniPresenti() {
        return strutture.stream()
                .map(StrutturaRicettiva::getChiaveComune)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<String> getTipologiePresenti() {
        return strutture.stream()
                .map(StrutturaRicettiva::getChiaveTipologia)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Map<String,List<StrutturaRicettiva>> raggruppaPerComune() {
        return strutture.stream()
                .collect(Collectors.groupingBy(StrutturaRicettiva::getChiaveComune));
    }

    public Map<String,List<StrutturaRicettiva>> raggruppaPerTipologia() {
        return strutture.stream()
                .collect(Collectors.groupingBy(StrutturaRicettiva::getChiaveTipologia));
    }

    public Map<String,Long> contaPerComune() {
        return strutture.stream()
                .collect(Collectors.groupingBy(StrutturaRicettiva::getChiaveComune, Collectors.counting()));
    }

    public Map<String,Long> contaPerTipologia() {
        return strutture.stream()
                .collect(Collectors.groupingBy(StrutturaRicettiva::getChiaveTipologia, Collectors.counting()));
    }

    public List<String> esportaCSV() {
        return strutture.stream()
                .map(StrutturaRicettiva::toCSV)
                .collect(Collectors.toList());
    }
}
