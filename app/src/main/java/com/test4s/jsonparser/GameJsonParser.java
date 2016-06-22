package com.test4s.jsonparser;

import com.app.tools.MyLog;
import com.test4s.gdb.Adverts;
import com.test4s.gdb.GameInfo;
import com.test4s.net.GameDetialParser;
import com.test4s.net.GameListParser;
import com.test4s.net.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/13.
 */
public class GameJsonParser {

    static String[] name_list={"hotGameList","loveGameList","localGameList"};

    public List<Adverts> gameAdverts;
    public Map<String,List> map;
    public List<String> titles;


    private static GameJsonParser gameJsonParser;

    public static GameJsonParser getIntance(){
        if (gameJsonParser==null){
            gameJsonParser=new GameJsonParser();
        }

        return gameJsonParser;
    }
    private GameJsonParser(){

    }
    public void parser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean success=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (success&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                //
                JSONArray advert=data.getJSONArray("adverts");
                gameAdverts=new ArrayList<>();
                for (int i=0;i<advert.length();i++){
                    Adverts adverts=new Adverts();
                    JSONObject adv=advert.getJSONObject(i);
                    adverts.setAdvert_name(adv.getString("advert_name"));
                    adverts.setAdvert_pic(adv.getString("advert_pic"));
                    adverts.setAdvert_url(adv.getString("advert_url"));
                    gameAdverts.add(adverts);
                }
                MyLog.i("gameAdverts");
                map=new HashMap<>();
                JSONArray games=data.getJSONArray("games");
                titles=new ArrayList<>();
                for (int i=0;i<games.length();i++){
                    JSONObject game=games.getJSONObject(i);
                    String title=game.getString("title");

                    JSONArray content=game.getJSONArray("content");
                    ArrayList<GameInfo> gameInfos=new ArrayList<>();

                    for (int j=0;j<content.length();j++){
                        GameInfo gameInfo=new GameInfo();
                        JSONObject jsonObject1=content.getJSONObject(j);
                        gameInfo.setGame_img(jsonObject1.getString("game_img"));
                        gameInfo.setGame_id(jsonObject1.getString("game_id"));
                        gameInfo.setGame_name(jsonObject1.getString("game_name"));
                        gameInfo.setTitle(jsonObject1.getString("title"));
                        gameInfos.add(gameInfo);
                    }
                    titles.add(title);
                    map.put(i+"",gameInfos);
                }
                MyLog.i("games");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static GameListParser getGameListParser(String result){
        GameListParser gameListParser=null;
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            if (code==200){
                gameListParser=new GameListParser();
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                gameListParser.setP(jsonObject1.getString("p"));
                gameListParser.setStaticUrl(jsonObject1.getString("staticUrl"));
                gameListParser.setWebUrl(jsonObject1.getString("webUrl"));

                JSONArray jsonArray=jsonObject1.getJSONArray("gameList");
                List<GameInfo> gameInfos=new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject game=jsonArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_name(game.getString("game_name"));
                    gameInfo.setGame_id(game.getString("game_id"));
                    gameInfo.setGame_img(game.getString("game_img"));
                    gameInfo.setGame_download_url(game.getString("game_download_url"));
                    gameInfo.setGame_download_nums(game.getString("game_download_nums"));
                    gameInfo.setRequire(game.getString("require"));
                    gameInfo.setGame_size(game.getString("game_size"));

                    gameInfos.add(gameInfo);
                }
                gameListParser.setGameInfoList(gameInfos);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gameListParser;
    }
    public static GameDetialParser getGameDetialParser(String result){
        GameDetialParser gameDetialParser=null;
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            if (code==200){
                gameDetialParser=new GameDetialParser();
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                JSONObject jsonObject2=jsonObject1.getJSONObject("gameInfo");
                GameInfo gameInfo=new GameInfo();
                gameInfo.setGame_name(jsonObject2.getString("game_name"));
                gameInfo.setGame_size(jsonObject2.getString("game_size"));

                gameInfo.setGame_download_nums(jsonObject2.getString("game_download_nums"));
                gameInfo.setGame_stage(jsonObject2.getString("game_stage"));
                gameInfo.setGame_download_url(jsonObject2.getString("game_download_url"));
                gameInfo.setGame_id(jsonObject2.getString("game_id"));
                gameInfo.setGame_img(jsonObject2.getString("game_img"));
                gameInfo.setGame_platform(jsonObject2.getString("game_platform"));

                gameDetialParser.setCreate_time(jsonObject2.getString("create_time"));
                gameDetialParser.setGame_intro(jsonObject2.getString("game_intro"));
                gameDetialParser.setGame_test_nums(jsonObject2.getString("game_test_nums"));
                gameDetialParser.setGame_update_intro(jsonObject2.getString("game_update_intro"));
                gameDetialParser.setIs_care(jsonObject2.getInt("is_care"));
                gameDetialParser.setScore((float) jsonObject2.getDouble("score"));
                gameDetialParser.setGameInfo(gameInfo);

                JSONObject cp=jsonObject1.getJSONObject("cpInfo");



                JSONArray shots=jsonObject2.getJSONArray("game_shot");
                List<String> game_shots=new ArrayList<>();
                for (int i=0;i<shots.length();i++){
                    String imgs=shots.getString(i);
                    game_shots.add(imgs);
                }
                gameDetialParser.setGame_shot(game_shots);
                gameDetialParser.setStaticUrl(jsonObject1.getString("staticUrl"));
                gameDetialParser.setWebUrl(jsonObject1.getString("webUrl"));

                JSONArray cpGame=jsonObject1.getJSONArray("cpGame");
                List<GameInfo> game_list=new ArrayList<>();

                for (int i=0;i<cpGame.length();i++){
                    JSONObject jsonObject3=cpGame.getJSONObject(i);
                    GameInfo gameInfo1=new GameInfo();
                    gameInfo1.setGame_id(jsonObject3.getString("game_id"));
                    gameInfo1.setGame_img(jsonObject3.getString("game_img"));
                    gameInfo1.setGame_name(jsonObject3.getString("game_name"));
                    game_list.add(gameInfo1);
                }
                gameDetialParser.setCpGame(game_list);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gameDetialParser;
    }

}
