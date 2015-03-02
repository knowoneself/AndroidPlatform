package com.comtop.app.utils;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.comtop.app.constant.Constants;
import com.comtop.app.db.DaoMaster;
import com.comtop.app.db.DaoMaster.OpenHelper;
import com.comtop.app.db.DaoSession;
import com.comtop.app.db.HolderEmployeeItem;
import com.comtop.app.db.HolderEmployeeItemDao;
import com.comtop.app.db.HolderEmployeeProject;
import com.comtop.app.db.HolderEmployeeProjectDao;
import com.comtop.app.db.PersonalCertificate;
import com.comtop.app.db.PersonalCertificateDao;
import com.comtop.app.db.PersonalCertificateInf;
import com.comtop.app.db.PersonalCertificateInfDao;
import com.comtop.app.entity.PersonalCertificateVO;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition.StringCondition;

public class DBHelper {

	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	private static DBHelper instance;
	private static Context mContext;

	/** 人员持证信息Dao */
	private static PersonalCertificateInfDao personalCertificateInfDao;

	/** 证书信息Dao */
	private static PersonalCertificateDao personalCertificateDao;

	/** 人员持证项目信息Dao */
	private static HolderEmployeeProjectDao holderEmployeeProjectDao;

	/** 持证情况明细Dao */
	private static HolderEmployeeItemDao holderEmployeeItemDao;

	public static DaoMaster getDaoMaster(Context context, String dataArea) {
		OpenHelper helper = new DaoMaster.DevOpenHelper(context, Constants.dataAreaDbName.get(dataArea), null);
		daoMaster = new DaoMaster(helper.getWritableDatabase());
		return daoMaster;
	}

	public static DaoSession getDaoSession(Context context, String dataArea) {
		daoMaster = getDaoMaster(context, dataArea);
		daoSession = daoMaster.newSession();

		return daoSession;
	}

	public static DBHelper getInstance(Context context, String dataArea) {
		instance = new DBHelper();
		mContext = context;

		// 数据库对�?
		DaoSession daoSession = getDaoSession(mContext, dataArea);
		personalCertificateInfDao = daoSession.getPersonalCertificateInfDao();
		personalCertificateDao = daoSession.getPersonalCertificateDao();
		holderEmployeeProjectDao = daoSession.getHolderEmployeeProjectDao();
		holderEmployeeItemDao = daoSession.getHolderEmployeeItemDao();

		return instance;
	}

	/**
	 * 获取人员信息列表
	 * 
	 * @return
	 */
	public List<PersonalCertificateInf> getPersonalCertificateInf(String queryStr, int pageNo) {
		Date d = new Date();
		long longtime = d.getTime();
		QueryBuilder<PersonalCertificateInf> qb = personalCertificateInfDao.queryBuilder();
		StringBuffer sbSQL = new StringBuffer(150);
		sbSQL.append("EXISTS (SELECT 1 FROM HOLDER_EMPLOYEE_ITEM T1,HOLDER_EMPLOYEE_PROJECT T2 ");
		sbSQL.append(" WHERE T1.HOLDER_EMPLOYEE_ID = T2.HOLDER_EMPLOYEE_ID ");
		sbSQL.append(" AND T2.PROJECT_NAME LIKE '%").append(queryStr).append("%' ");
		sbSQL.append(" AND T1.HOLDER_CERTIFICATE_ID = T.HOLDER_CERTIFICATE_ID  ");
		sbSQL.append(" AND T1.ADMISSION_TIME <= ").append(longtime);
		sbSQL.append(" AND ( T1.ADMISSION_STATUS IN(5,10) OR (T1.DEPARTURE_TIME>= ").append(longtime)
				.append(" AND  T1.ADMISSION_STATUS = 15 ))) ");

		qb.whereOr(com.comtop.app.db.PersonalCertificateInfDao.Properties.UserName.like("%" + queryStr + "%"),
				com.comtop.app.db.PersonalCertificateInfDao.Properties.IdCardNo.like("%" + queryStr + "%"),
				com.comtop.app.db.PersonalCertificateInfDao.Properties.ContractorName.like("%" + queryStr + "%"),
				new StringCondition(sbSQL.toString()));
		qb.orderDesc(com.comtop.app.db.PersonalCertificateInfDao.Properties.BlackedDate);

		qb.limit(18);
		qb.offset((pageNo-1)*18);

		return qb.list();

	}

	/**
	 * 查询满足条件的记录总数(人员总数)
	 * 
	 * @param queryStr
	 *            查询字符�?
	 * @return 符合条件的记录数
	 */
	public int getAllCountPersonInf(String queryStr) {
		Date d = new Date();
		long longtime = d.getTime();
		QueryBuilder<PersonalCertificateInf> qb = personalCertificateInfDao.queryBuilder();
		StringBuffer sbSQL = new StringBuffer(150);
		sbSQL.append("EXISTS (SELECT 1 FROM HOLDER_EMPLOYEE_ITEM T1,HOLDER_EMPLOYEE_PROJECT T2 ");
		sbSQL.append(" WHERE T1.HOLDER_EMPLOYEE_ID = T2.HOLDER_EMPLOYEE_ID ");
		sbSQL.append(" AND T2.PROJECT_NAME LIKE '%").append(queryStr).append("%' ");
		sbSQL.append(" AND T1.HOLDER_CERTIFICATE_ID = T.HOLDER_CERTIFICATE_ID  ");
		sbSQL.append(" AND T1.ADMISSION_TIME <= ").append(longtime);
		sbSQL.append(" AND ( T1.ADMISSION_STATUS IN(5,10) OR (T1.DEPARTURE_TIME>= ").append(longtime)
				.append(" AND  T1.ADMISSION_STATUS = 15 ))) ");

		qb.whereOr(com.comtop.app.db.PersonalCertificateInfDao.Properties.UserName.like("%" + queryStr + "%"),
				com.comtop.app.db.PersonalCertificateInfDao.Properties.IdCardNo.like("%" + queryStr + "%"),
				com.comtop.app.db.PersonalCertificateInfDao.Properties.ContractorName.like("%" + queryStr + "%"),
				new StringCondition(sbSQL.toString()));

		return qb.list().size();

	}

	/**
	 * 查询满足条件的记录总数(黑名单人员总数)
	 * 
	 * @param queryStr
	 *            查询字符�?
	 * @return 符合条件的记录数
	 */
	public int getAllBlackCountPersonInf(String queryStr) {
		Date d = new Date();
		long longtime = d.getTime();
		QueryBuilder<PersonalCertificateInf> qb = personalCertificateInfDao.queryBuilder();
		StringBuffer sbSQL = new StringBuffer(200);
		sbSQL.append(" T.BLACKED = 1 AND T.BLACKED_DATE >= ").append(longtime);
		sbSQL.append(" AND ( T.USER_NAME LIKE '%").append(queryStr).append("%'");
		sbSQL.append(" OR  T.ID_CARD_NO LIKE '%").append(queryStr).append("%'");
		sbSQL.append(" OR  T.CONTRACTOR_NAME LIKE '%").append(queryStr).append("%'");
		sbSQL.append(" OR EXISTS (SELECT 1 FROM HOLDER_EMPLOYEE_ITEM T1,HOLDER_EMPLOYEE_PROJECT T2 ");
		sbSQL.append(" WHERE T1.HOLDER_EMPLOYEE_ID = T2.HOLDER_EMPLOYEE_ID ");
		sbSQL.append(" AND T2.PROJECT_NAME LIKE '%").append(queryStr).append("%' ");
		sbSQL.append(" AND T1.HOLDER_CERTIFICATE_ID = T.HOLDER_CERTIFICATE_ID  ");
		sbSQL.append(" AND T1.ADMISSION_TIME <= ").append(longtime);
		sbSQL.append(" AND ( T1.ADMISSION_STATUS IN(5,10) OR (T1.DEPARTURE_TIME>= ").append(longtime)
				.append(" AND  T1.ADMISSION_STATUS = 15 )) ");
		sbSQL.append(" )) ");
		qb.where(new StringCondition(sbSQL.toString())).build();

		return qb.list().size();

	}

	/**
	 * 根据持证id获取人员信息
	 * 
	 * @param holderCertificateId
	 *            持证情况Id
	 */
	public List<PersonalCertificateInf> getPersonalCertificateInfById(String holderCertificateId) {
		QueryBuilder<PersonalCertificateInf> qb = personalCertificateInfDao.queryBuilder();
		qb.where(com.comtop.app.db.PersonalCertificateInfDao.Properties.HolderCertificateId.eq(holderCertificateId));
		return qb.list();

	}

	/**
	 * 根据持证id获取人员资质正式列表信息
	 * 
	 * @param holderCertificateId
	 *            持证情况Id
	 */
	public List<PersonalCertificate> getPersonalCertificateById(String holderCertificateId) {
		QueryBuilder<PersonalCertificate> qb = personalCertificateDao.queryBuilder();
		qb.where(com.comtop.app.db.PersonalCertificateDao.Properties.HolderCertificateId.eq(holderCertificateId));
		return qb.list();

	}

	/**
	 * 根据身份证ID获取人员持证id
	 * 
	 * @param id
	 *            身份证ID
	 * @return 人员持证ID
	 */
	public PersonalCertificateVO getHolderCertificateId(String id) {
		QueryBuilder<PersonalCertificateInf> qb = personalCertificateInfDao.queryBuilder();
		qb.where(com.comtop.app.db.PersonalCertificateInfDao.Properties.IdCardNo.eq(id));
		PersonalCertificateInf inf = qb.unique();
		
		PersonalCertificateVO tempVO = new PersonalCertificateVO();
		if (inf != null) {
			String holderCertificateId = inf.getHolderCertificateId();
			String personName = inf.getUserName();
			tempVO.setHolderCertificateId(holderCertificateId);
			tempVO.setUserName(personName);
		}
		return tempVO;
	}

	/**
	 * 根据持证情况ID获取人员有关的项目信�?
	 * 
	 * @param holderCertificateId
	 *            持证情况Id
	 * @return List<HolderEmployeeProject>
	 */
	public List<HolderEmployeeProject> getProjectInfByCertificateId(String holderCertificateId) {
		Date d = new Date();
		long longtime = d.getTime();
		QueryBuilder<HolderEmployeeProject> qb = holderEmployeeProjectDao.queryBuilder();
		StringBuffer sbSQL = new StringBuffer(500);
		sbSQL.append(" HOLDER_EMPLOYEE_ID IN (SELECT HOLDER_EMPLOYEE_ID FROM HOLDER_EMPLOYEE_ITEM WHERE HOLDER_CERTIFICATE_ID= '");
		sbSQL.append(holderCertificateId);
		sbSQL.append("'");
		sbSQL.append(" AND ADMISSION_TIME <= ").append(longtime);
		sbSQL.append(" AND ( ADMISSION_STATUS IN(5,10) OR (DEPARTURE_TIME>= ").append(longtime)
				.append(" AND  ADMISSION_STATUS = 15 ) )");
		sbSQL.append(")");

		qb.where(new StringCondition(sbSQL.toString())).build();
		return qb.list();
	}

	/**
	 * 查询以queryStr开头的所有承包商名称
	 * 
	 * @param queryStr
	 *            查询字符�?
	 * @return 查询结果
	 */
	public List<PersonalCertificateInf> getAutoContractorName(String queryStr) {
		QueryBuilder<PersonalCertificateInf> qb = personalCertificateInfDao.queryBuilder();
		StringBuffer sbSQL = new StringBuffer(50);
		sbSQL.append(" CONTRACTOR_NAME LIKE '");
		sbSQL.append(queryStr);
		sbSQL.append("%' GROUP BY CONTRACTOR_NAME ");
		qb.where(new StringCondition(sbSQL.toString())).build();
		return qb.list();

	}

	/**
	 * 查询以queryStr开头的所有项目名�?
	 * 
	 * @param queryStr
	 *            查询字符�?
	 * @return 查询结果
	 */
	public List<HolderEmployeeProject> getAutoProjectName(String queryStr) {
		QueryBuilder<HolderEmployeeProject> qb = holderEmployeeProjectDao.queryBuilder();

		StringBuffer sbSQL = new StringBuffer(50);
		sbSQL.append(" PROJECT_NAME LIKE '");
		sbSQL.append(queryStr);
		sbSQL.append("%' GROUP BY PROJECT_NAME ");
		qb.where(new StringCondition(sbSQL.toString())).build();
		return qb.list();

	}

	/**
	 * 插入项目信息
	 * 
	 * @param lstHolderEmployeeProject
	 */
	public void insertHolderEmployeeProject(List<HolderEmployeeProject> lstHolderEmployeeProject) {
		if (lstHolderEmployeeProject != null) {

			/*
			 * for(HolderEmployeeProject objInf : lstHolderEmployeeProject){
			 * holderEmployeeProjectDao.insert(objInf); }
			 */

			holderEmployeeProjectDao.insertInTx(lstHolderEmployeeProject);
		}
	}

	/**
	 * 插入项目对应的持证信�?
	 * 
	 * @param lstHolderEmployeeItem
	 */
	public void insertHolderEmployeeItem(List<HolderEmployeeItem> lstHolderEmployeeItem) {
		if (lstHolderEmployeeItem != null) {

			holderEmployeeItemDao.insertInTx(lstHolderEmployeeItem);
		}
	}

	/**
	 * 插入人员证书信息
	 * 
	 * @param lstPersonalCertificate
	 */
	public void insertPersonalCertificate(List<PersonalCertificate> lstPersonalCertificate) {
		if (lstPersonalCertificate != null) {

			personalCertificateDao.insertInTx(lstPersonalCertificate);
		}
	}

	/**
	 * 插入人员信息
	 * 
	 * @param lstPersonalCertificateInf
	 */
	public void insertPersonalCertificateInf(List<PersonalCertificateInf> lstPersonalCertificateInf) {
		if (lstPersonalCertificateInf != null) {

			personalCertificateInfDao.insertInTx(lstPersonalCertificateInf);

		}
	}

	/**
	 * 删除项目表信�?
	 */
	public void deleteAllHolderEmployeeProject() {
		holderEmployeeProjectDao.deleteAll();
		personalCertificateInfDao.deleteAll();
		personalCertificateDao.deleteAll();
		holderEmployeeItemDao.deleteAll();
	}

}
