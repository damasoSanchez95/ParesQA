import java.util.ArrayList;

public class AbstractTransformation {
	
	private String id;
	private	String type;
	private String nombre;
	private String owner;
	private String conexion;
	private ArrayList<Campo> camposTransformacion; //En los join sera el de salida, en una union sera el OUTPUT
	private ArrayList<Campo> camposTransformacionPrincipal; // en union sera el INPUT
	private ArrayList<Campo> camposTransformacionDetalle; //solo sirve para los Join
	private ArrayList<Boolean> listaOutputsCampos; //Sirve para ver si un campo tiene outPut o no, no lo he querido meter como un atributo del campo para no tocar lo que ya estaba hecho.
	
	
	public AbstractTransformation(String conexion, String owner, String id, String type, String nombre, ArrayList<Campo> camposTransformacion, ArrayList<Campo> camposTransformacionPrincipal, ArrayList<Campo> camposTransformacionDetalle, ArrayList<Boolean> listaOutputs ){
		this.id=id;
		this.type=type;
		this.nombre = nombre;
		this.conexion=conexion;
		this.owner=owner;
		this.camposTransformacion=camposTransformacion;
		this.camposTransformacionPrincipal = camposTransformacionPrincipal;
		this.camposTransformacionDetalle=camposTransformacionDetalle;
		this.listaOutputsCampos=listaOutputs;
	}
	
	
	public String getConexionTabla(){
		return this.conexion;
	}
	
	public void setConexionTabla(String conexion){
		this.conexion=conexion;
	}
	
	public String getOwnerTabla(){
		return this.owner;
	}
	
	public void setOwnerTabla(String owner){
		this.owner=owner;
	}
	
	public ArrayList<Campo> getCamposTransformacion(){
		return this.camposTransformacion;
	}
	
	public void setCamposTransformacion(ArrayList<Campo> camposTransformacion){
		this.camposTransformacion=camposTransformacion;
	}
	
	public ArrayList<Boolean> getListaOutputsCampos(){
		return this.listaOutputsCampos;
	}
	
	public void setListaOutputsCampos(ArrayList<Boolean> listaOutputsCampos){
		this.listaOutputsCampos=listaOutputsCampos;
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
		private boolean yaMostrado;
		private boolean yaMostradoOutput;
		private boolean yaMostradoOutput2;
		private boolean output;
		private boolean input;
		private boolean YaMostradoInput;
		private String escala;
		
		public Campo(boolean input,boolean output,String id, String columna,String feature, String name, String precision, String obdcType, String escala, boolean yaMostrado, boolean yaMostradoOutput, boolean yaMostradoOutput2, boolean YaMostradoInput){
			this.id=id;
			this.columna=columna;
			this.feature=feature;
			this.name=name;
			this.precision=precision;
			this.obdcType=obdcType;
			this.escala=escala;
			this.yaMostrado=yaMostrado;
			this.output=output;
			this.input=input;
			this.yaMostradoOutput=yaMostradoOutput;
			this.yaMostradoOutput2=yaMostradoOutput2;
			this.YaMostradoInput=YaMostradoInput;
		}
		
		public void setInput(boolean input){
			this.input=input;	
		}
		
		public Boolean getInput(){
			return input;
		}
		
		public void setOutput(boolean output){
			this.output=output;	
		}
		
		public Boolean getOutput(){
			return output;
		}
		
		public void setYaMostrado(boolean yaMostrado){
			this.yaMostrado=yaMostrado;	
		}
		
		public Boolean getYaMostrado(){
			return yaMostrado;
		}
		
		public void setYaMostradoOutput2(boolean yaMostradoOutpu2){
			this.yaMostradoOutput2=yaMostradoOutpu2;	
		}
		
		public Boolean getYaMostradoInput(){
			return YaMostradoInput;
		}
		
		public void setYaMostradoInput(boolean YaMostradoInput){
			this.YaMostradoInput=YaMostradoInput;	
		}
		
		public Boolean getYaMostradoOutput2(){
			return yaMostradoOutput2;
		}
		
		
		public void setYaMostradoOutput(boolean yaMostradoOutput){
			this.yaMostradoOutput=yaMostradoOutput;	
		}
		
		public Boolean getYaMostradoOutput(){
			return yaMostradoOutput;
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
