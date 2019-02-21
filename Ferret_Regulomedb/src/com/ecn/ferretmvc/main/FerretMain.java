package com.ecn.ferretmvc.main;

import com.ecn.ferretmvc.controller.*;
import com.ecn.ferretmvc.model.DownloadTheDataModel;
import com.ecn.ferretmvc.model.MenuBarModel;
import com.ecn.ferretmvc.view.GUI;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Eric
 */
public class FerretMain {

    public static void main(String[] args) {
        GUI ferretGUI = new GUI();
        
        DownloadTheDataModel dtdModel = new DownloadTheDataModel();
        MenuBarModel mbModel = new MenuBarModel();
        
        UpdateController UC = new UpdateController(ferretGUI);
        SettingsController SC = new SettingsController(ferretGUI);
        MenuBarController MBC = new MenuBarController(ferretGUI, mbModel);
        MainPanelController MFC = new MainPanelController(ferretGUI);
        DownloadTheDataController DTDC = new DownloadTheDataController(ferretGUI, dtdModel);
        ferretGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
