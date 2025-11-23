
package com.example.examengine.entity;

import javax.persistence.*;

@Entity
@Table(name = "Centre")
public class Centre {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "center_name")
    private String centerName;

    private Integer capacity;

    @Column(name = "is_pwd_friendly")
    private Boolean isPwdFriendly;

    public Centre() {}

    public Long getId() { return id; }
    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Boolean getIsPwdFriendly() { return isPwdFriendly; }
    public void setIsPwdFriendly(Boolean isPwdFriendly) { this.isPwdFriendly = isPwdFriendly; }
}
