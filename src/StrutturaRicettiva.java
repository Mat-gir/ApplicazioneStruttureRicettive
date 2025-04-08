import java.util.Objects;

class StrutturaRicettiva {
    private String comune;
    private String provincia;
    private String denominazioneATL;
    private String stelle;
    private String qualifica;
    private String tipologia;
    private String nomeStruttura;
    private String indirizzo;
    private String numeroCivico;
    private String cap;
    private String telefono;
    private String fax;
    private String email;
    private String altitudineComune;
    private String altitudineStruttura;
    private String marchioEcolabel;
    private String marchioQ;
    private String marchioYes;
    private String idoneitaDisabili;
    private String animaliCamera;
    private String assegno;
    private String bancomat;
    private String cartaCredito;
    private String garage;
    private String ascensore;
    private String parcheggioRiservato;
    private String ariaCondizionataCamere;
    private String ariaCondizionataAppartamenti;

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

    public StrutturaRicettiva() {
    }

    @Override
    public String toString() {
        return " **" + nomeStruttura + "** (" + tipologia + ")\n" +
                "Comune: " + comune + " (" + provincia + ")\n" +
                "Indirizzo: " + indirizzo + ", " + numeroCivico + " - CAP: " + cap + "\n" +
                "Altitudine struttura: " + altitudineStruttura + " m | Altitudine comune: " + altitudineComune + " m\n" +
                "\n Contatti:\n" +
                "Telefono: " + telefono + " | Fax: " + fax + "\n" +
                "Email: " + email + "\n" +
                "\n Classificazione:\n" +
                "Stelle: " + stelle + " | Qualifica: " + qualifica + " | ATL: " + denominazioneATL + "\n" +
                "\n Accessibilità & Animali:\n" +
                "Idonea disabili: " + idoneitaDisabili + " | Animali in camera: " + animaliCamera + "\n" +
                "\n Pagamenti Accettati:\n" +
                "Assegno: " + assegno + " | Bancomat: " + bancomat + " | Carta di Credito: " + cartaCredito + "\n" +
                "\n🏷 Marchi di Qualità:\n" +
                "Ecolabel: " + marchioEcolabel + " | Marchio Q: " + marchioQ + " | Marchio Yes: " + marchioYes + "\n" +
                "\n🛎 Servizi:\n" +
                "Garage: " + garage + " | Ascensore: " + ascensore + " | Parcheggio Riservato: " + parcheggioRiservato + "\n" +
                "Aria Condizionata (Camere): " + ariaCondizionataCamere + " | Aria Condizionata (Appartamenti): " + ariaCondizionataAppartamenti + "\n" +
                "------------------------------------------------------------\n";
    }

    // Metodi utili
    public boolean haComune(String nomeComune) {
        return comune != null && comune.equalsIgnoreCase(nomeComune);
    }

    public boolean haTipologia(String tipo) {
        return tipologia != null && tipologia.equalsIgnoreCase(tipo);
    }

    public boolean haProvincia(String siglaProvincia) {
        return provincia != null && provincia.equalsIgnoreCase(siglaProvincia);
    }

    public boolean accettaCartaCredito() {
        return cartaCredito != null && cartaCredito.equalsIgnoreCase("SI");
    }

    public boolean accettaAnimali() {
        return animaliCamera != null && animaliCamera.equalsIgnoreCase("SI");
    }

    public boolean haParcheggio() {
        return parcheggioRiservato != null && parcheggioRiservato.equalsIgnoreCase("SI");
    }

    public String getChiaveComune() {
        return comune != null ? comune.toUpperCase() : "NON DEFINITO";
    }

    public String getChiaveTipologia() {
        return tipologia != null ? tipologia.toUpperCase() : "NON DEFINITA";
    }

    public String toCSV() {
        return String.join(";",
                comune, provincia, denominazioneATL, stelle, qualifica, tipologia,
                nomeStruttura, indirizzo, numeroCivico, cap, telefono, fax,
                email, altitudineComune, altitudineStruttura, marchioEcolabel,
                marchioQ, marchioYes, idoneitaDisabili, animaliCamera, assegno,
                bancomat, cartaCredito, garage, ascensore, parcheggioRiservato,
                ariaCondizionataCamere, ariaCondizionataAppartamenti
        );
    }


    public String getComune() { return comune; }

    public void setComune(String comune) { this.comune = comune; }

    public String getProvincia() { return provincia; }

    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getDenominazioneATL() { return denominazioneATL; }

    public void setDenominazioneATL(String denominazioneATL) { this.denominazioneATL = denominazioneATL; }

    public String getStelle() { return stelle; }

    public void setStelle(String stelle) { this.stelle = stelle; }

    public String getQualifica() { return qualifica; }

    public void setQualifica(String qualifica) { this.qualifica = qualifica; }

    public String getTipologia() { return tipologia; }

    public void setTipologia(String tipologia) { this.tipologia = tipologia; }

    public String getNomeStruttura() { return nomeStruttura; }

    public void setNomeStruttura(String nomeStruttura) { this.nomeStruttura = nomeStruttura; }

    public String getIndirizzo() { return indirizzo; }

    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public String getNumeroCivico() { return numeroCivico; }

    public void setNumeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; }

    public String getCap() { return cap; }

    public void setCap(String cap) { this.cap = cap; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getFax() { return fax; }

    public void setFax(String fax) { this.fax = fax; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getAltitudineComune() { return altitudineComune; }

    public void setAltitudineComune(String altitudineComune) { this.altitudineComune = altitudineComune; }

    public String getAltitudineStruttura() { return altitudineStruttura; }

    public void setAltitudineStruttura(String altitudineStruttura) { this.altitudineStruttura = altitudineStruttura; }

    public String getMarchioEcolabel() { return marchioEcolabel; }

    public void setMarchioEcolabel(String marchioEcolabel) { this.marchioEcolabel = marchioEcolabel; }

    public String getMarchioQ() { return marchioQ; }

    public void setMarchioQ(String marchioQ) { this.marchioQ = marchioQ; }

    public String getMarchioYes() { return marchioYes; }

    public void setMarchioYes(String marchioYes) { this.marchioYes = marchioYes; }

    public String getIdoneitaDisabili() { return idoneitaDisabili; }

    public void setIdoneitaDisabili(String idoneitaDisabili) { this.idoneitaDisabili = idoneitaDisabili; }

    public String getAnimaliCamera() { return animaliCamera; }

    public void setAnimaliCamera(String animaliCamera) { this.animaliCamera = animaliCamera; }

    public String getAssegno() { return assegno; }

    public void setAssegno(String assegno) { this.assegno = assegno; }

    public String getBancomat() { return bancomat; }

    public void setBancomat(String bancomat) { this.bancomat = bancomat; }

    public String getCartaCredito() { return cartaCredito; }

    public void setCartaCredito(String cartaCredito) { this.cartaCredito = cartaCredito; }

    public String getGarage() { return garage; }

    public void setGarage(String garage) { this.garage = garage; }

    public String getAscensore() { return ascensore; }

    public void setAscensore(String ascensore) { this.ascensore = ascensore; }

    public String getParcheggioRiservato() { return parcheggioRiservato; }

    public void setParcheggioRiservato(String parcheggioRiservato) { this.parcheggioRiservato = parcheggioRiservato; }

    public String getAriaCondizionataCamere() { return ariaCondizionataCamere; }

    public void setAriaCondizionataCamere(String ariaCondizionataCamere) { this.ariaCondizionataCamere = ariaCondizionataCamere; }

    public String getAriaCondizionataAppartamenti() { return ariaCondizionataAppartamenti; }

    public void setAriaCondizionataAppartamenti(String ariaCondizionataAppartamenti) { this.ariaCondizionataAppartamenti = ariaCondizionataAppartamenti; }
}