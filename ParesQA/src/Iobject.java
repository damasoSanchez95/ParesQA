import java.util.ArrayList;

public class Iobject {

	private String id;
	private String type;
	private String name;
	private boolean ref;
	private ArrayList<CamposObjetosDataRecord> listaCampos;

	
	public Iobject(String id, String name,String type, boolean ref, ArrayList<CamposObjetosDataRecord> listaCampos){
		
		this.id=id;
		this.name=name;
		this.type=type;
		this.ref=ref;
		this.listaCampos=listaCampos;
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
	public boolean getRef(){
		return ref;
	}
	public void setRef(boolean ref){
		this.ref=ref;
	}
	public ArrayList<CamposObjetosDataRecord> getCampos(){
		return this.listaCampos;
	}
	
	public void setCampos(ArrayList<CamposObjetosDataRecord> campos){
		this.listaCampos=campos;
	}
	
}