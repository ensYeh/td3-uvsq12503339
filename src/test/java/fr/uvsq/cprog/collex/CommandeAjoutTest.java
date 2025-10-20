package fr.uvsq.cprog.collex;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

public class CommandeAjoutTest {

    private Dns dns;
    private Path tempFile;

    @Before
    public void setUp() throws IOException {
        // fichier temporaire pour la base DNS
        tempFile = Files.createTempFile("dns", ".txt");
        dns = new Dns(tempFile);
    }

    @Test
    public void testAjoutNouvelleMachine() throws IOException {
        CommandeAjout cmd = new CommandeAjout(dns, "test.uvsq.fr", "193.51.31.91");
        cmd.execute();

        DnsItem item = dns.getItem(new NomMachine("test.uvsq.fr"));
        assertNotNull(item);
        assertEquals("193.51.31.91", item.getAdresseIP().toString());
    }

    @Test
    public void testAjoutDuplicateNom() throws IOException {
        // ajout initial
        dns.addItem(new AdresseIP("193.51.31.91"), new NomMachine("test.uvsq.fr"));

        CommandeAjout cmd = new CommandeAjout(dns, "test.uvsq.fr", "193.51.31.92");

        // ici, execute() gère l'erreur, donc on ne reçoit pas d'exception
        cmd.execute();

        // vérifier que l'adresse initiale reste intacte
        DnsItem item = dns.getItem(new NomMachine("test.uvsq.fr"));
        assertEquals("193.51.31.91", item.getAdresseIP().toString());
    }
}
