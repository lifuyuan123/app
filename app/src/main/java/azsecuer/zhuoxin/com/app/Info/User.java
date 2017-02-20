package azsecuer.zhuoxin.com.app.Info;

/**
 * Created by Administrator on 2017/2/14.
 */

public class User {
    private String usename,password,age,sex,phone;
    private boolean istrue;

    public User(String usename, String password, String age, String sex, String phone,boolean istrue) {
        this.usename = usename;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
        this.istrue=istrue;
    }

    public boolean getIstrue() {
        return istrue;
    }
    public void setIstrue(boolean istrue) {
        this.istrue = istrue;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "usename='" + usename + '\'' +
                ", password='" + password + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", istrue=" + istrue +
                '}';
    }
}
