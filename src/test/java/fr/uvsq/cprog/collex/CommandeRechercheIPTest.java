package fr.uvsq.cprog.collex;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandeRechercheIPTest {

    private Dns dns;
    private Path tempFile;

    // Pour capturer la sortie console
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() throws IOException {
        // rediriger System.out vers outContent
        System.setOut(new PrintStream(outContent));

        // fichier temporaire pour la base DNS
        tempFile = Files.createTempFile("dns", ".txt");
        dns = new Dns(tempFile);

        // ajouter une machine test
        dns.addItem(new AdresseIP("193.51.31.90"), new NomMachine("www.uvsq.fr"));
    }

    @After
    public void tearDown() {
        // réinitialiser System.out
        System.setOut(originalOut);
    }

   @Test
    public void testRechercheIPExistante() {
        CommandeRechercheIP cmd = new CommandeRechercheIP(dns, "www.uvsq.fr");
    
        // exécution de la commande
        cmd.execute();

        // vérifier ce qui a été affiché
        String output = outContent.toString().trim();
        assertEquals("Adresse IP : 193.51.31.90", output);
    }

}
