import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: () => import('../views/ArticleDetail.vue')
  },
  {
    path: '/notes',
    name: 'Notes',
    component: () => import('../views/Notes.vue')
  },
  {
    path: '/message',
    name: 'Message',
    component: () => import('../views/MessageBoard.vue')
  },
  {
    path: '/user',
    name: 'UserCenter',
    component: () => import('../views/UserCenter.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/my-articles',
    name: 'MyArticles',
    component: () => import('../views/MyArticles.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/article-edit/:id?',
    name: 'UserArticleEdit',
    component: () => import('../views/admin/ArticleEdit.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/admin/Dashboard.vue')
      },
      {
        path: 'articles',
        name: 'ArticleManage',
        component: () => import('../views/admin/ArticleManage.vue')
      },
      {
        path: 'article-edit/:id?',
        name: 'ArticleEdit',
        component: () => import('../views/admin/ArticleEdit.vue')
      },
      {
        path: 'article-audit',
        name: 'ArticleAudit',
        component: () => import('../views/admin/ArticleAudit.vue')
      },
      {
        path: 'notes',
        name: 'NoteManage',
        component: () => import('../views/admin/NoteManage.vue')
      },
      {
        path: 'categories',
        name: 'CategoryManage',
        component: () => import('../views/admin/CategoryManage.vue')
      },
      {
        path: 'tags',
        name: 'TagManage',
        component: () => import('../views/admin/TagManage.vue')
      },
      {
        path: 'messages',
        name: 'MessageManage',
        component: () => import('../views/admin/MessageManage.vue')
      },
      {
        path: 'comments',
        name: 'CommentManage',
        component: () => import('../views/admin/CommentManage.vue')
      },
      {
        path: 'files',
        name: 'FileManage',
        component: () => import('../views/admin/FileManage.vue')
      },
      {
        path: 'users',
        name: 'UserManage',
        component: () => import('../views/admin/UserManage.vue')
      },
      {
        path: 'carousels',
        name: 'CarouselManage',
        component: () => import('../views/admin/CarouselManage.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router


