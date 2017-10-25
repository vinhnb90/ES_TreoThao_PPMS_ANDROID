
package com.es.tungnv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VinhNB on 8/5/2016. Department: Phòng ban của kiểm tra viên
 */
public class PpmsEntityDepartment implements Parcelable{
    private int DepartmentId;
    private String DepartmentName;
    private String DepartmentCode;
    private String Address;
    private String PhoneNumber;
    private String Email;
    private int ParentId;
    private int DepartmentLevel;
    private int DepartmentIndex;
    private String CreateDate;
    private int CreateUser;
    private Boolean IsActive;

    public PpmsEntityDepartment(String departmentName, String departmentCode, String address,
                                String phoneNumber, String email, int parentId, int departmentLevel,
                                int departmentIndex, String createDate, int createUser, Boolean isActive) {
        DepartmentName = departmentName;
        DepartmentCode = departmentCode;
        Address = address;
        PhoneNumber = phoneNumber;
        Email = email;
        ParentId = parentId;
        DepartmentLevel = departmentLevel;
        DepartmentIndex = departmentIndex;
        CreateDate = createDate;
        CreateUser = createUser;
        IsActive = isActive;
    }

    protected PpmsEntityDepartment(Parcel in) {
        DepartmentId = in.readInt();
        DepartmentName = in.readString();
        DepartmentCode = in.readString();
        Address = in.readString();
        PhoneNumber = in.readString();
        Email = in.readString();
        ParentId = in.readInt();
        DepartmentLevel = in.readInt();
        DepartmentIndex = in.readInt();
        CreateDate = in.readString();
        CreateUser = in.readInt();
    }

    public static final Creator<PpmsEntityDepartment> CREATOR = new Creator<PpmsEntityDepartment>() {
        @Override
        public PpmsEntityDepartment createFromParcel(Parcel in) {
            return new PpmsEntityDepartment(in);
        }

        @Override
        public PpmsEntityDepartment[] newArray(int size) {
            return new PpmsEntityDepartment[size];
        }
    };

    public int getDepartmentId() {
        return DepartmentId;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        DepartmentCode = departmentCode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }

    public int getDepartmentLevel() {
        return DepartmentLevel;
    }

    public void setDepartmentLevel(int departmentLevel) {
        DepartmentLevel = departmentLevel;
    }

    public int getDepartmentIndex() {
        return DepartmentIndex;
    }

    public void setDepartmentIndex(int departmentIndex) {
        DepartmentIndex = departmentIndex;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public int getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(int createUser) {
        CreateUser = createUser;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(DepartmentId);
        parcel.writeString(DepartmentName);
        parcel.writeString(DepartmentCode);
        parcel.writeString(Address);
        parcel.writeString(PhoneNumber);
        parcel.writeString(Email);
        parcel.writeInt(ParentId);
        parcel.writeInt(DepartmentLevel);
        parcel.writeInt(DepartmentIndex);
        parcel.writeString(CreateDate);
        parcel.writeInt(CreateUser);
    }
}
