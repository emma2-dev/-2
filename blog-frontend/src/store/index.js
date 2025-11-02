import Vue from 'vue'
import Vuex from 'vuex'
import axios from '../utils/request'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: null,
    token: localStorage.getItem('token') || ''
  },
  
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    
    SET_USER(state, user) {
      state.user = user
    },
    
    LOGOUT(state) {
      state.token = ''
      state.user = null
      localStorage.removeItem('token')
    }
  },
  
  actions: {
    // 登录
    login({ commit }, loginForm) {
      return new Promise((resolve, reject) => {
        axios.post('/auth/login', loginForm).then(res => {
          commit('SET_TOKEN', res.data)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    
    // 注册
    register({ commit }, registerForm) {
      return new Promise((resolve, reject) => {
        axios.post('/auth/register', registerForm).then(() => {
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    
    // 获取用户信息
    getUserInfo({ commit }) {
      return new Promise((resolve, reject) => {
        axios.get('/user/info').then(res => {
          commit('SET_USER', res.data)
          resolve(res.data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    
    // 退出登录
    logout({ commit }) {
      commit('LOGOUT')
    }
  }
})


