package com.manish.randomgengames.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
public class NameCatalogDao {

    // Holds all unique first names loaded from the first-name file.
    private final List<String> firstNames;

    // Holds all unique last names loaded from the last-name file.
    private final List<String> lastNames;

    /**
     * Loads the first-name and last-name files when the DAO is created.
     *
     * @param firstNamesResource file containing first names
     * @param lastNamesResource file containing last names
     */
    public NameCatalogDao(
            @Value("classpath:names/first-names.txt") Resource firstNamesResource,
            @Value("classpath:names/last-names.txt") Resource lastNamesResource
    ) {
        firstNames = readUniqueNames(firstNamesResource);
        lastNames = readUniqueNames(lastNamesResource);
    }

    /**
     * Returns all loaded first names.
     *
     * @return list of first names
     */
    public List<String> findAllFirstNames() {
        return firstNames;
    }

    /**
     * Returns all loaded last names.
     *
     * @return list of last names
     */
    public List<String> findAllLastNames() {
        return lastNames;
    }

    /**
     * Reads a name file, cleans each line, and removes duplicate names.
     *
     * @param resource name file to read
     * @return cleaned list of unique names
     */
    private List<String> readUniqueNames(Resource resource) {
        try {
            // Contains the cleaned, non-empty, unique names from the file.
            List<String> names = resource.getContentAsString(StandardCharsets.UTF_8)
                    .lines()
                    .map(String::trim)
                    .filter(name -> !name.isEmpty())
                    .distinct()
                    .toList();

            if (names.isEmpty()) {
                throw new IllegalStateException("Name catalog must not be empty: " + resource.getFilename());
            }

            return names;
        } catch (IOException exception) {
            // The exception contains the file-reading error from the catalog.
            throw new IllegalStateException("Could not load name catalog: " + resource.getFilename(), exception);
        }
    }
}
