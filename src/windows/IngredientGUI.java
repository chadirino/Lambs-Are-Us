package windows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import db.*;

public class IngredientGUI {

    JFrame fView, fAdd;
    JPanel pView, pAdd, pUpdate;
    JMenuBar menuBar;
    JMenu menu, subSort;
    JMenuItem miView, miAdd, miUpdate, miSortDesc, miSortAsc, miLogout;
    public static JTable tblView;
    JScrollPane spView;
    JButton btnSave, btnCancel;
    JLabel lblName, lblUnitOfMeasure, lblROP;
    JTextField tfName, tfUnitOfMeasure, tfROP;
    Integer ROP;
    MenuItemListener menuListen;

    public IngredientGUI() {
        
        // ======================================================
        //                        tables
        // ======================================================

        tblView = new JTable();
        // tblNonEdit.addMouseListener(new MouseAdapter() {
        //     public void mousePressed(MouseEvent event) {
        //         JTable table =(JTable) event.getSource();
        //         Point point = event.getPoint();
        //         int row = table.rowAtPoint(point);
        //         if (event.getClickCount() == 2 && table.getSelectedRow() != -1) {
        //             System.out.print(row); 
        //         }
        //     }
        // });

        spView = new JScrollPane(tblView);
        spView.setPreferredSize(new Dimension(375,200));

        // ======================================================
        //                          menu
        // ======================================================

        // --------------------- menu items ---------------------

        // menu item listener
        menuListen = new MenuItemListener();

        // menu menu items
        miView = new JMenuItem("View list of ingredients");
        miAdd = new JMenuItem("Add new ingredient");
        miUpdate = new JMenuItem("Update existing ingredient");
        miLogout = new JMenuItem("Logout");
        
        // submenu menu items
        miSortDesc = new JMenuItem("From A to Z");
        miSortAsc = new JMenuItem("From Z to A");

        // add listener to menu items
        miView.addActionListener(menuListen);
        miAdd.addActionListener(menuListen);
        miUpdate.addActionListener(menuListen);
        miSortDesc.addActionListener(menuListen);
        miSortAsc.addActionListener(menuListen);
        miLogout.addActionListener(menuListen);

        // ----------------------- menu -------------------------
        
        // sort sub menu
        subSort = new JMenu("Sort table");
        subSort.add(miSortDesc);
        subSort.add(miSortAsc);
        
        // main menu
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.add(miView);
        menu.add(miAdd);
        menu.add(miUpdate);
        menu.addSeparator();
        menu.add(subSort);
        menu.addSeparator();
        menu.add(miLogout);
        
        // ---------------------- menu bar ----------------------

        menuBar = new JMenuBar();
        menuBar.add(menu);

        // ======================================================
        //                         panels
        // ======================================================

        pView = new JPanel();
        pView.add(spView);

        pAdd = new JPanel();
        pAdd.add(lblName);
        pAdd.add(tfName);
        pAdd.add(lblUnitOfMeasure);
        pAdd.add(tfUnitOfMeasure);
        
        // ======================================================
        //                    frames/dialogs
        // ======================================================
        
        fView = new JFrame();
        fView.add(pView);
        fView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                fView.dispose(); 
            }
        });
        fView.setTitle("Ingredient Manager");
        fView.setJMenuBar(menuBar);
        fView.setSize(425,300);
        fView.setLocationRelativeTo(null);

        openViewWindow(); 
    }
    
    // ======================================================
    //                      main method
    // ======================================================

    public static void main(String[] args) {
        new IngredientGUI();
    }

    // ======================================================
    //                         panels
    // ======================================================
    
    private void openViewWindow() {
        getIngredients();
        pView.setVisible(true);
    }
    
    private void openAddWindow() {

    }
    
    private void openViewAtoZ() {
        switchPage();
        Sql.sortAtoZ();
        pView = new JPanel();
        pView.add(spNonEdit);
        cp.add(pView);
    }
    
    private void openViewZtoA() {
        switchPage();
        Sql.sortZtoA();
        pView = new JPanel();
        pView.add(spNonEdit);
        cp.add(pView);
    }

    // new ingredient page
    private void openAdd() {
        switchPage();
        pAdd = new JPanel();
        pAdd.add(spEdit);
        cp.add(pAdd);
    }

    // ingredient update page
    private void openUpdate() {
        switchPage();
        Sql.getIngredients();
        pUpdate = new JPanel();
        pUpdate.add(spEdit);
        cp.add(pUpdate);
    }

    // ======================================================
    //                       buttons
    // ======================================================
    
    // ======================================================
    //                     event handler
    // ======================================================
    
    // menu items
    private class MenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == miView) {
                openView(); 
            } else if (event.getSource() == miAdd) {
                openAdd(); 
            } else if (event.getSource() == miUpdate) {
                openUpdate(); 
            } else if (event.getSource() == miSortDesc) {
                // sort descending
            	openViewAtoZ();
            } else if (event.getSource() == miSortAsc) {
                // sort ascending
            	openViewZtoA();
            } else if (event.getSource() == miLogout) {
                dispose();
                new LoginGUI();
            }
        }
    }

    // ======================================================
    //                      misc methods
    // ======================================================

    private void getIngredients() {
        Sql.getIngredients();
    }
    
    private void addIngredient() {
        Sql.addIngredient();
    }

    public static void updateQty(Integer ingredientID, Integer unitQty) {
        Sql.updateQty();
    }
}
