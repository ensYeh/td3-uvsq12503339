package fr.uvsq.cprog.collex;

import java.io.IOException;
import java.nio.file.Paths;

public class DnsApp {

    private final DnsTUI tui;
    private final Dns dns;

    public DnsApp(String fichierBaseDns) throws IOException {
        this.dns = new Dns(Paths.get(fichierBaseDns));
        this.tui = new DnsTUI(dns);
    }

    public void run() {
        boolean continuer = true;

        while (continuer) {
            Commande cmd = tui.nextCommande();

            if (cmd instanceof CommandeQuitter) {
                continuer = false;
            } else {
                // la commande s'occupe d'afficher le résultat
                cmd.execute();
            }
        }
    }


    public static void main(String[] args) {
        String fichierBase = "dns.txt"; // ou récupérer via args

        try {
            DnsApp app = new DnsApp(fichierBase);
            app.run();
        } catch (IOException e) {
            System.err.println("Erreur lors du démarrage de l'application : " + e.getMessage());
        }
    }
}
