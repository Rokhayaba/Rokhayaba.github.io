package com.ecn.ferretmvc.model;

import com.ecn.ferretmvc.main.FerretMain;
import com.ecn.ferretmvc.view.GUI;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Observable;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author Eric
 */
public class DownloadTheDataModel extends Observable {

    public void performSearchAndDownload(GUI gui, boolean boolHaplo) {
        //Add selected populations
        final long startTime = System.nanoTime();
        ArrayList<CharSequence> populations = new ArrayList<>();
        for (int i = 0; i < gui.getAfrsub().length; i++) {
            if (gui.getAfrsub()[i].isSelected()) {
                populations.add(gui.getAfrCode()[i]);
            }
        }
        for (int i = 0; i < gui.getEursub().length; i++) {
            if (gui.getEursub()[i].isSelected()) {
                populations.add(gui.getEurCode()[i]);
            }
        }
        for (int i = 0; i < gui.getSansub().length; i++) {
            if (gui.getSansub()[i].isSelected()) {
                populations.add(gui.getSanCode()[i]);
            }
        }
        for (int i = 0; i < gui.getAsnsub().length; i++) {
            if (gui.getAsnsub()[i].isSelected()) {
                populations.add(gui.getAsnCode()[i]);
            }
        }
        for (int i = 0; i < gui.getAmrsub().length; i++) {
            if (gui.getAmrsub()[i].isSelected()) {
                populations.add(gui.getAmrCode()[i]);
            }
        }
        if (gui.getAllracessub()[0].isSelected()) {
            populations.add("ALL");
        }
        boolean popSelected = !populations.isEmpty();
        boolean fileLocSelected = (gui.getFileNameAndPath() != null);
        String linkFile = "C:\\Program Files\\Java\\";
        String runJar = "jar -xf \"";
        String linkJar = "\\Ferret_v2.1.1.jar\" evsClient0_15.jar";
        String evsClient = "evsClient0_15.jar";

        //LOCUS RUN
        switch (gui.getInputSelect().getSelectedIndex()) {
            case 0: {
                // Chr position input method
                boolean getESP = gui.getChrESPCheckBox().isSelected();
                String chrSelected = (String) gui.getChrList().getSelectedItem();
                String startPosition = gui.getStartPosTextField().getText();
                String endPosition = gui.getEndPosTextField().getText();
                boolean isChrSelected;
                boolean startEndValid = true;
                boolean startSelected;
                boolean endSelected;
                boolean withinRange = true;
                int chrEndBound = 0;
                isChrSelected = !chrSelected.equals(" ");
                // Checks to see if number and integer
                if ((startSelected = !startPosition.isEmpty())) {
                    for (int i = 0; i < startPosition.length(); i++) {
                        if (!Character.isDigit(startPosition.charAt(i))) {
                            startSelected = false;
                        }
                    }
                }
                if ((endSelected = !endPosition.isEmpty())) {
                    for (int i = 0; i < endPosition.length(); i++) {
                        if (!Character.isDigit(endPosition.charAt(i))) {
                            endSelected = false;
                        }
                    }
                }
                if (startSelected && endSelected) {
                    int tempEndPos;
                    int tempStartPos;
                    try {
                        if (startSelected && endSelected) {
                            startEndValid = Integer.parseInt(endPosition) >= Integer.parseInt(startPosition);
                        }
                        tempEndPos = Integer.parseInt(endPosition);
                        tempStartPos = Integer.parseInt(startPosition);
                    } catch (NumberFormatException ex) {
                        tempEndPos = 250000000;
                        tempStartPos = 0;
                    }
                    if (gui.getDefaultHG()[0]) {
                        switch (chrSelected) {
                            case "X":
                                if (tempEndPos > 155270560 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 155270560;
                                }
                                break;
                            case "1":
                                if (tempEndPos > 249250621 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 249250621;
                                }
                                break;
                            case "2":
                                if (tempEndPos > 243199373 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 243199373;
                                }
                                break;
                            case "3":
                                if (tempEndPos > 198022430 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 198022430;
                                }
                                break;
                            case "4":
                                if (tempEndPos > 191154276 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 191154276;
                                }
                                break;
                            case "5":
                                if (tempEndPos > 180915260 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 180915260;
                                }
                                break;
                            case "6":
                                if (tempEndPos > 171115067 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 171115067;
                                }
                                break;
                            case "7":
                                if (tempEndPos > 159138663 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 159138663;
                                }
                                break;
                            case "8":
                                if (tempEndPos > 146364022 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 146364022;
                                }
                                break;
                            case "9":
                                if (tempEndPos > 141213431 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 141213431;
                                }
                                break;
                            case "10":
                                if (tempEndPos > 135534747 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 135534747;
                                }
                                break;
                            case "11":
                                if (tempEndPos > 135006516 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 135006516;
                                }
                                break;
                            case "12":
                                if (tempEndPos > 133851895 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 133851895;
                                }
                                break;
                            case "13":
                                if (tempEndPos > 115169878 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 115169878;
                                }
                                break;
                            case "14":
                                if (tempEndPos > 107349540 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 107349540;
                                }
                                break;
                            case "15":
                                if (tempEndPos > 102531392 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 102531392;
                                }
                                break;
                            case "16":
                                if (tempEndPos > 90354753 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 90354753;
                                }
                                break;
                            case "17":
                                if (tempEndPos > 81195210 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 81195210;
                                }
                                break;
                            case "18":
                                if (tempEndPos > 78077248 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 78077248;
                                }
                                break;
                            case "19":
                                if (tempEndPos > 59128983 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 59128983;
                                }
                                break;
                            case "20":
                                if (tempEndPos > 63025520 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 63025520;
                                }
                                break;
                            case "21":
                                if (tempEndPos > 48129895 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 48129895;
                                }
                                break;
                            case "22":
                                if (tempEndPos > 51304566 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 51304566;
                                }
                                break;
                            default:
                                break;
                        }
                    } else {
                        switch (chrSelected) {
                            case "X":
                                if (tempEndPos > 156040895 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 156040895;
                                }
                                break;
                            case "1":
                                if (tempEndPos > 248956422 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 248956422;
                                }
                                break;
                            case "2":
                                if (tempEndPos > 242193529 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 242193529;
                                }
                                break;
                            case "3":
                                if (tempEndPos > 198295559 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 198295559;
                                }
                                break;
                            case "4":
                                if (tempEndPos > 190214555 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 190214555;
                                }
                                break;
                            case "5":
                                if (tempEndPos > 181538259 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 181538259;
                                }
                                break;
                            case "6":
                                if (tempEndPos > 170805979 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 170805979;
                                }
                                break;
                            case "7":
                                if (tempEndPos > 159345973 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 159345973;
                                }
                                break;
                            case "8":
                                if (tempEndPos > 145138636 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 145138636;
                                }
                                break;
                            case "9":
                                if (tempEndPos > 138394717 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 138394717;
                                }
                                break;
                            case "10":
                                if (tempEndPos > 133797422 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 133797422;
                                }
                                break;
                            case "11":
                                if (tempEndPos > 135086622 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 135086622;
                                }
                                break;
                            case "12":
                                if (tempEndPos > 133275309 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 133275309;
                                }
                                break;
                            case "13":
                                if (tempEndPos > 114364328 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 114364328;
                                }
                                break;
                            case "14":
                                if (tempEndPos > 107043718 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 107043718;
                                }
                                break;
                            case "15":
                                if (tempEndPos > 101991189 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 101991189;
                                }
                                break;
                            case "16":
                                if (tempEndPos > 90338345 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 90338345;
                                }
                                break;
                            case "17":
                                if (tempEndPos > 83257441 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 83257441;
                                }
                                break;
                            case "18":
                                if (tempEndPos > 80373285 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 80373285;
                                }
                                break;
                            case "19":
                                if (tempEndPos > 58617616 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 58617616;
                                }
                                break;
                            case "20":
                                if (tempEndPos > 64444167 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 64444167;
                                }
                                break;
                            case "21":
                                if (tempEndPos > 46709983 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 46709983;
                                }
                                break;
                            case "22":
                                if (tempEndPos > 50818468 || tempStartPos < 1) {
                                    withinRange = false;
                                    chrEndBound = 50818468;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                boolean espError = false;
                if (getESP && isChrSelected && popSelected && startSelected && endSelected && startEndValid && withinRange && fileLocSelected) {
                    Process proc;
                    try {
                        if (System.getProperty("os.name").contains("Windows")) {
                            File f = new File(linkFile);
                            if (f.exists()) {
                                FilenameFilter jdkFilter = (File dir, String name) -> name.contains("jdk");
                                File[] jdkList = f.listFiles(jdkFilter);
                                CodeSource codeSource = FerretMain.class.getProtectionDomain().getCodeSource();
                                File jarFile = new File(codeSource.getLocation().toURI().getPath());
                                String jarDir = jarFile.getParentFile().getPath();

                                if (jdkList.length == 0) {
                                    proc = Runtime.getRuntime().exec(runJar + jarDir + linkJar);
                                    proc.waitFor();
                                } else {
                                    Arrays.sort(jdkList);
                                    System.out.println(jdkList[0]);
                                    proc = Runtime.getRuntime().exec(jdkList[0].toString() + "\\bin\\jar -xf \"" + jarDir + linkJar);
                                    proc.waitFor();
                                }
                            }
                        } else {
                            CodeSource codeSource = FerretMain.class.getProtectionDomain().getCodeSource();
                            File jarFile = new File(codeSource.getLocation().toURI().getPath());
                            String jarDir = jarFile.getParentFile().getPath();
                            proc = Runtime.getRuntime().exec(new String[]{"bash", "-c", "jar -xf '" + jarDir + "/Ferret_v2.1.1.jar' evsClient0_15.jar"});
                            proc.waitFor();
                        }
                    } catch (IOException | InterruptedException | URISyntaxException ex) {
                    }

                    File evs = new File(evsClient);
                    if (!evs.exists()) {
                        int choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                "Ferret encountered a problem with Exome Sequencing Project\n"
                                + "Please check to make sure you have JDK installed (See FAQ)\n"
                                + "Do you want to run Ferret anyway?",
                                "Exome Sequencing Project Error",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                new Object[]{"Yes", "No"},
                                null);
                        if (choice == JOptionPane.YES_OPTION) {
                            getESP = false;
                        } else {
                            espError = true;
                        }
                    }
                }
                if (isChrSelected && popSelected && startSelected && endSelected && startEndValid && withinRange && fileLocSelected && !espError) {
                    InputRegion[] queries = {new InputRegion(chrSelected, Integer.parseInt(startPosition), Integer.parseInt(endPosition))};

                    // if not get esp, string is none, else if get only ref, then string is ref, else string is both
                    // this should be combined with the one single call to Ferret later
                    // HERE
                    final Integer[] variants = {0};
                    String output = null;

                    switch (gui.getCurrFileOut()[0]) {
                        case ALL:
                            output = "all";
                            break;
                        case FRQ:
                            output = "freq";
                            break;
                        case VCF:
                            output = "vcf";
                            break;
                    }

                    String webAddress = null;
                    if (gui.getCurrVersion()[0] == GUI.version1KG.ONE) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20110521/ALL.chr$.phase1_release_v3.20101123.snps_indels_svs.genotypes.vcf.gz";
                    } else if (gui.getCurrVersion()[0] == GUI.version1KG.THREE && gui.getDefaultHG()[0]) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/ALL.chr$.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz";
                    } else if (gui.getCurrVersion()[0] == GUI.version1KG.THREE && !(gui.getDefaultHG()[0])) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/supporting/GRCh38_positions/ALL.chr$.phase3_shapeit2_mvncall_integrated_v3plus_nounphased.rsID.genotypes.GRCh38_dbSNP_no_SVs.vcf.gz";
                    }
//LOCUS
                    FerretData currFerretWorker = new FerretData(queries, populations, gui.getFileNameAndPath(), getESP, gui.getProgressText(), webAddress, gui.getMafThreshold()[0], gui.getMafThresholdMax()[0], gui.getEspMAFBoolean()[0], output);
                    currFerretWorker.setHaplo(boolHaplo);

                    currFerretWorker.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                        switch (evt.getPropertyName()) {
                            case "progress":
                                GUI.getProgressBar().setValue((Integer) evt.getNewValue());
                            case "state":
                                try {
                                    switch ((SwingWorker.StateValue) evt.getNewValue()) {
                                        case DONE:
                                            GUI.getProgressWindow().setVisible(false);
                                            try {
                                                variants[0] = currFerretWorker.get();
                                            } catch (ExecutionException e) {
                                                variants[0] = -1;
                                            } catch (InterruptedException e) {
                                                variants[0] = -1;
                                                Thread.currentThread().interrupt();
                                            }

                                            new File(evsClient).delete();
                                            Object[] options = {"Yes", "No"};
                                            int choice;
                                            System.out.println("Total Time: " + (System.nanoTime() - startTime));
                                            if (null == variants[0]) {
                                                choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                        "Ferret has encountered a problem downloading data. \n"
                                                        + "Please try again later or consult the FAQ. \nDo you want to close Ferret?",
                                                        "Close Ferret?",
                                                        JOptionPane.YES_NO_OPTION,
                                                        JOptionPane.PLAIN_MESSAGE,
                                                        null,
                                                        options,
                                                        null);
                                            } else {
                                                switch (variants[0]) {
                                                    case 1:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "Files have been downloaded\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    case -3:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "After applying the MAF threshold, no variants were found"
                                                                + "\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    case 0:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "No variants were found in this region\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    default:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "Ferret has encountered a problem downloading data. \n"
                                                                + "Please try again later or consult the FAQ. \nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                }
                                            }
                                            if (choice == JOptionPane.YES_OPTION) {
                                                GUI.getSnpFerret().dispose();
                                                System.exit(0);
                                            } else {
                                                gui.enableComponents(GUI.getSnpFerret(), true);
                                                if (gui.getCurrFileOut()[0] == GUI.fileOutput.VCF) {
                                                    gui.getSnpESPCheckBox().setEnabled(false);
                                                    gui.getGeneESPCheckBox().setEnabled(false);
                                                    gui.getChrESPCheckBox().setEnabled(false);
                                                }
                                                for (int i = 0; i < gui.getAsnsub().length; i++) {
                                                    gui.getAsnPanel().add(gui.getAsnsub()[i]);
                                                    if (gui.getAsnsub()[i].getText().contains("n=0")) {
                                                        gui.getAsnsub()[i].setEnabled(false);
                                                    }
                                                }
                                                gui.getProgressText().setText("Initializing...");
                                                GUI.getProgressBar().setValue(0);
                                                gui.checkBoxReset();
                                            }
                                            break;
                                        case STARTED:
                                        case PENDING:
                                            Dimension windowSize = GUI.getSnpFerret().getSize();
                                            GUI.getProgressWindow().setSize(new Dimension((int) (windowSize.width * .5), (int) (windowSize.height * .2)));
                                            GUI.getProgressWindow().setLocationRelativeTo(GUI.getSnpFerret());
                                            GUI.getProgressWindow().setVisible(true);
                                            gui.enableComponents(GUI.getSnpFerret(), false);
                                    }
                                } catch (ClassCastException e) {
                                }
                        }
                    });
//FERRET DATA doInBackGround, method downloading and working on the data
                    currFerretWorker.execute();

                } else {
                    StringBuilder errorMessage = new StringBuilder("Correct the following errors:");
                    if (!isChrSelected) {
                        errorMessage.append("\n Select a chromosome");
                    }
                    if (!popSelected) {
                        errorMessage.append("\n Select one or more populations");
                    }
                    if (!startSelected) {
                        errorMessage.append("\n Enter a valid, integer starting position");
                    }
                    if (!endSelected) {
                        errorMessage.append("\n Enter a valid, integer ending position");
                    }
                    if (!startEndValid) {
                        errorMessage.append("\n Starting position must be less than ending position");
                    }
                    if (!withinRange) {
                        errorMessage.append("\n Invalid chromosome positions. Valid positions for chr").append(chrSelected).append(" are from 1 to ").append(chrEndBound);
                    }
                    if (!fileLocSelected) {
                        errorMessage.append("\n Select a destination for the files to be saved");
                    }
                    if (espError) {
                        errorMessage.append("\n JDK error. Consult the FAQ for help with exome sequencing project errors.");
                    }
                    JOptionPane.showMessageDialog(GUI.getSnpFerret(), errorMessage, "Error", JOptionPane.OK_OPTION);
                }

                //GENE RUN
                break;
            }
            case 1: {
                // Gene starts after this line ------------------------------------------------------------------
                boolean getESP = gui.getGeneESPCheckBox().isSelected();
                String geneString = gui.getGeneNameField().getText();
                String[] geneListArray = null;
                boolean geneListInputted = geneString.length() > 0;
                boolean geneFileImported = gui.getGeneFileNameAndPath() != null;
                boolean geneFileError = false;
                boolean geneFileExtensionError = false;
                boolean invalidCharacter = false;
                boolean geneNameInputted = gui.getGeneNameRadioButton().isSelected();
                String geneWindowSize = gui.getGeneWindowField().getText();
                boolean geneWindowSelected = gui.getGeneWindowCheckBox().isSelected();
                boolean validWindowSizeEntered = true; // must be both not empty and an int
                if (geneWindowSelected) {
                    if (geneWindowSize.length() == 0) {
                        validWindowSizeEntered = false; // must have something there
                    } else { // test for non ints
                        for (int i = 0; i < geneWindowSize.length(); i++) {
                            if (!Character.isDigit(geneWindowSize.charAt(i))) {
                                validWindowSizeEntered = false;
                            }
                        }
                    }
                } else { //if no window specified, it's always fine
                    geneWindowSize = "0";
                }
                String invalidRegex;
                if (geneNameInputted) {
                    invalidRegex = ".*[^a-zA-Z0-9\\-].*"; // This is everything except letters and numbers, including underscore
                } else {
                    invalidRegex = ".*\\D.*"; // This is everything except numbers
                }
                if (geneFileImported) {
                    if (gui.getGeneFileNameAndPath().length() <= 4) {
                        geneFileError = true;
                    } else {
                        String fileType = gui.getGeneFileNameAndPath().substring(gui.getGeneFileNameAndPath().length() - 4);
                        String delimiter = null;
                        switch (fileType) {
                            case ".csv":
                                delimiter = ",";
                                break;
                            case ".tab":
                            case ".tsv":
                                delimiter = "\\t";
                                break;
                            case ".txt":
                                delimiter = " ";
                                break;
                            default:
                                geneFileExtensionError = true;
                                break;
                        }
                        ArrayList<String> geneListArrayList = new ArrayList<>();

                        if (delimiter != null) {
                            try (
                                    BufferedReader geneFileRead = new BufferedReader(new FileReader(gui.getGeneFileNameAndPath()));) {
                                String geneStringToParse;
                                while ((geneStringToParse = geneFileRead.readLine()) != null) {
                                    String[] text = geneStringToParse.split(delimiter);
                                    for (int i = 0; i < text.length; i++) {
                                        text[i] = text[i].replace(" ", "").toUpperCase(new Locale("all")); // remove spaces
                                        if (text[i].matches(invalidRegex)) { // identify invalid characters
                                            invalidCharacter = true;
                                            break;
                                        }
                                        if (text[i].length() > 0) {
                                            geneListArrayList.add(text[i]);
                                        }
                                    }
                                }
                                geneListArray = geneListArrayList.toArray(new String[geneListArrayList.size()]);
                            } catch (IOException | NullPointerException ex) {
                                geneFileError = true;
                            }
                        }
                    }

                } else if (geneListInputted) {
                    geneString = geneString.toUpperCase(new Locale("all"));
                    String geneList = geneString.replace(" ", "");
                    invalidCharacter = geneList.replace(",", "").matches(invalidRegex);
                    if (geneList.endsWith(",")) {
                        geneList = geneList.substring(0, geneList.length() - 1);
                    }
                    geneListArray = geneList.split(",");
                }
                boolean espError = false;
                if (getESP && (geneListInputted || (geneFileImported && !geneFileError && !geneFileExtensionError)) && !invalidCharacter && popSelected && fileLocSelected) {
                    Process proc;
                    try {
                        if (System.getProperty("os.name").contains("Windows")) {
                            File f = new File(linkFile);
                            if (f.exists()) {

                                FilenameFilter jdkFilter = (File dir, String name) -> name.contains("jdk");

                                File[] jdkList = f.listFiles(jdkFilter);
                                CodeSource codeSource = FerretMain.class.getProtectionDomain().getCodeSource();
                                File jarFile = new File(codeSource.getLocation().toURI().getPath());
                                String jarDir = jarFile.getParentFile().getPath();

                                if (jdkList.length == 0) {
                                    proc = Runtime.getRuntime().exec(runJar + jarDir + linkJar);
                                    proc.waitFor();
                                } else {
                                    Arrays.sort(jdkList);
                                    proc = Runtime.getRuntime().exec(jdkList[0].toString() + "\\bin\\jar -xf \"" + jarDir + linkJar);
                                    proc.waitFor();
                                }
                            }
                        } else {
                            CodeSource codeSource = FerretMain.class.getProtectionDomain().getCodeSource();
                            File jarFile = new File(codeSource.getLocation().toURI().getPath());
                            String jarDir = jarFile.getParentFile().getPath();
                            proc = Runtime.getRuntime().exec(new String[]{"bash", "-c", "jar -xf '" + jarDir + "/Ferret_v2.1.1.jar' evsClient0_15.jar"});
                            proc.waitFor();
                        }
                    } catch (IOException | InterruptedException | URISyntaxException ex) {
                    }

                    File evs = new File(evsClient);
                    if (!evs.exists()) {
                        int choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                "Ferret encountered a problem with Exome Sequencing Project\n"
                                + "Please check to make sure you have JDK installed (See FAQ)\n"
                                + "Do you want to run Ferret anyway?",
                                "Exome Sequencing Project Error",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                new Object[]{"Yes", "No"},
                                null);
                        if (choice == JOptionPane.YES_OPTION) {
                            getESP = false;
                        } else {
                            espError = true;
                        }
                    }
                }
                if ((geneListInputted || (geneFileImported && !geneFileError && !geneFileExtensionError)) && !invalidCharacter && popSelected && fileLocSelected && !espError) {

                    // this should be combined with the one single call to Ferret later
                    final Integer[] variants = {0};
                    String output = null;

                    switch (gui.getCurrFileOut()[0]) {
                        case ALL:
                            output = "all";
                            break;
                        case FRQ:
                            output = "freq";
                            break;
                        case VCF:
                            output = "vcf";
                            break;
                    }

                    String webAddress = null;

                    if (gui.getCurrVersion()[0] == GUI.version1KG.ONE) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20110521/ALL.chr$.phase1_release_v3.20101123.snps_indels_svs.genotypes.vcf.gz";
                    } else if (gui.getCurrVersion()[0] == GUI.version1KG.THREE && gui.getDefaultHG()[0]) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/ALL.chr$.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz";
                    } else if (gui.getCurrVersion()[0] == GUI.version1KG.THREE && !(gui.getDefaultHG()[0])) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/supporting/GRCh38_positions/ALL.chr$.phase3_shapeit2_mvncall_integrated_v3plus_nounphased.rsID.genotypes.GRCh38_dbSNP_no_SVs.vcf.gz";
                    }

                    String geneQueryType;
                    if (geneNameInputted) {
                        geneQueryType = "geneName";
                    } else {
                        geneQueryType = "geneID";
                    }
//GENE
                    FerretData currFerretWorker = new FerretData(geneQueryType, geneListArray, populations, gui.getFileNameAndPath(), getESP, gui.getProgressText(), webAddress, gui.getMafThreshold()[0], gui.getMafThresholdMax()[0], gui.getEspMAFBoolean()[0], output, gui.getDefaultHG()[0], geneWindowSelected, Integer.parseInt(geneWindowSize), 0);
                    currFerretWorker.setHaplo(boolHaplo);

                    currFerretWorker.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                        switch (evt.getPropertyName()) {
                            case "progress":
                                GUI.getProgressBar().setValue((Integer) evt.getNewValue());
                                break;
                            case "state":
                                try {
                                    switch ((SwingWorker.StateValue) evt.getNewValue()) {
                                        case DONE:
                                            GUI.getProgressWindow().setVisible(false);
                                            try {
                                                variants[0] = currFerretWorker.get();
                                            } catch (ExecutionException e) {
                                                variants[0] = -1;
                                            } catch (InterruptedException e) {
                                                variants[0] = -1;
                                                Thread.currentThread().interrupt();
                                            }

                                            new File("evsClient0_15.jar").delete();
                                            Object[] options = {"Yes", "No"};
                                            int choice;
                                            System.out.println("Total Time: " + (System.nanoTime() - startTime));
                                            if (null == variants[0]) {

                                                choice = JOptionPane.NO_OPTION;
                                            } else {
                                                switch (variants[0]) {
                                                    case 1:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "Files have been downloaded\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    case -3:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "After applying the MAF threshold, no variants were found"
                                                                + "\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    case 0:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "No variants were found in this region\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    case -1:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "Ferret has encountered a problem downloading data. \n"
                                                                + "Please try again later or consult the FAQ. \nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    default:
                                                        choice = JOptionPane.NO_OPTION;
                                                        break;
                                                }
                                            }
                                            if (choice == JOptionPane.YES_OPTION) {
                                                GUI.getSnpFerret().dispose();
                                                System.exit(0);
                                            } else {
                                                gui.enableComponents(GUI.getSnpFerret(), true);
                                                if (gui.getCurrFileOut()[0] == GUI.fileOutput.VCF) {
                                                    gui.getSnpESPCheckBox().setEnabled(false);
                                                    gui.getGeneESPCheckBox().setEnabled(false);
                                                    gui.getChrESPCheckBox().setEnabled(false);
                                                }
                                                for (int i = 0; i < gui.getAsnsub().length; i++) {
                                                    gui.getAsnPanel().add(gui.getAsnsub()[i]);
                                                    if (gui.getAsnsub()[i].getText().contains("n=0")) {
                                                        gui.getAsnsub()[i].setEnabled(false);
                                                    }
                                                }
                                                gui.getProgressText().setText("Initializing...");
                                                GUI.getProgressBar().setValue(0);
                                                gui.checkBoxReset();
                                            }
                                            break;
                                        case STARTED:
                                        case PENDING:
                                            Dimension windowSize = GUI.getSnpFerret().getSize();
                                            GUI.getProgressWindow().setSize(new Dimension((int) (windowSize.width * .5), (int) (windowSize.height * .2)));
                                            GUI.getProgressWindow().setLocationRelativeTo(GUI.getSnpFerret());
                                            GUI.getProgressWindow().setVisible(true);
                                            gui.enableComponents(GUI.getSnpFerret(), false);
                                    }
                                } catch (ClassCastException ex) {
                                }
                            default:
                                break;
                        }
                    });
//FerretData method doInBackground
                    currFerretWorker.execute();

                } else {
                    StringBuilder errorMessage = new StringBuilder("Correct the following errors:");
                    if (!geneListInputted && !geneFileImported) {
                        errorMessage.append("\n Enter a gene name/ID or select a file");
                    }
                    if (geneFileImported && geneFileError) {
                        errorMessage.append("\n There was a problem reading the Gene file. Please check the FAQ.");
                    }
                    if (geneFileImported && geneFileExtensionError) {
                        errorMessage.append("\n Invalid file extension. Ferret supports tsv, csv, tab, and txt files.");
                    }
                    if ((geneListInputted || geneFileImported) && invalidCharacter) {
                        errorMessage.append("\n Invalid character entered");
                    }
                    if (!fileLocSelected) {
                        errorMessage.append("\n Select a destination for the files to be saved");
                    }
                    if (!popSelected) {
                        errorMessage.append("\n Select one or more populations");
                    }
                    if (espError) {
                        errorMessage.append("\n JDK error. Consult the FAQ for help with Exome Sequencing Project errors.");
                    }
                    JOptionPane.showMessageDialog(GUI.getSnpFerret(), errorMessage, "Error", JOptionPane.OK_OPTION);
                }
                break;
            }
            default: {
                // SNP starts here ---------------------------------------------------------------------------------

                boolean getESP = gui.getSnpESPCheckBox().isSelected();
                String snpString = gui.getSnpTextField().getText();
                boolean snpListInputted = snpString.length() > 0;
                boolean snpFileImported = gui.getSnpFileNameAndPath() != null;
                boolean snpFileError = false;
                boolean snpFileExtensionError = false;
                boolean invalidCharacter = false;
                String invalidRegex = ".*\\D.*"; // This is everything except numbers
                ArrayList<String> snpListArray = new ArrayList<>();
                String snpWindowSize = gui.getSnpWindowField().getText();
                boolean snpWindowSelected = gui.getSnpWindowCheckBox().isSelected();
                boolean validWindowSizeEntered = true; // must be both not empty and an int
                if (snpWindowSelected) {
                    if (snpWindowSize.length() == 0) {
                        validWindowSizeEntered = false; // must have something there
                    } else { // test for non ints
                        for (int i = 0; i < snpWindowSize.length(); i++) {
                            if (!Character.isDigit(snpWindowSize.charAt(i))) {
                                validWindowSizeEntered = false;
                            }
                        }
                    }
                } else { //if no window specified, it's always fine
                    snpWindowSize = "0";
                }
                if (snpFileImported) {
                    if (gui.getSnpFileNameAndPath().length() <= 4) {
                        snpFileError = true;
                    } else {
                        String fileType = gui.getSnpFileNameAndPath().substring(gui.getSnpFileNameAndPath().length() - 4);
                        String delimiter = null;
                        switch (fileType) {
                            case ".csv":
                                delimiter = ",";
                                break;
                            case ".tab":
                            case ".tsv":
                                delimiter = "\\t";
                                break;
                            case ".txt":
                                delimiter = " ";
                                break;
                            default:
                                snpFileExtensionError = true;
                                break;
                        }
                        if (delimiter != null) {
                            try (
                                    BufferedReader snpFileRead = new BufferedReader(new FileReader(gui.getSnpFileNameAndPath()));) {
                                String snpStringToParse;
                                while ((snpStringToParse = snpFileRead.readLine()) != null) {
                                    String[] text = snpStringToParse.split(delimiter);
                                    for (int i = 0; i < text.length; i++) {
                                        text[i] = text[i].replace(" ", ""); // remove spaces
                                        if (text[i].matches(invalidRegex)) { // identify invalid characters
                                            invalidCharacter = true; // probably can just throw error here, might be easier/more straight forward. But then errors wouldn't be 'accumulated' to the end
                                            break;
                                        }
                                        if (text[i].length() > 0) {
                                            snpListArray.add(text[i]);
                                        }
                                    }
                                }
                            } catch (IOException | NullPointerException ex) {
                                snpFileError = true;
                            }
                        }
                    }

                } else if (snpListInputted) {

                    while (snpString.endsWith(",") || snpString.endsWith(" ")) { // maybe this should be added for gene input too
                        snpString = snpString.substring(0, snpString.length() - 1);
                    }
                    String[] text = snpString.split(",");
                    for (int i = 0; i < text.length; i++) {
                        text[i] = text[i].replace(" ", "");// remove spaces
                        if (text[i].matches(invalidRegex)) {
                            invalidCharacter = true;
                            break;
                        }
                    }
                    snpListArray = new ArrayList<>(Arrays.asList(text));
                }
                boolean espError = false;
                if (getESP && (snpListInputted || (snpFileImported && !snpFileError && !snpFileExtensionError)) && !invalidCharacter && validWindowSizeEntered && popSelected && fileLocSelected) {
                    Process proc;
                    try {
                        if (System.getProperty("os.name").contains("Windows")) {
                            File f = new File(linkFile);
                            if (f.exists()) {

                                FilenameFilter jdkFilter = (File dir, String name) -> name.contains("jdk");

                                File[] jdkList = f.listFiles(jdkFilter);
                                CodeSource codeSource = FerretMain.class.getProtectionDomain().getCodeSource();
                                File jarFile = new File(codeSource.getLocation().toURI().getPath());
                                String jarDir = jarFile.getParentFile().getPath();

                                if (jdkList.length == 0) {
                                    proc = Runtime.getRuntime().exec(runJar + jarDir + linkJar);
                                    proc.waitFor();
                                } else {
                                    Arrays.sort(jdkList);
                                    proc = Runtime.getRuntime().exec(jdkList[0].toString() + "\\bin\\jar -xf \"" + jarDir + linkJar);
                                    proc.waitFor();
                                }
                            }
                        } else {
                            CodeSource codeSource = FerretMain.class.getProtectionDomain().getCodeSource();
                            File jarFile = new File(codeSource.getLocation().toURI().getPath());
                            String jarDir = jarFile.getParentFile().getPath();
                            proc = Runtime.getRuntime().exec(new String[]{"bash", "-c", "jar -xf '" + jarDir + "/Ferret_v2.1.1.jar' evsClient0_15.jar"});
                            proc.waitFor();
                        }
                    } catch (IOException | InterruptedException | URISyntaxException ex) {
                    }

                    File evs = new File(evsClient);
                    if (!evs.exists()) {
                        int choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                "Ferret encountered a problem with Exome Sequencing Project\n "
                                + "Please check to make sure you have JDK installed (See FAQ)\n"
                                + "Do you want to run Ferret anyway?",
                                "Exome Sequencing Project Error",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                new Object[]{"Yes", "No"},
                                null);
                        if (choice == JOptionPane.YES_OPTION) {
                            getESP = false;
                        } else {
                            espError = true;
                        }
                    }
                }
                if ((snpListInputted || (snpFileImported && !snpFileError && !snpFileExtensionError)) && !invalidCharacter && validWindowSizeEntered && popSelected && fileLocSelected && !espError) {

                    final Integer[] variants = {0};
                    String output = null;

                    switch (gui.getCurrFileOut()[0]) {
                        case ALL:
                            output = "all";
                            break;
                        case FRQ:
                            output = "freq";
                            break;
                        case VCF:
                            output = "vcf";
                            break;
                    }

                    String webAddress = null;

                    if (gui.getCurrVersion()[0] == GUI.version1KG.ONE) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20110521/ALL.chr$.phase1_release_v3.20101123.snps_indels_svs.genotypes.vcf.gz";
                    } else if (gui.getCurrVersion()[0] == GUI.version1KG.THREE && gui.getDefaultHG()[0]) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/ALL.chr$.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz";
                    } else if (gui.getCurrVersion()[0] == GUI.version1KG.THREE && !(gui.getDefaultHG()[0])) {
                        webAddress = "http://ftp.1000genomes.ebi.ac.uk/vol1/ftp/release/20130502/supporting/GRCh38_positions/ALL.chr$.phase3_shapeit2_mvncall_integrated_v3plus_nounphased.rsID.genotypes.GRCh38_dbSNP_no_SVs.vcf.gz";
                    }
//VARIANT
                    FerretData currFerretWorker = new FerretData("SNP", snpListArray, populations, gui.getFileNameAndPath(), getESP, gui.getProgressText(), webAddress, gui.getMafThreshold()[0], gui.getMafThresholdMax()[0],
                            gui.getEspMAFBoolean()[0], output, gui.getDefaultHG()[0], snpWindowSelected, Integer.parseInt(snpWindowSize));
                    currFerretWorker.setHaplo(boolHaplo);
                    currFerretWorker.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                        switch (evt.getPropertyName()) {
                            case "progress":
                                GUI.getProgressBar().setValue((Integer) evt.getNewValue());
                                break;
                            case "state":
                                try {
                                    switch ((SwingWorker.StateValue) evt.getNewValue()) {
                                        case DONE:
                                            GUI.getProgressWindow().setVisible(false);
                                            try {
                                                variants[0] = currFerretWorker.get();
                                            } catch (ExecutionException e) {
                                                variants[0] = -1;
                                            } catch (InterruptedException e) {
                                                variants[0] = -1;
                                                Thread.currentThread().interrupt();
                                            }

                                            new File(evsClient).delete();
                                            Object[] options = {"Yes", "No"};
                                            int choice;
                                            System.out.println("Total Time: " + (System.nanoTime() - startTime));
                                            if (null == variants[0]) { //Only comes here if no SNPs found and user does not want to quit or
                                                //not all SNPs found and user doesn't wish to continue with partial query
                                                choice = JOptionPane.NO_OPTION;
                                            } else {
                                                switch (variants[0]) {
                                                    case 1:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "Files have been downloaded\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    case -3:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "After applying the MAF threshold, no variants were found"
                                                                + "\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    case 0:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "No variants were found in this region\nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    case -1:
                                                        choice = JOptionPane.showOptionDialog(GUI.getSnpFerret(),
                                                                "Ferret has encountered a problem downloading data. \n"
                                                                + "Please try again later or consult the FAQ. \nDo you want to close Ferret?",
                                                                "Close Ferret?",
                                                                JOptionPane.YES_NO_OPTION,
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                options,
                                                                null);
                                                        break;
                                                    default:
                                                        //Only comes here if no SNPs found and user does not want to quit or
                                                        //not all SNPs found and user doesn't wish to continue with partial query
                                                        choice = JOptionPane.NO_OPTION;
                                                        break;
                                                }
                                            }
                                            if (choice == JOptionPane.YES_OPTION) {
                                                GUI.getSnpFerret().dispose();
                                                System.exit(0);
                                            } else {
                                                gui.enableComponents(GUI.getSnpFerret(), true);
                                                if (gui.getCurrFileOut()[0] == GUI.fileOutput.VCF) {
                                                    gui.getSnpESPCheckBox().setEnabled(false);
                                                    gui.getGeneESPCheckBox().setEnabled(false);
                                                    gui.getChrESPCheckBox().setEnabled(false);
                                                }
                                                for (int i = 0; i < gui.getAsnsub().length; i++) {
                                                    gui.getAsnPanel().add(gui.getAsnsub()[i]);
                                                    if (gui.getAsnsub()[i].getText().contains("n=0")) {
                                                        gui.getAsnsub()[i].setEnabled(false);
                                                    }
                                                }
                                                gui.getProgressText().setText("Initializing...");
                                                GUI.getProgressBar().setValue(0);
                                                gui.checkBoxReset();
                                            }
                                            break;
                                        case STARTED:
                                        case PENDING:
                                            Dimension windowSize = GUI.getSnpFerret().getSize();
                                            GUI.getProgressWindow().setSize(new Dimension((int) (windowSize.width * .5), (int) (windowSize.height * .2)));
                                            GUI.getProgressWindow().setLocationRelativeTo(GUI.getSnpFerret());
                                            GUI.getProgressWindow().setVisible(true);
                                            gui.enableComponents(GUI.getSnpFerret(), false);
                                    }
                                } catch (ClassCastException ex) {
                                }
                            default:
                                break;
                        }
                    });
                    currFerretWorker.execute();

                } else {

                    StringBuilder errorMessage = new StringBuilder("Correct the following errors:");
                    if (!snpListInputted && !snpFileImported) {
                        errorMessage.append("\n Enter a variant number or select a file");
                    }
                    if (snpFileImported && snpFileError) {
                        errorMessage.append("\n There was a problem reading the variant file. Please check the FAQ.");
                    }
                    if (snpFileImported && snpFileExtensionError) {
                        errorMessage.append("\n Invalid file extension. Ferret supports tsv, csv, tab, and txt files.");
                    }
                    if ((snpListInputted || snpFileImported) && invalidCharacter) {
                        errorMessage.append("\n Invalid character entered");
                    }
                    if (!fileLocSelected) {
                        errorMessage.append("\n Select a destination for the files to be saved");
                    }
                    if (!popSelected) {
                        errorMessage.append("\n Select one or more populations");
                    }
                    if (!validWindowSizeEntered) {
                        errorMessage.append("\n You must enter an integer window size if you wish to retrieve regions around variants");
                    }
                    if (espError) {
                        errorMessage.append("\n JDK error. Consult the FAQ for help with Exome Sequencing Project errors.");
                    }
                    JOptionPane.showMessageDialog(GUI.getSnpFerret(), errorMessage, "Error", JOptionPane.OK_OPTION);
                }
                break;
            }
        }
    }
}
