package kz.booking.common.api;

public class BekbolatovZholamanFieldViolation {
    private final String field;
    private final String message;

    public BekbolatovZholamanFieldViolation(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}

