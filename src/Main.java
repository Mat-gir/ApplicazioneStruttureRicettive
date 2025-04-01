import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "Regione-Piemonte---Elenco-delle-strutture-ricettive.csv";
        List<StrutturaRicettiva> strutture = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Salta l'intestazione
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length >= 28) {
                    StrutturaRicettiva struttura = new StrutturaRicettiva(
                            values[0], values[1], values[2], values[3], values[4],
                            values[5], values[6], values[7], values[8], values[9],
                            values[10], values[11], values[12], values[13], values[14],
                            values[15], values[16], values[17], values[18], values[19],
                            values[20], values[21], values[22], values[23], values[24],
                            values[25], values[26], values[27]
                    );
                    strutture.add(struttura);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stampa le prime 5 strutture per verifica
        for (int i = 0; i < strutture.size(); i++) {
            System.out.print(i + " ");
            System.out.println(strutture.get(i));
        }
    }
}