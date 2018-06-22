package com.ecn.ferretmvc.model;

import com.ecn.ferretmvc.main.FerretMain;
import com.ecn.ferretmvc.view.GUI;

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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FerretData extends SwingWorker<Integer, String> {


    //private GUI gui = new GUI();
    //private DownloadTheDataModel model = new DownloadTheDataModel();
	
    InputRegion[] queries;
    ArrayList<CharSequence> populations;
    String fileName;
    boolean retrieveESP;
    JLabel status;
    String ftpAddress;
    double MAF;
    double MAFMax;
    String outputFiles;
    double espMAF;
    private boolean usehaplo;

    String queryType = null; // for both gene and SNP queries
    Boolean defaultHG = null; // for both gene and SNP queries
    ArrayList<String> snpQueries = null; // only for SNP queries
    Boolean snpWindowSelected = null; // only for SNP
    Boolean geneWindowSelected = null;
    Integer windowSize = null; // only for SNP
    String[] geneQueries = null; // only for gene

    // constructor for the locus research
    public FerretData(InputRegion[] queries, ArrayList<CharSequence> populations, String fileName,
            boolean retrieveESP, JLabel status, String ftpAddress, double MAF, double MAFMax, Boolean ESPMAF, String outputFiles) {
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
        this.usehaplo = false;
    }

    //Constructor for the SNP (variants) research
    public FerretData(String queryType, ArrayList<String> snpQueries, ArrayList<CharSequence> populations, String fileName,
            boolean retrieveESP, JLabel status, String ftpAddress, double MAF, double MAFMax, Boolean ESPMAF, String outputFiles, boolean defaultHG,
            boolean snpWindowSelected, Integer windowSize) {
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
        this.queryType = queryType;
        this.snpQueries = snpQueries;
        this.defaultHG = defaultHG;
        this.snpWindowSelected = snpWindowSelected;
        this.windowSize = windowSize;
        this.usehaplo = false;
    }

    //Constructor for the gene research
    public FerretData(String queryType, String[] geneQueries, ArrayList<CharSequence> populations, String fileName,
            boolean retrieveESP, JLabel status, String ftpAddress, double MAF, double MAFMax, Boolean ESPMAF, String outputFiles, boolean defaultHG, boolean geneWindowSelected, Integer windowSize, int o) {
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
        this.queryType = queryType;
        this.geneQueries = geneQueries;
        this.defaultHG = defaultHG;
        this.geneWindowSelected = geneWindowSelected;
        this.windowSize = windowSize;
        this.usehaplo = false;
    }

    //setter that put the boolean usehaplo as true, use in doInBackGround to open or not Haploview at the end
    public void setHaplo(boolean b) {
        this.usehaplo = b;
    }

    @Override
    public Integer doInBackground() {

        // SNP query here
        if (queryType != null && "SNP".equals(queryType)) {
            publish("Looking up variant locations...");
            LinkedList<String> chromosome = new LinkedList<>();
            LinkedList<String> startPos = new LinkedList<>();
            LinkedList<String> endPos = new LinkedList<>();
            InputRegion[] queries = null;
            ArrayList<String> SNPsFound = new ArrayList<>();
            boolean allSNPsFound = true;
            BufferedReader br = null;
            try {
                for (int i = 0; i < snpQueries.size(); i++) {
                	System.out.println("snp queries" + snpQueries);
                    URL urlLocation = new URL("https://www.ncbi.nlm.nih.gov/projects/SNP/snp_gene.cgi?connect=&rs=" + snpQueries.get(i));
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
                    } else {
                        SNPsFound.add(snpQueries.get(i));
                    }
                    br.close();
                }
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
            } catch (FileNotFoundException e) {
                // Either will occur if ncbi is down or if something is wrong with the input
                JOptionPane.showMessageDialog(null, "Ferret was unable to retrieve any variants", "Error", JOptionPane.OK_OPTION);
                return -2;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ferret was unable to retrieve any variants", "Error", JOptionPane.OK_OPTION);
                return -2;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ferret was unable to retrieve any variants", "Error", JOptionPane.OK_OPTION);
                return -2;
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
                            if ((variantFreq[i] / (float) chromosomeCount) < MAF || (variantFreq[i] / (float) chromosomeCount) < MAFMax) {
                                tempBoolean = false; //if fail MAF Threshold, continue to next line
                            }
                        }
                        if (tempBoolean) {
                            vcfWrite.write(tempString.toString());
                            vcfWrite.newLine();
                        }

                    }

                }
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
                File vcfFile = new File(fileName + "_genotypes.vcf");
                vcfFile.createNewFile();
                try (BufferedWriter vcfBuffWrite = new BufferedWriter(new FileWriter(vcfFile))) {
                    for (int j = 0; j < queryNumber; j++) {
                        webAddress = ftpAddress.replace("$", sortedQueries.get(j).getChr());

                        //webAddress = "ftp://ftp-trace.ncbi.nih.gov/1000genomes/ftp/release/20130502/ALL.chr" + sortedQueries.get(j).getChr() + ".phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz";
                        //webAddress = "ftp://ftp-trace.ncbi.nih.gov/1000genomes/ftp/release/20130502/supporting/GRCh38_positions/ALL.chr" + sortedQueries.get(j).getChr() + ".phase3_shapeit2_mvncall_integrated_v3plus_nounphased.rsID.genotypes.GRCh38_dbSNP_no_SVs.vcf.gz";
                        TabixReader tr = new TabixReader(webAddress);

                        // Get the iterator
                        startTime = System.nanoTime();
                        TabixReader.Iterator iter = tr.query(sortedQueries.get(j).getChr() + ":" + sortedQueries.get(j).getStart() + "-" + sortedQueries.get(j).getEnd());
                        long endTime = System.nanoTime();
                        System.out.println("Tabix iterator time: " + (endTime - startTime));
                        while ((s = iter.next()) != null) {
                            variantCounter++;
                            String[] stringSplit = s.split("\t");
                            if (stringSplit[6].equals("PASS")) {
                                if (isInteger(stringSplit[1])) {
                                    tempInt = Integer.parseInt(stringSplit[1]) - sortedQueries.get(j).getStart();
                                    if (tempInt > 0) {
                                        setProgress((int) ((tempInt + querySize[j]) / (double) querySize[queryNumber] * 99));
                                    }
                                }
                                if (stringSplit[2].equals(".")) {
                                    stringSplit[2] = "chr" + sortedQueries.get(j).getChr() + "_" + stringSplit[1];
                                    for (int i = 0; i < stringSplit.length; i++) {
                                        vcfBuffWrite.write(stringSplit[i] + "\t");
                                    }
                                } else {
                                    vcfBuffWrite.write(s);
                                }
                                vcfBuffWrite.newLine();
                            } else {
                                vcfBuffWrite.write(s);
                                vcfBuffWrite.newLine();
                            }
                        }
                        long endEndTime = System.nanoTime();
                        System.out.println("Iteration time: " + (endEndTime - endTime));
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException " + tempInt);
                return -1;
            } catch (NullPointerException e) {
                System.out.println("Null Pointer Exception " + tempInt);
                return -1;
                // Tabix iterator doesn't have has.next method, so this protects from regions without variants
            } catch (RuntimeException e) {
                System.out.println("Runtime Exception" + tempInt);
                System.out.println("Iteration time: " + (System.nanoTime() - startTime));
                return -1;
            }
            // end VCF writing

            LinkedList<EspInfoObj> espData = null;
            if (retrieveESP) {
                publish("Downloading Data from Exome Sequencing Project...");
                espData = FerretData.exomeSequencingProject(sortedQueries);
            }
            if (variantCounter == 0 && (espData == null || espData.size() == 0)) {
                File vcfFile = new File(fileName + "_genotypes.vcf");
                vcfFile.delete();
                return 0;
            }

            publish("Outputting files...");
            try {
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

                File freqFile = new File(fileName + "AlleleFreq.frq");
                freqFile.createNewFile();
                frqWrite = new BufferedWriter(new FileWriter(freqFile));
                if (retrieveESP) {
                    frqWrite.write("CHROM\tVARIANT\tPOS\tALLELE1\tALLELE2\tNB_1KG_CHR\t1KG_A1_FREQ\tESP6500_EA_A1_FREQ\tESP6500_AA_A1_FREQ");
                    frqWrite.newLine();
                } else {
                    frqWrite.write("CHROM\tVARIANT\tPOS\tALLELE1\tALLELE2\tNB_CHR\t1KG_A1_FREQ\t1KG_A2_FREQ\tGENENAME\tGENEID\tFUNCTION\tPROTEINPOS\tAACHANGE\tPROTEINACC\tOTHERVARIANTS");
                    frqWrite.newLine();
                }

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
                int espErrorCount = 0;
              
                String[] Varid = null;
                String[] geneSymbol = null;
                String[] geneId = null;
                String[] proteinPos = null;
                String[] proteinAcc = null;
                String[] fxnName = null;
                String [] mrnaAcc = null;
                String[] aaCode = null;
                ArrayList<String>test = new ArrayList<>();
                String tst [] = null;
                String[] None = {"None"};
                
                while ((s = vcfRead.readLine()) != null) {
                    String[] text = s.split("\t");
                  
/* text[2] contains all the rsid for a gene, a locus or variants inputted by the user*/
                    System.out.print("\t text" + text[2]);
                   //text[2].substring(2);
                    
                  Varid = text[2].split(",");
                    BufferedReader br = null;
                   try {
                        for (int i = 0; i < Varid.length; i++) {
                        	System.out.println("\tCe qu'il y'a dans varid\t" + Varid[i].substring(2));
                            URL urlLocation = new URL("https://www.ncbi.nlm.nih.gov/projects/SNP/snp_gene.cgi?connect=&rs=" + Varid[i].substring(2));
                            br = new BufferedReader(new InputStreamReader(urlLocation.openStream()));
                            String currentString;
                                //while ((currentString = br.readLine()) != null && !currentString.contains("\"GRCh37.p13\" : [")) {
                                	 
                                //&& currentString.contains("\"GRCh38.p7\" : [")}
                             
                                while ((currentString = br.readLine()) != null  && !currentString.contains("\"mrnaAcc\" : ")) 
                                {
                                    if (currentString.contains("\"geneSymbol\"")) {
                                    	geneSymbol = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")).split("\t");
                                    	System.out.print("\tgeneSymbol" + geneSymbol[0]);
                                    	 if (geneSymbol[0].equals("")) {
                                         	geneSymbol = None;
                                         }
                                    	
                                       // startPos.add(currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")));
                                }
                                   
                                    
                                    if (currentString.contains("\"geneId\"")) {
                                    	geneId = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")).split("\t");
                                    	System.out.print("\tgeneId" + geneId[0]);
                                    	if (geneId[0].equals("")) {
                                         	geneId = None;
                                         }
                                    }
                                   
                                    
                                    
                                    if (currentString.contains("\"proteinPos\"")) {
                                    	proteinPos = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")).split("\t");
                                    	System.out.print("\tproteinPos" + proteinPos[0]);
                                    	if (proteinPos[0].equals("")) {
                                    		System.out.print("Je rentree dans if protpos");
                                         	proteinPos = None;
                                         }
                                    	
                                    }
                                    
                                   
                                    
                                    if (currentString.contains("\"proteinAcc\"")) {
                                    	proteinAcc = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")).split("\t");
                                    	System.out.print("\tproteinAcc" + proteinAcc[0]);
                                    	if (proteinAcc[0].equals("")) {
                                    		System.out.print("Je rentree dans if protacc");
                                        	proteinAcc = None;
                                        }
                                    }
                                    
                                
                                    if (currentString.contains("\"aaCode\"")) {
                                    	aaCode = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")).split("\t");
                                    	System.out.print("\t aacode" + aaCode[0]);
                                    	
                                    	//System.out.print("\taaCode" + aaCode.get(0) + "\t" aaCode.get(0));
                                    	
                                    	//System.out.print("\taaCode" + aaCode.get(i));
                                    	if (aaCode[0].equals("")) {
                                    		System.out.print("Je rentree dans if protacc");
                                         	aaCode = None;
                                         }
                                    	//test.add(currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")));
                                    	//System.out.print("\taaCode" + test);
                                    }
                                   
                                    
                                    
                                    if (currentString.contains("\"fxnName\"")) {
                                    	fxnName = currentString.substring(currentString.indexOf(" : \"") + 4, currentString.indexOf("\",")).split("\t");
                                    	System.out.print("\tfxnName" + fxnName[0]);
                                    	
                                    	//System.out.print("\t" + fxnName.get(i));
                                    	if (fxnName[0].equals("")) {
                                         	fxnName = None;
                                         }
                                    	break;
                                    }
                                //System.out.println(currentString);
                               }
                            
                        }
                   }  catch (Exception e2) {
              	     e2.printStackTrace();
                    }
                    
                    
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
                            if (!text[4].contains("CN") && (freqZero >= MAF && freqOne >= MAF)) {
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
                                frqWrite.write(temp.getChr() + "\t" + snpName + "\t" + temp.getPos() + "\t"
                                        + temp.getRefAllele() + "\t" + temp.getAltAllele() + "\t" + "." + "\t" + "." + "\t"
                                        + df.format(temp.getEAFreq()) + "\t" + df.format(temp.getAAFreq()));
                                frqWrite.newLine();
                            }
                        }
                        // The following line says: if espData is not empty AND esp chr equals 1KG chr AND esp pos equals 1KG pos AND it's biallelic 
                        if (!espData.isEmpty() && espData.peek().getChrAsInt() == Integer.parseInt(text[0]) && espData.peek().getPos() == Integer.parseInt(text[1]) && variantPossibilities.length == 2) {
                            EspInfoObj temp = espData.remove();///
                            // 1KG and ESP Ref alleles and Alt alleles match
                            if (variantPossibilities[0].equals(temp.getRefAllele()) && variantPossibilities[1].equals(temp.getAltAllele())) {
                                if ((freqZero >= MAF && freqOne >= MAF && freqOne <= MAFMax) || (temp.getEAFreq() >= espMAF && temp.getEAFreq() <= (1 - espMAF)) || (temp.getAAFreq() >= espMAF && temp.getAAFreq() <= (1 - espMAF))) {
                                    frqFileEmpty = false;
                                    frqWrite.write(text[0] + "\t" + text[2] + "\t" + text[1] + "\t"
                                            + variantPossibilities[0] + "\t" + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t"
                                            + df.format(temp.getEAFreq()) + "\t" + df.format(temp.getAAFreq()));
                                    frqWrite.newLine();
                                }
                                // 1KG and ESP ref/alt alleles are switched
                            } else if (variantPossibilities[0].equals(temp.getAltAllele()) && variantPossibilities[1].equals(temp.getRefAllele())) {
                                if ((freqOne >= MAF && freqZero >= MAF && freqOne <= MAFMax) || (temp.getEAFreq() >= espMAF && temp.getEAFreq() <= (1 - espMAF)) || (temp.getAAFreq() >= espMAF && temp.getAAFreq() <= (1 - espMAF))) {
                                    frqFileEmpty = false;
                                    frqWrite.write(text[0] + "\t" + text[2] + "\t" + text[1] + "\t"
                                            + variantPossibilities[0] + "\t" + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t"
                                            + df.format(temp.getEAFreqAlt()) + "\t" + df.format(temp.getAAFreqAlt()));
                                    frqWrite.newLine();
                                }
                                // ESP does not match with 1KG so ESP omitted
                            } else {
                                if (freqOne >= MAF && freqZero >= MAF && freqOne <= MAFMax) {
                                    frqFileEmpty = false;
                                    frqWrite.write(text[0] + "\t" + text[2] + "\t" + text[1] + "\t"
                                            + variantPossibilities[0] + "\t" + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t"
                                            + "." + "\t" + ".");
                                    frqWrite.newLine();
                                }
                                espErrorCount++;
                            }
                        } else if (variantPossibilities.length == 2) {
                            if (freqOne >= MAF && freqZero >= MAF && freqOne <= MAFMax) {
                                frqFileEmpty = false;
                                frqWrite.write(text[0] + "\t" + text[2] + "\t" + text[1] + "\t"
                                        + variantPossibilities[0] + "\t" + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t"
                                        + "." + "\t" + ".");
                                frqWrite.newLine();
                            }
                        }
                    } else {
                        if (variantPossibilities.length == 2 && freqOne >= MAF && freqZero >= MAF && freqOne <= MAFMax) {
                            frqFileEmpty = false;
                            frqWrite.write(text[0] + "\t" + text[2] + "\t" + text[1] + "\t" + variantPossibilities[0] + "\t"
                                    + variantPossibilities[1] + "\t" + numChr + "\t" + df.format(freqZero) + "\t" + df.format(freqOne) + "\t" + geneSymbol[0] + "\t" + geneId[0]+ "\t" + fxnName[0] + "\t" + proteinPos[0] + "\t" + aaCode[0]  + "\t" + proteinAcc[0] + "\t" + geneSymbol[0]);
                            frqWrite.newLine();
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
                    freqFile.delete();
                    return -3;
                }
            } catch (IOException e) {
                return -1;
            }
            
            
            //model.mergeFiles(gui);
            setProgress(100);
            System.out.println("Finished");
        }
        if (usehaplo) {
           
            try {
            	 //File file = new File("/home/rokhaya/Menu.html");
            	Process  proc = Runtime.getRuntime().exec("java -jar \"/FerretMVC/Haploview.jar\"");
            	 //Process  proc = Runtime.getRuntime().exec("java -jar \"Haploview.jar\" -pedfile \"" + fileName + "_m.ped\" -info \"" + fileName + "_m.info\"");
            	//Desktop.getDesktop().open(file);
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

    protected static boolean isInteger(String str) {
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
        // helper method for the below method
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
    
   public void regex(String varid) {
	   Pattern p = Pattern.compile("[0-9]+"); 
	   Matcher m = p.matcher(varid);
		while(m.find())
		{
			System.out.println("mgroup" + m.group());  // Tout le motif
			System.out.println("mgroup1" + m.group(1)); // Le contenu entre <b> et </b>
		}
}
    
}
