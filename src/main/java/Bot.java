import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
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


    public static void saveFile(String downloadPath, String fileSavePath) {


        try  { URL url = new URL(downloadPath);
            try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream()); FileOutputStream fileOutputStream = new FileOutputStream(fileSavePath)) {
               // URL url = new URL(downloadPath);
               // ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
               // FileOutputStream fileOutputStream = new FileOutputStream(fileSavePath);
                fileOutputStream.getChannel()
                        .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);


            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }


    public static void main(String[] args) {


        TelegramBot telegramBot = new TelegramBot("1814170119:AAGbMZ8cd0qkCRsNbwCeaSUQEifUF94f_6I");

        final String[] path = {"D:\\Download\\"};
        final boolean[] isChangePath = {false};

        telegramBot.setUpdatesListener(updates -> {

            System.out.println("_________________________________________");


            for (int i = 0; i < updates.size(); i++) {

                System.out.println("_________________________________________");

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

                        String downloadPath = telegramBot.getFullFilePath(file);

                        String fileSavePath = path[0] + fileId + downloadPath.substring(downloadPath.lastIndexOf('.'));


                        Bot.saveFile(downloadPath, fileSavePath);


                        response = telegramBot.execute(new SendMessage(chatId, fileSavePath));

                        response = telegramBot.execute(new SendMessage(chatId, "Next"));


                    }


                    if (updates.get(i)
                            .message()
                            .video() != null) {

                        chatId = updates.get(i)
                                .message()
                                .chat()
                                .id();


                        Video video = updates.get(i)
                                .message()
                                .video();

                        String fileId = video.fileId();

                        SendResponse response = telegramBot.execute(new SendMessage(chatId, "Current path is: " + path[0]));

                        GetFile fileRequest = new GetFile(fileId);
                        GetFileResponse getFileResponse = telegramBot.execute(fileRequest);

                        File file = getFileResponse.file();


                        String downloadPath = telegramBot.getFullFilePath(file);

                        String fileSavePath = path[0] + fileId + downloadPath.substring(downloadPath.lastIndexOf('.'));

                        response = telegramBot.execute(new SendMessage(chatId, downloadPath));

                        response = telegramBot.execute(new SendMessage(chatId, "Next"));


                        Bot.saveFile(downloadPath, fileSavePath);


                    }

                    if (updates.get(i)
                            .message()
                            .document() != null) {

                        chatId = updates.get(i)
                                .message()
                                .chat()
                                .id();

                        Document document = updates.get(i)
                                .message()
                                .document();

                        String fileId = document.fileId();

                        SendResponse response = telegramBot.execute(new SendMessage(chatId, "Current path is: " + path[0]));

                        GetFile fileRequest = new GetFile(fileId);
                        GetFileResponse getFileResponse = telegramBot.execute(fileRequest);

                        File file = getFileResponse.file();


                        try {
                            String downloadPath = telegramBot.getFullFilePath(file);

                            String fileSavePath = path[0] + fileId + downloadPath.substring(downloadPath.lastIndexOf('.'));

                            response = telegramBot.execute(new SendMessage(chatId, downloadPath));

                            response = telegramBot.execute(new SendMessage(chatId, "Next"));

                            Bot.saveFile(downloadPath, fileSavePath);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            response = telegramBot.execute(new SendMessage(chatId, "File is Null "));

                        }


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
