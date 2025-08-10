const modules = import.meta.glob('./modules/*.js')
const api = {}

async function loadModules() {
    for (const path in modules) {
        const mod = await modules[path]()
        const name = path.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')
        api[name] = mod.default
    }
}
await loadModules()

export default api