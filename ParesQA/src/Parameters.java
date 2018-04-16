
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
	public void setId(String id){
		this.id=id;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	
	public String getValor(){
		return valor;
	}
	public void setValor(String valor){
		this.valor=valor;
	}
}
