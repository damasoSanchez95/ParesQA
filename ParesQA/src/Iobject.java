import java.util.ArrayList;


public class Iobject {

	String id;
	String name;
	String precision;
	String nullable;
	String type;
	private ArrayList<Iobject> camposTransformacion;

	
	public Iobject(String id, String name, String precision, String nullable, String type, ArrayList<Iobject> camposTransformacion){
		
		this.id=id;
		this.name=name;
		this.precision=precision;
		this.nullable=nullable;
		this.type=type;
		this.camposTransformacion=camposTransformacion;
	}
	
	public String gedId(){
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
	public String getPrecision(){
		return precision;
	}
	public void setPrecision(String precision){
		this.precision=precision;
	}
	public String getNullable(){
		return nullable;
	}
	public void setNullable(String nullable){
		this.nullable=nullable;
	}
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type=type;
	}
	public ArrayList<Iobject> getOjeto(){
		return this.camposTransformacion;
	}
	
	public void setCampos(ArrayList<Iobject> objeto){
		this.camposTransformacion=objeto;
	}
}
