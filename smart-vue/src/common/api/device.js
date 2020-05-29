import service from "@/common/base.js"

export const addDevice_admin = params=>service.post('devicePlus/addDevice',params)
export const deviceList_admin = params=>service.get('devicePlus/list',params)
export const deleteDevice_admin = params=>service.post('devicePlus/deleteDevice',params)



export const addDevice_user = params=>service.post('device/add',params)
export const deviceList_user = params=>service.get('device/list',params)
export const deleteDevice_user = params=>service.post('device/delete',params)
export const updateDevice_user = params=>service.post('device/update',params)