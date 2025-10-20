package fr.uvsq.cprog.collex;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dns {

    private final Map<AdresseIP, DnsItem> ipToItem = new HashMap<>();
    private final Map<NomMachine, DnsItem> nameToItem = new HashMap<>();
    private final Path filePath;

    public Dns(Path filePath) throws IOException {
        this.filePath = filePath;
        loadDatabase();
    }



    private void loadDatabase() throws IOException {
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] parts = line.split("\\s+");
            if (parts.length != 2) continue;

            AdresseIP ip = new AdresseIP(parts[1]);
            NomMachine nom = new NomMachine(parts[0]);
            DnsItem item = new DnsItem(ip, nom);
            ipToItem.put(ip, item);
            nameToItem.put(nom, item);
        }
    }

    public DnsItem getItem(AdresseIP ip) {
        return ipToItem.get(ip);
    }

    public DnsItem getItem(NomMachine nom) {
        return nameToItem.get(nom);
    }

    
    public List<DnsItem> getItems(String domaine) {
        Stream<DnsItem> stream = nameToItem.values().stream()
            .filter(item -> item.getNomMachine().getDomaine().equals(domaine));

        return stream
            .sorted(Comparator.comparing(i -> i.getNomMachine().getNomComplet()))
            .collect(Collectors.toList());
    }

    public void addItem(AdresseIP ip, NomMachine nom) throws IOException {
        if (ipToItem.containsKey(ip)) {
            throw new IllegalArgumentException("ERREUR : L’adresse IP existe déjà !");
        }
        if (nameToItem.containsKey(nom)) {
            throw new IllegalArgumentException("ERREUR : Le nom de machine existe déjà !");
        }

        DnsItem item = new DnsItem(ip, nom);
        ipToItem.put(ip, item);
        nameToItem.put(nom, item);

        saveDatabase();
    }

    /**
     * Sauvegarde la base DNS dans le fichier texte.
     */
    private void saveDatabase() throws IOException {
        List<String> lines = nameToItem.values().stream()
            .sorted(Comparator.comparing(i -> i.getNomMachine().getNomComplet()))
            .map(i -> i.getNomMachine() + " " + i.getAdresseIP())
            .collect(Collectors.toList());
        Files.write(filePath, lines);
    }
}
