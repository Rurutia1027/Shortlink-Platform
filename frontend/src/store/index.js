import {createStore} from 'vuex'

// create a new instance of Store
const store = createStore({
    state() {
        return {
            domain: 'localhost'
        }
    }
})

export default store
