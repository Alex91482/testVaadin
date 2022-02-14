package org.example.service.xls;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import org.example.entity.MyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

    private void createXlsFile(List<MyEvent> myEventList) throws Exception{ //метод по созданию xls документа
        // в метод нужно передать список событий по которым будет создаватся документ
        // очистка директории temp
        cleanDirectory();
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
        Path path = Paths.get(PATH_TO_FILE_DIRECTORY + bodyOfName); //создаем файл в директории temp
        Files.createFile(path);
        logger.info(">> File exist: " + Files.exists(path) + ", directory: " + path.toAbsolutePath()); //файл создан
        return path; //возвращаем путь с файлом в формате Path
    }

    private String createNameFile(){ //метод по созданию названия файла
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHss");
        return  FILE_PREFIX + LocalDateTime.now().format(dateTimeFormatter) + FILE_EXTENSION;
    } //ожидаем строку в формате new_grid_yyyyMMdd_HHss.xls

    public void cleanDirectory(){ //получить названия всех файлов из дериктории /temp и удалить их
        File fileInDirectory = new File(String.valueOf(Paths.get(PATH_TO_FILE_DIRECTORY)));
        File[] allFile = fileInDirectory.listFiles(); //собрали все файлы в массив
        if(allFile != null && allFile.length > 0){ //если имеется хоть один файл
            for(File file : allFile){
                file.delete(); //удаляем в цикле
            }
        }
    } //ожидаем очистку директории temp

    public String getPathDirectory()throws Exception{ //метод читает дерикторию temp и возвращает путь к файлу из дериктории
        return Files.walk(
                        Paths.get(PATH_TO_FILE_DIRECTORY)) //прочитать файлы из дериктории
                .filter(Files::isRegularFile) //читать только файлы
                .findFirst() //выбраь первый
                .get() //развернуть optional
                .toFile() //превести path к file
                .getAbsolutePath(); //получаем путь к файлу
    }

    public String getPathResource(List<MyEvent> myEventList) throws Exception{ //метод запускает создание файла и возвращает путь к файлу
        createXlsFile(myEventList); //метод по созданию отчета xls в директории temp
        return getPathDirectory(); //ожидаем что файл будет создан и путь к нему будет возвращен
    }

}
