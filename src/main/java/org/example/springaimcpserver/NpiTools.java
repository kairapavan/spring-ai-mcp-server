package org.example.springaimcpserver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.springaimcpserver.Entity.Npi;
import org.example.springaimcpserver.Entity.NpiCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class NpiTools {

    private static final Logger logger = LoggerFactory.getLogger(NpiTools.class);
    private List<Npi> npiList;
    private final ObjectMapper objectMapper;

    @Value("classpath:/static/national_provider_identifier.json")
    Resource npiData;

    public NpiTools(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Tool(name = "npi-data", description = "List of unique identification number for covered health care providers")
    public List<Npi> getNpiList() {
        return npiList;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing NpiTools");
        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/static/national_provider_identifier.json")) {
            NpiCollection npiCollection = objectMapper.readValue(inputStream, NpiCollection.class);
            this.npiList = npiCollection.npiList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load npi-data.json", e);
        }
    }
}
// /Users/pavankaira/IdeaProjects/ai/spring-ai-mcp-server/build/libs/spring-ai-mcp-server-0.0.1-SNAPSHOT.jar