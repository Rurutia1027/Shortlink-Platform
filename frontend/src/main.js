import {createApp} from 'vue'
import App from './App.vue'
// import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// import API from '@/api/index'

const app = createApp(App)

// global mounted API
// app.config.globalProperties.$API = API

// close all Vue warning info
app.config.warnHandler = () => null

// app.use(router) // comment router for temporary

app.use(ElementPlus)

// register global ElementPlus icons
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')