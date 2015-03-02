package com.comtop.app.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PERSONAL_CERTIFICATE.
*/
public class PersonalCertificateDao extends AbstractDao<PersonalCertificate, Long> {

    public static final String TABLENAME = "PERSONAL_CERTIFICATE";

    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HolderCertificateDetailId = new Property(1, String.class, "holderCertificateDetailId", false, "HOLDER_CERTIFICATE_DETAIL_ID");
        public final static Property HolderCertificateId = new Property(2, String.class, "holderCertificateId", false, "HOLDER_CERTIFICATE_ID");
        public final static Property CertificateCode = new Property(3, String.class, "certificateCode", false, "CERTIFICATE_CODE");
        public final static Property JobTypeName = new Property(4, String.class, "jobTypeName", false, "JOB_TYPE_NAME");
        public final static Property WorkTypeName = new Property(5, String.class, "workTypeName", false, "WORK_TYPE_NAME");
        public final static Property CertificateDate = new Property(6, java.util.Date.class, "certificateDate", false, "CERTIFICATE_DATE");
        public final static Property CertificateVaildDate = new Property(7, java.util.Date.class, "certificateVaildDate", false, "CERTIFICATE_VAILD_DATE");
        public final static Property CertifyingAuthorityName = new Property(8, String.class, "certifyingAuthorityName", false, "CERTIFYING_AUTHORITY_NAME");
    };


    public PersonalCertificateDao(DaoConfig config) {
        super(config);
    }
    
    public PersonalCertificateDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String sql = "CREATE TABLE " + (ifNotExists? "IF NOT EXISTS ": "") + "'PERSONAL_CERTIFICATE' (" + //
                "'_id' INTEGER PRIMARY KEY ASC ," + // 0: id
                "'HOLDER_CERTIFICATE_DETAIL_ID' TEXT," + // 1: holderCertificateDetailId
                "'HOLDER_CERTIFICATE_ID' TEXT," + // 2: holderCertificateId
                "'CERTIFICATE_CODE' TEXT," + // 3: certificateCode
                "'JOB_TYPE_NAME' TEXT," + // 4: jobTypeName
                "'WORK_TYPE_NAME' TEXT," + // 5: workTypeName
                "'CERTIFICATE_DATE' INTEGER," + // 6: certificateDate
                "'CERTIFICATE_VAILD_DATE' INTEGER," + // 7: certificateVaildDate
                "'CERTIFYING_AUTHORITY_NAME' TEXT);"; // 8: certifyingAuthorityName
        db.execSQL(sql);
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PERSONAL_CERTIFICATE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PersonalCertificate entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String holderCertificateDetailId = entity.getHolderCertificateDetailId();
        if (holderCertificateDetailId != null) {
            stmt.bindString(2, holderCertificateDetailId);
        }
 
        String holderCertificateId = entity.getHolderCertificateId();
        if (holderCertificateId != null) {
            stmt.bindString(3, holderCertificateId);
        }
 
        String certificateCode = entity.getCertificateCode();
        if (certificateCode != null) {
            stmt.bindString(4, certificateCode);
        }
 
        String jobTypeName = entity.getJobTypeName();
        if (jobTypeName != null) {
            stmt.bindString(5, jobTypeName);
        }
 
        String workTypeName = entity.getWorkTypeName();
        if (workTypeName != null) {
            stmt.bindString(6, workTypeName);
        }
 
        java.util.Date certificateDate = entity.getCertificateDate();
        if (certificateDate != null) {
            stmt.bindLong(7, certificateDate.getTime());
        }
 
        java.util.Date certificateVaildDate = entity.getCertificateVaildDate();
        if (certificateVaildDate != null) {
            stmt.bindLong(8, certificateVaildDate.getTime());
        }
 
        String certifyingAuthorityName = entity.getCertifyingAuthorityName();
        if (certifyingAuthorityName != null) {
            stmt.bindString(9, certifyingAuthorityName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PersonalCertificate readEntity(Cursor cursor, int offset) {
        PersonalCertificate entity = new PersonalCertificate( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // holderCertificateDetailId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // holderCertificateId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // certificateCode
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // jobTypeName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // workTypeName
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // certificateDate
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // certificateVaildDate
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // certifyingAuthorityName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PersonalCertificate entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHolderCertificateDetailId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHolderCertificateId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCertificateCode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setJobTypeName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWorkTypeName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCertificateDate(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setCertificateVaildDate(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setCertifyingAuthorityName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected Long updateKeyAfterInsert(PersonalCertificate entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PersonalCertificate entity) {
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
