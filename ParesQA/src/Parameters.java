
public class Parameters {
	
	String id;
	String name;
	String valor;
	
	public void parameters(String id, String name, String valor){
		this.id=id;
		this.name=name;
		this.valor=valor;
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getValor(){
		return valor;
	}
}
