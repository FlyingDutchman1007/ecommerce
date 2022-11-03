package com.ecommerce.controller.viewobject;

/**
 * @ClassName UserVO
 * @Description Collects user information for front-end
 * @Author Steven
 * @Date 2022/11/2
 * @Version 1.0
 **/
public class UserVO {

    private int id;
    private String name;
    private Byte gender;
    private int age;
    private String telephone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


}
