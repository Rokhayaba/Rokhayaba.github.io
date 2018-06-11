/*
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------- FERRET APPLICATION ------------------------------------------------------------------------------------


AUTHOR : Sophie Limou
 */
package com.ecn.ferretmvc.view;

// import classes
import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Hashtable;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame {

    //Declarations
    private String fileNameAndPath;
    URL questionMarkURL = getClass().getResource("questionMark25.png");
    ImageIcon questionMark = new ImageIcon(questionMarkURL);

    // Races Selection Declarations	
    JLabel fileLocation = new JLabel("File location: None Selected");
    String[] asnCode = {"EAS", "CDX", "CHB", "CHS", "JPT", "KHV", "CHD"};
    String[] eurCode = {"EUR", "CEU", "GBR", "FIN", "IBS", "TSI"};
    String[] afrCode = {"AFR", "ACB", "ASW", "ESN", "GWD", "LWK", "MSL", "YRI"};
    String[] amrCode = {"AMR", "CLM", "MXL", "PEL", "PUR"};
    String[] sanCode = {"SAS", "BEB", "GIH", "ITU", "PJL", "STU"};
    String[] allracesString = {"ALL"};
    static JFrame snpFerret = new JFrame("Ferret v2.1.2");
    JLabel afrLabel = new JLabel("Africans");
    JLabel eurLabel = new JLabel("Europeans");
    JLabel asnLabel = new JLabel("East Asians");
    JLabel amrLabel = new JLabel("Americans");
    JLabel sanLabel = new JLabel("South Asians");
    JLabel allracesLabel = new JLabel("All Populations");
    JCheckBox[] afrsub = new JCheckBox[8];
    JCheckBox[] eursub = new JCheckBox[6];
    JCheckBox[] amrsub = new JCheckBox[5];
    JCheckBox[] sansub = new JCheckBox[6];
    JCheckBox[] asnsub = new JCheckBox[7];
    JCheckBox[] allracessub = new JCheckBox[1];

    //---------------------
    // Locus Research Panel Declarations
    JLabel chrLabel = new JLabel(": Chromosome:");
    JLabel selectChrRegionLabel = new JLabel("Input Locus ");
    JLabel startLabel = new JLabel("Start:");
    JLabel endLabel = new JLabel("End:");
    String[] chrOptions = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"};
    private JComboBox<String> chrList = new JComboBox<>(chrOptions);
    private JTextField startPosTextField = new JTextField(8);
    private JTextField endPosTextField = new JTextField(8);
    JCheckBox chrESPCheckBox = new JCheckBox("Output frequencies from the Exome Sequencing Project");
    JLabel questionMarkLocusInput = new JLabel(questionMark);

    //---------------------
    // Variant Panel Declarations
    private JTextField snpTextField = new JTextField(8);
    private JTextField snpWindowField = new JTextField(8);
    private JTextField geneNameField = new JTextField(8);
    JCheckBox snpWindowCheckBox = new JCheckBox("Include surrounding variant(s) in a window of ");
    JLabel snpWindowBP = new JLabel("bp");
    JLabel snpTextLabel = new JLabel("Input variant ID(s):");
    JPanel snpWindowPanel = new JPanel();
    JPanel snpSelectPanel = new JPanel();
    JPanel snpInputPanel = new JPanel();
    JPanel snpOptionsPanel = new JPanel();
    JPanel snpESPOptionPanel = new JPanel();
    JLabel snpOR = new JLabel("OR");
    JButton snpFileBrowseButton = new JButton("Browse");
    JButton snpFileClearButton = new JButton("Clear");
    JLabel snpFileLocation = new JLabel("No file selected");
    JCheckBox snpESPCheckBox = new JCheckBox("Output frequencies from the Exome Sequencing Project");
    String snpFileNameAndPath;
    JRadioButton snpNCBIRadioButton = new JRadioButton("NCBI");
    JRadioButton snpV37RadioButton = new JRadioButton("v37");
    ButtonGroup snpSourceButtonGroup = new ButtonGroup();
    JLabel questionMarkSNPInput = new JLabel(questionMark);
    JLabel questionMarkSNPFileInput = new JLabel(questionMark);

    // ---------------------
    // Gene Panel Declarations
    JLabel geneNameLabel = new JLabel("Input gene(s):");
    JCheckBox geneESPCheckBox = new JCheckBox("Output frequencies from the Exome Sequencing Project");
    private JTextField geneWindowField = new JTextField(8);
    JCheckBox geneWindowCheckBox = new JCheckBox("Include borders of all selected gene(s) in a window of ");
    JLabel geneWindowBP = new JLabel(" bp");
    JPanel geneWindowPanel = new JPanel();
    JPanel chromosomeSelectPanel = new JPanel();
    JPanel chromosomeInputPanel = new JPanel();
    JPanel chromosomeESPOptionPanel = new JPanel();
    JPanel geneSelectPanel = new JPanel();
    JPanel geneInputPanel = new JPanel();
    JPanel geneSelectOptionsPanel = new JPanel();
    JPanel geneESPOptionPanel = new JPanel();
    JTabbedPane inputSelect = new JTabbedPane();
    JLabel geneOR = new JLabel("OR");
    String geneFileNameAndPath;
    JLabel geneFileLocation = new JLabel("No file selected");
    JLabel geneInputType = new JLabel("Input gene as: ");
    JButton geneFileBrowseButton = new JButton("Browse");
    JButton geneFileClearButton = new JButton("Clear");
    JRadioButton geneNameRadioButton = new JRadioButton("Name");
    JRadioButton geneIDRadioButton = new JRadioButton("ID");
    ButtonGroup geneInputButtonGroup = new ButtonGroup();
    JRadioButton geneNCBIRadioButton = new JRadioButton("NCBI");
    JRadioButton geneV37RadioButton = new JRadioButton("v37");
    ButtonGroup geneSourceButtonGroup = new ButtonGroup();
    JLabel questionMarkGeneInput = new JLabel(questionMark);
    JLabel questionMarkGeneFileInput = new JLabel(questionMark);

    //------------------------ 
    JPanel bigPanel = new JPanel();
    JPanel kgPopulationPanel = new JPanel();
    JPanel goPanel = new JPanel();
    JPanel afrPanel = new JPanel();
    JPanel eurPanel = new JPanel();
    JPanel asnPanel = new JPanel();
    JPanel amrPanel = new JPanel();
    JPanel sanPanel = new JPanel();
    JPanel allracesPanel = new JPanel();
    JButton goButton = new JButton("Download the data !");
    JButton goHaplo = new JButton("Download and Visualize the data with HaploView !");
    JButton browseButton = new JButton("Browse");
    JFileChooser openFileChooser = new JFileChooser();
    JScrollPane scrollBigPanel = new JScrollPane(bigPanel);
    static JFrame progressWindow = new JFrame("Working...");
    static JProgressBar progressBar = new JProgressBar(0, 100);
    JLabel progressText = new JLabel("Initializing...");
    static Integer variantCounterResult;

    // ------------------
    // Update Window
    JFrame updateFrame = new JFrame("Update");
    JPanel updatePanel = new JPanel();
    JPanel updateBarHolder = new JPanel();
    JPanel updateButtonHolder = new JPanel();
    JProgressBar updateProgressBar = new JProgressBar();
    JLabel updateLabel = new JLabel("Checking for update...");
    JLabel updateDetailLabel = new JLabel("");
    JButton updateOK = new JButton("OK");
    final JFileChooser saveFileChooser = new JFileChooser();
    JPanel fileChoosePanel = new JPanel();

    // MenuBar
    JMenuBar ferretMenuBar = new JMenuBar();
    JMenu ferretMenu = new JMenu("Ferret");
    JMenu helpMenu = new JMenu("Help");
    JMenuItem settingsMenuItem = new JMenuItem("Settings");
    JMenuItem updateMenuItem = new JMenuItem("Check for updates");
    JMenuItem exitMenuItem = new JMenuItem("Quit");
    JMenuItem aboutMenuItem = new JMenuItem("About Ferret");
    JMenuItem faqMenuItem = new JMenuItem("FAQ");
    JMenuItem contactMenuItem = new JMenuItem("Contact");
    JLabel questionMarkMAFThreshold = new JLabel(questionMark);
    JLabel questionMarkESPMAF = new JLabel(questionMark);

    //Settings pane:
    JFrame settingsFrame = new JFrame("Settings");
    JPanel settingsPanel = new JPanel();
    JTextField vcfURLText = new JTextField();
    JTextField fileNomenclatureText = new JTextField();
    JSlider mafSlider = new JSlider(0, 5000, 0);
    JSlider mafSliderMax = new JSlider(0, 5000, 0);
    JRadioButton phase3Button = new JRadioButton("Phase 3 (2,504 individuals) [default]");
    JRadioButton phase1Button = new JRadioButton("Phase 1 (1,092 individuals)");
    ButtonGroup vcfRadioButtons = new ButtonGroup();
    JRadioButton allFilesButton = new JRadioButton("Allele Frequencies (.frq) + Plink/HaploView (.map/.ped/.info) [default]");
    JRadioButton freqFileButton = new JRadioButton("Allele Frequencies (.frq) only");
    JRadioButton vcfFileButton = new JRadioButton("VCF file only");
    ButtonGroup fileOutputButtons = new ButtonGroup();
    JRadioButton version19Button = new JRadioButton("hg19/GRCh37 [default]");
    JRadioButton version38Button = new JRadioButton("hg38/GRCh38 [only available for Phase 3 data]");
    ButtonGroup hgVersionButtons = new ButtonGroup();
    JLabel vcfVersionLabel = new JLabel("1000 Genomes Version");
    JLabel vcfURLLabel = new JLabel("OR specify the VCF URL");
    JLabel vcfNomenclatureLabel = new JLabel("AND file nomenclature for chr $");
    JLabel mafOptionLabel = new JLabel("Minor Allele Frequency (MAF)");
    JLabel mafThresholdLabel = new JLabel("MAF Threshold: ");
    JLabel hgVersionLabel = new JLabel("Human Genome Version");
    JLabel filesLabel = new JLabel("Output Files");
    JButton settingsOK = new JButton("OK");
    JButton settingsCancel = new JButton("Cancel");
    JPanel mafPanel = new JPanel();
    JPanel mafESPPanel = new JPanel();
    JPanel vcfVersionPanel = new JPanel();
    JPanel settingsButtonPanel = new JPanel();
    JCheckBox espMAF = new JCheckBox("Apply MAF threshold to the Exome Sequencing Project");

    //About window
    JFrame aboutFrame = new JFrame("About");
    JPanel aboutPanel = new JPanel();
    JLabel ferretVersionLabel = new JLabel("Ferret v2.1.1");
    JLabel ferretDateLabel = new JLabel("February 2016");
    JTextArea ferretCitation = new JTextArea("Citation: Limou, S., Taverner, A., Nelson, G., "
            + "Winkler, C.A. Ferret: a user-friendly Java tool to extract data from the 1000 Genomes "
            + "Project. Presented at the annual meeting of the American Society of Human Genetics "
            + "(ASHG), 2015, Baltimore, MD, USA.", 4, 50);

    //Contact window
    JFrame contactFrame = new JFrame("Contact");
    JPanel contactPanel = new JPanel();
    JLabel contactPeopleLabel = new JLabel("Sophie Limou and Andrew M. Taverner");
    JTextArea contactEmailLabel = new JTextArea("ferret@nih.gov");

    //Others
    NumberFormat mafFormat = NumberFormat.getNumberInstance();

    //Constants
    final JFormattedTextField mafText = new JFormattedTextField(mafFormat);
    final JFormattedTextField mafTextMax = new JFormattedTextField(mafFormat);
    final static version1KG[] currVersion = {version1KG.THREE};
    final static fileOutput[] currFileOut = {fileOutput.ALL};
    final static Boolean[] defaultHG = {true};
    final static double[] mafThreshold = {0.0};
    final static double[] mafThresholdMax = {0.5};
    final static Boolean[] espMAFBoolean = {false};
    final static Boolean[] checkedForUpdate = {false};

    // Enumerations
    public enum version1KG {
        ZERO, ONE, THREE
    }

    public enum fileOutput {
        ALL, FRQ, VCF
    }

    //-------------
    //Getters
    public JFormattedTextField getMafText() {
        return mafText;
    }

    public JFormattedTextField getMafTextMax() {
        return mafTextMax;
    }

    public version1KG[] getCurrVersion() {
        return currVersion;
    }

    public fileOutput[] getCurrFileOut() {
        return currFileOut;
    }

    public Boolean[] getDefaultHG() {
        return defaultHG;
    }

    public double[] getMafThreshold() {
        return mafThreshold;
    }

    public double[] getMafThresholdMax() {
        return mafThresholdMax;
    }

    public Boolean[] getEspMAFBoolean() {
        return espMAFBoolean;
    }

    public Boolean[] getCheckedForUpdate() {
        return checkedForUpdate;
    }

    public String getFileNameAndPath() {
        return fileNameAndPath;
    }

    public void setFileNameAndPath(String fileNameAndPath) {
        this.fileNameAndPath = fileNameAndPath;
    }

    public JLabel getFileLocation() {
        return fileLocation;
    }

    public String[] getAsnCode() {
        return asnCode;
    }

    public String[] getEurCode() {
        return eurCode;
    }

    public String[] getAfrCode() {
        return afrCode;
    }

    public String[] getAmrCode() {
        return amrCode;
    }

    public String[] getSanCode() {
        return sanCode;
    }

    public String[] getAllracesString() {
        return allracesString;
    }

    public static JFrame getSnpFerret() {
        return snpFerret;
    }

    public JCheckBox[] getAfrsub() {
        return afrsub;
    }

    public JCheckBox[] getEursub() {
        return eursub;
    }

    public JCheckBox[] getAmrsub() {
        return amrsub;
    }

    public JCheckBox[] getSansub() {
        return sansub;
    }

    public JCheckBox[] getAsnsub() {
        return asnsub;
    }

    public JCheckBox[] getAllracessub() {
        return allracessub;
    }

    public JComboBox<String> getChrList() {
        return chrList;
    }

    public JTextField getStartPosTextField() {
        return startPosTextField;
    }

    public JTextField getEndPosTextField() {
        return endPosTextField;
    }

    public JCheckBox getChrESPCheckBox() {
        return chrESPCheckBox;
    }

    public JLabel getQuestionMarkLocusInput() {
        return questionMarkLocusInput;
    }

    public JTextField getSnpTextField() {
        return snpTextField;
    }

    public JTextField getSnpWindowField() {
        return snpWindowField;
    }

    public JTextField getGeneNameField() {
        return geneNameField;
    }

    public JCheckBox getSnpWindowCheckBox() {
        return snpWindowCheckBox;
    }

    public JButton getSnpFileClearButton() {
        return snpFileClearButton;
    }

    public JLabel getSnpFileLocation() {
        return snpFileLocation;
    }

    public JCheckBox getSnpESPCheckBox() {
        return snpESPCheckBox;
    }

    public String getSnpFileNameAndPath() {
        return snpFileNameAndPath;
    }

    public void setSnpFileNameAndPath(String snpFileNameAndPath) {
        this.snpFileNameAndPath = snpFileNameAndPath;
    }

    public JRadioButton getSnpNCBIRadioButton() {
        return snpNCBIRadioButton;
    }

    public JCheckBox getGeneESPCheckBox() {
        return geneESPCheckBox;
    }

    public JTextField getGeneWindowField() {
        return geneWindowField;
    }

    public JCheckBox getGeneWindowCheckBox() {
        return geneWindowCheckBox;
    }

    public JTabbedPane getInputSelect() {
        return inputSelect;
    }

    public String getGeneFileNameAndPath() {
        return geneFileNameAndPath;
    }

    public void setGeneFileNameAndPath(String geneFileNameAndPath) {
        this.geneFileNameAndPath = geneFileNameAndPath;
    }

    public JLabel getGeneFileLocation() {
        return geneFileLocation;
    }

    public JButton getGeneFileClearButton() {
        return geneFileClearButton;
    }

    public JRadioButton getGeneNameRadioButton() {
        return geneNameRadioButton;
    }

    public JRadioButton getGeneNCBIRadioButton() {
        return geneNCBIRadioButton;
    }

    public JPanel getAfrPanel() {
        return afrPanel;
    }

    public JPanel getEurPanel() {
        return eurPanel;
    }

    public JPanel getAsnPanel() {
        return asnPanel;
    }

    public JPanel getAmrPanel() {
        return amrPanel;
    }

    public JPanel getSanPanel() {
        return sanPanel;
    }

    public JPanel getAllracesPanel() {
        return allracesPanel;
    }

    public JFileChooser getOpenFileChooser() {
        return openFileChooser;
    }

    public static JFrame getProgressWindow() {
        return progressWindow;
    }

    public static JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getProgressText() {
        return progressText;
    }

    public JFrame getUpdateFrame() {
        return updateFrame;
    }

    public JPanel getUpdatePanel() {
        return updatePanel;
    }

    public JPanel getUpdateBarHolder() {
        return updateBarHolder;
    }

    public JProgressBar getUpdateProgressBar() {
        return updateProgressBar;
    }

    public JLabel getUpdateLabel() {
        return updateLabel;
    }

    public JFileChooser getSaveFileChooser() {
        return saveFileChooser;
    }

    public JFrame getSettingsFrame() {
        return settingsFrame;
    }

    public JSlider getMafSlider() {
        return mafSlider;
    }

    public JSlider getMafSliderMax() {
        return mafSliderMax;
    }

    public JRadioButton getPhase3Button() {
        return phase3Button;
    }

    public JRadioButton getPhase1Button() {
        return phase1Button;
    }

    public JRadioButton getAllFilesButton() {
        return allFilesButton;
    }

    public JRadioButton getFreqFileButton() {
        return freqFileButton;
    }

    public JRadioButton getVcfFileButton() {
        return vcfFileButton;
    }

    public JRadioButton getVersion19Button() {
        return version19Button;
    }

    public JRadioButton getVersion38Button() {
        return version38Button;
    }

    public JCheckBox getEspMAF() {
        return espMAF;
    }

    public JFrame getAboutFrame() {
        return aboutFrame;
    }

    public JFrame getContactFrame() {
        return contactFrame;
    }

    // Constructor GUI
    public GUI() {
        LinkLabel ferretWebLabelAbout = null;
        try {
            ferretWebLabelAbout = new LinkLabel(new URI("http://limousophie35.github.io/Ferret/"), "http://limousophie35.github.io/Ferret/");
        } catch (URISyntaxException e) {
        }
        mafFormat.setMaximumFractionDigits(4);
                
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(20000);

        ferretMenu.add(settingsMenuItem);
        ferretMenu.add(updateMenuItem);
        ferretMenu.add(exitMenuItem);
        helpMenu.add(aboutMenuItem);
        helpMenu.add(faqMenuItem);
        helpMenu.add(contactMenuItem);
        ferretMenuBar.add(ferretMenu);
        ferretMenuBar.add(helpMenu);

        // Update window stuff
        updateFrame.setResizable(true);
        updateFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        updateFrame.getContentPane().add(updatePanel);
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));
        updatePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        updatePanel.add(updateLabel);
        updatePanel.add(Box.createRigidArea(new Dimension(500, 0)));
        updateLabel.setAlignmentX(CENTER_ALIGNMENT);
        updateProgressBar.setIndeterminate(true);
        updateDetailLabel.setAlignmentX(CENTER_ALIGNMENT);
        updateBarHolder.add(updateProgressBar);
        updatePanel.add(updateBarHolder);
        updatePanel.add(updateButtonHolder);
        updateButtonHolder.setLayout(new BoxLayout(updateButtonHolder, BoxLayout.X_AXIS));
        updateButtonHolder.add(updateOK);
        updateFrame.pack();

        // ----------------------------
        // About window stuff
        aboutFrame.getContentPane().add(aboutPanel);
        aboutFrame.setResizable(true);
        aboutFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        aboutPanel.add(ferretVersionLabel);
        aboutPanel.add(ferretDateLabel);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        ferretWebLabelAbout.setBackgroundColor(aboutPanel.getBackground());
        ferretWebLabelAbout.init();
        ferretWebLabelAbout.setAlignmentX(LEFT_ALIGNMENT);
        ferretWebLabelAbout.setMaximumSize(ferretWebLabelAbout.getPreferredSize());
        aboutPanel.add(ferretWebLabelAbout);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        ferretCitation.setAlignmentX(LEFT_ALIGNMENT);
        ferretCitation.setLineWrap(true);
        ferretCitation.setWrapStyleWord(true);
        ferretCitation.setBackground(aboutPanel.getBackground());
        ferretCitation.setMaximumSize(ferretCitation.getPreferredSize());
        aboutPanel.add(ferretCitation);
        aboutFrame.pack();

        // Contact window stuff
        contactFrame.getContentPane().add(contactPanel);
        contactFrame.setResizable(true);
        contactFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contactPeopleLabel.setAlignmentX(CENTER_ALIGNMENT);
        contactPanel.add(contactPeopleLabel);
        contactEmailLabel.setAlignmentX(CENTER_ALIGNMENT);
        contactEmailLabel.setBackground(contactPanel.getBackground());
        contactPanel.add(contactEmailLabel);
        contactFrame.pack();

        // Settings window stuff
        settingsFrame.getContentPane().add(settingsPanel);
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        settingsFrame.setResizable(true);
        settingsFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        vcfVersionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        settingsPanel.add(vcfVersionLabel);
        vcfRadioButtons.add(phase3Button);
        vcfRadioButtons.add(phase1Button);
        settingsPanel.add(phase3Button);
        settingsPanel.add(phase1Button);
        phase3Button.setSelected(true);

        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mafPanel.setAlignmentX(LEFT_ALIGNMENT);
        settingsPanel.add(mafOptionLabel);
        mafOptionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        settingsPanel.add(mafPanel);
        mafPanel.setLayout(new BoxLayout(mafPanel, BoxLayout.X_AXIS));
        mafPanel.add(mafThresholdLabel);
        mafText.setColumns(5);
        mafText.setMaximumSize(mafText.getPreferredSize());
        mafText.setValue(new Double(0));
        mafPanel.add(mafText);

        mafSlider.setMajorTickSpacing(1000);
        mafSlider.setPaintTicks(true);
        Hashtable labelTable = new Hashtable();
        labelTable.put(0, new JLabel("0.0"));
        labelTable.put(5000, new JLabel("0.5"));
        mafSlider.setLabelTable(labelTable);
        mafSlider.setValue(0);
        mafSlider.setPaintLabels(true);
        mafPanel.add(mafSlider);

        mafTextMax.setColumns(5);
        mafTextMax.setMaximumSize(mafTextMax.getPreferredSize());
        mafTextMax.setValue(0.5);
        mafPanel.add(mafTextMax);

        mafSliderMax.setMajorTickSpacing(1000);
        mafSliderMax.setPaintTicks(true);
        Hashtable labelTable2 = new Hashtable();
        labelTable2.put(0, new JLabel("0.0"));
        labelTable2.put(5000, new JLabel("0.5"));
        mafSliderMax.setLabelTable(labelTable2);
        mafSliderMax.setValue(5000);
        mafSliderMax.setPaintLabels(true);
        mafPanel.add(mafSliderMax);

        mafPanel.add(questionMarkMAFThreshold);
        questionMarkMAFThreshold.setToolTipText("<html>The MAF threshold is applied to the selected 1000 Genomes populations<br>"
                + "<u>Example:</u> For a MAF threshold of 0.05 (i.e. 5%), Ferret will only output variants with <br> a frequency >= 5% in the "
                + "selected populations.</html>");
        mafPanel.add(Box.createHorizontalGlue());
        mafESPPanel.setLayout(new BoxLayout(mafESPPanel, BoxLayout.X_AXIS));
        mafESPPanel.setAlignmentX(LEFT_ALIGNMENT);
        mafESPPanel.add(espMAF);
        mafESPPanel.add(questionMarkESPMAF);
        settingsPanel.add(mafESPPanel);
        questionMarkESPMAF.setToolTipText("<html> If checked, the MAF threshold is also applied to the Exome Sequencing Project populations."
                + "<br><u>Example:</u> For a MAF threshold of 0.05 (i.e. 5%), Ferret will only output variants with a frequency >= 5% <br> "
                + "in either the selected 1000 Genomes populations, or the European American population from the <br>"
                + "Exome Sequencing Project, or the African American population from the Exome Sequencing Project. </html>");
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        settingsPanel.add(filesLabel);
        filesLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        fileOutputButtons.add(allFilesButton);
        fileOutputButtons.add(freqFileButton);
        fileOutputButtons.add(vcfFileButton);
        settingsPanel.add(allFilesButton);
        settingsPanel.add(freqFileButton);
        settingsPanel.add(vcfFileButton);

        // setActionCommand
        allFilesButton.setActionCommand("allFilesButton");
        freqFileButton.setActionCommand("freqFileButton");
        vcfFileButton.setActionCommand("vcfFilesButton");

        allFilesButton.setSelected(true);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        settingsPanel.add(hgVersionLabel);
        hgVersionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        hgVersionButtons.add(version19Button);
        hgVersionButtons.add(version38Button);
        settingsPanel.add(version19Button);
        settingsPanel.add(version38Button);

        version19Button.setSelected(true);
        settingsButtonPanel.setAlignmentX(LEFT_ALIGNMENT);
        settingsButtonPanel.setLayout(new BoxLayout(settingsButtonPanel, BoxLayout.X_AXIS));
        settingsPanel.add(settingsButtonPanel);
        settingsButtonPanel.add(Box.createHorizontalGlue());
        settingsButtonPanel.add(settingsCancel);
        settingsButtonPanel.add(settingsOK);
        settingsFrame.pack();

        // Progress window stuff
        progressWindow.setResizable(true);
        progressWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JPanel progressPanel = new JPanel();
        progressWindow.getContentPane().add(progressPanel);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        progressPanel.add(Box.createVerticalGlue());
        progressPanel.add(progressText);
        progressText.setAlignmentX(Container.CENTER_ALIGNMENT);
        progressPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        progressPanel.add(progressBar);
        progressPanel.add(Box.createVerticalGlue());
        progressWindow.pack();

        snpFerret.setJMenuBar(ferretMenuBar);
        snpFerret.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        snpFerret.setResizable(true);
        snpFerret.getContentPane().add(scrollBigPanel);
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));

        bigPanel.add(inputSelect);

        // Method select tabs
        inputSelect.addTab("Locus", chromosomeSelectPanel);
        inputSelect.addTab("Gene", geneSelectPanel);
        inputSelect.addTab("Variant", snpSelectPanel);

        // Source selection panel for SNP
        snpSourceButtonGroup.add(snpNCBIRadioButton);
        snpSourceButtonGroup.add(snpV37RadioButton);
        snpNCBIRadioButton.setSelected(true);

        // SNP input panel
        snpInputPanel.setLayout(new BoxLayout(snpInputPanel, BoxLayout.X_AXIS));
        snpInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        snpInputPanel.add(snpTextLabel);
        snpInputPanel.add(questionMarkSNPInput);
        questionMarkSNPInput.setToolTipText("<html>Input the rs number without the letters 'rs'<br><u>Example:</u> 73885319 for rs73885319<br><br>"
                + "To input multiple variants at once, enter a list of variant IDs separated by a comma, or input a file.<br><u>Example:</u> 73885319, 2395029 for rs73885319 and rs2395029</html>");
        snpInputPanel.add(snpTextField);
        snpTextField.setMaximumSize(snpTextField.getPreferredSize());
        snpInputPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        snpInputPanel.add(snpOR);
        snpInputPanel.add(Box.createRigidArea(new Dimension(15, 0)));

        //Variant tab browse
        snpInputPanel.add(snpFileBrowseButton);
        snpFileBrowseButton.setPreferredSize(new Dimension(100, 30));
        snpInputPanel.add(snpFileLocation);
        snpInputPanel.add(snpFileClearButton);
        snpInputPanel.add(questionMarkSNPFileInput);
        questionMarkSNPFileInput.setToolTipText("<html>You can load a file in any of the following formats: <br> - a comma-delimited .csv file (example: variant.csv containing 73885319, 2395029) <br>"
                + " - a tab-delimited .tab or .tsv file (example: variant.tab containing 73885319 &nbsp&nbsp&nbsp&nbsp 2395029) <br>"
                + " - a space-delimited .txt file (example: variant.txt containing 73885319 2395029)"
                + "<br><br> A carriage return can also be used as a delimiter for all above file types.</html>");
        snpFileClearButton.setEnabled(false);
        snpFileClearButton.setPreferredSize(new Dimension(100, 30));
        snpWindowPanel.setLayout(new BoxLayout(snpWindowPanel, BoxLayout.X_AXIS));
        snpWindowPanel.add(snpWindowCheckBox);
        snpWindowPanel.add(snpWindowField);
        snpWindowField.setEnabled(false);
        snpWindowPanel.add(snpWindowBP);
        snpWindowField.setMaximumSize(snpWindowField.getPreferredSize());
        snpWindowPanel.setMaximumSize(snpWindowPanel.getPreferredSize());
        snpESPOptionPanel.setLayout(new BoxLayout(snpESPOptionPanel, BoxLayout.X_AXIS));
        snpESPOptionPanel.add(snpESPCheckBox);
        snpESPOptionPanel.setMaximumSize(snpESPOptionPanel.getPreferredSize());

        // SNP selection method
        snpSelectPanel.setLayout(new BoxLayout(snpSelectPanel, BoxLayout.Y_AXIS));
        snpSelectPanel.add(snpInputPanel);
        snpSelectPanel.add(snpWindowPanel);
        snpSelectPanel.add(snpESPOptionPanel);

        // Gene selection method ----------------------------------------------------------
        // Create the button groups
        geneSourceButtonGroup.add(geneNCBIRadioButton);
        geneSourceButtonGroup.add(geneV37RadioButton);
        geneNCBIRadioButton.setSelected(true);
        geneInputButtonGroup.add(geneNameRadioButton);
        geneInputButtonGroup.add(geneIDRadioButton);

        // Create the gene input panel
        geneInputPanel.setLayout(new BoxLayout(geneInputPanel, BoxLayout.X_AXIS));
        geneInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        geneInputPanel.add(geneNameLabel);
        geneInputPanel.add(questionMarkGeneInput);
        questionMarkGeneInput.setToolTipText("<html>Input a gene name or a gene ID, and check the corresponding box (Name vs ID) <br>"
                + "<u>Example:</u> CCR5 for gene name or 1234 for gene ID <br><br>"
                + "To input multiple genes at once, enter a list of genes separated by a comma or input a file. <br>"
                + "<u>Example:</u> CCR5, HCP5 for gene name input or 1234, 10866 for gene ID input.</html>");
        geneInputPanel.add(geneNameField);
        geneNameField.setMaximumSize(geneNameField.getPreferredSize());
        geneInputPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        geneInputPanel.add(geneOR);
        geneInputPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        geneInputPanel.add(geneFileBrowseButton);
        geneFileBrowseButton.setPreferredSize(new Dimension(100, 30));
        geneInputPanel.add(geneFileLocation);
        geneFileClearButton.setPreferredSize(new Dimension(100, 30));
        geneInputPanel.add(geneFileClearButton);
        geneInputPanel.add(questionMarkGeneFileInput);
        questionMarkGeneFileInput.setToolTipText("<html>You can load a file in any of the following formats for either gene names or gene IDs: <br> "
                + " - a comma-delimited .csv file (example: gene.csv containing CCR5, HCP5) <br>"
                + " - a tab-delimited .tab or .tsv file (example: gene.tab containing CCR5 &nbsp&nbsp&nbsp&nbsp HCP5) <br>"
                + " - a space-delimited .txt file (example: gene.txt containing CCR5 HCP5)"
                + "<br><br> A carriage return can also be used as a delimiter for all above file types.</html>");
        geneFileClearButton.setEnabled(false);

        //the option gene borders
        geneWindowPanel.setLayout(new BoxLayout(geneWindowPanel, BoxLayout.X_AXIS));
        geneWindowPanel.add(geneWindowCheckBox);
        geneWindowPanel.add(geneWindowField);
        geneWindowField.setEnabled(false);
        geneWindowPanel.add(geneWindowBP);
        geneWindowField.setMaximumSize(geneWindowField.getPreferredSize());
        geneWindowPanel.setMaximumSize(geneWindowPanel.getPreferredSize());
        // Create the NCBI/Frozen look-up panel
        geneSelectOptionsPanel.setLayout(new BoxLayout(geneSelectOptionsPanel, BoxLayout.X_AXIS));
        geneSelectOptionsPanel.add(geneInputType);
        geneSelectOptionsPanel.add(geneNameRadioButton);
        geneSelectOptionsPanel.add(geneIDRadioButton);
        geneNameRadioButton.setSelected(true);
        geneESPOptionPanel.setLayout(new BoxLayout(geneESPOptionPanel, BoxLayout.X_AXIS));
        geneESPOptionPanel.add(geneESPCheckBox);

        // Finally add the panels to the main panel
        geneSelectPanel.add(Box.createVerticalGlue());
        geneSelectPanel.setLayout(new BoxLayout(geneSelectPanel, BoxLayout.Y_AXIS));
        geneSelectPanel.add(geneInputPanel);
        geneSelectPanel.add(geneSelectOptionsPanel);
        geneSelectPanel.add(geneWindowPanel);
        geneSelectPanel.add(geneESPOptionPanel);
        geneSelectPanel.add(Box.createVerticalGlue());
        // end gene selection method ----------------------------------------------------------

        // Chromosome region selection method
        chromosomeInputPanel.setLayout(new BoxLayout(chromosomeInputPanel, BoxLayout.X_AXIS));
        chromosomeInputPanel.add(selectChrRegionLabel);
        chromosomeInputPanel.add(questionMarkLocusInput);
        questionMarkLocusInput.setToolTipText("<html>Input hg19 human genome version coordinates in bp. <br><u> Example for CCR5:</u> Chromosome: 3 Start: 46411633 End: 46417697</html>");
        chromosomeInputPanel.add(chrLabel);
        chromosomeInputPanel.add(chrList);
        chromosomeInputPanel.add(startLabel);
        chromosomeInputPanel.add(startPosTextField);
        startPosTextField.setMaximumSize(startPosTextField.getPreferredSize());
        chromosomeInputPanel.add(endLabel);
        chromosomeInputPanel.add(endPosTextField);
        endPosTextField.setMaximumSize(endPosTextField.getPreferredSize());

        chromosomeESPOptionPanel.add(chrESPCheckBox);

        chromosomeSelectPanel.setLayout(new BoxLayout(chromosomeSelectPanel, BoxLayout.Y_AXIS));
        chromosomeSelectPanel.add(Box.createVerticalGlue());
        chromosomeSelectPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 80));
        chromosomeSelectPanel.add(chromosomeInputPanel);
        chromosomeSelectPanel.add(chromosomeESPOptionPanel);
        chromosomeSelectPanel.add(Box.createVerticalGlue());
        // end chromosome selection panel (now called Locus) ----------------------------------

        inputSelect.setMaximumSize(inputSelect.getPreferredSize());

        bigPanel.add(kgPopulationPanel);

        kgPopulationPanel.setLayout(new GridLayout(2, 3));
        kgPopulationPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
        kgPopulationPanel.add(allracesPanel);
        allracesPanel.setLayout(new GridLayout(9, 1));
        allracessub[0] = new JCheckBox("ALL All Populations (n=2,504)");

        allracesLabel.setFont(new Font("Serif", Font.BOLD, 20));
        allracesPanel.add(allracesLabel);
        allracesPanel.add(allracessub[0]);

        kgPopulationPanel.add(afrPanel);
        afrPanel.setLayout(new GridLayout(9, 1));
        afrsub[0] = new JCheckBox("AFR All Africans (n=661)");
        afrsub[1] = new JCheckBox("ACB African Caribbean (n=96)");
        afrsub[2] = new JCheckBox("ASW African American (n=61)");
        afrsub[3] = new JCheckBox("ESN Esan (n=99)");
        afrsub[4] = new JCheckBox("GWD Gambian (n=113)");
        afrsub[5] = new JCheckBox("LWK Luhya (n=99)");
        afrsub[6] = new JCheckBox("MSL Mende (n=85)");
        afrsub[7] = new JCheckBox("YRI Yoruba (n=108)");
        afrLabel.setFont(new Font("Serif", Font.BOLD, 20));
        afrPanel.add(afrLabel);
        for (JCheckBox afrsub1 : afrsub) {
            afrPanel.add(afrsub1);
            if (afrsub1.getText().contains("n=0")) {
                afrsub1.setEnabled(false);
            }
        }

        kgPopulationPanel.add(amrPanel);
        amrPanel.setLayout(new GridLayout(9, 1));
        amrsub[0] = new JCheckBox("AMR All Americans (n=347)");
        amrsub[1] = new JCheckBox("CLM Colombian (n=94)");
        amrsub[2] = new JCheckBox("MXL Mexican American (n=64)");
        amrsub[3] = new JCheckBox("PEL Peruvian (n=85)");
        amrsub[4] = new JCheckBox("PUR Puerto Rican (n=104)");
        amrLabel.setFont(new Font("Serif", Font.BOLD, 20));
        amrPanel.add(amrLabel);
        for (int i = 0; i < amrsub.length; i++) {
            amrPanel.add(amrsub[i]);
            if (amrsub[i].getText().contains("n=0")) {
                amrsub[i].setEnabled(false);
            }
        }

        kgPopulationPanel.add(asnPanel);
        asnPanel.setLayout(new GridLayout(9, 1));
        asnsub[0] = new JCheckBox("EAS All East Asians (n=504)");
        asnsub[1] = new JCheckBox("CDX Dai Chinese (n=93)");
        asnsub[2] = new JCheckBox("CHB Han Chinese (n=103)");
        asnsub[3] = new JCheckBox("CHS Southern Han Chinese (n=105)");
        asnsub[4] = new JCheckBox("JPT Japanese (n=104)");
        asnsub[5] = new JCheckBox("KHV Kinh Vietnamese (n=99)");
        asnsub[6] = new JCheckBox("CHD Denver Chinese (n=0)");
        asnLabel.setFont(new Font("Serif", Font.BOLD, 20));
        asnPanel.add(asnLabel);
        for (int i = 0; i < asnsub.length; i++) {
            asnPanel.add(asnsub[i]);
            if (asnsub[i].getText().contains("n=0")) {
                asnsub[i].setEnabled(false);
            }
        }

        kgPopulationPanel.add(eurPanel);
        eurPanel.setLayout(new GridLayout(9, 1));
        eursub[0] = new JCheckBox("EUR All Europeans (n=503)");
        eursub[1] = new JCheckBox("CEU CEPH (n=99)");
        eursub[2] = new JCheckBox("GBR British (n=91)");
        eursub[3] = new JCheckBox("FIN Finnish (n=99)");
        eursub[4] = new JCheckBox("IBS Spanish (n=107)");
        eursub[5] = new JCheckBox("TSI Tuscan (n=107)");
        eurLabel.setFont(new Font("Serif", Font.BOLD, 20));
        eurPanel.add(eurLabel);
        for (int i = 0; i < eursub.length; i++) {
            eurPanel.add(eursub[i]);
            if (eursub[i].getText().contains("n=0")) {
                eursub[i].setEnabled(false);
            }
        }

        kgPopulationPanel.add(sanPanel);
        sanPanel.setLayout(new GridLayout(9, 1));
        sansub[0] = new JCheckBox("SAS All South Asians (n=489)");
        sansub[1] = new JCheckBox("BEB Bengali (n=86)");
        sansub[2] = new JCheckBox("GIH Gujarati Indian (n=103)");
        sansub[3] = new JCheckBox("ITU Indian Telugu (n=102)");
        sansub[4] = new JCheckBox("PJL Punjabi (n=96)");
        sansub[5] = new JCheckBox("STU Sri Lankan Tamil (n=102)");
        sanLabel.setFont(new Font("Serif", Font.BOLD, 20));
        sanPanel.add(sanLabel);
        for (int i = 0; i < sansub.length; i++) {
            sanPanel.add(sansub[i]);
            if (sansub[i].getText().contains("n=0")) {
                sansub[i].setEnabled(false);
            }
        }

        bigPanel.add(fileChoosePanel);
        fileChoosePanel.add(browseButton);
        browseButton.setPreferredSize(new Dimension(100, 30));
        fileChoosePanel.add(fileLocation);
        bigPanel.add(goPanel);
        goPanel.add(goButton);
        goPanel.add(goHaplo);
        goButton.setPreferredSize(new Dimension(320, 60));
        goHaplo.setPreferredSize(new Dimension(320, 60));
        goPanel.setBackground(Color.gray);
        bigPanel.add(Box.createVerticalGlue());
        snpFerret.pack();
        snpFerret.setVisible(true);

        //SetActionCommands
        phase3Button.setActionCommand("phase3Button");
        phase1Button.setActionCommand("phase1Button");
        version19Button.setActionCommand("version19Button");
        version38Button.setActionCommand("version38Button");
        settingsCancel.setActionCommand("settingsCancel");
        settingsOK.setActionCommand("settingsOK");
        settingsMenuItem.setActionCommand("settingsMenuItem");
        exitMenuItem.setActionCommand("exitMenuItem");
        updateMenuItem.setActionCommand("updateMenuItem");
        aboutMenuItem.setActionCommand("aboutMenuItem");
        faqMenuItem.setActionCommand("faqMenuItem");
        contactMenuItem.setActionCommand("contactMenuItem");
        snpFileBrowseButton.setActionCommand("snpFileBrowseButton");
        snpFileClearButton.setActionCommand("snpFileClearButton");
        snpWindowCheckBox.setActionCommand("snpWindowCheckBox");
        geneFileBrowseButton.setActionCommand("geneFileBrowseButton");
        geneFileClearButton.setActionCommand("geneFileClearButton");
        geneWindowCheckBox.setActionCommand("geneWindowCheckBox");
        allracessub[0].setActionCommand("allracessub[0]");
        afrsub[0].setActionCommand("afrsub[0]");
        amrsub[0].setActionCommand("amrsub[0]");
        asnsub[0].setActionCommand("asnsub[0]");
        eursub[0].setActionCommand("eursub[0]");
        sansub[0].setActionCommand("sansub[0]");
        browseButton.setActionCommand("browseButton");
        goButton.setActionCommand("goButton");
        goHaplo.setActionCommand("goHaplo");
        mafText.setName("mafText");
        mafTextMax.setName("mafTextMax");
        mafSlider.setName("mafSlider");
        mafSliderMax.setName("mafSliderMax");

    }

    public void checkBoxReset() {
        if (allracessub[0].isSelected()) {
            setAfrican(0, false);
            setEuropean(0, false);
            setAmerican(0, false);
            setSouthAsian(0, false);
            setAsian(0, false);
        } else {
            if (afrsub[0].isSelected()) {
                setAfrican(1, false);
            }
            if (eursub[0].isSelected()) {
                setEuropean(1, false);
            }
            if (amrsub[0].isSelected()) {
                setAmerican(1, false);
            }
            if (sansub[0].isSelected()) {
                setSouthAsian(1, false);
            }
            if (asnsub[0].isSelected()) {
                setAsian(1, false);
            }
        }
    }

    public void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container) component, enable);
            }
        }
    }

    public void setAfrican(int start, boolean state) {
        for (int i = start; i < afrsub.length; i++) {
            if (!afrsub[i].getText().contains("n=0)")) {
                afrsub[i].setEnabled(state);
            }
            afrsub[i].setSelected(false);
            afrsub[i].updateUI();
        }
    }

    public void setAsian(int start, boolean state) {
        for (int i = start; i < asnsub.length; i++) {
            if (!asnsub[i].getText().contains("n=0)")) {
                asnsub[i].setEnabled(state);
            }
            asnsub[i].setSelected(false);
            asnPanel.updateUI();
        }
    }

    public void setEuropean(int start, boolean state) {
        for (int i = start; i < eursub.length; i++) {
            if (!eursub[i].getText().contains("n=0)")) {
                eursub[i].setEnabled(state);
            }
            eursub[i].setSelected(false);
            eursub[i].updateUI();
        }
    }

    public void setAmerican(int start, boolean state) {
        for (int i = start; i < amrsub.length; i++) {
            if (!amrsub[i].getText().contains("n=0)")) {
                amrsub[i].setEnabled(state);
            }
            amrsub[i].setSelected(false);
            amrPanel.updateUI();
        }
    }

    public void setSouthAsian(int start, boolean state) {
        for (int i = start; i < sansub.length; i++) {
            if (!sansub[i].getText().contains("n=0)")) {
                sansub[i].setEnabled(state);
            }
            sansub[i].setSelected(false);
            sansub[i].updateUI();
        }
    }

    public void setPhase1() {

        allracessub[0].setText("ALL All Populations (n=1,092)");

        sansub[0].setText("SAS All South Asians (n=0)");
        sansub[1].setText("BEB Bengali (n=0)");
        sansub[2].setText("GIH Gujarati Indian (n=0)");
        sansub[3].setText("ITU Indian Telugu (n=0)");
        sansub[4].setText("PJL Punjabi (n=0)");
        sansub[5].setText("STU Sri Lankan Tamil (n=0)");
        if (!allracessub[0].isSelected() && !sansub[0].isSelected()) {
            for (int i = 0; i < sansub.length; i++) {
                if (sansub[i].getText().contains("n=0)")) {
                    sansub[i].setEnabled(false);
                    if (sansub[i].isSelected()) {
                        sansub[i].setSelected(false);
                    }
                } else {
                    sansub[i].setEnabled(true);
                }
            }
        }

        eursub[0].setText("EUR All Europeans (n=379)");
        eursub[1].setText("CEU CEPH (n=85)");
        eursub[2].setText("GBR British (n=89)");
        eursub[3].setText("FIN Finnish (n=93)");
        eursub[4].setText("IBS Spanish (n=14)");
        eursub[5].setText("TSI Tuscan (n=98)");
        if (!allracessub[0].isSelected() && !eursub[0].isSelected()) {
            for (int i = 0; i < eursub.length; i++) {
                if (eursub[i].getText().contains("n=0)")) {
                    eursub[i].setEnabled(false);
                    if (eursub[i].isSelected()) {
                        eursub[i].setSelected(false);
                    }
                } else {
                    eursub[i].setEnabled(true);
                }
            }
        }

        asnsub[0].setText("EAS All East Asians (n=286)");
        asnsub[1].setText("CDX Dai Chinese (n=0)");
        asnsub[2].setText("CHB Han Chinese (n=97)");
        asnsub[3].setText("CHS Southern Han Chinese (n=100)");
        asnsub[4].setText("JPT Japanese (n=89)");
        asnsub[5].setText("KHV Kinh Vietnamese (n=0)");
        asnsub[6].setText("CHD Denver Chinese (n=0)");
        if (!allracessub[0].isSelected() && !asnsub[0].isSelected()) {
            for (int i = 0; i < asnsub.length; i++) {
                if (asnsub[i].getText().contains("n=0)")) {
                    asnsub[i].setEnabled(false);
                    if (asnsub[i].isSelected()) {
                        asnsub[i].setSelected(false);
                    }
                } else {
                    asnsub[i].setEnabled(true);
                }
            }
        }

        amrsub[0].setText("AMR All Americans (n=181)");
        amrsub[1].setText("CLM Colombian (n=60)");
        amrsub[2].setText("MXL Mexican American (n=66)");
        amrsub[3].setText("PEL Peruvian (n=0)");
        amrsub[4].setText("PUR Puerto Rican (n=55)");
        if (!allracessub[0].isSelected() && !amrsub[0].isSelected()) {
            for (int i = 0; i < amrsub.length; i++) {
                if (amrsub[i].getText().contains("n=0)")) {
                    amrsub[i].setEnabled(false);
                    if (amrsub[i].isSelected()) {
                        amrsub[i].setSelected(false);
                    }
                } else {
                    amrsub[i].setEnabled(true);
                }
            }
        }

        afrsub[0].setText("AFR All Africans (n=246)");
        afrsub[1].setText("ACB African Caribbean (n=0)");
        afrsub[2].setText("ASW African American (n=61)");
        afrsub[3].setText("ESN Esan (n=0)");
        afrsub[4].setText("GWD Gambian (n=0)");
        afrsub[5].setText("LWK Luhya (n=97)");
        afrsub[6].setText("MSL Mende (n=0)");
        afrsub[7].setText("YRI Yoruba (n=88)");
        if (!allracessub[0].isSelected() && !afrsub[0].isSelected()) {
            for (int i = 0; i < afrsub.length; i++) {
                if (afrsub[i].getText().contains("n=0)")) {
                    afrsub[i].setEnabled(false);
                    if (afrsub[i].isSelected()) {
                        afrsub[i].setSelected(false);
                    }
                } else {
                    afrsub[i].setEnabled(true);
                }
            }
        }
    }

    public void setPhase3() {
        allracessub[0].setText("ALL All Populations (n=2,504)");

        sansub[0].setText("SAS All South Asians (n=489)");
        sansub[1].setText("BEB Bengali (n=86)");
        sansub[2].setText("GIH Gujarati Indian (n=103)");
        sansub[3].setText("ITU Indian Telugu (n=102)");
        sansub[4].setText("PJL Punjabi (n=96)");
        sansub[5].setText("STU Sri Lankan Tamil (n=102)");
        if (!allracessub[0].isSelected() && !sansub[0].isSelected()) {
            for (int i = 0; i < sansub.length; i++) {
                if (sansub[i].getText().contains("n=0)")) {
                    sansub[i].setEnabled(false);
                    if (sansub[i].isSelected()) {
                        sansub[i].setSelected(false);
                    }
                } else {
                    sansub[i].setEnabled(true);
                }
            }
        }

        eursub[0].setText("EUR All Europeans (n=503)");
        eursub[1].setText("CEU CEPH (n=99)");
        eursub[2].setText("GBR British (n=91)");
        eursub[3].setText("FIN Finnish (n=99)");
        eursub[4].setText("IBS Spanish (n=107)");
        eursub[5].setText("TSI Tuscan (n=107)");
        if (!allracessub[0].isSelected() && !eursub[0].isSelected()) {
            for (int i = 0; i < eursub.length; i++) {
                if (eursub[i].getText().contains("n=0)")) {
                    eursub[i].setEnabled(false);
                    if (eursub[i].isSelected()) {
                        eursub[i].setSelected(false);
                    }
                } else {
                    eursub[i].setEnabled(true);
                }
            }
        }

        asnsub[0].setText("EAS All East Asians (n=504)");
        asnsub[1].setText("CDX Dai Chinese (n=93)");
        asnsub[2].setText("CHB Han Chinese (n=103)");
        asnsub[3].setText("CHS Southern Han Chinese (n=105)");
        asnsub[4].setText("JPT Japanese (n=104)");
        asnsub[5].setText("KHV Kinh Vietnamese (n=99)");
        asnsub[6].setText("CHD Denver Chinese (n=0)");
        if (!allracessub[0].isSelected() && !asnsub[0].isSelected()) {
            for (int i = 0; i < asnsub.length; i++) {
                if (asnsub[i].getText().contains("n=0)")) {
                    asnsub[i].setEnabled(false);
                    if (asnsub[i].isSelected()) {
                        asnsub[i].setSelected(false);
                    }
                } else {
                    asnsub[i].setEnabled(true);
                }
            }
        }

        amrsub[0].setText("AMR All Americans (n=347)");
        amrsub[1].setText("CLM Colombian (n=94)");
        amrsub[2].setText("MXL Mexican American (n=64)");
        amrsub[3].setText("PEL Peruvian (n=85)");
        amrsub[4].setText("PUR Puerto Rican (n=104)");
        if (!allracessub[0].isSelected() && !amrsub[0].isSelected()) {
            for (int i = 0; i < amrsub.length; i++) {
                if (amrsub[i].getText().contains("n=0)")) {
                    amrsub[i].setEnabled(false);
                    if (amrsub[i].isSelected()) {
                        amrsub[i].setSelected(false);
                    }
                } else {
                    amrsub[i].setEnabled(true);
                }
            }
        }

        afrsub[0].setText("AFR All Africans (n=661)");
        afrsub[1].setText("ACB African Caribbean (n=96)");
        afrsub[2].setText("ASW African American (n=61)");
        afrsub[3].setText("ESN Esan (n=99)");
        afrsub[4].setText("GWD Gambian (n=113)");
        afrsub[5].setText("LWK Luhya (n=99)");
        afrsub[6].setText("MSL Mende (n=85)");
        afrsub[7].setText("YRI Yoruba (n=108)");
        if (!allracessub[0].isSelected() && !afrsub[0].isSelected()) {
            for (int i = 0; i < afrsub.length; i++) {
                if (afrsub[i].getText().contains("n=0)")) {
                    afrsub[i].setEnabled(false);
                    if (afrsub[i].isSelected()) {
                        afrsub[i].setSelected(false);
                    }
                } else {
                    afrsub[i].setEnabled(true);
                }
            }
        }
    }

    //Listeners for view->controller
    public void updateListener(ActionListener a) {
        updateOK.addActionListener(a);
    }

    public void settingsListener(ActionListener a) {
        phase3Button.addActionListener(a);
        phase1Button.addActionListener(a);
        allFilesButton.addActionListener(a);
        freqFileButton.addActionListener(a);
        vcfFileButton.addActionListener(a);
        version19Button.addActionListener(a);
        version38Button.addActionListener(a);
        settingsCancel.addActionListener(a);
        settingsOK.addActionListener(a);
    }

    public void settingsChangeListener(ChangeListener c) {
        mafSlider.addChangeListener(c);
        mafSliderMax.addChangeListener(c);
    }

    public void settingsPropertyChangeListener(PropertyChangeListener p) {
        mafText.addPropertyChangeListener(p);
        mafTextMax.addPropertyChangeListener(p);

    }

    public void menuBarListener(ActionListener a) {
        settingsMenuItem.addActionListener(a);
        exitMenuItem.addActionListener(a);
        updateMenuItem.addActionListener(a);
        aboutMenuItem.addActionListener(a);
        faqMenuItem.addActionListener(a);
        contactMenuItem.addActionListener(a);
    }

    public void mainPanelListener(ActionListener a) {
        snpFileBrowseButton.addActionListener(a);
        snpFileClearButton.addActionListener(a);
        snpWindowCheckBox.addActionListener(a);
        geneFileBrowseButton.addActionListener(a);
        geneFileClearButton.addActionListener(a);
        geneWindowCheckBox.addActionListener(a);
        allracessub[0].addActionListener(a);
        afrsub[0].addActionListener(a);
        amrsub[0].addActionListener(a);
        asnsub[0].addActionListener(a);
        eursub[0].addActionListener(a);
        sansub[0].addActionListener(a);
        browseButton.addActionListener(a);
    }

    public void downloadTheDataListener(ActionListener a) {
        goButton.addActionListener(a);
        goHaplo.addActionListener(a);
    }

}
