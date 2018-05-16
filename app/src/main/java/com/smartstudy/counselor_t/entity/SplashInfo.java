package com.smartstudy.counselor_t.entity;

/**
 * @author louis
 * @date on 2018/5/16
 * @describe 启动页数据模型
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class SplashInfo {

    /**
     * type : BEST_TEACHER
     * data : {"title":"本月最佳答主","titleColor":"#FFFBE8","subtitle":"The Best Answerer This Month","subtitleColor":"#FFFBE8","subtitleAlpha":0.25,"backgroundGradientBottom":"#F24176","backgroundGradientTop":"#FF5D68","name":"陈坤","photoUrl":"https://bkd-media.smartstudy.com/upload/ufile/f0/c8/f0c8bcbe5741f668bdbcf95e6e2e3cddec51b.png"}
     */

    private String type;
    private DataEntity data;

    public void setType(String type) {
        this.type = type;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * title : 本月最佳答主
         * titleColor : #FFFBE8
         * subtitle : The Best Answerer This Month
         * subtitleColor : #FFFBE8
         * subtitleAlpha : 0.25
         * backgroundGradientBottom : #F24176
         * backgroundGradientTop : #FF5D68
         * name : 陈坤
         * photoUrl : https://bkd-media.smartstudy.com/upload/ufile/f0/c8/f0c8bcbe5741f668bdbcf95e6e2e3cddec51b.png
         */

        private String title;
        private String titleColor;
        private String subtitle;
        private String subtitleColor;
        private float subtitleAlpha;
        private String backgroundGradientBottom;
        private String backgroundGradientTop;
        private String name;
        private String photoUrl;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setTitleColor(String titleColor) {
            this.titleColor = titleColor;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public void setSubtitleColor(String subtitleColor) {
            this.subtitleColor = subtitleColor;
        }

        public void setSubtitleAlpha(float subtitleAlpha) {
            this.subtitleAlpha = subtitleAlpha;
        }

        public void setBackgroundGradientBottom(String backgroundGradientBottom) {
            this.backgroundGradientBottom = backgroundGradientBottom;
        }

        public void setBackgroundGradientTop(String backgroundGradientTop) {
            this.backgroundGradientTop = backgroundGradientTop;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getTitleColor() {
            return titleColor;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getSubtitleColor() {
            return subtitleColor;
        }

        public float getSubtitleAlpha() {
            return subtitleAlpha;
        }

        public String getBackgroundGradientBottom() {
            return backgroundGradientBottom;
        }

        public String getBackgroundGradientTop() {
            return backgroundGradientTop;
        }

        public String getName() {
            return name;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }
    }
}
