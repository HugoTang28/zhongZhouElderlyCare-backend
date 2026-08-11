package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("visit_registration")
@Schema(description = "来访-来访登记")
public class VisitRegistration extends BaseEntity {
    @Schema(description = "visitorName")    private String visitorName;
    @Schema(description = "phone")    private String phone;
    @Schema(description = "idCard")    private String idCard;
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "relation")    private String relation;
    @Schema(description = "arriveTime")    private LocalDateTime arriveTime;
    @Schema(description = "leaveTime")    private LocalDateTime leaveTime;
}
