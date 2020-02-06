/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecn.ferretmvc.controller;

import com.ecn.ferretmvc.model.DownloadTheDataModel;
import com.ecn.ferretmvc.view.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 *
 * @author Eric
 */
//This controller (class) links the two buttons "Download the data!" and "Download and Visualize with HaploView!" to the model DownloadTheDataModel.java
public class DownloadTheDataController implements ActionListener {

    private static final String Genename = null;
	private GUI gui;
    private DownloadTheDataModel model = null;

    public DownloadTheDataController(GUI gui, DownloadTheDataModel model) {
        this.gui = gui;
        this.model = model;
        gui.downloadTheDataListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Retriving the ActionCommand ie. the String associated with the button clicked
        String viewAction = e.getActionCommand();
//        if (viewAction.equals("goHaplo")) {
//            model.performSearchAndDownload(gui, true);
//            model.hla_request(gui);
//            //model.hla_extraction(gui,Genename, 0, null, null, null );
//        } 
//        if (viewAction.equals("testButton")) {
//            model.connect(gui);
//        }
        
       
            model.performSearchAndDownload(gui);
            model.hla_request(gui);
            //model.hla_extraction(gui,Genename, 0, null, null, null);
            //model.
        
    }
}
