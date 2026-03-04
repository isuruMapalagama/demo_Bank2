package learning.demobank_2.service;

import learning.demobank_2.model.request.ExportTransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ExcelExportService {
    private static final String[] HEADERS ={
            "Amount", "Bank Name", "Branch Name", "Transaction Type",
            "Sender Name", "Beneficiary Name", "Category"
    };

    public byte[] exportTransactionsToExcel(List<ExportTransactionDto> exportDto) throws IOException {
        log.info("Exporting {} transactions to Excel", exportDto.size());

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

            Sheet sheet = workbook.createSheet("Transactions");

            createHeaderRow(sheet, workbook);

            createDataRows(sheet, exportDto, workbook);

            autoSizeColumns(sheet);

            workbook.write(outputStream);

            log.info("Successfully exported {} transactions to Excel", exportDto.size());
            return outputStream.toByteArray();
        }catch (IOException e){
            log.error("Error exporting transactions to Excel: {}", e.getMessage(),e);
            throw e;
        }
    }

    public ByteArrayInputStream exportTransactionsToExcelStream(List<ExportTransactionDto> exportDto) throws IOException {
        byte[] excelData = exportTransactionsToExcel(exportDto);
        return new ByteArrayInputStream(excelData);
    }

    private void createHeaderRow(Sheet sheet, Workbook workbook){
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for(int i=0; i<HEADERS.length; i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }
    private void createDataRows(Sheet sheet, List<ExportTransactionDto> exportDto, Workbook workbook){
        CellStyle currencyStyle = createCurrencyStyle(workbook);
        int rowNum = 1;

        for(ExportTransactionDto exportTransactionDto : exportDto){
            Row row = sheet.createRow(rowNum++);

            createCurrencyCell(row, 0, exportTransactionDto.getAmount(), currencyStyle);
            createCell(row, 1, exportTransactionDto.getBankName());
            createCell(row, 2, exportTransactionDto.getBranchName());
            createCell(row, 3, exportTransactionDto.getTransactionType());
            createCell(row, 4, exportTransactionDto.getSenderName());
            createCell(row, 5, exportTransactionDto.getBeneficiaryName());
            createCell(row, 6, exportTransactionDto.getCategory());
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook){
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("\"Rs:\"#,##0.00"));
        return style;
    }

    private void createCell(Row row,int column,Object value) {
        Cell cell = row.createCell(column);
        if (value != null){
            if(value instanceof Integer){
                cell.setCellValue((Integer) value);
            }else {
                cell.setCellValue(value.toString());
            }
        }
    }

    private void createCurrencyCell(Row row,int column, Double value, CellStyle style){
        Cell cell = row.createCell(column);
        if (value != null){
            cell.setCellValue(value);
            cell.setCellStyle(style);
        }
    }
}
