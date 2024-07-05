package org.pokesplash.gts.api.file;

/**
 * Class used for versioning
 */
public class Versioned {
    private String version; // File version.

    // Constructor.
    public Versioned(String version) {
        this.version = version;
    }

    // Getter
    public String getVersion() {
        return version;
    }
}
