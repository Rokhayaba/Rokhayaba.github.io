/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecn.ferretmvc.model;

import com.ecn.ferretmvc.view.GUI;
import com.ecn.ferretmvc.view.LinkLabel;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author Eric
 */
public class MenuBarModel extends Observable {

    public void performFerretUpdate(GUI gui) {
        gui.getUpdateFrame().setLocationRelativeTo(GUI.getSnpFerret());
        gui.getUpdateFrame().setVisible(true);
        if (!(gui.getCheckedForUpdate()[0])) {
            gui.getCheckedForUpdate()[0] = true;
            final CheckForUpdate updateWorker = new CheckForUpdate();
            updateWorker.addPropertyChangeListener((PropertyChangeEvent arg0) -> {
                if (arg0.getPropertyName().equals("state")) {
                    if ((SwingWorker.StateValue) arg0.getNewValue() == SwingWorker.StateValue.DONE) {
                        
                        String updateReason = updateWorker.updateStatus();
                        Boolean urgentUpdate = updateWorker.urgentUpdate();
                        Boolean needUpdate = updateWorker.needUpdate();
                        
                        if (urgentUpdate || needUpdate) {
                            gui.getUpdateLabel().setText(updateReason);
                            gui.getUpdateBarHolder().remove(gui.getUpdateProgressBar());
                            LinkLabel ferretUpdate = null;
                            try {
                                ferretUpdate = new LinkLabel(new URI("http://limousophie35.github.io/Ferret/"), "http://limousophie35.github.io/Ferret/");
                            } catch (URISyntaxException e) {
                            }
                            JLabel updateFerretLabel = new JLabel("Please update Ferret at:");
                            gui.getUpdateBarHolder().add(updateFerretLabel);
                            gui.getUpdateBarHolder().repaint();
                            updateFerretLabel.setText("");
                            updateFerretLabel.setText("Please update Ferret at:");
                            if (ferretUpdate != null) {
                                ferretUpdate.setBackgroundColor(gui.getUpdatePanel().getBackground());
                                ferretUpdate.init();
                                ferretUpdate.setAlignmentX(LEFT_ALIGNMENT);
                                ferretUpdate.setMaximumSize(ferretUpdate.getPreferredSize());
                                gui.getUpdateBarHolder().add(ferretUpdate);
                            }
                        } else {
                            gui.getUpdateLabel().setText("");
                            gui.getUpdateBarHolder().remove(gui.getUpdateProgressBar());
                            gui.getUpdateBarHolder().add(new JLabel(updateReason));
                        }
                    }
                }
            });
            updateWorker.execute();
        }
    }
}
