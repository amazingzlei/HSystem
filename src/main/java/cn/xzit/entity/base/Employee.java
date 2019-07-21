package cn.xzit.entity.base;

import java.io.Serializable;
import java.util.Objects;

/**
 * 工作人员表
 */
public class Employee implements Serializable{
    private Integer wid ;//工号
    private String name ;//姓名
    private String gender ;//性别
    private String age ;//年龄
    private String phone ;//手机号
    private String type ;//类型
    private String password ;//密码
    private String departId;
    private String createTime ;//创建时间
    private String updateTime ; //更新时间

    public Integer getWid() {
        return wid;
    }

    public void setWid(Integer wid) {
        this.wid = wid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartId() {return departId; }

    public void setDepartId(String departId) {this.departId = departId; }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(wid, employee.wid) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(gender, employee.gender) &&
                Objects.equals(age, employee.age) &&
                Objects.equals(phone, employee.phone) &&
                Objects.equals(type, employee.type) &&
                Objects.equals(password, employee.password) &&
                Objects.equals(departId, employee.departId) &&
                Objects.equals(createTime, employee.createTime) &&
                Objects.equals(updateTime, employee.updateTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(wid, name, gender, age, phone, type, password, departId, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "wid=" + wid +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                ", password='" + password + '\'' +
                ", departId=" + departId +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
