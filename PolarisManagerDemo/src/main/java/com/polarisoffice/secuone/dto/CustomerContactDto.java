package com.polarisoffice.secuone.dto;

import lombok.*;

public final class CustomerContactDto {
  private CustomerContactDto() {} // 바깥 DTO 컨테이너는 인스턴스화 금지

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
  public static class CreateReq {
      private String customerCode;  // 필수
      private String name;          // 필수
      private String email;         // 선택
      private String phone;         // 선택
      private String note;          // 선택
      private Boolean sendInvite;   // 선택 (true면 메일 발송)
  }

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
  public static class Res {
      private Long id;
      private String customerCode;
      private String name;
      private String email;
      private String phone;
      private String note;
  }
}
