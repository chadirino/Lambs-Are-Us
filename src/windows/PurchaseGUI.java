package windows;

import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import db.*;

public class PurchaseGUI {

    private JFrame fView, fDateRange, fAdd;
    private JMenuBar menuBar;
    private JMenu menu, subSort;
    private JMenuItem miView, miAdd, miSortNew, miSortOld, miDate, miLogout;
    private JPanel pView, pDateRange, pAdd;
    private JDialog dlgDateRange, dlgAdd;
    private JButton btnDone, btnCancel, btnSearch, btnBack, btnDateRangeOK, btnDateRangeCancel, btnNextItem;
    private JLabel lblSearch, lblDateTo, lblDateFrom, lblName, lblUnitQty, lblUnitPrice;
    private JTextField tfSearch, tfDateTo, tfDateFrom, tfUnitQty, tfUnitPrice;
    public static JComboBox<String> cbName;
    private String inputSearch, inputDateFrom, inputDateTo, selectedName, strUnitQtyInput, strUnitPriceInput, formattedDate, user;
    private Integer unitQty, employeeID;
    private static Integer purchaseItem, purchaseID;
    private Double unitPrice;
    private DateTimeFormatter dateFormat;
    private LocalDateTime date;
    public static JTable tblEdit, tblNonEdit;
    private JScrollPane spEdit, spNonEdit;
    private MenuItemListener menuListen;
    private ButtonListener buttonListen;

    public PurchaseGUI() {
        
        // ======================================================
        //                       buttons
        // ======================================================

        btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(75,20));

        btnDone = new JButton("Done");
        btnCancel = new JButton("Cancel");
        btnBack = new JButton("Back");
        btnDateRangeOK = new JButton("OK");
        btnDateRangeCancel = new JButton("Cancel");
        btnNextItem = new JButton("Next Item");
        
        buttonListen = new ButtonListener();

        // add action listener to buttons
        btnSearch.addActionListener(buttonListen);
        btnDone.addActionListener(buttonListen);
        btnCancel.addActionListener(buttonListen);
        btnBack.addActionListener(buttonListen);
        btnDateRangeOK.addActionListener(buttonListen);
        btnDateRangeCancel.addActionListener(buttonListen);
        btnNextItem.addActionListener(buttonListen);

        // ======================================================
        //                        tables
        // ======================================================
        
        // non-editable table for initial page
        tblNonEdit = new JTable();
        
        
        // editable table for update page
        tblEdit = new JTable();

        // put tables inside scroll panes (give them scroll bars)
        spNonEdit = new JScrollPane(tblNonEdit);
        spNonEdit.setPreferredSize(new Dimension(375,200));
        spEdit = new JScrollPane(tblEdit);
        spEdit.setPreferredSize(new Dimension(375,200));

        // ======================================================
        //                     labels/fields
        // ======================================================

        lblSearch = new JLabel("Purchase ID:");
        tfSearch = new JTextField(12);

        lblDateFrom = new JLabel("From: ");
        lblDateTo = new JLabel("To: ");

        tfDateFrom = new JTextField(10);
        tfDateTo = new JTextField(10);

        lblName = new JLabel("Ingredient Name:");
        cbName = new JComboBox<>();
        getUserCB();
        cbName.setSelectedIndex(0);

        lblUnitPrice = new JLabel("Unit Price:");
        tfUnitPrice = new JTextField(20);

        lblUnitQty = new JLabel("Unit Qty:");
        tfUnitPrice = new JTextField(20);

        // ======================================================
        //                          menu
        // ======================================================

        // --------------------- menu items ---------------------

        // menu item listener
        menuListen = new MenuItemListener();

        // menu menu items
        miView = new JMenuItem("View list of purchases");
        miAdd = new JMenuItem("Add new purchase");
        miDate = new JMenuItem("Choose time frame");
        miLogout = new JMenuItem("Logout");
        
        // submenu menu items
        miSortNew = new JMenuItem("Most recent first");
        miSortOld = new JMenuItem("Most recent last");
        
        // add listener to menu items
        miView.addActionListener(menuListen);
        miAdd.addActionListener(menuListen);
        miSortNew.addActionListener(menuListen);
        miSortOld.addActionListener(menuListen);
        miDate.addActionListener(menuListen);
        miLogout.addActionListener(menuListen);

        // ----------------------- menu ------------------------
        
        // sort sub menu
        subSort = new JMenu("Sort table");
        subSort.add(miSortNew);
        subSort.add(miSortOld);
        
        // main menu
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.add(miView);
        menu.add(miAdd);
        menu.addSeparator();
        menu.add(subSort);
        menu.addSeparator();
        menu.add(miDate);
        menu.addSeparator();
        menu.add(miLogout);

        // ---------------------- menu bar ----------------------

        menuBar = new JMenuBar();
        menuBar.add(menu);
        
        // ======================================================
        //                        panels
        // ======================================================

        // view
        pView = new JPanel();
        pView.add(lblSearch);
        pView.add(tfSearch);
        pView.add(btnSearch);
        pView.add(spNonEdit);
        
        // date range
        pDateRange = new JPanel();
        pDateRange.add(lblDateFrom);
        pDateRange.add(tfDateFrom);
        pDateRange.add(lblDateTo);
        pDateRange.add(tfDateTo);
        pDateRange.add(btnDateRangeOK);
        pDateRange.add(btnDateRangeCancel);

        // add purchase items
        pAdd = new JPanel();
        pAdd.add(lblName);
        pAdd.add(cbName);
        pAdd.add(lblUnitPrice);
        pAdd.add(tfUnitPrice);
        pAdd.add(lblUnitQty);
        pAdd.add(tfUnitQty);
        pAdd.add(btnNextItem);
        pAdd.add(btnDone);
        pAdd.add(btnCancel);

        // ======================================================
        //                    frames/dialogs
        // ======================================================

        // --------------------- main view ----------------------
        
        fView = new JFrame();
        fView.add(pView);
        fView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                fView.dispose(); 
            }
        });
        fView.setTitle("Purchases Manager");
        fView.setJMenuBar(menuBar);
        fView.setSize(450,325);
        fView.setLocationRelativeTo(null);
        

        // ----------------- data range dialog ------------------

        fDateRange = new JFrame();
        dlgDateRange = new JDialog(fDateRange);
        dlgDateRange.add(pDateRange);
        dlgDateRange.getRootPane().setDefaultButton(btnDateRangeOK);
        dlgDateRange.setTitle("Date Range");
        dlgDateRange.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                fDateRange.dispose(); 
            }
        });
        dlgDateRange.setSize(350,200);
        dlgDateRange.setLocationRelativeTo(null);

        // -------------- add purchase dialog -------------------

        fAdd = new JFrame();
        dlgAdd = new JDialog(fAdd);
        dlgAdd.add(pAdd);
        dlgAdd.setTitle("Add Purchase");
        dlgAdd.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                fAdd.dispose(); 
            }
        });
        dlgAdd.setSize(350,200);
        dlgAdd.setLocationRelativeTo(null);

        // open initial page
        openViewWindow();
    }

    // ======================================================
    //                      main method
    // ======================================================

    public static void main(String[] args) {
        new PurchaseGUI();
    }

    
    // ======================================================
    //                     event handler
    // ======================================================
    
    // buttons
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == btnSearch) {
                inputSearch = tfSearch.getText();
                // search and focus on cell
            } else if (event.getSource() == btnNextItem) {
                validateAdd();
            } else if (event.getSource() == btnDone) {
                showVerifyDialog();
                fAdd.dispose();
                openViewWindow();
            } else if (event.getSource() == btnCancel) {
                validateCancel();
                openViewWindow();
            } else if (event.getSource() == btnDateRangeOK) {
                setDateRange();
            } else if (event.getSource() == btnDateRangeCancel) {
                fDateRange.dispose();
            }
        }
    }
    
    // menu items
    private class MenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == miView) {
                openViewWindow(); 
            } else if (event.getSource() == miAdd) {
                openAddWindow(); 
            } else if (event.getSource() == miSortNew) {
            	openViewWindowMostRecentFirst();
            } else if (event.getSource() == miSortOld) {
            	openViewWindowMostRecentLast();
            } else if (event.getSource() == miDate) {
                openSetRangeDialog();
            } else if (event.getSource() == btnBack) {
                openViewWindow();
            } else if (event.getSource() == miLogout) {
                fView.dispose();
                new LoginGUI();
            } 
        }
    }

    // ======================================================
    //                validate add purchase
    // ======================================================

    // ------------------- method handler -------------------

    private void validateAdd() {

        user = LoginGUI.user;
        employeeID = getEmployeeID(user);
        purchaseID = 0;
        purchaseItem = 0;
        dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        date = LocalDateTime.now();
        formattedDate = dateFormat.format(date);
        selectedName = (String) cbName.getSelectedItem();
        strUnitPriceInput = tfUnitPrice.getText();
        unitPrice = Double.parseDouble(strUnitPriceInput);
        strUnitQtyInput = tfUnitQty.getText();
        unitQty = Integer.parseInt(strUnitQtyInput);

        if (boxesFilled(strUnitPriceInput, strUnitQtyInput) == false) {
            giveFillWarning();
        } else if (purchaseItem == 0) {
            addPurchase(formattedDate, employeeID);
            purchaseID = getPurchaseID();
            addPurchaseItem(purchaseID, selectedName, unitPrice, unitQty);
            purchaseItem++;
        } else {
            addPurchaseItem();
        }
    }

    // ------------------ get employee id -------------------
    
    private Integer getEmployeeID(String user) {
        return Sql.getEmployeeID(user);
    }

    // ------------------ get purchase id -------------------
    
    private Integer getPurchaseID() {
        return Sql.getPurchaseID();
    }
    
    // ------------------ create purchase -------------------

    private void addPurchase(String date, Integer empID) {
        Sql.addPurchase(date, empID);
    }

    // ------------------ create purchase -------------------

    private void addPurchaseItem(Integer purchaseID, String name, Double price, Integer qty) {
    	Sql.addPurchaseItem(purchaseID, name, price, qty);
    }
    
    // --------------- check if boxes filled ----------------

    private Boolean boxesFilled(String price, String qty) {
        if (price.equals("") || qty.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    // ----------------- show fill warning ------------------

    private void giveFillWarning() {
        JOptionPane.showMessageDialog(null, "Form not completely filled out."); 
    }

    // ======================================================
    //                      misc methods
    // ======================================================

    private void openSetRangeDialog() {
        dlgDateRange.setVisible(true);
    }

    private void openViewWindow() {
        getPurchases();
        fView.setVisible(true);
    }

    private void validateCancel() {
        if (purchaseItem > 0) {
            respose = JOptionPane.showConfirmDialog(null, "Do you want to save the purchase?");
        }
    }

    // ======================================================
    //                 database interactions
    // ======================================================
    
    private void getPurchases() {
        Sql.getPurchases();
    }

    private void openAddWindow() {
        dlgAdd.setVisible(true);
    }

    private void openViewWindowMostRecentFirst() {
        Sql.mostRecentFirst();
        fView.setVisible(true);
    }
    
    private void openViewWindowMostRecentLast() {
        Sql.mostRecentLast();
        fView.setVisible(true);
    }
    
    private void setDateRange() {
        inputDateFrom = tfDateFrom.getText();
        inputDateTo = tfDateTo.getText();
        Sql.setDateRange(inputDateFrom, inputDateTo);
    }
    
    private void getPurchaseItems(int purchaseID) {
    	Sql.getPurchaseItems(purchaseID);
    }
    
    private void getUserCB() {
        Sql.getUserCB();
    }
}
