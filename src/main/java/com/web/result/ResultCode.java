package com.web.result;

/**
 * @description: API 统一返回状态码
 * @author: raven
 * @create: 2020-04-10 17:57
 **/
public enum ResultCode {

    /* 成功状态码 */
    loginSuccess(200, "登陆成功"),
    /* 失败状态码 */
    loginFail(-1, "账号或密码错误"),
    notLogin(-1, "您尚未登录，无法进行操作哦~"),
    getDeviceList(200,""),
    addDeviceSuccess(200,"设备添加成功"),
    addDeviceFail(-1,"设备添加失败"),
    deleteDeviceSuccess(200,"删除成功"),
    updateDeviceSuccess(200,"修改成功"),
    updateDeviceFail(-1,"修改失败"),

    ;
    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

}

