module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  lintOnSave: false,
  productionSourceMap: false,
  devServer: {
    port: 9528,
    open: true,
    overlay: {
      warnings: false,
      errors: true
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
