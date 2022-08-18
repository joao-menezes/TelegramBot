import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
public class MyFirstBot extends TelegramLongPollingBot {

    private final String[] hellos = {"olá","oi","ola","ola como voce esta ?","ola como voce esta ?"};

    @Override
    public void onUpdateReceived(Update update) {
       if(update.hasMessage() && update.getMessage().hasText()) {
           var message = answer(update);
           try {
               execute(message);
           } catch (TelegramApiException e) {
               throw new RuntimeException(e);
           }
       }
    }

    private SendMessage answer(Update update) {
        var textMessage = update.getMessage().getText().toLowerCase(Locale.ROOT);
        var chatId = update.getMessage().getChatId().toString();

        var answer = "";
        textMessage = deAccent(textMessage).toLowerCase(Locale.ROOT);
        if("/start".equals(textMessage)) {
            answer = "Hello, I am a Elise, what you wish ?";
        }else if(textMessage.startsWith("/help")) {
            answer = "I can answer to the following questions: /start\n/data\n/hora\n/help";
        }else if(textMessage.startsWith("ola") || textMessage.startsWith("oi")) {
            answer = Hellos()+" "+update.getMessage().getFrom().getFirstName();
        }else if(textMessage.startsWith("/music")){
            answer = getMusic();
        }else if(textMessage.startsWith("/hora")){
            answer = getHora();
        }else if(textMessage.startsWith("/data")){
            answer = getData();
        }else if(textMessage.startsWith("/number")){

            answer = update.getMessage().getContact().getPhoneNumber();
        }else{
            answer = "Desculpa, não entendi utilize o comando /help";
        }
        return SendMessage.builder().text(answer).chatId(chatId).build();
    }

    private String getMusic() {
        return "https://www.youtube.com/watch?v=_mVW8tgGY_w";
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    private String Hellos(){
        Random random = new Random();
        int index = random.nextInt(0, hellos.length);
        return hellos[index];
    }

    private String getData(){
        var formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return "A data atual: "+formatter.format(new java.util.Date());
    }

    private String getHora(){
        var formatter = new SimpleDateFormat("HH:mm:ss");
        return "A hora atual: "+formatter.format(new java.util.Date());
    }

    @Override
    public String getBotUsername() {
        return BotData.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return BotData.getBotToken();
    }
}