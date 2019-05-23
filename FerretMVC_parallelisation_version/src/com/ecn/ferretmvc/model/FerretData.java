package com.ecn.ferretmvc.model;

import com.ecn.ferretmvc.main.FerretMain;
import com.ecn.ferretmvc.view.GUI;

import java.util.*;
import java.util.regex.*;
import java.awt.Component;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.broad.tribble.readers.TabixReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.concurrent.*;


public class FerretData extends SwingWorker<Integer, String> {

    private Map<Integer, String> StockLineFreq;


	private Runnable worker;

        ThreadPoolExecutor  executor= new ThreadPoolExecutor(300,300,500, TimeUnit.MILLISECONDS,
   		 new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());


    InputRegion[] queries;
    ArrayList<CharSequence> populations;
    String fileName;
    boolean retrieveESP;
    JLabel status;
    String ftpAddress;
    double MAF;
    double MAFMax;
    String outputFiles;
    String annotFiles;
    double espMAF;
    //private boolean usehaplo;
    boolean htmlOutputFile;
    boolean downloadHaplo;

    String queryType = null; // for both gene and SNP queries
    Boolean defaultHG = null; // for both gene and SNP queries
    ArrayList<String> snpQueries = null; // only for SNP queries
    Boolean snpWindowSelected = null; // only for SNP
    Boolean geneWindowSelected = null;
    Integer windowSize = null; // only for SNP
    String[] geneQueries = null; // only for gene

    // constructor for the locus research
    public FerretData(InputRegion[] queries, ArrayList<CharSequence> populations, String fileName,
            boolean retrieveESP, JLabel status, String ftpAddress, double MAF, double MAFMax, Boolean ESPMAF, String outputFiles , String annotFiles,boolean htmlOutputFile,boolean downloadHaplo ) {
        this.queries = queries;
        this.populations = populations;
        this.fileName = fileName;
        this.retrieveESP = retrieveESP;
        this.status = status;
        this.ftpAddress = ftpAddress;
        this.MAF = MAF;
        this.MAFMax = MAFMax;
        if (ESPMAF) {
            espMAF = MAF;
        } else {
            espMAF = 1;
        }
        this.outputFiles = outputFiles;
        this.annotFiles = annotFiles;
        this.htmlOutputFile = htmlOutputFile;
        this.downloadHaplo = downloadHaplo;
       // this.usehaplo = false;
    }

    //Constructor for the SNP (variants) research
    public FerretData(String queryType, ArrayList<String> snpQueries, ArrayList<CharSequence> populations, String fileName,
            boolean retrieveESP, JLabel status, String ftpAddress, double MAF, double MAFMax, Boolean ESPMAF, String outputFiles, boolean defaultHG,
            boolean snpWindowSelected, Integer windowSize, String annotFiles,boolean htmlOutputFile, boolean downloadHaplo) {
        // this constructor is exclusively for SNP queries
        this.queries = null;
        this.populations = populations;
        this.fileName = fileName;
        this.retrieveESP = retrieveESP;
        this.status = status;
        this.ftpAddress = ftpAddress;
        this.MAF = MAF;
        this.MAFMax = MAFMax;
        if (ESPMAF) {
            espMAF = MAF;
        } else {
            espMAF = 1;
        }
        this.outputFiles = outputFiles;
        this.annotFiles = annotFiles;
        this.htmlOutputFile = htmlOutputFile;
        this.queryType = queryType;
        this.snpQueries = snpQueries;
        this.defaultHG = defaultHG;
        this.snpWindowSelected = snpWindowSelected;
        this.windowSize = windowSize;
        //this.usehaplo = false;
        this.downloadHaplo = downloadHaplo;
    }

    //Constructor for the gene research
    public FerretData(String queryType, String[] geneQueries, ArrayList<CharSequence> populations, String fileName,
            boolean retrieveESP, JLabel status, String ftpAddress, double MAF, double MAFMax, Boolean ESPMAF, String outputFiles, boolean defaultHG, boolean geneWindowSelected, Integer windowSize, int o, String annotFiles,boolean htmlOutputFile, boolean downloadHaplo) {
        // this constructor is exclusively for gene queries
        this.queries = null;
        this.populations = populations;
        this.fileName = fileName;
        this.retrieveESP = retrieveESP;
        this.status = status;
        this.ftpAddress = ftpAddress;
        this.MAF = MAF;
        this.MAFMax = MAFMax;
        if (ESPMAF) {
            espMAF = MAF;
        } else {
            espMAF = 1;
        }
        this.outputFiles = outputFiles;
        this.annotFiles = annotFiles;
        this.htmlOutputFile = htmlOutputFile;
        this.queryType = queryType;
        this.geneQueries = geneQueries;
        this.defaultHG = defaultHG;
        this.geneWindowSelected = geneWindowSelected;
        this.windowSize = windowSize;
        //this.usehaplo = false;
        this.downloadHaplo =  downloadHaplo;
    }



	//setter that put the boolean usehaplo as true, use in doInBackGround to open or not Haploview at the end
//    public void setHaplo(boolean b) {
//        this.usehaplo = b;
//    }

    @Override
    public Integer doInBackground() throws Exception {

        // SNP query here
        if (queryType != null && "SNP".equals(queryType)) {
            publish("Looking up variant locations...");
            LinkedList<String> chromosome = new LinkedList<>();
            LinkedList<String> startPos = new LinkedList<>();
            LinkedList<String> endPos = new LinkedList<>();
            InputRegion[] queries = null;
            ArrayList<String> SNPsFound = new ArrayList<>();
            boolean allSNPsFound = true;
            //allSNPsFound = true;
            //BufferedReader br = null;
            //br = null;
            try {
            	
            	
                for (int i = 0; i < snpQueries.size(); i++) {
                	System.out.println("snp queries" + snpQueries);
                    URL urlLocation;
					
						urlLocation = new URL("https://www.ncbi.nlm.nih.gov/projects/SNP/snp_gene.cgi?connect=&rs=" + snpQueries.get(i));
						Runnable workersnpQueries = new ThreadingsnpQueries(i,snpQueries,chromosome,startPos,endPos,SNPsFound,allSNPsFound,defaultHG,urlLocation);
						executor.execute(workersnpQueries);

                }
                executor.shutdown();
              	// Wait until all threads are finish
              	while (!executor.isTerminated()) {

              	}
              	executor = new ThreadPoolExecutor(100,100,500, TimeUnit.MILLISECONDS,
              	   		 new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());

            
                if (!allSNPsFound && !SNPsFound.isEmpty()) {//Partial list
                    String[] options = {"Yes", "No"};

                    JPanel partialSNPPanel = new JPanel();
                    JTextArea listOfSNPs = new JTextArea(SNPsFound.toString().substring(1, SNPsFound.toString().length() - 1));
                    listOfSNPs.setWrapStyleWord(true);
                    listOfSNPs.setLineWrap(true);
                    listOfSNPs.setBackground(partialSNPPanel.getBackground());
                    partialSNPPanel.setLayout(new BoxLayout(partialSNPPanel, BoxLayout.Y_AXIS));
                    partialSNPPanel.add(new JLabel("Ferret encountered problems retrieving the variant positions from the NCBI SNP Database."));
                    partialSNPPanel.add(new JLabel("Here are the variants successfully retrieved:"));
                    JScrollPane listOfSNPScrollPane = new JScrollPane(listOfSNPs, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    listOfSNPScrollPane.setBorder(BorderFactory.createEmptyBorder());
                    listOfSNPScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
                    partialSNPPanel.add(listOfSNPScrollPane);
                    partialSNPPanel.add(new JLabel("Do you wish to continue?"));

                    int choice = JOptionPane.showOptionDialog(null,
                            partialSNPPanel,
                            "Continue?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            null);
                    if (choice == JOptionPane.YES_OPTION) {
                        queries = new InputRegion[chromosome.size()];
                        for (int i = 0; chromosome.size() > 0; i++) {
                            if (!snpWindowSelected) {
                                queries[i] = new InputRegion(chromosome.remove(), (Integer.parseInt(startPos.remove()) - 1), (Integer.parseInt(endPos.remove()) + 1));
                            } else {
                                queries[i] = new InputRegion(chromosome.remove(), (Integer.parseInt(startPos.remove()) - windowSize), (Integer.parseInt(endPos.remove()) + windowSize));
                            }
                        }
                    } else {
                        return -2;
                    }
                } else if (SNPsFound.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ferret was unable to retrieve any variants", "Error", JOptionPane.OK_OPTION);
                    return -2;
                } else if (allSNPsFound) {
                    queries = new InputRegion[chromosome.size()];
                    for (int i = 0; chromosome.size() > 0; i++) {
                        if (!snpWindowSelected) {
                            queries[i] = new InputRegion(chromosome.remove(), Integer.parseInt(startPos.remove()), Integer.parseInt(endPos.remove()));
                        } else {
                            queries[i] = new InputRegion(chromosome.remove(), (Integer.parseInt(startPos.remove()) - windowSize), (Integer.parseInt(endPos.remove()) + windowSize));
                        }
                    }
                }
                this.queries = queries;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ferret was unable to retrieve any variants", "Error", JOptionPane.OK_OPTION);
                return -2;
            } 
        }
        // gene treatment

        if ("geneID".equals(queryType) || "geneName".equals(queryType)) {
            publish("Looking up gene locations...");
            FoundGeneAndRegion[] geneLocationFromGeneName = {null};
            if ("geneName".equals(queryType)) {
                geneLocationFromGeneName[0] = getQueryFromGeneName(geneQueries, defaultHG);
            } else {
                geneLocationFromGeneName[0] = getQueryFromGeneID(geneQueries, defaultHG);
            }

            if (geneLocationFromGeneName[0] == null) {
                JOptionPane.showMessageDialog(null, "Ferret was unable to retrieve any genes", "Error", JOptionPane.OK_OPTION);
                return -2;
            } else if (!geneLocationFromGeneName[0].getFoundAllGenes()) {
                String[] options = {"Yes", "No"};

                JPanel partialGenePanel = new JPanel();
                JTextArea listOfGenes = new JTextArea(geneLocationFromGeneName[0].getFoundGenes());
                listOfGenes.setWrapStyleWord(true);
                listOfGenes.setLineWrap(true);
                listOfGenes.setBackground(partialGenePanel.getBackground());
                partialGenePanel.setLayout(new BoxLayout(partialGenePanel, BoxLayout.Y_AXIS));
                partialGenePanel.add(new JLabel("Ferret encountered problems retrieving the gene positions from the NCBI Gene Database."));
                partialGenePanel.add(new JLabel("Here are the genes successfully retrieved:"));
                JScrollPane listOfGenesScrollPane = new JScrollPane(listOfGenes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                listOfGenesScrollPane.setBorder(BorderFactory.createEmptyBorder());
                listOfGenesScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
                partialGenePanel.add(listOfGenesScrollPane);
                partialGenePanel.add(new JLabel("Do you wish to continue?"));

                int choice = JOptionPane.showOptionDialog(null,
                        partialGenePanel,
                        "Continue?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        null);
                if (choice == JOptionPane.YES_OPTION) {
                    this.queries = geneLocationFromGeneName[0].getInputRegionArray();
                } else {
                    return -2;
                }
            } else {
                this.queries = geneLocationFromGeneName[0].getInputRegionArray();
            }
        }

        publish("Parsing Individuals...");

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        // Initalize some variables
        ArrayList<String> peopleOfInterest = new ArrayList<>();
        int variantCounter = 0;
        int peopleCounter = 0;
        String[][] genotypes = null;

        ArrayList<InputRegion> sortedQueries = sortByWindow(queries);
        String webAddress = ftpAddress.replace('$', '1');
        HashMap<String, Integer> peopleSet;
        try {
            peopleSet = getPopulationIndices(webAddress);
        } catch (IOException e) {
            return -1;
        }

        int queryNumber = sortedQueries.size();
        int[] querySize = new int[queryNumber + 1];
        querySize[0] = 0;
        for (int i = 0; i < sortedQueries.size(); i++) {
            querySize[i + 1] = querySize[i] + (sortedQueries.get(i).getEnd() - sortedQueries.get(i).getStart());
        }

        // Gather the individuals to return genotypes
        try {
            String s;
            // If custom URL is specified and the date for phase 1 is not in the name, Ferret will use
            // phase 3 individuals
            BufferedReader popBuffRead = null;
            if (ftpAddress.contains("20110521")) {
                popBuffRead = new BufferedReader(new InputStreamReader(FerretData.class.getClassLoader().getResourceAsStream("samplesPhase1.txt")));
            } else {
                popBuffRead = new BufferedReader(new InputStreamReader(FerretData.class.getClassLoader().getResourceAsStream("samplesPhase3.txt")));
            }

            // Retrieving all the populations check by the user contained in samples files
            String[] IDs;
            s = popBuffRead.readLine();
            if (!populations.contains("ALL")) {
                while (s != null) {
                    IDs = s.split("\t");
                    if ((populations.contains(IDs[1]) || populations.contains(IDs[2])) && peopleSet.containsKey(IDs[0])) {
                        peopleOfInterest.add(IDs[0]);
                        peopleCounter++;
                    }
                    s = popBuffRead.readLine();
                }
            } else {
                while (s != null) {
                    IDs = s.split("\t");
                    if (peopleSet.containsKey(IDs[0])) {
                        peopleOfInterest.add(IDs[0]);
                        peopleCounter++;
                    }
                    s = popBuffRead.readLine();
                }
            }
            popBuffRead.close();
        } catch (IOException e) {
            //This shouldn't be a problem since the file being read comes with Ferret
        } finally {
        }

        publish("Downloading Data from 1000 Genomes...");
        String s;
        long startTime = 0;
        Integer tempInt = null;

        if (outputFiles.equals("vcf")) {
            // Outputting VCF does not require writing the VCF from 1KG
            File vcfWriteFile = new File(fileName + ".vcf");
            try {
                vcfWriteFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FerretData.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (BufferedWriter vcfWrite = new BufferedWriter(new FileWriter(vcfWriteFile))) {
                // Uses the web address from peopleSet, but I don't really see a problem with this
                TabixReader tr = new TabixReader(webAddress);

                s = tr.readLine();
                while (!s.contains("CHROM")) {
                    vcfWrite.write(s);
                    vcfWrite.newLine();
                    s = tr.readLine();
                }
                String[] stringSplit = s.split("\t");
                // Write VCF header/column label
                for (int i = 0; i < 9; i++) {
                    vcfWrite.write(stringSplit[i] + "\t");
                }
                ArrayList<Integer> peopleIndexSet = new ArrayList<>();
                // Write individuals
                for (int i = 9; i < stringSplit.length; i++) {
                    if (peopleOfInterest.contains(stringSplit[i])) {
                        vcfWrite.write(stringSplit[i] + "\t");
                        peopleIndexSet.add(i);
                    }
                }
                vcfWrite.newLine();

                for (int j = 0; j < queryNumber; j++) {
                	
                    webAddress = ftpAddress.replace("$", sortedQueries.get(j).getChr());
                    tr = new TabixReader(webAddress);
                    startTime = System.nanoTime();
                    TabixReader.Iterator iter = tr.query(sortedQueries.get(j).getChr() + ":" + sortedQueries.get(j).getStart() + "-" + sortedQueries.get(j).getEnd());
                    long endTime = System.nanoTime();
                    System.out.println("Tabix iterator time: " + (endTime - startTime));

                    while ((s = iter.next()) != null) {
                        variantCounter++;
                        stringSplit = s.split("\t");

                        if (isInteger(stringSplit[1])) {
                            tempInt = Integer.parseInt(stringSplit[1]) - sortedQueries.get(j).getStart();
                            if (tempInt > 0) {
                                setProgress((int) ((tempInt + querySize[j]) / (double) querySize[queryNumber] * 99));
                            }
                        }

                        String[] variantPossibilities;
                        String[] multiAllele = stringSplit[4].split(",");
                        int[] variantFreq = new int[multiAllele.length + 1];

                        int chromosomeCount = 0;

                        StringBuilder tempString = new StringBuilder();
                        for (int i = 0; i < 9; i++) {
                            tempString.append(stringSplit[i]).append("\t");
                        }
                        for (int i = 9; i < stringSplit.length; i++) {
                            if (peopleIndexSet.contains(i)) {
                                tempString.append(stringSplit[i]).append("\t");
                                variantFreq[Character.getNumericValue(stringSplit[i].charAt(0))]++;
                                variantFreq[Character.getNumericValue(stringSplit[i].charAt(0))]++;
                                chromosomeCount += 2;
                            }
                        }
                        // This loop is equivalent to an if "all"
                        boolean tempBoolean = true;
                        for (int i = 0; i < variantFreq.length; i++) {
                            if ((variantFreq[i] / (float) chromosomeCount) < MAF){// || (variantFreq[i] / (float) chromosomeCount) < MAFMax) {
                                tempBoolean = false; //if fail MAF Threshold, continue to next line
                            }
                        }
                        if (tempBoolean) {
                            vcfWrite.write(tempString.toString());
                            vcfWrite.newLine();
                        }

                    }

                }
                vcfWrite.close();
                if (variantCounter == 0) {
                    vcfWriteFile.delete();
                    return 0;
                }
            } catch (IOException e) {
                vcfWriteFile.delete();
                return -1;
            }

        } else {

            // VCF writing code is right here for compatibility with swingWorker
            try {
//            	File vcfFile = new File(fileName + "_genotypes.vcf");
//                vcfFile.createNewFile();
               
                //vcfFile.createNewFile();
            	BufferedWriter vcfBuffWrite = null;
                int compt = 0;
                    for (int j = 0; j < queryNumber; j++) {
                    	Runnable worker2 = new Threading1(j,startTime,sortedQueries,webAddress,ftpAddress,queryNumber,variantCounter,tempInt,querySize,fileName);
                    executor.execute(worker2);
//                  if (tempInt > 0) {
//                  setProgress((int) ((tempInt + querySize[j]) / (double) querySize[queryNumber] * 99));
//              }
                    compt++;
                    System.out.println("variantCounterfor--->" + variantCounter);
                    }
                
                
                    
                    System.out.println("compt" + compt);
                    executor.shutdown();
                  	// Wait until all threads are finish
                  	while (!executor.isTerminated()) {

                  	}
                  	System.out.println("variantCounteroutfor--->" + variantCounter);
                  	File vcfFile = new File(fileName + "_genotypes.vcf");
                  	vcfFile.createNewFile();
                  	vcfBuffWrite = new BufferedWriter(new FileWriter(vcfFile));
                  	//System.out.println("buffWrite" + vcfBuffWrite);
                  	//HERE
                  	
                  	for (int i = 0; i < compt; i++) {
                   	 if(Threading1.StockLineVcfall.get(i) == null){
                   		 //System.out.println("StockLineVcfall: idx ("+ i +") does not exist");
                   	 }
                   	else{
                   		int localSize = Threading1.StockLineVcfall.get(i).size();
                   		for (int t = 0; t < localSize; t++){
                   			//System.out.println("\tStockLineVcfall ----->" + Threading1.StockLineVcfall.get(i).get(t));
                   			vcfBuffWrite.write(Threading1.StockLineVcfall.get(i).get(t) +"\n");
                   		}
                    }
                   	 
                  	}
                  	vcfBuffWrite.close();
                  	
                  	executor = new ThreadPoolExecutor(300,300,500, TimeUnit.MILLISECONDS,
                  	   		 new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
                
           
        
//        } catch (IOException e) {
//                System.out.println("IOException " + tempInt);
//                return -1;
            } catch (NullPointerException e) {
                System.out.println("Null Pointer Exception " + tempInt);
                return -1;
                // Tabix iterator doesn't have has.next method, so this protects from regions without variants
            } catch (RuntimeException e) {
                System.out.println("Runtime Exception" + tempInt);
                System.out.println("Iteration time: " + (System.nanoTime() - startTime));
                return -1;
            }
            variantCounter = Threading1.variantCounter;
            //System.out.println("variantCounterouttry--->" + variantCounter);
            
            // end VCF writing

            //LinkedList<EspInfoObj> espData = null;
            LinkedList<EspInfoObj> espData = null;
            if (retrieveESP) {
                publish("Downloading Data from Exome Sequencing Project...");
                espData = FerretData.exomeSequencingProject(sortedQueries);
            }
            if (variantCounter == 0 && (espData == null || espData.size() == 0)) {
                File vcfFile = new File(fileName + "_genotypes.vcf");
                System.out.println("test" + variantCounter +"\t"+ espData);
                vcfFile.delete();
                return 0;
            }

            publish("Outputting files...");
            try {
            	//Runnable worker = new CallerRunsPolicyDemo ().new  MyRunnable(urlLocation,urlvep) { @Override public void run() {
                // Creating lookup HashMap for family info, etc.
                HashMap<String, String[]> familyInfo = new HashMap<>(5000);
                BufferedReader familyInfoRead = new BufferedReader(new InputStreamReader(FerretData.class.getClassLoader().getResourceAsStream("family_info.txt")));
                s = familyInfoRead.readLine();
                while ((s = familyInfoRead.readLine()) != null) {
                    String[] text = s.split("\t");
                    String[] temp = {text[0], text[2], text[3], text[4]};
                    familyInfo.put(text[1], temp);
                }
                familyInfoRead.close();

                BufferedWriter mapWrite = null, infoWrite = null, pedWrite = null, frqWrite = null;
                boolean fileEmpty = true, frqFileEmpty = true;
                
//                mapWrite = null; infoWrite = null; pedWrite = null; frqlpoiWrite = null;
//                fileEmpty = true; frqFileEmpty = true;


                if (outputFiles == "all") {
                    genotypes = new String[peopleCounter + 1][2 * variantCounter + 6];

                    File mapFile = new File(fileName + ".map");
                    mapFile.createNewFile();
                    mapWrite = new BufferedWriter(new FileWriter(mapFile));

                    File infoFile = new File(fileName + ".info");
                    infoFile.createNewFile();
                    infoWrite = new BufferedWriter(new FileWriter(infoFile));

                    File pedFile = new File(fileName + ".ped");
                    pedFile.createNewFile();
                    pedWrite = new BufferedWriter(new FileWriter(pedFile));
                }

                BufferedReader vcfRead = new BufferedReader(new FileReader(fileName + "_genotypes.vcf"));



                // Populate the genotypes array with patient/family data
                if (outputFiles == "all") {
                    for (int i = 0; i < peopleCounter; i++) {
                        genotypes[i + 1][1] = peopleOfInterest.get(i);
                        String[] temp = familyInfo.get(genotypes[i + 1][1]);
                        genotypes[i + 1][0] = temp[0];
                        genotypes[i + 1][2] = temp[1];
                        genotypes[i + 1][3] = temp[2];
                        genotypes[i + 1][4] = temp[3];
                        genotypes[i + 1][5] = "0";
                    }
                }

                int index = 0;
               // index = 0;
               
               int espErrorCount = 0;

				// Connecting to the local database containing RegulomeDB scores
				String url = "jdbc:postgresql://localhost:5432/regulome_score";
				String user = "postgres";
				String passwd = "Ferret.1";
				Connection conn = null;
				try {
					Class.forName("org.postgresql.Driver");
					conn = DriverManager.getConnection(url, user, passwd);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// Verifying that connection is OK
				if (conn == null) {
					JOptionPane.showMessageDialog(null, "Connection to regulomeDB failed", "Warning",
							JOptionPane.ERROR_MESSAGE);
				}
                String Varid = null;
              int count = 0;
              int countfake = 0;
              StockLineFreq = new HashMap<>();
              
				//String [] test = {"rs201864858", "rs201864858", "rs372723457", "rs372723457", "rs11553019", "rs11553019", "rs139017158", "rs139017158", "rs149964072", "rs149964072", "rs11553018", "rs11553018", "rs141052988", "rs141052988", "rs145664491", "rs145664491", "rs148306581", "rs148306581", "rs74558623", "rs74558623", "rs79377094", "rs79377094", "rs201610844", "rs201610844", "rs375850117", "rs375850117", "rs201342263", "rs201342263", "rs201892015", "rs201892015", "rs115595484", "rs115595484", "rs367663038", "rs367663038", "rs199853927", "rs199853927", "rs200757617", "rs200757617", "rs138567361", "rs138567361", "rs369805092", "rs369805092", "rs200178716", "rs200178716", "rs150755193", "rs150755193", "rs111662059", "rs111662059", "rs376725006", "rs376725006", "rs375747724", "rs375747724", "rs200476766", "rs200476766", "rs375678465", "rs375678465", "rs370150531", "rs370150531", "rs75915990", "rs75915990", "rs141372323", "rs141372323", "rs121909386", "rs121909386", "rs371779195", "rs371779195", "rs190323395", "rs190323395", "rs375833021", "rs375833021", "rs149516753", "rs149516753", "rs140753322", "rs140753322", "rs183437359", "rs183437359", "rs367727112", "rs367727112", "rs370770872", "rs370770872", "rs200241388", "rs200241388", "rs143432523", "rs143432523", "rs370731821", "rs370731821", "rs201280490", "rs201280490", "rs372770283", "rs372770283", "rs199753491", "rs199753491", "rs182944047", "rs182944047", "rs200892725", "rs200892725", "rs372546647", "rs372546647", "rs112200975", "rs112200975", "rs367763475", "rs367763475", "rs150890799", "rs150890799", "rs140162687", "rs140162687", "rs374218650", "rs374218650", "rs149456198", "rs149456198", "rs141485845", "rs141485845", "rs371085455", "rs371085455", "rs368955427", "rs368955427", "rs376644821", "rs376644821", "rs368720992", "rs368720992", "rs191962962"};
//String [] test = {"rs201864858", "rs201864858", "rs372723457", "rs372723457", "rs11553019", "rs11553019", "rs139017158", "rs139017158", "rs149964072", "rs149964072", "rs11553018", "rs11553018", "rs141052988", "rs141052988", "rs145664491", "rs145664491", "rs148306581", "rs148306581", "rs74558623", "rs74558623", "rs79377094", "rs79377094", "rs201610844", "rs201610844", "rs375850117", "rs375850117", "rs201342263", "rs201342263", "rs201892015", "rs201892015", "rs115595484", "rs115595484", "rs367663038", "rs367663038", "rs199853927", "rs199853927", "rs200757617", "rs200757617", "rs138567361", "rs138567361", "rs369805092", "rs369805092", "rs200178716", "rs200178716", "rs150755193", "rs150755193", "rs111662059", "rs111662059", "rs376725006", "rs376725006", "rs375747724", "rs375747724", "rs200476766", "rs200476766", "rs375678465", "rs375678465", "rs370150531", "rs370150531", "rs75915990", "rs75915990", "rs141372323", "rs141372323", "rs121909386", "rs121909386", "rs371779195", "rs371779195", "rs190323395", "rs190323395", "rs375833021", "rs375833021", "rs149516753", "rs149516753", "rs140753322", "rs140753322", "rs183437359", "rs183437359", "rs367727112", "rs367727112", "rs370770872", "rs370770872", "rs200241388", "rs200241388", "rs143432523", "rs143432523", "rs370731821", "rs370731821", "rs201280490", "rs201280490", "rs372770283", "rs372770283", "rs199753491", "rs199753491", "rs182944047", "rs182944047", "rs200892725", "rs200892725", "rs372546647", "rs372546647", "rs112200975", "rs112200975", "rs367763475", "rs367763475", "rs150890799", "rs150890799", "rs140162687", "rs140162687", "rs374218650", "rs374218650", "rs149456198", "rs149456198", "rs141485845", "rs141485845", "rs371085455", "rs371085455", "rs368955427", "rs368955427", "rs376644821", "rs376644821", "rs368720992", "rs368720992", "rs191962962", "rs200130252", "rs371464691", "rs371464691", "rs149860299", "rs149860299", "rs17853311", "rs17853311", "rs141799648", "rs141799648", "rs200991345", "rs200991345", "rs373556968", "rs373556968", "rs370490541", "rs370490541", "rs192433801", "rs192433801", "rs193921120", "rs193921120", "rs114832056", "rs114832056", "rs141148427", "rs141148427", "rs139868171", "rs139868171", "rs55645588", "rs55645588", "rs148930332", "rs148930332", "rs142380036", "rs142380036", "rs143114089", "rs143114089", "rs371795113", "rs371795113", "rs140868942", "rs140868942", "rs77612325", "rs77612325", "rs201930721", "rs201930721", "rs369386837", "rs369386837", "rs200063787", "rs200063787", "rs144694271", "rs144694271", "rs61754144", "rs61754144", "rs371058203", "rs371058203", "rs374299422", "rs374299422", "rs202183416", "rs202183416", "rs376237191", "rs376237191", "rs142931576", "rs142931576", "rs138453809", "rs138453809", "rs372669126", "rs372669126", "rs200741488", "rs200741488", "rs145258022", "rs145258022", "rs142133680", "rs142133680", "rs111936698", "rs111936698", "rs200087965", "rs200087965", "rs199969149", "rs199969149", "rs141849409", "rs141849409", "rs201695911", "rs201695911", "rs139592813", "rs139592813", "rs147924412", "rs147924412", "rs140792332", "rs140792332", "rs377181231", "rs377181231", "rs150189833", "rs150189833", "rs375141804", "rs375141804", "rs116116323", "rs116116323", "rs376471217", "rs376471217", "rs372047402", "rs372047402", "rs146864292", "rs146864292", "rs150447575", "rs150447575", "rs201238689", "rs201238689", "rs56060938", "rs56060938", "rs145969024", "rs145969024", "rs150396730", "rs150396730", "rs375719718", "rs375719718", "rs368122000", "rs368122000", "rs202219610", "rs202219610", "rs141553742", "rs141553742", "rs143598492", "rs143598492", "rs139848963", "rs139848963", "rs150726674", "rs150726674", "rs150071690", "rs150071690", "rs369614398", "rs369614398", "rs199508384", "rs199508384", "rs142579927", "rs142579927", "rs370707974", "rs370707974", "rs199541629", "rs199541629", "rs144489576", "rs144489576", "rs141603160", "rs141603160", "rs200376025", "rs200376025", "rs377247971", "rs377247971", "rs368507889", "rs368507889", "rs200504997", "rs200504997", "rs372554319", "rs372554319", "rs200846585", "rs200846585", "rs374693835", "rs374693835", "rs199677710", "rs199677710", "rs149375068", "rs149375068", "rs35715176", "rs35715176", "rs370312220", "rs370312220", "rs187291416", "rs187291416", "rs377047066", "rs377047066", "rs369470185", "rs369470185", "rs201203932", "rs201203932", "rs370657286", "rs370657286", "rs377175915", "rs377175915", "rs376584070", "rs376584070", "rs370690563", "rs370690563", "rs367765965", "rs367765965", "rs372071733", "rs372071733", "rs150523147", "rs150523147", "rs371354151", "rs371354151", "rs372163152", "rs372163152", "rs201841925", "rs201841925", "rs71427088", "rs71427088", "rs374710689", "rs374710689", "rs368841036", "rs368841036", "rs200413243", "rs200413243", "rs370205425", "rs370205425", "rs191606676", "rs191606676", "rs367710609", "rs367710609", "rs142154282", "rs142154282", "rs181473681", "rs181473681", "rs187155661", "rs187155661", "rs199818112", "rs199818112", "rs371033068", "rs371033068", "rs373626756", "rs373626756", "rs368217243", "rs368217243", "rs369596744", "rs369596744", "rs202089899", "rs202089899", "rs200820148", "rs200820148", "rs71427089", "rs71427089", "rs371667746", "rs371667746", "rs71427090", "rs71427090", "rs182279361", "rs182279361", "rs373744329", "rs373744329", "rs151143572", "rs151143572", "rs139005498", "rs139005498", "rs375375643", "rs375375643", "rs375994530", "rs375994530", "rs374460728", "rs374460728", "rs184478113", "rs184478113", "rs374283621", "rs374283621", "rs376710920", "rs376710920", "rs368058960", "rs368058960", "rs369812628", "rs369812628", "rs377218922", "rs377218922", "rs373937760", "rs373937760", "rs367716506", "rs367716506", "rs187784560", "rs187784560", "rs371057125", "rs371057125", "rs375430386", "rs375430386", "rs200360125", "rs200360125", "rs376612569", "rs376612569", "rs201025190", "rs201025190", "rs368242766", "rs368242766", "rs371796431", "rs371796431", "rs375965191", "rs375965191", "rs201857731", "rs201857731", "rs149461143", "rs149461143", "rs201676969", "rs201676969", "rs182265220", "rs182265220", "rs376575004", "rs376575004", "rs368659402", "rs368659402", "rs138453809", "rs372669126", "rs372669126", "rs200741488", "rs200741488", "rs145258022", "rs145258022", "rs142133680", "rs142133680", "rs111936698", "rs111936698", "rs200087965", "rs200087965", "rs199969149", "rs199969149", "rs141849409", "rs141849409", "rs201695911", "rs201695911", "rs139592813", "rs139592813", "rs147924412", "rs147924412", "rs140792332", "rs140792332", "rs377181231", "rs377181231", "rs150189833", "rs150189833", "rs375141804", "rs375141804", "rs116116323", "rs116116323", "rs376471217", "rs376471217", "rs372047402", "rs372047402", "rs146864292", "rs146864292", "rs150447575", "rs150447575", "rs201238689", "rs201238689", "rs56060938", "rs56060938", "rs145969024", "rs145969024", "rs150396730", "rs150396730", "rs375719718", "rs375719718", "rs368122000", "rs368122000", "rs202219610", "rs202219610", "rs141553742", "rs141553742", "rs143598492", "rs143598492", "rs139848963", "rs139848963", "rs150726674", "rs150726674", "rs150071690", "rs150071690", "rs369614398", "rs369614398", "rs199508384", "rs199508384", "rs142579927", "rs142579927", "rs370707974", "rs370707974", "rs199541629", "rs199541629", "rs144489576", "rs144489576", "rs141603160", "rs141603160", "rs200376025", "rs200376025", "rs377247971", "rs377247971", "rs368507889", "rs368507889", "rs200504997", "rs200504997", "rs372554319", "rs372554319", "rs200846585", "rs200846585", "rs374693835", "rs374693835", "rs199677710", "rs199677710", "rs149375068", "rs149375068", "rs35715176", "rs35715176", "rs370312220", "rs370312220", "rs187291416", "rs187291416", "rs377047066", "rs377047066", "rs369470185", "rs369470185", "rs201203932", "rs201203932", "rs370657286", "rs370657286", "rs377175915", "rs377175915", "rs376584070", "rs376584070", "rs370690563", "rs370690563", "rs62269205", "rs190964735", "rs190964735", "rs370057056", "rs370057056", "rs201919360", "rs201919360", "rs139164443", "rs139164443", "rs201539423", "rs201539423", "rs139137023", "rs139137023", "rs146027039", "rs146027039", "rs142746163", "rs142746163", "rs144519399", "rs144519399", "rs201585253", "rs201585253", "rs182963279", "rs182963279", "rs148433157", "rs148433157", "rs115649165", "rs115649165", "rs145512022", "rs145512022", "rs200029008", "rs200029008", "rs149648608", "rs149648608", "rs112272566", "rs112272566", "rs143072070", "rs143072070", "rs148962924", "rs148962924", "rs143709160", "rs143709160", "rs148044268", "rs148044268", "rs200848106", "rs200848106", "rs145984650", "rs145984650", "rs200363593", "rs200363593", "rs141814734", "rs141814734", "rs9282642", "rs9282642", "rs2681417", "rs2681417", "rs151260884", "rs151260884", "rs371529328", "rs371529328", "rs144220043", "rs144220043", "rs373228315", "rs373228315", "rs1129055", "rs1129055", "rs370380261", "rs370380261", "rs9282648", "rs9282648", "rs368315978", "rs368315978", "rs371670872", "rs371670872", "rs144326666", "rs144326666", "rs139407450", "rs139407450", "rs370220584", "rs370220584", "rs193922420", "rs193922420", "rs193922432", "rs193922432", "rs104893691", "rs104893691", "rs104893695", "rs104893695", "rs201923228", "rs201923228", "rs121909264", "rs121909264", "rs104893689", "rs104893689", "rs104893697", "rs104893697", "rs200004528", "rs200004528", "rs201091657", "rs201091657", "rs373376842", "rs373376842", "rs62269092", "rs62269092", "rs202179597", "rs202179597", "rs200554202", "rs200554202", "rs200039241", "rs200039241", "rs121909259", "rs121909259", "rs200838528", "rs200838528", "rs193922444", "rs193922444", "rs200771541", "rs200771541", "rs201338034", "rs201338034", "rs145869851", "rs145869851", "rs199980578", "rs199980578", "rs202228006", "rs202228006", "rs201177696", "rs201177696", "rs193922421", "rs193922421", "rs141315218", "rs141315218", "rs192507347", "rs192507347", "rs201423861", "rs201423861", "rs199719951", "rs199719951", "rs140467141", "rs140467141", "rs148174536", "rs148174536", "rs77838721", "rs77838721", "rs377268683", "rs377268683", "rs201572629", "rs201572629", "rs104893716", "rs104893716", "rs200240922", "rs200240922", "rs201536450", "rs201536450", "rs193922423", "rs193922423", "rs377233360", "rs377233360", "rs199688157", "rs199688157", "rs201568219", "rs201568219", "rs115230894", "rs115230894", "rs104893719", "rs104893719", "rs193922425", "rs193922425", "rs201048395", "rs201048395", "rs104893712", "rs104893712", "rs143470909", "rs143470909", "rs201852643", "rs201852643", "rs193922430", "rs193922430", "rs201609857", "rs201609857", "rs104893700", "rs104893700", "rs193922431", "rs193922431", "rs200818687", "rs200818687", "rs201828974", "rs201828974", "rs201670662", "rs201670662", "rs104893718", "rs104893718", "rs200318708", "rs200318708", "rs375468610", "rs375468610", "rs193922433", "rs193922433", "rs199508583", "rs199508583", "rs193922436", "rs193922436", "rs104893706", "rs104893706", "rs373819680", "rs373819680", "rs200777304", "rs200777304", "rs121909269", "rs121909269", "rs201449422", "rs201449422", "rs76327999", "rs76327999", "rs4987051", "rs4987051", "rs200238591", "rs200238591", "rs142704083", "rs142704083", "rs201739901", "rs201739901", "rs144802947", "rs144802947", "rs201579531", "rs201579531", "rs370210949", "rs370210949", "rs142032585", "rs142032585", "rs200394711", "rs200394711", "rs368381766", "rs201864858", "rs372723457", "rs372723457", "rs11553019", "rs11553019", "rs139017158", "rs139017158", "rs149964072", "rs149964072", "rs11553018", "rs11553018", "rs141052988", "rs141052988", "rs145664491", "rs145664491", "rs148306581", "rs148306581", "rs74558623", "rs74558623", "rs79377094", "rs79377094", "rs201610844", "rs201610844", "rs375850117", "rs375850117", "rs201342263", "rs201342263", "rs201892015", "rs201892015", "rs115595484", "rs115595484", "rs367663038", "rs367663038", "rs199853927", "rs199853927", "rs200757617", "rs200757617", "rs138567361", "rs138567361", "rs369805092", "rs369805092", "rs200178716", "rs200178716", "rs150755193", "rs150755193", "rs111662059", "rs111662059", "rs376725006", "rs376725006", "rs375747724", "rs375747724", "rs200476766", "rs200476766", "rs375678465", "rs375678465", "rs370150531", "rs370150531", "rs75915990", "rs75915990", "rs141372323", "rs141372323", "rs121909386", "rs121909386", "rs371779195", "rs371779195", "rs190323395", "rs190323395", "rs375833021", "rs375833021", "rs149516753", "rs149516753", "rs140753322", "rs140753322", "rs183437359", "rs183437359", "rs367727112", "rs367727112", "rs370770872", "rs370770872", "rs200241388", "rs200241388", "rs143432523", "rs143432523", "rs370731821", "rs370731821", "rs201280490", "rs201280490", "rs372770283", "rs372770283", "rs199753491", "rs199753491", "rs182944047", "rs182944047", "rs200892725", "rs200892725", "rs372546647", "rs372546647", "rs112200975", "rs112200975", "rs367763475", "rs367763475", "rs150890799", "rs150890799", "rs140162687", "rs140162687", "rs374218650", "rs374218650", "rs149456198", "rs149456198", "rs141485845", "rs141485845", "rs371085455", "rs371085455", "rs368955427", "rs368955427", "rs376644821", "rs376644821", "rs368720992", "rs368720992", "rs191962962", "rs200130252", "rs371464691", "rs375430386", "rs375430386", "rs200360125", "rs200360125", "rs376612569", "rs376612569", "rs201025190", "rs201025190", "rs368242766", "rs368242766", "rs371796431", "rs371796431", "rs375965191", "rs375965191", "rs201857731", "rs201857731", "rs149461143", "rs149461143", "rs201676969", "rs201676969", "rs182265220", "rs182265220", "rs376575004", "rs376575004", "rs368659402", "rs368659402", "rs138453809", "rs372669126", "rs372669126", "rs200741488", "rs200741488", "rs145258022", "rs145258022", "rs142133680", "rs142133680", "rs111936698", "rs111936698", "rs200087965", "rs200087965", "rs199969149", "rs199969149", "rs141849409", "rs141849409", "rs201695911", "rs201695911", "rs139592813", "rs139592813", "rs147924412", "rs147924412", "rs140792332", "rs140792332", "rs377181231", "rs377181231", "rs150189833", "rs150189833", "rs375141804", "rs375141804", "rs116116323", "rs116116323", "rs376471217", "rs376471217", "rs372047402", "rs372047402", "rs146864292", "rs146864292", "rs150447575", "rs150447575", "rs201238689", "rs201238689", "rs56060938", "rs56060938", "rs145969024", "rs145969024", "rs150396730", "rs150396730", "rs375719718", "rs375719718", "rs368122000", "rs368122000", "rs202219610", "rs202219610", "rs141553742", "rs141553742", "rs143598492", "rs143598492", "rs139848963", "rs139848963", "rs150726674", "rs150726674", "rs150071690", "rs150071690", "rs369614398", "rs369614398", "rs199508384", "rs199508384", "rs142579927", "rs142579927", "rs370707974", "rs370707974", "rs199541629", "rs199541629", "rs144489576", "rs144489576", "rs141603160", "rs141603160", "rs200376025", "rs200376025", "rs377247971", "rs377247971", "rs368507889", "rs368507889", "rs200504997", "rs200504997", "rs372554319", "rs372554319", "rs200846585", "rs200846585", "rs374693835", "rs374693835", "rs199677710", "rs199677710", "rs149375068", "rs149375068", "rs35715176", "rs35715176", "rs370312220", "rs370312220", "rs187291416", "rs187291416", "rs377047066", "rs377047066", "rs369470185", "rs369470185", "rs201203932", "rs201203932", "rs370657286", "rs370657286", "rs377175915", "rs377175915", "rs376584070", "rs376584070", "rs370690563", "rs370690563", "rs62269205", "rs190964735", "rs190964735", "rs370057056", "rs370057056", "rs201919360", "rs201919360", "rs139164443", "rs139164443", "rs201539423", "rs201539423", "rs139137023", "rs139137023", "rs146027039", "rs146027039", "rs142746163", "rs142746163", "rs144519399", "rs144519399", "rs201585253", "rs201585253", "rs182963279", "rs182963279", "rs148433157", "rs148433157", "rs115649165", "rs115649165", "rs145512022", "rs145512022", "rs200029008", "rs200029008", "rs149648608", "rs149648608", "rs112272566", "rs112272566", "rs143072070", "rs143072070", "rs148962924", "rs148962924", "rs143709160", "rs143709160", "rs148044268", "rs148044268", "rs200848106", "rs200848106", "rs145984650", "rs145984650", "rs200363593", "rs200363593", "rs141814734", "rs141814734", "rs9282642", "rs9282642", "rs2681417"};
        
                while ((s = vcfRead.readLine()) != null) {
                	
                   String [] text = s.split("\t");
                    //System.out.print("\t text" + text[2]);
                    Varid = text[2].substring(2);
                    //System.out.print("test" + test[count]);
                	//System.out.println("Varid : " + Varid);
                   
/* text[2] contains all the rsid for a gene, a locus or variants inputted by the user*/

                    
                    
                   
                 worker = new AnnotThreading(count,Varid,conn);
                    //worker = new AnnotThreading(count,Varid,conn);
                 executor.execute(worker);
                 setProgress(Threading1.progress);
                 

                    


                    String[] variantPossibilities;
                    String[] multAllele = text[4].split(",");
                    int[] variantFreq = {0, 0};
                    variantPossibilities = new String[multAllele.length + 1];
                    variantPossibilities[0] = text[3];
                    for (int i = 0; i < multAllele.length; i++) {
                        variantPossibilities[i + 1] = multAllele[i];
                    }
                    // indel trick
                    String[] retainedPossibility = new String[2];
                    if (!text[4].contains("CN") && variantPossibilities.length == 2
                            && (variantPossibilities[0].length() > 1 || variantPossibilities[1].length() > 1)) {
                        text[2] = "indel_" + text[2] + "_" + variantPossibilities[0] + "/" + variantPossibilities[1];
                        retainedPossibility[0] = variantPossibilities[0];
                        retainedPossibility[1] = variantPossibilities[1];
                        variantPossibilities[0] = "A";
                        variantPossibilities[1] = "T";
                    }
                    int numChr = 0;
                    double freqZero = 0, freqOne = 0;
                    // only write for biallelic variants
                    if (variantPossibilities.length == 2) {
                        //CN = CNV
                        if (!text[4].contains("CN")) {
                            for (int i = 0; i < peopleOfInterest.size(); i++) {
                                String tempPerson = peopleOfInterest.get(i);
                                int personIndex = peopleSet.get(tempPerson);
                                String personGtype = text[personIndex];
                                int temp = Character.getNumericValue(personGtype.charAt(0));
                                if (outputFiles == "all") {
                                    genotypes[i + 1][2 * index + 6] = variantPossibilities[temp];
                                }
                                variantFreq[temp]++;
                                temp = Character.getNumericValue(personGtype.charAt(2));
                                if (outputFiles == "all") {
                                    genotypes[i + 1][2 * index + 7] = variantPossibilities[temp];
                                }
                                variantFreq[temp]++;
                            }
                            // If you were thinking index should be incremented here, you're right
                            // but now it's in essentially the same loop several lines down
                        } else if (text[4].contains("CN")) {
                            for (int i = 0; i < peopleOfInterest.size(); i++) {
                                String tempPerson = peopleOfInterest.get(i);
                                int personIndex = peopleSet.get(tempPerson);
                                String personGtype = text[personIndex];
                                int temp = Character.getNumericValue(personGtype.charAt(0));
                                variantFreq[temp]++;
                                temp = Character.getNumericValue(personGtype.charAt(2));
                                variantFreq[temp]++;
                            }
                        }
                        numChr = variantFreq[0] + variantFreq[1];
                        freqZero = Math.round(variantFreq[0] / ((double) numChr) * 10000) / 10000.0;
                        freqOne = Math.round(variantFreq[1] / ((double) numChr) * 10000) / 10000.0;
                        if (outputFiles == "all") {
                            if (!text[4].contains("CN")) {
                                genotypes[0][2 * index + 6] = Double.toString(freqZero);
                                genotypes[0][2 * index + 7] = Double.toString(freqZero);
                                index++;
                            }
                            //if (!text[4].contains("CN") && (freqZero >= MAF && freqOne >= MAF && freqOne <= MAFMax)) {
                            if (!text[4].contains("CN") && (freqZero >= MAF && freqOne >= MAF && ((freqOne <= MAFMax) || (freqZero <= MAFMax)))) {
                                fileEmpty = false;
                                mapWrite.write(text[0] + "\t" + text[2] + "\t0\t" + text[1]);
                                mapWrite.newLine();
                                infoWrite.write(text[2] + "\t" + text[1]);
                                infoWrite.newLine();
                            }
                        }
                    }
                    if (text[2].contains("indel")) {
                        variantPossibilities = retainedPossibility;
                    }
                    // frq file is written here, ESP data is added
                    if (retrieveESP) {
                        // The following line says: while espData is not empty AND [esp chr is less than 1KG chr OR (esp chr is equal to 1KG chr AND esp pos is less than 1KG pos)] then:
                        while (!espData.isEmpty() && ((espData.peek().getChrAsInt() < Integer.parseInt(text[0]))
                                || ((espData.peek().getChrAsInt() == Integer.parseInt(text[0])) && (espData.peek().getPos() < Integer.parseInt(text[1]))))) {
                            EspInfoObj temp = espData.remove();
                            if ((temp.getEAFreq() >= espMAF && temp.getEAFreq() <= (1 - espMAF)) || (temp.getAAFreq() >= espMAF && temp.getAAFreq() <= (1 - espMAF))) {
                                String snpName;
                                if (temp.getSNP().equals(".")) {
                                    snpName = "chr" + temp.getChr() + "_" + temp.getPos();
                                } else {
                                    snpName = temp.getSNP();
                                }
                                frqFileEmpty = false;

                                StockLineFreq.put(count,(temp.getChr() + "\t" + snpName + "\t" + temp.getPos() + "\t"
                                        + temp.getRefAllele() + "\t" + temp.getAltAllele() + "\t" + "." + "\t" + "." + "\t"
                                        + df.format(temp.getEAFreq()) + "\t" + df.format(temp.getAAFreq())));

                            }
                        }
                        // The following line says: if espData is not empty AND esp chr equals 1KG chr AND esp pos equals 1KG pos AND it's biallelic
                        if (!espData.isEmpty() && espData.peek().getChrAsInt() == Integer.parseInt(text[0]) && espData.peek().getPos() == Integer.parseInt(text[1]) && variantPossibilities.length == 2) {
                            EspInfoObj temp = espData.remove();///
                            // 1KG and ESP Ref alleles and Alt alleles match
                            if (variantPossibilities[0].equals(temp.getRefAllele()) && variantPossibilities[1].equals(temp.getAltAllele())) {
                                if ((freqZero >= MAF && freqOne >= MAF && ((freqOne <= MAFMax) || (freqZero <= MAFMax))) || (temp.getEAFreq() >= espMAF && temp.getEAFreq() <= (1 - espMAF)) || (temp.getAAFreq() >= espMAF && temp.getAAFreq() <= (1 - espMAF))) {
                                	frqFileEmpty = false;
                                    StockLineFreq.put(count,(text[0] + "\t" + text[2] + "\t" + text[1] + "\t"
                                            + variantPossibilities[0] + "\t" + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t"
                                            + df.format(temp.getEAFreq()) + "\t" + df.format(temp.getAAFreq() +"t")));
                                }
                                // 1KG and ESP ref/alt alleles are switched
                            } else if (variantPossibilities[0].equals(temp.getAltAllele()) && variantPossibilities[1].equals(temp.getRefAllele())) {
                                if ((freqOne >= MAF && freqZero >= MAF && ((freqOne <= MAFMax) || (freqZero <= MAFMax))) || (temp.getEAFreq() >= espMAF && temp.getEAFreq() <= (1 - espMAF)) || (temp.getAAFreq() >= espMAF && temp.getAAFreq() <= (1 - espMAF))) {
                                	frqFileEmpty = false;
                                    StockLineFreq.put(count,(text[0] + "\t" + text[2] + "\t" + text[1] + "\t"
                                            + variantPossibilities[0] + "\t" + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t"
                                            + df.format(temp.getEAFreqAlt()) + "\t" + df.format(temp.getAAFreqAlt()+"t")));
                                }
                                // ESP does not match with 1KG so ESP omitted
                            } else {
                                if (freqOne >= MAF && freqZero >= MAF && ((freqOne <= MAFMax) || (freqZero <= MAFMax))) {
                                	frqFileEmpty = false;
                                    StockLineFreq.put(count,(text[0] + "\t" + text[2] + "\t" + text[1] + "\t"
                                            + variantPossibilities[0] + "\t" + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t"
                                            + "." + "\t" + "."+"t"));
                                }
                                espErrorCount++;
                            }
                        } else if (variantPossibilities.length == 2) {
                            if (freqOne >= MAF && freqZero >= MAF && ((freqOne <= MAFMax) || (freqZero <= MAFMax))) {
                            	frqFileEmpty = false;
                                StockLineFreq.put(count,(text[0] + "\t" + text[2] + "\t" + text[1] + "\t"
                                        + variantPossibilities[0] + "\t" + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t"
                                        + "." + "\t" + "."+"t"));
                            }
                        }
                    } else {
                        if (variantPossibilities.length == 2 && freqOne >= MAF && freqZero >= MAF && ((freqOne <= MAFMax) || (freqZero <= MAFMax))) {
                        	frqFileEmpty = false;
                            //System.out.print("BECAUSE1 : " + count + "\t" +Varid);
                            if(annotFiles.equals("no"))
                            {
                            	//System.out.print("BECAUSE2 : " + count + "\t" +Varid);
                            	StockLineFreq.put(count,(text[0] + "\t" + text[2] + "\t" + text[1] + "\t" + variantPossibilities[0] + "\t"
                                    + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t" + df.format(freqOne)+"\t"));
                            }
                            if(annotFiles.equals("def"))
                            {
                            	//System.out.print("BECAUSE3 : " + count + "\t" +Varid);
                            	StockLineFreq.put(count,(text[0] + "\t" + text[2] + "\t" + text[1] + "\t" + variantPossibilities[0] + "\t"
                                    + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t" + df.format(freqOne)+"\t")); // + "\t" + geneSymbol+ "\t" + geneId+ "\t" + fxnName + "\t" + proteinPos + "\t" + aa2 + aa1  + "\t" + proteinAcc);
                            }
                            if(annotFiles.equals("adv"))
                            {
                            	//System.out.print("BECAUSE4 : " + count + "\t" + Varid);
                            	StockLineFreq.put(count,(text[0] + "\t" + text[2] + "\t" + text[1] + "\t" + variantPossibilities[0] + "\t"
                                    + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t" + df.format(freqOne)+"\t"));
                            	//System.out.print("BECAUSEOFYOU-----> : " + StockLineFreq + "\t" + Varid + "\t" + count);
                            }
                        }
                    }
                    //System.out.println("StockLineFreq[count] : " + StockLineFreq[count]);

                    count++;
                   
                }
            
                System.out.println("count = " + count);
               


                executor.shutdown();
              	// Wait until all threads are finish
              	while (!executor.isTerminated()) {

              	}
            
              	
              	System.out.println("\nFinished all threads");
              	 File freqFile = new File(fileName + "_Summary.txt");
                 freqFile.createNewFile();
                 frqWrite = new BufferedWriter(new FileWriter(freqFile));
                 if (retrieveESP) {
                	// System.out.print("BECAUSE5 : " + count + "\t" +Varid);
                     frqWrite.write("CHROM\tVARIANT\tPOS\tALLELE1\tALLELE2\tNB_1KG_CHR\t1KG_A1_FREQ\tESP6500_EA_A1_FREQ\tESP6500_AA_A1_FREQ\tGENENAME\tGENEID\tFUNCTION\tPROTEINPOS\tAACHANGE\tPROTEINACC");
                  
                     frqWrite.newLine();
                 } else {
                 	if(annotFiles.equals("no")){
                 		//System.out.print("BECAUSE6 : " + count + "\t" +Varid);
                     frqWrite.write("CHROM\tVARIANT\tPOS\tALLELE1\tALLELE2\tNB_CHR\t1KG_A1_FREQ\t1KG_A2_FREQ");
                     frqWrite.newLine();
                 	}
                 	if(annotFiles.equals("def")){
                 		//System.out.print("BECAUSE7 : " + count + "\t" +Varid);
                         frqWrite.write("CHROM\tVARIANT\tPOS\tALLELE1\tALLELE2\tNB_CHR\t1KG_A1_FREQ\t1KG_A2_FREQ\tGENENAME\tGENEID\tFUNCTION\tPROTEINPOS\tAACHANGE\tPROTEINACC");
                         frqWrite.newLine();
                     	}
                 	if(annotFiles.equals("adv")){
                 		//System.out.print("BECAUSE8 : " + count + "\t" +Varid);
                 		frqWrite.write("CHROM\tVARIANT\tPOS\tALLELE1\tALLELE2\tNB_CHR\t1KG_A1_FREQ\t1KG_A2_FREQ\tGENENAME\tGENEID\tFUNCTION\tPROTEINPOS\tAACHANGE\tPROTEINACC\tRBDB_SCORE\tRBDB_PREDICTION\tSIFT_SCORE\tSIFT_PREDICTION\tPOLYPHEN_SCORE\tPOLYPHEN_PREDICTION\tPROVEAN_SCORE\tPROVEAN_PREDICTION\tCADD_SCORE");
                 		frqWrite.newLine();
                     	}
                 }

                System.out.println();

                for (int i = 0; i < count; i++) {
                	 if(StockLineFreq.get(i) == null){
                		 //System.out.println("StockLineFreq: idx ("+ i +") not exist");
                	 }
                	 else if(AnnotThreading.StockLineAnnotdef.get(i) == null && AnnotThreading.StockLineAnnotadv.get(i) == null){
                		 //System.out.println("StockLineAnnot: idx ("+ i +") not exist");
                	 }
                	 else{
                		//System.out.println("StockLineFreq ----->" + StockLineFreq.get(i)+ "\tStockLineAnnot ----->" + AnnotThreading.StockLineAnnot.get(i));
                		 if(annotFiles.equals("no")){
                			 frqWrite.write(StockLineFreq.get(i) +"\n");
                		 }
                		 if(annotFiles.equals("def")){
                			 frqWrite.write(StockLineFreq.get(i) +  AnnotThreading.StockLineAnnotdef.get(i) +"\n");
                		 }
                		 if(annotFiles.equals("adv")){
                		 
                         frqWrite.write(StockLineFreq.get(i) +  AnnotThreading.StockLineAnnotdef.get(i) +  AnnotThreading.StockLineAnnotadv.get(i) +"\n");
                     }
                		 
                	 }
                 }
                // this bracket marks the end of VCF reading
                // Don't need to have MAF threshold here, because not written to genotypes array if MAF too low
                if (outputFiles == "all") {
                    for (int i = 0; i < peopleCounter; i++) {
                        // Write information about individuals
                        for (int j = 0; j <= 5; j++) {
                            pedWrite.write(genotypes[i + 1][j] + "\t");
                        }
                        // Write information about genotypes
                        for (int j = 6; j < index * 2 + 6; j++) {
                            if (Double.parseDouble(genotypes[0][j]) >= MAF && (1 - Double.parseDouble(genotypes[0][j])) >= MAF && (Double.parseDouble(genotypes[0][j]) <= MAFMax || (1 - Double.parseDouble(genotypes[0][j])) <= MAFMax)) {
                            	pedWrite.write(genotypes[i + 1][j] + "\t");
                            }
                        }
                        pedWrite.newLine();
                    }
                    pedWrite.close();
                    mapWrite.close();
                    infoWrite.close();
                    if (fileEmpty) {
                        new File(fileName + ".ped").delete();
                        new File(fileName + ".info").delete();
                        new File(fileName + ".map").delete();
                    }

                }


                frqWrite.close();
                vcfRead.close();
                File vcfFile = new File(fileName + "_genotypes.vcf");
                vcfFile.delete();
                if (frqFileEmpty) {
                	System.out.print("OUI");
                    freqFile.delete();
                    return -3;
                }
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            	if(htmlOutputFile) {
					HtmlOutput html = new HtmlOutput(freqFile.getAbsolutePath(), freqFile.getName());
					html.writeFile(annotFiles);
				}
            }
            catch (IOException e) {
                return -1;
            }
            setProgress(100);
            System.out.println("Finished");

        }
//        if (usehaplo) {
//        	Process proc;
//            try {
//            	//Process  proc = Runtime.getRuntime().exec("java -jar \"/FerretMVC/Haploview.jar\"");
//            	proc = Runtime.getRuntime().exec("java -jar \"Haploview.jar\" -pedfile \"" + fileName + ".ped\" -info \"" + fileName + ".info\"");
//            } catch (IOException e) {
//            }
//        }
        if (downloadHaplo) {
        	Process proc;
            try {
            	//Process  proc = Runtime.getRuntime().exec("java -jar \"/FerretMVC/Haploview.jar\"");
            	proc = Runtime.getRuntime().exec("java -jar \"Haploview.jar\" -pedfile \"" + fileName + ".ped\" -info \"" + fileName + ".info\"");
            } catch (IOException e) {
            }
        }
        return 1;
    }


    @Override
    protected void process(List<String> processStatus) {
        int statusIndex = processStatus.size();
        status.setText(processStatus.get(statusIndex - 1));
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private static ArrayList<InputRegion> sortByWindow(InputRegion[] queries) {

        ArrayList<ArrayList<InputRegion>> sortedByChr = new ArrayList<>(23);
        for (int i = 0; i < 23; i++) {
            sortedByChr.add(new ArrayList<>());
        }

        for (int i = 0; i < queries.length; i++) {
            sortedByChr.get(queries[i].getChrAsInt() - 1).add(queries[i]);
        }
        ArrayList<InputRegion> sortedQueries;
        sortedQueries = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            ArrayList<InputRegion> currentList = sortedByChr.get(i);
            Collections.sort(currentList);
            int j = 0;
            while (j < currentList.size()) {
                String currentChr = currentList.get(j).getChr();
                int minPos = currentList.get(j).getStart();
                int maxPos = currentList.get(j++).getEnd();
                while (j < currentList.size() && currentList.get(j).getStart() <= maxPos) {
                    maxPos = currentList.get(j++).getEnd();
                }
                sortedQueries.add(new InputRegion(currentChr, minPos, maxPos));
            }
        }
        return sortedQueries;
    }

    public static HashMap<String, Integer> getPopulationIndices(String fileName) throws IOException {
        // Since this seems to stay the same, this could be added to the population txt file. It would
        // probably save 3-4 seconds of run time
        HashMap<String, Integer> peopleSet = new HashMap<>(3500);

        TabixReader tr = new TabixReader(fileName);
        // Get the header info
        String s = tr.readLine();
        while (!s.contains("CHROM")) {
            s = tr.readLine();
        }
        String[] peopleStringArray = s.split("\t");
        for (int i = 0; i < peopleStringArray.length; i++) {
            peopleSet.put(peopleStringArray[i], i);
        }

        return peopleSet;
    }

    public static String getPeopleStringPhase3(String chrNum) {

    	System.out.println("phase 3");
    	  String s = null;
        try {
        	
            //String webAddress = "ftp://ftp-trace.ncbi.nih.gov/1000genomes/ftp/release/20130502/ALL.chr" + chrNum + ".phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz";
            String webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/ALL.chr" + chrNum + ".phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz";
            // Prepares objects for file writing
            TabixReader tr = new TabixReader(webAddress);
            // Get the header info
            s = tr.readLine();
            while (!s.contains("CHROM")) {
                s = tr.readLine();
            }
            
        } catch (IOException | RuntimeException e) {
        }
        return s;

    }

    public static String getPeopleStringPhase3GRCh38(String chrNum) {
        // helper method for the below method
    	System.out.println("phase 3 38");
        String s = null;
        try {
            String webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/supporting/GRCh38_positions/ALL.chr" + chrNum + ".phase3_shapeit2_mvncall_integrated_v3plus_nounphased.rsID.genotypes.GRCh38_dbSNP_no_SVs.vcf.gz";
            // Prepares objects for file writing
            TabixReader tr = new TabixReader(webAddress);
            // Get the header info
            s = tr.readLine();
            while (!s.contains("CHROM")) {
                s = tr.readLine();
            }
        } catch (IOException | RuntimeException e) {
        }
        
        return s;
    }

    public static String getPeopleStringPhase1(String chrNum) {
        // helper method for the below method
    	System.out.println("phase 1");
    	String s = null;
    		 
        try {
            //ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20110521/
            String webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20110521/ALL.chr" + chrNum + ".phase1_release_v3.20101123.snps_indels_svs.genotypes.vcf.gz";
            TabixReader tr = new TabixReader(webAddress);
            // Get the header info
            s = tr.readLine();
            while (!s.contains("CHROM")) {
                s = tr.readLine();
            }
            
        } catch (IOException | RuntimeException e) {
        }

        return s;
    }

    public static boolean testPeopleOrder(String whichTest, String match) {
        // Run this to make sure the people are in the same order between all chromosomes, mainly for testing; never run
        for (int i = 1; i < 23; i++) {
            switch (whichTest) {
                case "Phase 1":
                    if (!match.equals(getPeopleStringPhase1(Integer.toString(i)))) {
                        return false;
                    }
                    break;
                case "Phase 3":
                    if (!match.equals(getPeopleStringPhase3(Integer.toString(i)))) {
                        return false;
                    }
                    break;
                case "Phase 3 GRCh38":
                    if (!match.equals(getPeopleStringPhase3GRCh38(Integer.toString(i)))) {
                        return false;
                    }
                    break;
            }
        }
        /*
        switch (whichTest){
        case "Phase 1":
            if(!match.equals(getPeopleStringPhase1("X"))){
                return false;
            }
        case "Phase 3":
            if(!match.equals(getPeopleStringPhase3("X"))){
                return false;
            }
        case "Phase 3 GRCh38":
            if(!match.equals(getPeopleStringPhase3GRCh38("X"))){
                return false;
            }
        }*/
        return true;
    }

    public static boolean testPeopleOrder(String whichTest) {
        // Run this to make sure the people are in the same order between all chromosomes, mainly for testing; never run
        for (int i = 2; i < 23; i++) {
            switch (whichTest) {
                case "Phase 1":
                    if (getPeopleStringPhase1("1") != null && !getPeopleStringPhase1("1").equals(getPeopleStringPhase1(Integer.toString(i)))) {
                        return false;
                    } else {

                    }
                    break;
                case "Phase 3":
                    if (getPeopleStringPhase3("1") != null && !(getPeopleStringPhase3("1").equals(getPeopleStringPhase3(Integer.toString(i))))) {
                        return false;
                    }
                    break;
                case "Phase 3 GRCh38":
                    if (getPeopleStringPhase3GRCh38("1") != null && !getPeopleStringPhase3GRCh38("1").equals(getPeopleStringPhase3GRCh38(Integer.toString(i)))) {
                        return false;
                    }
                    break;
            }
        }

        switch (whichTest) {
            case "Phase 1":
                if (!getPeopleStringPhase1("1").equals(getPeopleStringPhase1("X"))) {
                    return false;
                }
                break;
            case "Phase 3":
                if (!getPeopleStringPhase3("1").equals(getPeopleStringPhase3("X"))) {
                    return false;
                }
                break;
            case "Phase 3 GRCh38":
                if (!getPeopleStringPhase3GRCh38("1").equals(getPeopleStringPhase3GRCh38("X"))) {
                    return false;
                }
                break;
        }
        return true;
    }

    public static LinkedList<EspInfoObj> exomeSequencingProject(ArrayList<InputRegion> sortedQueries) {
        LinkedList<EspInfoObj> espData = null;
        try {
            espData = new LinkedList<>();
            CodeSource codeSource = FerretMain.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();
            Process proc;
            for (int i = 0; i < sortedQueries.size(); i++) {
                int startQuery = sortedQueries.get(i).getStart();
                while (sortedQueries.get(i).getEnd() - startQuery >= 1000000) {
                    String temp = "java -jar evsClient0_15.jar -t " + sortedQueries.get(i).getChr() + ":" + startQuery + "-" + (startQuery + 999999) + " -f vcf";
                    proc = Runtime.getRuntime().exec(temp);
                    proc.waitFor();
                    String fileName = sortedQueries.get(i).getChr() + "-" + startQuery + "-" + (startQuery + 999999);
                    try (BufferedReader vcfRead = new BufferedReader(new FileReader("wsEVS_variant_download_chr" + fileName + ".vcf"))) {
                        String s;
                        while ((s = vcfRead.readLine()) != null) {
                            if (s.charAt(0) != '#') {
                                String[] stringSplit = s.split("\t");
                                String[] uselessSubString = stringSplit[7].split(";");
                                String[] EAString = uselessSubString[1].split(",");
                                String[] AAString = uselessSubString[2].split(",");
                                espData.add(new EspInfoObj(stringSplit[0], stringSplit[1], stringSplit[2], stringSplit[3], stringSplit[4], EAString[1], EAString[0].substring(6), AAString[1], AAString[0].substring(6)));
                            }
                        }
                    }
                    new File("wsEVS_variant_download_chr" + fileName + ".vcf").delete();
                    new File("wsEVS_Coverage_download_chr" + fileName + "_AllSites.txt").delete();
                    new File("wsEVS_Coverage_download_chr" + fileName + "_SummaryStats.txt").delete();
                    startQuery += 1000000;
                }
                String temp = "java -jar evsClient0_15.jar -t " + sortedQueries.get(i).getChr() + ":" + startQuery + "-" + sortedQueries.get(i).getEnd() + " -f vcf";
                proc = Runtime.getRuntime().exec(temp);
                proc.waitFor();
                String fileName = sortedQueries.get(i).getChr() + "-" + startQuery + "-" + sortedQueries.get(i).getEnd();
                try (BufferedReader vcfRead = new BufferedReader(new FileReader("wsEVS_variant_download_chr" + fileName + ".vcf"))) {
                    String s;
                    while ((s = vcfRead.readLine()) != null) {
                        if (s.charAt(0) != '#') {
                            String[] stringSplit = s.split("\t");
                            String[] uselessSubString = stringSplit[7].split(";");
                            String[] EAString = uselessSubString[1].split(",");
                            String[] AAString = uselessSubString[2].split(",");
                            espData.add(new EspInfoObj(stringSplit[0], stringSplit[1], stringSplit[2], stringSplit[3], stringSplit[4], EAString[1], EAString[0].substring(6), AAString[1], AAString[0].substring(6)));
                        }
                    }
                }
                new File("wsEVS_variant_download_chr" + fileName + ".vcf").delete();
                new File("wsEVS_Coverage_download_chr" + fileName + "_AllSites.txt").delete();
                new File("wsEVS_Coverage_download_chr" + fileName + "_SummaryStats.txt").delete();
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (URISyntaxException e) {
        } catch (Exception e) {

        }
        return espData;
    }

    private static Node getChildByName(Node parentNode, String childName) {
        Node toReturn = null;
        NodeList childrenNodeList = parentNode.getChildNodes();
        int listLength = childrenNodeList.getLength();
        for (int i = 0; i < listLength; i++) {
            if (childrenNodeList.item(i).getNodeName().equals(childName)) {
                toReturn = childrenNodeList.item(i);
                break;
            }
        }
        return toReturn;
    }

    private static Node xmlCommentFinder(NodeList test, String typeDesired, String headingDesired, String nodeNameToRetrieve) {
        /*
         * Each comment has a commentary type and commentary heading. Given a list of comment nodes, this will
         * search through the comments finding the one with matching type and matching heading. It will return the
         * specified node.
         */
        Node toReturn = null;
        int listLength = test.getLength();
        Node desiredNode = null;
        for (int i = 0; i < listLength; i++) {
            Node currentNode = test.item(i);
            if (xmlCommentChecker(currentNode, typeDesired, headingDesired)) {
                desiredNode = currentNode;
                break;
            }
        }

        if (desiredNode != null) {
            NodeList desiredNodeList = desiredNode.getChildNodes();
            listLength = desiredNodeList.getLength();
            for (int i = 0; i < listLength; i++) {
                if (desiredNodeList.item(i).getNodeName().equals(nodeNameToRetrieve)) {
                    toReturn = desiredNodeList.item(i);
                }
            }
        }

        return toReturn;
    }

    private static boolean xmlCommentChecker(Node test, String typeDesired, String headingDesired) {
        boolean typeMatch = false, headingMatch = false;
        if (typeDesired.equals("any")) {
            typeMatch = true;
        }
        if (headingDesired.equals("any")) {
            headingMatch = true;
        }
        NodeList commentTagList = test.getChildNodes();
        int commentLength = commentTagList.getLength();
        for (int j = 0; j < commentLength; j++) {
            String commentString = commentTagList.item(j).getNodeName();
            switch (commentString) {
                case "Gene-commentary_type":
                    NodeList geneCommentaryType = commentTagList.item(j).getChildNodes();
                    for (int k = 0; k < geneCommentaryType.getLength(); k++) {
                        if (geneCommentaryType.item(k).getNodeType() == Node.TEXT_NODE) {
                            typeMatch = geneCommentaryType.item(k).getNodeValue().equals(typeDesired);
                        }
                    }
                    break;
                case "Gene-commentary_heading":
                    NodeList geneCommentaryHeading = commentTagList.item(j).getChildNodes();
                    for (int k = 0; k < geneCommentaryHeading.getLength(); k++) {
                        if (geneCommentaryHeading.item(k).getNodeType() == Node.TEXT_NODE) {
                            headingMatch = geneCommentaryHeading.item(k).getNodeValue().equals(headingDesired);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return typeMatch && headingMatch;
    }

    public static FoundGeneAndRegion getQueryFromGeneID(String[] geneListArray, boolean defaultHG) {

        if (geneListArray.length == 0) {
            return null;
        }

        StringBuffer geneList = new StringBuffer();
        for (int i = 0; i < geneListArray.length - 1; i++) {
            geneList.append(geneListArray[i]).append(",");
        }
        geneList.append(geneListArray[geneListArray.length - 1]);

        DocumentBuilder docBldr;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setCoalescing(false);
        StringBuffer foundGenes = new StringBuffer();
        ArrayList<InputRegion> queriesArrayList = new ArrayList<>();
        try {
            docBldr = dbf.newDocumentBuilder();
            // see if there are more than 500 in the list
            String ncbiEutilsFetchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id=" + geneList + "&retmode=xml";
            org.w3c.dom.Document doc = docBldr.parse(ncbiEutilsFetchURL);
            // Do the next steps in a loop for each gene id
            NodeList entrezgeneNodeList = doc.getElementsByTagName("Entrezgene");
            int listLength = entrezgeneNodeList.getLength();
            for (int i = 0; i < listLength; i++) {
                Node currentEntrezNode = entrezgeneNodeList.item(i);
                Node trackNode = getChildByName(currentEntrezNode, "Entrezgene_track-info");
                Node geneTrackNode = null;
                String currentGene;
                if (trackNode != null && getChildByName(trackNode, "Gene-track") != null && getChildByName(geneTrackNode, "Gene-track_geneid") != null) {
                    geneTrackNode = getChildByName(trackNode, "Gene-track");
                    currentGene = getChildByName(geneTrackNode, "Gene-track_geneid").getFirstChild().getNodeValue();
                    Node subSourceNameNode = getChildByName(getChildByName(getChildByName(getChildByName(getChildByName(currentEntrezNode, "Entrezgene_source"),
                            "BioSource"), "BioSource_subtype"), "SubSource"), "SubSource_name");
                    String chromosome = subSourceNameNode.getFirstChild().getNodeValue();
                    NodeList commentList = getChildByName(currentEntrezNode, "Entrezgene_comments").getChildNodes();
                    Node geneLocationHistoryNode = xmlCommentFinder(commentList, "254", "Gene Location History", "Gene-commentary_comment");
                    Node primaryAssemblyNode;

                    if (defaultHG) {
                        Node annotationRelease105Node = xmlCommentFinder(geneLocationHistoryNode.getChildNodes(), "254", "Homo sapiens Annotation Release 105", "Gene-commentary_comment");
                        if (annotationRelease105Node == null) {
                            continue;
                        }
                        Node grch37p13Node = xmlCommentFinder(annotationRelease105Node.getChildNodes(), "24", "GRCh37.p13", "Gene-commentary_comment");

                        if (grch37p13Node == null) {
                            primaryAssemblyNode = null;

                        } else {
                            primaryAssemblyNode = xmlCommentFinder(grch37p13Node.getChildNodes(), "25", "Primary Assembly", "Gene-commentary_comment");
                        }

                    } else {
                        Node annotationRelease107Node = xmlCommentFinder(geneLocationHistoryNode.getChildNodes(), "254", "Homo sapiens Annotation Release 107", "Gene-commentary_comment");
                        if (annotationRelease107Node == null) {
                            continue;
                        }
                        Node grch38p2Node = xmlCommentFinder(annotationRelease107Node.getChildNodes(), "24", "GRCh38.p2", "Gene-commentary_comment");
                        primaryAssemblyNode = xmlCommentFinder(grch38p2Node.getChildNodes(), "25", "Primary Assembly", "Gene-commentary_comment");
                    }
                    Node genomicAssemblyNode = xmlCommentFinder(primaryAssemblyNode.getChildNodes(), "1", "any", "Gene-commentary_seqs");
                    Node seqLocNode = getChildByName(genomicAssemblyNode, "Seq-loc");
                    Node seqLocIntNode = getChildByName(seqLocNode, "Seq-loc_int");
                    Node seqIntervalNode = getChildByName(seqLocIntNode, "Seq-interval");
                    NodeList sequenceLocationNodeList = seqIntervalNode.getChildNodes();
                    int listLocationLength = sequenceLocationNodeList.getLength();
                    String startPos = new String();
                    String endPos = new String();
                    for (int j = 0; j < listLocationLength; j++) {
                        Node currentNode = sequenceLocationNodeList.item(j);
                        if (currentNode.getNodeName().equals("Seq-interval_from")) {
                            startPos = currentNode.getFirstChild().getNodeValue();
                        }
                        if (currentNode.getNodeName().equals("Seq-interval_to")) {
                            endPos = currentNode.getFirstChild().getNodeValue();
                        }
                    }
                    if (!chromosome.equals("X") && !chromosome.equals("Y") && !chromosome.equals("MT")) {
                        queriesArrayList.add(new InputRegion(chromosome, Integer.parseInt(startPos), Integer.parseInt(endPos)));
                        foundGenes.append(currentGene).append(",");
                    }
                }

            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
        InputRegion[] queriesFound = queriesArrayList.toArray(new InputRegion[queriesArrayList.size()]);

        foundGenes.deleteCharAt(foundGenes.length() - 1);
        return new FoundGeneAndRegion(foundGenes.toString(), queriesFound, queriesFound.length == geneListArray.length);
    }

    public static FoundGeneAndRegion getQueryFromGeneName(String[] geneListArray, boolean defaultHG) {
        if (geneListArray.length == 0) {
            return null;
        }
        StringBuffer geneList = new StringBuffer();
        for (int i = 0; i < geneListArray.length - 1; i++) {
            geneList.append(geneListArray[i]).append("[GENE]+OR+");
        }
        geneList.append(geneListArray[geneListArray.length - 1]).append("[GENE]");

        ArrayList<InputRegion> queryArrayList = new ArrayList<>();
        String geneString;
        StringBuffer foundGenes = new StringBuffer();
        DocumentBuilder docBldr;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setCoalescing(false);
        try {
            docBldr = dbf.newDocumentBuilder();

            int listLength = 0;
            String ncbiEutilsSearchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gene&term=" + geneList + "+AND+Homo+sapiens[ORGN]";
            org.w3c.dom.Document doc = docBldr.parse(ncbiEutilsSearchURL);
            Node resultsNode = doc.getElementsByTagName("eSearchResult").item(0);
            Node idListNode = getChildByName(resultsNode, "IdList");
            NodeList idsNodeList = null;
            if (idListNode != null && idListNode.getChildNodes() != null) {
                idsNodeList = idListNode.getChildNodes();
                listLength = idsNodeList.getLength();
            }
            if (listLength == 0) { // nothing found so return null; might have to do something more advanced here later
                return null;
            }
            StringBuffer geneListIDBuffer = new StringBuffer();
            for (int i = 0; i < listLength; i++) {
                geneListIDBuffer.append(idsNodeList.item(i).getFirstChild().getNodeValue());
                geneListIDBuffer.append(',');
            }
            geneListIDBuffer.trimToSize();
            geneListIDBuffer = geneListIDBuffer.deleteCharAt(geneListIDBuffer.length() - 1);
            geneString = geneListIDBuffer.toString();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
// retrieve gene information for geneString input
        try {
            // see if there are more than 500 in the list
            String ncbiEutilsFetchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id=" + geneString + "&retmode=xml";
            docBldr = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = docBldr.parse(ncbiEutilsFetchURL);
            // Do the next steps in a loop for each gene id
            NodeList entrezgeneNodeList = doc.getElementsByTagName("Entrezgene");
            int listLength = entrezgeneNodeList.getLength();
            for (int i = 0; i < listLength; i++) {
                Node currentEntrezNode = entrezgeneNodeList.item(i);
                Node trackNode = getChildByName(currentEntrezNode, "Entrezgene_track-info");
                Node geneTrackNode = getChildByName(trackNode, "Gene-track");
                Node geneStatusNode = getChildByName(geneTrackNode, "Gene-track_status");
                NamedNodeMap attributes = null;
                if (geneStatusNode != null) {
                    attributes = geneStatusNode.getAttributes();
                }
                if (attributes != null && attributes.getLength() != 1) {
                    continue;
                }
                if (attributes != null && !attributes.item(0).getNodeValue().equals("live")) {
                    continue;
                }
                Node geneNode = getChildByName(currentEntrezNode, "Entrezgene_gene");
                Node geneRefNode = getChildByName(geneNode, "Gene-ref");
                Node geneRefLocusNode = getChildByName(geneRefNode, "Gene-ref_locus");
                String geneNameFound = null;
                if (geneRefLocusNode != null) {
                    geneNameFound = geneRefLocusNode.getFirstChild().getNodeValue();
                }
                if (geneNameFound != null && !Arrays.asList(geneListArray).contains(geneNameFound.toUpperCase())) {
                    continue;
                }

                // This is a horrible line. Mainly, this is horrible because I don't think nesting makes sense here but if not, would
                // result in a lot of confusing variable names for nodes
                Node subSourceNodeForChr = getChildByName(getChildByName(getChildByName(getChildByName(getChildByName(currentEntrezNode, "Entrezgene_source"),
                        "BioSource"), "BioSource_subtype"), "SubSource"), "SubSource_name");
                String chromosome = null;
                if (subSourceNodeForChr != null) {
                    chromosome = subSourceNodeForChr.getFirstChild().getNodeValue();
                }
                NodeList commentList = null;
                Node geneLocationHistoryNode = null;

                if (getChildByName(currentEntrezNode, "Entrezgene_comments") != null) {
                    commentList = getChildByName(currentEntrezNode, "Entrezgene_comments").getChildNodes();
                    geneLocationHistoryNode = xmlCommentFinder(commentList, "254", "Gene Location History", "Gene-commentary_comment");
                }
                Node primaryAssemblyNode;
                if (defaultHG && geneLocationHistoryNode != null) {
                    Node annotationRelease105Node = xmlCommentFinder(geneLocationHistoryNode.getChildNodes(), "254", "Homo sapiens Annotation Release 105", "Gene-commentary_comment");
                    if (annotationRelease105Node == null) {
                        continue;
                    }
                    Node grch37p13Node = xmlCommentFinder(annotationRelease105Node.getChildNodes(), "24", "GRCh37.p13", "Gene-commentary_comment");
                    primaryAssemblyNode = xmlCommentFinder(grch37p13Node.getChildNodes(), "25", "Primary Assembly", "Gene-commentary_comment");
                } else {
                    Node annotationRelease107Node = null;
                    if ((geneLocationHistoryNode) != null) {
                        annotationRelease107Node = xmlCommentFinder(geneLocationHistoryNode.getChildNodes(), "254", "Homo sapiens Annotation Release 107", "Gene-commentary_comment");
                    }
                    if (annotationRelease107Node == null) {
                        continue;
                    }
                    Node grch38p2Node = xmlCommentFinder(annotationRelease107Node.getChildNodes(), "24", "GRCh38.p2", "Gene-commentary_comment");
                    if (grch38p2Node != null) {
                        primaryAssemblyNode = xmlCommentFinder(grch38p2Node.getChildNodes(), "25", "Primary Assembly", "Gene-commentary_comment");
                    } else {
                        primaryAssemblyNode = null;
                    }
                }
                if (primaryAssemblyNode != null) {
                    Node genomicAssemblyNode = xmlCommentFinder(primaryAssemblyNode.getChildNodes(), "1", "any", "Gene-commentary_seqs");
                    Node seqLocNode = getChildByName(genomicAssemblyNode, "Seq-loc");
                    Node seqLocIntNode = getChildByName(seqLocNode, "Seq-loc_int");
                    Node seqIntervalNode = getChildByName(seqLocIntNode, "Seq-interval");
                    NodeList sequenceLocationNodeList = null;

                    if (seqIntervalNode != null) {
                        sequenceLocationNodeList = seqIntervalNode.getChildNodes();
                        int listLocationLength = sequenceLocationNodeList.getLength();
                        String startPos = new String(), endPos = new String();
                        for (int j = 0; j < listLocationLength; j++) {
                            Node currentNode = sequenceLocationNodeList.item(j);
                            if (currentNode.getNodeName().equals("Seq-interval_from")) {
                                startPos = currentNode.getFirstChild().getNodeValue();
                            }
                            if (currentNode.getNodeName().equals("Seq-interval_to")) {
                                endPos = currentNode.getFirstChild().getNodeValue();
                            }
                        }
                        if (chromosome != null && !chromosome.equals("X") && !chromosome.equals("Y") && !chromosome.equals("MT")) {
                            queryArrayList.add(new InputRegion(chromosome, Integer.parseInt(startPos), Integer.parseInt(endPos)));
                            foundGenes.append(geneNameFound).append(",");
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
        InputRegion[] queriesFound = queryArrayList.toArray(new InputRegion[queryArrayList.size()]);

        foundGenes.deleteCharAt(foundGenes.length() - 1);
        return new FoundGeneAndRegion(foundGenes.toString(), queriesFound, queryArrayList.size() == geneListArray.length);
    }


//   public void regex(String varid) {
//	   Pattern p = Pattern.compile("[0-9]+");
//	   Matcher m = p.matcher(varid);
//		while(m.find())
//		{
//			System.out.println("mgroup" + m.group());  // Tout le motif
//			System.out.println("mgroup1" + m.group(1)); // Le contenu entre <b> et </b>
//		}
//}
   
}
