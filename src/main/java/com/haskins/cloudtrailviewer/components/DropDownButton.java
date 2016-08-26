package com.haskins.cloudtrailviewer.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropDownButton extends AbstractButton {

    private JButton actionButton;
    private JPopupMenu popupMenu;

    public DropDownButton(JButton _actionButton, JPopupMenu _popupMenu) {

        this.popupMenu = _popupMenu;
        this.actionButton = _actionButton;

        setLayout(new BorderLayout());
        actionButton.setBorderPainted(false);

        add(BorderLayout.CENTER, actionButton);

        actionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                popupMenu.show(actionButton, 0, actionButton.getSize().height);
            }
        });
    }
}
