'use client'

import React from 'react'
import Image from 'next/image'
import Link from 'next/link'
import { usePathname } from 'next/navigation'

const NavBar = () => {
    const pathname = usePathname()
    const isActive = pathname.startsWith('/admin/itens');
    return (
        <aside className='bg-white h-screen space-y-10 w-60 py-6'>
            <Image src='/images/logo.jpg' width={150} height={0} alt='logo' className='m-auto'></Image>
            <div className='flex flex-col gap-2 pl-5'>
                <Link href='/admin' className={`pl-6 py-2 rounded-l-full  ${pathname === '/admin' ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Dashboard</Link>
                <Link href='/admin/itens' className={`pl-6 py-2 rounded-l-full ${isActive ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Itens</Link>
                <Link href='/admin/agendamentos' className={`pl-6 py-2 rounded-l-full ${pathname === '/admin/agendamentos' ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Agendamentos</Link>
                <Link href='/admin/usuarios' className={`pl-6 py-2 rounded-l-full ${pathname === '/admin/usuarios' ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Usuários</Link>
            </div>
        </aside>
    )
}

export default NavBar