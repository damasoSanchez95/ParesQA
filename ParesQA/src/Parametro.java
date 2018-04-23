

public class Parametro {
	
	String name;
	String valor;

	public Parametro(String name, String valor){
		this.name=name;
		this.valor=valor;
	}
	
	public String getName(){
		return name;
	}
	public void SetName(String name){
		this.name=name;
	}
	
	public String getValor(){
		return valor;
	}
	public void SetValor(String valor){
		this.valor=valor;
	}
}
