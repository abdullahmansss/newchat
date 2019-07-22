package softagi.ss.chatroom.data.model;

public class UserModel {
   private String userid,fullname,email,mobile_number,address,gender,blood_type,dateOfBirth,last_donate,diseases, imageUrl;
   private boolean isSelected = false;

   public UserModel() {
   }

   public UserModel(String userid, String fullname, String email, String mobile_number, String address, String gender, String blood_type, String dateOfBirth, String last_donate, String diseases, String imageUrl)
   {
      this.userid = userid;
      this.fullname = fullname;
      this.email = email;
      this.mobile_number = mobile_number;
      this.address = address;
      this.gender = gender;
      this.blood_type = blood_type;
      this.dateOfBirth = dateOfBirth;
      this.last_donate = last_donate;
      this.diseases = diseases;
      this.imageUrl = imageUrl;
   }

   public void setSelected(boolean selected) {
      isSelected = selected;
   }


   public boolean isSelected() {
      return isSelected;
   }

   public String getFullname() {
      return fullname;
   }

   public void setFullname(String fullname) {
      this.fullname = fullname;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getMobile_number() {
      return mobile_number;
   }

   public void setMobile_number(String mobile_number) {
      this.mobile_number = mobile_number;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getGender() {
      return gender;
   }

   public void setGender(String gender) {
      this.gender = gender;
   }

   public String getBlood_type() {
      return blood_type;
   }

   public void setBlood_type(String blood_type) {
      this.blood_type = blood_type;
   }

   public String getDateOfBirth() {
      return dateOfBirth;
   }

   public void setDateOfBirth(String dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
   }

   public String getLast_donate() {
      return last_donate;
   }

   public void setLast_donate(String last_donate) {
      this.last_donate = last_donate;
   }

   public String getDiseases() {
      return diseases;
   }

   public void setDiseases(String diseases) {
      this.diseases = diseases;
   }

   public String getImageUrl() {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public String getUserid() {
      return userid;
   }

   public void setUserid(String userid) {
      this.userid = userid;
   }
}
