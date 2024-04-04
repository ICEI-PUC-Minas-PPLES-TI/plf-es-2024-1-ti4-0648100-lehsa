import type { NextRequest } from 'next/server'
 
export function middleware(request: NextRequest) {
  const token = request.cookies.get('token')?.value
 
  if (token && !request.nextUrl.pathname.startsWith('/admin')) {
    return Response.redirect(new URL('/admin', request.url))
  }
 
  if (!token && request.nextUrl.pathname != "/") {
    return Response.redirect(new URL('/', request.url))
  }
}
 
export const config = {
    matcher: ['/((?!api|.\..|_next/static|_next/image|favicon.ico|sw.js).)']
  }