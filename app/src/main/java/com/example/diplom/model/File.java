package com.example.diplom.model;

public class File {


    private String title;
    private String sign;

    public File(String title, String sign) {
        this.title = title;
        this.sign = sign;
    }

    public File(String title) {
        this.title = title;
        this.sign = "false";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof File) {
            File s = (File)obj;
            String elementName = this.title;
            String targetName = s.title;
            if(elementName.equals(targetName)) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
