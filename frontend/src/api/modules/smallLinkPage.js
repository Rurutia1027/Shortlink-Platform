import http from '../axios'

export default {
    queryPage(data) {
        return http({
            url: '/page',
            metohd: 'get',
            params: data
        })
    },

    addSmallLink(data) {
        return http({
            url: '/create',
            method: 'post',
            params: data
        })
    },

    editSmallLink(data) {
        return http({
            url: '/update',
            method: 'post',
            params: data
        })
    },

    // query title via short link
    queryTitle(data) {
        return http({
            url: '/title',
            method: 'get',
            params: data
        })
    },

    // move enabled short link to recycle-bin
    toRecycleBin(data) {
        return http({
            url: '/recycle-bin/page',
            method: 'get',
            params: data
        })
    },

    // query short link(s) from recycle-bin
    queryRecycleBin(data) {
        return http({
            url: '/recycle-bin/page',
            method: 'get',
            params: data
        })
    },

    // recover recycled short link
    recoverLink(data) {
        return http({
            url: '/recycle-bin/recover',
            method: 'post',
            params: data
        })
    },

    // remove link
    removeLink(data) {
        return http({
            url: '/recycle-bin/remove',
            method: 'post',
            params: data
        })
    },

    // query link monitor metric stats
    queryLinkStats(data) {
        return http({
            url: '/stats',
            method: 'get',
            params: data
        })
    }
}