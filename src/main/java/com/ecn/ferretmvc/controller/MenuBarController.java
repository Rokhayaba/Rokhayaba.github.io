/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecn.ferretmvc.controller;

import com.ecn.ferretmvc.model.MenuBarModel;
import com.ecn.ferretmvc.view.GUI;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Eric
 */
//This controller (class) links the buttons of the menu bar to the model (class) MenuBarModel
public class MenuBarController implements ActionListener {

    private final GUI gui;
    private final MenuBarModel model;

    public MenuBarController(GUI gui, MenuBarModel model) {
        this.gui = gui;
        this.model = model;
        gui.menuBarListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Retriving the ActionCommand ie. the String associated with the button clicked
        String viewAction = e.getActionCommand();
        switch (viewAction) {
            case "settingsMenuItem":
                gui.getSettingsFrame().setLocationRelativeTo(GUI.getSnpFerret());
                gui.getSettingsFrame().setVisible(true);
                break;
            case "exitMenuItem":
                System.exit(0);
                break;
            case "updateMenuItem":
                
                model.performFerretUpdate(gui);
                   break;

            case "aboutMenuItem":
                gui.getAboutFrame().setLocationRelativeTo(GUI.getSnpFerret());
                gui.getAboutFrame().setVisible(true);
                break;
            case "faqMenuItem":
                try {
                    Desktop.getDesktop().browse(new URI("http://limousophie35.github.io/Ferret/#faq"));
                } catch (IOException | URISyntaxException exception) {
                }
                break;
            case "contactMenuItem":
                gui.getContactFrame().setLocationRelativeTo(GUI.getSnpFerret());
                gui.getContactFrame().setVisible(true);
                break;
            default:
                break;
        }
    }
}
