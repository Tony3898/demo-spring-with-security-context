package in.tejas.rana.demospringwithsecuritycontext.controller;

import io.jsonwebtoken.JwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ControllerAdvice
public class ControllerExceptionHandler {

    static Log logger = LogFactory.getLog(ControllerExceptionHandler.class);

    private void logException(Exception exception) {
        logger.error(exception.getMessage(), exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception) {
        logException(exception);
        return "error/500";
    }

    @ExceptionHandler({SecurityException.class, JwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String invalidToken(Exception exception) {
        logException(exception);
        return "error/401";
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception exception) {
        logException(exception);
        return "error/404";
    }
}
