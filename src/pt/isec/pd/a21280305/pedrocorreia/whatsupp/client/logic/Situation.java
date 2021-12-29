package pt.isec.pd.a21280305.pedrocorreia.whatsupp.client.logic;

public enum Situation {
    INITIAL_OPTION("Choose the option you want."),
    REGISTER_USER("Register a new user."),
    LOGIN_USER("Login user."),
    LOGGED_IN("User logged in."),
    ENTER_STATE("Client opened app."),
    CONTACT_SERVER_MANAGER("Contacting Server Manager."),
    SEARCH_USERS("Search user by username."),
    SEE_FRIENDS("See users friends."),
    CREATE_GROUP("Create a new group."),
    SEE_GROUPS("See users groups."),
    MESSAGE("Messages state"),
    MANAGE_GROUP("Group management");

    private final String description;

    Situation(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
