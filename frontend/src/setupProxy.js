const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
    // Check if we're running in Docker (services available by name) or locally
    const isDocker = process.env.NODE_ENV === 'production' || process.env.DOCKER_ENV === 'true';

    const alarmTarget = isDocker ? 'http://alarm-service:8080' : 'http://localhost:8080';
    const customerTarget = isDocker ? 'http://customer-service:8080' : 'http://localhost:8084';
    const ticketTarget = isDocker ? 'http://ticket-service:8080' : 'http://localhost:8082';

    // Alarm Service
    app.use(
        '/api/alarms',
        createProxyMiddleware({
            target: alarmTarget,
            changeOrigin: true,
            logLevel: 'debug'
        })
    );

    // Customer Service
    app.use(
        '/api/customers',
        createProxyMiddleware({
            target: customerTarget,
            changeOrigin: true,
            logLevel: 'debug'
        })
    );

    // Ticket Service
    app.use(
        '/api/tickets',
        createProxyMiddleware({
            target: ticketTarget,
            changeOrigin: true,
            logLevel: 'debug'
        })
    );
};
