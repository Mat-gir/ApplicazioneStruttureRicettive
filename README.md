# Applicazione Strutture Ricettive
Versione: supporto TCP interattivo e UDP, logging dettagliato, parsing robusto del CSV e gestione comandi da terminale

**DESCRIZIONE**

Un client–server Java che permette la consultazione remota di un elenco di strutture ricettive (CSV Regione Piemonte). Offre:

- Server TCP interattivo: prompt >>>, comandi help/exit, logging delle connessioni client (IP, porta)
- Server UDP frammentato: risposte suddivise in pacchetti da 1024 byte, marcatore __ END __
- Client Java multi‑protocollo (TCP/UDP) con invio automatico di help su UDP per generare subito log lato server
- Parsing CSV con ; come delimitatore, gestione delle virgolette, e sostituzione di campi vuoti con NON_PRESENTE
- Indicizzazione get_row in input 1-based → 0-based internamente

**STRUTTURA DEL PROGETTO**

    src   
        server
            src
                Protocollo.java
                StrutturaRicettiva.java   
                GestoreCSV.java    
                ThreadClientHandler.java
                ServerStrutture.java  
            Regione-Piemonte---Elenco-delle-strutture-ricettive.csv    
        client
            src
                ClientConnection.java
                Connection.java
                MainClient.java
                UdpClientConnection.java

**COMPLIAZIONE**

Esegui da terminale (root del progetto):

    mkdir -p out    
    javac -d out src/server/*.java src/client/*.java

**ESECUZIONE**

Avvio Server

    cd out  
    java server.ServerStrutture [<csvPath>] [<tcpPort>] [<udpPort>]

- csvPath (opzionale): percorso al CSV (default src/server/Regione-Piemonte---Elenco-delle-strutture-ricettive.csv)
- tcpPort (opzionale): porta TCP (default 1050)
- udpPort (opzionale): porta UDP (default 3030)

Il server stamperà:

    CSV: [<csvPath>]  
    Porte: TCP=[<tcpPort>]  UDP=[<udpPort>]   
    Server avviato (TCP[<tcpPort>]  UDP[<udpPort>])

CONNESSIONE TCP

    telnet localhost 1050

Il server invia un messaggio di benvenuto e prompt:

    Benvenuto nel server Strutture Ricettive!   
    Digita 'help' per l'elenco comandi, 'exit' per chiudere.    
    >>>

Connessione UDP

    # con client Java:   
    cd out
    java client.ClientStrutture

Il client Java in modalità UDP invia automaticamente help all’avvio per generare subito il logging lato server.

**COMANDI DISPONIBILI (TCP/UDP)**

- tutti                   : mostra tutte le strutture
- num_strutture           : numero totale di strutture
- get_row <n>             : dettaglio riga n (1‑based)
- comuni, tipologie     : elenchi distinti
- filtra comune:<nome>    : filtra per comune
- filtra provincia:<sigla>: filtra per provincia
- filtra stelle:<n>       : filtra per stelle
- filtra atl:<atl>        : filtra per ATL
- filtra tipologia:<tipo> : filtra per tipologia
- filtra marchio:<Q/Yes/Ecolabel>: filtra per marchio
- disabili, aria condizionata, carte, animali, parcheggio
- cerca nome:<parola>     : ricerca in nome struttura
- help                    : mostra questo elenco
- exit                    : termina la connessione (solo TCP)

In UDP, exit chiude il client Java; help è inviato una volta all’avvio per segnalare l'inizio della connessione, ma può essere rimandato.

**LOGGING**

TCP: ad ogni accept(), il server stampa:

    [TCP] Connessione ricevuta da <IP>:<porta>

UDP: ad ogni pacchetto ricevuto, il server stampa:

    [UDP] Richiesta da <IP>:<porta>  comando="<testo_comando>"

**PARSING CSV**

- Delimitatore ;, campi con virgolette gestiti correttamente.
- Campi vuoti sostituiti con NON_PRESENTE.
- Header loggata all’avvio con:


    → Intestazione CSV: <riga_header>
    → Caricate X strutture dal CSV.

