package server.src;

public class Protocollo {
    public static final int TCP_PORT       = 1050;
    public static final int UDP_PORT       = 3030;
    public static final int UDP_CHUNK_SIZE = 1024;

    public static final String TUTTI              = "tutti";
    public static final String GET_ROW            = "get_row";
    public static final String NUM_STRUTTURE      = "num_strutture";
    public static final String COMUNI             = "comuni";
    public static final String TIPOLOGIE          = "tipologie";
    public static final String FILTRA_COMUNE      = "filtra comune:";
    public static final String FILTRA_PROVINCIA   = "filtra provincia:";
    public static final String FILTRA_STELLE      = "filtra stelle:";
    public static final String FILTRA_ATL         = "filtra atl:";
    public static final String FILTRA_TIPOLOGIA   = "filtra tipologia:";
    public static final String FILTRA_MARCHIO     = "filtra marchio:";
    public static final String DISABILI          = "disabili";
    public static final String ARIA_CONDIZIONATA = "aria condizionata";
    public static final String CARTE              = "carte";
    public static final String ANIMALI            = "animali";
    public static final String PARCHEGGIO         = "parcheggio";
    public static final String CERCA_NOME         = "cerca nome:";
    public static final String HELP               = "help";
    public static final String EXIT               = "exit";
    public static final String ERRORE_COMANDO  = "ERROR: Comando non riconosciuto.";
    public static final String ERRORE_RIGA     = "ERROR: Riga non valida.";

    /**
     * Messaggio di help completo, terminato dal marker __END__.
     *
     * @return stringa contenente l’elenco dei comandi disponibili,
     *         ciascuno su una nuova riga e terminato dal marker "__END__"
     */
    public static String getHelpMessage() {
        return String.join("\n",
                "Comandi disponibili:",
                "- " + HELP + "                      : mostra questo elenco",
                "- " + TUTTI + "                      : elenca tutte le strutture",
                "- " + NUM_STRUTTURE + "             : numero totale di strutture",
                "- " + GET_ROW + " <n>               : dettaglio della riga n",
                "- " + COMUNI + "                     : elenca i comuni distinti",
                "- " + TIPOLOGIE + "                  : elenca le tipologie distinte",
                "- " + FILTRA_COMUNE + "<nome>        : filtra per comune",
                "- " + FILTRA_PROVINCIA + "<sigla>     : filtra per provincia",
                "- " + FILTRA_STELLE + "<n>           : filtra per stelle",
                "- " + FILTRA_ATL + "<atl>            : filtra per ATL",
                "- " + FILTRA_TIPOLOGIA + "<tipo>      : filtra per tipologia",
                "- " + FILTRA_MARCHIO + "<mar>         : filtra per marchio",
                "- " + DISABILI + "                   : filtra per disabili",
                "- " + ARIA_CONDIZIONATA + "           : filtra con aria condizionata",
                "- " + CARTE + "                      : filtra che accettano carte",
                "- " + ANIMALI + "                    : filtra che accettano animali",
                "- " + PARCHEGGIO + "                 : filtra con parcheggio",
                "- " + CERCA_NOME + "<parola>         : ricerca nel nome",
                "- " + EXIT + "                      : termina il server",
                "__END__"
        );
    }
}