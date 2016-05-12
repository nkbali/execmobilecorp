package com.execmobile.helpers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.execmobile.data.ProductHome;

public class UsageFetcher {
	
	public List<com.execmobile.models.Usage> getUsageHistory(String imei, String productID, int days, SessionFactory sessionFactory) throws Exception {
		DateTime endDateTime = DateTime.now(DateTimeZone.UTC);
		DateTime startDateTime = endDateTime.minusDays(days);
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		String startDate = dateTimeFormatter.print(startDateTime);
		String endDate = dateTimeFormatter.print(endDateTime);
		com.execmobile.data.Product product = new ProductHome(sessionFactory).findById(productID);
		String commandString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
				+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n"
				+ "  <soap12:Header>\r\n" + "    <Credentials xmlns=\"http://wws.iamwebbing.com/\">\r\n"
				+ "      <Username>" + product.getUsername() + "</Username>\r\n" + "      <Password>" + product.getPassword() + "</Password>\r\n"
				+ "      <WSKey>" + product.getKey() + "</WSKey>\r\n" + "    </Credentials>\r\n"
				+ "  </soap12:Header>\r\n" + "  <soap12:Body>\r\n"
				+ "    <GetSpotUsage xmlns=\"http://wws.iamwebbing.com/\">\r\n" + "      <GetUsageRequest>\r\n"
				+ "        <StartDate>" + startDate + "</StartDate>\r\n" + "        <EndDate>" + endDate
				+ "</EndDate>\r\n" + "        <Device>" + imei + "</Device>\r\n" + "		<PaginationRequest>\r\n"
				+ "          <PageSize>1000</PageSize>\r\n" + "          <PageNumber>1</PageNumber>\r\n"
				+ "        </PaginationRequest>\r\n" + "      </GetUsageRequest>\r\n" + "    </GetSpotUsage>\r\n"
				+ "  </soap12:Body>\r\n" + "</soap12:Envelope>";

		String responseString = "";
		String outputString = "";
		String wsURL = "https://wws.iamwebbing.com/usage/Usage.asmx";
		URL url = new URL(wsURL);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[commandString.length()];
		buffer = commandString.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();

		out.write(b);
		out.close();
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
		}

		MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		SOAPMessage message = factory.createMessage(new MimeHeaders(),
				new ByteArrayInputStream(outputString.getBytes(Charset.forName("UTF-8"))));
		SOAPBody body = message.getSOAPBody();
		NodeList returnList = body.getElementsByTagName("SpotUsageRecord");
		List<com.execmobile.models.Usage> usageRecords = new ArrayList<com.execmobile.models.Usage>();
		for (int i = 0; i < returnList.getLength(); i++) {
			com.execmobile.models.Usage usageRecord = new com.execmobile.models.Usage();
			Element spotUsageElement = (Element) returnList.item(i);
			if(spotUsageElement.getElementsByTagName("Date").item(0).getFirstChild() != null)
				usageRecord.setDate(spotUsageElement.getElementsByTagName("Date").item(0).getFirstChild().getNodeValue());
			if(spotUsageElement.getElementsByTagName("DeviceIMEI").item(0).getFirstChild() != null)
				usageRecord.setIMEI(spotUsageElement.getElementsByTagName("DeviceIMEI").item(0).getFirstChild().getNodeValue());
			if(spotUsageElement.getElementsByTagName("CountryCode").item(0).getFirstChild() != null)
				usageRecord.setCountryCode(spotUsageElement.getElementsByTagName("CountryCode").item(0).getFirstChild().getNodeValue());
			if(spotUsageElement.getElementsByTagName("CountryName").item(0).getFirstChild() != null)
				usageRecord.setCountryName(spotUsageElement.getElementsByTagName("CountryName").item(0).getFirstChild().getNodeValue());
			if(spotUsageElement.getElementsByTagName("TotalWBUsage").item(0).getFirstChild() != null)
				usageRecord.setWBUsage(spotUsageElement.getElementsByTagName("TotalWBUsage").item(0).getFirstChild().getNodeValue());
			if(spotUsageElement.getElementsByTagName("TotalHSUsage").item(0).getFirstChild() != null)
				usageRecord.setHSUsage(spotUsageElement.getElementsByTagName("TotalHSUsage").item(0).getFirstChild().getNodeValue());
			if(spotUsageElement.getElementsByTagName("TotalUsage").item(0).getFirstChild() != null)
				usageRecord.setTotalUsage(spotUsageElement.getElementsByTagName("TotalUsage").item(0).getFirstChild().getNodeValue());
			usageRecords.add(usageRecord);
		}
		
		return usageRecords;
	}
	
	public int getCurrentMonthsUsage(String imei, String productID, SessionFactory sessionFactory) throws Exception {
		DateTime endDateTime = DateTime.now(DateTimeZone.UTC);
		DateTime startDateTime = endDateTime.withDayOfMonth(1);
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		String startDate = dateTimeFormatter.print(startDateTime);
		String endDate = dateTimeFormatter.print(endDateTime);
		com.execmobile.data.Product product = new ProductHome(sessionFactory).findById(productID);
		String commandString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
				+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n"
				+ "  <soap12:Header>\r\n" + "    <Credentials xmlns=\"http://wws.iamwebbing.com/\">\r\n"
				+ "      <Username>" + product.getUsername() + "</Username>\r\n" + "      <Password>" + product.getPassword() + "</Password>\r\n"
				+ "      <WSKey>" + product.getKey() + "</WSKey>\r\n" + "    </Credentials>\r\n"
				+ "  </soap12:Header>\r\n" + "  <soap12:Body>\r\n"
				+ "    <GetSpotUsage xmlns=\"http://wws.iamwebbing.com/\">\r\n" + "      <GetUsageRequest>\r\n"
				+ "        <StartDate>" + startDate + "</StartDate>\r\n" + "        <EndDate>" + endDate
				+ "</EndDate>\r\n" + "        <Device>" + imei + "</Device>\r\n" + "		<PaginationRequest>\r\n"
				+ "          <PageSize>1000</PageSize>\r\n" + "          <PageNumber>1</PageNumber>\r\n"
				+ "        </PaginationRequest>\r\n" + "      </GetUsageRequest>\r\n" + "    </GetSpotUsage>\r\n"
				+ "  </soap12:Body>\r\n" + "</soap12:Envelope>";

		String responseString = "";
		String outputString = "";
		String wsURL = "https://wws.iamwebbing.com/usage/Usage.asmx";
		URL url = new URL(wsURL);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[commandString.length()];
		buffer = commandString.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();

		out.write(b);
		out.close();
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
		}

		MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		SOAPMessage message = factory.createMessage(new MimeHeaders(),
				new ByteArrayInputStream(outputString.getBytes(Charset.forName("UTF-8"))));
		SOAPBody body = message.getSOAPBody();
		NodeList returnList = body.getElementsByTagName("SpotUsageRecord");
		int totalUsageForMonth = 0;
		
		for (int i = 0; i < returnList.getLength(); i++) {
			Element spotUsageElement = (Element) returnList.item(i);
			int totalUsage = Integer.parseInt(spotUsageElement.getElementsByTagName("TotalUsage").item(0).getFirstChild().getNodeValue());
			totalUsageForMonth += totalUsage;
		}
		
		return totalUsageForMonth;
	}

}
