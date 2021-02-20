package homeinventory;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.toedter.calendar.JDateChooser;
import java.beans.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;

public class HomeInventory extends JFrame
{

	private static final String DEPRECATION = "deprecation";
	//ToolBar
	JToolBar inventoryToolBar = new JToolBar();
	JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/new.png")));
	JButton deleteButton = new JButton(new ImageIcon(this.getClass().getResource("/delete.png")));
	JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/save.png")));
	JButton previousButton = new JButton(new ImageIcon(this.getClass().getResource("/previous.png")));
	JButton nextButton = new JButton(new ImageIcon(this.getClass().getResource("/next.png")));
	JButton printButton = new JButton(new ImageIcon(this.getClass().getResource("/print.png")));
	JButton exitButton = new JButton();
	
	//frame
	JLabel itemLabel = new JLabel();
	JTextField itemTextField = new JTextField();
	JLabel locationLabel = new JLabel();
	JComboBox locationComboBox = new JComboBox();
	JCheckBox markedCheckBox = new JCheckBox();
	JLabel serialLabel = new JLabel();
	JTextField serialTextField = new JTextField();
	JLabel priceLabel = new JLabel();
	JTextField priceTextField = new JTextField();
	JLabel dateLabel = new JLabel();
	JDateChooser dateDateChooser = new JDateChooser();
	JLabel storeLabel = new JLabel();
	JTextField storeTextField = new JTextField();
	JLabel noteLabel = new JLabel();
	JTextField noteTextField = new JTextField();
	JLabel photoLabel = new JLabel();
	static JTextArea photoTextArea = new JTextArea();
	JButton photoButton = new JButton();
	JPanel searchPanel = new JPanel();
	JButton[] searchButton = new JButton[26];
	PhotoPanel photoPanel = new PhotoPanel();
	
	static final int maximumEntries = 100;
	static int numberEntries;
	static InventoryItem[] myInventory = new InventoryItem[maximumEntries];
	int currentEntry;
	static final int entriesPerPage = 2;
	static int lastPage;
	
	
	@SuppressWarnings(DEPRECATION)
	public static void main(String[] args) {
		new HomeInventory().show();
	}
	
	public HomeInventory()
	{
		setTitle("Home Inventory Manager");
		setResizable(false);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				exitFrom(we);
			}
		});
		
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gridConstraints;
		
		inventoryToolBar.setFloatable(false);
		inventoryToolBar.setBackground(Color.ORANGE);
		inventoryToolBar.setOrientation(SwingConstants.VERTICAL);
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 0;
		gridConstraints.gridheight = 8;
		gridConstraints.fill = GridBagConstraints.VERTICAL;
		getContentPane().add(inventoryToolBar,gridConstraints);
		
		inventoryToolBar.addSeparator();
		
		Dimension bSize = new Dimension(70,50);
		newButton.setText("New");
		sizeButton(newButton, bSize);
		newButton.setToolTipText("Add New Item");
		newButton.setHorizontalTextPosition(SwingConstants.CENTER);
		newButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		inventoryToolBar.add(newButton);
		newButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				newButtonActionPerformed(ae);
			}
		});
		
		deleteButton.setText("Delete");
		sizeButton(deleteButton, bSize);
		deleteButton.setToolTipText("Delete Current Item");
		deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
		deleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		inventoryToolBar.add(deleteButton);
		deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				deleteButtonActionPerformed(ae);
			}
		});
		
		saveButton.setText("Save");
		sizeButton(saveButton, bSize);
		saveButton.setToolTipText("Save Current Item");
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		inventoryToolBar.add(saveButton);
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				saveButtonActionPerformed(ae);
			}
		});
		
		inventoryToolBar.addSeparator();
		
		previousButton.setText("Previous");
		sizeButton(previousButton, bSize);
		previousButton.setToolTipText("Display Previous Item");
		previousButton.setHorizontalTextPosition(SwingConstants.CENTER);
		previousButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		inventoryToolBar.add(previousButton);
		previousButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				previousButtonActionPerformed(ae);
			}
		});
		
		nextButton.setText("Next");
		sizeButton(nextButton, bSize);
		nextButton.setToolTipText("Display Next Item");
		nextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		nextButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		inventoryToolBar.add(nextButton);
		nextButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				nextButtonActionPerformed(ae);
			}
		});
		
		inventoryToolBar.addSeparator();
		
		printButton.setText("Print");
		sizeButton(printButton, bSize);
		printButton.setToolTipText("Print Inventory List");
		printButton.setHorizontalTextPosition(SwingConstants.CENTER);
		printButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		inventoryToolBar.add(printButton);
		printButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				printButtonActionPerformed(ae);
			}
		});
		
		exitButton.setText("Exit");
		sizeButton(exitButton, bSize);
		exitButton.setToolTipText("Exit Program");
		inventoryToolBar.add(exitButton);
		exitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				exitButtonActionPerformed(ae);
			}
		});
		
		//frame
		itemLabel.setText("Inventory Item");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 0;
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.insets = new Insets(10,10,0,10);
		getContentPane().add(itemLabel,gridConstraints);
		
		itemTextField.setPreferredSize(new Dimension(400,25));
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 2;
		gridConstraints.gridy = 0;
		gridConstraints.gridwidth = 5;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(itemTextField,gridConstraints);
		itemTextField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				itemTextFieldActionPerformed(ae);
			}
		});
		
		locationLabel.setText("Location");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 1;
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.insets = new Insets(10,10,0,10);
		getContentPane().add(locationLabel,gridConstraints);
		
		locationComboBox.setPreferredSize(new Dimension(270,25));
		locationComboBox.setFont(new Font("Arial",Font.PLAIN,12));
		locationComboBox.setEditable(true);
		locationComboBox.setBackground(Color.WHITE);
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 2;
		gridConstraints.gridy = 1;
		gridConstraints.gridwidth = 3;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(locationComboBox,gridConstraints);
		locationComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				locationComboBoxActionPerformed(ae);
			}
		});
		
		markedCheckBox.setText("Marked?");
		markedCheckBox.setFocusable(false);
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 5;
		gridConstraints.gridy = 1;
		gridConstraints.anchor = GridBagConstraints.WEST;
		gridConstraints.insets = new Insets(10,10,0,0);
		getContentPane().add(markedCheckBox,gridConstraints);
		
		serialLabel.setText("Serial Number");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 2;
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.insets = new Insets(10,10,0,10);
		getContentPane().add(serialLabel,gridConstraints);
		
		serialTextField.setPreferredSize(new Dimension(270,25));
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 2;
		gridConstraints.gridy = 2;
		gridConstraints.gridwidth = 3;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(serialTextField,gridConstraints);
		serialTextField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				serialTextFieldActionPerformed(ae);
			}
		});
		
		priceLabel.setText("Purchase Price");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 3;
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.insets = new Insets(10,10,0,10);
		getContentPane().add(priceLabel,gridConstraints);
		
		priceTextField.setPreferredSize(new Dimension(160,25));
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 2;
		gridConstraints.gridy = 3;
		gridConstraints.gridwidth = 2;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(priceTextField,gridConstraints);
		priceTextField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				priceTextFieldActionPerformed(ae);
			}
		});
		
		dateLabel.setText("Date Purchased");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 4;
		gridConstraints.gridy = 3;
		gridConstraints.anchor = GridBagConstraints.WEST;
		gridConstraints.insets = new Insets(10,10,0,0);
		getContentPane().add(dateLabel,gridConstraints);
		
		dateDateChooser.setPreferredSize(new Dimension(120,25));
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 5;
		gridConstraints.gridy = 3;
		gridConstraints.gridwidth = 2;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(dateDateChooser,gridConstraints);
		dateDateChooser.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent pe)
			{
				dateDateChooserPropertyChange(pe);
			}
		});
		
		storeLabel.setText("Store/Website");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 4;
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.insets = new Insets(10,10,0,10);
		getContentPane().add(storeLabel,gridConstraints);
		
		storeTextField.setPreferredSize(new Dimension(400,25));
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 2;
		gridConstraints.gridy = 4;
		gridConstraints.gridwidth = 5;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(storeTextField,gridConstraints);
		storeTextField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				storeTextFieldActionPerformed(ae);
			}
		});
		
		noteLabel.setText("Note");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 5;
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.insets = new Insets(10,10,0,10);
		getContentPane().add(noteLabel,gridConstraints);
		
		noteTextField.setPreferredSize(new Dimension(400,25));
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 2;
		gridConstraints.gridy = 5;
		gridConstraints.gridwidth = 5;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(noteTextField,gridConstraints);
		noteTextField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				noteTextFieldActionPerformed(ae);
			}
		});
		
		photoLabel.setText("Photo");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 6;
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.insets = new Insets(10,10,0,10);
		getContentPane().add(photoLabel,gridConstraints);
		
		photoTextArea.setPreferredSize(new Dimension(350,35));
		photoTextArea.setFocusable(false);
		photoTextArea.setLineWrap(true);
		photoTextArea.setWrapStyleWord(true);
		photoTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		photoTextArea.setFont(new Font("Arial", Font.PLAIN,12));
		photoTextArea.setEditable(false);
		photoTextArea.setBackground(Color.ORANGE);
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 2;
		gridConstraints.gridy = 6;
		gridConstraints.gridwidth = 4;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(photoTextArea,gridConstraints);
		
		photoButton.setText("...");
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 6;
		gridConstraints.gridy = 6;
		gridConstraints.insets = new Insets(10,0,0,10);
		gridConstraints.anchor = GridBagConstraints.WEST;
		getContentPane().add(photoButton,gridConstraints);
		photoButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				photoButtonActionPerformed(ae);
			}
		});
		
		searchPanel.setPreferredSize(new Dimension(240,160));
		searchPanel.setBorder(BorderFactory.createTitledBorder("Item Search"));
		searchPanel.setLayout(new GridBagLayout());
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 7;
		gridConstraints.gridwidth = 3;
		gridConstraints.insets = new Insets(10,0,10,0);
		gridConstraints.anchor = GridBagConstraints.CENTER;
		getContentPane().add(searchPanel,gridConstraints);
		
		int x=0, y=0;
		// create 26 buttons
		for(int i=0;i<26;i++)
		{
			searchButton[i] = new JButton();
			searchButton[i].setText(String.valueOf((char)(65+i)));
			searchButton[i].setFont(new Font("Arial", Font.PLAIN,12));
			searchButton[i].setMargin(new Insets(-10,-10,-10,-10));
			sizeButton(searchButton[i], new Dimension(37,27));
			searchButton[i].setBackground(Color.ORANGE);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = x;
			gridConstraints.gridy = y;
			searchPanel.add(searchButton[i],gridConstraints);
			searchButton[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					searchButtonActionPerformed(ae);
				}
			});
			x++;
			if(x%6==0)
			{
				x = 0;
				y++;
			}
		}
		
		
		photoPanel.setPreferredSize(new Dimension(240,160));
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 4;
		gridConstraints.gridy = 7;
		gridConstraints.gridwidth = 3;
		gridConstraints.insets = new Insets(10,0,10,10);
		gridConstraints.anchor = GridBagConstraints.CENTER;
		getContentPane().add(photoPanel,gridConstraints);
		
		pack();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((int)(0.5*(screenSize.width-getWidth())), (int)(0.5*(screenSize.height-getHeight())),getWidth(), getHeight());
		
		int n;
		//open file for entries
		try
		{
			BufferedReader inputFile = new BufferedReader(new FileReader("inventory.txt.txt"));
			numberEntries = Integer.valueOf(inputFile.readLine()).intValue();
			if(numberEntries != 0)
			{
				for(int i=0;i<numberEntries;i++)
				{
					myInventory[i] = new InventoryItem();
					myInventory[i].description = inputFile.readLine();
					myInventory[i].location = inputFile.readLine();
					myInventory[i].serialNumber = inputFile.readLine();
					myInventory[i].marked = Boolean.valueOf(inputFile.readLine()).booleanValue();
					myInventory[i].purchasePrice = inputFile.readLine();
					myInventory[i].purchaseDate = inputFile.readLine();
					myInventory[i].purchaseLocation = inputFile.readLine();
					myInventory[i].note = inputFile.readLine();
					myInventory[i].photoFile = inputFile.readLine();
				}
			}
			//read Combo box elements
			n = Integer.valueOf(inputFile.readLine()).intValue();
			if(n!=0) {
				for(int i=0;i<n;i++) {
					locationComboBox.addItem(inputFile.readLine());
				}
			}
			inputFile.close();
			currentEntry = 1;
			showEntry(currentEntry);
		}
		catch(Exception e)
		{
			numberEntries = 0;
			currentEntry = 0;
		}
		
		if(numberEntries == 0)
		{
			newButton.setEnabled(false);
			deleteButton.setEnabled(false);
			previousButton.setEnabled(false);
			nextButton.setEnabled(false);
			printButton.setEnabled(false);
		}
	}

	private void showEntry(int j) {
		// display entry j ( 1 to number Entries)
		itemTextField.setText(myInventory[j-1].description);
		locationComboBox.setSelectedItem(myInventory[j-1].location);
		markedCheckBox.setSelected(myInventory[j-1].marked);
		serialTextField.setText(myInventory[j-1].serialNumber);
		priceTextField.setText(myInventory[j-1].purchasePrice);
		dateDateChooser.setDate(stringToDate(myInventory[j-1].purchaseDate));
		storeTextField.setText(myInventory[j-1].purchaseLocation);
		noteTextField.setText(myInventory[j-1].note);
		showPhoto(myInventory[j-1].photoFile);
		nextButton.setEnabled(true);
		previousButton.setEnabled(true);
		if(j==1)
			previousButton.setEnabled(false);
		if(j==numberEntries)
			nextButton.setEnabled(false);
		itemTextField.requestFocus();
	}

	private void showPhoto(String photoFile) {
		if(!photoFile.equals(""))
		{
			try {
				photoTextArea.setText(photoFile);
			}
			catch(Exception e)
			{
				photoTextArea.setText("");
			}
		}
		else
		{
			photoTextArea.setText("");
		}
		photoPanel.repaint();
	}

	private Date stringToDate(String s) {
		int m = Integer.valueOf(s.substring(0,2)).intValue()-1;
		int d = Integer.valueOf(s.substring(3,5)).intValue();
		int y = Integer.valueOf(s.substring(6)).intValue()-1900;
		
		return (new Date(y,m,d));
	}

	private void searchButtonActionPerformed(ActionEvent ae) {
		int i;
		if(numberEntries==0)
			return;
		// search for item letter
		String letterClicked = ae.getActionCommand();
		i=0;
		do {
			if(myInventory[i].description.substring(0,1).equals(letterClicked)) {
				currentEntry =i+1;
				showEntry(currentEntry);
				return;
			}
			i++;
		}while(i<numberEntries);
		JOptionPane.showConfirmDialog(null, "No "+letterClicked+" inventory items found.","None Found",
				JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
	}

	private void photoButtonActionPerformed(ActionEvent ae) {
		JFileChooser openChooser = new JFileChooser();
		openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		openChooser.setDialogTitle("Open Photo File");
		openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Photo Files","png"));
		if(openChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
			showPhoto(openChooser.getSelectedFile().toString());
	}

	protected void noteTextFieldActionPerformed(ActionEvent ae) {
		photoButton.requestFocus();
	}

	protected void storeTextFieldActionPerformed(ActionEvent ae) {
		noteTextField.requestFocus();
	}

	private void dateDateChooserPropertyChange(PropertyChangeEvent pe) {
		storeTextField.requestFocus();
	}

	private void priceTextFieldActionPerformed(ActionEvent ae) {
		dateDateChooser.requestFocus();
	}

	private void serialTextFieldActionPerformed(ActionEvent ae) {
		priceTextField.requestFocus();
	}

	private void locationComboBoxActionPerformed(ActionEvent ae) {
		//if it is in list
		if(locationComboBox.getItemCount()!=0)
		{
			for(int i=0;i<locationComboBox.getItemCount();i++)
			{
				if(locationComboBox.getSelectedItem().toString().equals(locationComboBox.getItemAt(i).toString()));
				{
					serialTextField.requestFocus();
					return;
				}
			}
		}
		
		//if item not found in list
		locationComboBox.addItem(locationComboBox.getSelectedItem());
		serialTextField.requestFocus();
	}

	private void itemTextFieldActionPerformed(ActionEvent ae) {
		locationComboBox.requestFocus();
	}

	private void sizeButton(JButton b, Dimension d) {
		b.setPreferredSize(d);
		b.setMinimumSize(d);
		b.setMaximumSize(d);
	}

	private void exitButtonActionPerformed(ActionEvent ae) {
		exitFrom(null);
	}

	private void printButtonActionPerformed(ActionEvent ae) {
		lastPage = (int)(1+(numberEntries-1/entriesPerPage));
		PrinterJob inventoryPrinterJob = PrinterJob.getPrinterJob();
		inventoryPrinterJob.setPrintable(new InventoryDocument());
		if(inventoryPrinterJob.printDialog()) {
			try {
				inventoryPrinterJob.print();
			}
			catch(PrinterException e){
				JOptionPane.showConfirmDialog(null, e.getMessage(),"Print Error",
						JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void nextButtonActionPerformed(ActionEvent ae) {
		checkSave();
		currentEntry++;
		showEntry(currentEntry);
	}

	private void previousButtonActionPerformed(ActionEvent ae) {
		checkSave();
		currentEntry--;
		showEntry(currentEntry);
	}

	private void saveButtonActionPerformed(ActionEvent ae) {
		//check for description
		itemTextField.setText(itemTextField.getText().trim());
		if(itemTextField.getText().equals("")) {
			JOptionPane.showConfirmDialog(null, "Must have Item Description","Error",
					JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
			itemTextField.requestFocus();
			return;
		}
		if(newButton.isEnabled()) {
			//delete edit ently then resave
			deleteEntry(currentEntry);
		}
		//captilize the first letter
		String s= itemTextField.getText();
		itemTextField.setText(s.substring(0, 1).toUpperCase()+s.substring(1));
		numberEntries++;
		//determine the new current Entry
		currentEntry=1;
		if(numberEntries!=1) {
			do {
				if(itemTextField.getText().compareTo(myInventory[currentEntry-1].description)<0)
					break;
				currentEntry++;
			}while(currentEntry<numberEntries);
		}
		
		// move all entries below new value down one position unless at end
		if(currentEntry!=numberEntries) {
			for(int i=numberEntries;i>=currentEntry+1;i--) {
				myInventory[i-1]=myInventory[i-2];
				myInventory[i-2]=new InventoryItem();
			}
		}
		
		myInventory[currentEntry-1]=new InventoryItem();
		myInventory[currentEntry-1].description = itemTextField.getText();
		myInventory[currentEntry-1].location = locationComboBox.getSelectedItem().toString();
		myInventory[currentEntry-1].marked = markedCheckBox.isSelected();
		myInventory[currentEntry-1].serialNumber =serialTextField.getText() ;
		myInventory[currentEntry-1].purchasePrice = priceTextField.getText();
		myInventory[currentEntry-1].purchaseDate = dateToString(dateDateChooser.getDate());
		myInventory[currentEntry-1].purchaseLocation = storeTextField.getText();
		myInventory[currentEntry-1].photoFile = photoTextArea.getText();
		myInventory[currentEntry-1].note = noteTextField.getText();
		showEntry(currentEntry);
		if(numberEntries<maximumEntries)
			newButton.setEnabled(true);
		else
			newButton.setEnabled(false);
		deleteButton.setEnabled(true);
		printButton.setEnabled(true);
	}

	@SuppressWarnings(DEPRECATION)
	private String dateToString(Date date) {
		String yString = String.valueOf(date.getYear()+1900);
		@SuppressWarnings(DEPRECATION)
		int m=date.getMonth()+1;
		String mString = new DecimalFormat("00").format(m);
		int d =date.getDate();
		String dString = new DecimalFormat("00").format(d);
		return (dString+"/"+mString+"/"+yString);
	}

	private void deleteEntry(int j) {
		//delete Entry
		if(j!=numberEntries)
		{
			//move all entries under j up one level
			for(int i=j;i<numberEntries;i++)
			{
				myInventory[i-1]=new InventoryItem();
				myInventory[i-1]=myInventory[i];
			}
		}
		numberEntries--;
	}

	private void deleteButtonActionPerformed(ActionEvent ae) {
		if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this item?",
				"Delete Inventory Item",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.NO_OPTION)
		return;
		deleteEntry(currentEntry);
		if(numberEntries==0) {
			currentEntry=0;
			blankValues();
		}
		else{
			currentEntry--;
			if(currentEntry==0)
				currentEntry=1;
			showEntry(currentEntry);
		}
	}

	private void newButtonActionPerformed(ActionEvent ae) {
		checkSave();
		blankValues();
	}

	private void checkSave() {
		boolean edited = false;
		if(!(myInventory[currentEntry - 1]).description.equals(itemTextField.getText()))
			edited = true;
		else if(!(myInventory[currentEntry - 1]).location.equals(locationComboBox.getSelectedItem().toString()))
			edited = true;
		else if(myInventory[currentEntry - 1].marked!=markedCheckBox.isSelected())
			edited = true;
		else if(!(myInventory[currentEntry - 1]).serialNumber.equals(serialTextField.getText()))
			edited = true;
		else if(!(myInventory[currentEntry - 1]).purchasePrice.equals(priceTextField.getText()))
			edited = true;
		else if(!(myInventory[currentEntry - 1]).purchaseDate.equals(dateToString(dateDateChooser.getDate())))
			edited = true;
		else if(!(myInventory[currentEntry - 1]).purchaseLocation.equals(storeTextField.getText()))
			edited = true;
		else if(!(myInventory[currentEntry - 1]).note.equals(noteTextField.getText()))
			edited = true;
		else if(!(myInventory[currentEntry - 1]).photoFile.equals(photoTextArea.getText()))
			edited = true;
		
		if(edited) {
			if(JOptionPane.showConfirmDialog(null,"You have edited item.\nDo you want to save changes?",
				"Save Item",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
				saveButton.doClick();
		}
		
	}

	private void blankValues() {
		//blank input scree
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
		saveButton.setEnabled(true);
		previousButton.setEnabled(false);
		nextButton.setEnabled(false);
		printButton.setEnabled(false);
		itemTextField.setText("");
		locationComboBox.setSelectedItem("");
		markedCheckBox.setSelected(false);
		serialTextField.setText("");
		priceTextField.setText("");
		dateDateChooser.setDate(new Date());
		storeTextField.setText("");
		noteTextField.setText("");
		photoTextArea.setText("");
		photoPanel.repaint();
		itemTextField.requestFocus();
	}

	private void exitFrom(WindowEvent we) {
		if(JOptionPane.showConfirmDialog(null,"Any unsaved changes will lost.\nAre you sure you want to exit?",
				"Exit Program",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.NO_OPTION)
		return;
		
		//write entries back to file
		try
		{
			PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter("inventory.txt.txt")));
			outputFile.println(numberEntries);
			if(numberEntries != 0)
			{
				for(int i=0;i<numberEntries;i++)
				{
					outputFile.println(myInventory[i].description);
					outputFile.println(myInventory[i].location);
					outputFile.println(myInventory[i].serialNumber);
					outputFile.println(myInventory[i].marked);
					outputFile.println(myInventory[i].purchasePrice);
					outputFile.println(myInventory[i].purchaseDate);
					outputFile.println(myInventory[i].purchaseLocation);
					outputFile.println(myInventory[i].note);
					outputFile.println(myInventory[i].photoFile);
				}
			}
			//write Combo box elements
			outputFile.println(locationComboBox.getItemCount());
			if(locationComboBox.getItemCount() !=0) {
				for(int i=0;i<locationComboBox.getItemCount();i++) {
					outputFile.println(locationComboBox.getItemAt(i));
				}
			}
			outputFile.close();
		}
		catch(Exception e)
		{
			
		}
		System.exit(0);
	}

}
