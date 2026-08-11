package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("checkin_admission")
@Schema(description = "入退-入住管理")
public class CheckinAdmission extends BaseEntity {
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "idCard")    private String idCard;
    @Schema(description = "sex")    private Integer sex;
    @Schema(description = "age")    private Integer age;
    @Schema(description = "bedId")    private Long bedId;
    @Schema(description = "contractNo")    private String contractNo;
    @Schema(description = "familyName")    private String familyName;
    @Schema(description = "familyPhone")    private String familyPhone;
    @Schema(description = "checkInTime")    private LocalDateTime checkInTime;
    @Schema(description = "status")    private Integer status;
}
