package GreenDaoDB;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import zj.health.health_v1.Model.CalendarDateListModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CALENDAR_DATE_LIST_MODEL".
*/
public class CalendarDateListModelDao extends AbstractDao<CalendarDateListModel, Long> {

    public static final String TABLENAME = "CALENDAR_DATE_LIST_MODEL";

    /**
     * Properties of entity CalendarDateListModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Date = new Property(1, String.class, "date", false, "DATE");
        public final static Property Img = new Property(2, String.class, "img", false, "IMG");
        public final static Property Color = new Property(3, String.class, "color", false, "COLOR");
    }


    public CalendarDateListModelDao(DaoConfig config) {
        super(config);
    }
    
    public CalendarDateListModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CALENDAR_DATE_LIST_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"DATE\" TEXT," + // 1: date
                "\"IMG\" TEXT," + // 2: img
                "\"COLOR\" TEXT);"); // 3: color
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CALENDAR_DATE_LIST_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CalendarDateListModel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(2, date);
        }
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(3, img);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(4, color);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CalendarDateListModel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(2, date);
        }
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(3, img);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(4, color);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public CalendarDateListModel readEntity(Cursor cursor, int offset) {
        CalendarDateListModel entity = new CalendarDateListModel( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // date
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // img
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // color
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CalendarDateListModel entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setDate(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setImg(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setColor(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CalendarDateListModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CalendarDateListModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CalendarDateListModel entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
