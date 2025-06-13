package org.vitya0717.tiszaQuests.configuration.exceptions;

public class PlayerConfigurationExistsException extends Exception {

    private final String uuid;

    public PlayerConfigurationExistsException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
