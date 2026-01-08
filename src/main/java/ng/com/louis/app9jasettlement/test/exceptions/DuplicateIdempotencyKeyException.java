package ng.com.louis.app9jasettlement.test.exceptions;

public class DuplicateIdempotencyKeyException extends RuntimeException {

    private final String idempotencyKey;

    public DuplicateIdempotencyKeyException(String idempotencyKey) {
        super(String.format("Idempotency key '%s' already used with different parameters", idempotencyKey));
        this.idempotencyKey = idempotencyKey;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }
}

