module.exports = {
  devServer: {
    port: 8081,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '' // 去掉 /api 前缀
        }
      }
    }
  },
  lintOnSave: false
}


