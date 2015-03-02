package com.comtop.app.entity;

import java.util.List;

import com.comtop.app.entity.base.BaseContentList;

/**
 * 人员信息详情实体
 * 
 * 2014-04-24
 * 
 * @author by xxx
 * 
 */
public class PersonalCertificateView extends BaseContentList {

	private PersonalCertificateVO PersonalInf;
	private List<PersonalCertificateDataVO> CertificateDatas;

	public List<PersonalCertificateDataVO> getCertificateDatas() {
		return CertificateDatas;
	}

	public void setCertificateDatas(List<PersonalCertificateDataVO> certificateDatas) {
		CertificateDatas = certificateDatas;
	}

	public PersonalCertificateVO getPersonalInf() {
		return PersonalInf;
	}

	public void setPersonalInf(PersonalCertificateVO personalInf) {
		PersonalInf = personalInf;
	}

}
