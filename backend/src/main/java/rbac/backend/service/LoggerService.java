package rbac.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggerService.class);

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message, Exception exception) {
        logger.error(message, exception);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }
}