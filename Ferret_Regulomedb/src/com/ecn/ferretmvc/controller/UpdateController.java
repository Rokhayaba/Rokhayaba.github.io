/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecn.ferretmvc.controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.ecn.ferretmvc.view.*;

/**
 *
 * @author Eric
 */
public class UpdateController implements ActionListener {

    private GUI gui;

    public UpdateController(GUI gui) {
        this.gui = gui;
        gui.updateListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gui.getUpdateFrame().dispose();
    }
}
