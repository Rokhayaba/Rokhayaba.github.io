

package com.ecn.ferretmvc.model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.broad.tribble.readers.TabixReader;

import com.sun.jimi.core.*;


public class Threading1bis implements Runnable {
	
    int j;
    long startTime;
    ArrayList<InputRegion> sortedQueries;
    String webAddress;
    String ftpAddress;
    int queryNumber;
    int variantCounter;
    Integer tempInt;
    int[] querySize;
    String fileName;
    int cpt;
    public static volatile Map<Integer, String> StockLineVcfall;
   
 
		public Threading1bis(int cpt,int j,long startTime,ArrayList<InputRegion> sortedQueries,String webAddress,String ftpAddress,int queryNumber,int variantCounter,Integer tempInt,int[] querySize, String fileName) {
			if(Threading1bis.StockLineVcfall == null)
				 Threading1bis.StockLineVcfall = new HashMap<>();
		    this.j = j;
		    this.cpt = cpt;
		    this.startTime = startTime ;
		    this.sortedQueries = sortedQueries ;
		    this.webAddress = webAddress;
		    this.ftpAddress = ftpAddress;
		    this.queryNumber = queryNumber;
		    this.variantCounter = variantCounter;
		    this.tempInt = tempInt;
		    this.querySize = querySize;
		    this.fileName = fileName;			
			 			}
		
		
 
		@Override
		public void run() {
			try{
				//File vcfFile = new File(fileName + "_genotypes.vcf");
				
			webAddress = ftpAddress.replace("$", sortedQueries.get(j).getChr());
            System.out.println("QUERY NUMBER ---->" + queryNumber + "\tSORTED QUERIES ---->" + sortedQueries.get(j).getChr() + "\tWEBADRESS ---->" + webAddress);
            //webAddress = "ftp://ftp-trace.ncbi.nih.gov/1000genomes/ftp/release/20130502/ALL.chr" + sortedQueries.get(j).getChr() + ".phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz";
            //webAddress = "ftp://ftp-trace.ncbi.nih.gov/1000genomes/ftp/release/20130502/supporting/GRCh38_positions/ALL.chr" + sortedQueries.get(j).getChr() + ".phase3_shapeit2_mvncall_integrated_v3plus_nounphased.rsID.genotypes.GRCh38_dbSNP_no_SVs.vcf.gz";
            TabixReader tr = new TabixReader(webAddress);
            System.out.println("tr --->" + tr);
           // BufferedWriter vcfBuffWrite = new BufferedWriter(new FileWriter(vcfFile));
            // Get the iterator
            startTime = System.nanoTime();
            TabixReader.Iterator iter = tr.query(sortedQueries.get(j).getChr() + ":" + sortedQueries.get(j).getStart() + "-" + sortedQueries.get(j).getEnd());
            System.out.println("iter --->" + iter);
            System.out.println("WHAT'S UP ? --->" + sortedQueries.get(j).getChr() + ":" + sortedQueries.get(j).getStart() + "-" + sortedQueries.get(j).getEnd());
            long endTime = System.nanoTime();
            System.out.println("Tabix iterator timeff: " + (endTime - startTime));
            String s;
            //int cpt = 0;
            while ((s = iter.next()) != null) {
                variantCounter++;
                String[] stringSplit = s.split("\t");
                if (stringSplit[6].equals("PASS")) {
                    if (FerretData.isInteger(stringSplit[1])) {
                        tempInt = Integer.parseInt(stringSplit[1]) - sortedQueries.get(j).getStart();
//                        if (tempInt > 0) {
//                            setProgress((int) ((tempInt + querySize[j]) / (double) querySize[queryNumber] * 99));
//                        }
                    }
                    if (stringSplit[2].equals(".")) {
                        stringSplit[2] = "chr" + sortedQueries.get(j).getChr() + "_" + stringSplit[1];
                        for (int i = 0; i < stringSplit.length; i++) {
                            Threading1bis.StockLineVcfall.put(cpt,stringSplit[i] + "\t"); 
                            //vcfBuffWrite.write(stringSplit[i] + "\t");
                            System.out.println("StringSplit---->" + stringSplit[i] + "\t");
                            System.out.println("Thr1----->" +Threading1bis.StockLineVcfall + "\t");
                        }
                    } else {
                    	Threading1bis.StockLineVcfall.put(cpt,s);
                        //vcfBuffWrite.write(s);
                        System.out.println("s1----->" + s);
                        System.out.println("Thr1s1----->" +Threading1bis.StockLineVcfall + "\t");
                    }
                    //vcfBuffWrite.newLine();
                } else {
                	Threading1bis.StockLineVcfall.put(cpt,s);
                	System.out.println("s2----->" + s);
                    System.out.println("Thr1s2----->" +Threading1bis.StockLineVcfall + "\t");
                    //vcfBuffWrite.write(s);
                    //System.out.println(s);
                    //vcfBuffWrite.newLine();
                }
                //cpt++;
            }
            long endEndTime = System.nanoTime();
            System.out.println("Iteration timeff: " + (endEndTime - endTime));
		} catch (IOException e) {
			
			e.printStackTrace();
			System.out.print("e--------------------");
			}
         	
	            }
			
            
            }





