package fr.uvsq.cprog.collex;

public class CommandeRechercheIP implements Commande {
    private final Dns dns;
    private final String nomMachine;

    public CommandeRechercheIP(Dns dns, String nomMachine) {
        this.dns = dns;
        this.nomMachine = nomMachine;
    }

    @Override
    public void execute() {
        DnsItem item = dns.getItem(new NomMachine(nomMachine));
        if (item != null) {
            System.out.println("Adresse IP : " + item.getAdresseIP());
        } else {
            System.out.println("Nom de machine introuvable : " + nomMachine);
        }
    }
}
