package com.bsuir.skripskaya.gummie.model.entities.user;

public enum UserRole {
    ADMIN,
    USER,
    VISITOR;

    @Override
    public String toString() {
        return switch (this) {
            case ADMIN -> "Admin";
            case USER -> "User";
            case VISITOR -> "Visitor";
            default -> "UNKNOWN";
        };
    }
}
