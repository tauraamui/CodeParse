package java.scope;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Namespace {
	private String name;
	private Namespace parent;
	
	public static void main(String[] args) {
		Namespace root = new Namespace("", null);
		Namespace dirt = root.create("World.Environment.Blocks.Dirt");
		Namespace tnt = root.create("World.Environment.Entities.TNT");
		Namespace foundtnt = root.get("World.Environment.Entities.Sand");
		String entitiesfullname = tnt.getFullName();
		
		
		ArrayList<String> xml = root.toXML();
		PrintWriter writer;
		try {
			writer = new PrintWriter("c:/hierarchy.xml", "UTF-8");
			for (String line : xml) 
				writer.write(line+"\n");
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<Namespace> children = new ArrayList<Namespace>(1);
	
	private Namespace(String name, Namespace parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public Namespace create(String namespace) {
		return insert(getNamespaces(namespace));
	}
	private Namespace insert(List<String> hierarchy) {
		if (hierarchy.size() == 0) return this;
		
		String nextname = hierarchy.get(0);
		for (Namespace ns : children) {
			if (ns.name.equalsIgnoreCase(nextname)) {
				hierarchy.remove(0);
				return ns.insert(hierarchy);
			}
		}
		
		hierarchy.remove(0);
		return attach(nextname).insert(hierarchy);
	}
	
	public Namespace get(String namespace) {
		return get(getNamespaces(namespace));
	}
	private Namespace get(List<String> hierarchy) {
		if (hierarchy.size() > 0) {
			String nextname = hierarchy.get(0);
			for (Namespace ns : children) {
				if (ns.name.equalsIgnoreCase(nextname)) {
					hierarchy.remove(0);
					return ns.get(hierarchy);
				}
			}
		}
		
		return this;
	}
	
	private Namespace attach(String name) {
		Namespace namespace = new Namespace(name, this);
		this.children.add(namespace);
		return namespace;
	}

	//TODO: BUG, last node appears twice at end
	public String getFullName() {
		if (parent == null) return name;
		return getParentNamespace() + '.' + name;
	}
	
	public List<Namespace> getHierarchy() {
		List<Namespace> tree = new ArrayList<Namespace>();
		if (parent == null) {
			tree.add(this);
			return tree;
		}
		
		tree.addAll(getHierarchy());
		tree.add(this);
		
		return tree;
	}
	
	public String getParentNamespace() {
		if (parent == null) return name;
		return parent.getParentNamespace() + '.' + name;
	}
	
	private ArrayList<String> getNamespaces(String fullname) {
		fullname = fullname.replace('\\', '.');
		fullname = fullname.replace('/', '.');
		String[] namespaces = fullname.split("\\.");
		ArrayList<String> namespacesarray = new ArrayList<String>(namespaces.length);
		for (String name : namespaces)
			namespacesarray.add(name);
		return namespacesarray;
	}
	
	public ArrayList<String> toXML() {
		return toXML(new ArrayList<String>(), 0);
	}
	private ArrayList<String> toXML(ArrayList<String> xml, int numtabs) {
		String tabs = "";
		for (int i=0; i<numtabs; i++)
			tabs += "	";
		
		xml.add(tabs + "<namespace name=\"" + name +"\">");
		if (children.size() > 0) {
			for (Namespace ns : children)
				ns.toXML(xml, numtabs+1);
		}
		xml.add(tabs + "</namespace>");
		
		return xml;
	}
	
	public Namespace getParent() {
		return parent;
	}
}