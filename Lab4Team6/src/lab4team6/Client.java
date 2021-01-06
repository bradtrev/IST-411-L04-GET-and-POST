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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Client {

    String userInput = "Not going to exit on start";

    public static void main(String[] args) throws Exception {

        //Create a new client object called http
        Client http = new Client();

        while (true) {
            http.sendPost();
            http.sendGet();
        }

    }

    private void sendGet() throws Exception {

        //Create a new String with the website address to connect to
        String query = "http://127.0.0.1/index";

        //Create a new URL object equal to the query above
        URL url = new URL(query);

        //Create a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //Set the request method to GET
        connection.setRequestMethod("GET");

        //Set the request property
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        //Create a new integer and set it equal to the response code
        int responseCode = connection.getResponseCode();

        //Print out the response code received
        System.out.println("GET: Response Code: " + responseCode);

        //See if a valid (200) response code was received
        if (responseCode == 200) {
            //if a valid response code was received, print out the response
            String response = getResponse(connection);
            String newresponse = response.replace("&n", System.lineSeparator());
            System.out.println("GET Response Message: ");
            System.out.println(newresponse);
        } else {
            //if an invalid response code was received, print out the response code
            System.out.println("Bad Response Code: " + responseCode);
        }
    }

    private void sendPost() throws Exception {

        System.out.println("Type EXIT to quit program");

        //Create a new String with the website address to connect to
        String query = "http://127.0.0.1/index";
        // while (!"exit".equalsIgnoreCase(userInput)){ 
        //Create a new URL object equal to the query above
        URL url = new URL(query);

        //Create a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //Display message asking for user input
        System.out.println("Type diary entry:");

        //Get user input
        Scanner input = new Scanner(System.in);
        userInput = input.nextLine();
        
        if("exit".equalsIgnoreCase(userInput))
        {
            System.exit(0);
        }
        

        //Set the request method to POST
        connection.setRequestMethod("POST");

        //Set the request property
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        //Allow connection to output
        connection.setDoOutput(true);
        DataOutputStream outputstream = new DataOutputStream(connection.getOutputStream());
        outputstream.writeBytes(userInput);

        //Create a new integer and set it equal to the response code
        int responseCode = connection.getResponseCode();

        //Print out the response code received
        System.out.println("POST Response Code: " + responseCode);

        //See if a valid (200) response code was received
        if (responseCode == 200) {
            //if a valid response code was received, print out the response
            String response = getResponse(connection);
            System.out.println("POST Response Message: " + "OK");
        } else {
            //if an invalid response code was received, print out the response code
            System.out.println("Bad Response Code: " + responseCode);
        }

        // }
    }

    //Method to handle responses

    private String getResponse(HttpURLConnection connection) {
        //Try statement with creation of buffered reader
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));) {
            //New String object to handle the input
            String inputLine;

            //New StringBuilder object to create the entire response
            StringBuilder response = new StringBuilder();

            //Continue to run loop while buffered reader has data. Set inputLine equal to the buffered reader line
            while ((inputLine = br.readLine()) != null) {
                //Add the inputLine to the response
                response.append(inputLine);
            }

            //Close the buffered reader after done reading
            br.close();

            //Return the response
            return response.toString();
        } catch (IOException ex) {
            //Display an error if caught
            System.out.println("Error! IO Exception Error!");
        }

        //This return statement is if nothing is read from the buffered reader
        return "";
    }

}
