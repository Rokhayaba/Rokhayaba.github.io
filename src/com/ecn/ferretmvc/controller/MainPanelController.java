/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecn.ferretmvc.controller;

import com.ecn.ferretmvc.view.GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Eric
 */
public class MainPanelController implements ActionListener {

    private final GUI gui;
    private static final String FILE_LOCATION = "File Location: ";

    public MainPanelController(GUI gui) {
        this.gui = gui;
        gui.mainPanelListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Retriving the ActionCommand ie. the String associated with the button clicked
        String viewAction = e.getActionCommand();
        switch (viewAction) {
            case "snpFileBrowseButton":
                gui.getOpenFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = gui.getOpenFileChooser().showOpenDialog(gui);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File snpFile = gui.getOpenFileChooser().getSelectedFile();
                    gui.setSnpFileNameAndPath(snpFile.getAbsolutePath());
                    String snpDisplayFileName;
                    if (gui.getSnpFileNameAndPath().length() > 35 && gui.getSnpFileNameAndPath().lastIndexOf('/') != -1) {

                        snpDisplayFileName = ".." + gui.getSnpFileNameAndPath().substring(gui.getSnpFileNameAndPath().lastIndexOf('/'));
                    } else if (gui.getSnpFileNameAndPath().length() > 35 && gui.getSnpFileNameAndPath().lastIndexOf('\\') != -1) {
                        snpDisplayFileName = ".." + gui.getSnpFileNameAndPath().substring(gui.getSnpFileNameAndPath().lastIndexOf('\\'));
                    } else {
                        snpDisplayFileName = gui.getSnpFileNameAndPath();
                    }
                    gui.getSnpFileLocation().setText(FILE_LOCATION + snpDisplayFileName);
                    gui.getSnpFileClearButton().setEnabled(true);
                    gui.getSnpTextField().setEnabled(false);
                    gui.getSnpTextField().setText("");
                }
                break;
            case "snpFileClearButton":
                gui.setSnpFileNameAndPath(null);
                gui.getSnpFileLocation().setText("No file selected");
                gui.getSnpFileClearButton().setEnabled(false);
                gui.getSnpTextField().setEnabled(true);
                break;
            case "snpWindowCheckBox":
                if (gui.getSnpWindowCheckBox().isSelected()) {
                    gui.getSnpWindowField().setEnabled(true);
                } else {
                    gui.getSnpWindowField().setEnabled(false);
                    gui.getSnpWindowField().setText("");
                }
                break;
            case "geneFileBrowseButton":
                gui.getOpenFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal2 = gui.getOpenFileChooser().showOpenDialog(gui);
                if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                    File geneFile = gui.getOpenFileChooser().getSelectedFile();
                    gui.setGeneFileNameAndPath(geneFile.getAbsolutePath());

                    String geneDisplayFileName;
                    if (gui.getGeneFileNameAndPath().length() > 35 && gui.getGeneFileNameAndPath().lastIndexOf('/') != -1) {
                        geneDisplayFileName = ".." + gui.getGeneFileNameAndPath().substring(gui.getGeneFileNameAndPath().lastIndexOf('/'));
                    } else if (gui.getGeneFileNameAndPath().length() > 35 && gui.getGeneFileNameAndPath().lastIndexOf('\\') != -1) {
                        geneDisplayFileName = ".." + gui.getGeneFileNameAndPath().substring(gui.getGeneFileNameAndPath().lastIndexOf('\\'));

                    } else {
                        geneDisplayFileName = gui.getGeneFileNameAndPath();
                    }
                    gui.getGeneFileLocation().setText(FILE_LOCATION + geneDisplayFileName);
                    gui.getGeneFileClearButton().setEnabled(true);
                    gui.getGeneNameField().setEnabled(false);
                    gui.getGeneNameField().setText("");
                }
                break;
            case "geneFileClearButton":
                gui.setGeneFileNameAndPath(null);
                gui.getGeneFileLocation().setText("No file selected");
                gui.getGeneFileClearButton().setEnabled(false);
                gui.getGeneNameField().setEnabled(true);
                break;
            case "geneWindowCheckBox":
                if (gui.getGeneWindowCheckBox().isSelected()) {
                    gui.getGeneWindowField().setEnabled(true);
                } else {
                    gui.getGeneWindowField().setEnabled(false);
                    gui.getGeneWindowField().setText("");
                }
                break;
            case "allracessub[0]":
                gui.setAfrican(0, !(gui.getAllracessub()[0].isSelected()));
                gui.setAsian(0, !(gui.getAllracessub()[0].isSelected()));
                gui.setEuropean(0, !(gui.getAllracessub()[0].isSelected()));
                gui.setAmerican(0, !(gui.getAllracessub()[0].isSelected()));
                gui.setSouthAsian(0, !(gui.getAllracessub()[0].isSelected()));
                break;
            case "afrsub[0]":
                gui.setAfrican(1, !(gui.getAfrsub()[0].isSelected()));
                break;
            case "amrsub[0]":
                gui.setAmerican(1, !(gui.getAmrsub()[0].isSelected()));
                break;
            case "asnsub[0]":
                gui.setAsian(1, !(gui.getAsnsub()[0].isSelected()));
                break;
            case "eursub[0]":
                gui.setEuropean(1, !(gui.getEursub()[0].isSelected()));
                break;
            case "sansub[0]":
                gui.setSouthAsian(1, !(gui.getSansub()[0].isSelected()));
                break;
            case "browseButton":
                gui.getSaveFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                gui.getSaveFileChooser().setDialogTitle("Save As");
                int returnVal3 = gui.getSaveFileChooser().showSaveDialog(gui);
                if (returnVal3 == JFileChooser.APPROVE_OPTION) {
                    File file = gui.getSaveFileChooser().getSelectedFile();
                    gui.setFileNameAndPath(file.getAbsolutePath());
                    gui.getFileLocation().setText(FILE_LOCATION + gui.getFileNameAndPath());
                }
                break;
            default:
                break;
        }
    }
}
