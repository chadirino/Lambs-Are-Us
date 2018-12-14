package windows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import db.*;

public class AdminGUI extends JFrame {

    private static JFrame fView, fAdd;
    private static JDialog dlgAdd;
    private JPanel pView, pAdd;
    public static JTable tblUsers;
    private JScrollPane spUsers;
    private JLabel lblEmployeeID, lblUserName, lblUserType;
    private JTextField tfEmployeeID, tfUserName;
    private JComboBox<String> cbUserType;
    private String idInput, userInput, selectedType;
    private Integer employeeID;
    private JButton btnAdd, btnSave, btnCancel;
    private ButtonListener buttonListen;
    
    public AdminGUI() {

        // ======================================================
        //                       buttons
        // ======================================================

        buttonListen = new ButtonListener();

        btnAdd = new JButton("New User");
        btnAdd.setPreferredSize(new Dimension(85,25));
        btnAdd.addActionListener(buttonListen);

        btnSave = new JButton("Save");
        btnSave.setPreferredSize(new Dimension(85,25));
        btnSave.addActionListener(buttonListen);

        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(85,25));
        btnCancel.addActionListener(buttonListen);

        // ======================================================
        //                   labels/fields/boxes
        // ======================================================

        lblEmployeeID = new JLabel("Employee ID:");
        lblUserName = new JLabel("Username:");
        lblUserType = new JLabel("User Type:");
        
        tfEmployeeID = new JTextField(8);
        tfUserName = new JTextField();
        
        String[] userTypes = {"Staff","Manager","Admin"};
        cbUserType = new JComboBox<>(userTypes);
        cbUserType.setSelectedIndex(0);
        // cbUserType.addActionListener(l);

        // ======================================================
        //                         table
        // ======================================================

        tblUsers = new JTable();
        spUsers = new JScrollPane(tblUsers);
        spUsers.setPreferredSize(new Dimension(375,200));

        // ======================================================
        //                         panels
        // ======================================================
        
        pView = new JPanel();
        pView.add(tblUsers);
        pView.add(btnAdd);

        pAdd = new JPanel();
        pAdd.add(lblEmployeeID);
        pAdd.add(tfEmployeeID);
        pAdd.add(lblUserName);
        pAdd.add(tfUserName);
        pAdd.add(lblUserType);
        pAdd.add(cbUserType);
        pAdd.add(btnSave);
        pAdd.add(btnCancel);
        
        // ======================================================
        //                     dialogs/frames
        // ======================================================
        
        fView = new JFrame();
        fView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fView.setTitle("Admin Page");
        fView.setSize(425,300);
        fView.setLocationRelativeTo(null);
        fView.setVisible(true);

        fAdd = new JFrame();
        dlgAdd = new JDialog(fAdd);
        dlgAdd.add(pAdd);
        fAdd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fAdd.setTitle("New User");
        fAdd.setSize(425,300);
        fAdd.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new AdminGUI();
    }

    // ======================================================
    //                     event handler
    // ======================================================
    
    private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
            if (event.getSource() == btnAdd) {
                openAddDialog();
            } else if (event.getSource() == btnSave) {
                validateAdd();;
                openViewWindow();
            }
        }
    }

    private void openViewWindow() {
        getUsers();
        fView.setVisible(true);
    }

    private void openAddDialog() {
        dlgAdd.setVisible(true);
    }

    // ======================================================
    //                  validate add user
    // ======================================================
    
    private void validateAdd() {
        
        idInput = tfEmployeeID.getText();
        employeeID = Integer.parseInt(idInput);
        userInput = tfUserName.getText();
        selectedType = (String) cbUserType.getSelectedItem();
        
        if (fieldsFilled(idInput, userInput, selectedType) == false) {
            giveFillWarning();
        } else if (validEmployee(employeeID) == false) {
            giveInvalidWarning();
        } else if (duplicateEmployee(employeeID) == true) {
            giveDuplicateEmployeeWarning();
        } else if (duplicateUserName(userInput) == true) {
            giveDuplicateUserNameWarning();
        } else {
            addUser(employeeID, userInput, selectedType);
            JOptionPane.showMessageDialog(null, getEmployeeName(employeeID) + " was successfully added as a " + selectedType + ".");
            fAdd.dispose();
            openViewWindow();
        }
    }

    private Boolean fieldsFilled(String id, String user, String type) {
        if (id.equals("") || user.equals("") || type.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void giveFillWarning() {
        JOptionPane.showMessageDialog(null, "Form not completely filled.");
    }

    private void giveInvalidWarning() {
        JOptionPane.showMessageDialog(null, "Invalid Employee.");
    }

    private void giveDuplicateEmployeeWarning() {
        JOptionPane.showMessageDialog(null, "Employee is already a user.");
    }

    private void giveDuplicateUserNameWarning() {
        JOptionPane.showMessageDialog(null, "Username is already taken.");
    }
    
    // ======================================================
    //                  database interaction
    // ======================================================
    
    private void getUsers() {
        Sql.getUsers();
    }

    private String getEmployeeName() {
        return Sql.getEmployeeName();
    }
    
    private Boolean validEmployee(Integer employeeID) {
        return Sql.validEmployee(employeeID);
    }
    
    private Boolean duplicateEmployee(Integer employeeID) {
        return Sql.duplicateEmployee(employeeID);
    }

    private Boolean duplicateUserName(String userName) {
        return Sql.duplicateUserName(userName);
    }
    
    private void addUser(Integer employeeID, String userName, String userType) {
        Sql.addUser(employeeID, userName, userType);
    }

    private void deleteUser(String userName) {
        Sql.deleteUser(userName);
    }	
}
