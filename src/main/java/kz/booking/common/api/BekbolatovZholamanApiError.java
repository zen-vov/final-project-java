package kz.booking.common.api;

import java.time.Instant;
import java.util.List;

public class BekbolatovZholamanApiError {
    private final Instant timestamp = Instant.now();
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final List<BekbolatovZholamanFieldViolation> violations;

    public BekbolatovZholamanApiError(int status, String error, String message, String path, List<BekbolatovZholamanFieldViolation> violations) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.violations = violations;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public List<BekbolatovZholamanFieldViolation> getViolations() {
        return violations;
    }
}

