package com.web.result;

/**
 * @description: API 统一返回状态码
 * @author: raven
 * @create: 2020-04-10 17:57
 **/
public enum ResultCode {
    Success(200,"success"),
    Fail(400,"error"),
    /* 成功状态码 */
    loginSuccess(200, "登陆成功"),
    /* 失败状态码 */
    loginFail(40001, "账号或密码错误"),
    updateUserInfoFail(40002,"修改失败"),
    updateUserInfoCurrPassError(40003,"当前的密码填写错误，修改失败！"),
    updateUserInfoSuccess(200,"修改成功"),
    registerSuccess(200, "注册成功"),
    registerError(40004, "注册失败！"),
    registerUserNameExist(40005, "当前用户名已经被注册了，注册失败！"),
    registerUserPasswordNotEqual(40006, "两次密码不匹配，注册失败！"),
    notLogin(40007, "您尚未登录，无法进行操作哦~"),
    getDeviceList(200,""),
    addDeviceSuccess(200,"设备添加成功"),
    addDeviceNoExist(50001,"当前设备id不存在或者设备密码错误，操作失败！"),
    addDeviceExist(50002,"当前设备已经在您的设备列表里了，请不要重复添加！"),
    addDeviceFail(50003,"设备添加失败"),
    deleteDeviceFail(50004,"设备密码错误，删除失败了."),
    deleteDeviceSuccess(200,"设备已从您的账户成功移除"),
    updateDeviceSuccess(200,"修改成功"),
    updateDeviceFail(50005,"修改失败"),
    updateDeviceFailNoExist(50006,"修改失败，旧设备不存在于您的账户。"),
    updateDeviceFailOldDeviceError(50007,"旧设备信息错误或者不存在，操作失败！"),
    updateDeviceFailNewDeviceError(50006,"新设备信息错误或者不存在，操作失败！");
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

