package fr.uvsq.cprog.collex;

import java.util.Scanner;

public class DnsTUI {
    private final Dns dns;
    private final Scanner scanner = new Scanner(System.in);

    public DnsTUI(Dns dns) {
        this.dns = dns;
    }

    public Commande nextCommande() {
        System.out.print("> ");
        String input = scanner.nextLine().trim();
        String[] parts = input.split("\\s+");

        if (parts.length == 0) return null;

        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "ip":
                if (parts.length == 2) return new CommandeRechercheIP(dns, parts[1]);
                break;
            case "nom":
                if (parts.length == 2) return new CommandeRechercheNom(dns, parts[1]);
                break;
            case "domaine":
                if (parts.length == 2) return new CommandeRechercheDomaine(dns, parts[1]);
                break;
            case "ajout":
                if (parts.length == 3) return new CommandeAjout(dns, parts[1], parts[2]);
                break;
            case "quit":
            case "exit":
                return new CommandeQuitter();
            default:
                System.out.println("Commande inconnue : " + cmd);
        }
        return null;
    }

    public void affiche(String message) {
        System.out.println(message);
    }

    public void start() {
        while (true) {
            Commande commande = nextCommande();
            if (commande != null) {
                commande.execute();
            }
        }
    }
}
