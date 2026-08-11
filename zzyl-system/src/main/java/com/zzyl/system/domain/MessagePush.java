package com.zzyl.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.core.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("message_push")
@Schema(description = "消息中心-消息推送")
public class MessagePush extends BaseEntity {
    @Schema(description = "title")    private String title;
    @Schema(description = "content")    private String content;
    @Schema(description = "receiver")    private String receiver;
    @Schema(description = "msgType")    private String msgType;
    @Schema(description = "status")    private Integer status;
}
