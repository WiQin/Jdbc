package com.wyw.jdbc;

/**
 * @ClassName Stu
 * @Description
 * @Author Wangyw
 */
public class Stu {
    private  int stuid;
    private  String stuname;
    private  String stusex;

    public Stu() {
    }

    public Stu(int stuid, String stuname, String stusex) {
        this.stuid = stuid;
        this.stuname = stuname;
        this.stusex = stusex;
    }

    public int getStuid() {
        return stuid;
    }

    public void setStuid(int stuid) {
        this.stuid = stuid;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getStusex() {
        return stusex;
    }

    public void setStusex(String stusex) {
        this.stusex = stusex;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "stuid=" + stuid +
                ", stuname='" + stuname + '\'' +
                ", stusex='" + stusex + '\'' +
                '}';
    }
}
