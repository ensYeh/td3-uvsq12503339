package fr.uvsq.cprog.collex;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Classe DnsTUI (Text User Interface)
 * Gère les interactions avec l'utilisateur pour manipuler le DNS.
 */
public class DnsTui {

    private final Scanner scanner;

    /**
     * Constructeur par défaut.
     */
    public DnsTui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Lit une commande de l'utilisateur, l'analyse et l'exécute sur le DNS.
     *
     * @param dns instance du DNS à manipuler
     * @return false si l'utilisateur souhaite quitter, true sinon
     */
    public boolean nextCommande(Dns dns) {
        System.out.print("> ");
        String ligne = scanner.nextLine().trim();

        if (ligne.isEmpty()) {
            return true;
        }

        // Commande pour quitter
        if (ligne.equalsIgnoreCase("quit") || ligne.equalsIgnoreCase("exit")) {
            affiche("Fermeture du programme.");
            return false;
        }

        try {
            // Commande add
            if (ligne.startsWith("add ")) {
                String[] parts = ligne.split("\\s+");
                if (parts.length != 3) {
                    affiche("ERREUR : Syntaxe : add <adresse.IP> <nom.machine>");
                    return true;
                }
                AdresseIP ip = new AdresseIP(parts[1]);
                NomMachine nom = new NomMachine(parts[2]);
                dns.addItem(ip, nom);
                affiche("Ajout réussi : " + ip + " " + nom);
                return true;
            }

            // Commande ls
            if (ligne.startsWith("ls ")) {
                String[] parts = ligne.split("\\s+");
                boolean triParAdresse = false;
                String domaine;

                if (parts[1].equals("-a")) {
                    if (parts.length < 3) {
                        affiche("ERREUR : Syntaxe : ls [-a] <domaine>");
                        return true;
                    }
                    triParAdresse = true;
                    domaine = parts[2];
                } else {
                    domaine = parts[1];
                }

                List<DnsItem> items = dns.getItems(domaine);
                if (triParAdresse) {
                    items = items.stream()
                            .sorted(Comparator.comparing(i -> i.getAdresseIP().getAdresse()))
                            .collect(Collectors.toList());
                }
                if (items.isEmpty()) {
                    affiche("Aucune machine trouvée pour le domaine " + domaine);
                } else {
                    items.forEach(i ->
                            affiche(i.getAdresseIP() + " " + i.getNomMachine()));
                }
                return true;
            }

            // Commande : adresse IP → nom de machine
            if (ligne.matches("^\\d{1,3}(\\.\\d{1,3}){3}$")) {
                AdresseIP ip = new AdresseIP(ligne);
                DnsItem item = dns.getItem(ip);
                if (item == null) {
                    affiche("Adresse inconnue.");
                } else {
                    affiche(item.getNomMachine().toString());
                }
                return true;
            }

            // Commande : nom de machine → adresse IP
            if (ligne.contains(".")) {
                NomMachine nom = new NomMachine(ligne);
                DnsItem item = dns.getItem(nom);
                if (item == null) {
                    affiche("Nom inconnu.");
                } else {
                    affiche(item.getAdresseIP().toString());
                }
                return true;
            }

            affiche("Commande non reconnue : " + ligne);
        } catch (IllegalArgumentException e) {
            affiche("ERREUR : " + e.getMessage());
        } catch (IOException e) {
            affiche("ERREUR d’accès au fichier : " + e.getMessage());
        }

        return true;
    }

    /**
     * Affiche un message dans la console.
     *
     * @param texte le texte à afficher
     */
    public void affiche(String texte) {
        System.out.println(texte);
    }

    /**
     * Point d'entrée principal pour exécuter l’interface.
     *
     * @param args arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        try {
            Dns dns = new Dns(java.nio.file.Path.of("src/main/resources/dns.txt"));
            DnsTui tui = new DnsTui();
            boolean continuer = true;
            System.out.println("=== Simulation DNS (tapez 'quit' pour quitter) ===");
            while (continuer) {
                continuer = tui.nextCommande(dns);
            }
        } catch (IOException e) {
            System.err.println("Erreur de chargement de la base DNS : " + e.getMessage());
        }
    }
}
