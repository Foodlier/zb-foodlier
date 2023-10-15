const { createProxyMiddleware } = require('http-proxy-middleware')

module.exports = function (app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://ec2-15-165-55-217.ap-northeast-2.compute.amazonaws.com',
      changeOrigin: true,
      secure: false,
      rewrite: path => path.replace(/^\/api/, ''),
    })
  )
}
