package org.pokesplash.gts.config.options;

public class Webhook {
    private boolean useWebhooks;
    private String url;

    public Webhook() {
        useWebhooks = false;
        url = "WEBHOOK_URL";
    }

    public boolean isUseWebhooks() {
        return useWebhooks;
    }

    public String getUrl() {
        return url;
    }
}
