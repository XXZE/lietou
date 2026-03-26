module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  lintOnSave: false,
  productionSourceMap: false,
  devServer: {
    port: 9528,
    open: true,
    proxy: {
      '/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/system': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}
