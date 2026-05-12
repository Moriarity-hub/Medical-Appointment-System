// D:\maven_project\mas\frontend\src\router\index.js
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/index.vue')
    },
    // 患者端
    {
        path: '/patient',
        name: 'PatientHome',
        component: () => import('@/views/patient/index.vue'),
        meta: { requiresAuth: true, roles: ['PATIENT'] },
        children: [
            {
                path: '',
                name: 'PatientDashboard',
                component: () => import('@/views/patient/dashboard.vue')
            },
            {
                path: 'appointments',
                name: 'PatientAppointments',
                component: () => import('@/views/patient/appointments.vue')
            },
            {
                path: 'appointments/add',
                name: 'PatientAddAppointment',
                component: () => import('@/views/patient/add-appointment.vue')
            },
            {
                path: 'profile',
                name: 'PatientProfile',
                component: () => import('@/views/patient/profile.vue')
            }
        ]
    },
    // 医生端
    {
        path: '/doctor',
        name: 'DoctorHome',
        component: () => import('@/views/doctor/index.vue'),
        meta: { requiresAuth: true, roles: ['DOCTOR'] },
        children: [
            {
                path: '',
                name: 'DoctorDashboard',
                component: () => import('@/views/doctor/dashboard.vue')
            },
            {
                path: 'appointments',
                name: 'DoctorAppointments',
                component: () => import('@/views/doctor/appointments.vue')
            },
            {
                path: 'profile',
                name: 'DoctorProfile',
                component: () => import('@/views/doctor/profile.vue')
            }
        ]
    },
    // 管理员端（使用现有的home目录）
    {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN'] },
        children: [
            {
                path: '',
                name: 'Dashboard',
                component: () => import('@/views/home/dashboard.vue')
            },
            {
                path: 'patients',
                name: 'PatientList',
                component: () => import('@/views/home/patient/index.vue')
            },
            {
                path: 'patients/add',
                name: 'AddPatient',
                component: () => import('@/views/home/patient/add.vue')
            },
            {
                path: 'patients/edit/:id',
                name: 'EditPatient',
                component: () => import('@/views/home/patient/add.vue')
            },
            {
                path: 'doctors',
                name: 'DoctorList',
                component: () => import('@/views/home/doctor/index.vue')
            },
            {
                path: 'doctors/add',
                name: 'AddDoctor',
                component: () => import('@/views/home/doctor/add.vue')
            },
            {
                path: 'doctors/edit/:id',
                name: 'EditDoctor',
                component: () => import('@/views/home/doctor/add.vue')
            },
            {
                path: 'departments',
                name: 'DepartmentList',
                component: () => import('@/views/home/department/index.vue')
            },
            {
                path: 'departments/add',
                name: 'AddDepartment',
                component: () => import('@/views/home/department/add.vue')
            },
            {
                path: 'departments/edit/:id',
                name: 'EditDepartment',
                component: () => import('@/views/home/department/add.vue')
            },
            {
                path: 'appointments',
                name: 'AppointmentList',
                component: () => import('@/views/home/appointment/index.vue')
            },
            {
                path: 'appointments/add',
                name: 'AddAppointment',
                component: () => import('@/views/home/appointment/add.vue')
            }
        ]
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('@/views/not-found/index.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    const role = localStorage.getItem('role')

    // 如果需要认证
    if (to.meta.requiresAuth) {
        if (!token) {
            next('/login')
            return
        }

        // 检查角色权限
        if (to.meta.roles && !to.meta.roles.includes(role)) {
            // 根据角色跳转到对应首页
            if (role === 'PATIENT') {
                next('/patient')
            } else if (role === 'DOCTOR') {
                next('/doctor')
            } else if (role === 'ADMIN') {
                next('/home')
            } else {
                next('/login')
            }
            return
        }

        next()
    } else {
        // 不需要认证的页面
        if (to.path === '/login' && token) {
            // 已登录，根据角色跳转
            if (role === 'PATIENT') {
                next('/patient')
            } else if (role === 'DOCTOR') {
                next('/doctor')
            } else if (role === 'ADMIN') {
                next('/home')
            } else {
                next()
            }
        } else {
            next()
        }
    }
})

export default router