package util.book;

/** 类名：Packager
 * 作用：打包操作符和图书信息返回给Controller；
 * 打包退出信息
 */

public class Packager {

    public String createPackage( String name, String author, String date ){
        // ----- 创建字符串缓冲 ----- //
        StringBuilder message=new StringBuilder("");
        // ----- 获取图书信息 ----- //
        String content = BookPackage( name, author, date );
        // ----- 打包操作符 ----- //
        message.append("operate:create"+"\n");
        // ----- 打包图书信息 ----- //
        message.append("content:"+content+"\n");
        // ----- 打包完成，返回Controller ----- //
        return message.toString();
    }

    public String retrievePackage(){
        StringBuilder message=new StringBuilder("");
        message.append("operate:retrieve"+"\n");
        message.append("content:"+"\n");
        return message.toString();
    }

    public String updatePackage(String name,String id,String author){
        StringBuilder message=new StringBuilder("");
        message.append("operate:update"+"\n");
        String content=BookPackage(name,id,author);
        message.append("content:"+content+"\n");
        return message.toString();
    }

    public String deletePackage(String name){
        StringBuilder message=new StringBuilder("");
        message.append("operate:delete"+"\n");
        message.append("content:"+name+"\n");
        return message.toString();
    }

    public String signInPackage(Boolean type, Boolean account, String name, String password){
        StringBuilder message=new StringBuilder("");
        String content = AccountPackage(type,account,name,password);
        message.append("operate:signIn"+"\n");
        message.append("content:"+content+"\n");
        return message.toString();
    }

    public String signUpPackage(Boolean type,Boolean account,String name, String password){
        StringBuilder message=new StringBuilder("");
        String content = AccountPackage(type,account,name,password);
        message.append("operate:signUp"+"\n");
        message.append("content:"+content+"\n");
        return message.toString();
    }

    public String exitPackage(){
        StringBuilder message=new StringBuilder("");
        message.append("operate:exit"+"\n");
        message.append("content:"+"\n");
        return message.toString();
    }

    public String BookPackage(String name,String author,String date){
        // ----- 创建字符串缓冲 ----- //
        StringBuilder message=new StringBuilder("");
        // ----- 用“#”将图书信息分离，便于服务端提取 ----- //
        message.append( name+"#"+author+"#"+date );
        // ----- 返回图书信息 ----- //
        return message.toString();
    }

    /**
     * 类名：Packager
     * 方法名：AccountPackage
     * 作用：打包账户信息
     * @param type
     * @param account
     * @param name
     * @param password
     * @return
     */
    public String AccountPackage(Boolean type,Boolean account,String name, String password){
        // ----- 创建字符串缓冲 ----- //
        StringBuilder message=new StringBuilder("");
        // ----- 用“#”将图书信息分离，便于服务端提取 ----- //
        message.append( type+"#"+account+"#"+name+"#"+password );
        // ----- 返回账户信息 ----- //
        return message.toString();
    }
}
