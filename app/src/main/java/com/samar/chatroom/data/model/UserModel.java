package com.samar.chatroom.data.model;

public class UserModel {
   String first_name,last_name,email,password,mobile_number,address,gender,blood_type,dateOfBirth,last_donate,diseases, imageUrl;

   public UserModel(String first_name, String last_name, String email, String password, String mobile_number, String address, String gender, String blood_type, String dateOfBirth, String last_donate, String diseases, String imageUrl) {
      this.first_name = first_name;
      this.last_name = last_name;
      this.email = email;
      this.password = password;
      this.mobile_number = mobile_number;
      this.address = address;
      this.gender = gender;
      this.blood_type = blood_type;
      this.dateOfBirth = dateOfBirth;
      this.last_donate = last_donate;
      this.diseases = diseases;
      this.imageUrl = imageUrl;
   }

   public String getFirst_name() {
      return first_name;
   }

   public void setFirst_name(String first_name) {
      this.first_name = first_name;
   }

   public String getLast_name() {
      return last_name;
   }

   public void setLast_name(String last_name) {
      this.last_name = last_name;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
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
}
