import TopMenu from '@/components/topMenu'
import React, { ReactNode } from 'react'

interface Props {
    children: ReactNode
}

const agendamentosLayout = ({ children }: Props) => {
    return (
        <div className='flex flex-col space-y-6 h-full'>
            <TopMenu title='Agendamentos'/>
            <div className='grow'>{ children }</div>
        </div>
    )
}

export default agendamentosLayout