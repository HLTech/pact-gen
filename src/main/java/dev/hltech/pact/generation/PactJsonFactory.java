package dev.hltech.pact.generation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.model.Pact;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public class PactJsonFactory {

    private ObjectMapper objectMapper = new ObjectMapper();

    public void generatePactFiles(Collection<Pact> pacts) throws IOException {
        for (Pact pact : pacts) {
            generatePactFile(pact);
        }
    }

    private void generatePactFile(Pact pact) throws IOException {
        File pactsDirectory = new File("build/pacts");
        if (pactsDirectory.exists() || new File("build/pacts").mkdirs()) {
            objectMapper.writeValue(new File("build/pacts/pact-file-" + UUID.randomUUID() + ".json"), pact);
        } else {
            throw new IllegalStateException("Unable to generate pact file");
        }
    }
}
