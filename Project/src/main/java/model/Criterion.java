package model;

/**
 * Represents different criteria that are used for the Algorithms.
 */
public enum Criterion {

    Criterion_06_Food_Preference("criterion_06"),
    Criterion_07_Age_Difference("criterion_07"),
    Criterion_08_Sex_Diversity("criterion_08"),
    Criterion_09_Path_Length("criterion_09"),
    Criterion_10_Group_Amount("criterion_10");


    private final String token;

    Criterion(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
