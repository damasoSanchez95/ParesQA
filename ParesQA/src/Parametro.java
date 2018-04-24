

public class Parametro {
	
	String id;
	String name;

	public Parametro(String id, String name){
		this.id = id;
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id=id;
	}
	
//	public String getValor(){
//		return valor;
//	}
//	public void SetValor(String valor){
//		this.valor=valor;
//	}
}
