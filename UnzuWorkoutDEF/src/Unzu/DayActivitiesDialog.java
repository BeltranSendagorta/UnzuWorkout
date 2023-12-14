package Unzu;

import javax.swing.*;

import gui.sesion.VentanaSesion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@SuppressWarnings("serial")
public class DayActivitiesDialog extends JDialog {

    private UnzuWorkout gymApp;
    private JList<Actividad> activityList;
    private DefaultListModel<Actividad> listModel;

    public DayActivitiesDialog(UnzuWorkout gymApp, String day, List<Actividad> actividadesDia) {
        super(gymApp, "Actividades del " + day, true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.gymApp = gymApp;
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/Icono.jpg")));

        listModel = new DefaultListModel<>();
        for (Actividad actividad : actividadesDia) {
            listModel.addElement(actividad);
        }

        activityList = new JList<>(listModel);
        activityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(activityList);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton enrollButton = new JButton("Apuntarse");
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollInActivity(activityList.getSelectedValue());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(enrollButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(gymApp);
    }

    private void enrollInActivity(Actividad actividad) {
        if (actividad != null) {
            if(gymApp.addActivityToUsuario(actividad)) {
                JOptionPane.showMessageDialog(this, "Te has apuntado a la actividad: " + actividad);
                dispose();	
            }else {
            	JOptionPane.showMessageDialog(this, "Ya estás apuntado a esta actividad");	
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una actividad antes de apuntarte");
        }
    }
}
