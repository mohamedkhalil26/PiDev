package tn.esprit.entites;

public enum Role {
    passager,chauffeur,admin;
    @Override
    public String toString() {
        return this.name();
    }

}
