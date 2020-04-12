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
    updateUserInfoFail(-1,"修改失败"),
    updateUserInfoCurrPassError(-1,"当前的密码填写错误，修改失败！"),
    updateUserInfoSuccess(200,"修改成功"),
    registerSuccess(200, "注册成功"),
    registerError(-1, "注册失败！"),
    registerUserNameExist(-1, "当前用户名已经被注册了，注册失败！"),
    notLogin(-1, "您尚未登录，无法进行操作哦~"),
    getDeviceList(200,""),
    addDeviceSuccess(200,"设备添加成功"),
    addDeviceNoExist(-1,"当前设备id不存在，操作失败！"),
    addDeviceExist(-1,"当前设备已经在您的设备列表里了，无法重复添加！"),
    addDeviceFail(-1,"设备添加失败"),
    deleteDeviceFail(-1,"设备删除失败，错误码-1，请联系客服"),
    deleteDeviceSuccess(200,"设备已从您的账户成功移除"),
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

