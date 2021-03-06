package GreenDaoDB;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import zj.health.health_v1.Model.ConsultTimeModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONSULT_TIME_MODEL".
*/
public class ConsultTimeModelDao extends AbstractDao<ConsultTimeModel, Long> {

    public static final String TABLENAME = "CONSULT_TIME_MODEL";

    /**
     * Properties of entity ConsultTimeModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property ConsultId = new Property(1, String.class, "consultId", false, "CONSULT_ID");
        public final static Property ConsultTime = new Property(2, String.class, "consultTime", false, "CONSULT_TIME");
    }


    public ConsultTimeModelDao(DaoConfig config) {
        super(config);
    }
    
    public ConsultTimeModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONSULT_TIME_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"CONSULT_ID\" TEXT," + // 1: consultId
                "\"CONSULT_TIME\" TEXT);"); // 2: consultTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONSULT_TIME_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ConsultTimeModel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String consultId = entity.getConsultId();
        if (consultId != null) {
            stmt.bindString(2, consultId);
        }
 
        String consultTime = entity.getConsultTime();
        if (consultTime != null) {
            stmt.bindString(3, consultTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ConsultTimeModel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String consultId = entity.getConsultId();
        if (consultId != null) {
            stmt.bindString(2, consultId);
        }
 
        String consultTime = entity.getConsultTime();
        if (consultTime != null) {
            stmt.bindString(3, consultTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public ConsultTimeModel readEntity(Cursor cursor, int offset) {
        ConsultTimeModel entity = new ConsultTimeModel( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // consultId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // consultTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ConsultTimeModel entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setConsultId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setConsultTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ConsultTimeModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ConsultTimeModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ConsultTimeModel entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
