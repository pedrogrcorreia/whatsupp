package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

public enum Properties {
    INITIAL_STATE("Initial State"),
    LOGIN_STATE("Login State"),
    REGISTER_STATE("Register State");


    private final String description;

    Properties(String s) { description = s; }
    @Override
    public String toString(){ return description; }
}
