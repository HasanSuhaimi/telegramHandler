import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

        Message message = update.getMessage();
        String text = message.getText();
        System.out.println(text);

        String textLower = text.toLowerCase();

        if(text != null && counter == 0 ) {
            counter++;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("Hi what can i help?");

            try {
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                //do some error handling
            }

        }
        else if(textLower.contains("calculate")) {
            calculatorCounter ++;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("What do you want to calculate?\nformat=1+1");

            try {
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                //do some error handling
            }
        }

        else if( calculatorCounter > 0 && (textLower.contains("+") || textLower.contains("-") || textLower.contains("*") || textLower.contains("//") )) {

            String a = text;
            System.out.println(a);
            String operators[]=a.split("[0-9]+");
            String operands[]=a.split("[+-]");
            int agregate = Integer.parseInt(operands[0]);
            for(int i=1;i<operands.length;i++){
                if(operators[i].equals("+"))
                    agregate += Integer.parseInt(operands[i]);
                else
                    agregate -= Integer.parseInt(operands[i]);
            }

            String value = Integer.toString(agregate);

            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText(value);


            try {
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                //do some error handling
            }
        }

        if (textLower.contains("end") || textLower.contains("bye") || textLower.contains("thank")) {
            counter = 0;
            calculatorCounter = 0;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("Thank you byee");

            try {
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                //do some error handling
            }
        }
        else if (textLower.contains("wtf")) {
            counter = 0;
            calculatorCounter = 0;
            //create a object that contains the information to send back the message
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString());
            sendMessageRequest.setText("Nope no cursing byee");

            try {
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                //do some error handling
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
