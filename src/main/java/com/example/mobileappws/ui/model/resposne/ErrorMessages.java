package com.example.mobileappws.ui.model.resposne;

public enum ErrorMessages {
    MISSING_REQUIRED_FILED("Missing required field,Please check the documentation"),
    RECORD_ALREADY_EXISTS("Record already exist"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record does not exist"),
    AUTHENTICATION_FILED("Authentication failed"),
    COULD_NOT_UPDATE_RECORD("Could not update the record"),
    COULD_NOT_DELETED_RECORD("Could not delete the record"),
    EMAIL_ADDRESS_NOT_VERIFIED("E-mail address cannot be verified");

    private String errorMessage;

    ErrorMessages(String message) {
        errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
