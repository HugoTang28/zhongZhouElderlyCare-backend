package com.zzyl.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.zzyl.admin.domain.dto.LoginBody;
import com.zzyl.admin.domain.vo.RouterVo;
import com.zzyl.common.core.R;
import com.zzyl.common.exception.ServiceException;
import com.zzyl.framework.security.LoginUser;
import com.zzyl.framework.security.SecurityUtils;
import com.zzyl.framework.security.TokenService;
import com.zzyl.framework.security.UserDetailsServiceImpl;
import com.zzyl.system.domain.SysMenu;
import com.zzyl.system.domain.SysUser;
import com.zzyl.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 登录认证
 */
@Tag(name = "登录认证")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;
    private final SysMenuService menuService;

    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
    public R<Map<String, String>> login(@Validated @RequestBody LoginBody loginBody) {
        // 认证（密码错误会抛出 BadCredentialsException，由全局异常处理）
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginBody.getUsername(), loginBody.getPassword()));

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 补充角色与权限
        loginUser = userDetailsService.buildLoginUser(loginUser.getUser());

        String token = tokenService.createToken(loginUser);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return R.ok("登录成功", result);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/getInfo")
    public R<Map<String, Object>> getInfo() {
        LoginUser loginUser = currentLoginUser();
        Map<String, Object> result = new HashMap<>();
        result.put("user", loginUser.getUser());
        result.put("roles", loginUser.getRoles());
        result.put("permissions", loginUser.getPermissions());
        return R.ok(result);
    }

    @Operation(summary = "获取当前用户动态路由")
    @GetMapping("/getRouters")
    public R<List<RouterVo>> getRouters() {
        LoginUser loginUser = currentLoginUser();
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getId());
        return R.ok(buildRouters(menus));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<Void> logout() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            tokenService.removeToken(loginUser.getUuid());
        }
        return R.ok("退出成功", null);
    }

    private LoginUser currentLoginUser() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new ServiceException(401, "登录状态已过期，请重新登录");
        }
        return loginUser;
    }

    /**
     * 菜单树转换为前端路由结构
     */
    private List<RouterVo> buildRouters(List<SysMenu> menus) {
        return buildRouters(menus, "");
    }

    /**
     * 菜单树转换为前端路由结构（带父级完整路径，用于生成唯一路由名）
     */
    private List<RouterVo> buildRouters(List<SysMenu> menus, String parentFullPath) {
        return menus.stream().map(menu -> {
            RouterVo router = new RouterVo();
            // 完整路径：一级目录以 / 开头，子级拼接父路径
            String fullPath = menu.getParentId() == 0
                    ? "/" + menu.getPath()
                    : parentFullPath + "/" + menu.getPath();
            // 路由名必须全局唯一（vue-router 重名会互相覆盖导致 404），用完整路径生成
            router.setName(pathToName(fullPath));
            router.setPath(menu.getParentId() == 0 ? "/" + menu.getPath() : menu.getPath());
            router.setComponent(menu.getComponent());
            router.setHidden(menu.getVisible() != null && menu.getVisible() == 1);
            router.setMeta(new RouterVo.MetaVo(menu.getMenuName(), menu.getIcon()));
            // 一级目录使用 Layout 布局
            if (menu.getParentId() == 0 && "M".equals(menu.getMenuType())) {
                router.setComponent("Layout");
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
            }
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                router.setChildren(buildRouters(menu.getChildren(), fullPath));
            }
            return router;
        }).collect(Collectors.toList());
    }

    /**
     * 完整路径转 camelCase 唯一路由名：/order/list -> OrderList
     */
    private String pathToName(String fullPath) {
        StringBuilder sb = new StringBuilder();
        for (String seg : fullPath.split("/")) {
            if (!seg.isEmpty()) {
                sb.append(StrUtil.upperFirst(seg));
            }
        }
        return sb.toString();
    }
}
