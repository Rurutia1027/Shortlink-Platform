import axios from 'axios'
import router from '@/router'
import {getToken, getUsername} from "@/core/auth.js"
import {isNotEmpty} from '@/utils/plugins.js'

const baseURL = '/api/short-link/admin/v1'
const http = axios.create({
    baseURL: '/api' + baseURL,
    // improve for debugging 15000
    timeout: 5000000
})

http.interceptors.request.use(
    (config) => {
        config.headers.Token = isNotEmpty(getToken()) ? getToken() : ''
        config.headers.Username = isNotEmpty(getUsername()) ? getUsername() : ''
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

http.interceptors.response.use(
    (res) => {
        if (res.data.code === 'B000200' || res.data.code === 'A000200') {
            localStorage.removeItem('token')
        }

        if (res.status == 0 || res.status == 200) {
            return Promise.resolve(res)
        }

        return Promise.reject(res)
    },
    (err) => {
        if (err.response.status === 401) {
            localStorage.removeItem('token')
            router.push('/login')
        }
        return Promise.reject(err)
    }
)

export default http