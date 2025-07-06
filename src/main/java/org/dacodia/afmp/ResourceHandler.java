package org.dacodia.afmp;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ResourceHandler {

    Map<String, String> resources; //swap to a tree

    public ResourceHandler() {
        resources = new HashMap<>();
    }

    //    public byte[] get() {
//
//    }

    public String getAsString(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/maxgraf/Documents/GitHub/afmp/src/main/resources/" + path));
            StringBuilder builder = new StringBuilder();
            for(String line : reader.lines().toList()) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
}
