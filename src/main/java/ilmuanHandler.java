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


public class ilmuanHandler extends TelegramLongPollingBot {

    private static int counter = 0;
    private static int calculatorCounter = 0;

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new ilmuanHandler());
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

        //ilmuanbot chat templates
        if(text != null && counter < 1 ) {
            counter++;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("Hi tell me what you would like to know, please choose a number");

            try {
                execute(sendMessageRequest);
                sendMessageRequest.setText("<b>1)</b> Our client use case\n<b>2)</b> Contact us").setParseMode("html");
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                //do some error handling
            }
        }//ilmuanbot chat templates
        else if(text.contains("1") ) {
            counter = 0;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("Companies often integrate different web platforms for different functions – e.g. Onpay, Google Forms etc. – making the process tedious and tough to monitor.");

            try {
                execute(sendMessageRequest);

                sendMessageRequest.setText("Our client EzDurian Putrajaya, https://ezydurian.onpay.my/ faced the exact same problem. To deliver the freshest durian to the customer’s doorsteps, they had to");
                execute(sendMessageRequest);
                sendMessageRequest.setText("<b>(i)</b>\tManually check the Onpay website for every single new order; \n" +
                        "<b>(ii)</b>\tManually update the kitchen team on each order to start order preparations; and then\n" +
                        "<b>(iii)</b>\tCall the driver for the order pickup.\n").setParseMode("html");
                execute(sendMessageRequest);
                sendMessageRequest.setText("Our simple solution?");
                execute(sendMessageRequest);

                sendMessageRequest.setText("•\t<b>Browser automation:</b> With our web scraping solution, customer orders are automatically and extracted from the Onpay website;\n" +
                        "•\t<b>Alert system via Telegram:</b> Our system then immediately sends the order information to the EzDurian kitchen team via Telegram, without any manual input;\n" +
                        "•\t<b>Scheduler program:</b> We have programmed the order form to accept responses only at specified times, allowing the EzDurian team to better manage their operations.\n").setParseMode("html");
                execute(sendMessageRequest);

            } catch (TelegramApiException e) {
                //do some error handling
            }

        }
        else if(text.contains("2")) {
            counter=2;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("Please copy the form and send us with the same format:");

            try {
                execute(sendMessageRequest);
                sendMessageRequest.setText("<b>Contact Form</b>\n" +
                        "Name: \n" +
                        "Company Name:\n" +
                        "Contact No:\n" +
                        "Email:\n").setParseMode("html");
                execute(sendMessageRequest);
                sendMessageRequest.setText("All info will be confidential");
            } catch (TelegramApiException e) {
                //do some error handling
            }
        }
        else if(lowerText.contains("form") && counter > 1) {
            counter=0;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("Received");

            try {
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                //do some error handling
            }
        }
        else {
            counter = 0;
        }

    }

    /**
     * This method returns the bot's name, which was specified during registration.
     * @return bot name
     */
    @Override
    public String getBotUsername() {
        String botUsername = "IlmuanRancangBot";
        return botUsername;
    }

    /**
     * This method returns the bot's token for communicating with the Telegram server
     * @return the bot's token
     */
    @Override
    public String getBotToken() {
        String apiToken = "1360391605:AAHu2_jhBniM2H5-UPmy4JCwWLW1oA_tb-Q";
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
