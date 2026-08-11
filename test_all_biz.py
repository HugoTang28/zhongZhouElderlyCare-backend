import urllib.request, urllib.error, json, time, sys

BASE = "http://localhost:9995"
TS = str(int(time.time()))

# 每个模块的 @BizModule value(key) + 一个最小可插入示例
SAMPLES = {
    "visitAppointment": {"visitorName": "访客A" + TS, "phone": "13800000000", "elderName": "张三",
                          "visitTime": "2026-08-12 10:00:00", "purpose": "探望", "status": 0},
    "visitRegistration": {"visitorName": "访客B" + TS, "phone": "13800000001", "idCard": "110101199001011234",
                           "elderName": "李四", "relation": "儿子", "arriveTime": "2026-08-12 09:00:00",
                           "leaveTime": "2026-08-12 11:00:00"},
    "checkinAdmission": {"elderName": "王五" + TS, "idCard": "110101199002021234", "sex": 0, "age": 80,
                          "contractNo": "HT" + TS, "familyName": "王父", "familyPhone": "13800000002",
                          "checkInTime": "2026-08-10 10:00:00", "status": 0},
    "checkinCheckout": {"elderName": "赵六" + TS, "bedId": 1, "checkOutTime": "2026-08-11 10:00:00",
                         "reason": "回家", "refundAmount": 100.00, "status": 0},
    "residentRoom": {"roomNo": "R" + TS, "typeName": "标准单间", "floor": "3", "bedCount": 2,
                      "area": 25.5, "status": 0},
    "residentBed": {"bedNo": "B" + TS, "roomId": 1, "status": 0},
    "residentContract": {"contractNo": "C" + TS, "elderName": "钱七", "bedId": 1,
                          "startDate": "2026-08-01 00:00:00", "endDate": "2027-07-31 00:00:00",
                          "monthlyFee": 3000.00, "deposit": 5000.00, "status": 0},
    "residentLeave": {"elderName": "孙八" + TS, "bedId": 1, "startTime": "2026-08-15 00:00:00",
                       "endTime": "2026-08-20 00:00:00", "reason": "就医", "status": 0},
    "serviceTask": {"taskName": "测体温" + TS, "elderName": "周九", "executor": "护士小美",
                     "planTime": "2026-08-12 08:00:00", "status": 0},
    "servicePlan": {"planName": "康复计划" + TS, "elderName": "周九", "startDate": "2026-08-12 00:00:00",
                     "endDate": "2026-09-12 00:00:00", "content": "每日散步", "status": 0},
    "financeBill": {"billNo": "F" + TS, "elderName": "周九", "billMonth": "2026-08",
                     "billType": "护理", "amount": 1200.00, "status": 0},
    "financePrestore": {"elderName": "周九", "amount": 500.00, "balance": 500.00, "transType": 0},
    "orderInfo": {"orderNo": "O" + TS, "customerName": "吴十", "productName": "护理套餐",
                  "amount": 880.00, "status": 0},
    "orderRefund": {"orderNo": "O" + TS, "refundNo": "RF" + TS, "amount": 100.00, "status": 0},
    "customerInfo": {"name": "客户" + TS, "phone": "13900000000", "idCard": "110101199003031234",
                      "cusType": "潜在", "source": "官网"},
    "messagePush": {"title": "温馨提示" + TS, "content": "今日有活动", "receiver": "家属",
                     "msgType": "通知", "status": 0},
    "iotDevice": {"deviceName": "手环" + TS, "deviceNo": "D" + TS, "deviceType": "手环",
                   "location": "301室", "status": 1},
    "iotAlarmRule": {"ruleName": "心率预警" + TS, "deviceType": "手环", "alarmCondition": "心率>120",
                      "threshold": "120", "alarmLevel": 0, "status": 0},
}

def req(method, path, token=None, data=None):
    headers = {}
    if token:
        headers["Authorization"] = "Bearer " + token
    body = None
    if data is not None:
        headers["Content-Type"] = "application/json"
        body = json.dumps(data).encode("utf-8")
    r = urllib.request.Request(BASE + path, data=body, headers=headers, method=method)
    try:
        resp = urllib.request.urlopen(r, timeout=15)
        return resp.status, resp.read().decode("utf-8")
    except urllib.error.HTTPError as e:
        return e.code, e.read().decode("utf-8")

def jd(body):
    try:
        return json.loads(body)
    except Exception:
        return {}

def ok(status, body, expect=200):
    return status == expect and jd(body).get("code") == 200

def main():
    s, d = req("POST", "/auth/login", data={"username": "admin", "password": "123456"})
    if not ok(s, d):
        print("LOGIN FAIL", s, d[:200]); sys.exit(1)
    tok = jd(d)["data"]["token"]
    print("LOGIN OK\n")

    results = []
    for mod, sample in SAMPLES.items():
        # add
        s1, d1 = req("POST", f"/biz/{mod}", tok, sample)
        added = ok(s1, d1)
        # 查询刚插入的记录取 id
        s2, d2 = req("GET", f"/biz/{mod}/list?pageSize=5", tok)
        recs = jd(d2).get("data", {}).get("records", []) if ok(s2, d2) else []
        nid = None
        for r in recs:
            if r.get("visitorName") == sample.get("visitorName") or \
               r.get("elderName") == sample.get("elderName") or \
               r.get("roomNo") == sample.get("roomNo") or \
               r.get("bedNo") == sample.get("bedNo") or \
               r.get("contractNo") == sample.get("contractNo") or \
               r.get("billNo") == sample.get("billNo") or \
               r.get("orderNo") == sample.get("orderNo") or \
               r.get("name") == sample.get("name") or \
               r.get("title") == sample.get("title") or \
               r.get("deviceName") == sample.get("deviceName") or \
               r.get("ruleName") == sample.get("ruleName") or \
               r.get("taskName") == sample.get("taskName") or \
               r.get("planName") == sample.get("planName"):
                nid = r.get("id"); break
        # get
        s3, d3 = (req("GET", f"/biz/{mod}/{nid}", tok) if nid else (0, ""))
        got = ok(s3, d3)
        # edit: 把 status 改为 1（若为 0）或改一个文本字段
        edited = True
        if nid:
            upd = dict(sample)
            upd["id"] = nid
            # 找一个整型 status 改成 1 以验证更新
            if "status" in upd:
                upd["status"] = 1 if upd.get("status") == 0 else 0
            s4, d4 = req("PUT", f"/biz/{mod}", tok, upd)
            edited = ok(s4, d4)
        # remove
        removed = True
        if nid:
            s5, d5 = req("DELETE", f"/biz/{mod}/{nid}", tok)
            removed = ok(s5, d5)
        # 验证已删除
        s6, d6 = req("GET", f"/biz/{mod}/list?pageSize=5", tok)
        after = jd(d6).get("data", {}).get("records", []) if ok(s6, d6) else []
        still = any(r.get("id") == nid for r in after) if nid else False
        passed = added and got and edited and removed and (not still)
        results.append((mod, passed, added, got, edited, removed))
        print(f"[{'PASS' if passed else 'FAIL'}] {mod:20s} add={added} get={got} edit={edited} del={removed} gone={not still}")

    # workbench stats
    s, d = req("GET", "/workbench/stats", tok)
    wb = ok(s, d)
    wbd = jd(d).get("data", {})
    print(f"\n[{'PASS' if wb else 'FAIL'}] workbench/stats http={s} keys={list(wbd.keys())[:6]}...")

    fails = [m for m, p, *_ in results if not p]
    wbfail = not wb
    print("\n==== SUMMARY ====")
    print(f"modules tested: {len(results)}, passed: {sum(1 for _,p,*_ in results if p)}")
    if fails:
        print("FAILED modules:", fails)
    if wbfail:
        print("FAILED: workbench/stats")
    print("ALL PASS" if not fails and not wbfail else "HAS FAILURES")

if __name__ == "__main__":
    main()
