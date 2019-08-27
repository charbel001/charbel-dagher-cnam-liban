package server;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextArea;

public class ServerWithGUI extends javax.swing.JFrame 
{
  
	private static final long serialVersionUID = 1L;
ArrayList<PrintWriter> clientOutputStreams = new ArrayList<>();
   ArrayList<String> users = new ArrayList<>();

   public class ClientHandler implements Runnable	
   {
       BufferedReader reader;
       Socket sock;
       PrintWriter client;
       int id_privateChat ;

       public ClientHandler(Socket clientSocket, PrintWriter user)
       {
            client = user;
            try 
            {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            }
            catch (Exception ex) 
            {
                ta_chat.append("Unexpected error... \n");
            }

       }

       public void run() 
       {
            String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat" , privatemsg = "private" ;
            String[] data;

            try 
            {
                while ((message = reader.readLine()) != null) 
                {
                   
                    ta_chat.append("\nReceived: " + message + "\n");
                    data = message.split(":");
                    int counter2steps = 0;
                    for (String token:data) 
                    {
                    	if(counter2steps!=2)
                        ta_chat.append(token + " ");
                        counter2steps++;
                    }

                    if (data[2].equals(connect)) 
                    {
                        tellEveryone((data[0] + ":" + data[1] + ":" + chat));
                        userAdd(data[0]);
                    } 
                    else if (data[2].equals(disconnect)) 
                    {
                        tellEveryone((data[0] + "  has :Disconnected" + ":" + chat));
                        clientOutputStreams.remove(getid(data[0]));
                        userRemove(data[0]);
                    } 
                    else if (data[2].equals(chat)) 
                    {
                        tellEveryone(message);
                    } 
                    else if (data[2].equals(privatemsg)) {
                        
                        int recievedID  = getid(data[3]);
                       if (recievedID != -1) {
                        	tellthispersononly(message, recievedID, data[3]);
						} else {
							tellthispersononly(message, recievedID, data[0]);
						}
                        
                        
                    }else if (data[2].equals("request")) {
                        
                        int recievedID  ;
                        StringBuilder stringBuilder = new StringBuilder();
                             for (String current_user : users)
                     {
                                recievedID  = getid(current_user);
                                stringBuilder.append(current_user).append(" , ");
                    }           
                             recievedID  = getid(data[0]);
                             String finalString = stringBuilder.toString();
                             finalString = data[0]+ ":" +finalString+ ":" + "request" ;
                             tellthispersononly(finalString, recievedID, data[0]);
                    }
                    else 
                    {
                        ta_chat.append("No Conditions were met. \n");
                    }
                } 
             } 
             catch (Exception ex) 
             {
                ta_chat.append("Lost the connection. \n");
             } 
	} 
    }

    public ServerWithGUI() 
    {
    	setBackground(Color.LIGHT_GRAY);
        initComponents();
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
    }
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        ta_chat.setForeground(Color.BLACK);
        ta_chat.setBackground(SystemColor.info);
        b_start = new javax.swing.JButton();
        b_start.setFont(new Font("Tahoma", Font.BOLD, 11));
        b_start.setBackground(Color.YELLOW);
        b_end = new javax.swing.JButton();
        b_end.setFont(new Font("Tahoma", Font.BOLD, 11));
        b_end.setBackground(Color.YELLOW);
        b_users = new javax.swing.JButton();
        b_users.setFont(new Font("Tahoma", Font.BOLD, 11));
        b_users.setBackground(Color.YELLOW);
        b_clear = new javax.swing.JButton();
        b_clear.setFont(new Font("Tahoma", Font.BOLD, 11));
        b_clear.setBackground(Color.YELLOW);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setName("server");
        setResizable(false);

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_start.setText("RUN SERVER");
        b_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_startActionPerformed(evt);
            }
        });

        b_end.setText("_SHUTDOWN");
        b_end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_endActionPerformed(evt);
            }
        });

        b_users.setText("_WHO");
        b_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_usersActionPerformed(evt);
            }
        });

        b_clear.setText("DELETE HIST");
        b_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(b_start, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_users, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_end, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_start, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_users, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_end, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }

    private void b_endActionPerformed(java.awt.event.ActionEvent evt) {
        try 
        {
            ta_chat.append("\n");
            ta_chat.append("\n");
            ta_chat.append("\n");
            ta_chat.append("\n");
            tellEveryone("Server: is stopping and all users will be disconnected:Chat");
            ta_chat.append("Server stopping ... \n");
            ta_chat.setText("");
            ta_chat.setText("closing everything ...");
            Thread.sleep(500);
            System.exit(0);
            
        } 
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}
        
        tellEveryone("Server:is stopping and all users will be disconnected.\n:Chat");
        ta_chat.append("Server stopping... \n");
        
        ta_chat.setText("");
    }

    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {
        Thread starter = new Thread(new ServerStart());
        starter.start();
        ta_chat.setEditable(false);
        ta_chat.append(" Server has been started \n Waiting for connection ...");
        changetxtareafontsize(ta_chat) ;
    }

    private void b_usersActionPerformed(java.awt.event.ActionEvent evt) {
        ta_chat.append("\n Online users : \n");
        if(!users.isEmpty())
        {
            
        
            for (String current_user : users)
            {
            ta_chat.append(current_user + " , " );
            ta_chat.append("\n");
            }    
        }else {
        	ta_chat.append(" No one is online ...");
        }
    }

    private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {
        ta_chat.setText("");
    }

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new ServerWithGUI().setVisible(true);
            }
        });
    }
    
    public class ServerStart implements Runnable 
    {
        
		@Override
        public void run() 
        {
            try 
            {
                @SuppressWarnings("resource")
				ServerSocket serverSock = new ServerSocket(6000);

                while (true) 
                {
				Socket clientSock = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
                                clientOutputStreams.add(writer);
				Thread listener = new Thread(new ClientHandler(clientSock, writer));
				listener.start();
				ta_chat.append("Got a connection. \n");
				              
                }
            }
            catch (Exception ex)
            {
                ta_chat.append("Error making a connection. \n");
            }
        }
    }
    
    public int getid(String data){
        
       int userid = users.indexOf(data);
        return userid ;
    }
    
    public void userAdd (String data) 
    {
        String message, s = ": :Connect", done = "Server: :Done", name = data;
        users.add(name); 
        for (String token:users)
        {
            message = (token + s);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void userRemove (String data) 
    {
        @SuppressWarnings("unused")
		String message, s = ": :Disconnect", done = "Server: :Done", name = data;
        users.remove(name);
        for (String UserName:users) 
        {
            message = (UserName + s);
            tellEveryone(message);
        }
    }
    
    public void tellthispersononly(String msg , int personid, String recievername){
        
    if (personid == -1) {
    	
		msg =  "The User is Not Found in the online users your private message has not been deliverd";
		personid=getid(recievername);
		try 
        {
            PrintWriter writer =   clientOutputStreams.get(personid);
            writer.println(msg);
            writer.flush();
            ta_chat.append("Sending to {"+recievername+"} only this msg : Message not sent because the User not found in the online Users \n");
            ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
        }catch (Exception ex) 
        {
        	ta_chat.append("Error in telling this to "+ recievername +"." +"\n");
        }
		
	} else {

		
    	
        if (clientOutputStreams.get(personid)!= null) {
                
            try 
            {
                PrintWriter writer =    clientOutputStreams.get(personid);
                writer.println(msg);
                writer.flush();
		ta_chat.append("Sending to {"+recievername+"} only this msg :  " + msg + "\n");
                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
            }catch (Exception ex) 
            {
		ta_chat.append("Error in telling this "+ recievername +"." +"\n");
            }
        }
        else
        {
            ta_chat.append("Error in telling this ... his ID not found OR His outputstream is null "+ recievername +"." +"\n");
        }
    		}
    }
    
    public void changetxtareafontsize(JTextArea txtarea){
    	Font font1 = new Font("SansSerif", Font.BOLD, 12);
    	txtarea.setFont(font1);
    }
    
    public void tellEveryone(String message) 
    {
	 @SuppressWarnings("rawtypes")
	Iterator it = clientOutputStreams.iterator();
        
       while (it.hasNext()) 
        {
            try 
            {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

            } 
            catch (Exception ex) 
            {
		ta_chat.append("Error telling everyone. \n");
            }
        } 
    }
    private javax.swing.JButton b_clear;
    private javax.swing.JButton b_end;
    private javax.swing.JButton b_start;
    private javax.swing.JButton b_users;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea ta_chat;
}
