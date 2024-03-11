import React, { ReactNode } from 'react'
import Image from 'next/image'
import Link from 'next/link'

interface Props {
    children: ReactNode
}

const adminLayout = ({ children }: Props) => {
  return (
    <div className='flex'>
        <aside className='bg-white h-screen p-6 space-y-10'>
            <Image src='/logo.jpg' width={150} height={0} alt='logo'></Image>
            <div className='flex flex-col gap-2'>
                <Link href='/admin'>Dashboard</Link>
                <Link href='/admin/equipamentos'>Equipamentos</Link>
            </div>
        </aside>
        <div className='p-6'>{ children }</div>
    </div>
  )
}
    
export default adminLayout