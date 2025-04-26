# Applicazione Strutture Ricettive
**DESCRIZIONE**

Questo progetto implementa un'applicazione client‑server in Java per la consultazione remota di un file CSV contenente l'elenco delle strutture ricettive della Regione Piemonte. Il server offre sia interfacce TCP che UDP per ricevere comandi di ricerca e filtro; il client consente all'utente di selezionare il protocollo, inviare richieste e visualizzare i risultati.

**REQUISITI**

- Java 8 (o superiore)
- Ambiente di sviluppo Java
- File CSV: Regione-Piemonte---Elenco-delle-strutture-ricettive.csv

**STRUTTURA DEL PROGETTO**

**Server**
 
/server/Protocollo.java     
/server/StrutturaRicettiva.java     
/server/GestoreCSV.java     
/server/ThreadClientHandler.java        
/server/ServerStrutture.java  

**Client**

/client/ClientStrutture.java    
/client/GestioneClient.java

**CONFIGURAZIONE**

1. Copia il file CSV nella cartella di progetto
2. Se necessario, rinomina o modifica il percorso in fase di esecuzione.

IMPORTAZIONE E COMPILAZIONE

1. Avvia il tuo compilatore e seleziona File → New → Project
2. Scegli la cartella radice del progetto.
3. Crea i progetti server e client nelle rispettive cartelle src e incolla al loro interno i file Java.
4. Aggiungi il CSV all'interno della cartella server.
5. Verifica che il progetto compili senza errori.

**ESECUZIONE**

**Server**

nel terminale:   
cd <progetto>/server    
java -cp . server.ServerStrutture <percorso_csv> [tcp_port] [udp_port]

- <percorso_csv>: path al file CSV (es. data/Regione-Piemonte---Elenco-delle-strutture-ricettive.csv)
- [tcp_port]: porta TCP (default 1050)
- [udp_port]: porta UDP (default 3030)

**Client**

nel terminale:  
cd <progetto>/client    
java -cp . client.ClientStrutture

Il client chiederà IP, protocollo (tcp/udp) e avvierà la sessione interattiva.

**PROTOCOLLO E COMANDI DISPONIBILI**

- tutti: restituisce tutte le strutture
- num_strutture: numero totale di record
- get_row: restituisce la riga n (0-based)
- comuni: elenco dei comuni distinti
- tipologie: elenco delle tipologie distinte
- filtra comune:
- filtra provincia:
- filtra stelle:
- filtra atl:
- filtra tipologia:
- filtra marchio:<Q/Yes/Ecolabel>
- disabili: strutture accessibili a persone con disabilità
- aria condizionata
- carte: strutture che accettano carta di credito
- animali: strutture che accettano animali
- parcheggi: strutture con parcheggio
- cerca nome:
- help: mostra questo elenco
- exit: termina la connessione
