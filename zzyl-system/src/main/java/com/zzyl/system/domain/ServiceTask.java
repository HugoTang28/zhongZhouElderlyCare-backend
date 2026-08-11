package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_task")
@Schema(description = "服务-护理任务")
public class ServiceTask extends BaseEntity {
    @Schema(description = "taskName")    private String taskName;
    @Schema(description = "elderName")    private String elderName;
    @Schema(description = "executor")    private String executor;
    @Schema(description = "planTime")    private LocalDateTime planTime;
    @Schema(description = "status")    private Integer status;
}
