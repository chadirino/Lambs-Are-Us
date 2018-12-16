package windows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import db.*;

public class IngredientGUI {

    JFrame fView, fAdd;
    JPanel pView, pAdd, pUpdate;
    JDialog dlgAdd;
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
    ButtonListener buttonListen;

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
        //                       buttons
        // ======================================================

        btnSave = new JButton("Save");
        btnSave.setPreferredSize(new Dimension(123,25));
        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(123,25));
        
        // ======================================================
        //                        Labels/Fields
        // ======================================================
        
        lblName = new JLabel("Name:");
        tfName = new JTextField(20);
        lblUnitOfMeasure = new JLabel("Unit of Measure");
        tfUnitOfMeasure = new JTextField(20);
        lblROP = new JLabel("Reorder Point");
        tfROP = new JTextField(20);
        
        
        // ======================================================
        //                         methods
        // ======================================================

        pView = new JPanel();
        pView.add(spView);

        pAdd = new JPanel();
        pAdd.add(lblName);
        pAdd.add(tfName);
        pAdd.add(lblUnitOfMeasure);
        pAdd.add(tfUnitOfMeasure);
        pAdd.add(lblROP);
        pAdd.add(tfROP);
        pAdd.add(btnSave);
        pAdd.add(btnCancel);
        
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

        fAdd = new JFrame();
        dlgAdd = new JDialog(fAdd);
        dlgAdd.add(pAdd);
        dlgAdd.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                fAdd.dispose(); 
            }
        });
        dlgAdd.setTitle("Add Ingredient");
        dlgAdd.setJMenuBar(menuBar);
        dlgAdd.setSize(425,300);
        dlgAdd.setLocationRelativeTo(null);

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
        dlgAdd.setVisible(true);
    }
    
    private void sortAtoZ() {
        Sql.sortAtoZ();
        pView.setVisible(true);
    }
    
    private void sortZtoA() {
        Sql.sortZtoA();
        pView.setVisible(true);
    }

    
    
    // ======================================================
    //                     event handler
    // ======================================================
    
    // menu items
    private class MenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == miView) {
                openViewWindow(); 
            } else if (event.getSource() == miAdd) {
                openAddWindow(); 
            } else if (event.getSource() == miUpdate) {
                // openUpdate(); 
            } else if (event.getSource() == miSortDesc) {
                // sort descending
            	sortAtoZ();
            } else if (event.getSource() == miSortAsc) {
                // sort ascending
            	sortZtoA();
            } else if (event.getSource() == miLogout) {
                fView.dispose();
                new LoginGUI();
            }
        }
    }
    
    private class ButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent event) {
    		if (event.getSource() == btnSave) {
    			addIngredient();
    			//updateQty();
    			} 
    		else if (event.getSource() == btnCancel) {
    				fAdd.dispose();
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
        //Sql.addIngredient();
    }

    public static void updateQty(Integer ingredientID, Integer unitQty) {
        Sql.updateQty();
    }
}
