package windows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import db.*;

public class IngredientGUI {

    private static JFrame fView;
	private JFrame fAdd;
    private JPanel pView, pAdd;
    private JDialog dlgAdd;
    private JMenuBar menuBar;
    private JMenu menu, subSort;
    private JMenuItem miView, miAdd, miSortDesc, miSortAsc, miLogout;
    public static JTable tblView;
    private JScrollPane spView;
    private JButton btnSave, btnCancel;
    private JLabel lblName, lblUnitOfMeasure, lblROP;
    private JTextField tfName, tfUnitOfMeasure, tfROP;
    private String strNameInput, strUOMInput, strROPInput;
    private Integer ROP;
    private MenuItemListener menuListen;
    private ButtonListener buttonListen;

    public IngredientGUI() {
        
        // ======================================================
        //                        tables
        // ======================================================

        tblView = new JTable();

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
        miLogout = new JMenuItem("Logout");
        
        // submenu menu items
        miSortDesc = new JMenuItem("From A to Z");
        miSortAsc = new JMenuItem("From Z to A");

        // add listener to menu items
        miView.addActionListener(menuListen);
        miAdd.addActionListener(menuListen);
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

        buttonListen = new ButtonListener();
        btnSave.addActionListener(buttonListen);
        btnCancel.addActionListener(buttonListen);
        
        // ======================================================
        //                        Labels/Fields
        // ======================================================
        
        lblName = new JLabel("Name:");
        tfName = new JTextField(20);
        lblUnitOfMeasure = new JLabel("Unit of Measure:");
        tfUnitOfMeasure = new JTextField(20);
        lblROP = new JLabel("Reorder Point:");
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
        dlgAdd.getRootPane().setDefaultButton(btnSave);
        dlgAdd.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                fAdd.dispose(); 
            }
        });
        dlgAdd.setTitle("Add Ingredient");
        dlgAdd.setSize(355,175);
        dlgAdd.setLocationRelativeTo(null);

        openViewWindow(); 
    }

    public static void main(String[] args) {
        new IngredientGUI();
    }

    // ======================================================
    //                        methods
    // ======================================================
    
    private void openViewWindow() {
        refreshViewWindow();
        fView.setVisible(true);
    }

    private void refreshViewWindow() {
        getIngredients();
        fView.revalidate();
        fView.repaint();
    }
    
    private void openAddWindow() {
        dlgAdd.setVisible(true);
    }
    
    private void sortAtoZ() {
        Sql.sortAtoZ();
        fView.setVisible(true);
    }
    
    private void sortZtoA() {
        Sql.sortZtoA();
        fView.setVisible(true);
    }
    
    // ======================================================
    //                     event handlers
    // ======================================================
    
    // menu items
    private class MenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == miView) {
                openViewWindow(); 
            } else if (event.getSource() == miAdd) {
                openAddWindow(); 
            } else if (event.getSource() == miSortDesc) {
            	sortAtoZ();
            } else if (event.getSource() == miSortAsc) {
            	sortZtoA();
            } else if (event.getSource() == miLogout) {
                fView.dispose();
                HomeGUI.fUser.dispose();
                new LoginGUI();
            }
        }
    }
    
    // buttons 
    private class ButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent event) {
    		if (event.getSource() == btnSave) {
    			if (validAddIngredient(strNameInput, strUOMInput, strROPInput) == true) {
	                addIngredient();
	                fAdd.dispose();
	                JOptionPane.showMessageDialog(null, strNameInput + " successfully added.");
	                refreshViewWindow();
    			}
    			else {
    				JOptionPane.showMessageDialog(null, "Please fill all existing fields.");
    			}
    		} else if (event.getSource() == btnCancel) {
    			fAdd.dispose();
    		}
    	}
    }

    // ======================================================
    //                  database interactions
    // ======================================================

    private static void getIngredients() {
        Sql.getIngredients();
    }
    
    private void addIngredient() {
        
        strNameInput = tfName.getText();
        strUOMInput = tfUnitOfMeasure.getText();
        strROPInput = tfROP.getText(); 
        ROP = Integer.parseInt(strROPInput);

        Sql.addIngredient(strNameInput, strUOMInput, ROP);
    }

    public static void updateQty(String name, Integer unitQty) {
        Sql.updateQty(name, unitQty);
        getIngredients();
        fView.revalidate();
        fView.repaint();
    }

    // ======================================================
    //                  error handling
    // ======================================================
    
    //----------- check if completely filled out -----------
    private boolean validAddIngredient(String name, String uom, String rop) {
        
    	name = tfName.getText();
        uom = tfUnitOfMeasure.getText();
        rop = tfROP.getText();
        
        if (name.equals("") || uom.equals("") || rop.equals("")) {
            return false;
        } else {
            return true;
        }     
    }
}
