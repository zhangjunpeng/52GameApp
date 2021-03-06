package com.test4s.gdb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.test4s.gdb.IP;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table IP.
*/
public class IPDao extends AbstractDao<IP, Void> {

    public static final String TABLENAME = "IP";

    /**
     * Properties of entity IP.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Company_name = new Property(0, String.class, "company_name", false, "COMPANY_NAME");
        public final static Property Id = new Property(1, String.class, "id", false, "ID");
        public final static Property Ip_name = new Property(2, String.class, "ip_name", false, "IP_NAME");
        public final static Property Ip_logo = new Property(3, String.class, "ip_logo", false, "IP_LOGO");
        public final static Property Ip_style = new Property(4, String.class, "ip_style", false, "IP_STYLE");
        public final static Property Ip_cat = new Property(5, String.class, "ip_cat", false, "IP_CAT");
        public final static Property Uthority = new Property(6, String.class, "uthority", false, "UTHORITY");
        public final static Property Type = new Property(7, String.class, "type", false, "TYPE");
        public final static Property Style = new Property(8, String.class, "style", false, "STYLE");
        public final static Property Range = new Property(9, String.class, "range", false, "RANGE");
        public final static Property Introuduction = new Property(10, String.class, "introuduction", false, "INTROUDUCTION");
        public final static Property Location = new Property(11, String.class, "location", false, "LOCATION");
        public final static Property Scale = new Property(12, String.class, "scale", false, "SCALE");
        public final static Property WebSite = new Property(13, String.class, "webSite", false, "WEB_SITE");
        public final static Property TelePhone = new Property(14, String.class, "telePhone", false, "TELE_PHONE");
        public final static Property Address = new Property(15, String.class, "address", false, "ADDRESS");
        public final static Property OtherIp = new Property(16, String.class, "otherIp", false, "OTHER_IP");
    };


    public IPDao(DaoConfig config) {
        super(config);
    }
    
    public IPDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'IP' (" + //
                "'COMPANY_NAME' TEXT," + // 0: company_name
                "'ID' TEXT," + // 1: id
                "'IP_NAME' TEXT," + // 2: ip_name
                "'IP_LOGO' TEXT," + // 3: ip_logo
                "'IP_STYLE' TEXT," + // 4: ip_style
                "'IP_CAT' TEXT," + // 5: ip_cat
                "'UTHORITY' TEXT," + // 6: uthority
                "'TYPE' TEXT," + // 7: type
                "'STYLE' TEXT," + // 8: style
                "'RANGE' TEXT," + // 9: range
                "'INTROUDUCTION' TEXT," + // 10: introuduction
                "'LOCATION' TEXT," + // 11: location
                "'SCALE' TEXT," + // 12: scale
                "'WEB_SITE' TEXT," + // 13: webSite
                "'TELE_PHONE' TEXT," + // 14: telePhone
                "'ADDRESS' TEXT," + // 15: address
                "'OTHER_IP' TEXT);"); // 16: otherIp
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'IP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, IP entity) {
        stmt.clearBindings();
 
        String company_name = entity.getCompany_name();
        if (company_name != null) {
            stmt.bindString(1, company_name);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String ip_name = entity.getIp_name();
        if (ip_name != null) {
            stmt.bindString(3, ip_name);
        }
 
        String ip_logo = entity.getIp_logo();
        if (ip_logo != null) {
            stmt.bindString(4, ip_logo);
        }
 
        String ip_style = entity.getIp_style();
        if (ip_style != null) {
            stmt.bindString(5, ip_style);
        }
 
        String ip_cat = entity.getIp_cat();
        if (ip_cat != null) {
            stmt.bindString(6, ip_cat);
        }
 
        String uthority = entity.getUthority();
        if (uthority != null) {
            stmt.bindString(7, uthority);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(8, type);
        }
 
        String style = entity.getStyle();
        if (style != null) {
            stmt.bindString(9, style);
        }
 
        String range = entity.getRange();
        if (range != null) {
            stmt.bindString(10, range);
        }
 
        String introuduction = entity.getIntrouduction();
        if (introuduction != null) {
            stmt.bindString(11, introuduction);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(12, location);
        }
 
        String scale = entity.getScale();
        if (scale != null) {
            stmt.bindString(13, scale);
        }
 
        String webSite = entity.getWebSite();
        if (webSite != null) {
            stmt.bindString(14, webSite);
        }
 
        String telePhone = entity.getTelePhone();
        if (telePhone != null) {
            stmt.bindString(15, telePhone);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(16, address);
        }
 
        String otherIp = entity.getOtherIp();
        if (otherIp != null) {
            stmt.bindString(17, otherIp);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public IP readEntity(Cursor cursor, int offset) {
        IP entity = new IP( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // company_name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ip_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ip_logo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ip_style
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ip_cat
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // uthority
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // type
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // style
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // range
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // introuduction
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // location
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // scale
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // webSite
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // telePhone
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // address
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16) // otherIp
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, IP entity, int offset) {
        entity.setCompany_name(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIp_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIp_logo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIp_style(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIp_cat(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUthority(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setType(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setStyle(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRange(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIntrouduction(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setLocation(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setScale(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setWebSite(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setTelePhone(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setAddress(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setOtherIp(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(IP entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(IP entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
