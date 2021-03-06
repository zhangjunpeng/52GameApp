package com.test4s.gdb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.test4s.gdb.GameInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GAME_INFO.
*/
public class GameInfoDao extends AbstractDao<GameInfo, Long> {

    public static final String TABLENAME = "GAME_INFO";

    /**
     * Properties of entity GameInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Sort = new Property(1, String.class, "sort", false, "SORT");
        public final static Property Game_id = new Property(2, String.class, "game_id", false, "GAME_ID");
        public final static Property Require = new Property(3, String.class, "require", false, "REQUIRE");
        public final static Property Game_img = new Property(4, String.class, "game_img", false, "GAME_IMG");
        public final static Property Game_download_nums = new Property(5, String.class, "game_download_nums", false, "GAME_DOWNLOAD_NUMS");
        public final static Property Game_platform = new Property(6, String.class, "game_platform", false, "GAME_PLATFORM");
        public final static Property Game_stage = new Property(7, String.class, "game_stage", false, "GAME_STAGE");
        public final static Property Game_name = new Property(8, String.class, "game_name", false, "GAME_NAME");
        public final static Property Game_download_url = new Property(9, String.class, "game_download_url", false, "GAME_DOWNLOAD_URL");
        public final static Property Game_size = new Property(10, String.class, "game_size", false, "GAME_SIZE");
        public final static Property Norms = new Property(11, String.class, "norms", false, "NORMS");
        public final static Property Game_grade = new Property(12, String.class, "game_grade", false, "GAME_GRADE");
        public final static Property Game_type = new Property(13, String.class, "game_type", false, "GAME_TYPE");
        public final static Property Game_dev = new Property(14, String.class, "game_dev", false, "GAME_DEV");
        public final static Property Create_time = new Property(15, String.class, "create_time", false, "CREATE_TIME");
        public final static Property Is_test = new Property(16, String.class, "is_test", false, "IS_TEST");
        public final static Property Online = new Property(17, Integer.class, "online", false, "ONLINE");
        public final static Property Enabled = new Property(18, Integer.class, "enabled", false, "ENABLED");
        public final static Property Sdk = new Property(19, String.class, "sdk", false, "SDK");
        public final static Property Pack = new Property(20, String.class, "pack", false, "PACK");
        public final static Property Checked = new Property(21, String.class, "checked", false, "CHECKED");
        public final static Property Title = new Property(22, String.class, "title", false, "TITLE");
    };


    public GameInfoDao(DaoConfig config) {
        super(config);
    }
    
    public GameInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GAME_INFO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'SORT' TEXT," + // 1: sort
                "'GAME_ID' TEXT," + // 2: game_id
                "'REQUIRE' TEXT," + // 3: require
                "'GAME_IMG' TEXT," + // 4: game_img
                "'GAME_DOWNLOAD_NUMS' TEXT," + // 5: game_download_nums
                "'GAME_PLATFORM' TEXT," + // 6: game_platform
                "'GAME_STAGE' TEXT," + // 7: game_stage
                "'GAME_NAME' TEXT," + // 8: game_name
                "'GAME_DOWNLOAD_URL' TEXT," + // 9: game_download_url
                "'GAME_SIZE' TEXT," + // 10: game_size
                "'NORMS' TEXT," + // 11: norms
                "'GAME_GRADE' TEXT," + // 12: game_grade
                "'GAME_TYPE' TEXT," + // 13: game_type
                "'GAME_DEV' TEXT," + // 14: game_dev
                "'CREATE_TIME' TEXT," + // 15: create_time
                "'IS_TEST' TEXT," + // 16: is_test
                "'ONLINE' INTEGER," + // 17: online
                "'ENABLED' INTEGER," + // 18: enabled
                "'SDK' TEXT," + // 19: sdk
                "'PACK' TEXT," + // 20: pack
                "'CHECKED' TEXT," + // 21: checked
                "'TITLE' TEXT);"); // 22: title
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GAME_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GameInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String sort = entity.getSort();
        if (sort != null) {
            stmt.bindString(2, sort);
        }
 
        String game_id = entity.getGame_id();
        if (game_id != null) {
            stmt.bindString(3, game_id);
        }
 
        String require = entity.getRequire();
        if (require != null) {
            stmt.bindString(4, require);
        }
 
        String game_img = entity.getGame_img();
        if (game_img != null) {
            stmt.bindString(5, game_img);
        }
 
        String game_download_nums = entity.getGame_download_nums();
        if (game_download_nums != null) {
            stmt.bindString(6, game_download_nums);
        }
 
        String game_platform = entity.getGame_platform();
        if (game_platform != null) {
            stmt.bindString(7, game_platform);
        }
 
        String game_stage = entity.getGame_stage();
        if (game_stage != null) {
            stmt.bindString(8, game_stage);
        }
 
        String game_name = entity.getGame_name();
        if (game_name != null) {
            stmt.bindString(9, game_name);
        }
 
        String game_download_url = entity.getGame_download_url();
        if (game_download_url != null) {
            stmt.bindString(10, game_download_url);
        }
 
        String game_size = entity.getGame_size();
        if (game_size != null) {
            stmt.bindString(11, game_size);
        }
 
        String norms = entity.getNorms();
        if (norms != null) {
            stmt.bindString(12, norms);
        }
 
        String game_grade = entity.getGame_grade();
        if (game_grade != null) {
            stmt.bindString(13, game_grade);
        }
 
        String game_type = entity.getGame_type();
        if (game_type != null) {
            stmt.bindString(14, game_type);
        }
 
        String game_dev = entity.getGame_dev();
        if (game_dev != null) {
            stmt.bindString(15, game_dev);
        }
 
        String create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindString(16, create_time);
        }
 
        String is_test = entity.getIs_test();
        if (is_test != null) {
            stmt.bindString(17, is_test);
        }
 
        Integer online = entity.getOnline();
        if (online != null) {
            stmt.bindLong(18, online);
        }
 
        Integer enabled = entity.getEnabled();
        if (enabled != null) {
            stmt.bindLong(19, enabled);
        }
 
        String sdk = entity.getSdk();
        if (sdk != null) {
            stmt.bindString(20, sdk);
        }
 
        String pack = entity.getPack();
        if (pack != null) {
            stmt.bindString(21, pack);
        }
 
        String checked = entity.getChecked();
        if (checked != null) {
            stmt.bindString(22, checked);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(23, title);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GameInfo readEntity(Cursor cursor, int offset) {
        GameInfo entity = new GameInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // sort
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // game_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // require
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // game_img
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // game_download_nums
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // game_platform
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // game_stage
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // game_name
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // game_download_url
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // game_size
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // norms
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // game_grade
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // game_type
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // game_dev
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // create_time
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // is_test
            cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17), // online
            cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18), // enabled
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // sdk
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // pack
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // checked
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22) // title
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GameInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSort(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setGame_id(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRequire(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGame_img(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGame_download_nums(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGame_platform(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setGame_stage(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setGame_name(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGame_download_url(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setGame_size(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setNorms(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setGame_grade(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setGame_type(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setGame_dev(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setCreate_time(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setIs_test(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setOnline(cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17));
        entity.setEnabled(cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18));
        entity.setSdk(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setPack(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setChecked(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setTitle(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GameInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GameInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
