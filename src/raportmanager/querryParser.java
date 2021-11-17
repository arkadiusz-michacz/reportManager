/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raportmanager;



import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Arek
 */
public class querryParser {
    
    public Reader reader;
    
    public querryParser()
    {
        reader = new Reader();
    }
    
    public void runRverything() throws FileNotFoundException, DocumentException
    {
        for(int i = 0; i<reader.tasks.size();i++)
        {
            List dane = executeQuerry(i);
            createExcell(dane,reader.tasks.get(i).getTitle());
            createPdf(dane,reader.tasks.get(i).getTitle());
            /*for(int j = 0; j<dane.size();j++)
                  {
                      System.out.println(dane.get(j));
                  }*/
        
        
        }
        
    
    }
    
    
    
    
    public List<String> executeQuerry(int i)
    {
        String result= new String();
        List<String> resultList = new ArrayList<>();
        
        try {
            PreparedStatement st=reader.connection.prepareStatement(reader.getTask(i).getQuerry());
            ResultSet rs;
            rs = st.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int y = 1; y <= rsmd.getColumnCount(); y++)
            {
                result += (rsmd.getColumnLabel(y)+";");
                System.out.println("Query label: "+y);
            }
            resultList.add(result);
            result = "";
            
            
            if(rs.isBeforeFirst())
            {
                  while(rs.next())
                  {
                      for(int j=1; j<=columnCount; j++)
                      {
                          result+=(rs.getString(j)+";");

                      }

                      resultList.add(result);
                      result ="";

                  }
                  
                  /*for(int j = 0; j<resultList.size();j++)
                  {
                      System.out.println(resultList.get(j));
                  }*/
                  return resultList;
            }
            else
            {
                result = "NO DATA";
            }
        } catch (SQLException ex) {
            Logger.getLogger(querryParser.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
        
        return resultList;
    }
    
    
    
    
    public String doOneTask(int taskNum)
    {
        
        try{
        LocalDate date = LocalDate.now();
        System.out.println(date);
        String time = LocalTime.now().toString();
        String [] czas = time.split("\\.", 2);
        czas = czas[0].split(":",3);
        time = czas[0]+"-"+czas[1];    

        String nazwaPliku = reader.tasks.get(taskNum).getTitle()+" "+date+" "+time+"."+reader.tasks.get(taskNum).outputType;   
        List dane = executeQuerry(taskNum);
        if(reader.tasks.get(taskNum).outputType.equals("pdf"))
        {
            
            createPdf(dane,nazwaPliku);
        }
        else if(reader.tasks.get(taskNum).outputType.equals("xls") || reader.tasks.get(taskNum).outputType.equals("xlsx"))
        {
            createExcell(dane,nazwaPliku);
        }
        
        
        return nazwaPliku;
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(querryParser.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
        
       return "0";
     
    
    }
    
    
    
    
    public void createExcell(List<String> task,String title)
    {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet(title);
        CellStyle colOdd = workbook.createCellStyle();
        CellStyle colEven = workbook.createCellStyle();
        
        /*colEven.setFillBackgroundColor(IndexedColors.WHITE1.getIndex()); 
        colEven.setFillPattern(FillPatternType.BIG_SPOTS);*/ 
        
        
        colEven.setBorderBottom(BorderStyle.THIN);  
        colEven.setBorderRight(BorderStyle.THIN);
        colEven.setBorderTop(BorderStyle.THIN);  
        colEven.setBorderLeft(BorderStyle.THIN);

        colOdd.setBorderBottom(BorderStyle.THIN);  
        colOdd.setBorderRight(BorderStyle.THIN);
        colOdd.setBorderTop(BorderStyle.THIN);  
        colOdd.setBorderLeft(BorderStyle.THIN);
        
        int rowNum = 0;
        for(String dane:task)
        {
            //System.out.println(dane);
            String[] splitDane = dane.split(";");
            
            Row row = sheet.createRow(rowNum++);
            for(int i = 0; i<splitDane.length;i++) {
                row.createCell(i).setCellValue(splitDane[i]);
                if(i%2 ==0)
                    row.getCell(i).setCellStyle(colEven);
                else
                    row.getCell(i).setCellStyle(colOdd);
                
                
                
            }
            
        
        
        }
        
        int dlugoscWiersza = task.get(0).split(";").length;
        for(int i = 0; i < dlugoscWiersza; i++) {
            sheet.autoSizeColumn(i);
        }
        
        try {
            String path = querryParser.class.getProtectionDomain()
                         .getCodeSource().getLocation().getPath();
            System.out.println(path);
            path=path.replace("RaportManager.jar", "taski/"+title);
            //File f = new File(path);
            path=path.replace("%20", " ");
            System.out.println(path);
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            
            workbook.close();
            
            
            
           
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(querryParser.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(querryParser.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
    
    
    
    
    }
    
    public void createPdf(List<String> task, String title) throws FileNotFoundException, DocumentException
    {
         Document document = new Document();
         
         String path = querryParser.class.getProtectionDomain()
                         .getCodeSource().getLocation().getPath();
            System.out.println(path);
            path=path.replace("RaportManager.jar", "taski/"+title);
            path=path.replace("%20", " ");
            //File f= new File(path);
            System.out.println(path);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            
            document.open();
            /*Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk("Hello World", font);*/
            
            PdfPTable table = new PdfPTable(task.get(0).split(";").length);
            
            //table.setTotalWidth(100);
            //table.setSplitRows(true);
           // table.setSplitLate(false);
            System.out.println(task.get(0).split(";").length);
            System.out.println(task.size());
            
            
            for(int i = 0; i<task.size(); i++)
            {
                
                String[] rzad = task.get(i).split(";");
                //System.out.println(rzad.length);
                addRow(table,rzad);
            
            
            }
            table.setHeaderRows(1);
            
            
            
            
            
            
            //document.add(chunk);
            document.add(table);
            
 
            
            document.close();
    
    }
    
    public void addRow(PdfPTable tabela, String[] rzad)
    {
        for(int i =0; i<rzad.length;i++)
        {
            tabela.addCell(rzad[i]);
        }
    
    }
    
    
    
    
    
}
