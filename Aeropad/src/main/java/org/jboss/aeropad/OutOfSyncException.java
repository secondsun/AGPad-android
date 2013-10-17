package org.jboss.aeropad;

public class OutOfSyncException extends RuntimeException{

    public OutOfSyncException() {}
    public OutOfSyncException(String message) {
        super(message);
    }

}
