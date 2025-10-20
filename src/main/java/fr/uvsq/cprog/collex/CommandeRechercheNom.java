package fr.uvsq.cprog.collex;

public class CommandeRechercheNom implements Commande {
    private final Dns dns;
    private final String adresseIP;

    public CommandeRechercheNom(Dns dns, String adresseIP) {
        this.dns = dns;
        this.adresseIP = adresseIP;
    }

    @Override
    public void execute() {
        DnsItem item = dns.getItem(new AdresseIP(adresseIP));
        if (item != null) {
            System.out.println("Nom de machine : " + item.getNomMachine());
        } else {
            System.out.println("Adresse IP introuvable : " + adresseIP);
        }
    }
}
