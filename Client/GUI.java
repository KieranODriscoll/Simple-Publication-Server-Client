import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {
	
	JFrame clientFrame;
	private FlowLayout frameLayout;
	
	private JPanel topPanel;
	private JPanel topPanelBuffer;
	private FlowLayout topLayout;
	JButton connectButton;
	private JTextField addressField;
	JButton disconnectButton;
	
	private JPanel leftPanel;
	private FlowLayout leftLayout;
	private JLabel requestLabel;
	private JPanel leftBuffer0;	
	private JLabel requestAction;
	private String[] actionTitles;
	private JComboBox<String> actionDrop;
	private JPanel leftBuffer1;
	private JLabel requestHeader;
	private JPanel leftBuffer2;
	private JLabel labelISBN;
	private JTextArea requestISBN;
	private JPanel leftBuffer3;
	private JLabel labelTitle;
	private JTextArea requestTitle;
	private JPanel leftBuffer4;
	private JLabel labelAuthor;
	private JTextArea requestAuthor;
	private JPanel leftBuffer5;
	private JLabel labelPublisher;
	private JTextArea requestPublisher;
	private JPanel leftBuffer6;
	private JLabel labelYear;
	private JTextArea requestYear;
		
	private JPanel midPanel;
	private BorderLayout midLayout;
	JButton sendButton;
	
	private JPanel rightPanel;
	private FlowLayout rightLayout;
	private JLabel responseHeader;
	private JPanel rightBuffer1;
	private JCheckBox bibtexOption;
	private JTextArea responseBody;
	private JScrollPane responseScroll;
	private JPanel rightBuffer2;
	private JLabel errorHeader;
	private JTextArea errorMessage;
	
	public GUI(){
			
			newClient();
		}
	
	
	public void newClient(){
			
			/*
			 * Create client frame
			 */
			clientFrame = new JFrame("Bibliographic List Management Protocol - Client");
			frameLayout = new FlowLayout();
			clientFrame.setLayout(frameLayout);
			clientFrame.setSize(680,520);
			clientFrame.setLocation(50, 50);
			clientFrame.setVisible(true);
			clientFrame.setResizable(false);
			clientFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent windowEvent){
		            System.exit(0);
	     		}        
			});
	
			topPanel = new JPanel();
			topLayout = new FlowLayout();
			topPanel.setLayout(topLayout);
			//topPanel.setBackground(new Color(0, 0, 0));
			topPanel.setPreferredSize(new Dimension(640,80));
			topPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			
			topPanelBuffer = new JPanel();
			topPanelBuffer.setPreferredSize(new Dimension(640,20));
			topPanel.add(topPanelBuffer);
			
			connectButton = new JButton("Connect");
			connectButton.setEnabled(false);
			topPanel.add(connectButton);
			
			
			addressField = new JTextField(30);
			addressField.setText("IP Address:Port");
			addressField.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
					addressField.setText(""); 
					connectButton.setEnabled(true);
				}
				public void focusLost(FocusEvent e) {
				}
			}); 
			topPanel.add(addressField);
			
			disconnectButton = new JButton("Disconnect");
			disconnectButton.setEnabled(false);
			topPanel.add(disconnectButton);
			
			/*
			 * Fill Left Panel
			 */
			leftPanel = new JPanel();
			leftLayout = new FlowLayout();
			leftPanel.setLayout(leftLayout);
			//leftPanel.setBackground(new Color(255, 0, 0));
			leftPanel.setPreferredSize(new Dimension(280,380));
			
			requestLabel = new JLabel("Request:");
			leftPanel.add(requestLabel);
			leftBuffer0 = new JPanel();
			leftBuffer0.setPreferredSize(new Dimension(280,2));
			leftPanel.add(leftBuffer0);
			
			requestAction = new JLabel("Action: ");
			leftPanel.add(requestAction);
			
			actionTitles = new String[] {"SUBMIT", "UPDATE", "REMOVE", "GET"};		
			actionDrop = new JComboBox<String>(actionTitles);
			leftPanel.add(actionDrop);

			leftBuffer1 = new JPanel();
			leftBuffer1.setPreferredSize(new Dimension(280,2));
			leftPanel.add(leftBuffer1);
			
			requestHeader = new JLabel("<html>Bibliographic List Management Protocol - v1.0<br>" + "Connected to: ");
			leftPanel.add(requestHeader);
			
			//ISBN
			leftBuffer2 = new JPanel();
			leftBuffer2.setPreferredSize(new Dimension(280,2));
			leftPanel.add(leftBuffer2);
			
			labelISBN = new JLabel("ISBN: ");
			leftPanel.add(labelISBN);
			
			requestISBN = new JTextArea(1,24);
			leftPanel.add(requestISBN);
			
			//Title
			leftBuffer3 = new JPanel();
			leftBuffer3.setPreferredSize(new Dimension(280,2));
			leftPanel.add(leftBuffer3);
			
			labelTitle = new JLabel("Title: ");
			leftPanel.add(labelTitle);
			
			requestTitle = new JTextArea(2,24);
			requestTitle.setLineWrap(true);
			leftPanel.add(requestTitle);
			
			//Author
			leftBuffer4 = new JPanel();
			leftBuffer4.setPreferredSize(new Dimension(280,2));
			leftPanel.add(leftBuffer4);
			
			labelAuthor = new JLabel("Author: ");
			leftPanel.add(labelAuthor);
			
			requestAuthor = new JTextArea(1,24);
			leftPanel.add(requestAuthor);
					
			//Publisher
			leftBuffer5 = new JPanel();
			leftBuffer5.setPreferredSize(new Dimension(280,2));
			leftPanel.add(leftBuffer5);
			
			labelPublisher = new JLabel("Publisher: ");
			leftPanel.add(labelPublisher);
			
			requestPublisher = new JTextArea(1,24);
			leftPanel.add(requestPublisher);
			
			//Year
			leftBuffer6 = new JPanel();
			leftBuffer6.setPreferredSize(new Dimension(280,2));
			leftPanel.add(leftBuffer6);
			
			labelYear = new JLabel("Year: ");
			leftPanel.add(labelYear);
			
			requestYear = new JTextArea(1,24);
			leftPanel.add(requestYear);

			/*
			 * Fill Middle Panel 
			 */
			midPanel = new JPanel();
			midLayout = new BorderLayout();
			midPanel.setLayout(midLayout);
			//midPanel.setBackground(new Color(0, 255, 0));
			midPanel.setPreferredSize(new Dimension(80,380));
			
			sendButton = new JButton("Send");
			sendButton.setEnabled(false);
			midPanel.add(sendButton);
			
			/*
			 * Fill Right Panel 
			 */
			rightPanel = new JPanel();
			rightLayout = new FlowLayout();
			rightPanel.setLayout(rightLayout);
			//rightPanel.setBackground(new Color(0, 0, 255));
			rightPanel.setPreferredSize(new Dimension(280,380));
			
			//Title
			responseHeader = new JLabel("Response: ");
			rightPanel.add(responseHeader);
			
			rightBuffer1 = new JPanel();
			rightBuffer1.setPreferredSize(new Dimension(280,5));
			rightPanel.add(rightBuffer1);
			
			//Bibtex Request Option
			bibtexOption = new JCheckBox("GET returns BibTeX response");
			bibtexOption.setSelected(false);
			rightPanel.add(bibtexOption);
			
			//Response Body
			responseBody = new JTextArea(12, 23);
			responseBody.setLineWrap(true);
		    responseBody.setEditable(false); // set textArea non-editable
		    responseScroll = new JScrollPane(responseBody);
		    responseScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		    responseScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		    rightPanel.add(responseScroll);
		    
		    rightBuffer2 = new JPanel();
			rightBuffer2.setPreferredSize(new Dimension(280,5));
			rightPanel.add(rightBuffer2);
			
			//Error Message
			errorHeader = new JLabel("Client Status: ");
			rightPanel.add(errorHeader);
			errorMessage = new JTextArea(6,24);
			errorMessage.setEditable(false);
			rightPanel.add(errorMessage);
			
			/*
			 * Add panels to frame and redraw
			 */
			clientFrame.add(topPanel);
			clientFrame.add(leftPanel);
			clientFrame.add(midPanel);
			clientFrame.add(rightPanel);
			clientFrame.validate();
	        clientFrame.repaint();

		}


	public String getAddress() {
		return this.addressField.getText();
		
	}
	
	public String getRequestAction() {
		return actionTitles[this.actionDrop.getSelectedIndex()];
	}
	
	public void setHeader(String address) {
		requestHeader.setText("<html>Bibliographic List Management Protocol - v1.0<br>" + "Connected to: " + address);
	}
	
	public String getHeader() {
		return requestHeader.getText();
	}

	public String getRequestISBN() {
		return this.requestISBN.getText();
		
	}
	
	public void setRequestISBN(String s) {
		requestISBN.setText(s);
		
	}
	
	public String getRequestTitle() {
		return this.requestTitle.getText();
		
	}
	
	public void setRequestTitle(String s) {
		requestISBN.setText(s);
		
	}

	public String getRequestAuthor() {
		return this.requestAuthor.getText();
		
	}
	
	public void setRequestAuthor(String s) {
		requestISBN.setText(s);
		
	}
	
	public String getRequestPublisher() {
		return this.requestPublisher.getText();
		
	}
	
	public void setRequestPublisher(String s) {
		requestISBN.setText(s);
		
	}
	
	public String getRequestYear() {
		return this.requestYear.getText();
		
	}
	
	public void setRequestYear(String s) {
		requestISBN.setText(s);
		
	}
	
	public boolean getBibtex() {
		
		return bibtexOption.isSelected();
	}
	
	public void setResponse(String response) {
		responseBody.setText(response);
		return;
	}
	
	public void setError(String error) {
		errorMessage.setText(error);
		
		return;
	}
	
	public void redraw() {
		clientFrame.validate();
		clientFrame.repaint();
	}
}
