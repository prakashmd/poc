import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
   
    stackList: []
  },
  mutations: {
   
    update(state, stacks) {
     
      alert("store update"+JSON.stringify(stacks));
       state.stackList.push(stacks);
      
    }
  },
  getters: {
    name: state => state.name,
    category: state => state.category,
    licenceType: state => state.licenceType,
    stackList:state => state.stackList
  },
  actions: {
    addstack (context, payload) {
      alert("addstack" +JSON.stringify(payload));
      context.commit('update', payload.response.data)
    },
    deletestack (context, payload) {
      alert("deletestack" +JSON.stringify(payload));
      context.commit('update', payload.response.data)
    }
    
  }
})
