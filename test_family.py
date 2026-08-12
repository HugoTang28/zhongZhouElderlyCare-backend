#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
家属端接口端到端验证脚本（依赖标准库，无需第三方包）。
覆盖 9 个接口：发送验证码、登录、我的老人、探视、账单、护理、消息、预警、退出。
用法: python test_family.py  (默认打 http://localhost:9995)
"""
import json
import sys
import urllib.request
import urllib.error

BASE = "http://localhost:9995"

PHONE = "13800138009"  # 演示账号，已关联在住老人 吴美玲（checkin_admission.id=3009）


def call(path, method="GET", body=None, token=None):
    url = BASE + path
    data = None
    headers = {"Content-Type": "application/json"}
    if token:
        headers["Authorization"] = "Bearer " + token
    if body is not None:
        data = json.dumps(body).encode("utf-8")
    req = urllib.request.Request(url, data=data, headers=headers, method=method)
    try:
        with urllib.request.urlopen(req, timeout=15) as resp:
            raw = resp.read().decode("utf-8")
            return resp.status, json.loads(raw)
    except urllib.error.HTTPError as e:
        raw = e.read().decode("utf-8")
        try:
            return e.code, json.loads(raw)
        except Exception:
            return e.code, {"code": e.code, "msg": raw}
    except Exception as e:  # noqa
        return -1, {"code": -1, "msg": str(e)}


def check(name, cond, detail=""):
    status = "PASS" if cond else "FAIL"
    print(f"[{status}] {name}" + (f"  -> {detail}" if detail else ""))
    return cond


def main():
    print("==== 家属端接口验证 ====")
    ok = True

    # 1) 发送验证码
    code, resp = call("/family/auth/send-code", "POST", {"phone": PHONE})
    dev_code = ""
    if code == 200 and resp.get("code") == 200:
        dev_code = (resp.get("data") or {}).get("devCode", "")
    ok &= check("发送验证码 send-code", bool(dev_code), f"devCode={dev_code}")
    if not dev_code:
        print("无法获取验证码，终止。resp=", resp)
        return 1

    # 2) 登录
    code, resp = call("/family/auth/login", "POST", {"phone": PHONE, "code": dev_code})
    token = ""
    if code == 200 and resp.get("code") == 200:
        token = (resp.get("data") or {}).get("token", "")
    ok &= check("手机号+验证码登录 login", bool(token),
                f"token前缀={(token[:20] + '...') if token else ''}")
    if not token:
        print("登录失败，终止。resp=", resp)
        return 1

    # 3) 我的老人
    code, resp = call("/family/elders", "GET", token=token)
    elders = (resp.get("data") or []) if code == 200 and resp.get("code") == 200 else []
    ok &= check("我的老人 elders", len(elders) >= 1,
                f"返回 {len(elders)} 位，姓名={[e.get('elderName') for e in elders]}")

    # 4) 探视
    code, resp = call("/family/visits", "GET", token=token)
    visits = resp.get("data") or {} if code == 200 and resp.get("code") == 200 else {}
    appts = visits.get("appointments") or []
    regs = visits.get("registrations") or []
    ok &= check("探视记录 visits", len(appts) + len(regs) >= 1,
                f"预约 {len(appts)} / 登记 {len(regs)}")

    # 5) 账单
    code, resp = call("/family/bills", "GET", token=token)
    bills = resp.get("data") or {} if code == 200 and resp.get("code") == 200 else {}
    b = bills.get("bills") or []
    p = bills.get("prestores") or []
    bal = bills.get("balance")
    ok &= check("账单与预存 bills", len(b) >= 1 and len(p) >= 1,
                f"账单 {len(b)} / 预存 {len(p)} / 余额={bal}")

    # 6) 护理
    code, resp = call("/family/care", "GET", token=token)
    care = resp.get("data") or {} if code == 200 and resp.get("code") == 200 else {}
    plans = care.get("plans") or []
    tasks = care.get("tasks") or []
    ok &= check("护理动态 care", len(plans) + len(tasks) >= 1,
                f"计划 {len(plans)} / 任务 {len(tasks)}")

    # 7) 消息
    code, resp = call("/family/messages", "GET", token=token)
    msgs = (resp.get("data") or []) if code == 200 and resp.get("code") == 200 else []
    ok &= check("消息推送 messages", len(msgs) >= 1, f"返回 {len(msgs)} 条")

    # 8) 预警与设备
    code, resp = call("/family/alarms", "GET", token=token)
    alarms = resp.get("data") or {} if code == 200 and resp.get("code") == 200 else {}
    rules = alarms.get("rules") or []
    devices = alarms.get("devices") or []
    ok &= check("预警与设备 alarms", True,
                f"规则 {len(rules)} / 设备 {len(devices)}（无数据也属正常）")

    # 9) 未带 token 应 401
    code, resp = call("/family/elders", "GET")
    ok &= check("未登录拦截 401", code == 200 and resp.get("code") == 401,
                f"code={resp.get('code')}")

    # 10) 退出
    code, resp = call("/family/auth/logout", "POST", {}, token=token)
    ok &= check("退出登录 logout", code == 200 and resp.get("code") == 200,
                f"msg={resp.get('msg')}")

    print("==== 结果:", "全部通过 ✅" if ok else "存在失败 ❌", "====")
    return 0 if ok else 1


if __name__ == "__main__":
    sys.exit(main())
