package com.zzyl.admin.controller.system;

import com.zzyl.common.core.R;
import com.zzyl.system.domain.SysMenu;
import com.zzyl.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @Operation(summary = "菜单列表（平铺）")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping("/list")
    public R<List<SysMenu>> list(SysMenu menu) {
        return R.ok(menuService.selectMenuList(menu));
    }

    @Operation(summary = "菜单下拉树")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping("/treeselect")
    public R<List<SysMenu>> treeselect() {
        return R.ok(menuService.treeselect());
    }

    @Operation(summary = "菜单详情")
    @PreAuthorize("hasAuthority('system:menu:query')")
    @GetMapping("/{id}")
    public R<SysMenu> getInfo(@PathVariable Long id) {
        return R.ok(menuService.selectMenuById(id));
    }

    @Operation(summary = "新增菜单")
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysMenu menu) {
        menuService.insertMenu(menu);
        return R.ok();
    }

    @Operation(summary = "修改菜单")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysMenu menu) {
        menuService.updateMenu(menu);
        return R.ok();
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        menuService.deleteMenuById(id);
        return R.ok();
    }
}
