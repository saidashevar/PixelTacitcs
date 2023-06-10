package com.saidashevar.ptgame.exception.game;

@SuppressWarnings("serial")
public class NoMoreActionsLeftException extends Exception {
    private String message;

    public NoMoreActionsLeftException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
