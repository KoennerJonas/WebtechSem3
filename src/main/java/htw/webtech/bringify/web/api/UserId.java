package htw.webtech.bringify.web.api;

public class UserId {
    Long id;

    public UserId(Long id){
        this.id = id;
    }
    public UserId(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
