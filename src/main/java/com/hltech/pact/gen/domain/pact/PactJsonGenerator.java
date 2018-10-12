package com.hltech.pact.gen.domain.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hltech.pact.gen.PactGenerationException;
import com.hltech.pact.gen.domain.pact.model.Pact;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Slf4j
public class PactJsonGenerator {

    private ObjectMapper objectMapper = new ObjectMapper();

    public void writePactFiles(File destinationDir, Collection<Pact> pacts) {
        pacts.forEach(pact -> writePactFile(destinationDir, pact));
    }

    private void writePactFile(File destinationDir, Pact pact) {
        if (destinationDir != null && !destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        final String pactFileName = pact.getConsumer().getName() + "-" + pact.getProvider().getName() + ".json";
        try {
            objectMapper.writeValue(new File(destinationDir, pactFileName), pact);
        } catch (IOException ex) {
            log.error("Unable to write {} to json file", pact);
            throw new PactGenerationException("Unable to write pact to json file", ex);
        }
    }
}
