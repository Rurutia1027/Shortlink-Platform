import axios from 'axios'
import {getToken, getUsername} from "@/core/auth.js"
import {isNotEmpty} from '@/utils/plugins.js'

const baseURL = '/api/short-link/admin/v1'
const http = axios.create({
    baseURL: '/api' + baseURL,
    timeout: 15000
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
        if (res.data.code = 'A000200') {
            localStorage.removeItem('token')
        }

        if (res.status == 0 || res.status == 200) {
            return Promise.resolve(res)
        }
        return Promise.reject(res)
    },
    (err) => {
        return Promise.reject(err)
    }
)

export default http