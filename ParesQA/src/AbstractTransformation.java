
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
	
	public String getType() {
		return type;
	}
	
	public String getActive(){
		return active;
	}
	
	public String getNombre(){
		return nombre;
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
