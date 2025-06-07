package org.vitya0717.tiszaQuests.utils;


import org.vitya0717.tiszaQuests.main.Main;

public class QuestValidator {

    private final Main instance;
    private boolean valid = false;
    private String errorMessage;

    public QuestValidator(Main instance){
        this.instance=instance;
    }


    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Main getInstance() {
        return instance;
    }
}
