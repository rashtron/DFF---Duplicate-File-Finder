package dff1;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class DuplicateFileFinder extends JPanel
                             implements ActionListener {
	private JList list1;
	private DefaultListModel listModel;
	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
    	JButton browsebutton;
    	JTextArea log;
    	JFileChooser fc;
    
    public DuplicateFileFinder() {
        super(new BorderLayout());
        
      // Create and populate the list model.
        listModel = new DefaultListModel();
        list1 = new JList(listModel);
        list1.setBackground(new Color(240, 255, 255));
        list1.setSize(100, 100);
        list1.setSelectionMode(
                ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        //Create the log first, because the action listeners need to refer to it.
        log = new JTextArea(100,100);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        logScrollPane.add(list1);
        
        //Create a file chooser
        fc = new JFileChooser();
        
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the Browse button.
        browsebutton = new JButton("BROWSER");
        browsebutton.setBackground(SystemColor.control);
        browsebutton.setFont(new Font("Garamond", Font.BOLD, 15));
        browsebutton.setIcon(new ImageIcon(DuplicateFileFinder.class.getResource("/images/assdcshjcb.jpg")));
        browsebutton.addActionListener(this);


        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.setBackground(new Color(0, 102, 102));
        buttonPanel.add(browsebutton);


        //Add the buttons and the list to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(new JScrollPane(list1), BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle Browse button action.
        if (e.getSource() == browsebutton) {
        	
            int returnVal = fc.showOpenDialog(DuplicateFileFinder.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
                // Hashmap to store the duplicate files
                Map<String, List<String>> lists = new HashMap<String, List<String>>();
                find(lists, file);
                
                //This is where a real application would open the file.
                listModel.addElement("Looking for Duplicates in Folder Name: " + file.getName() + " at location " + file.getParent());
                listModel.addElement("Double Click on File to open the Folder");
                
                for (List<String> list : lists.values()) {
                   if (list.size() > 1) {
                	   listModel.addElement("--");
                	   listModel.addElement("Duplicates Found");
                       for (String file1 : list) {
                           listModel.addElement(file1);
                       }
                   }
               }
               listModel.addElement("--");
               
               list1.addMouseListener(new MouseAdapter() {
            	    public void mouseClicked(MouseEvent evt) {
            	        JList list = (JList)evt.getSource();
            	        if (evt.getClickCount() == 2) {
            	            int index = list.locationToIndex(evt.getPoint());
            	            String str = listModel.getElementAt(index).toString();
            	            
            	            	if(new File(str)!=null)
					try {
						Desktop.getDesktop().open(new File(str).getParentFile());
					} catch (Exception e) {
						// Should not generate an exception!
					}
            	        } else if (evt.getClickCount() == 3) {   // Triple-click
            	            int index = list.locationToIndex(evt.getPoint());
            	            System.out.println(index);
            	        }            	       
            	    }           	    
            	});     
            } 
            else {
                log.append("Browse command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        } 
    }
    
    /**
     * Create the GUI and show it.
     */
    
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Duplicate File Finder");
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.getContentPane().add(new DuplicateFileFinder());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 500);
	}
    
	public static void find(Map<String, List<String>> lists, File dir) 
	{
        for (File f : dir.listFiles()) 
        {
            if (f.isDirectory()) 
            {
                find(lists, f);
            } 
            else 
            {
                    String hash = f.getName() + f.length();
                    List<String> list = lists.get(hash);
                    if (list == null) 
                    {
                    	
                    	// Create a linked list and add duplicate entries
                        list = new LinkedList<String>();
                        lists.put(hash, list);
                    }
                    list.add(f.getAbsolutePath());
                }
            }
        }

    public static void main(String[] args) {

        // Creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}
