package com.zzyl.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 前端动态路由 VO（适配 Vue Router）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 路由名称 */
    private String name;

    /** 路由地址 */
    private String path;

    /** 是否隐藏 */
    private Boolean hidden;

    /** 重定向地址 */
    private String redirect;

    /** 组件地址 */
    private String component;

    /** 是否总是显示根菜单 */
    private Boolean alwaysShow;

    /** 元信息 */
    private MetaVo meta;

    /** 子路由 */
    private List<RouterVo> children;

    @Data
    public static class MetaVo implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /** 菜单标题 */
        private String title;

        /** 菜单图标 */
        private String icon;

        /** 是否缓存 */
        private boolean noCache;

        public MetaVo(String title, String icon) {
            this.title = title;
            this.icon = icon;
        }
    }
}
