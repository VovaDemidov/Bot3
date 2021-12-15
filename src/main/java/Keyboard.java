import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public abstract class Keyboard {

    public static InlineKeyboardMarkup sendKeyboard() {

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("Change path");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton("Button 2");
        inlineKeyboardButton1.callbackData("Change path");
        inlineKeyboardButton2.callbackData("Button 2");
        inlineKeyboard.addRow(inlineKeyboardButton1, inlineKeyboardButton2);

        return inlineKeyboard;


    }


}
