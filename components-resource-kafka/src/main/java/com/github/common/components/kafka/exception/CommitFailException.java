package com.github.common.components.kafka.exception;

/**
 * CommitFailException
 *
 */
public class CommitFailException extends RuntimeException {

    public CommitFailException() {
        super();
    }

    public CommitFailException(String errorMessage) {
        super(errorMessage);
    }

    public CommitFailException(Throwable t) {
        super(t);
    }

    public CommitFailException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }

}