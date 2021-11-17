/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raportmanager;

import java.time.LocalDate;
import java.time.Month;
import static java.time.temporal.TemporalQueries.localDate;

/**
 *
 * @author Arek
 */
public class MsgTemp {
    
        private String title;
        private String querry;
        private String [] recipients;
        private int[] time;
        private int repeat;
        public LocalDate lastTaskRun;
        public String outputType;
        
        MsgTemp(String t, String [] rec, String q, String tm,String rep, String ot)
        {
            time = parseTime(tm);
            repeat = Integer.parseInt(rep);
            title = t;
            querry = q;
            recipients = rec;
            lastTaskRun = LocalDate.of(2000, 1, 1);
            outputType = ot;
            
        
        }
    
        
        private int [] parseTime(String time)
        {
            String [] czas;
            int [] czasInt = new int[2]; 
            
            czas = time.split(":");
            
            czasInt[0] = Integer.parseInt(czas[0]);
            czasInt[1] = Integer.parseInt(czas[1]);
            
            return czasInt;
            
            
        }
        
        public String getTitle()
        {
            return title;
        }
        
        public String getQuerry()
        {
            return querry;
        }
        
        public String[] getRecipients()
        {
            return recipients;
        }
        
        public String getRecipient(int i)
        {
            if(i<recipients.length)
            {
                return recipients[i];
            }
            else
                return recipients[0];
        
        }
        
        public int [] getTime()
        {
            return time;
        }
        
        public int getRep()
        {
            return repeat;
        }
}
