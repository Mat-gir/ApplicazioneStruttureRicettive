package server.src;

public class Protocollo {
    // protocolli e costanti per la comunicazione client–server

    public static final String TUTTI = "tutti";
    public static final String GET_ROW = "get_row";
    public static final String NUM_STRUTTURE = "num_strutture";
    public static final String COMUNI = "comuni";
    public static final String TIPOLOGIE = "tipologie";
    public static final String FILTRA_COMUNE = "filtra comune:";
    public static final String FILTRA_TIPOLOGIA = "filtra tipologia:";
    public static final String FILTRA_PROVINCIA = "filtra provincia:";
    public static final String FILTRA_STELLE = "filtra stelle:";
    public static final String FILTRA_ATL = "filtra atl:";
    public static final String FILTRA_MARCHIO = "filtra marchio:";
    public static final String DISABILI = "disabili";
    public static final String ARIA_CONDIZIONATA = "aria condizionata";
    public static final String CARTE = "carte";
    public static final String ANIMALI = "animali";
    public static final String PARCHEGGIO = "parcheggio";
    public static final String CERCA_NOME = "cerca nome:";
    public static final String ERRORE_COMANDO = "ERROR: Comando non riconosciuto.";
    public static final String ERRORE_RIGA = "ERROR: Invalid row";

    public static String getHelpMessage() {
        return String.join("\n",
                "Comandi disponibili:",
                "- tutti                     : mostra tutte le strutture",
                "- num_strutture             : numero totale di strutture",
                "- get_row <n>               : dettaglio della riga n",
                "- comuni, tipologie         : elenchi distinti",
                "- filtra comune:<nome>",
                "- filtra provincia:<sigla>",
                "- filtra stelle:<n>",
                "- filtra atl:<atl>",
                "- filtra tipologia:<tipo>",
                "- filtra marchio:<Q/Yes/Ecolabel>",
                "- disabili, aria condizionata, carte, animali, parcheggio",
                "- cerca nome:<parola>",
                "- help                      : mostra questo elenco",
                "- exit                      : termina la connessione"
        );
    }
}