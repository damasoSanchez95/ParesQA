
public class Instance {

	String id;
	String name;
	String body;
	
	public Instance(String id, String name, String body){
		this.id = id;
		this.name = name;
		this.body = body;
	}
	
	asd;
	public String getId(){
		return id; 
	}
	
	public String getName(){
		return name;
	}
	
	public String body(){
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	class Campos{ //Clase para los campos de dicha Instancia, NestedPort en el XML
		private String id;
		private String to_ports;
		private String structural_feature;
		
		public String getId(){return id;}
		public String getToPorts(){return to_ports;}
		public String getStructural_feature(){return structural_feature;}
	}	
}
