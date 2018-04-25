import java.util.ArrayList;

public class Instance {

	private String id;
	private String name;
	private String body;
	private ArrayList<Campos> camposInstancia;
	
	public Instance(String id, String name, String body, ArrayList<Campos> camposInstancia){
		this.id = id;
		this.name = name;
		this.body = body;
		this.camposInstancia=camposInstancia;
	}
	
	public ArrayList<Campos> getCampos(){
		return this.camposInstancia;
	}
	
	public void setCampos(ArrayList<Campos> campos){
		this.camposInstancia=campos;
	}
	
	public String getId(){
		return id; 
	}
	public void setId(String id){
		this.id=id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getBody(){
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	class Campos{ //Clase para los campos de dicha Instancia, NestedPort en el XML
		private String id;
		private String type;
		private String to_ports;
		private String structural_feature;
		private String from_port;
		private String TransformationField;
		
		
		public String getId(){return id;}
		public String getToPorts(){return to_ports;}
		public String getFromPorts(){return from_port;}
		public String getStructural_feature(){return structural_feature;}
		
		
		public Campos(String id, String to_ports,String structural_feature, String from_port, String TransformationField, String type){
			this.id=id;
			this.to_ports=to_ports;
			this.structural_feature=structural_feature;
			this.from_port=from_port;
			this.TransformationField = TransformationField;
			this.type=type;
		}
		
		public String getTransformationField(){
			return TransformationField;
		}
		
		public void setTransformationField(String TransformationField){
			this.TransformationField=TransformationField;
		}
		
		public void setId(String id){
			this.id=id;
		}
		
		public void setToPorts(String to_ports){
			this.to_ports = to_ports;
		}
		
		public void setFromPort(String from_port){
			this.from_port = from_port;
		}
		
		public void setStructural_feature(String structural_feature){
			this.structural_feature=structural_feature;
		}
	}	
}
