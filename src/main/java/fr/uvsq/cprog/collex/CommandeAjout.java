package fr.uvsq.cprog.collex;

import java.io.IOException;

public class CommandeAjout implements Commande {
    private final Dns dns;
    private final String nom;
    private final String ip;

    public CommandeAjout(Dns dns, String nom, String ip) {
        this.dns = dns;
        this.nom = nom;
        this.ip = ip;
    }

    @Override
    public void execute() {
        try {
            dns.addItem(new AdresseIP(ip), new NomMachine(nom));
            System.out.println("Ajout réussi : " + nom + " → " + ip);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : cette IP ou ce nom existe déjà.");
        } catch (IOException e) {
        System.out.println("Erreur lors de la sauvegarde du fichier DNS : " + e.getMessage());
    }
    }
}
