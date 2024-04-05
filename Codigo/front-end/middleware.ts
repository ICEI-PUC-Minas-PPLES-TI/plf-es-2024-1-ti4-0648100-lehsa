import type { NextRequest } from 'next/server'
import { NextResponse } from 'next/server';
import isJWTValid from './utils/isJwtValid'

export function middleware(request: NextRequest) {
  const token = request.cookies.get('token')?.value

  // Se não houver token e a rota não for a página inicial, redireciona para /
  if (!token && !(request.nextUrl.pathname === "/" || request.nextUrl.pathname.startsWith('/cadastrar'))) {
    return NextResponse.redirect(new URL('/', request.url));
}


  if (token) {
    const { isValid, role } = isJWTValid(token);

    // Se o token não for válido, redireciona para a página de login ou inicial
    if (!isValid) {
      return NextResponse.redirect(new URL('/', request.url));
    }

    // Redireciona com base na role
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