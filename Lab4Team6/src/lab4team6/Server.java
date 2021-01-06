/* 
Project: Lab 4 Group Work
Purpose Details: Get and Post
Course: IST 411
Author: Team X
Date Developed: 2/10/2020
Last Date Changed:
Revision: 1
*/

package lab4team6;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Server {
    
    
    
    public static void main(String[] args) throws Exception {
        
        
        //Print message that server has started
        System.out.println("Server Started");
        
        //Create new HttpServer object using the local host and port 80
        HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
        
        //Associate /index path with an IndexHandler instance
        server.createContext("/index", new IndexHandler());
        
        //Start the server
        server.start();
    }
    
    static class IndexHandler implements HttpHandler {
        
        //Override the handle method of the HttpHandler class
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            
            //Create a new ArrayList of Strings
            ArrayList<String> list = new ArrayList<String>();
            
            //Create a string for the file name
            String FileName = "diary.ser";
            
            //Create a new file input stream object
            FileInputStream fis = null;
            
            //Create a new object input stream
            ObjectInputStream in = null;
            
            //Try statement to read file
            try {
                
                    //Set the file input stream equal to the chose file name
                    fis = new FileInputStream(FileName);
                    
                    //Set the object input stream object equal to a new object input stream 
                    //using the file input stream
                    in = new ObjectInputStream(fis);
                    
                    //Read the file into the array list
                    list = (ArrayList)in.readObject();
                    
                    //Close the object input stream
                    in.close();
                }
                //Catch exceptions
                catch(IOException ex) {
                    ex.printStackTrace();
                }
                catch(ClassNotFoundException ex) {
                
                    ex.printStackTrace();
                } 
            
            //Run this block if a GET request is sent
            if (exchange.getRequestMethod().equals("GET"))
            {
                
                //Print out the request method
                System.out.println(exchange.getRequestMethod());
                
                //Print out the Remote Address
                System.out.println(exchange.getRemoteAddress());
                                
                //Create a new String and set it equal to the response
                String response = getResponse(list);
                
                //Call the sendResponseHeaders method and
                //send a response code of 200 and the length of the message (in bytes)
                exchange.sendResponseHeaders(200, response.length());

                //Create a new output stream and set it equal to the response message received
                OutputStream out = exchange.getResponseBody();
                
                //Write the response message to the output stream
                out.write(response.getBytes());
                
                //Close the output stream
                out.close();
            }
            
            //Run this block if a POST request is sent
            else if(exchange.getRequestMethod().equals("POST"))
            {
                //Create a new file output stream
                FileOutputStream fos = null;
                
                //Create a new object output stream
                ObjectOutputStream outfile = null;
                
                //Create buffered reader to read in the user's diary entry
                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                //Print out the request method
                System.out.println(exchange.getRequestMethod());
                
                //Print out the Remote Address to server console
                System.out.println(exchange.getRemoteAddress());
                

                
                //Create a new String and set it equal to OK
                String response = "OK";
                
                //Call the sendResponseHeaders method and
                //send a response code of 200 and the length of the message (in bytes)
                exchange.sendResponseHeaders(200, response.length());
                
                //Create a new user input string and read the buffer reader into it
                String userInput = br.readLine();
                
                //Add the user's input to the array
                list.add(userInput);
                
                
                //System.out.println(userInput);
                //Try statement for creating output file
                try
                {
                    
                    //Set the file output stream
                    fos = new FileOutputStream(FileName);
                    
                    //Set the object output stream
                    outfile = new ObjectOutputStream(fos);
                    
                    //Write the contents of the list array to the file
                    outfile.writeObject(list);
                    
                    //Close the file
                    outfile.close();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
                //Create a new output stream and set it equal to the response message received
                OutputStream out = exchange.getResponseBody();

                
                //PrintWriter pw = new PrintWriter(exchange.getResponseBody());
                
                //Send the response to the client
                out.write(response.getBytes());
                
                //Close the output stream
                out.close();

            }
                    

        }
        
        public String getResponse(ArrayList<String> list) {
            
            //Create a new blank String
            String responseBuffer = "";
            
            //For statement to get all of the items in the array
            for(int i=0; i < list.size(); i++ ) {
                
                //String responseBuffer will have contents of the entire Array.
                //&n is used by the client later to denote line breaks.
                responseBuffer = responseBuffer + list.get(i) + "&n";
               
            }

            //Return the responseBuffer
            return responseBuffer;
        }


    }
}
