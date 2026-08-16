package com.manish.randomgengames.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
public class NameCatalogDao {

    private final List<String> firstNames;
    private final List<String> lastNames;

    public NameCatalogDao(
            @Value("classpath:names/first-names.txt") Resource firstNamesResource,
            @Value("classpath:names/last-names.txt") Resource lastNamesResource
    ) {
        firstNames = readUniqueNames(firstNamesResource);
        lastNames = readUniqueNames(lastNamesResource);
    }

    public List<String> findAllFirstNames() {
        return firstNames;
    }

    public List<String> findAllLastNames() {
        return lastNames;
    }

    private List<String> readUniqueNames(Resource resource) {
        try {
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
            throw new IllegalStateException("Could not load name catalog: " + resource.getFilename(), exception);
        }
    }
}
