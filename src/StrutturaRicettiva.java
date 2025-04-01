class StrutturaRicettiva {
    String comune;
    String provincia;
    String denominazioneATL;
    String stelle;
    String qualifica;
    String tipologia;
    String nomeStruttura;
    String indirizzo;
    String numeroCivico;
    String cap;
    String telefono;
    String fax;
    String email;
    String altitudineComune;
    String altitudineStruttura;
    String marchioEcolabel;
    String marchioQ;
    String marchioYes;
    String idoneitaDisabili;
    String animaliCamera;
    String assegno;
    String bancomat;
    String cartaCredito;
    String garage;
    String ascensore;
    String parcheggioRiservato;
    String ariaCondizionataCamere;
    String ariaCondizionataAppartamenti;

    public StrutturaRicettiva(String comune, String provincia, String denominazioneATL,
                              String stelle, String qualifica, String tipologia, String nomeStruttura,
                              String indirizzo, String numeroCivico, String cap, String telefono, String fax,
                              String email, String altitudineComune, String altitudineStruttura, String marchioEcolabel,
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

    @Override
    public String toString() {
        return "StrutturaRicettiva " + comune + ", " + provincia + ", " + denominazioneATL + ", " + stelle + ", " + qualifica + ", " + tipologia + ", " + nomeStruttura + ", " + indirizzo + ", " + numeroCivico + ", " +
                cap + ", " + telefono + ", " + fax + ", " + email + ", " + altitudineComune + ", " + altitudineStruttura + ", " + marchioEcolabel + ", " + marchioQ + ", " + marchioYes + ", " + idoneitaDisabili + ", " +
                animaliCamera + ", " + assegno + ", " + bancomat + ", " + cartaCredito + ", " + garage + ", " + ascensore + ", " + parcheggioRiservato + ", " + ariaCondizionataCamere + ", " + ariaCondizionataAppartamenti;
    }
}