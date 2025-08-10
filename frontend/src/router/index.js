import {createRouter, createWebHistory} from "vue-router";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            name: 'LoginIndex',
            component: () => import('@/views/login/LoginIndex.vue')
        },
        {
            path: '/home',
            name: 'LayoutIndex',
            redirect: '/home/mySpace',
            component: () => import('@/views/home/HomeIndex.vue'),
            children:
                [
                    {
                        // no / needed
                        path: 'mySpace',
                        name: 'MySpace',
                        component: () => import('@/views/mySpace/MySpaceIndex.vue'),
                        meta: {title: 'MySpace'}
                    },
                    {
                        path: 'recycleBin',
                        name: 'RecycleBin',
                        component: () => import('@/views/recycleBin/RecycleBinIndex.vue'),
                        meta: {title: 'Account'}
                    },
                    {
                        path: 'mine',
                        name: 'Mine',
                        component: () => import('@/views/mine/MineIndex.vue'),
                        meta: {title: 'Profile'}
                    }
                ]
        }
    ]
})


// eslint-disable-next-line no-unused-vars
router.beforeEach(async (to, from, next) => {
    setToken(localStorage.getItem('token'))
    setUsername(localStorage.getItem('username'))
    const token = getToken()
    if (to.path === '/login') {
        next()
    }
    if (isNotEmpty(token)) {
        next()
    } else {
        next('/login')
    }
})

export default router