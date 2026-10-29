package com.zzyl.admin.controller;

import cn.hutool.core.util.RandomUtil;
import com.zzyl.admin.domain.dto.FamilyLoginBody;
import com.zzyl.admin.domain.dto.FamilySendCodeBody;
import com.zzyl.common.constant.Constants;
import com.zzyl.common.core.R;
import com.zzyl.common.exception.ServiceException;
import com.zzyl.framework.cache.CacheService;
import com.zzyl.framework.security.LoginUser;
import com.zzyl.framework.security.SecurityUtils;
import com.zzyl.framework.security.TokenService;
import com.zzyl.system.domain.CheckinAdmission;
import com.zzyl.system.domain.FamilyUser;
import com.zzyl.system.service.FamilyService;
import com.zzyl.system.service.FamilyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 家属端接口
 */
@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
@Tag(name = "家属端")
public class FamilyController {

    private final FamilyUserService familyUserService;
    private final FamilyService familyService;
    private final TokenService tokenService;
    private final CacheService cacheService;

    @Operation(summary = "发送短信验证码（开发期在响应中返回 devCode，生产接入短信通道后移除）")
    @PostMapping("/auth/send-code")
    public R<Map<String, String>> sendCode(@Validated @RequestBody FamilySendCodeBody body) {
        String phone = body.getPhone();
        String code = String.format("%06d", RandomUtil.randomInt(0, 1000000));
        cacheService.set(Constants.FAMILY_CODE_KEY + phone, code, 5, TimeUnit.MINUTES);
        Map<String, String> result = new HashMap<>();
        result.put("devCode", code);
        return R.ok("验证码已发送", result);
    }

    @Operation(summary = "手机号+验证码登录（首次登录自动建档并关联在住老人）")
    @PostMapping("/auth/login")
    public R<Map<String, Object>> login(@Validated @RequestBody FamilyLoginBody body) {
        String phone = body.getPhone();
        String code = body.getCode();

        String cached = cacheService.get(Constants.FAMILY_CODE_KEY + phone, String.class);
        if (cached == null || !cached.equals(code)) {
            throw new ServiceException("验证码错误或已过期");
        }

        // 校验该手机号是否关联在住老人，未绑定则拒绝
        List<String> elderNames = familyService.getElderNames(phone);
        if (elderNames.isEmpty()) {
            throw new ServiceException("该手机号未关联在住老人，请联系养老机构");
        }

        FamilyUser familyUser = familyUserService.getOrCreate(phone);

        LoginUser loginUser = new LoginUser();
        loginUser.setUserType(Constants.USER_TYPE_FAMILY);
        loginUser.setFamilyUser(familyUser);
        loginUser.setRoles(Collections.emptySet());
        loginUser.setPermissions(Collections.emptySet());
        String token = tokenService.createToken(loginUser);

        cacheService.delete(Constants.FAMILY_CODE_KEY + phone);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("phone", phone);
        result.put("elderNames", elderNames);
        return R.ok("登录成功", result);
    }

    @Operation(summary = "我的老人")
    @GetMapping("/elders")
    public R<List<CheckinAdmission>> elders() {
        LoginUser loginUser = currentFamily();
        return R.ok(familyService.getMyElders(loginUser.getFamilyUser().getPhone()));
    }

    @Operation(summary = "探视记录（预约+登记）")
    @GetMapping("/visits")
    public R<Map<String, Object>> visits() {
        LoginUser loginUser = currentFamily();
        return R.ok(familyService.getVisits(loginUser.getFamilyUser().getPhone()));
    }

    @Operation(summary = "账单与预存（含账户余额）")
    @GetMapping("/bills")
    public R<Map<String, Object>> bills() {
        LoginUser loginUser = currentFamily();
        return R.ok(familyService.getBills(loginUser.getFamilyUser().getPhone()));
    }

    @Operation(summary = "护理动态（计划+任务）")
    @GetMapping("/care")
    public R<Map<String, Object>> care() {
        LoginUser loginUser = currentFamily();
        return R.ok(familyService.getCare(loginUser.getFamilyUser().getPhone()));
    }

    @Operation(summary = "消息推送")
    @GetMapping("/messages")
    public R<List<?>> messages() {
        LoginUser loginUser = currentFamily();
        return R.ok(familyService.getMessages(loginUser.getFamilyUser().getPhone()));
    }

    @Operation(summary = "预警与设备概览")
    @GetMapping("/alarms")
    public R<Map<String, Object>> alarms() {
        LoginUser loginUser = currentFamily();
        return R.ok(familyService.getAlarms(loginUser.getFamilyUser().getPhone()));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/auth/logout")
    public R<Void> logout() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            tokenService.removeToken(loginUser.getUuid());
        }
        return R.ok("退出成功", null);
    }

    private LoginUser currentFamily() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || !Constants.USER_TYPE_FAMILY.equals(loginUser.getUserType())) {
            throw new ServiceException(401, "请先登录");
        }
        return loginUser;
    }
}
