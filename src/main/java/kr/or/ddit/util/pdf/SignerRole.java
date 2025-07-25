package kr.or.ddit.util.pdf;

public enum SignerRole {
    AGENT,
    LESSOR,
    LESSEE;

    public static SignerRole from(String role) {
        return switch (role.toUpperCase()) {
            case "AGENT" -> AGENT;
            case "LESSOR" -> LESSOR;
            case "LESSEE" -> LESSEE;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}