package java.scope;

public enum Visibility {
	DEFAULT,
	PUBLIC,
	PRIVATE,
	PROTECTED;
	
	public Visibility getFromSigniture(String signiture) {
		if (signiture.contains("public")) return PUBLIC;
		if (signiture.contains("private")) return PRIVATE;
		if (signiture.contains("protected")) return PROTECTED;
		return DEFAULT;
	}
}
