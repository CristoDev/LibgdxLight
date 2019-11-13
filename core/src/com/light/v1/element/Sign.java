package com.light.v1.element;

// @todo doit etendre une classe plus générique pour tous les objets d'une map et éviter ce vieux style de code
public class Sign {
    private String name, message;

    public Sign(String _name, String _message) {
        name=name;
        message=_message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
