import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.*;


public class Test_para1 {
	private static final int MYTHREADS = 100;
	private static BufferedReader br;
	 public static void main(String[] args) throws Exception {
		 ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		
		 String [] Varid = {"rs111951747","rs9273410","rs9273411","rs9273412","rs9273413","rs9273414","rs9273415","rs9273416","rs537937592","rs9273417","rs9273418","rs9273419","rs1130457","rs1130456","rs12918","rs9273423","rs9273424","rs9273425","rs1130455","rs533116120"};
		 
		 try {
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
   	                 	System.out.println(currentString);
   	                 }
   		        	 
   		        	 
   		        	 
   		         } catch (Exception e2) {
   		      	     e2.printStackTrace();
   		            }
   				
   				
   				 }
                 };
                 executor.execute(worker);
                 
                 
	 
	 }
     		executor.shutdown();
    		// Wait until all threads are finish
    		while (!executor.isTerminated()) {
     
    		}
    		System.out.println("\nFinished all threads");
             
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
				

