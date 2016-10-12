package com.teemo.zhihu.model;

import java.util.List;

/**
 * 最新数据
 * 分析：
 * <p>
 * date : 日期
 * stories : 当日新闻
 * title : 新闻标题
 * images : 图像地址（官方 API 使用数组形式。目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
 * ga_prefix : 供 Google Analytics 使用
 * type : 作用未知
 * id : url 与 share_url 中最后的数字（应为内容的 id）
 * multipic : 消息是否包含多张图片（仅出现在包含多图的新闻中）
 * top_stories : 界面顶部 ViewPager 滚动显示的显示内容（子项格式同上）（请注意区分此处的 image 属性与 stories 中的 images 属性）
 *
 * Created by admin on 2016/10/12.
 */

public class LatestNews {

    /**
     * date : 20161012
     * stories : [{"images":["http://pic1.zhimg.com/331b217e9bcc4498f5346f58534268f0.jpg"],"type":0,"id":8876181,"ga_prefix":"101210","title":"RPG 游戏，就是要有迷宫才好玩"},{"images":["http://pic2.zhimg.com/a6621ed6854ac0f4ce0a6378f9d9ce9d.jpg"],"type":0,"id":8870453,"ga_prefix":"101209","title":"关于上瘾这件事，别高估你的意志力"},{"images":["http://pic1.zhimg.com/b963e893f90c70bf3d5485fb539755d0.jpg"],"type":0,"id":8876124,"ga_prefix":"101208","title":"中国股市市值超过欧洲，这能体现经济发展水平吗？"},{"images":["http://pic4.zhimg.com/38f7e8420c11da93164e46dd554474eb.jpg"],"type":0,"id":8876172,"ga_prefix":"101207","title":"曾经风光无限的 Twitter，现在想卖都卖不出去了"},{"images":["http://pic2.zhimg.com/878d002ed4c730853a1339bd94ab5425.jpg"],"type":0,"id":8874855,"ga_prefix":"101207","title":"是的，我们参加的「网络调查」大都没什么用"},{"images":["http://pic4.zhimg.com/80a7fd99c80019c914708ab3b1d02b4f.jpg"],"type":0,"id":8874841,"ga_prefix":"101207","title":"5300 多年前的木乃伊，最近「开口说话」了"},{"images":["http://pic2.zhimg.com/aa4259e42806e1b1ee54e0655b81de21.jpg"],"type":0,"id":8876297,"ga_prefix":"101207","title":"读读日报 24 小时热门 TOP 5 · 中老年相亲族占座"},{"images":["http://pic4.zhimg.com/60e99b008709261e961e6a1b2626da6b.jpg"],"type":0,"id":8874128,"ga_prefix":"101206","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic3.zhimg.com/eb1af4874a1b7aeb1f1f2ad4e70ece16.jpg","type":0,"id":8876297,"ga_prefix":"101207","title":"读读日报 24 小时热门 TOP 5 · 中老年相亲族占座"},{"image":"http://pic4.zhimg.com/88befc98d0bc45bacac4334e5fa3aea3.jpg","type":0,"id":8874855,"ga_prefix":"101207","title":"是的，我们参加的「网络调查」大都没什么用"},{"image":"http://pic2.zhimg.com/647842c6b3f6b931d116e55405ae6fb9.jpg","type":0,"id":8874841,"ga_prefix":"101207","title":"5300 多年前的木乃伊，最近「开口说话」了"},{"image":"http://pic3.zhimg.com/765f9c85222f5ae37022615a3defc38a.jpg","type":0,"id":8875079,"ga_prefix":"101117","title":"知乎好问题 · 如何挑选基金？"},{"image":"http://pic4.zhimg.com/477df61a69cb71ac6374358d88016357.jpg","type":0,"id":8874945,"ga_prefix":"101116","title":"万一地铁发生事故，记得有这样一条逃生通道"}]
     */

    private String date;
    /**
     * images : ["http://pic1.zhimg.com/331b217e9bcc4498f5346f58534268f0.jpg"]
     * type : 0
     * id : 8876181
     * ga_prefix : 101210
     * title : RPG 游戏，就是要有迷宫才好玩
     */

    private List<StoriesBean> stories;
    /**
     * image : http://pic3.zhimg.com/eb1af4874a1b7aeb1f1f2ad4e70ece16.jpg
     * type : 0
     * id : 8876297
     * ga_prefix : 101207
     * title : 读读日报 24 小时热门 TOP 5 · 中老年相亲族占座
     */

    private List<TopStoriesBean> top_stories;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LatestNews{");
        sb.append("date='").append(date).append('\'');
        sb.append(", stories=").append(stories);
        sb.append(", top_stories=").append(top_stories);
        sb.append('}');
        return sb.toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;

        public StoriesBean(List<String> images, String title) {
            this.images = images;
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("TopStoriesBean{");
            sb.append("image='").append(image).append('\'');
            sb.append(", type=").append(type);
            sb.append(", id=").append(id);
            sb.append(", ga_prefix='").append(ga_prefix).append('\'');
            sb.append(", title='").append(title).append('\'');
            sb.append('}');
            return sb.toString();
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
