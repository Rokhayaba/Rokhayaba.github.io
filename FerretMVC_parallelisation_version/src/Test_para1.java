import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.concurrent.*;

public class Test_para1 {
	private static final int MYTHREADS = 100;
	private static BufferedReader br;
	private static String geneSymbol;
	private static String aa2;
	 public static void main(String[] args) throws Exception {
		 ThreadPoolExecutor  executor= new ThreadPoolExecutor(100,100,500, TimeUnit.MILLISECONDS,
		   		 new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
	//ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
	
		 //String [] Varid = {"rs111951747","rs9273410","rs9273411","rs9273412","rs9273413","rs9273414","rs9273415","rs9273416","rs537937592","rs9273417","rs9273418","rs9273419","rs1130457","rs1130456","rs12918","rs9273423","rs9273424","rs9273425","rs1130455","rs533116120","rs111951747","rs9273410","rs9273411","rs9273412","rs9273413","rs9273414","rs9273415","rs9273416","rs537937592","rs9273417","rs9273418","rs9273419","rs1130457","rs1130456","rs12918","rs9273423","rs9273424","rs9273425","rs1130455","rs533116120","rs111951747","rs9273410","rs9273411","rs9273412","rs9273413","rs9273414","rs9273415","rs9273416","rs537937592","rs9273417","rs9273418","rs9273419","rs1130457","rs1130456","rs12918","rs9273423","rs9273424","rs9273425","rs1130455","rs533116120","rs111951747","rs9273410","rs9273411","rs9273412","rs9273413","rs9273414","rs9273415","rs9273416","rs537937592","rs9273417","rs9273418","rs9273419","rs1130457","rs1130456","rs12918","rs9273423","rs9273424","rs9273425","rs1130455","rs533116120","rs111951747","rs9273410","rs9273411","rs9273412","rs9273413","rs9273414","rs9273415","rs9273416","rs537937592","rs9273417","rs9273418","rs9273419","rs1130457","rs1130456","rs12918","rs9273423","rs9273424","rs9273425","rs1130455","rs533116120","rs111951747","rs9273410","rs9273411","rs9273412","rs9273413","rs9273414","rs9273415","rs9273416","rs537937592","rs9273417","rs9273418","rs9273419","rs1130457","rs1130456","rs12918","rs9273423","rs9273424","rs9273425","rs1130455","rs533116120"};
		 String [] Varid = {"rs3176126","rs536093215","rs372816835","rs3176136","rs534957785","rs570890550","rs191096584","rs557474793","rs113252822","rs542015016","rs561688432","rs572395082","rs183647515","rs564360014","rs3176124","rs543047467","rs563439109","rs528933728","rs549574979","rs143450327","rs528644986","rs551683637","rs150954754","rs3176123","rs557031106","rs567754337","rs536323252","rs555554659","rs1042580","rs539785413","rs73611750","rs577992030","rs543416665","rs140801073","rs35315479","rs542392545","rs41282276","rs182592762","rs3176122","rs571614429","rs531063182","rs550976991","rs11696919","rs536637715","rs186669520","rs56354707","rs3176134","rs557945096","rs577889428","rs543317002","rs3176121","rs573584085","rs542603082","rs559059986","rs572561997","rs3176133","rs565111682","rs77670413","rs551028498","rs377311614","rs530087674","rs13306852","rs566980627","rs539089204","rs202143136","rs201487514","rs537228949","rs138861385","rs369357178","rs201936427","rs552503327","rs572623850","rs76135678","rs1800579","rs200575906","rs41348347","rs1042579","rs530378253","rs547020473","rs147377392","rs532533123","rs552731990","rs569331040","rs201927190","rs41400249","rs200712265","rs536111902","rs144853671","rs73901577","rs538228511","rs558416795","rs575288551","rs544488425","rs199987510","rs143748797","rs540961320","rs560743900","rs542108654","rs531781779","rs550522588","rs567418032","rs529826570","rs546519295","rs566429250","rs538290084","rs201268201","rs1800576","rs537919471","rs554351262","rs575112505","rs540826492","rs554544961","rs191884040","rs545878202","rs13306849","rs531725981","rs542345382","rs16984852"};
		 int cpt = 1;
		 try {
			 geneSymbol = "franc";
             for (int i = 0; i < Varid.length; i++) {
            	 System.out.println("\tCe qu'il y'a dans varid\t" + Varid[i].substring(2));
                 URL urlLocation = new URL("https://www.ncbi.nlm.nih.gov/projects/SNP/snp_gene.cgi?connect=&rs=" + Varid[i].substring(2));
                  br = null;
                 Runnable worker = new MyRunnable(urlLocation) { @Override public void run() {
     				
   		         BufferedReader brvep = null;
   		         try{
   		        	 br = new BufferedReader(new InputStreamReader(urlLocation.openStream()));
   		        	String currentString;
   	                 while ((currentString = br.readLine()) != null  && !currentString.contains("\"mrnaAcc\" : ")) 
   	                 {
   	                	 if (currentString.contains("\"geneSymbol\"")) {
                         	geneSymbol = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                         	System.out.print("\tgeneSymbol" + geneSymbol);
                         	 if (geneSymbol.equals("")) {
                              	geneSymbol = ".";
                              }
   	                	 }
   	                 	//System.out.println(currentString);
   	                 if (currentString.contains("\"aaCode\"")) {
   	              	aa2 = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                	
                	System.out.print("\t aacode2" + aa2);
                	
                	if (aa2.equals("")) {
                		System.out.print("Je rentree dans if protacc");
                		aa2 = ".";
                                         }
                     	 
   	                 	
   	                 	
   	                 }
   	                 }
   		        	 
   		        	 
   		        	 
   		         } catch (Exception e2) {
   		      	     e2.printStackTrace();
   		            }
   				
   				
   				 }
                 };
                 //System.out.println("Thread" + cpt);
                 //cpt ++;
                 executor.execute(worker);
                 
                 
	 
	 }
             executor.shutdown();
     		while (!executor.isTerminated()) {

     		    		}
     		
     		    		
     		    		System.out.println("\nFinished all threads");
             System.out.println("geneSymbol à la sortie de executor" + aa2);
             
             File testfile = new File("C:\\Users\\e300264\\Documents\\Projet_de_Stage2018_Ferret\\Fichiers\\testfile");
             PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(testfile)));
             writer.print("geneSymbol\t" + geneSymbol+ "\n");
             writer.print("aa2\t" + aa2+ "\n");
             writer.close();
     		//executor.shutdown();
     		
    		// Wait until all threads are finish
//    		while (!executor.isTerminated()) {
//     
//    		}
//    		
//    		System.out.println("\nFinished all threads");
             
		 }  catch (Exception e2) {
      	     e2.printStackTrace();
            }
		 

}
	 public static class MyRunnable implements Runnable {
			private final URL url;
	 
			MyRunnable(URL url) {
				this.url = url;
			}

			@Override
			public void run() {
//				BufferedReader br = null;
//		         BufferedReader brvep = null;
//		         try{
//		        	 br = new BufferedReader(new InputStreamReader(url.openStream()));
//		        	 String currentString;
//	                 while ((currentString = br.readLine()) != null  && !currentString.contains("\"mrnaAcc\" : ")) 
//	                 {
//	                 	System.out.println(currentString);
//	                 }
//		        	 
//		        	 
//		        	 
//		         } catch (Exception e2) {
//		      	     e2.printStackTrace();
//		            }
				// TODO Auto-generated method stub
				
				 }
	 }
}
				

