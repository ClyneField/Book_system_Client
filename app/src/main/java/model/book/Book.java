package model.book;

public class Book{

    private String book_name;
    private String book_author;
    private String book_date;

    public Book(String name,String author ,String date){
        this.book_name=name;
        this.book_author=author;
        this.book_date=date;
    }

    public String getName(){
        return book_name;
    }
    public void setName(String name){
        this.book_name=name;
    }

    public String getDate(){
        return book_date;
    }

    public void setDate(String date){
        this.book_date=date;
    }

    public String getAuthor(){
        return book_author;
    }

    public void setAuthor(String author){
        this.book_author=author;
    }

    public String toString(){
        return book_date+" "+book_name+" "+book_author;
    }
}
