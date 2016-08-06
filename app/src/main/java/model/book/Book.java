package model.book;

public class Book{

    private String book_name;
    private String book_id;
    private String book_price;

    public Book(String name,String id,String price){
        this.book_name=name;
        this.book_id=id;
        this.book_price=price;
    }

    public String getName(){
        return book_name;
    }
    public void setName(String name){
        this.book_name=name;
    }

    public String getId(){
        return book_id;
    }

    public void setId(String id){
        this.book_id=id;
    }

    public String getPrice(){
        return book_price;
    }

    public void setPrice(String price){
        this.book_price=price;
    }

    public String toString(){
        return book_id+" "+book_name+" "+book_price;
    }
}
