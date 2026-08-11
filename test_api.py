import urllib.request, json, sys, time

BASE = "http://localhost:9995"
TS = str(int(time.time()))  # 每次运行唯一后缀，避免名称冲突


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


def show(tag, status, body, limit=140):
    print(f"[{tag}] http={status} body={body[:limit]}")


def find_id_by_name(list_path, name_field, name, token):
    s, d = req("GET", list_path, token)
    try:
        data = json.loads(d)["data"]
        # 兼容两种返回：分页接口包装在 data.records，菜单/部门列表直接是数组
        recs = data["records"] if isinstance(data, dict) and "records" in data else data
        for rec in recs:
            if rec.get(name_field) == name:
                return rec["id"]
    except Exception:
        pass
    return None


def delete_and_verify(del_path, list_path, name_field, name, token, tag):
    s, d = req("DELETE", del_path, token)
    show(tag + ".del", s, d)
    rid = find_id_by_name(list_path, name_field, name, token)
    print(f"  -> after delete, remaining '{name}' id = {rid} (None 表示删除成功)")


# 1. 登录
s, d = req("POST", "/auth/login", data={"username": "admin", "password": "123456"})
show("login", s, d, 80)
try:
    tok = json.loads(d)["data"]["token"]
except Exception:
    print("LOGIN FAILED, abort")
    sys.exit(1)

# 2. 四个列表 + 菜单树（只读，验证权限放行）
for name, path in [
    ("user.list", "/system/user/list?pageNum=1&pageSize=2"),
    ("role.list", "/system/role/list?pageNum=1&pageSize=2"),
    ("menu.list", "/system/menu/list"),
    ("dept.list", "/system/dept/list"),
    ("role.menuTree", "/system/role/menuTree"),
]:
    s, d = req("GET", path, tok)
    show(name, s, d)

# 3. 新增用户 + 删除用户（验证增删链路）
UNAME = "tuser_" + TS
s, d = req("POST", "/system/user", tok, {"username": UNAME, "nickName": "测试",
            "password": "123456", "status": 0, "roleIds": [2]})
show("user.add", s, d)
uid = find_id_by_name("/system/user/list?pageSize=10", "username", UNAME, tok)
print("new user id =", uid)
if uid:
    delete_and_verify(f"/system/user/{uid}", "/system/user/list?pageSize=10", "username", UNAME, tok, "user")

# 4. 新增菜单 / 部门 / 角色（唯一名称）+ 删除
MNAME = "测试菜单_" + TS
s, d = req("POST", "/system/menu", tok, {"menuName": MNAME, "parentId": 0, "menuType": "M",
            "path": "tm" + TS, "component": "system/test/index", "sort": 99, "status": 0, "visible": 0})
show("menu.add", s, d)
mid = find_id_by_name("/system/menu/list", "menuName", MNAME, tok)
if mid:
    delete_and_verify(f"/system/menu/{mid}", "/system/menu/list", "menuName", MNAME, tok, "menu")

DNAME = "测试部门_" + TS
s, d = req("POST", "/system/dept", tok, {"deptName": DNAME, "parentId": 0, "sort": 99, "status": 0})
show("dept.add", s, d)
did = find_id_by_name("/system/dept/list", "deptName", DNAME, tok)
if did:
    delete_and_verify(f"/system/dept/{did}", "/system/dept/list", "deptName", DNAME, tok, "dept")

RNAME = "测试角色_" + TS
s, d = req("POST", "/system/role", tok, {"roleName": RNAME, "roleKey": "testrole" + TS,
            "sort": 99, "status": 0, "menuIds": [1]})
show("role.add", s, d)
rid = find_id_by_name("/system/role/list?pageSize=10", "roleName", RNAME, tok)
if rid:
    delete_and_verify(f"/system/role/{rid}", "/system/role/list?pageSize=10", "roleName", RNAME, tok, "role")

# 5. 无 token 访问受保护接口 -> 期望 401
s, d = req("GET", "/system/user/list")
show("no-token(user.list)", s, d, 200)

print("DONE")
