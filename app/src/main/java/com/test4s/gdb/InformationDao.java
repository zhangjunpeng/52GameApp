package com.test4s.gdb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.test4s.gdb.Information;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table INFORMATION.
*/
public class InformationDao extends AbstractDao<Information, Long> {

    public static final String TABLENAME = "INFORMATION";

    /**
     * Properties of entity Information.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Time = new Property(2, String.class, "time", false, "TIME");
        public final static Property Looknum = new Property(3, Integer.class, "looknum", false, "LOOKNUM");
        public final static Property Comment_num = new Property(4, Integer.class, "comment_num", false, "COMMENT_NUM");
        public final static Property Context = new Property(5, String.class, "context", false, "CONTEXT");
    };


    public InformationDao(DaoConfig config) {
        super(config);
    }
    
    public InformationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'INFORMATION' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TITLE' TEXT NOT NULL ," + // 1: title
                "'TIME' TEXT NOT NULL ," + // 2: time
                "'LOOKNUM' INTEGER," + // 3: looknum
                "'COMMENT_NUM' INTEGER," + // 4: comment_num
                "'CONTEXT' TEXT);"); // 5: context
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'INFORMATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Information entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
        stmt.bindString(3, entity.getTime());
 
        Integer looknum = entity.getLooknum();
        if (looknum != null) {
            stmt.bindLong(4, looknum);
        }
 
        Integer comment_num = entity.getComment_num();
        if (comment_num != null) {
            stmt.bindLong(5, comment_num);
        }
 
        String context = entity.getContext();
        if (context != null) {
            stmt.bindString(6, context);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Information readEntity(Cursor cursor, int offset) {
        Information entity = new Information( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // title
            cursor.getString(offset + 2), // time
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // looknum
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // comment_num
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // context
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Information entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setTime(cursor.getString(offset + 2));
        entity.setLooknum(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setComment_num(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setContext(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Information entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Information entity) {
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
