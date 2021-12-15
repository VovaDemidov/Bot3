import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class Bot {

    public static void main(String[] args) {

        TelegramBot telegramBot = new TelegramBot("1814170119:AAGbMZ8cd0qkCRsNbwCeaSUQEifUF94f_6I");

        final String[] path = {"D:\\Download\\"};
        final boolean[] isChangePath = {false};

        telegramBot.setUpdatesListener(updates -> {


            for (int i = 0; i < updates.size(); i++) {

                System.out.println(updates.get(i));

                long chatId;


                if (updates.get(i)
                        .message() != null) {

                    if (updates.get(i)
                            .message()
                            .text() != null) {

                        chatId = updates.get(i)
                                .message()
                                .chat()
                                .id();

                        if (isChangePath[0]) {

                            path[0] = updates.get(i)
                                    .message()
                                    .text();

                            isChangePath[0] = false;

                        }


                        SendResponse response = telegramBot.execute(new SendMessage(chatId, "Hello!"));
                        response = telegramBot.execute(new SendMessage(chatId, "Current path is: " + path[0]));
                        response = telegramBot.execute(new SendMessage(chatId, "If you don't need to change path, send photo.").replyMarkup(Keyboard.sendKeyboard()));

                    }


                    if (updates.get(i)
                            .message()
                            .photo() != null) {

                        chatId = updates.get(i)
                                .message()
                                .chat()
                                .id();

                        PhotoSize[] photoSize = updates.get(i)
                                .message()
                                .photo();

                        System.out.println(photoSize[photoSize.length - 1].fileId());
                        String fileId = photoSize[photoSize.length - 1].fileId();

                        SendResponse response = telegramBot.execute(new SendMessage(chatId, "Your photo " + fileId));
                        response = telegramBot.execute(new SendMessage(chatId, "Current path is: " + path[0]));
                        response = telegramBot.execute(new SendPhoto(chatId, fileId));

                        GetFile fileRequest = new GetFile(fileId);
                        GetFileResponse getFileResponse = telegramBot.execute(fileRequest);

                        File file = getFileResponse.file();

                        System.out.println("____________________");

                        System.out.println(file.fileId());
                        System.out.println(file.filePath());
                        System.out.println(file.fileSize());

                        System.out.println("____________________");

                        String FullPath = path[0] + fileId + ".jpg";


                        String fullPath = telegramBot.getFullFilePath(file);

                        System.out.println("____________________");

                        System.out.println(path[0]);
                        System.out.println(FullPath);
                        System.out.println("____________________");


                        try {
                            URL url = new URL(fullPath);
                            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                            FileOutputStream fileOutputStream = new FileOutputStream(FullPath);
                            fileOutputStream.getChannel()
                                    .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                            fileOutputStream.close();
                            readableByteChannel.close();


                        } catch (MalformedURLException ex) {
                            ex.printStackTrace();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }


                        response = telegramBot.execute(new SendMessage(chatId, fullPath));

                        response = telegramBot.execute(new SendMessage(chatId, "Next"));


                    }

                }


                if (updates.get(i)
                        .callbackQuery() != null) {

                    CallbackQuery callbackQuery = updates.get(i)
                            .callbackQuery();
                    chatId = callbackQuery.message()
                            .chat()
                            .id();

                    if (callbackQuery.data()
                            .equals("Change path")) {


                        SendResponse response = telegramBot.execute(new SendMessage(chatId, "Write new path"));


                        isChangePath[0] = true;

                    }


                }


            }


            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

}
