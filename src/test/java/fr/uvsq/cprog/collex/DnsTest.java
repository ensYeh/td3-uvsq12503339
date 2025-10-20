package fr.uvsq.cprog.collex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class DnsTest {

    private Path tempFile;
    private Dns dns;

    @Before
    public void setUp() throws IOException {
        // Création d’un fichier temporaire pour tester la base DNS
        tempFile = Files.createTempFile("dns_test", ".txt");

        // Contenu de base : 3 machines
        String data = String.join("\n",
                "www.uvsq.fr 193.51.31.90",
                "poste.uvsq.fr 193.51.31.154",
                "ecampus.uvsq.fr 193.51.25.12"
        );
        Files.write(tempFile, data.getBytes());

        dns = new Dns(tempFile);
    }

    @After
    public void cleanUp() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testGetItemByNomMachine() {
        NomMachine nom = new NomMachine("www.uvsq.fr");
        DnsItem item = dns.getItem(nom);

        assertNotNull(item);
        assertEquals("193.51.31.90", item.getAdresseIP().toString());
    }

    @Test
    public void testGetItemByAdresseIP() {
        AdresseIP ip = new AdresseIP("193.51.25.12");
        DnsItem item = dns.getItem(ip);

        assertNotNull(item);
        assertEquals("ecampus.uvsq.fr", item.getNomMachine().toString());
    }

    @Test
    public void testGetItemsByDomaine() {
        List<DnsItem> items = dns.getItems("uvsq.fr");

        assertEquals(3, items.size());
        assertEquals("ecampus.uvsq.fr", items.get(0).getNomMachine().toString()); // tri alphabétique
    }

    @Test
    public void testAddItemSuccess() throws IOException {
        AdresseIP newIp = new AdresseIP("193.51.25.24");
        NomMachine newNom = new NomMachine("pikachu.uvsq.fr");

        dns.addItem(newIp, newNom);

        DnsItem added = dns.getItem(newNom);
        assertNotNull(added);
        assertEquals("193.51.25.24", added.getAdresseIP().toString());

        // Vérifie que le fichier a bien été mis à jour
        String fileContent = String.join("\n", Files.readAllLines(tempFile));
        assertTrue(fileContent.contains("pikachu.uvsq.fr 193.51.25.24"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemDuplicateNameThrowsError() throws IOException {
        AdresseIP newIp = new AdresseIP("193.51.99.99");
        NomMachine existingNom = new NomMachine("www.uvsq.fr");

        dns.addItem(newIp, existingNom);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemDuplicateIpThrowsError() throws IOException {
        AdresseIP existingIp = new AdresseIP("193.51.31.90");
        NomMachine newNom = new NomMachine("newhost.uvsq.fr");

        dns.addItem(existingIp, newNom);
    }
}
