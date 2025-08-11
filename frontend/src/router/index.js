import {createRouter, createWebHistory} from "vue-router";
import {getToken, setToken, setUsername} from '@/core/auth.js'
import {isNotEmpty} from '@/utils/plugins'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/login',
            name: 'LoginIndex',
            component: () => import('@/views/login/LoginIndex.vue')
        },
        {
            path: '/home',
            name: 'LayoutIndex',
            redirect: '/home/space',
            component: () => import('@/views/home/HomeIndex.vue'),
            children:
                [
                    {
                        // no / needed
                        path: 'space',
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
                        path: 'account',
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
    console.log(localStorage.getItem('token'))
    setUsername(localStorage.getItem('username'))
    console.log(localStorage.getItem('username'))
    const token = getToken()
    if (to.path === '/login') {
        return next()
    }
    if (isNotEmpty(token)) {
        return next()
    } else {
        return next('/login')
    }
})

export default router