

public class CamposObjetosDataRecord {
		private String id;
		private String type;
		private String name;
		private String nullable;
		private String precision;	
		private String escala;
		
		
		public CamposObjetosDataRecord(String id, String type,String name, String nullable, String precision,String escala){
			this.id=id;
			this.type=type;		
			this.name=name;
			this.nullable=nullable;
			this.precision=precision;
			this.escala=escala;
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
		public String getType(){
			return type;
		}
		public void setType(String type){
			this.type=type;
		}
		public String getNullable(){
			return nullable;
			
		}
		
		public void setNullable(String nullable){
			this.nullable=nullable;
		}
		public String getPrecision(){
			return precision;
		}
		public void setPrecision(String precision){
			this.precision=precision;
		}
		
		public String getEscala(){
			return escala;
		}
		
		public void setEscala(String escala){
			this.escala=escala;
		}
	}