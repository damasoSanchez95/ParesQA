
public class AbstractTransformation {
	
	String id;
	String type;
	String active;
	String nombre;
	
	public AbstractTransformation(String id, String type, String active, String nombre){
		this.id=id;
		this.type=type;
		this.active = active;
		this.nombre = nombre;
	}
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id=id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type){
		this.type=type;
	}
	
	public String getActive(){
		return active;
	}
	public void setActive(String active){
		this.active=active;
	}
	
	public String getNombre(){
		return nombre;
	}
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
	
	class campos{ //Clase para los campos de dicha Transformacion, relacionalField en el XML
		private String id;
		private String columna;
		private String feature;
		private String name;
		private String precision;
		
		public String getId(){return id;}
		public String getName(){return name;}
		public String getPrecision(){return precision;}
		public String getColumna(){return columna;}
		public String getFeature(){return feature;}
	}
}
