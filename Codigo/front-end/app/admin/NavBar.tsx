'use client'

import React from 'react'
import Image from 'next/image'
import Link from 'next/link'
import { usePathname } from 'next/navigation'

const NavBar = () => {
    const pathname = usePathname()
    const isActiveItem = pathname.startsWith('/admin/itens');
    const isActiveAgenda = pathname.startsWith('/admin/agendamento');
    const isActiveEmprest = pathname.startsWith('/admin/emprestimo');
    return (
        <aside className='bg-white h-screen space-y-10 w-60 py-6'>
            <Image src='/images/logo.jpg' width={150} height={0} alt='logo' className='m-auto'></Image>
            <div className='flex flex-col gap-2 pl-5'>
                <Link href='/admin/itens' className={`pl-6 py-2 rounded-l-full ${isActiveItem ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Itens</Link>
                <Link href='/admin/agendamentos' className={`pl-6 py-2 rounded-l-full ${isActiveAgenda ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Agendamentos</Link>
                <Link href='/admin/emprestimos' className={`pl-6 py-2 rounded-l-full ${isActiveEmprest ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Emprestimos</Link>
                <Link href='/admin/usuarios' className={`pl-6 py-2 rounded-l-full ${pathname === '/admin/usuarios' ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Usuários</Link>
                <Link href='/admin/professores' className={`pl-6 py-2 rounded-l-full ${pathname === '/admin/professores' ? 'bg-primary text-white' : 'hover:bg-slate-100'}`}>Professores</Link>
            </div>
        </aside>
    )
}

export default NavBar