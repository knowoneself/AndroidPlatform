package com.comtop.app.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.comtop.app.db.HolderEmployeeProject;
import com.comtop.app.db.PersonalCertificate;
import com.comtop.app.db.PersonalCertificateInf;
import com.comtop.app.entity.HolderEmployeeProjectVO;
import com.comtop.app.entity.PersonalCertificateDataVO;
import com.comtop.app.entity.PersonalCertificateVO;

/**
 * VO转换工具�?
 * 
 * @author by xxx
 * 
 */
public class FormConvertUtil {

	/**
	 * personalCertificateInf �?PersonalCertificateVO 的转换方�?
	 * 
	 * @param sourceVO
	 *            personalCertificateInf
	 * @param resutVO
	 *            PersonalCertificateVO
	 */
	public static void personalCertificateInfToPersonalCertificateVO(PersonalCertificateInf sourceVO,
			PersonalCertificateVO resutVO) {
		if (sourceVO != null) {
			resutVO.setIdCardNo(sourceVO.getIdCardNo());
			resutVO.setBlacked(sourceVO.getBlacked());
			
			String strCurrentDate = DateUtil.getCurrentDate();
			Date blackedDate = sourceVO.getBlackedDate();
			if (blackedDate != null) {
				String strBlackDate = DateUtil.getFormatDate(blackedDate);
				int iTemp = strCurrentDate.compareTo(strBlackDate);
				if (iTemp <= 0) {// 是黑名单
					resutVO.setBlacked(1);
				} else {// 不是黑名�?
					resutVO.setBlacked(2);
				}
			} else {
				resutVO.setBlacked(2);
			}

			resutVO.setBlackedRemark(sourceVO.getBlackedRemark());
			resutVO.setBlackedDate(sourceVO.getBlackedDate());
			resutVO.setHolderCertificateId(sourceVO.getHolderCertificateId());
			resutVO.setSex(sourceVO.getSex());
			resutVO.setUserName(sourceVO.getUserName());
			resutVO.setContractorName(sourceVO.getContractorName());
		}

	}

	/**
	 * personalCertificateInfLst �?PersonalCertificateVOLst的转换方�?
	 * 
	 * @param sourceLst
	 *            List<PersonalCertificateInf>
	 * @return List<PersonalCertificateVO>
	 */
	public static List<PersonalCertificateVO> personalCertificateInfLstToPersonalCertificateVOLst(
			List<PersonalCertificateInf> sourceLst) {
		List<PersonalCertificateVO> lstVO = new ArrayList<PersonalCertificateVO>();
		if (sourceLst != null) {
			for (PersonalCertificateInf sourceVO : sourceLst) {
				PersonalCertificateVO resultVO = new PersonalCertificateVO();
				personalCertificateInfToPersonalCertificateVO(sourceVO, resultVO);
				lstVO.add(resultVO);

			}
		}
		return lstVO;

	}

	/**
	 * PersonalCertificate到PersonalCertificateDataVO的转�?
	 * 
	 * @param sourceVO
	 *            PersonalCertificate
	 * @param resultVO
	 *            PersonalCertificateDataVO
	 */
	public static void PersonalCertificateToPersonalCertificateDataVO(PersonalCertificate sourceVO,
			PersonalCertificateDataVO resultVO) {
		if (sourceVO != null) {
			resultVO.setCertificateCode(sourceVO.getCertificateCode());
			resultVO.setCertificateDate(sourceVO.getCertificateDate());
			resultVO.setCertificateVaildDate(sourceVO.getCertificateVaildDate());
			resultVO.setCertifyingAuthorityName(sourceVO.getCertifyingAuthorityName());
			resultVO.setHolderCertificateDetailId(sourceVO.getHolderCertificateDetailId());
			resultVO.setHolderCertificateId(sourceVO.getHolderCertificateId());
			resultVO.setJobTypeName(sourceVO.getJobTypeName());
			resultVO.setWorkTypeName(sourceVO.getWorkTypeName());

		}

	}

	/**
	 * PersonalCertificateLst到personalCertificateDataVOLst转换
	 * 
	 * @param sourcelst
	 *            List<PersonalCertificateDataVO>
	 * @return List<PersonalCertificate>
	 */
	public static List<PersonalCertificateDataVO> personalCertificateLstTopersonalCertificateDataVOLst(
			List<PersonalCertificate> sourcelst) {
		List<PersonalCertificateDataVO> resultLst = new ArrayList<PersonalCertificateDataVO>();
		if (sourcelst != null) {
			for (PersonalCertificate sourceVO : sourcelst) {
				PersonalCertificateDataVO resultVO = new PersonalCertificateDataVO();
				PersonalCertificateToPersonalCertificateDataVO(sourceVO, resultVO);
				resultLst.add(resultVO);

			}
		}
		return resultLst;

	}

	/**
	 * HolderEmployeeProject到HolderEmployeeProjectVO的转�?
	 * 
	 * @param sourceVO
	 *            HolderEmployeeProject
	 * @param resultVO
	 *            HolderEmployeeProjectVO
	 */
	public static void HolderEmployeeProjectToHolderEmployeeProjectVO(HolderEmployeeProject sourceVO,
			HolderEmployeeProjectVO resultVO) {
		if (sourceVO != null) {
			resultVO.setHolderEmployeeId(sourceVO.getHolderEmployeeId());
			resultVO.setProjectId(sourceVO.getProjectId());
			resultVO.setProjectName(sourceVO.getProjectName());
			resultVO.setProjectType(sourceVO.getProjectType());

		}

	}

	/**
	 * lst转换
	 * 
	 * @param sourceLst
	 *            List<HolderEmployeeProject>
	 * @return List<HolderEmployeeProjectVO>
	 */
	public static List<HolderEmployeeProjectVO> getlstFromHolderEmployeeProject(List<HolderEmployeeProject> sourceLst) {
		List<HolderEmployeeProjectVO> resultLst = new ArrayList<HolderEmployeeProjectVO>();
		for (HolderEmployeeProject sourceVO : sourceLst) {
			HolderEmployeeProjectVO resultVO = new HolderEmployeeProjectVO();
			HolderEmployeeProjectToHolderEmployeeProjectVO(sourceVO, resultVO);
			resultLst.add(resultVO);

		}
		return resultLst;

	}

}
