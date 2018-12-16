package windows;

import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.text.*;
import javax.swing.*;
import db.*;

public class PurchaseGUI {

    private JFrame fView, fDateRange, fAdd, fItems;
    private JMenuBar menuBar;
    private JMenu menu, subSort;
    private JMenuItem miView, miAdd, miSortNew, miSortOld, miDate, miLogout;
    private JPanel pView, pDateRange, pAdd, pItems;
    private JDialog dlgDateRange, dlgAdd, dlgItems;
    private JButton btnDone, btnCancel, btnSearch, btnBack, btnDateRangeOK, btnDateRangeCancel, btnNextItem;
    private JLabel lblSearch, lblDateTo, lblDateFrom, lblName, lblUnitQty, lblUnitPrice;
    private JTextField tfSearch, tfDateTo, tfDateFrom, tfUnitQty, tfUnitPrice;
    public static JComboBox<String> cbName;
    private static String inputSearch, inputDateFrom, inputDateTo, selectedName, strUnitQtyInput, strUnitPriceInput, formattedDate, user;
    private static Integer unitQty, employeeID;
    private int response, response2;
    private static Integer purchaseItem, purchaseID;
    private static Double unitPrice;
    private static DateTimeFormatter dateFormat;
    private static LocalDateTime date;
    public static JTable tblView, tblItems;
    private JScrollPane spView, spItems;
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
        
        tblView = new JTable();
        tblView.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                JTable table =(JTable) event.getSource();
                Point point = event.getPoint();
                int row = table.rowAtPoint(point);
                if (event.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    getPurchaseItems(row);
                }
            }
        });

        tblItems = new JTable();

        spView = new JScrollPane(tblView);
        spView.setPreferredSize(new Dimension(375,200));
        spItems = new JScrollPane(tblItems);
        spItems.setPreferredSize(new Dimension(375,200));

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
        cbName.setSelectedIndex(-1);

        lblUnitPrice = new JLabel("Unit Price:");
        tfUnitPrice = new JTextField(20);

        lblUnitQty = new JLabel("Unit Qty:");
        tfUnitQty = new JTextField(20);

        // ======================================================
        //                          menu
        // ======================================================

        // --------------------- menu items ---------------------

        menuListen = new MenuItemListener();

        // menu menu items
        miView = new JMenuItem("View all purchases");
        miAdd = new JMenuItem("Add new purchase");
        miDate = new JMenuItem("Choose time frame");
        miLogout = new JMenuItem("Logout");
        
        // submenu menu items
        miSortNew = new JMenuItem("Most recent first");
        miSortOld = new JMenuItem("Most recent last");
        
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

        // --------------------- main view ----------------------
        
        pView = new JPanel();
        pView.add(lblSearch);
        pView.add(tfSearch);
        pView.add(btnSearch);
        pView.add(spView);
        
         // -------------------- date range  --------------------
        
        pDateRange = new JPanel();
        pDateRange.add(lblDateFrom);
        pDateRange.add(tfDateFrom);
        pDateRange.add(lblDateTo);
        pDateRange.add(tfDateTo);
        pDateRange.add(btnDateRangeOK);
        pDateRange.add(btnDateRangeCancel);

        // ----------------- add purchase item ------------------
        
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

        // --------------------- view items ---------------------
        
        pItems = new JPanel();
        pItems.add(spItems);
        pItems.add(btnBack);

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
        
        // ----------------- date range dialog ------------------

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

        // ------------------ view items -----------------------

        fItems = new JFrame();
        dlgItems = new JDialog();
        dlgItems.add(pItems);
        dlgItems.setTitle("Purchase Items");
        dlgItems.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                fItems.dispose(); 
            }
        });
        dlgItems.setSize(350,200);
        dlgItems.setLocationRelativeTo(null);
        
        // open initial page
        openViewWindow();
    }

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
                validateDone();
            } else if (event.getSource() == btnCancel) {
                validateCancel();
            } else if (event.getSource() == btnDateRangeOK) {
                setDateRange();
            } else if (event.getSource() == btnDateRangeCancel) {
                fDateRange.dispose();
            } else if (event.getSource() == btnBack) {
                fItems.dispose();
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

        selectedName = (String) cbName.getSelectedItem();
        strUnitPriceInput = tfUnitPrice.getText();
        strUnitQtyInput = tfUnitQty.getText();
        
        if (boxesFilled() == false) {
            giveFillWarning();
        } else {
            
            unitPrice = Double.parseDouble(strUnitPriceInput);
        	unitQty = Integer.parseInt(strUnitQtyInput);
        
	        if (purchaseItem == 0) {
	            addPurchase(formattedDate, employeeID);
	            purchaseID = getPurchaseID();
	            addPurchaseItem(purchaseID, selectedName, unitPrice, unitQty);
	            purchaseItem++;
	        } else {
                eraseInput();
	            addPurchaseItem(purchaseID, selectedName, unitPrice, unitQty);
	        }
        }
    }

    private void validateDone() {
        if (boxesEmpty() == false) {
            response = JOptionPane.showConfirmDialog(null, "Do you want to save this purchase item?");
            if (response == JOptionPane.YES_OPTION) {
                validateAdd();
            } else {
                fAdd.dispose();
            }
        } else if (purchaseItem > 0) {
            response2 = JOptionPane.showConfirmDialog(null, "Save and create purchase?");
            if (response2 == JOptionPane.YES_OPTION) {
                fAdd.dispose();
            } 
        } else {
            fAdd.dispose();
        }
    }

    private void validateCancel() {
        if (purchaseItem > 0) {
            response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the purchase?");
            if (response == JOptionPane.YES_OPTION) {
                deletePurchase(purchaseID); 
            }
        } else {
            fAdd.dispose();
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
    
    // -------------------- add purchase --------------------

    private void addPurchase(String date, Integer empID) {
        Sql.addPurchase(date, empID);
    }

    // ----------------- add purchase item ------------------

    private void addPurchaseItem(Integer purchaseID, String name, Double price, Integer qty) {
    	Sql.addPurchaseItem(purchaseID, name, price, qty);
    }
    
    // --------------- check if boxes filled ----------------

    private Boolean boxesFilled() {
        if (selectedName == null || strUnitPriceInput.equals("") || strUnitQtyInput.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    // ----------------- show fill warning ------------------

    private void giveFillWarning() {
        JOptionPane.showMessageDialog(null, "Form not completely filled out."); 
    }

    // ----------------- show fill warning ------------------

    private void eraseInput() {
        cbName.setSelectedIndex(-1);
        tfUnitPrice.setText("");
        tfUnitQty.setText("");
    }

    // ======================================================
    //                      misc methods
    // ======================================================

    private void openAddWindow() {
        dlgAdd.setVisible(true);
        purchaseID = 0;
        purchaseItem = 0;
        user = LoginGUI.user;
        employeeID = getEmployeeID(user);
        dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        date = LocalDateTime.now();
        formattedDate = dateFormat.format(date);
    }
    
    private void openSetRangeDialog() {
        dlgDateRange.setVisible(true);
    }

    private void openViewWindow() {
        getPurchases();
        fView.setVisible(true);
    }

    private Boolean boxesEmpty() {
        if (selectedName == null && strUnitPriceInput.equals("") && strUnitQtyInput.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    // ======================================================
    //                 database interactions
    // ======================================================
    
    private void getPurchases() {
        Sql.getPurchases();
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

    private void deletePurchase(Integer id) {
        Sql.deletePurchase(id);
    }
}
