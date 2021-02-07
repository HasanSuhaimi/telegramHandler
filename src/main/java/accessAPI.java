import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.util.store.MemoryDataStoreFactory;

//Sheet API
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.lang.Object;

public class accessAPI {
    //append sheet
    public String runAppend (List<String> dataValue, String range, String spreadsheetId) throws Exception {

        /** OAuth 2 scope. */
        String SCOPE = "https://www.googleapis.com/auth/spreadsheets";

        /** Global instance of the HTTP transport. */
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        /** Global instance of the JSON factory. */
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        //tesAutomate sheet
        //String spreadsheetId = "14ql0geK26IPAvj8KlD-Ljtv9IqiiRR7WsVEKFSvSEYY";
        //String spreadsheetId = "1A1g6kCiFFnJnY__Md4wTgDj-7bak6emLBsQS_Vx98fs";

        ValueRange requestBody = new ValueRange();
        requestBody.setRange(range);

        String[] data = new String[dataValue.size()];
        data = dataValue.toArray(data);

        List<List<Object>> values = Arrays.asList(
                Arrays.asList(
                        // Cell values ...
                        data
                )
                // Additional rows ...
        );

        //final Credential credential = new accessAPI().authorize(HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE);
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("/home/dev/telegramHandler/telegramHandler/src/main/java/access.json"))
                .createScoped(Collections.singleton(SCOPE));

        requestBody.setValues(values);

        Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("appendSheet")
                .build();

        Sheets.Spreadsheets.Values.Append postRequest =
                sheetsService.spreadsheets().values().append(spreadsheetId, range, requestBody);

        postRequest.setValueInputOption("USER_ENTERED");

        AppendValuesResponse postResponse = postRequest.execute();

        // TODO: Change code below to process the `response` object:
        System.out.println(postResponse);

        //get the index of updated row using get request
        Sheets.Spreadsheets.Values.Get getRequest =
                sheetsService.spreadsheets().values().get(spreadsheetId, range);
        ValueRange response = getRequest.execute();

        int latestRow = response.getValues().size();
        String targetRow = Integer.toString(latestRow);

        return targetRow;
    }
    
    //clear specific row
    public void clearRow (List<String> dataValue) throws Exception {

        /** OAuth 2 scope. */
        String SCOPE = "https://www.googleapis.com/auth/spreadsheets";

        /** Global instance of the HTTP transport. */
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        /** Global instance of the JSON factory. */
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

        String spreadsheetId = "14ql0geK26IPAvj8KlD-Ljtv9IqiiRR7WsVEKFSvSEYY";
        //set the range to e:g Extraction!A3:J3
        String range = dataValue.get(0)+"!A"+dataValue.get(1)+":J"+dataValue.get(1);
        //remove all whitespace
        range = range.replaceAll("\\s+","");

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("/home/dev/telegramHandler/telegramHandler/src/main/java/access.json"))
                .createScoped(Collections.singleton(SCOPE));

        Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("appendSheet")
                .build();

        ClearValuesRequest requestBody = new ClearValuesRequest();
        Sheets.Spreadsheets.Values.Clear request =
                sheetsService.spreadsheets().values().clear(spreadsheetId, range, requestBody);
        ClearValuesResponse clearResponse = request.execute();

        // TODO: Change code below to process the `response` object:
        System.out.println(clearResponse);
    }
    //sort sheet based on the range
    public void sortSheet (String range, String spreadsheetId) throws Exception {

        String SCOPE = "https://www.googleapis.com/auth/spreadsheets";

        /** Global instance of the HTTP transport. */
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        /** Global instance of the JSON factory. */
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

        range = range.replaceAll("\\s+","");

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("/home/dev/telegramHandler/telegramHandler/src/main/java/access.json"))
                .createScoped(Collections.singleton(SCOPE));

        Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("appendSheet")
                .build();

        //get the sheet values from spreadsheet.values api
        Sheets.Spreadsheets.Values.Get getRequest =
                sheetsService.spreadsheets().values().get(spreadsheetId, range);
        ValueRange response = getRequest.execute();

        int totalRow = response.getValues().size();

        //get the sheet properties from spreadsheets.get api
        List<String> ranges = new ArrayList<>();
        ranges.add(range);
        Sheets.Spreadsheets.Get propRequest = sheetsService.spreadsheets().get(spreadsheetId);
        propRequest.setRanges(ranges);
        Spreadsheet propResponse = propRequest.execute();
        //get the sheetID
        int sheetID = propResponse.getSheets().get(0).getProperties().getSheetId();

        //start the batch update to sort
        List<Request> requests = new ArrayList<>();

        List<SortSpec> sortSpecs = new ArrayList<>();
        sortSpecs.add(new SortSpec().setDimensionIndex(0).setSortOrder("ASCENDING"));

        //request to sort the range specify using the sheetID for data range A1:J27
        requests.add(new Request()
                .setSortRange(new SortRangeRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetID)
                                .setStartRowIndex(2)
                                .setEndRowIndex(totalRow)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(10))
                        .setSortSpecs(sortSpecs)));

        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
        requestBody.setRequests(requests);

        Sheets.Spreadsheets.BatchUpdate batchRequest =
                sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody);

        BatchUpdateSpreadsheetResponse batchResponse = batchRequest.execute();

        // TODO: Change code below to process the `response` object:
        System.out.println(batchResponse);
    }

    //run google calendar service
    public String runCalender(String range, String spreadsheetId, String calendarId) throws Exception {

        /** OAuth 2 scope. */
        String SCOPE = "https://www.googleapis.com/auth/spreadsheets";
        String SCOPE2 = "https://www.googleapis.com/auth/calendar";

        List<String> SCOPES = Arrays.asList(
                SCOPE,SCOPE2
        );

        /** Global instance of the HTTP transport. */
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        /** Global instance of the JSON factory. */
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("/home/dev/telegramHandler/telegramHandler/src/main/java/access.json"))
                .createScoped(SCOPES);

        Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("appendSheet")
                .build();

        //get the sheet values from spreadsheet.values api
        Sheets.Spreadsheets.Values.Get getRequest =
                sheetsService.spreadsheets().values().get(spreadsheetId, range);
        ValueRange response = getRequest.execute();

        // get the last row
        int totalRow = response.getValues().size()-1;

        String date = response.getValues().get(totalRow).get(0).toString();
        String details = response.getValues().get(0).get(1).toString() + " : " + response.getValues().get(totalRow).get(1).toString()
                            +"\n"+response.getValues().get(0).get(2).toString() + " : " + response.getValues().get(totalRow).get(2).toString()
                            +"\n"+response.getValues().get(0).get(3).toString() + " : " + response.getValues().get(totalRow).get(3).toString()
                            +"\n"+response.getValues().get(0).get(4).toString() + " : " + response.getValues().get(totalRow).get(4).toString();

        System.out.println(response.getValues().size() + " : "+response.getValues().get(totalRow).get(0));

        String startValue = date+"T10:00:00+08:00";
        String endValue = date+"T10:30:00+08:00";

        // Initialize Calendar service with valid OAuth credentials
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("appendSheet").build();

        Event event = new Event()
                .setSummary(range + " job schedule")
                .setDescription(details);

        DateTime startDateTime = new DateTime(startValue);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = new DateTime(endValue);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
        event.setEnd(end);

        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());

        return event.getHtmlLink();
    }
    
    /** Authorizes the installed application to access user's protected data. */
    public Credential authorize(HttpTransport HTTP_TRANSPORT, JsonFactory JSON_FACTORY, String CLIENT_ID, String CLIENT_SECRET, String SCOPE) throws Exception {
        // set up authorization code flow
        AuthorizationCodeFlow googleFlow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                CLIENT_ID,
                CLIENT_SECRET,
                Collections.singleton(SCOPE)).setDataStoreFactory(MemoryDataStoreFactory.getDefaultInstance()).setAccessType("offline").build();

        // authorize
        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder()
                        .setHost(OAuth2ClientCredentials.DOMAIN)
                        .setPort(OAuth2ClientCredentials.PORT)
                        .build();

        return new AuthorizationCodeInstalledApp(googleFlow, receiver).authorize("user");
    }

    public class OAuth2ClientCredentials {

        /**
         * Value of the "API Key".
         */
        public static final String API_KEY = "";

        /**
         * Value of the "API Secret".
         */
        public static final String API_SECRET = "";

        /**
         * Port in the "Callback URL".
         */
        public static final int PORT = 8080;

        /**
         * Domain name in the "Callback URL".
         */
        public static final String DOMAIN = "localhost";
    }

}
