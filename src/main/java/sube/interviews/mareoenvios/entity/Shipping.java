package sube.interviews.mareoenvios.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Shipping")
public class Shipping {
    @Id
    private Integer id;
    private Integer customerId;
    private String state;
    @Temporal(TemporalType.DATE)
    private Date sendDate;
    @Temporal(TemporalType.DATE)
    private Date arriveDate;
    private Integer priority;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
        this.arriveDate = arriveDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}