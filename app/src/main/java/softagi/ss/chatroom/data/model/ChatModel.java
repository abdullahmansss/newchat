package softagi.ss.chatroom.data.model;

public class ChatModel
{
    String message,id,image,name;

    public ChatModel() {
    }

    public ChatModel(String message, String id, String image, String name) {
        this.message = message;
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
