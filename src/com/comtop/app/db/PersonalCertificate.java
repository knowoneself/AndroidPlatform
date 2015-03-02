package com.comtop.app.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PERSONAL_CERTIFICATE.
 */
public class PersonalCertificate {

    private Long id;
    private String holderCertificateDetailId;
    private String holderCertificateId;
    private String certificateCode;
    private String jobTypeName;
    private String workTypeName;
    private java.util.Date certificateDate;
    private java.util.Date certificateVaildDate;
    private String certifyingAuthorityName;

    public PersonalCertificate() {
    }

    public PersonalCertificate(Long id) {
        this.id = id;
    }

    public PersonalCertificate(Long id, String holderCertificateDetailId, String holderCertificateId, String certificateCode, String jobTypeName, String workTypeName, java.util.Date certificateDate, java.util.Date certificateVaildDate, String certifyingAuthorityName) {
        this.id = id;
        this.holderCertificateDetailId = holderCertificateDetailId;
        this.holderCertificateId = holderCertificateId;
        this.certificateCode = certificateCode;
        this.jobTypeName = jobTypeName;
        this.workTypeName = workTypeName;
        this.certificateDate = certificateDate;
        this.certificateVaildDate = certificateVaildDate;
        this.certifyingAuthorityName = certifyingAuthorityName;
    }

    public PersonalCertificate( String holderCertificateDetailId, String holderCertificateId, String certificateCode, String jobTypeName, String workTypeName, java.util.Date certificateDate, java.util.Date certificateVaildDate, String certifyingAuthorityName) {
        this.holderCertificateDetailId = holderCertificateDetailId;
        this.holderCertificateId = holderCertificateId;
        this.certificateCode = certificateCode;
        this.jobTypeName = jobTypeName;
        this.workTypeName = workTypeName;
        this.certificateDate = certificateDate;
        this.certificateVaildDate = certificateVaildDate;
        this.certifyingAuthorityName = certifyingAuthorityName;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHolderCertificateDetailId() {
        return holderCertificateDetailId;
    }

    public void setHolderCertificateDetailId(String holderCertificateDetailId) {
        this.holderCertificateDetailId = holderCertificateDetailId;
    }

    public String getHolderCertificateId() {
        return holderCertificateId;
    }

    public void setHolderCertificateId(String holderCertificateId) {
        this.holderCertificateId = holderCertificateId;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public java.util.Date getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(java.util.Date certificateDate) {
        this.certificateDate = certificateDate;
    }

    public java.util.Date getCertificateVaildDate() {
        return certificateVaildDate;
    }

    public void setCertificateVaildDate(java.util.Date certificateVaildDate) {
        this.certificateVaildDate = certificateVaildDate;
    }

    public String getCertifyingAuthorityName() {
        return certifyingAuthorityName;
    }

    public void setCertifyingAuthorityName(String certifyingAuthorityName) {
        this.certifyingAuthorityName = certifyingAuthorityName;
    }

}