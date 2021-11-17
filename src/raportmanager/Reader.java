/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raportmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arek
 */
public class Reader {
    
    public List <MsgTemp> tasks;
    public String bazaDanych;
    public String conn;
    public String login;
    public String passwd;
    public String dbType;
    public Connection connection;
    public String emailFrom;
    public String emailPasswd;
    public String smtp;
    
    private final String kluczyk = "superswietnykuczszyfrujacydsjkfhdkghkfshdfkdhskdhgfdkghdskjghfksjhkldsjfdslkfjdslkfjsdlgrhfskjfhjgffjefljewpofjfids^%&^%^&%^#$^$^sdnfbsdmfbnd";
    
    
    
    public Reader()
    {
        tasks = new ArrayList<>();
        readDb();
        readFile();
    }
    
    public void addTask(String t, String [] rec, String q, String tm,String rep,String ot)
    {
        tasks.add(new MsgTemp(t, rec, q, tm,rep,ot));
    }
    
    public void removeTask(int i)
    {
        tasks.remove(i);
    }
    
    public MsgTemp getTask(int i)
    {
        try{
            return tasks.get(i);
        }catch(Exception e)
        {
            e.printStackTrace();
            return tasks.get(0);
        }
    }
    
    public void printTasks()
    {
       for(int i= 0; i<tasks.size();i++)
       {
           System.out.println(tasks.get(i).getTitle()+tasks.get(i).getQuerry()+tasks.get(i).getRecipient(0));
       
       }
    }
    
    
    public void readDb()
    {
        try{
                String path = RaportManager.class.getProtectionDomain()
                                    .getCodeSource().getLocation().getPath();
                String filename = path.replace("RaportManager.jar", "db Config.txt");
                filename = filename.replace("/", "\\");
                filename = filename.replace("%20", " ");
                //File f = new File(filename);
                //FileInputStream fis = new FileInputStream("F:\\projekty\\RaportManager\\db Config.txt");
                FileInputStream fis = new FileInputStream(filename);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                dbType = AES.decrypt(in.readLine(),kluczyk);
                conn = AES.decrypt(in.readLine(),kluczyk);
                System.out.println(dbType);
                if (dbType.equals("ms"))
                {
                    conn = "jdbc:sqlserver://"+conn;
                }
                else if(dbType.equals("maria"))
                {
                    conn = "jdbc:mariadb://"+conn;
                }
                else if(dbType.equals("postgre"))
                {
                    conn = "jdbc:postgresql://"+conn;
                }
                
                System.out.println(conn);
                //bazaDanych = in.readLine();
                login = AES.decrypt(in.readLine(),kluczyk);
                System.out.println(login);
                passwd = AES.decrypt(in.readLine(),kluczyk);
                System.out.println(passwd);
                emailFrom = AES.decrypt(in.readLine(),kluczyk);
                System.out.println(emailFrom);
                emailPasswd = AES.decrypt(in.readLine(),kluczyk);
                System.out.println(emailPasswd);
                smtp = AES.decrypt(in.readLine(),kluczyk);
                System.out.println(smtp);
                
                connection = DriverManager.getConnection(conn,login,passwd);
                
                
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
            
        
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.err.print(ex.getMessage());
            
            
            
        }
    
    }
    
    
    public void readFile()
    {
        try{
                String path = RaportManager.class.getProtectionDomain()
                                    .getCodeSource().getLocation().getPath();
                String filename = path.replace("RaportManager.jar", "tasks Config.txt");
                //File f = new File(path);
                filename = filename.replace("/", "\\");
                filename = filename.replace("%20", " ");
                //FileInputStream fis = new FileInputStream("F:\\projekty\\RaportManager\\tasks Config.txt");
                FileInputStream fis = new FileInputStream(filename);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                String line;
                String title;
                String querry;
                String recipients;
                String time;
                String repeat;
                String outputType;
                while((line = in.readLine())!= null)
                {
                    title = AES.decrypt(in.readLine(),kluczyk);
                    querry = AES.decrypt(in.readLine(),kluczyk);
                    recipients = AES.decrypt(in.readLine(),kluczyk);
                    time = AES.decrypt(in.readLine(),kluczyk);
                    repeat = AES.decrypt(in.readLine(),kluczyk);
                    outputType = AES.decrypt(in.readLine(),kluczyk);
                    
                    
                    addTask(title,recipients.split(","),querry,time,repeat,outputType);
                    
                    
                    
                
                }
               
                
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
              
    
    }
    
}
