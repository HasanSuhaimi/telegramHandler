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


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.lang.Object;

public class accessAPI {

    public void runAppend (List<String> dataValue, String range) throws Exception {

        /** OAuth 2 scope. */
        String SCOPE = "https://www.googleapis.com/auth/spreadsheets";

        /** Global instance of the HTTP transport. */
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        /** Global instance of the JSON factory. */
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

        String spreadsheetId = "14ql0geK26IPAvj8KlD-Ljtv9IqiiRR7WsVEKFSvSEYY";

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
