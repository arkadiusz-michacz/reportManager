/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raportmanager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import static java.lang.System.err;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 *
 * @author Arek
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextArea ta;
    
    //public querryParser qP = new querryParser();
    public static raportScheduler rS ;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        
    }
    
    
    public void appendText(String str) {
    Platform.runLater(() -> ta.appendText(str));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        PrintStream defaultOut = System.out;
        //qP.runRverything();
        
        OutputStream out = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            appendText(String.valueOf((char) b));
            defaultOut.write(b);
        }
        
        
    };
    System.setOut(new PrintStream(out, true));
    
       
            
        System.setErr(new PrintStream(out,true));

   
        rS = new raportScheduler();
        
        
        sendMail.setMailServerProperties();
        rS.schedule();
    }    
    
}
