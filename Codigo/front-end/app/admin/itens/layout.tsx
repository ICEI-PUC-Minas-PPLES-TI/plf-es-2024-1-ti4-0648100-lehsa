import TopMenu from '@/components/topMenu'
import React, { ReactNode } from 'react'

interface Props {
    children: ReactNode
}

const itensLayout = ({ children }: Props) => {
  return (
    <div className='flex flex-col space-y-6 h-full'>
        <TopMenu title='Itens'/>
        <div className='grow'>{ children }</div>
    </div>
  )
}

export default itensLayout