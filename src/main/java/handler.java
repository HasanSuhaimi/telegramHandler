import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;


public class handler extends TelegramLongPollingBot {

    private static int counter = 0;
    private static int calculatorCounter = 0;

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new handler());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    /**
     * Method for receiving messages.
     * @param update Contains a message from the user.
     */
    @Override
    public void onUpdateReceived(Update update) {

        accessAPI access = new accessAPI();
        Message message = update.getMessage();
        String text = message.getText();
        //System.out.println(text);

        String textLower = text.toLowerCase();

        if(textLower.contains("test-buddy") ) {
            counter++;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("test works!");

            try {
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                //do some error handling
            }

        }
        else if(text != null && counter < 1 ) {
            counter++;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("Hi what can i help?");

            try {
                execute(sendMessageRequest);
                System.out.println("test..");
            } catch (TelegramApiException e) {
                //do some error handling
            }

        }
        else if(textLower.contains("clear") && textLower.contains("form")) {
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());

            //split from new line
            String str[] = textLower.split("\\r?\\n");

            List<String> al = new ArrayList<String>();
            al = Arrays.asList(str);

            List<String> data = new ArrayList<String>();

            for (int x = 1; x < al.size(); x++) {

                //get value after :
                String item = al.get(x).substring(al.get(x).lastIndexOf(":") + 1);
                item.trim();
                data.add(item);
                //remove any spaces
                String formattedItem = item.replaceAll("\\s+", "");
                System.out.println(formattedItem);

            }

            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            
            try {
                access.clearRow(data,"1A1g6kCiFFnJnY__Md4wTgDj-7bak6emLBsQS_Vx98fs");
                sendMessageRequest.setText("Deleted row"+ data.get(1) + " from"+ data.get(0) + " at "+ time);
                execute(sendMessageRequest);
                System.out.println(time + " : CLEAR invoke");
                return;
            } catch (TelegramApiException e) {
                //do some error handling
                System.out.println(e);
            } catch (Exception e) {
                //do some error handling
                System.out.println(e);
            }
        }
        else {

            action(textLower, message, access);

        }


    }

    /**
     * This method add the input text into the google sheet
     * @param inputText
     * @param message
     * @param access
     */
    public void action(String inputText,Message message,accessAPI access) {

        String spreadsheetId = "1A1g6kCiFFnJnY__Md4wTgDj-7bak6emLBsQS_Vx98fs";
        String calenderId = "messbuddy@gmail.com";
        
        List<String> tabList = Arrays.asList("housekeeping","extraction","homecleaning","office cleaning","moving in/out","disinfecting");

        for (String tab : tabList) {

            if( inputText.contains("\n") && inputText.contains(tab)) {

                SendMessage sendMessageRequest = new SendMessage();
                sendMessageRequest.setChatId(message.getChatId().toString());

                //split from new line
                String str[] = inputText.split("\\r?\\n");

                List<String> al = Arrays.asList(str);

                List<String> data = new ArrayList<String>();

                for(int x = 1 ; x < al.size() ; x++){

                    //get value after :
                    String item = al.get(x).substring(al.get(x).indexOf(":") + 1, al.get(x).length());
                    data.add(item);
                    //remove any spaces
                    String formattedItem = item.replaceAll("\\s+","");

                }

                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                data.add(time);

                String output = tab.substring(0, 1).toUpperCase() + tab.substring(1);

                try {
                    String updatedRow = access.runAppend(data,output,spreadsheetId);
                    sendMessageRequest.setText("received, row "+updatedRow+" from "+output+" updated");
                    execute(sendMessageRequest);

                    String url = access.runCalender(output,spreadsheetId,calenderId);
                    sendMessageRequest.setText("create event, check " + url);
                    execute(sendMessageRequest);

                    access.sortSheet(output,spreadsheetId);
                    sendMessageRequest.setText(output +" sheet sorted");
                    execute(sendMessageRequest);
                    
                    System.out.println(time + " : ACTION invoke");

                } catch (TelegramApiException e) {
                    //do some error handling
                    System.out.println(e);
                } catch (Exception e) {
                    //do some error handling
                    System.out.println(e);
                }


            }

        }

    }
    
    /**
     * This method returns the bot's name, which was specified during registration.
     * @return bot name
     */
    @Override
    public String getBotUsername() {
        return "hasBot";
    }

    /**
     * This method returns the bot's token for communicating with the Telegram server
     * @return the bot's token
     */
    @Override
    public String getBotToken() {
        String apiToken = "1282972627:AAE0Ib-C7xSPyEt8LXE_0_-EqQ3bhHGnq6o";
        return apiToken;
    }

    /**
     * Method for creating a message and sending it.
     * @param chatId chat id
     * @param s The String that you want to send as a message.
     */
    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);

    }



}
