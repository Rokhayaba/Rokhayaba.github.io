package com.ecn.ferretmvc.model;
import com.ecn.ferretmvc.model.FerretData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AnnotThreading implements Runnable{
//	public static URL url;
//	public static URL urlvep;
//	public String [] Varlist;
	//public static volatile String [] test;
	public static volatile Map<Integer, String> StockLineAnnotdef;
	public static volatile Map<Integer, String> StockLineAnnotadv;
	private int cpt;
	private String Varid;
	private Connection conn;
	
		public AnnotThreading (int cpt,String Varid,Connection conn) {
			
		 if(AnnotThreading.StockLineAnnotdef == null && AnnotThreading.StockLineAnnotadv == null )
			 AnnotThreading.StockLineAnnotdef = new HashMap<>();
		 AnnotThreading.StockLineAnnotadv = new HashMap<>();
		 
		 this.cpt = cpt; 
		 this.Varid = Varid;
		 this.conn = conn;
		}

		@Override
		public void run() {
			 String geneSymbol = "unavailable";
			 String geneId = "unavailable";
			 String proteinPos = "unavailable";
			 String proteinAcc = "unavailable";
			 String fxnName = "unavailable";
			 String aa1 = "lable";
			 String aa2 = "unavai";
			 String protein_end = "unavailable";
			 String sift_score = null;
			 String sift_score1 = null;
			 String polyphen_score = null;
			 String polyphen_score1 = null;
			 String sift_prediction = null;
			 String sift_prediction1 = null;
			 String polyphen_prediction = null;
			 String polyphen_prediction1 = null;
			 Boolean passee = false;
			 Boolean passee1 = false;
        	 BufferedReader br = null;
        	 BufferedReader brvep = null;
				String regulomeDB_score = null;
				String rgdb_score_signification = null;
				String SIFT = null;
				String SIFT_P = null;
				String Polyphen = null;
				String Polyphen_P = null;
				String Provean = null;
				String Provean_P = null;
				String CADD = null;
		
				


         	  

        	
        	
        	try {
            	
                URL urlLocation = new URL("https://www.ncbi.nlm.nih.gov/projects/SNP/snp_gene.cgi?connect=&rs=" + Varid +"&api_key=21a24e28baf4d0ce4a9eca9b9c8210286909");
//                String server = "https://rest.ensembl.org";
//    		    String ext = "/vep/human/id/rs" + Varid+ "?content-type=application/json";
//    		    URL urlvep = new URL(server + ext);
    		    URLConnection connection = urlLocation.openConnection();
    		    //connection.setConnectTimeout(1000);
//    		    HttpURLConnection httpConnection = (HttpURLConnection)connection;
//    		    httpConnection.setConnectionTimeout( httpParameters, 20000 );
//    		    httpConnection.setSoTimeout( httpParameters, 42000 );
    		    br = null;
//    		    brvep = null;
    		   
//    		    URLConnection connection = urlvep.openConnection();
//    		    HttpURLConnection httpConnection = (HttpURLConnection)connection;
//       		
//    		    httpConnection.setRequestProperty("Content-Type", "application/json");
    		    	
    		    try{
    		    	
    		    	br = new BufferedReader(new InputStreamReader(urlLocation.openStream()));

                
                String currentString;

                    while ((currentString = br.readLine()) != null  && !currentString.contains("\"mrnaAcc\" : ")) 
                    {
                        if (currentString.contains("\"geneSymbol\"")) {
                        	geneSymbol = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                        	 if (geneSymbol.equals("")) {
                             	geneSymbol = ".";
                             }
                        	 
                    }
                       
                        
                        if (currentString.contains("\"geneId\"")) {
                        	geneId = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                        	if (geneId.equals("")) {
                             	geneId = ".";
                             }
                        }
                       
                        
                        
                        if (currentString.contains("\"proteinPos\"")) {
                        	proteinPos = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                        	if (proteinPos.equals("")) {
                             	proteinPos = ".";
                             }
                        	
                        	}
                        	
                        
                        if (currentString.contains("\"proteinAcc\"")) {
                        	proteinAcc = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                        	if (proteinAcc.equals("")) {
                            	proteinAcc = ".";
                            }
                        }
                        
                    
                        if (currentString.contains("\"aaCode\"") && passee1 == false) {
                        	aa1 = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                        	if (aa1.equals("")) {
                        		aa1 = ".";
                             }
                        	
                        	passee1 = true;
                        }
                        if (currentString.contains("\"aaCode\"") && passee1 == true) {
                        	
                        	aa2 = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                        	if (aa2.equals("")) {
                        		aa2 = ".";
                            
                             }
                        
                        }
                        
                       
                        if (currentString.contains("\"fxnName\"") && passee == false) {
                        	
                        	fxnName = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\","));
                        	passee=true;
                        	if (fxnName.equals("")) {
                             	fxnName = ".";
                             }
                        	
                        }
                    }
                  //br.close();

// 	           ***********ICI COMMENCE L'EXTRACTION VEP*************
 	           
//                    System.out.println("****** Content of the URL ********");			
//             	   JSONParser parser = new JSONParser();
//             	   String input;
//             	
//
//             		  brvep = new BufferedReader(new InputStreamReader(urlvep.openStream()));
//					while ((input = brvep.readLine()) != null){
//						   //System.out.println("brvepin" + brvep);
//						  //System.out.println("brin" + br);
//						 //System.out.println("input\t" + input);
//						 
//						 JSONArray array = (JSONArray) parser.parse(input);
//						 JSONObject JO = (JSONObject) array.get(0);
//						   JSONArray transcript_consequences = (JSONArray)JO.get("transcript_consequences");
//
//					        for(Object obj: transcript_consequences){
//					     	   JSONObject object = (JSONObject) obj;
//					     	   protein_end = String.valueOf(object.get("protein_end"));
//					     	 if ((!(protein_end.equals("null")) && protein_end.equals(proteinPos)))
//					     	 {
//					     		 System.out.println("Je rentre dans le if vep");
//					     	System.out.println("protein_end: " + protein_end);
//					     	System.out.println(obj);
//					     sift_score1 = String.valueOf(object.get("sift_score"));
//					     
//					     System.out.println("sift_score1: " + sift_score1);
//					     if(!(sift_score1.equals("null"))){
//					    	 sift_score = sift_score1;
//					     System.out.println("sift_score: " + sift_score);
//					     }
//					     if((sift_score1.equals("null"))){
//					        	 sift_score = ".";
//					         System.out.println("sift_scorep: " + sift_score);
//					         }
//					     
//					     polyphen_score1 = String.valueOf(object.get("polyphen_score"));
//					     System.out.println("polyphen_score1: " + polyphen_score1);
//					     
//					     if(!(polyphen_score1.equals("null"))){
//					     polyphen_score = polyphen_score1;
//					     System.out.println("polyphen_score: " + polyphen_score);
//					     
//					     }
//					     if((polyphen_score1.equals("null"))){
//					         polyphen_score = ".";
//					         System.out.println("polyphen_scorep: " + polyphen_score);
//					         }
//					     
//
//					     sift_prediction1 = String.valueOf(object.get("sift_prediction"));
//					     System.out.println("sift_prediction1: " + sift_prediction1);
//					     if (!(sift_prediction1.equals("null")))
//					     {
//					    	 sift_prediction = sift_prediction1;
//					    	 System.out.println("sift_prediction: " + sift_prediction);
//					     }
//					     if ((sift_score1.equals("null")))
//					     {
//					    	 sift_prediction = ".";
//					    	 System.out.println("sift_predictionp: " + sift_prediction);
//					     }
//					     
//					     polyphen_prediction1 = String.valueOf(object.get("polyphen_prediction"));
//					     System.out.println("polyphen_prediction1: " + polyphen_prediction1);
//					     if (!(polyphen_prediction1.equals("null")))
//					     {
//					    	 polyphen_prediction = polyphen_prediction1;
//					    	 System.out.println("polyphen_prediction: " + polyphen_prediction);
//					     }
//					     if ((polyphen_prediction1.equals("null")))
//					     {
//					    	 polyphen_prediction = ".";
//					    	 System.out.println("polyphen_predictionp: " + polyphen_prediction);
//					     }
//					     	 } 
//					         
//					        }
//					        
//					        if(proteinPos.equals("."))
//					        
//					        {
//					    	  System.out.println("On rentre dans un if protend = .");
//					        	sift_score = ".";
//					        	polyphen_score = ".";
//					        	sift_prediction = ".";
//					        	polyphen_prediction = ".";
//					        }
//						 
//					   }
					// Requesting regulomeDB and preparing explanation for the score
					ResultSet res = conn.createStatement()
							.executeQuery("SELECT score FROM scores_table WHERE variant='rs" + Varid + "';");
					if (res.next()) {
						regulomeDB_score = res.getString("score");
						if (regulomeDB_score.equals("1a") || regulomeDB_score.equals("1b")
								|| regulomeDB_score.equals("1c") || regulomeDB_score.equals("1d")
								|| regulomeDB_score.equals("1e") || regulomeDB_score.equals("1f")) {
							rgdb_score_signification = "(likely to affect binding and linked to expression of a gene target)";
						} else if (regulomeDB_score.equals("2a") || regulomeDB_score.equals("2b")
								|| regulomeDB_score.equals("2c")) {
							rgdb_score_signification = "(likely to affect binding)";
						} else if (regulomeDB_score.equals("3a") || regulomeDB_score.equals("3b")) {
							rgdb_score_signification = "(less likely to affect binding)";
						} else if (regulomeDB_score.equals("4") || regulomeDB_score.equals("5")
								|| regulomeDB_score.equals("6")) {
							rgdb_score_signification = "(minimal binding evidence)";
						} else if (regulomeDB_score.equals("7")) {
							rgdb_score_signification = "(no data)";
						}
					} else {
						regulomeDB_score = "not in the base";
						rgdb_score_signification = "(regulomeDB may not be up to date)";
					}
					
					
//					ResultSet result = conn.createStatement()
//							.executeQuery("SELECT id FROM scoressnps FETCH NEXT 1000 ROWS ONLY "); //'"+Varid+"'";
//					while(result.next()){
//					Test.add(result.getString("id"));
//					System.out.println("TEST"+Test);
//					}
						//String [] test = {"rs201864858", "rs201864858", "rs372723457", "rs372723457", "rs11553019", "rs11553019", "rs139017158", "rs139017158", "rs149964072", "rs149964072", "rs11553018", "rs11553018", "rs141052988", "rs141052988", "rs145664491", "rs145664491", "rs148306581", "rs148306581", "rs74558623", "rs74558623", "rs79377094", "rs79377094", "rs201610844", "rs201610844", "rs375850117", "rs375850117", "rs201342263", "rs201342263", "rs201892015", "rs201892015", "rs115595484", "rs115595484", "rs367663038", "rs367663038", "rs199853927", "rs199853927", "rs200757617", "rs200757617", "rs138567361", "rs138567361", "rs369805092", "rs369805092", "rs200178716", "rs200178716", "rs150755193", "rs150755193", "rs111662059", "rs111662059", "rs376725006", "rs376725006", "rs375747724", "rs375747724", "rs200476766", "rs200476766", "rs375678465", "rs375678465", "rs370150531", "rs370150531", "rs75915990", "rs75915990", "rs141372323", "rs141372323", "rs121909386", "rs121909386", "rs371779195", "rs371779195", "rs190323395", "rs190323395", "rs375833021", "rs375833021", "rs149516753", "rs149516753", "rs140753322", "rs140753322", "rs183437359", "rs183437359", "rs367727112", "rs367727112", "rs370770872", "rs370770872", "rs200241388", "rs200241388", "rs143432523", "rs143432523", "rs370731821", "rs370731821", "rs201280490", "rs201280490", "rs372770283", "rs372770283", "rs199753491", "rs199753491", "rs182944047", "rs182944047", "rs200892725", "rs200892725", "rs372546647", "rs372546647", "rs112200975", "rs112200975", "rs367763475", "rs367763475", "rs150890799", "rs150890799", "rs140162687", "rs140162687", "rs374218650", "rs374218650", "rs149456198", "rs149456198", "rs141485845", "rs141485845", "rs371085455", "rs371085455", "rs368955427", "rs368955427", "rs376644821", "rs376644821", "rs368720992", "rs368720992", "rs191962962"};
						
						
					
					//System.out.println("resdbsnp" + resdbsnp);
					//ResultSetMetaData resultdbsnp = resdbsnp.getMetaData();
					//ResultSetMetaData resultMeta1 = resultsubgroup.getMetaData();
					ResultSet resdbsnp = conn.createStatement()
							.executeQuery("SELECT * FROM scoressnps WHERE id = 'rs" + Varid + "'"); //'"+Varid+"'"

					if (resdbsnp.next()) {
						
						SIFT = resdbsnp.getString("SIFT_score");
						SIFT_P = resdbsnp.getString("SIFT_pred");
						Polyphen = resdbsnp.getString("Polyphen2_HDIV_score");
						Polyphen_P = resdbsnp.getString("Polyphen2_HDIV_pred");
						Provean = resdbsnp.getString("PROVEAN_score");
						Provean_P = resdbsnp.getString("PROVEAN_pred");
					CADD = resdbsnp.getString("CADD_phred");
						}
					else {
						SIFT = ".";
						SIFT_P = ".";
						Polyphen = ".";
						Polyphen_P = ".";
						Provean = ".";
						Provean_P = ".";
					CADD = ".";
					}
					
					
					
					

                    AnnotThreading.StockLineAnnotdef.put(this.cpt,geneSymbol + "\t" + geneId+ "\t" + fxnName + "\t" + proteinPos + "\t" + aa2 + aa1  + "\t" + proteinAcc);
                    AnnotThreading.StockLineAnnotadv.put(this.cpt,"\t" +regulomeDB_score + "\t" + rgdb_score_signification+ "\t" + SIFT + "\t" + SIFT_P + "\t" + Polyphen + "\t" + Polyphen_P + "\t" 
                    + Provean + "\t" + Provean_P + "\t" + CADD);
    			} catch (IOException e) {
    				
					e.printStackTrace();
					System.out.print("e");
					//if (message : tantantan)
				}catch (Exception e2) {
              	     e2.printStackTrace();
              	   System.out.print("e1");
                }
        	} catch (IOException e) {
				e.printStackTrace();
				System.out.print("e2");
            }
		}

		
		
/*public static void Ecriturefile() throws Exception {

	File Freqtest = new File("C:\\Users\\e300264\\Documents\\Projet_de_Stage2018_Ferret\\Fichiers\\FreqTest.txt");
	PrintWriter frqWrite = new PrintWriter(new BufferedWriter(new FileWriter(Freqtest)));
	frqWrite.write("CHROM\tVARIANT\tPOS\tALLELE1\tALLELE2\tNB_CHR\t1KG_A1_FREQ\t1KG_A2_FREQ\tGENENAME\tGENEID\tFUNCTION\tPROTEINPOS\tAACHANGE\tPROTEINACC\tSIFT_SCORE\tSIFT_PREDICTION\tPOLYPHEN_SCORE\tPOLYPHEN_PREDICTION");
	frqWrite.println("");
	frqWrite.println(CallerRunsPolicyDemo.StockLineAnnot[cpt]);
//	frqWrite.write(geneSymbol + "\t" + geneId+ "\t" + fxnName + "\t" + proteinPos + "\t" + aa2 + aa1  + "\t" + proteinAcc + "\t" + sift_score+ "\t" + sift_prediction+ "\t" + polyphen_score+ "\t" + polyphen_prediction);
	frqWrite.println("");
	frqWrite.close();
}*/



	   }

	 
//public static void main(String... args) {
//Runnable runner =new CallerRunsPolicyDemo().new MyRunnable(url,urlvep);
//  
//ThreadPoolExecutor  executor = new ThreadPoolExecutor(2,3,500, TimeUnit.MILLISECONDS,
// 		 new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
//  
//executor.execute(runner);
//	 executor.shutdown();
//		// Wait until all threads are finish
//		while (!executor.isTerminated()) {
//
//		}
//		System.out.println("\nFinished all threads");
//	}


