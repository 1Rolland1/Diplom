package com.example.diplom.model;

public class File {


    private String name;
    private boolean sign;

    public File(String name, boolean sign) {
        this.name = name;
        this.sign = sign;
    }

    public File(String name) {
        this.name = name;
        this.sign = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof File) {
            File s = (File)obj;
            String elementName = this.name;
            String targetName = s.name;
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
