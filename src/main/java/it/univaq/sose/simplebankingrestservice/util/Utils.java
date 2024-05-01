package it.univaq.sose.simplebankingrestservice.util;

import javax.ws.rs.core.MediaType;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Class Utils");
    }

    public static MediaType getFirstMediaType(String acceptHeader) {
        if (acceptHeader != null && !acceptHeader.isEmpty()) {
            String[] types = acceptHeader.split(",");
            if (types.length > 0) {
                return MediaType.valueOf(types[0].trim());
            }
        }
        // Default Json Type if the header is invalid or empty
        return MediaType.APPLICATION_JSON_TYPE;
    }
}
