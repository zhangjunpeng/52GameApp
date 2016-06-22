package dao.test4s;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class ExampleGenerator {
    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(2, "com.test4s.gdb");
        addGameInfo(schema);
        addInformation(schema);
        new DaoGenerator().generateAll(schema, "E:\\work\\MyApp\\app\\src\\main\\java");
    }


    private static void addGameInfo(Schema schema) {
        Entity gameInfo = schema.addEntity("GameInfo");
        Entity investment=schema.addEntity("Investment");
        Entity outSource=schema.addEntity("OutSource");
        Entity ip=schema.addEntity("IP");
        Entity cp=schema.addEntity("CP");
        Entity distribution=schema.addEntity("Distribution");
        Entity gameType=schema.addEntity("GameType");
        Entity advert=schema.addEntity("Adverts");
        Entity indexItemInfo=schema.addEntity("IndexItemInfo");
        Entity indexAdvert=schema.addEntity("IndexAdvert");
        Entity order=schema.addEntity("Order");
        Entity newsInfo=schema.addEntity("NewsInfo");

        //newsInfo
        newsInfo.addIdProperty().primaryKey();
        newsInfo.addStringProperty("ueser_id");
        newsInfo.addStringProperty("title");
        newsInfo.addStringProperty("views");
        newsInfo.addStringProperty("comments");
        newsInfo.addStringProperty("cover_img");
        newsInfo.addStringProperty("time");
        newsInfo.addStringProperty("url");


        //Index Order
        order.addIdProperty().primaryKey();
        order.addStringProperty("method_name");
        order.addStringProperty("name");

        //indexItemInfo
        indexItemInfo.addIdProperty().primaryKey();
        indexItemInfo.addStringProperty("user_id");
        indexItemInfo.addStringProperty("logo");
        indexItemInfo.addStringProperty("identity_cat");
        indexItemInfo.addStringProperty("company_name");
        indexItemInfo.addStringProperty("method_name");


        //indexAdvert
        indexAdvert.addIdProperty().primaryKey();
        indexAdvert.addStringProperty("user_id");
        indexAdvert.addStringProperty("advert_pic");
        indexAdvert.addStringProperty("advert_name");
        indexAdvert.addStringProperty("advert_url");


        //游戏详情
        gameInfo.addIdProperty().primaryKey();
        gameInfo.addStringProperty("sort");
        gameInfo.addStringProperty("game_id");
        gameInfo.addStringProperty("require");
        gameInfo.addStringProperty("game_img");
        gameInfo.addStringProperty("game_download_nums");
        gameInfo.addStringProperty("game_platform");
        gameInfo.addStringProperty("game_stage");
        gameInfo.addStringProperty("game_name");
        gameInfo.addStringProperty("game_download_url");
        gameInfo.addStringProperty("game_size");
        gameInfo.addStringProperty("norms");
        gameInfo.addStringProperty("game_grade");
        gameInfo.addStringProperty("game_type");
        gameInfo.addStringProperty("game_dev");
        gameInfo.addStringProperty("create_time");
        gameInfo.addStringProperty("is_test");
        gameInfo.addIntProperty("online");
        gameInfo.addIntProperty("enabled");
        gameInfo.addStringProperty("sdk");
        gameInfo.addStringProperty("pack");
        gameInfo.addStringProperty("checked");
        gameInfo.addStringProperty("title");

        //gameIndex
        gameType.addIdProperty().primaryKey();
//        gameType.addStringProperty("method_name");
        gameType.addStringProperty("title");
        gameType.addStringProperty("advert_cat_id");

        //advert
        advert.addIdProperty().primaryKey();
        advert.addStringProperty("advert_pic");
        advert.addStringProperty("advert_name");
        advert.addStringProperty("advert_url");


        //Investment投资公司
        investment.addIdProperty().primaryKey();
        investment.addStringProperty("company_name");
        investment.addStringProperty("identity_cat");
        investment.addStringProperty("logo");
        investment.addStringProperty("user_id");

        investment.addStringProperty("introuduction");
        investment.addStringProperty("location");
        investment.addStringProperty("scale");
        investment.addStringProperty("webSite");
        investment.addStringProperty("telePhone");
        investment.addStringProperty("address");




        //外包公司
        outSource.addIdProperty().primaryKey();

        //index参数
        outSource.addStringProperty("company_name");
        outSource.addStringProperty("identity_cat");
        outSource.addStringProperty("logo");
        outSource.addStringProperty("user_id");

        outSource.addStringProperty("type");
        outSource.addStringProperty("introuduction");
        outSource.addStringProperty("location");
        outSource.addStringProperty("scale");
        outSource.addStringProperty("webSite");
        outSource.addStringProperty("telePhone");
        outSource.addStringProperty("address");



        //ip

        //index
        ip.addStringProperty("company_name");
        ip.addStringProperty("id");
        ip.addStringProperty("ip_name");
        ip.addStringProperty("ip_logo");
        ip.addStringProperty("ip_style");
        ip.addStringProperty("ip_cat");
        ip.addStringProperty("uthority");

        ip.addStringProperty("type");
        ip.addStringProperty("style");
        ip.addStringProperty("range");
        ip.addStringProperty("introuduction");
        ip.addStringProperty("location");
        ip.addStringProperty("scale");
        ip.addStringProperty("webSite");
        ip.addStringProperty("telePhone");
        ip.addStringProperty("address");
        ip.addStringProperty("otherIp");

        //cp游戏开发
        cp.addIdProperty().primaryKey();
        cp.addStringProperty("company_name");
        cp.addStringProperty("identity_cat");
        cp.addStringProperty("logo");
        cp.addStringProperty("user_id");
        //index
        cp.addStringProperty("introuduction");
        cp.addStringProperty("location");
        cp.addStringProperty("scale");
        cp.addStringProperty("webSite");
        cp.addStringProperty("telePhone");
        cp.addStringProperty("address");


        //distribution
        distribution.addIdProperty().primaryKey();
        distribution.addStringProperty("name");
        distribution.addStringProperty("introuduction");
        distribution.addStringProperty("location");
        distribution.addStringProperty("scale");
        distribution.addStringProperty("webSite");
        distribution.addStringProperty("telePhone");
        distribution.addStringProperty("address");

        //历史记录
        Entity history = schema.addEntity("History");
        history.addIdProperty().primaryKey();
        history.addStringProperty("keyword");


    }

    private static void addInformation(Schema schema) {
        Entity information = schema.addEntity("Information");

        information.addIdProperty().primaryKey();
        information.addStringProperty("title").notNull();
        information.addStringProperty("time").notNull();
        information.addIntProperty("looknum");
        information.addIntProperty("comment_num");
        information.addStringProperty("context");


    }
}
