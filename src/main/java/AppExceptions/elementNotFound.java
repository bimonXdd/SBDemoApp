package AppExceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class elementNotFound extends Exception{
    String element;
    public elementNotFound(String element){
        this.element = element;
    }
    public void logError(){
        Logger logger = LoggerFactory.getLogger(elementNotFound.class);
        logger.error("Element not found: " + element);

    }
}
