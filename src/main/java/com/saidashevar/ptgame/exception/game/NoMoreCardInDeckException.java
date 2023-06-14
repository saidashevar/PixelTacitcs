package com.saidashevar.ptgame.exception.game;

@SuppressWarnings("serial")
public class NoMoreCardInDeckException extends Exception {
    private String message;

    public NoMoreCardInDeckException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
