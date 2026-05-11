import axios from 'axios'

const baseURL = '/api'

const instance = axios.create({
    baseURL,
    timeout: 10000
})

// 请求拦截器
instance.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
instance.interceptors.response.use(
    response => {
        return response
    },
    error => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('userId')
            localStorage.removeItem('userName')
            localStorage.removeItem('phone')
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

// 认证相关API
export const authApi = {
    login: (data) => instance.post('/auth/login', data),
    register: (data) => instance.post('/auth/register', data),
    current: () => instance.get('/auth/current')
}

// 患者相关API
export const patientApi = {
    getAll: () => instance.get('/patients'),
    getById: (id) => instance.get(`/patients/${id}`),
    create: (data) => instance.post('/patients', data),
    update: (id, data) => instance.put(`/patients/${id}`, data),
    delete: (id) => instance.delete(`/patients/${id}`)
}

// 医生相关API
export const doctorApi = {
    getAll: () => instance.get('/doctors'),
    getById: (id) => instance.get(`/doctors/${id}`),
    getByDepartment: (deptId) => instance.get(`/doctors/department/${deptId}`),
    create: (data) => instance.post('/doctors', data),
    update: (id, data) => instance.put(`/doctors/${id}`, data),
    delete: (id) => instance.delete(`/doctors/${id}`)
}

// 科室相关API
export const departmentApi = {
    getAll: () => instance.get('/departments'),
    getById: (id) => instance.get(`/departments/${id}`),
    create: (data) => instance.post('/departments', data),
    update: (id, data) => instance.put(`/departments/${id}`, data),
    delete: (id) => instance.delete(`/departments/${id}`)
}

// 预约相关API
export const appointmentApi = {
    getAll: () => instance.get('/appointments'),
    getById: (id) => instance.get(`/appointments/${id}`),
    getByPatient: (patientId) => instance.get(`/appointments/patient/${patientId}`),
    getByDoctor: (doctorId) => instance.get(`/appointments/doctor/${doctorId}`),
    create: (data) => instance.post('/appointments', data),
    updateStatus: (id, status) => instance.put(`/appointments/${id}/status`, { status }),
    cancel: (id) => instance.put(`/appointments/${id}/cancel`)
}


export default instance