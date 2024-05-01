import { NextRequest, NextResponse } from 'next/server';
import isJWTValid from './utils/isJwtValid';

export function middleware(request: NextRequest): NextResponse | undefined {
  const token = request.cookies.get('token')?.value;

  if (!token && !(request.nextUrl.pathname === "/" || request.nextUrl.pathname.startsWith('/cadastrar'))) {
    return NextResponse.redirect(new URL('/', request.url));
  }

  if (token) {
    const { isValid, role } = isJWTValid(token);

    if (!isValid) {
      const response = NextResponse.redirect(new URL('/', request.url));
      response.cookies.delete('token');
      return response;
    }

    if (role === 'admin' && !request.nextUrl.pathname.startsWith('/admin')) {
      return NextResponse.redirect(new URL('/admin', request.url));
    } else if (role === 'usuario' && !request.nextUrl.pathname.startsWith('/user')) {
      return NextResponse.redirect(new URL('/user', request.url));
    }
  }
}

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|images|icons|favicon.ico).*)']
}
