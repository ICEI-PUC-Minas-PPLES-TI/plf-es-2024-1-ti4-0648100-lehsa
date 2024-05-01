/** @type {import('next').NextConfig} */
export const images = {
    remotePatterns: [
        {
            protocol: 'http',
            hostname: 'localhost',
            port: '8080',
            pathname: '/item/img/**',
        },
    ],
};
