import java.util.ArrayList;

public class DataRecord {

	private String id;
	private String name;
	private ArrayList<CamposObjetosDataRecord> listaCampos;

	public DataRecord(String id, String name, ArrayList<CamposObjetosDataRecord> listaCampos){
		this.id=id;
		this.name=name;
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
	
	public ArrayList<CamposObjetosDataRecord> getCampos(){
		return this.listaCampos;
	}
	
	public void setCampos(ArrayList<CamposObjetosDataRecord> campos){
		this.listaCampos=campos;
	}
}
