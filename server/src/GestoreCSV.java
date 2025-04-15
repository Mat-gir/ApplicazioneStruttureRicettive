import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GestoreCSV {
    //Si occupa della lettura, pulizia e trasformazione dei dati del file CSV.

    private List<StrutturaRicettiva> strutture;

    public GestoreCSV(String percorsoCSV) {
        this.strutture = new ArrayList<>();
        caricaDaCSV(percorsoCSV);
        pulisciDati();
    }

    private void caricaDaCSV(String percorso) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(percorso))) {
            String intestazione = reader.readLine(); // Salta la prima riga
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campi = linea.split(";", -1);
                if (campi.length >= 28) {
                    StrutturaRicettiva s = new StrutturaRicettiva(
                            campi[0], campi[1], campi[2], campi[3], campi[4], campi[5], campi[6],
                            campi[7], campi[8], campi[9], campi[10], campi[11], campi[12],
                            campi[13], campi[14], campi[15], campi[16], campi[17], campi[18],
                            campi[19], campi[20], campi[21], campi[22], campi[23], campi[24],
                            campi[25], campi[26], campi[27]
                    );
                    strutture.add(s);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura del file CSV: " + e.getMessage());
        }
    }

    private void pulisciDati() {
        strutture = strutture.stream()
                .filter(s -> s.getComune() != null && !s.getComune().isBlank())
                .map(this::normalizza)
                .collect(Collectors.toList());
    }

    private StrutturaRicettiva normalizza(StrutturaRicettiva s) {
        s.setComune(s.getComune().toUpperCase().trim());
        s.setTipologia(s.getTipologia().toUpperCase().trim());
        s.setNomeStruttura(s.getNomeStruttura().trim());
        s.setIndirizzo(s.getIndirizzo().trim());
        return s;
    }

    public List<StrutturaRicettiva> getTutteLeStrutture() {
        return strutture;
    }

    public List<StrutturaRicettiva> filtra(Predicate<StrutturaRicettiva> criterio) {
        return strutture.stream().filter(criterio).collect(Collectors.toList());
    }

    public List<StrutturaRicettiva> filtraPerComune(String comune) {
        return filtra(s -> s.getComune().equalsIgnoreCase(comune));
    }

    public List<StrutturaRicettiva> filtraPerTipologia(String tipologia) {
        return filtra(s -> s.getTipologia().equalsIgnoreCase(tipologia));
    }

    public StrutturaRicettiva cercaPerNome(String nome) {
        return strutture.stream()
                .filter(s -> s.getNomeStruttura().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public Map<String, Long> contaPerComune() {
        return strutture.stream()
                .collect(Collectors.groupingBy(
                        StrutturaRicettiva::getComune,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> contaPerTipologia() {
        return strutture.stream()
                .collect(Collectors.groupingBy(
                        StrutturaRicettiva::getTipologia,
                        Collectors.counting()
                ));
    }

    public int getNumeroStrutture() {
        return strutture.size();
    }

    public boolean esisteStruttura(String nome) {
        return strutture.stream().anyMatch(s -> s.getNomeStruttura().equalsIgnoreCase(nome));
    }

    public List<String> getComuniPresenti() {
        return strutture.stream()
                .map(StrutturaRicettiva::getComune)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getTipologiePresenti() {
        return strutture.stream()
                .map(StrutturaRicettiva::getTipologia)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public StrutturaRicettiva getStruttura(int index) {
        if (index >= 0 && index < strutture.size()) {
            return strutture.get(index);
        }
        return null;
    }

    public void esportaCSV(String path) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
            for (StrutturaRicettiva s : strutture) {
                writer.write(s.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Errore durante l'esportazione: " + e.getMessage());
        }
    }

    // Metodo per ottenere solo strutture con campo non vuoto per una certa colonna
    public List<StrutturaRicettiva> conCampoNonVuoto(Predicate<StrutturaRicettiva> criterio) {
        return strutture.stream().filter(criterio).collect(Collectors.toList());
    }

}
