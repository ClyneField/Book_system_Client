package model.book;

/** Response功能：携带有操作符，操作结果以及图书列表 */

public class Response {

    private static Response response;
    private BookList booklist;
    private String result;
    private String operate;

    private Response() {
    }

    public static Response getResponse() {
        if (response == null)
            response = new Response();
        return response;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setBookList(BookList booklist) {
        this.booklist = booklist;
    }

    public String getOperate() {
        return operate;
    }

    public String getResult() {
        return result;
    }

    public BookList getBookList() {
        return booklist;
    }
}
