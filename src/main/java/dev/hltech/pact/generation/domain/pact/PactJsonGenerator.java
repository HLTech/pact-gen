package dev.hltech.pact.generation.domain.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.domain.pact.model.Pact;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class PactJsonGenerator {

    private ObjectMapper objectMapper = new ObjectMapper();

    public void generatePactFiles(File destinationDir, Collection<Pact> pacts) throws IOException {
        for (Pact pact : pacts) {
            generatePactFile(destinationDir, pact);
        }
    }

    private void generatePactFile(File destinationDir, Pact pact) throws IOException {
        if (destinationDir != null && !destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        final String pactFileName = pact.getConsumer().getName() + "-" + pact.getProvider().getName() + ".json";
        objectMapper.writeValue(new File(destinationDir, pactFileName), pact);
    }
}
