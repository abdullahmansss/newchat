package softagi.ss.chatroom.data.model;

import java.util.List;

public class RoomModel
{
   String name,imageURL;
   List<String> members;

    public RoomModel() {
    }

    public RoomModel(String name)
    {
        this.name = name;
    }

    public RoomModel(String name, String imageURL, List<String> members) {
        this.name = name;
        this.imageURL = imageURL;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
