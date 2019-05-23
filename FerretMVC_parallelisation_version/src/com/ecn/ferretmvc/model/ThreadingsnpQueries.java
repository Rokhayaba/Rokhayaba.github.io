package com.ecn.ferretmvc.model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ThreadingsnpQueries implements Runnable {
	
	
	ArrayList<String> snpQueries;
	LinkedList<String> chromosome = new LinkedList<>();
    LinkedList<String> startPos = new LinkedList<>();
    LinkedList<String> endPos = new LinkedList<>();
    ArrayList<String> SNPsFound = new ArrayList<>();
    boolean allSNPsFound;
    BufferedReader br = null;
    Boolean defaultHG;
    //boolean chrFound, startFound, endFound, locatedOnInvalidChr;
    URL urlLocation;
    int i;

   
 
		public ThreadingsnpQueries(int i,ArrayList<String> snpQueries,LinkedList<String> chromosome,LinkedList<String> startPos,LinkedList<String> endPos,ArrayList<String> SNPsFound,boolean allSNPsFound,Boolean defaultHG,URL urlLocation) {
			this.snpQueries = snpQueries;
			this.chromosome = chromosome;
		    this.startPos = startPos;
		    this.endPos = endPos;
		   this.SNPsFound = SNPsFound;
		    this.allSNPsFound = allSNPsFound;
		    this.defaultHG = defaultHG;
//		    this.chrFound = chrFound;
//		    this.startFound = startFound;
//		    this.endFound = endFound;
//		    this.locatedOnInvalidChr = locatedOnInvalidChr;
		    this.urlLocation = urlLocation;
		    this.i = i;
			
			 			}
		
		
 
		@Override
		public void run() {
			
         	
					try {
                br = new BufferedReader(new InputStreamReader(urlLocation.openStream()));
                String currentString;
                if (defaultHG) {
                    while ((currentString = br.readLine()) != null && !currentString.contains("\"GRCh37.p13\" : [")) {
                    }
                } else {
                    while ((currentString = br.readLine()) != null && !currentString.contains("\"GRCh38.p2\" : [")) {
                    }
                }
                boolean chrFound = false, startFound = false, endFound = false, locatedOnInvalidChr = false;
                while (!(startFound && endFound && chrFound) && (currentString = br.readLine()) != null) {
                    if (currentString.contains("\"chrPosFrom\"")) {
                        startPos.add(currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")));
                        startFound = true;
                    } else if (currentString.contains("\"chr\"")) {
                        chromosome.add(currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")));
                        locatedOnInvalidChr = chromosome.peekLast().equals("X") || chromosome.peekLast().equals("Y") || chromosome.peekLast().equals("MT");
                        chrFound = true;
                    } else if (currentString.contains("\"chrPosTo\"")) {
                        endPos.add(currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")));
                        endFound = true;
                    }
                }
                if (!(startFound && endFound && chrFound && !locatedOnInvalidChr)) {
                    // If one of the three elements is missing the other elements corresponding to the missing one are removed
                    if (startFound) {
                        startPos.removeLast();
                    }
                    if (endFound) {
                        endPos.removeLast();
                    }
                    if (chrFound) {
                        chromosome.removeLast();
                    }
                    allSNPsFound = false;
                }

                
                else {
                    SNPsFound.add(snpQueries.get(i));
                }
                br.close();
                
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("SPECIFIQUE --->w");
					e.printStackTrace();
				} finally {
	                if (br != null) {
	                    try {
	                        br.close();
	                    } catch (IOException ex) {
	                        Logger.getLogger(FerretData.class.getName()).log(Level.SEVERE, null, ex);
	                    }
	                }
	            }
            
            }
		
		

}
