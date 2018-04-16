
public class dataRecord {

	private String id;
	private String nombre;
	
	public dataRecord(String id, String nombre) {
		this.id=id;
		this.nombre=nombre;
	}
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id=id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
	
	class campos{ //Clase para los campos de dicha dataRecord, columnas en el XML
		private String id;
		private String name;
		private String precision;
		
		public String getId(){return id;}
		public String getName(){return name;}
		public String getPrecision(){return precision;}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public void setName(String name){
			this.name=name;
		}
		
		public void setPrecision(String precision){
			this.precision=precision;
		}
	}
}
