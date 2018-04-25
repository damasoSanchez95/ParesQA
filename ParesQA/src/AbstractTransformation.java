import java.util.ArrayList;

public class AbstractTransformation {
	
	String id;
	String type;
	String active;
	String nombre;
	private ArrayList<campos> camposTransformacion;
	
	public AbstractTransformation(String id, String type, String active, String nombre, ArrayList<campos> camposTransformacion){
		this.id=id;
		this.type=type;
		this.active = active;
		this.nombre = nombre;
		this.camposTransformacion=camposTransformacion;
	}
	public ArrayList<campos> getCampos(){
		return this.camposTransformacion;
	}
	
	public void setCampos(ArrayList<campos> campoTransformacion){
		this.camposTransformacion=camposTransformacion;
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
		private String obdcType;
		
		public campos(String id, String columna,String feature, String name, String precision, String obdcType){
			this.id=id;
			this.columna=columna;
			this.feature=feature;
			this.name=name;
			this.precision=precision;
			this.obdcType=obdcType;
		}
		
		public String getId(){return id;}
		public void setID(String id) {
			this.id=id;
		}
		
		public String getName(){return name;}
		public void setName(String name) {
			this.name=name;
		}
		
		public String getPrecision(){return precision;}
		public void setPrecision(String precision) {
			this.precision=precision;
		}
		public String getColumna(){return columna;}
		public void setColumna(String columna) {
			this.columna=columna;
		}
		public String getFeature(){return feature;}
		public void setfeature(String feature) {
			this.feature=feature;
		}
		public String getType(){return obdcType;}
		public void setType(String type) {
			this.obdcType=type;
		}
	}
}
