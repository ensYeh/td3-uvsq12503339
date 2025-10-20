package fr.uvsq.cprog.collex;

import java.util.List;

public class CommandeRechercheDomaine implements Commande {
    private final Dns dns;
    private final String domaine;

    public CommandeRechercheDomaine(Dns dns, String domaine) {
        this.dns = dns;
        this.domaine = domaine;
    }

    @Override
    public void execute() {
        List<DnsItem> items = dns.getItems(domaine);
        if (items.isEmpty()) {
            System.out.println("Aucune machine trouvée pour le domaine : " + domaine);
        } else {
            items.forEach(i -> System.out.println(i.getNomMachine() + " → " + i.getAdresseIP()));
        }
    }
}
