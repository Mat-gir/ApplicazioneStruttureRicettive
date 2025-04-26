package server.src;

import java.util.Objects;

public class StrutturaRicettiva {
    // modello di una struttura ricettiva

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

    @Override
    public String toString() {
        return nomeStruttura + " (" + tipologia + "), " + indirizzo + " " + numeroCivico + " - " + comune + " (" + provincia + ")" +
                "\nCAP: " + cap + ", Tel: " + telefono + ", Fax: " + fax + ", Email: " + email +
                "\nStelle: " + stelle + ", Qualifica: " + qualifica + ", ATL: " + denominazioneATL +
                "\nAltitudine Comune: " + altitudineComune + ", Altitudine Struttura: " + altitudineStruttura +
                "\nMarchi: Ecolabel(" + marchioEcolabel + "), Q(" + marchioQ + "), Yes(" + marchioYes + ")" +
                "\nServizi: Disabili(" + idoneitaDisabili + "), Animali in camera(" + animaliCamera + ")" +
                "\nPagamenti: Assegno(" + assegno + "), Bancomat(" + bancomat + "), Carta(" + cartaCredito + ")" +
                "\nAccessori: Garage(" + garage + "), Ascensore(" + ascensore + "), Parcheggio(" + parcheggioRiservato + ")" +
                ", Aria cond. camere(" + ariaCondizionataCamere + "), appartamenti(" + ariaCondizionataAppartamenti + ")" +
                "\n------------------------------------------------------------";
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StrutturaRicettiva)) return false;
        StrutturaRicettiva that = (StrutturaRicettiva) o;
        return Objects.equals(nomeStruttura, that.nomeStruttura) &&
                Objects.equals(indirizzo, that.indirizzo) &&
                Objects.equals(numeroCivico, that.numeroCivico) &&
                Objects.equals(comune, that.comune) &&
                Objects.equals(provincia, that.provincia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeStruttura, indirizzo, numeroCivico, comune, provincia);
    }

    public boolean haComune(String c) {
        return comune != null && comune.equalsIgnoreCase(c);
    }
    public boolean haProvincia(String p) {
        return provincia != null && provincia.equalsIgnoreCase(p);
    }
    public boolean haStelle(String s) {
        return stelle != null && stelle.equalsIgnoreCase(s);
    }
    public boolean haMarchio(String m) {
        return (m.equalsIgnoreCase("Q")       && marchioQ.equalsIgnoreCase("SI"))   ||
                (m.equalsIgnoreCase("Yes")     && marchioYes.equalsIgnoreCase("SI")) ||
                (m.equalsIgnoreCase("Ecolabel")&& marchioEcolabel.equalsIgnoreCase("SI"));
    }
    public boolean isIdoneitaDisabili() {
        return idoneitaDisabili != null && idoneitaDisabili.equalsIgnoreCase("SI");
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
    public boolean contieneNome(String kw) {
        return nomeStruttura != null && nomeStruttura.toLowerCase().contains(kw.toLowerCase());
    }

    public String getChiaveComune()    { return comune; }
    public String getChiaveTipologia(){ return tipologia; }
    public String getDenominazioneATL(){ return denominazioneATL; }
    public String getAriaCondizionataCamere()     { return ariaCondizionataCamere; }
    public String getAriaCondizionataAppartamenti(){ return ariaCondizionataAppartamenti; }
}