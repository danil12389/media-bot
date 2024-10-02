package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IODateCheckerService {

    private static final org. slf4j. Logger log
            = org. slf4j. LoggerFactory. getLogger(IODateCheckerService.class);

    public Map<String, String> getDomainsAndLastDate(String filePath) {
        Map<String, String> map = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> line.contains("="))
                    .forEach(line -> {
                        String[] keyValuePair = line.split("=", 2);
                        String key = keyValuePair[0];
                        String value = keyValuePair[1];
                            map.put(key, value);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public void updateTimestamp(String filePath, String domain, String date) {
        Path path = Paths.get(filePath);
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            List<String> list = stream.map(line -> line.contains(domain) ? domain + "=" + date : line)
                    .collect(Collectors.toList());
            Files.write(path, list, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("IOException for : " + path, e);
            e.printStackTrace();
        }
    }

}
