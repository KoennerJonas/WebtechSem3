package htw.webtech.bringify.web.api;

public class Token {
    String token;
    public Token(String token){
        this.token = token;
    }
    public Token(){

    }
    public String getToken(){
        return token;
    }
}
