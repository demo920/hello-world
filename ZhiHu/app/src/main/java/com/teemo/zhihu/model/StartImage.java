package com.teemo.zhihu.model;

/**
 * Created by admin on 2016/10/11.
 */

public class StartImage {

    /**
     * text : © Fido Dido
     * img : http://p2.zhimg.com/10/7b/107bb4894b46d75a892da6fa80ef504a.jpg
     */

    private String text;
    private String img;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StartImage{");
        sb.append("text='").append(text).append('\'');
        sb.append(", img='").append(img).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
