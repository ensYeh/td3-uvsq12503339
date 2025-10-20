package fr.uvsq.cprog.collex;

public class AdresseIP {
    private final String adresse;

    public AdresseIP(String adresse) {
        if (!adresse.matches("^\\d{1,3}(\\.\\d{1,3}){3}$")) {
            throw new IllegalArgumentException("Adresse IP invalide : " + adresse);
        }
        this.adresse = adresse;
    }

    public String getAdresse() {
        return adresse;
    }

    @Override
    public String toString() {
        return adresse; 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AdresseIP)) return false;
        AdresseIP other = (AdresseIP) obj;
        return this.adresse.equals(other.adresse);
    }

    @Override
    public int hashCode() {
        return adresse.hashCode();
    }


}

