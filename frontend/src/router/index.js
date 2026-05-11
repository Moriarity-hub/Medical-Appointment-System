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
    {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { requiresAuth: true },
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
    if (to.meta.requiresAuth && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router