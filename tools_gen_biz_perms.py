# 生成业务模块菜单按钮权限 SQL，并授权超级管理员
modules = [
    (201, "visitAppointment",   "visit:appointment"),
    (202, "visitRegistration",  "visit:registration"),
    (301, "checkinAdmission",   "checkin:manage"),
    (302, "checkinCheckout",    "checkin:checkout"),
    (401, "residentContract",   "resident:contract"),
    (402, "residentBed",        "resident:bed"),
    (403, "residentRoom",        "resident:room"),
    (404, "residentLeave",      "resident:leave"),
    (501, "serviceTask",        "service:task"),
    (502, "servicePlan",        "service:plan"),
    (601, "financeBill",        "finance:bill"),
    (602, "financePrestore",    "finance:prestored"),
    (701, "orderInfo",           "order:list"),
    (702, "orderRefund",         "order:refund"),
    (801, "customerInfo",        "customer:list"),
    (1001, "messagePush",        "message:push"),
    (1101, "iotDevice",          "iot:device"),
    (1102, "iotAlarmRule",       "iot:alarm"),
]

actions = [("query", "查询"), ("add", "新增"), ("edit", "修改"), ("remove", "删除")]

lines = []
lines.append("USE zzyl;")
lines.append("SET NAMES utf8mb4;")
cids = ", ".join(str(m[0]) for m in modules)
lines.append(f"UPDATE sys_menu SET component = 'business/index' WHERE id IN ({cids});")

fid = 2001
for cid, key, prefix in modules:
    sort = 1
    for act, label in actions:
        mname = label
        perms = f"{prefix}:{act}"
        lines.append(
            f"INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort) "
            f"VALUES ({fid}, {cid}, '{mname}', 'F', '', '', '{perms}', '', {sort});"
        )
        fid += 1
        sort += 1

lines.append("INSERT INTO sys_role_menu (role_id, menu_id) SELECT 1, id FROM sys_menu WHERE id BETWEEN 2001 AND %d AND menu_type='F';" % (fid - 1))

print("\n".join(lines))
