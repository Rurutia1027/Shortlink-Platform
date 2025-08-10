import http from '../axios'

export default {
    // query grouping short link
    queryGroup(data) {
        return http({
            url: '/group',
            method: 'get',
            params: data
        })
    },

    // add new group
    addGroup(data) {
        return http({
            url: '/group',
            method: 'post',
            params: data
        })
    },

    // edit group
    editGroup(data) {
        return http({
            url: '/group',
            method: 'put',
            params: data
        })
    },

    // delete short link group
    deleteGroup(data) {
        return http({
            url: '/group',
            method: 'delete',
            params: data
        })
    },


    // sort group
    sortGroup(data) {
        return http({
            url: '/group/sort',
            method: 'post',
            params: data
        })
    }
}