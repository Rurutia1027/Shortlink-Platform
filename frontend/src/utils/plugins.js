/**
 * Replace the __require() method inside third-party plugins
 * @param {*} code
 * @param {*} id
 * @returns
 */
// eslint-disable-next-line camelcase
import {nextTick} from 'vue'

export const transformRequireDynamic = function (code, id) {
    if (!/\/node_modules\//g.test(id)) return code
    const requireRegex = /_{2}require*\(\s*(["'].*["'])\s*\)/g
    const IMPORT_STRING_PREFIX = '__require_for_vite'
    const requireMatches = code.matchAll(requireRegex)
    let importsString = ''
    let packageName = ''
    let replaced = false
    for (const item of requireMatches) {
        if (!isString(item[1])) {
            console.warn(`Dynamic import not supported, file: ${id}`)
            continue
        }
        replaced = true
        packageName = `${IMPORT_STRING_PREFIX}_${randomString(6)}`
        importsString += `import ${packageName} from ${item[1]};\n`
        code = code.replace(item[0], `${packageName}`)
    }
    if (replaced) {
        code = importsString + code
    }
    return code
}

export const resourceUserInfo = () => {
    return localStorage.getItem('resourceUserInfo')
        ? JSON.parse(localStorage.getItem('resourceUserInfo'))
        : {}
}

/**
 * @param {Required, Number} length
 * @returns Random hash string
 */
function randomString(length) {
    const code = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890'
    let result = ''
    for (let index = 0; index < length; index++) {
        result += code[Math.floor(Math.random() * code.length)]
    }
    return result
}

function isString(text) {
    try {
        // eslint-disable-next-line no-eval
        return typeof eval(text) === 'string'
    } catch (err) {
        return false
    }
}

export const hidePhone = (phone) => {
    let reg = /^(\d{3})\d{4}(\d{4})$/
    if (phone) {
        return phone.replace(reg, '$1****$2')
    }
    return ''
}


export const setImg = (str) => {
    if (str?.includes('/src/assets/images')) {
        return new URL(str, import.meta.url).href
    }
    return str
}

export function isMax(file) {
    return Math.ceil(file.size / 1024 / 1024) < 3072 // less than 3GB
}

export function isMin(file) {
    return Math.floor(file.size / 1024 / 1024) > 5 // greater than 5MB
}

// Allowed video extensions
export function acceptVideo() {
    return '.MP4'
}

export function isValid(file) {
    let acceptArr = [
        'MP4'
        /*  'TS',
        'MOV',
        'MXF',
        'FLV',
        'MPG',
        'WMV',
        'AVI',
        'F4V',
        'M4V',
        'HLS',
        'MPEG' */
    ]
    if (file.raw?.type) {
        let type = file.raw.type.split('/')[1].toUpperCase()
        return acceptArr.includes(type)
    }
    if (file.type) {
        let type = file.type.split('/')[1].toUpperCase()
        return acceptArr.includes(type)
    }
    return true
}

export const computeSize = (fileSize) => {
    if (!fileSize) return 0
    let size = ''
    if (fileSize >= 1024 * 1024) {
        size = `${(fileSize / (1024 * 1024)).toFixed(2)} MB`
    } else if (fileSize >= 1024) {
        size = `${(fileSize / 1024).toFixed(2)} KB`
    } else {
        size = `${fileSize.toFixed(2)}B`
    }
    return size
}

// Convert tree structure to a flat array
export const flattenTree = (tree, childrenKey = 'childrenTag') => {
    return tree.reduce((arr, item) => {
        let children = item[childrenKey]
        arr.push(item)
        arr = arr.concat(Array.isArray(children) ? flattenTree(children, childrenKey) : [])
        return arr
    }, [])
}

export const isNotEmpty = (value) => {
    // eslint-disable-next-line eqeqeq
    return value && value != 'undefined' && value != 'null'
}

export function moveToErr() {
    nextTick(() => {
        let isError = document.getElementsByClassName('is-error')
        if (isError.length) {
            isError[0].scrollIntoView({
                block: 'center',
                behavior: 'smooth'
            })
        }
    })
}

// Return the last route segment from a path
export function getLasteRoute(fullpath) {
    const arr = fullpath.split('/')
    const length = arr.length
    return '/' + arr[length - 1]
}

// Get the current date in format YYYY-MM-DD
export function getNowFormatDate() {
    let date = new Date(),
        year = date.getFullYear(),
        month = date.getMonth() + 1,
        strDate = date.getDate()
    if (month < 10) month = `0${month}`
    if (strDate < 10) strDate = `0${strDate}`
    return `${year}-${month}-${strDate}`
}

// Get the date one week from today in format YYYY-MM-DD
export function getNextWeekFormatDate() {
    let nextWeekDate = new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000),
        year = nextWeekDate.getFullYear(),
        month = nextWeekDate.getMonth() + 1,
        strDate = nextWeekDate.getDate()
    if (month < 10) month = `0${month}`
    if (strDate < 10) strDate = `0${strDate}`
    return `${year}-${month}-${strDate}`
}