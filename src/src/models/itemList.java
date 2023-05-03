package models;

import java.net.http.HttpResponse;

import application.LoginController;
import application.SampleController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class itemList {
	private  String name;
    private  boolean isDone = false;
    private String id_Acta;
    private String token;
    private SampleController sc = new SampleController();
    private LoginController lc =new LoginController();
 
    public itemList(String name) {
        this.name = name;
    }

    public String getName() {
    	return this.name;
    }
    
    @Override
    public String toString() {
        return this.name + (this.isDone ? " FIRMADO !" : "");
    }

	public void isDoneProperty(boolean op) {
        this.isDone=op;
    }
    
	
	public boolean getDoneProperty(String id_Acta) throws InterruptedException {
		String x[] = name.split("-");
		token = lc.getoken();
		//System.out.println("http://127.0.0.1:4040/api/certificates/actasSinoidales/"+id_Acta+"/"+x[0]+"\nToken en itemList: "+token);
		/*HttpResponse<String> res = sc.getRequest(0,"http://127.0.0.1:4040/api/certificates/actasSinoidales/"+id_Acta+"/"+x[0],token);
		String s[]=res.body().split("[.!:;?{}]");
		if(Integer.parseInt(s[2])==1) {
			return true;
		}*/
		//sc.getClass(0,)
		return false;
	}
    
    
}
