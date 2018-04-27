import java.util.ArrayList;

public class Instance {

	private String id;
	private String type;
	private String name;
	private String body;
	private String fromInstance;
	private String toInstance;
	private ArrayList<Campos> camposInstancia;
	
	public Instance(String id, String type, String name, String body, String fromInstance, String toInstance, ArrayList<Campos> camposInstancia){
		this.id = id;
		this.type=type;
		this.name = name;
		this.body = body;
		this.fromInstance=fromInstance;
		this.toInstance=toInstance;
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
	public String getType(){
		return type; 
	}
	public void setType(String type){
		this.type=type;
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
	public String getfromInstance(){
		return fromInstance; 
	}
	public void setfromInstance(String fromInstance){
		this.fromInstance=fromInstance;
	}
	public String getToInstance(){
		return toInstance; 
	}
	public void setToInstance(String toInstance){
		this.toInstance=toInstance;
	}
	
	class Campos{ //Clase para los campos de dicha Instancia, NestedPort en el XML
		private String id;
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
