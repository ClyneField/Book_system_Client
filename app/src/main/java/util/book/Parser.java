package util.book;

import model.book.Book;
import model.book.BookList;
import model.book.Response;

/** Parser功能：解析从服务端接收的响应信息或图书信息。 */

public class Parser {

    // ----- 解析服务端的响应信息 ----- //
    public Response parserResponse(String message) {
        // ----- 剥离换行符 ----- //
        String[] message_of_response = message.split("\n");
        // ----- 获得字符串长度 ----- //
        int length = message_of_response.length;
        // ----- 获得操作符 ----- //
        String operate = message_of_response [0].substring ( 8, message_of_response [0].length());
        // ----- 获得操作结果 ----- //
        String result = message_of_response [length - 1].substring(7,message_of_response[length - 1].length());
        // ----- 初始化Response ----- //
        Response response = Response.getResponse();
        // ----- 存储操作符 ----- //
        response.setOperate(operate);
        // ----- 存储操作结果 ----- //
        response.setResult(result);
        // ----- 初始化图书列表 ----- //
        BookList booklist = new BookList();
        // ----- 分离图书信息 ----- //
        for (int i = 1; i < message_of_response.length - 1; ++i) {
            String string;
            if (i == 1) {
                string = message_of_response [1].substring(8, message_of_response [1].length());
            } else {
                string = message_of_response [i];
            }
            // ----- 将图书信息添加到图书列表 ----- //
            Book book = parseBook(string);
            booklist.add(book);
        }
        // ----- 存储图书列表 ----- //
        response.setBookList(booklist);
        return response;
    }

    // ----- 解析服务端的图书信息 ----- //
    public Book parseBook(String string) {
        String[] message = string.split("#");
        String id = message[0];
        String name = message[1];
        String author = message[2];
        return new Book( id, name, author );
    }
}