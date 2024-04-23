import React, { ReactNode } from 'react'
import TopMenu from '@/components/topMenu'

interface Props {
    children: ReactNode
}

const adminLayout = ({ children }: Props) => {
  return (
    
        <div className='p-6  space-y-8'>
          <TopMenu title='Página do Usuário'/>
          { children }
          </div>
    
  )
}
    
export default adminLayout