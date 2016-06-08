package java.scope;

public class Class {
	public String Name;
	public Visibility Visibility;
	public boolean isStatic;
	public String returnType;
	// TODO: List of methods
	// TOOD: List of fields
	
	public Class(String signiture) {
		Name = signiture.substring(signiture.lastIndexOf(""), signiture.lastIndexOf("("));
		returnType = signiture.replace(Name, "").substring(signiture.lastIndexOf(" "));
		isStatic = signiture.contains("static");
		Visibility = Visibility.getFromSigniture(signiture);
	}
}