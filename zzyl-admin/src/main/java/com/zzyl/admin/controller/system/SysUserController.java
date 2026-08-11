package com.zzyl.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzyl.common.core.R;
import com.zzyl.system.domain.SysRole;
import com.zzyl.system.domain.SysUser;
import com.zzyl.system.service.SysRoleService;
import com.zzyl.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;
    private final SysRoleService roleService;

    @Operation(summary = "分页查询用户")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/list")
    public R<IPage<SysUser>> list(Page<SysUser> page, SysUser user) {
        return R.ok(userService.selectPage(page, user));
    }

    @Operation(summary = "角色下拉选项")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/roleOptions")
    public R<List<SysRole>> roleOptions() {
        return R.ok(roleService.optionSelect());
    }

    @Operation(summary = "获取用户详情（含已分配角色）")
    @PreAuthorize("hasAuthority('system:user:query')")
    @GetMapping("/{id}")
    public R<Map<String, Object>> getInfo(@PathVariable Long id) {
        SysUser user = userService.getById(id);
        List<Long> roleIds = userService.selectRoleIdsByUserId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roleIds", roleIds);
        result.put("roles", roleService.optionSelect());
        return R.ok(result);
    }

    @Operation(summary = "新增用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysUser user) {
        userService.insertUser(user, user.getRoleIds());
        return R.ok();
    }

    @Operation(summary = "修改用户")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysUser user) {
        userService.updateUser(user, user.getRoleIds());
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('system:user:remove')")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        userService.deleteUserByIds(ids);
        return R.ok();
    }

    @Operation(summary = "重置密码")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    @PutMapping("/resetPwd")
    public R<Void> resetPwd(@RequestParam Long userId, @RequestParam String password) {
        userService.resetPwd(userId, password);
        return R.ok();
    }

    @Operation(summary = "状态修改")
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestParam Long userId, @RequestParam Integer status) {
        userService.updateStatus(userId, status);
        return R.ok();
    }
}
