/*
 * @author lorie.
 *
 * I've been working on this new file since I handed in U03a1. 
 * I found I din't have the JSON simple jar in my library, corrected that.
 * I tried various different commands JSONParser, StringBuilder, BufferedReader, InputStreamReader
 * as well as a try/catch with a timeout.  I just couldn't get things put together correctly.
 * 
 * I cut an pasted the imports from the file you sent me, 
 * but other than that I am typing and putting together the rest so that I can better undertand it
 * and personalize it a bit.
*/

package programmablethermostat;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Stats {
    /*First method
      Strings info, forms http connection, sets get request, opens connection, 
      gets response code for format (throws runtime exception if not 200)scanner opend URL to read code,
      opens url stream and returns info
    */
    public static String retrieveData(String url_str) throws ProtocolException, IOException {
        String info = null;
        URL url;
        try {
            url = new URL(url_str);  
        } catch (MalformedURLException e) {
            System.out.println("New URL failed");
            return "";
        }
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        int responseCode = conn.getResponseCode();
        if(responseCode !=200)
            throw new RuntimeException("HttpResponseCode: " +responseCode);
        else {
            try (Scanner sc = new Scanner(url.openStream())) { 
                while(sc.hasNext()) {
                    info += sc.nextLine();
                }
            }
        }
        if(info.substring(0, 4).equals("null")) {
            return info.substring(4);
        }
        return info;
    }
    
    //Second method
    //Parses data, puts into an object, and returns data
    public static JSONObject rawToJSON(String info) throws ParseException {
        JSONParser parse = new JSONParser();
        JSONObject object = (JSONObject)parse.parse(info);
        return object;
    }
    
    //Third Method
    //Gets JSON from web, converts to readable language, gets user information, serializes then deserializes the collection of times and days, and gets data for results array
    public static void main(String[] args) throws MalformedURLException, ProtocolException, IOException, org.json.simple.parser.ParseException, ClassNotFoundException {
        String info = retrieveData("http://media.capella.edu/BBCourse_Production/IT4774/temperature.json");
        JSONObject thermostatInfo = rawToJSON(info);
        
        
        //Creates file for ArrayList
        File file = new File("Schedule.txt");
        
        // Creates ArrayList object
        ArrayList<ScheduledTemp> dayandtime = new ArrayList<>();
        
        //I see there is information in the schedule.txt but I don't think the day and time are storing
        dayandtime.add(new ScheduledTemp("Monday", 1300));
        dayandtime.add(new ScheduledTemp("Tuesday", 1545));
        dayandtime.add(new ScheduledTemp("Wednesday", 800));
        dayandtime.add(new ScheduledTemp("Thursday", 1800));
        dayandtime.add(new ScheduledTemp("Friday", 1400));
        dayandtime.add(new ScheduledTemp("Saturday", 1252));
        dayandtime.add(new ScheduledTemp("Sunday", 200));      

             
        try (FileOutputStream fo = new FileOutputStream(file); ObjectOutputStream output = new ObjectOutputStream(fo)) {
            for (ScheduledTemp st : dayandtime) {
                output.writeObject(st);
            }
        }
       
             
        FileInputStream fi = new FileInputStream(file);
        ObjectInputStream input = new ObjectInputStream(fi);
        ArrayList<ScheduledTemp> readday = new ArrayList<>();
        try {
        while (true) {
            ScheduledTemp st = (ScheduledTemp)input.readObject();
            readday.add(st);
        }    
        } catch (EOFException ex) {  
        }
        
        //Cannot get day and time to print only null and 0
        System.out.println("  Day        Time");
             System.out.println(readday);         

        
        System.out.println("Items in result array!");
        System.out.println("Identifier: " +thermostatInfo.get("identifier"));
        System.out.println("Name: " +thermostatInfo.get("name"));
        System.out.println("Thermostat Time: " +thermostatInfo.get("thermostatTime"));
        System.out.println("utcTime: " +thermostatInfo.get("utcTime"));
        JSONObject runtime = (JSONObject) thermostatInfo.get("runtime");
        System.out.println("Actual Temperature: " +runtime.get("actualTemperature"));
        System.out.println("Actual Humidity: " +runtime.get("actualHumididty\n\n"));
    }
}
