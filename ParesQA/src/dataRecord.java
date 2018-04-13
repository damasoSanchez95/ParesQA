
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
	
	public String getNombre() {
		return nombre;
	}
	
	class campos{ //Clase para los campos de dicha dataRecord, columnas en el XML
		private String id;
		private String name;
		private String precision;
		
		public String getId(){return id;}
		public String getName(){return name;}
		public String getPrecision(){return precision;}
	}
}
