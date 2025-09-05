package com.polarisoffice.secuone.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
    name = "settlements",
    uniqueConstraints = @UniqueConstraint(
        name="uk_settle_customer_month",
        columnNames = {"customer_code","settle_month"}
    )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SettlementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 고객사 FK → customers.code (String PK)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "customer_code",
      referencedColumnName = "code",
      nullable = false,
      foreignKey = @ForeignKey(name="fk_settle_customer_code")
  )
  private CustomerEntity customer;

  // 정산 대상 월(YYYY-MM, 예: 2025-08). YearMonth를 String으로 저장
  @Column(name = "settle_month", length = 7, nullable = false)
  private String settleMonth;

  // 집계 지표
  @Column(name = "downloads")
  private Long downloads;

  @Column(name = "deletes")
  private Long deletes;

  public Long getId() {
    return id;
}

  public void setId(Long id) {
    this.id = id;
  }

  public CustomerEntity getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerEntity customer) {
    this.customer = customer;
  }

  public String getSettleMonth() {
    return settleMonth;
  }

  public void setSettleMonth(String settleMonth) {
    this.settleMonth = settleMonth;
  }

  public Long getDownloads() {
    return downloads;
  }

  public void setDownloads(Long downloads) {
    this.downloads = downloads;
  }

  public Long getDeletes() {
    return deletes;
  }

  public void setDeletes(Long deletes) {
    this.deletes = deletes;
  }

  public BigDecimal getCpiRate() {
    return cpiRate;
  }

  public void setCpiRate(BigDecimal cpiRate) {
    this.cpiRate = cpiRate;
  }

  public BigDecimal getRsRate() {
    return rsRate;
  }

  public void setRsRate(BigDecimal rsRate) {
    this.rsRate = rsRate;
  }

  public BigDecimal getCpiAmount() {
    return cpiAmount;
  }

  public void setCpiAmount(BigDecimal cpiAmount) {
    this.cpiAmount = cpiAmount;
  }

  public BigDecimal getRsAmount() {
    return rsAmount;
  }

  public void setRsAmount(BigDecimal rsAmount) {
    this.rsAmount = rsAmount;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  // 요율/금액
  @Column(name = "cpi_rate", precision = 6, scale = 2)
  private BigDecimal cpiRate;

  @Column(name = "rs_rate", precision = 6, scale = 2)
  private BigDecimal rsRate;

  @Column(name = "cpi_amount", precision = 18, scale = 2)
  private BigDecimal cpiAmount;

  @Column(name = "rs_amount", precision = 18, scale = 2)
  private BigDecimal rsAmount;

  @Column(name = "total_amount", precision = 18, scale = 2)
  private BigDecimal totalAmount;

  @Column(name = "currency", length = 3)
  private String currency; // KRW, USD 등

  @Column(name = "memo", columnDefinition = "TEXT")
  private String memo;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @PrePersist
  void onCreate() {
    if (createdAt == null) createdAt = Instant.now();
  }
}
