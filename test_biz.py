import urllib.request, json, sys, time

BASE = "http://localhost:9995"
TS = str(int(time.time()))

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
        resp = urllib.request.urlopen(r, timeout=10)
        return resp.status, resp.read().decode("utf-8")
    except urllib.error.HTTPError as e:
        return e.code, e.read().decode("utf-8")

def show(tag, status, body, limit=160):
    print(f"[{tag}] http={status} body={body[:limit]}")

s, d = req("POST", "/auth/login", data={"username": "admin", "password": "123456"})
show("login", s, d, 70)
tok = json.loads(d)["data"]["token"]

MOD = "visitAppointment"

# list
s, d = req("GET", f"/biz/{MOD}/list?pageNum=1&pageSize=5", tok)
show(f"{MOD}.list", s, d)

# add
s, d = req("POST", f"/biz/{MOD}", tok, {"visitorName": "zuser"+TS, "phone": "13800000000",
            "elderName": "李四", "visitTime": "2026-08-12 10:00:00", "purpose": "探望", "status": 0})
show(f"{MOD}.add", s, d)

# find id by query
s, d = req("GET", f"/biz/{MOD}/list?visitorName=zuser{TS}", tok)
recs = json.loads(d)["data"]["records"]
print("search records:", len(recs))
nid = recs[0]["id"] if recs else None
print("new id =", nid)

if nid:
    # get
    s, d = req("GET", f"/biz/{MOD}/{nid}", tok)
    show(f"{MOD}.get", s, d)
    # edit
    s, d = req("PUT", f"/biz/{MOD}", tok, {"id": nid, "visitorName": "zuserx"+TS, "status": 1})
    show(f"{MOD}.edit", s, d)
    # remove
    s, d = req("DELETE", f"/biz/{MOD}/{nid}", tok)
    show(f"{MOD}.remove", s, d)
    # verify gone
    s, d = req("GET", f"/biz/{MOD}/list?visitorName=zuserx{TS}", tok)
    print("after delete remaining:", len(json.loads(d)["data"]["records"]))

# 无 token 访问 -> 401
s, d = req("GET", f"/biz/{MOD}/list")
show(f"no-token.{MOD}.list", s, d, 160)

# 不存在的模块 -> 应 401/500（权限或找不到）
s, d = req("GET", "/biz/notExistModule/list", tok)
show("biz.notExist.list", s, d, 160)

print("DONE")
