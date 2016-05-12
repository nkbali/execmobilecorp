package com.execmobile.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;

import com.execmobile.data.CompanyHome;
import com.execmobile.data.Country;
import com.execmobile.data.CountryHome;
import com.execmobile.data.Device;
import com.execmobile.data.DeviceHome;
import com.execmobile.data.ProductHome;
import com.execmobile.data.Usage;
import com.execmobile.data.UsageHome;

public class ExcelParser {

	String zone = null;
	private static final Log log = LogFactory.getLog(ExcelParser.class);

	public void parseUsageFile(byte[] usageBinaryData, SessionFactory sessionFactory) throws Exception {

		InputStream excelStream = new ByteArrayInputStream(usageBinaryData);
		org.apache.poi.ss.usermodel.Workbook myWorkBook = WorkbookFactory.create(excelStream);
		List<Usage> usageList = new ArrayList<Usage>();
		List<Usage> exceptionList = new ArrayList<Usage>();
		List<com.execmobile.data.Country> countries = new CountryHome(sessionFactory).getCountries();

		org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

		Iterator<Row> rowIterator = mySheet.iterator();

		int i = 0;
		boolean skipRow = false;
		try {
			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				if (i == 0) {
					i++;
					continue;
				}
				skipRow = false;
				Iterator<Cell> cellIterator = row.cellIterator();
				com.execmobile.data.Device device = null;
				com.execmobile.data.Product product = null;
				int totalUsage = 0;
				int bundlesUsed = 0;

				Usage usage = new Usage();
				while (cellIterator.hasNext()) {

					if (skipRow)
						break;
					Cell cell = cellIterator.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);

					if (cell.getColumnIndex() == 0) {
						device = new DeviceHome(sessionFactory).findById(cell.getStringCellValue());
						usage.setDevice(device);
						if (device == null)
						{
							skipRow = true;
							break;
						}
						

						product = device.getProduct();
					}
					if (cell.getColumnIndex() == 1) {

						usage.setRecordDate(cell.getStringCellValue());
					}
					if (cell.getColumnIndex() == 2) {
						System.out.println(cell.getStringCellValue());
						com.execmobile.data.Country country = null;
						Optional<com.execmobile.data.Country> countryList = countries.stream().filter(existingCountry -> existingCountry.getCountryName()
									.equals(cell.getStringCellValue())).findFirst();
						if(countryList != null && countryList.isPresent())
						{
							country = countryList.get();
						}
						if(country == null)
						{
							com.execmobile.data.Country newCountry = new Country();
							newCountry.setCountryName(cell.getStringCellValue());
							country = new CountryHome(sessionFactory).save(newCountry);
						}
						usage.setCountry(country);
					}
					if (cell.getColumnIndex() == 3) {
						usage.setZone(cell.getStringCellValue());
						zone = cell.getStringCellValue();
					}
					if (cell.getColumnIndex() == 4) {
						usage.setBase(""+(int) Double.parseDouble(cell.getStringCellValue()));
					}
					if (cell.getColumnIndex() == 5) {
						usage.setExt(""+(int) Double.parseDouble(cell.getStringCellValue()));
					}
					if (cell.getColumnIndex() == 6) {
						totalUsage = (int) Double.parseDouble(cell.getStringCellValue());
						usage.setTotal(""+totalUsage);
					}
					if (cell.getColumnIndex() == 7) {

						String bundlesValue = cell.getStringCellValue();
						if (bundlesValue.equals("-"))
							continue;
						usage.setBundles(Integer.parseInt(bundlesValue));
						bundlesUsed = Integer.parseInt(bundlesValue);
						int bundlesUsedSoFar = device.getBundlesUsed();
						bundlesUsedSoFar += bundlesUsed;
						device.setBundlesUsed(bundlesUsedSoFar);
						new DeviceHome(sessionFactory).update(device);

					}

				}

				if (!skipRow) {
					usageList.add(usage);
					if (product.getProductId().equals("bc54a385-2bc3-4f74-88e8-8d879dff7b4d")) {
						if (product.getDataLimit() < totalUsage)
							exceptionList.add(usage);
					}

					if (product.getProductId().equals("56e65512-513b-478f-b74f-76e28e387b95")) {
						int totalBundlesUsed = device.getBundlesUsed() + bundlesUsed;
						if (totalBundlesUsed > product.getBundles())
							exceptionList.add(usage);
					}
				}
			}

			new UsageHome(sessionFactory).saveMonthlyUsage(usageList);

			XSSFWorkbook exceptionUsageWorkbook = new XSSFWorkbook();
			XSSFSheet exceptionUsageSheet = exceptionUsageWorkbook.createSheet("Exceptional Usage");
			int rowNumber = 0;
			int columnNumber = 0;
			String csvString = "Device ID,Date,Country,Billing Zone,Base SIMs  (MB),Ext.  SIMs  (MB),Total Usage (MB),Bundles";
			String[] headers = csvString.split(",");
			XSSFRow currentRow = exceptionUsageSheet.createRow(rowNumber);
			for (String header : headers) {
				currentRow.createCell(columnNumber).setCellValue(header);
				columnNumber++;
			}
			rowNumber++;

			for (Usage exceptionRecord : exceptionList) {
				columnNumber = 0;
				currentRow = exceptionUsageSheet.createRow(rowNumber);
				currentRow.createCell(columnNumber).setCellValue(exceptionRecord.getDevice().getDeviceId());
				columnNumber++;
				currentRow.createCell(columnNumber).setCellValue(exceptionRecord.getRecordDate());
				columnNumber++;
				currentRow.createCell(columnNumber).setCellValue(exceptionRecord.getCountry().getCountryName());
				columnNumber++;
				currentRow.createCell(columnNumber).setCellValue(exceptionRecord.getZone());
				columnNumber++;
				currentRow.createCell(columnNumber).setCellValue(exceptionRecord.getBase());
				columnNumber++;
				currentRow.createCell(columnNumber).setCellValue(exceptionRecord.getExt());
				columnNumber++;
				currentRow.createCell(columnNumber).setCellValue(exceptionRecord.getTotal());
				columnNumber++;
				currentRow.createCell(columnNumber).setCellValue(exceptionRecord.getBundles());
				columnNumber++;
				rowNumber++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			exceptionUsageWorkbook.write(outputStream);

			new Mailer().sendMail(outputStream.toByteArray());

			if (exceptionUsageWorkbook != null)
				exceptionUsageWorkbook.close();

			if (myWorkBook != null)
				myWorkBook.close();
		} catch (Exception ex) {
			log.error(ex, ex);
			throw ex;
		}

	}

	public void parseDevicesFile(byte[] devicesBinaryData, SessionFactory sessionFactory) throws Exception {
		InputStream excelStream = new ByteArrayInputStream(devicesBinaryData);
		org.apache.poi.ss.usermodel.Workbook myWorkBook = WorkbookFactory.create(excelStream);
		List<Device> deviceList = new ArrayList<Device>();
		List<com.execmobile.data.Company> companies = new CompanyHome(sessionFactory).getCompanies();
		List<com.execmobile.data.Product> products = new ProductHome(sessionFactory).getProducts();
		org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

		Iterator<Row> rowIterator = mySheet.iterator();

		int i = 0;

		while (rowIterator.hasNext()) {

			Row row = rowIterator.next();
			if (i == 0) {
				i++;
				continue;
			}

			Iterator<Cell> cellIterator = row.cellIterator();

			Device device = new Device();
			while (cellIterator.hasNext()) {

				Cell cell = cellIterator.next();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (cell.getColumnIndex() == 0 && !StringUtils.isBlank(cell.getStringCellValue())) {
					device.setDeviceId(cell.getStringCellValue());

				} else if (cell.getColumnIndex() == 0 && StringUtils.isBlank(cell.getStringCellValue()))
					continue;
				if (cell.getColumnIndex() == 1) {

					device.setRef(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 2) {
					if (!StringUtils.isBlank(cell.getStringCellValue())) {
						device.setProduct(products.stream()
								.filter(existingProduct -> existingProduct.getName().equals(cell.getStringCellValue()))
								.findFirst().get());
					}
				}
				if (cell.getColumnIndex() == 3) {
					device.setImei(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 4) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setInvoiceName(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 5) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setZteref(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 6) {
					if (!StringUtils.isBlank(cell.getStringCellValue())) {
						device.setCompany(companies.stream()
								.filter(existingCompany -> existingCompany.getName().equals(cell.getStringCellValue()))
								.findFirst().get());
						device.setIsActive(true);
					} else
						device.setIsActive(false);

				}
				if (cell.getColumnIndex() == 7) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setPeriod(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 8) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setInvoiceNumber(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 9) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setRinvNo(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 10) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setMsisdn(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 11) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setSim(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 12) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setComment(cell.getStringCellValue());
				}
				if (cell.getColumnIndex() == 13) {
					if (!StringUtils.isBlank(cell.getStringCellValue()))
						device.setSupport(cell.getStringCellValue());
				}
			}

			deviceList.add(device);

		}

		new DeviceHome(sessionFactory).saveDevices(deviceList);

		if (myWorkBook != null)
			myWorkBook.close();
	}

}
