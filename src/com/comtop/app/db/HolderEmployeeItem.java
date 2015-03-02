package com.comtop.app.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table HOLDER_EMPLOYEE_ITEM.
 */
public class HolderEmployeeItem {

    private Long id;
    private String holderEmployeeItemId;
    private String holderEmployeeId;
    private String holderCertificateId;
    private Integer admissionStatus;
    private java.util.Date admissionTime;
    private java.util.Date departureTime;

    public HolderEmployeeItem() {
    }

    public HolderEmployeeItem(Long id) {
        this.id = id;
    }

    public HolderEmployeeItem(Long id, String holderEmployeeItemId, String holderEmployeeId, String holderCertificateId, Integer admissionStatus, java.util.Date admissionTime, java.util.Date departureTime) {
        this.id = id;
        this.holderEmployeeItemId = holderEmployeeItemId;
        this.holderEmployeeId = holderEmployeeId;
        this.holderCertificateId = holderCertificateId;
        this.admissionStatus = admissionStatus;
        this.admissionTime = admissionTime;
        this.departureTime = departureTime;
    }
    
    public HolderEmployeeItem( String holderEmployeeItemId, String holderEmployeeId, String holderCertificateId, Integer admissionStatus, java.util.Date admissionTime, java.util.Date departureTime) {
        this.holderEmployeeItemId = holderEmployeeItemId;
        this.holderEmployeeId = holderEmployeeId;
        this.holderCertificateId = holderCertificateId;
        this.admissionStatus = admissionStatus;
        this.admissionTime = admissionTime;
        this.departureTime = departureTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHolderEmployeeItemId() {
        return holderEmployeeItemId;
    }

    public void setHolderEmployeeItemId(String holderEmployeeItemId) {
        this.holderEmployeeItemId = holderEmployeeItemId;
    }

    public String getHolderEmployeeId() {
        return holderEmployeeId;
    }

    public void setHolderEmployeeId(String holderEmployeeId) {
        this.holderEmployeeId = holderEmployeeId;
    }

    public String getHolderCertificateId() {
        return holderCertificateId;
    }

    public void setHolderCertificateId(String holderCertificateId) {
        this.holderCertificateId = holderCertificateId;
    }

    public Integer getAdmissionStatus() {
        return admissionStatus;
    }

    public void setAdmissionStatus(Integer admissionStatus) {
        this.admissionStatus = admissionStatus;
    }

    public java.util.Date getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(java.util.Date admissionTime) {
        this.admissionTime = admissionTime;
    }

    public java.util.Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(java.util.Date departureTime) {
        this.departureTime = departureTime;
    }

}
