import java.util.ArrayList;

public class AbstractTransformation {
	
	private String id;
	private	String type;
	private String nombre;
	private ArrayList<Campo> camposTransformacion; //En los join sera el de salida, en una union sera el OUTPUT
	private ArrayList<Campo> camposTransformacionPrincipal; // en union sera el INPUT
	private ArrayList<Campo> camposTransformacionDetalle; //solo sirve para los Join
	
	
	public AbstractTransformation(String id, String type, String nombre, ArrayList<Campo> camposTransformacion, ArrayList<Campo> camposTransformacionPrincipal, ArrayList<Campo> camposTransformacionDetalle ){
		this.id=id;
		this.type=type;
		this.nombre = nombre;
		this.camposTransformacion=camposTransformacion;
		this.camposTransformacionPrincipal = camposTransformacionPrincipal;
		this.camposTransformacionDetalle=camposTransformacionDetalle;
	}
	
	public ArrayList<Campo> getCamposTransformacion(){
		return this.camposTransformacion;
	}
	
	public void setCamposTransformacion(ArrayList<Campo> camposTransformacion){
		this.camposTransformacion=camposTransformacion;
	}
	
	public ArrayList<Campo> getCamposTransformacionPrincipal(){
		return this.camposTransformacionPrincipal;
	}
	
	public void setCamposTransformacionPrincipal(ArrayList<Campo> camposTransformacionPrincipal){
		this.camposTransformacionPrincipal=camposTransformacionPrincipal;
	}
	
	public ArrayList<Campo> getCamposTransformacionDetalle(){
		return this.camposTransformacionDetalle;
	}
	
	public void setCamposTransformacionDetalle(ArrayList<Campo> camposTransformacionDetalle){
		this.camposTransformacionDetalle=camposTransformacionDetalle;
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
	
	public String getNombre(){
		return nombre;
	}
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
	
	class Campo{ //Clase para los campos de dicha Transformacion, relacionalField en el XML
		private String id;
		private String columna;
		private String feature;
		private String name;
		private String precision;
		private String obdcType;
		private String escala;
		
		public Campo(String id, String columna,String feature, String name, String precision, String obdcType, String escala){
			this.id=id;
			this.columna=columna;
			this.feature=feature;
			this.name=name;
			this.precision=precision;
			this.obdcType=obdcType;
			this.escala=escala;
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
		
		public String getEscala(){return escala;}
		
		public void setEscala(String escala) {
			this.escala=escala;
		}
		
		
		public void setType(String type) {
			this.obdcType=type;
		}
	}
}
