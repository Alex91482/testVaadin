package org.example.service.xls;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import org.example.entity.MyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class XlsService {

    private final Logger logger = LoggerFactory.getLogger(XlsService.class);

    private final String PATH_TO_FILE_DIRECTORY = "temp/";
    private final String FILE_PREFIX = "new_grid_";
    private final String FILE_EXTENSION = ".xls";

    public boolean createXlsFile(List<MyEvent> myEventList) throws Exception{ //метод по созданию xls документа
        // в метод нужно передать список событий по которым будет создаватся документ
        if(myEventList.isEmpty()){
            return false; //если в метод передан пустой лист то завершаем выполнение
        }
        // создание самого excel файла в памяти ("книга")
        HSSFWorkbook workbook = new HSSFWorkbook();
        // создание листа с названием "new_grid" ("лист из книги")
        HSSFSheet sheet = workbook.createSheet("new_grid");

        int rowNumber = 0; //переменная необходима для определения строки в которую будет вестись запись
         Row row = sheet.createRow( 0); //создание начнется с нулевой строки в документе
        //в таблице повтаряем структуру сущности
        //это будут названия колонок
        row.createCell(0).setCellValue("Id");
        row.createCell(1).setCellValue("Name");
        row.createCell(2).setCellValue("Date");
        row.createCell(3).setCellValue("City");
        row.createCell(4).setCellValue("Building");

        for(MyEvent myEvent : myEventList){
            //в цикле перебираем список и заполняем таблицу
            createSheetHeader(sheet, ++rowNumber, myEvent);
        }
        //теперь запись в файл
        //вызываем метод для получения path преобразуем его в file
        FileOutputStream fo = new FileOutputStream(createPath().toFile());
        workbook.write(fo);
        fo.close();
        logger.info(">> Report creation in xls format completed");
        return true; //ожидаем что файл создан в папке temp
    }

    private void createSheetHeader(HSSFSheet sheet, int rowNum, MyEvent myEvent) { //метод заполнения строки таблицы
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(myEvent.getId());
        row.createCell(1).setCellValue(myEvent.getName());
        row.createCell(2).setCellValue(myEvent.getDate());
        row.createCell(3).setCellValue(myEvent.getCity());
        row.createCell(4).setCellValue(myEvent.getBuilding());
    }

    private Path createPath() throws Exception{ //метод по созданию пути к файлу & файла
        String bodyOfName = createNameFile();
        Path path = Paths.get(PATH_TO_FILE_DIRECTORY + FILE_PREFIX + bodyOfName + FILE_EXTENSION); //создаем файл в директории temp
        Files.createFile(path);
        logger.info(">> File exist: " + Files.exists(path)); //файл создан
        return path; //возвращаем путь с файлом в формате Path
    }

    private String createNameFile(){ //метод по созданию названия файла
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(dateTimeFormatter); //ожидаем строку в формате yyyy-MM-dd_HH-mm-ss
    }
}
