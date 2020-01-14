package com.ecn.ferretmvc.model;
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

	public static volatile Map<Integer, String> StockLineAnnotdef;
	public static volatile Map<Integer, String> StockLineAnnotadv;
	private int cpt;
	private String Varid;
	
		public AnnotThreading (int cpt,String Varid) {
			
		 if(AnnotThreading.StockLineAnnotdef == null && AnnotThreading.StockLineAnnotadv == null )
			 AnnotThreading.StockLineAnnotdef = new HashMap<>();
		 AnnotThreading.StockLineAnnotadv = new HashMap<>();
		 
		 this.cpt = cpt; 
		 this.Varid = Varid;
		}

		@Override
		public void run() {
			 String geneSymbol = "unavailable";
			 String geneId = "unavailable";
			 String proteinPos = "unavailable";
			 String proteinAcc = "unavailable";
			 String fxnName = "unavailable";
			 String aa2 = "unavai";
			 String aa1 = "lable";
			 Boolean passee = false;
			 Boolean passee1 = false;
        	 BufferedReader br = null;
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

    		    br = null;
    		    
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
 
					// Requesting regulomeDB and preparing explanation for the score
                    
					ResultSet res = DBconnection.conn.createStatement()
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
					// Requesting dbsnpf scores
					ResultSet resdbsnp = DBconnection.conn.createStatement()
							.executeQuery("SELECT \"SIFT_score\",\"SIFT_pred\",\"Polyphen2_HDIV_score\",\"Polyphen2_HDIV_pred\",\"PROVEAN_score\",\"PROVEAN_pred\",\"CADD_phred\" FROM scoressnps WHERE id = 'rs" + Varid + "'"); 

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

                    AnnotThreading.StockLineAnnotdef.put(this.cpt,geneSymbol + "\t" + geneId+ "\t" + fxnName + "\t" + proteinPos + "\t" + aa2 + "/" + aa1  + "\t" + proteinAcc);
                    AnnotThreading.StockLineAnnotadv.put(this.cpt,"\t" +regulomeDB_score + "\t" + rgdb_score_signification+ "\t" + SIFT + "\t" + SIFT_P + "\t" + Polyphen + "\t" + Polyphen_P + "\t" 
                    + Provean + "\t" + Provean_P + "\t" + CADD);
    			} catch (IOException e) {
    				
					e.printStackTrace();
					System.out.print("e");
				}catch (Exception e2) {
              	     e2.printStackTrace();
              	   System.out.print("e1");
                }
        	} catch (IOException e) {
				e.printStackTrace();
				System.out.print("e2");
            }
		}


	   }
