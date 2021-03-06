/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecn.ferretmvc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.ecn.ferretmvc.view.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Eric
 */
public class SettingsController implements ActionListener, ChangeListener, PropertyChangeListener {

    private final GUI gui;

    public SettingsController(GUI gui) {
        this.gui = gui;
        gui.settingsListener(this);
        gui.settingsChangeListener(this);
        gui.settingsPropertyChangeListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Retriving the ActionCommand ie. the String associated with the button clicked
        String viewAction = e.getActionCommand();
        switch (viewAction) {
            case "phase3Button":
                gui.getVersion38Button().setEnabled(true);
                break;
            case "phase1Button":
                gui.getVersion19Button().setSelected(true);
                gui.getVersion38Button().setEnabled(false);
                break;
            case "allFilesButton":
                if (!(gui.getVersion38Button().isSelected())) {
                    gui.getEspMAF().setEnabled(true);
                }
                gui.getno_annot().setEnabled(true);
                gui.getdefault_annot().setEnabled(true);
                gui.getadvanced_annot().setEnabled(true);
                gui.getHtmlFileButton().setEnabled(true);
                gui.getHtmlFileButton().setSelected(true);
                gui.getdownloadHaploButton().setEnabled(true);
                break;
            case "freqFileButton":
                if (!(gui.getVersion38Button().isSelected())) {
                    gui.getEspMAF().setEnabled(true);
                }
                gui.getno_annot().setEnabled(true);
                gui.getdefault_annot().setEnabled(true);
                gui.getadvanced_annot().setEnabled(true);
                gui.getHtmlFileButton().setEnabled(true);
                gui.getHtmlFileButton().setSelected(true);
                gui.getdownloadHaploButton().setEnabled(true);
                break;
            case "vcfFilesButton":
                if (gui.getEspMAF().isSelected()) {
                    gui.getEspMAF().setSelected(false);
                }
                if (gui.getHtmlFileButton().isSelected()) {
                    gui.getHtmlFileButton().setSelected(false);
                };
                gui.getEspMAF().setEnabled(false);
                gui.getno_annot().setEnabled(false);
                gui.getdefault_annot().setEnabled(false);
                gui.getadvanced_annot().setEnabled(false);
                gui.getHtmlFileButton().setEnabled(false);
                gui.getdownloadHaploButton().setEnabled(true);
                break;
            case "version19Button":
                gui.getPhase1Button().setEnabled(true);
                if (!(gui.getVcfFileButton().isSelected())) {
                    gui.getEspMAF().setEnabled(true);
                }
                gui.getdownloadHaploButton().setEnabled(true);
                break;
            case "version38Button":
                gui.getPhase3Button().setSelected(true);
                gui.getPhase1Button().setEnabled(false);
                if (gui.getEspMAF().isSelected()) {
                    gui.getEspMAF().setSelected(false);
                }
                gui.getEspMAF().setEnabled(false);
                gui.getdownloadHaploButton().setEnabled(true);
                break;
                
            case "settingsCancel":
                gui.getPhase3Button().setSelected(gui.getCurrVersion()[0] == GUI.version1KG.THREE);
                gui.getPhase3Button().setEnabled(true);
                gui.getPhase1Button().setEnabled(gui.getDefaultHG()[0]);
                gui.getPhase1Button().setSelected(gui.getCurrVersion()[0] == GUI.version1KG.ONE);
                gui.getMafText().setValue(gui.getMafThreshold()[0]);
                gui.getMafTextMax().setValue(gui.getMafThresholdMax()[0]);
                gui.getMafSlider().setValue((int) (gui.getMafThreshold()[0] * 10000));
                gui.getMafSlider().setUpperValue((int) (gui.getMafThresholdMax()[0] * 10000));

                if (gui.getCurrFileOut()[0] == GUI.fileOutput.VCF || !(gui.getDefaultHG()[0])) {
                    gui.getEspMAF().setEnabled(false);
                } else {
                    gui.getEspMAF().setEnabled(true);
                }
                gui.getEspMAF().setSelected(gui.getEspMAFBoolean()[0]);
                gui.getAllFilesButton().setSelected(gui.getCurrFileOut()[0] == GUI.fileOutput.ALL);
                gui.getFreqFileButton().setSelected(gui.getCurrFileOut()[0] == GUI.fileOutput.FRQ);
                gui.getHtmlFileButton().setSelected(gui.isHtmlFile());
                gui.getdownloadHaploButton().setSelected(gui.isdownloadHaplo());
                gui.getVcfFileButton().setSelected(gui.getCurrFileOut()[0] == GUI.fileOutput.VCF);
                gui.getVcfFileButton().setEnabled(true);
                gui.getno_annot().setSelected(gui.getCurrAnnot()[0] == GUI.annotOutput.NO);
                gui.getdefault_annot().setSelected(gui.getCurrAnnot()[0] == GUI.annotOutput.DEF);
                gui.getadvanced_annot().setSelected(gui.getCurrAnnot()[0] == GUI.annotOutput.ADV);
                gui.getno_annot().setEnabled(true);
                gui.getdefault_annot().setEnabled(true);
                gui.getadvanced_annot().setEnabled(true);
                gui.getVersion19Button().setEnabled(true);
                gui.getVersion19Button().setSelected(gui.getDefaultHG()[0]);
                gui.getVersion38Button().setSelected(!(gui.getDefaultHG()[0]));
                gui.getVersion38Button().setEnabled(gui.getCurrVersion()[0] == GUI.version1KG.THREE);
                gui.getSettingsFrame().dispose();
                break;
            case "settingsOK":
                if (gui.getPhase3Button().isSelected()) {
                    gui.getCurrVersion()[0] = GUI.version1KG.THREE;
                    gui.setPhase3();
                } else if (gui.getPhase1Button().isSelected()) {
                    gui.getCurrVersion()[0] = GUI.version1KG.ONE;
                    gui.setPhase1();
                } else {
                    gui.getCurrVersion()[0] = GUI.version1KG.ZERO;
                }

                if (gui.getAllFilesButton().isSelected()) {
                    gui.getCurrFileOut()[0] = GUI.fileOutput.ALL;
                } else if (gui.getFreqFileButton().isSelected()) {
                    gui.getCurrFileOut()[0] = GUI.fileOutput.FRQ;
                } else if (gui.getVcfFileButton().isSelected()) {
                    gui.getCurrFileOut()[0] = GUI.fileOutput.VCF;
                }
                if (gui.getHtmlFileButton().isSelected()) {
                    gui.setHtmlFile(true);
                } else {
                    gui.setHtmlFile(false);
                }
                if (gui.getdownloadHaploButton().isSelected()) {
                    gui.setdownloadHaplo(true);
                } else {
                    gui.setdownloadHaplo(false);
                }
                if (gui.getno_annot().isSelected()) {
                    gui.getCurrAnnot()[0] = GUI.annotOutput.NO;
                } else if (gui.getdefault_annot().isSelected()) {
                	gui.getCurrAnnot()[0] = GUI.annotOutput.DEF;
                } else if (gui.getadvanced_annot().isSelected()) {
                	gui.getCurrAnnot()[0] = GUI.annotOutput.ADV;
                }

                gui.getDefaultHG()[0] = gui.getVersion19Button().isSelected();
                gui.getEspMAFBoolean()[0] = gui.getEspMAF().isSelected();
                // Requesting either GRCh38 or VCF only prevents ESP button from working
                if ((gui.getVersion38Button().isSelected() || gui.getVcfFileButton().isSelected()) && (gui.getSnpESPCheckBox().isSelected() || gui.getGeneESPCheckBox().isSelected() || gui.getChrESPCheckBox().isSelected())) {
                    gui.getSnpESPCheckBox().setSelected(false);
                    gui.getGeneESPCheckBox().setSelected(false);
                    gui.getChrESPCheckBox().setSelected(false);
                }
                if (gui.getVersion38Button().isSelected()) {
                    gui.getQuestionMarkLocusInput().setToolTipText("<html>Input hg38 human genome version coordinates in bp. <br><u> Example for CCR5:</u> Chromosome: 3 Start: 46370142 End: 46376206</html>");
                }
                if (gui.getVersion19Button().isSelected()) {
                    gui.getQuestionMarkLocusInput().setToolTipText("<html>Input hg19 human genome version coordinates in bp. <br><u> Example for CCR5:</u> Chromosome: 3 Start: 46411633 End: 46417697</html>");
                }
                gui.getSnpESPCheckBox().setEnabled(gui.getVersion19Button().isSelected() && !(gui.getVcfFileButton().isSelected()));
                gui.getGeneESPCheckBox().setEnabled(gui.getVersion19Button().isSelected() && !(gui.getVcfFileButton().isSelected()));
                gui.getChrESPCheckBox().setEnabled(gui.getVersion19Button().isSelected() && !(gui.getVcfFileButton().isSelected()));

                gui.getMafThreshold()[0] = ((Number) gui.getMafText().getValue()).doubleValue();
                gui.getMafThresholdMax()[0] = ((Number) gui.getMafTextMax().getValue()).doubleValue();
                
                gui.getSettingsFrame().dispose();
                break;
            default:
                break;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object rs = e.getSource();
        if (rs instanceof RangeSlider) {
            double localMAFThreshold = gui.getMafSlider().getValue();
            gui.getMafText().setValue(localMAFThreshold / 10000);
            double localMAFThresholdMax = gui.getMafSlider().getUpperValue();
            gui.getMafTextMax().setValue(localMAFThresholdMax / 10000);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object mafText = evt.getSource();
        if (mafText instanceof JFormattedTextField) {
            String mafTextName = ((JFormattedTextField) mafText).getName();
            if (mafTextName.equals("mafText")) {
                double localMAFThreshold = ((Number) gui.getMafText().getValue()).doubleValue();
                if (localMAFThreshold > 0.5) {
                    localMAFThreshold = 0.5;
                    gui.getMafText().setValue(localMAFThreshold);
                } else if (localMAFThreshold < 0.0) {
                    localMAFThreshold = 0.0;
                    gui.getMafText().setValue(localMAFThreshold);
                }
                gui.getMafSlider().setValue((int) (localMAFThreshold * 10000));

            } else if (mafTextName.equals("mafTextMax")) {
                double localMAFThreshold = ((Number) gui.getMafTextMax().getValue()).doubleValue();
                if (localMAFThreshold > 0.5) {
                    localMAFThreshold = 0.5;
                    gui.getMafTextMax().setValue(localMAFThreshold);
                } else if (localMAFThreshold < 0.0) {
                    localMAFThreshold = 0.0;
                    gui.getMafTextMax().setValue(localMAFThreshold);
                }
                gui.getMafSlider().setUpperValue((int) (localMAFThreshold * 10000));
            }
        }
    }
}
