package aiss.videominer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CaptionNotFoundException extends RuntimeException {
    public CaptionNotFoundException(String id) {
        super("Could not find caption with id: " + id);
    }

}
