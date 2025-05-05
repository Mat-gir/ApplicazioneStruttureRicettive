package server.src;

import java.util.Objects;

/**
 * Modello di una struttura ricettiva contenente informazioni
 * base e servizi offerti.
 */
public class StrutturaRicettiva {
    // Informazioni di localizzazione
    private String comune;
    private String provincia;
    private String denominazioneATL;
    private String stelle;
    private String qualifica;
    private String tipologia;

    // Dati identificativi e contatti
    private String nomeStruttura;
    private String indirizzo;
    private String numeroCivico;
    private String cap;
    private String telefono;
    private String fax;
    private String email;

    // Altitudini
    private String altitudineComune;
    private String altitudineStruttura;

    // Marchi e certificazioni
    private String marchioEcolabel;
    private String marchioQ;
    private String marchioYes;

    // Servizi e accessibilità
    private String idoneitaDisabili;
    private String animaliCamera;

    // Opzioni di pagamento
    private String assegno;
    private String bancomat;
    private String cartaCredito;

    // Parcheggio e accessori
    private String garage;
    private String ascensore;
    private String parcheggioRiservato;
    private String ariaCondizionataCamere;
    private String ariaCondizionataAppartamenti;

    /**
     * Costruisce una nuova istanza di StrutturaRicettiva con tutti i dati.
     *
     * @param comune                       comune in cui si trova la struttura
     * @param provincia                    provincia di appartenenza
     * @param denominazioneATL             nome ATL di riferimento
     * @param stelle                       valutazione in stelle
     * @param qualifica                    qualifica ufficiale (es. "Turistico")
     * @param tipologia                    tipologia di struttura (es. "Hotel", "B&B")
     * @param nomeStruttura                denominazione commerciale
     * @param indirizzo                    via e numero civico
     * @param numeroCivico                 numero civico
     * @param cap                          codice di avviamento postale
     * @param telefono                     numero di telefono
     * @param fax                          numero di fax
     * @param email                        email di contatto
     * @param altitudineComune             altitudine del comune (metri)
     * @param altitudineStruttura          altitudine della struttura (metri)
     * @param marchioEcolabel              indicatore Ecolabel (SI/NO)
     * @param marchioQ                     indicatore marchio Q (SI/NO)
     * @param marchioYes                   indicatore marchio Yes (SI/NO)
     * @param idoneitaDisabili             accessibilità disabili (SI/NO)
     * @param animaliCamera                permesso animali in camera (SI/NO)
     * @param assegno                      accetta assegni (SI/NO)
     * @param bancomat                     accetta bancomat (SI/NO)
     * @param cartaCredito                 accetta carta di credito (SI/NO)
     * @param garage                       garage disponibile (SI/NO)
     * @param ascensore                    ascensore presente (SI/NO)
     * @param parcheggioRiservato          parcheggio riservato (SI/NO)
     * @param ariaCondizionataCamere       aria condizionata in camere (SI/NO)
     * @param ariaCondizionataAppartamenti aria condizionata in appartamenti (SI/NO)
     */
    public StrutturaRicettiva(String comune, String provincia, String denominazioneATL, String stelle,
                              String qualifica, String tipologia, String nomeStruttura, String indirizzo,
                              String numeroCivico, String cap, String telefono, String fax, String email,
                              String altitudineComune, String altitudineStruttura, String marchioEcolabel,
                              String marchioQ, String marchioYes, String idoneitaDisabili, String animaliCamera,
                              String assegno, String bancomat, String cartaCredito, String garage,
                              String ascensore, String parcheggioRiservato, String ariaCondizionataCamere,
                              String ariaCondizionataAppartamenti) {
        this.comune = comune;
        this.provincia = provincia;
        this.denominazioneATL = denominazioneATL;
        this.stelle = stelle;
        this.qualifica = qualifica;
        this.tipologia = tipologia;
        this.nomeStruttura = nomeStruttura;
        this.indirizzo = indirizzo;
        this.numeroCivico = numeroCivico;
        this.cap = cap;
        this.telefono = telefono;
        this.fax = fax;
        this.email = email;
        this.altitudineComune = altitudineComune;
        this.altitudineStruttura = altitudineStruttura;
        this.marchioEcolabel = marchioEcolabel;
        this.marchioQ = marchioQ;
        this.marchioYes = marchioYes;
        this.idoneitaDisabili = idoneitaDisabili;
        this.animaliCamera = animaliCamera;
        this.assegno = assegno;
        this.bancomat = bancomat;
        this.cartaCredito = cartaCredito;
        this.garage = garage;
        this.ascensore = ascensore;
        this.parcheggioRiservato = parcheggioRiservato;
        this.ariaCondizionataCamere = ariaCondizionataCamere;
        this.ariaCondizionataAppartamenti = ariaCondizionataAppartamenti;
    }

    /**
     * Restituisce una stringa di presentazione della struttura con i principali dati.
     *
     * @return descrizione multilinea della struttura
     */
    @Override
    public String toString() {
        return nomeStruttura + " (" + tipologia + "), " + indirizzo + " " + numeroCivico + " - " + comune + " (" + provincia + ")"
                + "\nCAP: " + cap + ", Tel: " + telefono + ", Fax: " + fax + ", Email: " + email
                + "\nStelle: " + stelle + ", Qualifica: " + qualifica + ", ATL: " + denominazioneATL
                + "\nAltitudine Comune: " + altitudineComune + ", Altitudine Struttura: " + altitudineStruttura
                + "\nMarchi: Ecolabel(" + marchioEcolabel + "), Q(" + marchioQ + "), Yes(" + marchioYes + ")"
                + "\nServizi: Disabili(" + idoneitaDisabili + "), Animali(" + animaliCamera + ")"
                + "\nPagamenti: Assegno(" + assegno + "), Bancomat(" + bancomat + "), Carta(" + cartaCredito + ")"
                + "\nAccessori: Garage(" + garage + "), Ascensore(" + ascensore + "), Parcheggio(" + parcheggioRiservato + ")"
                + ", Aria cond. camere(" + ariaCondizionataCamere + "), appartamenti(" + ariaCondizionataAppartamenti + ")"
                + "\n------------------------------------------------------------";
    }

    /**
     * Converte i dati della struttura in formato CSV.
     *
     * @return riga CSV separata da virgole
     */
    public String toCSV() {
        return String.join(",",
                Objects.toString(comune, ""),
                Objects.toString(provincia, ""),
                Objects.toString(denominazioneATL, ""),
                Objects.toString(stelle, ""),
                Objects.toString(qualifica, ""),
                Objects.toString(tipologia, ""),
                Objects.toString(nomeStruttura, ""),
                Objects.toString(indirizzo, ""),
                Objects.toString(numeroCivico, ""),
                Objects.toString(cap, ""),
                Objects.toString(telefono, ""),
                Objects.toString(fax, ""),
                Objects.toString(email, ""),
                Objects.toString(altitudineComune, ""),
                Objects.toString(altitudineStruttura, ""),
                Objects.toString(marchioEcolabel, ""),
                Objects.toString(marchioQ, ""),
                Objects.toString(marchioYes, ""),
                Objects.toString(idoneitaDisabili, ""),
                Objects.toString(animaliCamera, ""),
                Objects.toString(assegno, ""),
                Objects.toString(bancomat, ""),
                Objects.toString(cartaCredito, ""),
                Objects.toString(garage, ""),
                Objects.toString(ascensore, ""),
                Objects.toString(parcheggioRiservato, ""),
                Objects.toString(ariaCondizionataCamere, ""),
                Objects.toString(ariaCondizionataAppartamenti, "")
        );
    }

    /**
     * Confronta due strutture per uguaglianza basandosi sul nome e ubicazione.
     *
     * @param o oggetto da confrontare
     * @return true se rappresentano la stessa struttura, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrutturaRicettiva)) return false;
        StrutturaRicettiva that = (StrutturaRicettiva) o;
        return Objects.equals(nomeStruttura, that.nomeStruttura)
                && Objects.equals(indirizzo, that.indirizzo)
                && Objects.equals(numeroCivico, that.numeroCivico)
                && Objects.equals(comune, that.comune)
                && Objects.equals(provincia, that.provincia);
    }

    /**
     * Genera l'hashcode basato su nome e ubicazione della struttura.
     *
     * @return hashcode univoco
     */
    @Override
    public int hashCode() {
        return Objects.hash(nomeStruttura, indirizzo, numeroCivico, comune, provincia);
    }

    // Metodi di utilità per filtraggio sulle proprietà

    /**
     * Controlla se la struttura appartiene al comune specificato.
     *
     * @param c comune da verificare
     * @return true se il comune corrisponde, false altrimenti
     */
    public boolean haComune(String c) {
        return comune != null && comune.equalsIgnoreCase(c);
    }

    /**
     * Controlla se la struttura appartiene alla provincia specificata.
     *
     * @param p provincia da verificare
     * @return true se la provincia corrisponde, false altrimenti
     */
    public boolean haProvincia(String p) {
        return provincia != null && provincia.equalsIgnoreCase(p);
    }

    /**
     * Verifica se la struttura ha il numero di stelle indicato.
     *
     * @param s stelle da verificare
     * @return true se il valore corrisponde, false altrimenti
     */
    public boolean haStelle(String s) {
        return stelle != null && stelle.equalsIgnoreCase(s);
    }

    /**
     * Verifica se la struttura possiede il marchio specificato: "Q", "Yes" o "Ecolabel".
     *
     * @param m marchio da verificare
     * @return true se presente, false altrimenti
     */
    public boolean haMarchio(String m) {
        return (m.equalsIgnoreCase("Q") && marchioQ.equalsIgnoreCase("SI"))
                || (m.equalsIgnoreCase("Yes") && marchioYes.equalsIgnoreCase("SI"))
                || (m.equalsIgnoreCase("Ecolabel") && marchioEcolabel.equalsIgnoreCase("SI"));
    }

    /**
     * Controlla se la struttura è accessibile ai disabili.
     *
     * @return true se idoneità è "SI"
     */
    public boolean isIdoneitaDisabili() {
        return idoneitaDisabili != null && idoneitaDisabili.equalsIgnoreCase("SI");
    }

    /**
     * Verifica se la struttura accetta carta di credito.
     *
     * @return true se cartaCredito è "SI"
     */
    public boolean accettaCartaCredito() {
        return cartaCredito != null && cartaCredito.equalsIgnoreCase("SI");
    }

    /**
     * Verifica se la struttura consente animali nelle camere.
     *
     * @return true se animaliCamera è "SI"
     */
    public boolean accettaAnimali() {
        return animaliCamera != null && animaliCamera.equalsIgnoreCase("SI");
    }

    /**
     * Verifica se la struttura dispone di parcheggi riservati.
     *
     * @return true se parcheggioRiservato è "SI"
     */
    public boolean haParcheggio() {
        return parcheggioRiservato != null && parcheggioRiservato.equalsIgnoreCase("SI");
    }

    /**
     * Verifica se il nome della struttura contiene la keyword specificata (case-insensitive).
     *
     * @param kw sottostringa da cercare
     * @return true se presente nel nome, false altrimenti
     */
    public boolean contieneNome(String kw) {
        return nomeStruttura != null && nomeStruttura.toLowerCase().contains(kw.toLowerCase());
    }

    /**
     * Restituisce il comune come chiave per raggruppamenti.
     *
     * @return chiave comune
     */
    public String getChiaveComune() {
        return comune;
    }

    /**
     * Restituisce la tipologia come chiave per raggruppamenti.
     *
     * @return chiave tipologia
     */
    public String getChiaveTipologia() {
        return tipologia;
    }

    /**
     * Restituisce la denominazione ATL.
     *
     * @return denominazione ATL
     */
    public String getDenominazioneATL() {
        return denominazioneATL;
    }

    /**
     * Restituisce l'indicatore di aria condizionata nelle camere.
     *
     * @return "SI" se presente, "NO" altrimenti
     */
    public String getAriaCondizionataCamere() {
        return ariaCondizionataCamere;
    }

    /**
     * Restituisce l'indicatore di aria condizionata negli appartamenti.
     *
     * @return "SI" se presente, "NO" altrimenti
     */
    public String getAriaCondizionataAppartamenti() {
        return ariaCondizionataAppartamenti;
    }
}