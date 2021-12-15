package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

public enum Situation {
    INITIAL_OPTION("Choose the option you want."),
    REGISTER_USER("Register a new user."),
    LOGIN_USER("Login user."),
    LOGGED_IN("User logged in."),
    CONTACT_SERVER_MANAGER("Contacting Server Manager");

    private final String description;

    Situation(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
