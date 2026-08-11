package com.zzyl.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzyl.common.core.R;
import com.zzyl.system.domain.SysMenu;
import com.zzyl.system.domain.SysRole;
import com.zzyl.system.service.SysMenuService;
import com.zzyl.system.service.SysRoleService;
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
 * 角色管理
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;
    private final SysMenuService menuService;

    @Operation(summary = "分页查询角色")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping("/list")
    public R<IPage<SysRole>> list(Page<SysRole> page, SysRole role) {
        return R.ok(roleService.selectPage(page, role));
    }

    @Operation(summary = "菜单下拉树")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping("/menuTree")
    public R<List<SysMenu>> menuTree() {
        return R.ok(menuService.treeselect());
    }

    @Operation(summary = "角色详情（含已分配菜单）")
    @PreAuthorize("hasAuthority('system:role:query')")
    @GetMapping("/{id}")
    public R<Map<String, Object>> getInfo(@PathVariable Long id) {
        SysRole role = roleService.getById(id);
        List<Long> menuIds = roleService.selectMenuIdsByRoleId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("role", role);
        result.put("menuIds", menuIds);
        return R.ok(result);
    }

    @Operation(summary = "新增角色")
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysRole role) {
        roleService.insertRole(role, role.getMenuIds());
        return R.ok();
    }

    @Operation(summary = "修改角色")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysRole role) {
        roleService.updateRole(role, role.getMenuIds());
        return R.ok();
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('system:role:remove')")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        roleService.deleteRoleByIds(ids);
        return R.ok();
    }

    @Operation(summary = "分配菜单权限")
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/authMenu")
    public R<Void> authMenu(@RequestParam Long roleId, @RequestParam Long[] menuIds) {
        roleService.authMenu(roleId, menuIds);
        return R.ok();
    }
}
