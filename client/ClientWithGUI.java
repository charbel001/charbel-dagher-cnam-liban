package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ClientWithGUI extends javax.swing.JFrame 
{
    
	private static final long serialVersionUID = 1L;
	String username, address;
    @SuppressWarnings({ "unchecked", "rawtypes" })
	ArrayList<String> users = new ArrayList();
    int port = 6000;
    Boolean isConnected = false;
    
    Socket sock;
    BufferedReader reader;
    PrintWriter writer;
    public void ListenThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());
         IncomingReader.start();
    }
    
    public void userAdd(String data) 
    {
         users.add(data);
    }
    public void userRemove(String data) 
    {
         users.remove(data);
         
    }
    

    
    public void sendDisconnect() 
    {
        String Disconnect = (username + ": :Disconnect");
        try
        {
            writer.println(Disconnect); 
            writer.flush(); 

        } catch (Exception e) 
        {
            ta_chat.append("Could not send Disconnect message.\n");
        }
    }
    public void Disconnect() 
    {
        try 
        {
            ta_chat.append("Disconnected.\n");
            isConnected = false;
        } catch(Exception ex) {
            ta_chat.append("Failed to disconnect. \n");
        }
        

    }
    
    public ClientWithGUI() 
    {
    	getContentPane().setForeground(new Color(0, 0, 0));
        initComponents();
        this.getContentPane().setBackground(new Color(211, 211, 211));
    }
    

    public class IncomingReader implements Runnable
    {
        
        @Override
        public void run() 
        {
            String[] data;
            String stream, connect = "Connect", disconnect = "Disconnect", chat = "Chat" , privatemsg = "private";

            try 
            {
                while ((stream = reader.readLine()) != null) 
                {
                     data = stream.split(":");

                     if (data[2].equals(chat)) 
                     {
                        ta_chat.append(data[0] + ": " + data[1] + "\n");
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                     } 
                     else if (data[2].equals(connect))
                     {
                        ta_chat.removeAll();
                        userAdd(data[0]);
                     } 
                     else if (data[2].equals(disconnect)) 
                     {
                         userRemove(data[0]);
                     } 
                     else if(data[2].equals(privatemsg)){
                         ta_chat.append("Private Message from " + data[0] + " : " + data[1] +"\n");
                         ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                     }
                     else if(data[2].equals("request"))
                     {
                        ta_chat.append(" Server replied " + "\n" + data[1] +"\n");
                         ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                    }
                }
           }catch(Exception ex) { }
        }
    }
    private void initComponents() {

        lb_username = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        ta_chat.setEditable(false);
        ta_chat.setBackground(SystemColor.info);
        tf_chat = new javax.swing.JTextField();
        tf_chat.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if(e.getKeyCode()== KeyEvent.VK_ENTER){
        			send_it();
        		}
        	}
        });
        tf_chat.setBackground(SystemColor.info);
        b_send = new javax.swing.JButton();
        b_send.setBackground(Color.yellow);
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Client's frame");
        setBackground(java.awt.SystemColor.textHighlight);
        setForeground(new java.awt.Color(0, 51, 51));
        setName("client");
        setResizable(false);
        lb_username.setText("Please connect to the server : _connect <surnom> <machine> <port> ");
        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_send.setText("SEND");
        b_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sendActionPerformed(evt);
            }
        });

        jLabel2.setText("Conversation Area");



        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 473, GroupLayout.PREFERRED_SIZE)
        						.addComponent(tf_chat, 473, 473, 473))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(b_send, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
            							.addComponent(lb_username, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        							.addGap(18)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING, false)))
        						.addComponent(jLabel2))
        					.addPreferredGap(ComponentPlacement.RELATED, 320, Short.MAX_VALUE)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lb_username))
        			.addGap(8)
        			.addComponent(jLabel2)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(15))
        				.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(tf_chat, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        				.addComponent(b_send, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }
    private void b_connect(String usercon) {
        if (isConnected == false) 
            {
        if(usercon.length()!=0){
            
           if(!users.contains(usercon)){
        	 username = usercon;
            ta_chat.setEditable(false);

            try 
            {
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());
                writer.println(username + ": has connected :Connect");
                writer.flush(); 
                isConnected = true; 
                changetxtareafontsize(ta_chat) ;
            } 
            catch (Exception ex) 
            {
                ta_chat.append("Cannot Connect! Try Again. \n");

            }
            
            ListenThread();
            
            } else{
             JOptionPane.showMessageDialog(null, "Write another name for your Client, this name is already taken", "Duplicate name found !!!", JOptionPane.ERROR_MESSAGE);

                   }
        } else{         
                    JOptionPane.showMessageDialog(null, "Write a name for your Client first", "Missing Field !!!", JOptionPane.ERROR_MESSAGE);

           }
        
       }else if (isConnected == true) 
        {
            ta_chat.append("You are already connected. \n");
        }
        
    }

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {
        
        send_it();
    }

    private void onlineUsers(){
        ta_chat.append("\n Online users : \n");
        try {
               writer.println(username + ":" + "Request to know who is online " + ":" + "request" + ":" + username );
               writer.flush();
            } catch (Exception ex) {
                ta_chat.append("Message was not sent. \n");
            }
        
    }
    public void send_it(){
    	
    	String nothing = "";
        String mydata = tf_chat.getText() ;
        Pattern pattern = Pattern.compile("(@).*\\1");
        Matcher matcher = pattern.matcher(mydata);
        if(mydata.startsWith("_connect")){
            String [] connection = mydata.split(" ");
            if(connection.length < 4){
                ta_chat.append("Commande invalide. Usage: _connect <surnom> <machine> <port>");
              }
            else {
            username = connection[1];
            lb_username.setText("Profile : " + username);
            address = connection[2];
            b_connect(username);
          }
        }
          
        
        if (mydata.equals("_who")){
            onlineUsers();
    }  
        if (mydata.equals("_quit")){
            sendDisconnect();
            Disconnect();
        lb_username.setText("Please connect to the server with a new profile : _connect <surnom> <machine> <port> ");
    }
        if ((tf_chat.getText()).equals(nothing)) {
            tf_chat.setText("");
            tf_chat.requestFocus();
        } 
        else if (matcher.find()) {
        	
            String[] data = mydata.split("@");
            try {
               writer.println(username + ":" + data[2] + ":" + "private" + ":" + data[1] );
               writer.flush();
               ta_chat.append("You have sent {  "+data[2]+"  } as a private message to : " + "'"+data[1]+ "'" +"\n");
            } catch (Exception ex) {
                ta_chat.append("Message was not sent. \n"+"No online users found by that name  ");
            }
            
        }
        else if (!mydata.equals("_quit") && !mydata.equals("_who") ){
            try {
               writer.println(username + ":" + tf_chat.getText() + ":" + "Chat");
               writer.flush();
            } catch (Exception ex) {
                ta_chat.append("Message was not sent. \n");
            }
            tf_chat.setText("");
            tf_chat.requestFocus();
        }

        tf_chat.setText("");
        tf_chat.requestFocus();
    	
    }
    

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new ClientWithGUI().setVisible(true);
            }
        });
    }
    
    public void changetxtareafontsize(JTextArea txtarea){
    	Font font1 = new Font("SansSerif", Font.BOLD, 11);
    	txtarea.setFont(font1);
    }
    private javax.swing.JButton b_send;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_username;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField tf_chat;
}
