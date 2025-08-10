import http from '../axios.js'

export default {
    // register
    addUser(data) {
        return http({
            url: '/user',
            method: 'post',
            data
        })
    },

    // edit info
    editUser(data) {
        return http({
            url: '/user',
            method: 'put',
            data
        })
    },

    // login
    login(data) {
        return http({
            url: '/user/login',
            method: 'post',
            data
        })
    },

    // logout
    logout(data) {
        return http({
            url: '/user/logout?token=' + data.token + '&username=' + data.username,
            method: 'delete'
        })
    },

    // validate username available
    isUsernameAvailable(data) {
        return http({
            url: '/user/is-username-available',
            method: 'get',
            params: data
        })
    },

    // find user profile via username
    queryUserInfo(data) {
        return http({
            url: '/actual/user/' + data,
            method: 'get'
        })
    }
}