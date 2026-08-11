package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("visit_appointment")
@Schema(description = "来访-预约登记")
public class VisitAppointment extends BaseEntity {
    @Schema(description = "visitorName")    private String visitorName;
    @Schema(description = "phone")    private String phone;
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "visitTime")    private LocalDateTime visitTime;
    @Schema(description = "purpose")    private String purpose;
    @Schema(description = "status")    private Integer status;
}
