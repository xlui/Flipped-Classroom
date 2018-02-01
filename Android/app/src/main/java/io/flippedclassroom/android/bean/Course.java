package io.flippedclassroom.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

//课程的实体类
public class Course implements Parcelable{
    private int id;
    private String name;
    private String major;
    private int count;
    private String code;

    public Course() {
    }

    protected Course(Parcel in) {
        id = in.readInt();
        name = in.readString();
        major = in.readString();
        count = in.readInt();
        code = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(major);
        dest.writeInt(count);
        dest.writeString(code);
    }
}
