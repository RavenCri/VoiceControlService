
import service from "@/common/base.js"
export const login = params => service.post('account/login',params);
export const register = params => service.post('account/register',params);
export const userInfo = params => service.get('account/userInfo',params);
export const updateUserInfo = params => service.post('account/update',params);
export const userList = () => service.get('account/userList');
export const userForbidden = params => service.post('account/close',params);
export const userUnForbidder = params => service.post('account/open',params);



export const robotreplay = params => service.get('roobot/replay',params);

export const getUserRole = () => service.get('/permission/user');
