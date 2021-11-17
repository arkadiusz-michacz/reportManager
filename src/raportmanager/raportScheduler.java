/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raportmanager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author Arek
 */
public class raportScheduler {
    
    public static ScheduledExecutorService scheduledExecutorService;
    //private List <MsgTemp> tasks;
    public static querryParser qP;

    
    
    public raportScheduler(/*List <MsgTemp> tsk*/)
    {
        scheduledExecutorService= Executors.newScheduledThreadPool(5);
        //tasks = tsk;
        qP = new querryParser();
    }
    
    private List<Integer> checkTimes()
    {
        List <Integer> tasksToDo = new ArrayList<>();
        
        
        for(int i = 0 ; i<qP.reader.tasks.size();i++)
        {
            
            int[]  czas = qP.reader.tasks.get(i).getTime();
            if(getMinutesUntilTarget(czas[0],czas[1]) && !qP.reader.tasks.get(i).lastTaskRun.equals(LocalDate.now()))
            {
                tasksToDo.add(i);
                System.out.println(qP.reader.tasks.get(i).lastTaskRun);
                qP.reader.tasks.get(i).lastTaskRun = LocalDate.now();
                
            }
        
        }
    
    
        return tasksToDo;
    }
     
    public void schedule()
    {
        LocalTime time = LocalTime.now();
        System.out.println("Task is starting "+time);
       
        
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
       @Override
       public void run() { 
           List<Integer> tasksToDo= checkTimes();
           
           for(int i = 0; i <tasksToDo.size();i++)
           {
               //String nazwaDoWyslania = qP.doOneTask(tasksToDo.get(i));
               //System.out.println("Task "+i+" document created "+nazwaDoWyslania);
               try {
                   sendMail.createEmailMessage(qP.doOneTask(tasksToDo.get(i)),qP.reader.tasks.get(tasksToDo.get(i)).getRecipients());
               } catch (IOException ex) 
               {
                   Logger.getLogger(raportScheduler.class.getName()).log(Level.SEVERE, null, ex);
                   System.err.println(ex.getMessage());
               }
           
           }
           LocalTime time2 = LocalTime.now();
           System.out.println("worked "+time2); }
     }   , 0, 1, TimeUnit.MINUTES);
    
    }
    
    
    public static boolean getMinutesUntilTarget(int targetHour, int targetMinute) {
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int currentTime = (hour * 60) +  minute;
    return currentTime < (targetHour*60+targetMinute) ? false : true;
}
    
}
