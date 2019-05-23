import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ecn.ferretmvc.model.FerretData;


public class Test_pattern {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String Varid = null;
    	String [] tst = {"indel_rs576345574_ACAGGCTCCTGGACGGAGC/A","rs576345574","rs3176126","rs536093215","rs372816835","rs3176136"};
    	for(int i=0; i< tst.length; i++){
        if (tst[i].contains("indel") == true){
     	   System.out.print("Nous avons un indel:" + tst[i]);
     	   Pattern p = Pattern.compile("^(indel_rs)([0-9]+)(.*)");
     	   Matcher m = p.matcher(tst[i]);
     	  if (m.find()) {
     		 Varid = m.group(2);
     		System.out.print("Varid indel modifié : " + m.group(2) + Varid);
     	  }
     	  
        }
        
        else {
     	   Varid = tst[i].substring(2);
     	   System.out.print("Varid no indel modifié : " + Varid);
        }

	}
	}

}
